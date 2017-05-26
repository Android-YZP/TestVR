package com.example.m1762.testvr.vrplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.m1762.testvr.R;
import com.example.m1762.testvr.vrplayer.Definition;
import com.example.m1762.testvr.vrplayer.seekbar.DiscreteSeekBar;


/**
 * //TODO 参数过长的都改掉 改成builder类型
 * 对话框集合类
 * Created by snail on 16/4/19.
 */
public class DialogUtils {


    public static void showSelectFovDialog(final Activity activity, DiscreteSeekBar.OnProgressChangeListener progressChangeListener, final View.OnClickListener submitListener, final View.OnClickListener onCancelListener) {
        View view = LayoutInflater.from(activity).inflate(R.layout.select_fov_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity,R.style.SingleChoiceDialog).setView(view).create();
        final DiscreteSeekBar discreteSeekBar=(DiscreteSeekBar)view.findViewById(R.id.discrete);
        discreteSeekBar.setOnProgressChangeListener(progressChangeListener);
//        discreteSeekBar.setValue(uiutils.getPreferenceKeyIntValue(activity, Definition.KEY_FOV, PlayActivity.FOV_DEFAULT));///////////////////////////////////////////////
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int discreteSeekBarWidth=dm.widthPixels-activity.getResources().getDimensionPixelSize(R.dimen.dimen_330)
                -activity.getResources().getDimensionPixelSize(R.dimen.padding_16);

        int bestAngleWidth=discreteSeekBarWidth/7;

        TextView minAngle = (TextView) view.findViewById(R.id.best_min_angle);

        TextView maxAngle = (TextView) view.findViewById(R.id.best_max_angle);

        RelativeLayout.LayoutParams minAngleParams=(RelativeLayout.LayoutParams) minAngle.getLayoutParams();
        RelativeLayout.LayoutParams maxAngleParams=(RelativeLayout.LayoutParams) maxAngle.getLayoutParams();
        if(minAngleParams!=null&&maxAngleParams!=null) {
            int marginLeft=discreteSeekBarWidth * 2 / 7+activity.getResources().getDimensionPixelSize(R.dimen.padding_16);
            minAngleParams.leftMargin=marginLeft;
            maxAngleParams.leftMargin=marginLeft+bestAngleWidth/2+activity.getResources().getDimensionPixelSize(R.dimen.padding_12);
        }

        final TextView submit = (TextView) view.findViewById(R.id.btn_submit);

        TextView cancel = (TextView) view.findViewById(R.id.btn_cancle);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(discreteSeekBar.getValue());
                uiutils.setPreferenceKeyIntValue(activity, Definition.KEY_FOV, discreteSeekBar.getValue());
                dialog.dismiss();
                submitListener.onClick(v);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if(onCancelListener!=null)
                    onCancelListener.onClick(view);
            }
        });
        dialog.show();

        dialog.getWindow().setLayout(dip2px(activity,330), dip2px(activity,236));
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context , float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
