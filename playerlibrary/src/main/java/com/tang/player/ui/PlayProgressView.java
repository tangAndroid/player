package com.tang.player.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.tang.player.R;

/**
 * @author txf
 * @Title 播放进度
 * @package com.tang.player.ui
 * @date 017/3/21 0021
 */
public class PlayProgressView extends TextView {
    private String TAG = getClass().getName();
    private Paint mPaint;
    private float mStartX, mStartY, mStopX, mStopY, mWidth, mPlayProgress, mPlayProgressLevel;
    private float mPlayProgressPercent;
    private long mCurrentTime;
    public PlayProgressView(Context context) {
        super(context);
        init();
    }

    public PlayProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.rounded_rectangle_ce000000));
        setGravity(Gravity.CENTER);
        setTextColor(Color.parseColor("#ffffff"));
        setTextSize(18);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartX = 20;
        mStartY = getMeasuredHeight() - 15;
        mStopX = getMeasuredWidth() - mStartX;
        mStopY = mStartY;
        //进度总长度
        mWidth = mStopX - mStartX;
        //进度增加为100个等级,每个等级的长度.
        mPlayProgressLevel = mWidth / 100;
        resetPlayProgress();
        Log.i(TAG, " onSizeChanged :" + "mWidth " + mWidth + " mPlayProgressLevel " + mPlayProgressLevel);
    }

    /**
     * 设置进度百分比
     * @param brightness 亮度百分比 1~100
     * @param time       当前时长
     */
    public void setBrightnessPercent(float brightness, String time) {
        this.mPlayProgressPercent = brightness;
        resetPlayProgress();
        setText(time);
    }
    public void setCurrentTime(long currentTime) {
        this.mCurrentTime = currentTime;
    }
    public long getCurrentTime() {
        return mCurrentTime;
    }
    private void resetPlayProgress() {
        if (mPlayProgressPercent <= 100 && mPlayProgressLevel != 0) {
            this.mPlayProgress = mPlayProgressLevel * mPlayProgressPercent;
            Log.i(TAG, "进度大小" + mPlayProgress);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度背景灰色
        mPaint.setColor(Color.parseColor("#ce757575"));
        canvas.drawLine(mStartX, mStartY, mStopX, mStopY, mPaint);
        //绘制进度具体大小
        if (mStartX < (mStartX + mPlayProgress)) {
            mPaint.setColor(Color.parseColor("#ffffff"));
            canvas.drawLine(mStartX, mStartY, (mStartX + mPlayProgress), mStopY, mPaint);
        }
    }
}
