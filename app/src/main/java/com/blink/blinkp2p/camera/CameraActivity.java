package com.blink.blinkp2p.camera;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blink.blinkp2p.R;
import com.example.administrator.data_sdk.CommonIntent;
import com.example.administrator.data_sdk.ImageUtil.ImageTransformation;

import java.io.IOException;
import java.util.Calendar;

import com.blink.blinkp2p.Tool.System.Tools;

public class CameraActivity extends Activity implements SurfaceHolder.Callback {
    private static String TAG = "ScanBarZBarActivity";
    private Camera mCamera;
    //拍照、保存、继续拍  
//    private Button mButton, mButton1, mButton2;
    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;
    //    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback();
    private Bitmap bmp;
    private Calendar c;
    //    public native String getISBN(Bitmap bmp);
    private int width, height = 0;
    private LinearLayout cameraImage = null;
    private TextView mButton2, mButton1 = null;
    private LinearLayout cameraLinear = null;
    private Context context = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            /* 隐藏状态栏 */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
            /* 隐藏标题栏 */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera_main);
            /* SurfaceHolder设定 */
        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView);

        Log.e(TAG, "onCreate: 进来拍照界面");

        context = this;
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();


        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        c = Calendar.getInstance();
  
            /* Button初始化 */
        //TextView mButton = (TextView) findViewById(R.id.myButton);

        cameraImage = (LinearLayout) findViewById(R.id.cameraImage);
        mButton1 = (TextView) findViewById(R.id.myButton);
        mButton2 = (TextView) findViewById(R.id.myButton2);
        cameraLinear = (LinearLayout) findViewById(R.id.cameraLinear);
            /* 拍照Button的事件处理 */
        cameraImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (mCamera != null) {
                    //点击拍照
                    mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
                    cameraImage.setVisibility(View.GONE);
                    cameraLinear.setVisibility(View.VISIBLE);
                }
            }
        });
            /* Button的事件处理 */
        mButton1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.e(TAG, "onClick: /**********************************出来图片的方法 ***********/");
                if (bmp != null) {
                    ImageTransformation.savePictureByte(bmp, Tools.getPic(context));
                }
                CommonIntent.SetActivity(CameraActivity.this, 1);
                finish();
            }
        });
            /* 点击继续拍照 */
        mButton2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 继续拍照");
                bmp = null;
                initCamera(width, height);
                cameraImage.setVisibility(View.VISIBLE);
                cameraLinear.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
                    /* 打开相机， */
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
            mCamera.setDisplayOrientation(90);
            Log.i(TAG, "create camera---");
        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
            Toast.makeText(this, "app没有开启照相机权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w,
                               int h) {
            /* 相机初始化 */
        initCamera(w, h);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        stopCamera();
        mCamera.release();
        mCamera = null;

        finish();
    }

    /* 拍照的method */
    private void takePicture() {
        if (mCamera != null) {
            mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
        }
    }

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {

        }
    };

    private Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
        }
    };

    private Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
            try {
                            /* 取得Bitmap对象 */
                if (bmp == null)
                    bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
//                Toast.makeText(CameraActivity.this, "拍照完成", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

//    /* 告定义class AutoFocusCallback */
//    public final class AutoFocusCallback implements
//            android.hardware.Camera.AutoFocusCallback {
//        public void onAutoFocus(boolean focused, Camera camera) {
//                    /* 对到焦点拍照 */
//            camera.autoFocus(null);
//        }
//    }
//
//    ;

    /* 相机初始化的method */
    private void initCamera(int width, int height) {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();

                parameters.setPreviewSize(height, width);
                parameters.setPictureFormat(PixelFormat.JPEG);
                //设置默认输出的图片为4208*3120
                parameters.setPictureSize(height * 3, width * 3);
                mCamera.setParameters(parameters);
                            /* 开启预览画面 */
                mCamera.startPreview();
                //自动对焦
                mCamera.autoFocus(null);
            } catch (Exception e) {
                Toast.makeText(this, "app没有开启照相机权限", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCamera.autoFocus(null);
                break;

        }
        return super.onTouchEvent(event);
    }

    /* 停止相机的method */
    private void stopCamera() {
        if (mCamera != null) {
            try {
                            /* 停止预览 */
                mCamera.stopPreview();
            } catch (Exception e) {
                Toast.makeText(this, "app没有开启照相机权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}  