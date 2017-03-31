package com.tang.player.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tang.player.R;
import com.tang.player.tools.DelayedTask;

import static com.tang.player.R.id.mFullscreen;

/**
 * @author txf
 * @Title 播放控制类
 * @package com.tang.player.ui
 * @date 2017/3/24 0024
 */

public class PlayerControlWidget extends GestureControl implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, DelayedTask.OnDelayedTaskListener {
    private String TAG = getClass().getName();
    private RelativeLayout mTopLayout;//控制层顶部
    private LinearLayout mBotftomLayout;//控制层底部
    private TextView mDuration,
            mCurrentTime;
    private SeekBar mSeekBar;//拖拽进度条
    private int mMaxDuration;//总时长
    private OnPlayControlListener l;
    private boolean isHideControlBar;//是否执行了动画 隐藏了控制ui
    private boolean isPause;//是否暂停
    private DelayedTask mDelayedTask;

    public PlayerControlWidget(Context context, OnPlayControlListener l) {
        super(context);
        init(l);
    }
    public PlayerControlWidget(Context context, AttributeSet attrs, OnPlayControlListener l) {
        super(context, attrs);
        init(l);
    }
    public PlayerControlWidget(Context context, AttributeSet attrs, int defStyleAttr, OnPlayControlListener l) {
        super(context, attrs, defStyleAttr);
        init(l);
    }

    private void init(OnPlayControlListener l) {
        this.l = l;
        initViews();
        initListener();
        mDelayedTask = new DelayedTask(this);
    }
    private void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_playcontrol_layout, this);
        mTopLayout = (RelativeLayout) findViewById(R.id.topLayout);
        mBotftomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        mSeekBar = (SeekBar) findViewById(R.id.mSeekBar);
        mDuration = (TextView) findViewById(R.id.mDuration);
        mCurrentTime = (TextView) findViewById(R.id.mCurrentTime);
    }
    private void initListener() {
        mSeekBar.setOnSeekBarChangeListener(this);
        findViewById(R.id.mPlay).setOnClickListener(this);
        findViewById(R.id.mFullscreen).setOnClickListener(this);
    }
    public void showControlBar() {
        startDelayedTask();
        isHideControlBar = false;
        buildObjectAnimator(mTopLayout, -mTopLayout.getHeight(), 0, 0.5f, 1.0f).setDuration(200).start();
        buildObjectAnimator(mBotftomLayout, mBotftomLayout.getHeight(), 0, 0.5f, 1.0f).setDuration(200).start();
    }

    public void hideControlBar() {
        stopDelayedTask();
        isHideControlBar = true;
        buildObjectAnimator(mTopLayout, 0, -mTopLayout.getHeight(), 1.0f, 0.5f).setDuration(200).start();
        buildObjectAnimator(mBotftomLayout, 0, mBotftomLayout.getHeight(), 1.0f, 0.5f).setDuration(200).start();
    }
    private AnimatorSet buildObjectAnimator(View view, float start, float end, float starta, float ende) {
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, start, end);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, View.ALPHA, starta, ende);
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new AccelerateInterpolator());
        set.playTogether(animator2, animator3);
        return set;
    }
    /**
     * 开始延时任务隐藏控制栏
     * */
    private void startDelayedTask(){
        mDelayedTask.setOnDelayedTaskListener(this);
        mDelayedTask.startForwardTimer(8000,0);
    }
    /**
     * 结束延时任务
     * */
    private void stopDelayedTask(){
        if(mDelayedTask != null){
            mDelayedTask.setOnDelayedTaskListener(null);
            mDelayedTask.stopForwardTimer();
        }
    }
    @Override
    public boolean singleTapConfirmed(MotionEvent e) {
        if (isHideControlBar) {
            showControlBar();
        } else {
            hideControlBar();
        }
        return true;
    }

    @Override
    public boolean doubleTap(MotionEvent e) {
        findViewById(R.id.mPlay).performClick();
        return true;
    }

    protected void startSeek() {
        l.onStartTrackingTouch(null);
    }

    protected void seekChanged(long time) {
        l.onProgressChanged(null, (int) (time / 1000), true);
    }

    protected void stopSeek(long time) {
        l.onStopTrackingTouch(null);
    }

    /**
     * 设置播放总时长
     *
     * @param time 毫秒
     */
    public void setDuration(long time) {
//        Log.i(TAG,"视频总时长 "+(time/1000)+" 秒");
        setGestureMaxTime(time);
        mMaxDuration = (int) (time / 1000);
        mDuration.setText(timeFormat(time));
        mSeekBar.setMax(mMaxDuration);
    }

    /**
     * 设置当前播放位置(时间)
     * @param time 毫秒
     */
    public void setCurrentTime(long time) {
        setGestureCurrentTime(time);
//        Log.i(TAG,"当前播放位置 "+(time/1000)+" 秒");
        mCurrentTime.setText(timeFormat(time));
        mSeekBar.setProgress((int) (time / 1000));
    }

    /************************************************/
    //seekBra 监听
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        l.onProgressChanged(seekBar, progress, fromUser);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        l.onStartTrackingTouch(seekBar);
        stopDelayedTask();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        l.onStopTrackingTouch(seekBar);
        startDelayedTask();
    }

    /************************************************/
    //点击事件监听
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.mPlay) {//暂停或播放事件
            if (!isPause) {
                isPause = true;
                v.setSelected(isPause);
            } else {
                isPause = false;
                v.setSelected(isPause);
            }
            l.onPlayOrPause(isPause);
            return;
        }
        if (id == mFullscreen) {
            l.onFullscreen(v);
            return;
        }
    }

    @Override
    public void onHandleMessage(int msg) {
        hideControlBar();
    }

    /************************************************/
    public interface OnPlayControlListener {
        /**
         * SeekBar发生改变
         *
         * @param seekBar
         * @param progress 值1 ~ seekBar.max
         * @param fromUser 是否用户操作
         */
        void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

        /**
         * SeekBar开始操作
         */
        void onStartTrackingTouch(SeekBar seekBar);

        /**
         * SeekBar结束操作
         */
        void onStopTrackingTouch(SeekBar seekBar);

        /**
         * 播放或暂停
         */
        void onPlayOrPause(boolean isPause);

        /**
         * 全屏
         */
        void onFullscreen(View v);
    }
}
