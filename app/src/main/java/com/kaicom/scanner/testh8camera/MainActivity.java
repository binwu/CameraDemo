package com.kaicom.scanner.testh8camera;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends Activity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceView sv_camera_preview;
    private SurfaceHolder surfaceHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCamera();

    }

    private void initCamera() {
        sv_camera_preview = findViewById(R.id.sv_camera_preview);
        mCamera = Camera.open(0);
        Camera.Parameters mParameters = mCamera.getParameters();

//        Camera.Size size = mCamera.new Size(640,480);//getFitSize(sizes);
//        if (size != null) {// 不为空，设置合适的，，否则用默认的
//            mParameters.setPictureSize(size.width, size.height);
//        }
        List<Camera.Size> sizes = mParameters.getSupportedPreviewSizes();
        Camera.Size size = mCamera.new Size(0, 0);
        for (Camera.Size s : sizes) {
            Log.i("wubin ", "camera width:" + s.width + "camera height: " + s.height);
            if ((s.width * s.height) > (size.width * size.height)) {
                size = s;
            }
        }
        List<String> focusModes = mParameters.getSupportedFocusModes();
        for(String s : focusModes){
            Log.i("wubin ", "focus mode " + s);
        }

        Log.i("wubin ", "final select size " + size.width + "   " + size.height);

        mParameters.setPreviewSize(size.width, size.height);


        mParameters.setJpegQuality(60);// 设置照片质量
        mParameters.setPictureFormat(PixelFormat.JPEG);
        mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        if (false) {//debug
            // LiuY, 2014-07-30, 拍照闪光灯
            /*=========================修改开始=============================*/
            //parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            /*=========================修改结束=============================*/
        } else {
            // LiuY, 2014-07-30, 拍照闪光灯
            /*=========================修改开始=============================*/
            //parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
//            mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            /*=========================修改结束=============================*/
        }
        mCamera.setParameters(mParameters);


        surfaceHolder = sv_camera_preview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera.startPreview();
        Log.i("wubin", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("wubin", "surfaceChanged");
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.i("wubin ", "camera set preview error");
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        Log.i("wubin", "surfaceDestroyed");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mCamera.startPreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCamera.stopPreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}
