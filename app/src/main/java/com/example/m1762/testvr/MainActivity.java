package com.example.m1762.testvr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final float FLING_MIN_DISTANCE = 10;
    /**
     * Actual panorama widget.
     **/
    private VrPanoramaView panoWidgetView;
    /**
     * Arbitrary variable to track load status. In this example, this variable should only be accessed
     * on the UI thread. In a real app, this variable would be code that performs some UI actions when
     * the panorama is fully loaded.
     */
    public boolean loadImageSuccessful;
    /**
     * Tracks the file to be loaded across the lifetime of this app.
     **/
    private String fileUri = "aaaa.png";
    /**
     * Configuration information for the panorama.
     **/
    private VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();
    private ImageLoaderTask backgroundImageLoaderTask;
    private VerticalViewPager verticalViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPanorama();
        initViewPager();
    }

    private void initViewPager() {
        verticalViewPager = (VerticalViewPager) findViewById(R.id.view_pager);
        verticalViewPager.setAdapter(new VertViewAdapter(getSupportFragmentManager(), "nihaodhsadiasdoiasodiasofefe fe fewfwe few few few fwe few few few few fefewf ew few few few few few fwe few few few few fedhaosihdoiasoi"));
        verticalViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {//停止状态---显示图片
                    initPanorama();

                } else {//其他状态---不显示图片
                    panoWidgetView.setVisibility(View.INVISIBLE);
                }
            }
        });
        //监测左右滑动事件
        verticalViewPager.setOnMoveListener(new VerticalViewPager.OnMoveListener() {
            @Override
            public void onMove(boolean isRight) {
                if (isRight) {
                    Log.e("滑动333", "向右滑动");
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    Log.e("滑动333", "向左滑动");
                }
            }
        });
    }


    /**
     * 初始化全景图播放器
     */
    private void initPanorama() {
        loadImageSuccessful = false;//初始化图片状态
        panoWidgetView = (VrPanoramaView) findViewById(R.id.pano_view);
        panoWidgetView.setEventListener(new ActivityEventListener());
        //影藏三個界面的按鈕
        panoWidgetView.setFullscreenButtonEnabled(false);
        panoWidgetView.setInfoButtonEnabled(false);
        panoWidgetView.setStereoModeButtonEnabled(false);
        panoWidgetView.setOnTouchListener(null);//禁用手势滑动
        panoOptions.inputType = VrPanoramaView.Options.TYPE_MONO;
        //加载背景图片
        Glide.with(this)
                .load("http://123.206.89.244/ty/10752.jpg")
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        BitmapDrawable bd = (BitmapDrawable) resource;
                        panoWidgetView.loadImageFromBitmap(bd.getBitmap(), panoOptions);
                    }
                });
    }

    @Override
    protected void onPause() {
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        panoWidgetView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        // Destroy the widget and free memory.
        panoWidgetView.shutdown();
        // The background task has a 5 second timeout so it can potentially stay alive for 5 seconds
        // after the activity is destroyed unless it is explicitly cancelled.
        if (backgroundImageLoaderTask != null) {
            backgroundImageLoaderTask.cancel(true);
        }
        super.onDestroy();
    }

    //异步任务
    class ImageLoaderTask extends AsyncTask<Pair<String, VrPanoramaView.Options>, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Pair<String, VrPanoramaView.Options>... fileInformation) {//真正写项目根据情况添加条件判断吧
            InputStream istr = null;
            try {

                istr = getAssets().open(fileInformation[0].first);//获取图片的输入流
            } catch (IOException e) {
                Log.e(TAG, "Could not decode default bitmap: " + e);
                return false;
            }
            Bitmap bitmap = BitmapFactory.decodeStream(istr);//创建bitmap
            Log.e("dflefseofjsdopfj", "Could not decode default bitmap: ");
//            panoWidgetView.loadImageFromBitmap(bitmap, fileInformation[0].second);//参数一为图片的bitmap，参数二为 VrPanoramaView 所需要的设置
            panoWidgetView.loadImageFromBitmap(ImageUtil.returnBitmap("http://123.206.89.244/ty/10752.jpg"), panoOptions);
            try {
                istr.close();//关闭InputStream
            } catch (IOException e) {
                Log.e(TAG, "Could not close input stream: " + e);
            }
            return true;
        }
    }


    /**
     * Listen to the important events from widget.
     */
    private class ActivityEventListener extends VrPanoramaEventListener {
        /**
         * Called by pano widget on the UI thread when it's done loading the image.
         */
        @Override
        public void onLoadSuccess() {
            loadImageSuccessful = true;
            Log.e("dflefseofjsdopfj", "Could not decode default bitmap: 2");
            panoWidgetView.setVisibility(View.VISIBLE);
        }

        /**
         * Called by pano widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            Log.e("dflefseofjsdopfj", "Could not decode default bitmap: 3");
            loadImageSuccessful = false;
        }
    }
}
