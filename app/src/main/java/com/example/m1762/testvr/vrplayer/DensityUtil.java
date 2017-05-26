package com.example.m1762.testvr.vrplayer;

import android.content.Context;

public class DensityUtil {  
  
    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */

    /**
     * 作者: 姚中平  时间: 2017年5月24日17:35:13
     * 描述: 将px值转为dip的值
     * @param context 上下文对象
     * @param pxValue px值
     * @return dip的值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
}  