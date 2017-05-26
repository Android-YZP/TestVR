package com.example.m1762.testvr;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by m1762 on 2017/5/25.
 */

public class VerticalViewPager extends ViewPager {
    float mPosX = 0, mPosY = 0, mCurPosX = 0, mCurPosY = 0;
    private OnMoveListener mOnMoveListener = null;

    public VerticalViewPager(Context context) {
        super(context);
        init();
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // The majority of the magic happens here
        setPageTransformer(true, new VerticalPageTransformer());
        // The easiest way to get rid of the overscroll drawing that happens on the left and right
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    private class VerticalPageTransformer implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                view.setAlpha(1);

                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    /**
     * Swaps the X and Y coordinates of your touch event.
     */
    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();

        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;

        ev.setLocation(newX, newY);

        return ev;
    }

    /**
     * 给外面调用的滑动事件的接口
     */
    public void setOnMoveListener(OnMoveListener listener) {
        this.mOnMoveListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
        swapXY(ev); // return touch coordinates to original reference frame for any child views
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                mPosX = ev.getX();
                mPosY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                mCurPosX = ev.getX();
                mCurPosY = ev.getY();

                if (mCurPosX - mPosX > 0
                        && (Math.abs(mCurPosX - mPosX) > 350) && Math.abs(mCurPosY - mPosY) < 100) {
                    mOnMoveListener.onMove(true);//向左滑动

                    Log.e("滑动", mCurPosY - mPosY + "");
                } else if (mCurPosX - mPosX < 0 && (Math.abs(mCurPosX - mPosX) > 350) && Math.abs(mCurPosY - mPosY) < 100) {
                    mOnMoveListener.onMove(false);//向右滑动
                    Log.e("滑动", mCurPosY - mPosY + "");
                }

                break;
        }

        return super.onTouchEvent(swapXY(ev));
    }


    public interface OnMoveListener {
        public abstract void onMove(boolean isRight);
    }

}
