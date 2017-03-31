package com.tang.player.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.tang.player.R;

/**
 * @author txf
 * @Title 进度View
 * @package com.tang.player.ui
 * @date 2017/3/21 0021
 */
public class ProgressView extends ImageView {
    private String TAG = getClass().getName();
    private Paint mPaint;
    private float mStartX, mStartY, mStopX, mStopY, mWidth, mProgress,mProgressLevel;
    private int mProgressPercent;

    public ProgressView(Context context) {
        super(context);
        init();
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);
        setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.rounded_rectangle_ce000000));
        setImageResource(R.mipmap.icon_volume);
        int padding = dip2px(18);
        setPadding(padding, padding, padding, padding);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartX = 20;
        mStartY = getMeasuredHeight() - 15;
        mStopX = getMeasuredWidth() - mStartX;
        mStopY = mStartY;
        //音量总长度
        mWidth = mStopX - mStartX;
        //音量增加为100个等级,每个等级的长度.
        mProgressLevel = mWidth / 100;
        reset();
//        Log.i(TAG, " onSizeChanged :" + "mWidth " + mWidth + " mProgressLevel " + mProgressLevel);
    }
    /**
     * 设置进度百分比
     * @param var 进度百分比 1~100
     */
    public void setProgressPercent(int var) {
        this.mProgressPercent = var;
        reset();
    }
    private void reset() {
        if (mProgressPercent <= 100 && mProgressLevel != 0) {
            this.mProgress = mProgressLevel * mProgressPercent;
//            Log.i(TAG, "进度大小" + mProgress);
            invalidate();
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度条背景
        mPaint.setColor(Color.parseColor("#757575"));
        canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
        //绘制进度具体大小
        if(mStartX < (mStartX + mProgress)){
            mPaint.setColor(Color.parseColor("#ffffff"));
            canvas.drawLine(mStartX, mStartY, (mStartX + mProgress), mStopY, mPaint);
        }
    }
}
