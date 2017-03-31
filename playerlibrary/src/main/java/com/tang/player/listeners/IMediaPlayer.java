package com.tang.player.listeners;

import android.content.Context;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author txf
 * @Title 视频播放接口
 * @package com.tang.player.listeners
 * @date 2017/3/17 0017
 */

public interface IMediaPlayer {
    int STATE_UNDEF = -1;
    int STATE_IDLE = 0;//空闲状态
    int STATE_INITIALIZED = 1;//初始化
    int STATE_PREPARING = 2;//准备中。。
    int STATE_PREPARED = 3;//准备完成
    int STATE_STARTED = 4;//开始
    int STATE_PAUSED = 5;//暂停
    int STATE_STOPED = 6;//停止
    int STATE_PLAYBACK_COMPLETE = 7;//播放完成
    int STATE_END = 8;//结束
    int STATE_ERROR = 9;//播放错误
    /**
     * 获取当前播放状态
     */
    int getCurrentState();

    /**
     * 设置当前播放状态
     */
    void setCurrentState(int state);

    /**
     * 获取当前播放位置
     */
    long getCurrentPosition();

    /**
     * 获取当前播放持续时间(时长)
     */
    long getDuration();

    /**
     * 获取视频高度
     */
    int getVideoHeight();

    /**
     * 获取视频宽度
     */
    int getVideoWidth();

    /**
     * 是否在播放
     */
    boolean isPlaying();

    /**
     * 暂停
     */
    void pause();

    /**
     * 准备
     */
    void prepare();

    /**
     * 准备异步
     */
    void prepareAsync();

    /**
     * 释放
     */
    void release();

    /**
     * 重置
     */
    void reset();

    /**
     * @param var1 移动至？处播放
     */
    void seekTo(long var1);

    /**
     * 开始播放
     */
    void start();

    /**
     * 停止
     */
    void stop();

    /**
     * 设置视频画面显示的目标
     */
    void setDisplay(SurfaceHolder var1);

    /**
     * 设置表面类型
     */
    void setSurfaceType(SurfaceView var1);

    /**
     * 设置音频流类型
     */
    void setAudioStreamType(int var1);

    /**
     * @param context
     * @param uri     设置数据源
     */
    void setDataSource(Context context, Uri uri);

    /**
     * @param uri 设置数据源
     */
    void setDataSource(String uri);

    /**
     * 设置视频播放画面在屏幕上
     */
    void setScreenOnWhilePlaying(boolean var1);

    /**
     * 设置音量
     */
    void setVolume(float var1, float var2);

    /**
     * 设置缓冲更新侦听器
     */
    void setOnBufferingUpdateListener(OnBufferingUpdateListener var1);

    /**
     * 设置视频播放完成侦听器
     */
    void setOnCompletionListener(OnCompletionListener var1);

    /**
     * 设置视频播放错误侦听器
     */
    void setOnErrorListener(OnErrorListener var1);

    /**
     * 设置视频播放信息侦听器
     */
    void setOnInfoListener(OnInfoListener var1);

    /**
     * 设置视频播放准备侦听器
     */
    void setOnPreparedListener(OnPreparedListener var1);

    /**
     * 设置视频播放寻求完成侦听器
     */
    void setOnSeekCompleteListener(OnSeekCompleteListener var1);

    /**
     * 设置视频播放画面大小改变侦听器
     */
    void setOnVideoSizeChangedListener(OnVideoSizeChangedListener var1);


    interface OnInfoListener {
        boolean onInfo(IMediaPlayer var1, int var2, int var3);
    }

    interface OnErrorListener {
        boolean onError(IMediaPlayer var1, int var2, int var3);
    }

    interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(IMediaPlayer var1, int var2, int var3, int var4, int var5);
    }

    interface OnSeekCompleteListener {
        void onSeekComplete(IMediaPlayer var1);
    }

    interface OnBufferingUpdateListener {
        void onBufferingUpdate(IMediaPlayer var1, int var2);
    }

    interface OnCompletionListener {
        void onCompletion(IMediaPlayer var1);
    }

    interface OnPreparedListener {
        void onPrepared(IMediaPlayer var1);
    }
}
