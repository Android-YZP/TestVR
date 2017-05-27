package com.example.m1762.testvr;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import static android.graphics.BitmapFactory.decodeResource;
import static com.tencent.rtmp.TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION;

public class HomeActivity extends AppCompatActivity {

    private TXLivePushConfig mLivePushConfig;
    private TXCloudVideoView mCaptureView;
    private TXLivePusher mLivePusher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        int[] sdkver = TXLivePusher.getSDKVersion();
        if (sdkver != null && sdkver.length >= 3) {
            Log.e("rtmpsdk", "rtmp sdk version is:" + sdkver[0] + "." + sdkver[1] + "." + sdkver[2]);
        }
        //step 2: 创建Pusher对象
        TXLivePusher mLivePusher = new TXLivePusher(HomeActivity.this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setConfig(mLivePushConfig);
        String rtmpUrl = "rtmp://9250.livepush.myqcloud.com/live/9250_1111112111?bizid=9250&txSecret=d711198c514ede5ee15808452cc829e1&txTime=5929A27F";
        mLivePusher.startPusher(rtmpUrl);

        TXCloudVideoView mCaptureView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLivePusher.startCameraPreview(mCaptureView);

        mLivePusher.setVideoQuality(VIDEO_QUALITY_HIGH_DEFINITION);//设定清晰度
        mLivePusher.setBeautyFilter(7, 3);//配置美颜

        //用图片来设置滤镜
        Bitmap bmp = null;
        bmp = decodeResource(getResources(), R.drawable.huaijiu);
        if (mLivePusher != null) {
            mLivePusher.setFilter(bmp);
        }





    }


}
