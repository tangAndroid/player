package com.tang.player.ui;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tang.player.R;

/**
 * @author txf
 * @Title 播放器手势控制UI
 * @package com.tang.player.ui
 * @date 2017/3/21 0021
 */

public class GestureControl extends FrameLayout {
    private String TAG = getClass().getName();
    private ProgressView mProgressView;
    //    private BrightnessView mBrightnessView;
    private PlayProgressView mPlayProgressView;
    GestureDetector detector;//手势检测器实例
    private AudioManager mAudioManager;

    private int mMaxVoice;//最大音量
    private int mCurrentVoice;//当前音量
    private int mVoiceProportion;

    private int mMaxBrightness;//最大亮度
    private int mCurrentBrightness;//当前亮度
    private int mBrightnessProportion;

    private long mMaxTime;//最大时间
    private long mCurrentTime;//当前时间
    private int mProgressProportion;

    private int mDx;
    private float downX, downY;//手指按下时的坐标
    private float minMove = 40;//最小滑动距离
    private boolean leftORright, topORbottom;//当前滑动方向
    private boolean isRefreshProgress;//是否更新进度

    public GestureControl(Context context) {
        super(context);
        init();
    }

    public GestureControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void init() {
        //创建手势检测器
        detector = new GestureDetector(getContext(), new MyGestureListener());
        //添加控制 音量 亮度UI
        addVolumeView();
        //添加控制 进度UI
        addPlayProgressView();
    }

    private void addPlayProgressView() {
        LayoutParams lp = new LayoutParams(dip2px(100), dip2px(80));
        lp.gravity = Gravity.CENTER;
        mPlayProgressView = new PlayProgressView(getContext());
        hidePlayProgressView();
        addView(mPlayProgressView, lp);
    }

    private void addVolumeView() {
        mCurrentVoice = -1;
        LayoutParams lp = new LayoutParams(dip2px(100), dip2px(80));
        lp.gravity = Gravity.CENTER;
        mProgressView = new ProgressView(getContext());
        hideVolumeView();
        addView(mProgressView, lp);
    }

    private void showPlayProgressView() {
        if (mPlayProgressView.getVisibility() == INVISIBLE) {
            mPlayProgressView.setVisibility(VISIBLE);
        }
    }

    private void hidePlayProgressView() {
        if (mPlayProgressView.getVisibility() == VISIBLE) {
            mPlayProgressView.setVisibility(INVISIBLE);
        }
    }

    private void showVolumeView() {
        mProgressView.setImageResource(R.mipmap.icon_volume);
        if (mProgressView.getVisibility() == INVISIBLE) {
            mProgressView.setVisibility(VISIBLE);
        }
    }

    private void hideVolumeView() {
        if (mProgressView.getVisibility() == VISIBLE) {
            mProgressView.setVisibility(INVISIBLE);
        }
    }

    private void showBrightnessView() {
        mProgressView.setImageResource(R.mipmap.icon_brightness);
        if (mProgressView.getVisibility() == INVISIBLE) {
            mProgressView.setVisibility(VISIBLE);
        }
    }

    private void hideBrightnessView() {
        if (mProgressView.getVisibility() == VISIBLE) {
            mProgressView.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置音量百分比
     *
     * @param volume 音量百分比 1~100
     */
    public void setVolumePercent(int volume) {
        getAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC, (getMaxVoice() / (100 / volume)), 0);
    }

    /**
     * 设置亮度百分比
     *
     * @param brightness 亮度百分比 1~100
     */
    public void setBrightnessPercent(int brightness) {
        setWindowBrightness((getMaxBrightness() / (100 / brightness)));
    }

    private AudioManager getAudioManager() {
        if (mAudioManager == null) {
            mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        }
        return mAudioManager;
    }

    private int getMaxVoice() {
        if (mMaxVoice == 0) {
            mMaxVoice = getAudioManager().getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        }
        return mMaxVoice;
    }

    private int getCurrentVoice() {
        if (mCurrentVoice == -1) {
            mCurrentVoice = getAudioManager().getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        return mCurrentVoice;
    }

    private int getVoiceProportion() {
        if (mVoiceProportion == 0)
            mVoiceProportion = getMeasuredHeight() / 2 / getMaxVoice();
        return mVoiceProportion;
    }

    private int getMaxBrightness() {
        if (mMaxBrightness == 0)
            mMaxBrightness = 255;
        return mMaxBrightness;
    }

    private int getCurrentBrightness() {
        if (mCurrentBrightness == -1) {
            mCurrentBrightness = (int) (((Activity) getContext()).getWindow().getAttributes().screenBrightness * 255f);
            mCurrentBrightness = mCurrentBrightness < 0 ? getSystemBrightness() : mCurrentBrightness;
        }
        return mCurrentBrightness;
    }

    /**
     * 获得系统亮度
     *
     * @return
     */
    private int getSystemBrightness() {
        int systemBrightness = 0;
        try {
            systemBrightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            return systemBrightness;
        }
        return systemBrightness;
    }

    public int getmBrightnessProportion() {
        if (mBrightnessProportion == 0)
            mBrightnessProportion = getMeasuredHeight() / 2 / getMaxBrightness();
        return mBrightnessProportion;
    }

    private void setWindowBrightness(int brightness) {
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    public void setGestureCurrentTime(long currentTime) {
        this.mCurrentTime = currentTime;
    }

    public long getGestureCurrentTime() {
        return mCurrentTime;
    }

    public void setGestureMaxTime(long maxTime) {
        this.mMaxTime = maxTime;
    }

    public long getGestureMaxTime() {
        return mMaxTime;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float startX = event.getX();
        float startY = event.getY();
        detector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = startX;
                downY = startY;
//                Log.i(TAG, " 按下 downX " + downX + " downY " + downY);
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.i(TAG, " 移动 startX " + startX + " startY " + startY);
                if (Math.abs(downX - startX) > minMove && !leftORright) {   //左右滑动
                    topORbottom = true;
                    // 计算手指此时的坐标和上次的坐标滑动的距离。
                    int dx = (int) (startX - downX);
                    downX = startX;
                    if (minMove == 0) {
                        refreshProgress(dx);//刷新进度
                    } else {
                        startSeek();
                    }
                    minMove = 0;
                } else if (Math.abs(downY - startY) > minMove && !topORbottom) {   //上下滑动
                    leftORright = true;
                    // 计算手指此时的坐标和上次的坐标滑动的距离。
                    int dx = (int) (startY - downY);
                    downY = startY;
                    if (minMove == 0) {
                        if (downX >= (getMeasuredWidth() / 2)) {
                            refreshVolume(dx);//右边开始刷新音量
                        } else {
                            refreshBrightness(dx);//左边开始刷新亮度
                        }
                    }
                    minMove = 0;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isRefreshProgress)
                    stopSeek(getGestureCurrentTime());
                isRefreshProgress = topORbottom = leftORright = false;
                mCurrentVoice = mCurrentBrightness = -1;
                minMove = 40;
                mDx = 0;
                hideVolumeView();
                hidePlayProgressView();
                break;
        }
        return true;
    }

    /**
     * 刷新进度
     */
    private void refreshProgress(int dx) {
        if (getGestureMaxTime() <= 0 || dx == 0) return;
        isRefreshProgress = true;
        showPlayProgressView();
        long playProgress = dx;
        if (dx > 0) {//前进
            playProgress = getGestureCurrentTime() + playProgress * 1000;
            if (playProgress > getGestureMaxTime()) {
                playProgress = getGestureMaxTime();
            }
        } else if (dx < 0) {//后退
            playProgress = getGestureCurrentTime() - Math.abs(playProgress) * 1000;
            if (playProgress < 0) {
                playProgress = 0;
            }
        }
        seekChanged(playProgress);
//setGestureCurrentTime(playProgress);
        mPlayProgressView.setBrightnessPercent(playProgress * 100 / getGestureMaxTime(), timeFormat(playProgress));
//                Log.i(TAG, "总时长  " + getGestureMaxTime() +
//                " 当前播放位置  " + getGestureCurrentTime() +
//                " 移动距离 dx " + dx +
//                " playProgress " + playProgress +
//                " 绘制进度百分比 " + (playProgress*100/getGestureMaxTime()));
    }

    public String timeFormat(long time) {
        long a = time / 60 / 60 / 1000;
        long b = time / 1000 / 60 % 60;
        long c = time / 1000 % 60;
        String hh = String.valueOf(a);
        String mm = String.valueOf(b);
        String ss = String.valueOf(c);
        if (a < 10) {
            hh = "0" + hh;
        }
        if (b < 10) {
            mm = "0" + mm;
        }
        if (c < 10) {
            ss = "0" + ss;
        }
        if (a == 0) {
            return mm + ":" + ss;
        }
        return hh + ":" + mm + ":" + ss;
    }

    /**
     * 刷新亮度
     */
    private void refreshBrightness(int dx) {
        showBrightnessView();
        //累加移动距离
        mDx = mDx + dx;
        int brightness = 0;
        if (Math.abs(mDx) >= getmBrightnessProportion()) {
            brightness = mDx / getmBrightnessProportion();
            if (brightness > 0) {
                brightness = getCurrentBrightness() - brightness;
                if (brightness < 0) {
                    mDx = 0;
                    brightness = 0;
                    mCurrentBrightness = -1;
                }
            } else if (brightness < 0) {
                brightness = getCurrentBrightness() + Math.abs(brightness);
                if (brightness > getMaxBrightness()) {
                    mDx = 0;
                    brightness = getMaxBrightness();
                    mCurrentBrightness = -1;
                }
            }
            setWindowBrightness(brightness);
            mProgressView.setProgressPercent(brightness * 100 / getMaxBrightness());
        }
//        Log.i(TAG, " 获取当前亮度 getCurrentBrightness " + getCurrentBrightness() +
//                " 最大亮度 getMaxBrightness " + getMaxBrightness() +
//                " 移动距离 mDx " + mDx +
//                " 亮度 brightness " + brightness +
//                " 绘制亮度百分 brightness " + (brightness * 100 / getMaxBrightness()));
    }

    /**
     * 刷新音量
     */
    private void refreshVolume(int dx) {
        showVolumeView();
        //累加移动距离
        mDx = mDx + dx;
        int volume = 0;
        if (Math.abs(mDx) >= getVoiceProportion()) {
            volume = mDx / getVoiceProportion();
            if (volume > 0) {
                volume = getCurrentVoice() - volume;
                if (volume < 0) {
                    mDx = 0;
                    volume = 0;
                    mCurrentVoice = -1;
                }
            } else if (volume < 0) {
                volume = getCurrentVoice() + Math.abs(volume);
                if (volume > getMaxVoice()) {
                    mDx = 0;
                    volume = getMaxVoice();
                    mCurrentVoice = -1;
                }
            }
            getAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
            mProgressView.setProgressPercent((volume * 100 / getMaxVoice()));
        }
//      Log.i(TAG, " 获取当前音量 mCurrentVoice " + getCurrentVoice() +
//                " 最大音量 mMaxVoice " + getMaxVoice() +
//                " 移动距离 mDx " + mDx +
//                " 音量 volume " + volume+
//                " 绘制音量百分比 volume " + (volume * 100 / getMaxVoice()));
    }
    /**
     * 单击手势
     */
    protected boolean singleTapConfirmed(MotionEvent e) {
        return false;
    }
    /**
     * 双击手势
     */
    protected boolean doubleTap(MotionEvent e) {
        return false;
    }

    /**
     * 手势推拽进度开始
     */
    protected void startSeek() {

    }
    /**
     * 手势推拽进度发生改变
     * @param time 毫秒值
     */
    protected void seekChanged(long time) {

    }
    /**
     * 手势拖拽进度结束
     * @param time 毫秒值
     */
    protected void stopSeek(long time) {

    }
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return singleTapConfirmed(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return doubleTap(e);
        }
    }
}
