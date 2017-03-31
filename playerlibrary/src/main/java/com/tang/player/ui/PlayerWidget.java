package com.tang.player.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.tang.player.beans.MediaBean;
import com.tang.player.listeners.IMediaPlayer;
import com.tang.player.listeners.IPlayCallback;
import com.tang.player.listeners.IPlayerSurfaceCallback;
import com.tang.player.logic.MediaPlayerProvider;
import com.tang.player.tools.DelayedTask;

/**
 * @author txf
 * @Title 视频显示UI
 * 1,控制UI
 * 2.视频显示的surfaceView
 * 3.视频缓冲Loading
 * 4.视频播放前的缩略图
 * @package com.tang.player
 * @date 2017/3/10 0010
 */
public class PlayerWidget extends FrameLayout implements
        IPlayerSurfaceCallback,
        IPlayCallback,
        PlayerControlWidget.OnPlayControlListener,
        DelayedTask.OnDelayedTaskListener {
    private PlayerSurface mPlayerSurface;//视频层
    private PlayerControlWidget mPlayControlWidget;//控制层
    private LoadingScaleView mLoadingScaleView;//Loading层
    private MediaBean mMediaBean;
    private SurfaceHolder mSurfaceHolder;
    private DelayedTask mDelayedTask;
    private int mProgress;

    public PlayerWidget(Context context) {
        super(context);
        init();
    }

    public PlayerWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        //设置视频播放前的背景!
        setBackgroundColor(0xff000000);
        //添加视频呈现的SurfaceView
        addSurfaceView();
        //添加控制UI
        addPlayControl();
        //添加Loading层
        addLoading();
    }

    private void addLoading() {
        mLoadingScaleView = new LoadingScaleView(getContext());
        this.addView(mLoadingScaleView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mLoadingScaleView.startAnimation();
    }
    private void addPlayControl() {
        mPlayControlWidget = new PlayerControlWidget(getContext(),this);
        hidePlayControlWidget();
        mPlayControlWidget.setBackgroundColor(0x01000000);
        this.addView(mPlayControlWidget, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
    private void addSurfaceView() {
        mPlayerSurface = new PlayerSurface(getContext(), this);
        this.addView(mPlayerSurface, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }
    public void showLoading(){
        if(mLoadingScaleView.getVisibility() == INVISIBLE)
            mLoadingScaleView.setVisibility(VISIBLE);
    }
    public void hideLoading(){
        if(mLoadingScaleView.getVisibility() == VISIBLE)
            mLoadingScaleView.setVisibility(INVISIBLE);
    }
    public void hidePlayControlWidget(){
        if(mPlayControlWidget.getVisibility() == VISIBLE)
            mPlayControlWidget.setVisibility(INVISIBLE);
    }
    public void showPlayControlWidget(){
        if(mPlayControlWidget.getVisibility() == INVISIBLE)
            mPlayControlWidget.setVisibility(VISIBLE);
    }
    /**
     * 开始播放
     */
    public void startPlay(MediaBean bean) {
        mMediaBean = bean;
        MediaPlayerProvider.getInstance().start(this, mSurfaceHolder, bean);
    }
    public PlayerSurface getPlayerSurface() {
        return mPlayerSurface;
    }
    private boolean checkUISize(int videoWidth, int videoHeight) {
        PlayerSurface surface = getPlayerSurface();
        int mSurfaceWidth = surface.getSurfaceWidth();
        int mSurfaceHeight = surface.getSurfaceHeight();
        if (videoWidth != 0 && videoHeight != 0) {
            if (videoWidth != mSurfaceWidth && videoHeight != mSurfaceHeight) {
                return true;
            }
        }
        return false;
    }

    private void stopDelayedTask() {
        if (mDelayedTask != null) {
            mDelayedTask.setOnDelayedTaskListener(null);
            mDelayedTask.stopForwardTimer();
            mDelayedTask = null;
        }
    }
    private void pauseDelayedTask() {
        if (mDelayedTask != null) {
            mDelayedTask.setOnDelayedTaskListener(null);
            mDelayedTask.stopForwardTimer();
        }
    }
    private void startDelayedTask() {
        if (mDelayedTask != null) {
            mDelayedTask.setOnDelayedTaskListener(this);
        }else {
            mDelayedTask = new DelayedTask(this);
        }
        mDelayedTask.startForwardTimer(1000,0);
    }
    /**************************************************************/
    //surfaceView相关接口
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.mSurfaceHolder = holder;
        MediaPlayerProvider.getInstance().start(this, mSurfaceHolder, mMediaBean);
        showLoading();
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        MediaPlayerProvider.getInstance().release();
        mSurfaceHolder = null;
        stopDelayedTask();
    }

    /**************************************************************/
    //视频播放器相关接口
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

    }
    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int var2, int var3) {
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        int videoWidth = iMediaPlayer.getVideoWidth();
        int videoHeight = iMediaPlayer.getVideoHeight();
        if (checkUISize(videoWidth, videoHeight)) {
            mPlayerSurface.updateVideoSize(videoWidth, videoHeight);
        }
        iMediaPlayer.start();
        showPlayControlWidget();
        mPlayControlWidget.setDuration(iMediaPlayer.getDuration());
        mPlayControlWidget.setCurrentTime(iMediaPlayer.getCurrentPosition());
        startDelayedTask();
    }
    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
        int videoWidth = iMediaPlayer.getVideoWidth();
        int videoHeight = iMediaPlayer.getVideoHeight();
        if (checkUISize(videoWidth, videoHeight)) {
            mPlayerSurface.updateVideoSize(videoWidth, videoHeight);
        }
    }
    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int var2) {

    }
    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int var2, int var3) {
        switch (var2) {
            case 701://开始缓冲
                showLoading();
                break;
            case 702://结束缓冲
                hideLoading();
                break;
            case 10003://媒体信息加载完毕开始播放
                hideLoading();
                break;
        }
        return true;
    }
    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {

    }
    /**************************************************************/
    //控制UI相关接口
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mProgress = progress;
            mPlayControlWidget.setCurrentTime(mProgress * 1000);
        }
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        pauseDelayedTask();
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        MediaPlayerProvider.getInstance().getMediaPlayer().seekTo(mProgress * 1000);
        startDelayedTask();
    }
    @Override
    public void onPlayOrPause(boolean isPause) {
        if (isPause) {
            MediaPlayerProvider.getInstance().getMediaPlayer().pause();
            pauseDelayedTask();
        } else {
            MediaPlayerProvider.getInstance().getMediaPlayer().start();
            startDelayedTask();
        }
    }

    @Override
    public void onFullscreen(View v) {
//        if(((Activity)getContext()).getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
//            ((Activity)getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }
    }
    /**************************************************************/
    //延时任务接口
    @Override
    public void onHandleMessage(int msg) {
        mPlayControlWidget.setCurrentTime(MediaPlayerProvider.getInstance().getMediaPlayer().getCurrentPosition());
        mDelayedTask.startForwardTimer(1000, 0);
    }
}