package com.tang.player.logic;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tang.player.listeners.IMediaPlayer;
import com.tang.player.listeners.IPlayCallback;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * @author txf
 * @Title 媒体播放器
 * @package com.tang.player.logic
 * @date 2017/3/16 0016
 */

public class MediaPlayer implements IMediaPlayer {
    public final String TAG = getClass().getName();
    private IjkMediaPlayer mMediaPlayer;//媒体播放器
    private int mState;
    private IPlayCallback mIPlayCallback;

    public MediaPlayer() {
        mState = STATE_UNDEF;
        mMediaPlayer = new IjkMediaPlayer();
    }

    public IjkMediaPlayer getIjkMediaPlayer() {
        return mMediaPlayer;
    }

    /**
     * 全部播放状态回调
     */
    public void setAllPlayCallback(IPlayCallback l) {
        this.mIPlayCallback = l;
        setOnCompletionListener(null);
        setOnErrorListener(null);
        setOnPreparedListener(null);
        setOnVideoSizeChangedListener(null);
        setOnBufferingUpdateListener(null);
        setOnInfoListener(null);
        setOnSeekCompleteListener(null);
    }

    /**
     * 检查播放器状态
     */
    public int checkPlayerState() {
        if (mMediaPlayer == null)
            return -1;
        return 0;
    }

    @Override
    public int getCurrentState() {
        return mState;
    }

    @Override
    public void setCurrentState(int state) {
        this.mState = state;
    }

    @Override
    public long getCurrentPosition() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>> 播放器为空 getCurrentPosition()");
            return -1;
        }
        return (int) mMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>> 播放器为空 getDuration()");
            return -1;
        }
        return  mMediaPlayer.getDuration();
    }

    @Override
    public int getVideoHeight() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>> 播放器为空 getVideoHeight()");
            return -1;
        }
        return mMediaPlayer.getVideoHeight();
    }

    @Override
    public int getVideoWidth() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>> 播放器为空 getVideoWidth()");
            return -1;
        }
        return mMediaPlayer.getVideoWidth();
    }

    @Override
    public boolean isPlaying() {
        if (checkPlayerState() == -1) {
            return false;
        }
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void pause() {
        if (!isPlaying()) {
            Log.i(TAG, "------>>> 没有开始播放 pause()失败");
            return;
        }
        try {
            mMediaPlayer.pause();
            mState = STATE_PAUSED;
        } catch (IllegalStateException e) {
            Log.i(TAG, "------>>> pause()异常");
            e.printStackTrace();
        }
    }

    @Override
    public void prepare() {
        prepareAsync();
    }

    @Override
    public void prepareAsync() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 prepareAsync()");
            return;
        }
        try {
            mMediaPlayer.prepareAsync();
            mState = STATE_PREPARING;
        } catch (IllegalStateException e) {
            Log.i(TAG, "------>>> prepareAsync()异常");
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 release()");
            return;
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        mState = STATE_UNDEF;
    }

    @Override
    public void reset() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 reset()");
            return;
        }
        mMediaPlayer.reset();
        mState = STATE_IDLE;
    }

    @Override
    public void seekTo(long var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 seekTo()");
            return;
        }
        try {
            mMediaPlayer.seekTo(var1);
        } catch (IllegalStateException e) {
            Log.i(TAG, "------>>> seekTo()异常");
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 start()");
            return;
        }
        try {
            mMediaPlayer.start();
            mState = STATE_STARTED;
        } catch (IllegalStateException e) {
            Log.i(TAG, "------>>> start()异常");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 stop()");
            return;
        }
        try {
            mMediaPlayer.stop();
            mState = STATE_STOPED;
        } catch (IllegalStateException e) {
            Log.i(TAG, "------>>> stop()异常");
            e.printStackTrace();
        }
    }

    @Override
    public void setDisplay(SurfaceHolder holder) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setDisplay()");
            return;
        }
        mMediaPlayer.setDisplay(holder);
        mMediaPlayer.setScreenOnWhilePlaying(true);
    }

    @Override
    public void setSurfaceType(SurfaceView surfaceView) {
        if (surfaceView != null && surfaceView.getHolder() != null)
            surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void setAudioStreamType(int var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setAudioStreamType()");
            return;
        }
        mMediaPlayer.setAudioStreamType(var1);
    }

    @Override
    public void setDataSource(Context context, Uri uri) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setDataSource()");
            return;
        }
        try {
            mMediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            Log.i(TAG, "------>>>setDataSource() 异常");
            e.printStackTrace();
        }
    }

    @Override
    public void setDataSource(String uri) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setDataSource()");
            return;
        }
        try {
            mMediaPlayer.setDataSource(uri);
        } catch (IOException e) {
            Log.i(TAG, "------>>>setDataSource() 异常");
            e.printStackTrace();
        }
    }

    @Override
    public void setScreenOnWhilePlaying(boolean var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setScreenOnWhilePlaying()");
            return;
        }
        mMediaPlayer.setScreenOnWhilePlaying(var1);
    }

    @Override
    public void setVolume(float var1, float var2) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setVolume()");
            return;
        }
        mMediaPlayer.setVolume(var1, var2);
    }

    @Override
    public void setOnBufferingUpdateListener(OnBufferingUpdateListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnBufferingUpdateListener()");
            return;
        }
        mMediaPlayer.setOnBufferingUpdateListener(new MyOnBufferingUpdateListener(this, var1));
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnCompletionListener()");
            return;
        }
        mMediaPlayer.setOnCompletionListener(new MyOnCompletionListener(this, var1));
    }

    @Override
    public void setOnErrorListener(OnErrorListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnErrorListener()");
            return;
        }
        mMediaPlayer.setOnErrorListener(new MyOnErrorListener(this, var1));
    }

    @Override
    public void setOnInfoListener(OnInfoListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnInfoListener()");
            return;
        }
        mMediaPlayer.setOnInfoListener(new MyOnInfoListener(this, var1));
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnPreparedListener()");
            return;
        }
        mMediaPlayer.setOnPreparedListener(new MyOnPreparedListener(this, var1));
    }

    @Override
    public void setOnSeekCompleteListener(OnSeekCompleteListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnSeekCompleteListener()");
            return;
        }
        mMediaPlayer.setOnSeekCompleteListener(new MyOnSeekCompleteListener(this, var1));
    }

    @Override
    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener var1) {
        if (checkPlayerState() == -1) {
            Log.i(TAG, "------>>>播放器为空 setOnVideoSizeChangedListener()");
            return;
        }
        mMediaPlayer.setOnVideoSizeChangedListener(new MyOnVideoSizeChangedListener(this, var1));
    }

    class MyOnBufferingUpdateListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener {
        private IMediaPlayer im;
        private OnBufferingUpdateListener l;

        public MyOnBufferingUpdateListener(IMediaPlayer im, OnBufferingUpdateListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public void onBufferingUpdate(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer, int i) {
            Log.i(TAG, "---->>>>  播发器原始侦听 缓冲更新 ; " + i);
            if (mIPlayCallback != null) {
                mIPlayCallback.onBufferingUpdate(im, i);
            }
            if (im != null && l != null)
                l.onBufferingUpdate(im, i);
        }
    }

    class MyOnCompletionListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener {
        private IMediaPlayer im;
        private OnCompletionListener l;

        public MyOnCompletionListener(IMediaPlayer im, OnCompletionListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public void onCompletion(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer) {
            mState = STATE_PLAYBACK_COMPLETE;
            Log.i(TAG, "---->>>>  播发器原始侦听 播放完成");
            if (mIPlayCallback != null) {
                mIPlayCallback.onCompletion(im);
            }
            if (im != null && l != null) {
                l.onCompletion(im);
            }
        }
    }

    class MyOnErrorListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener {
        private IMediaPlayer im;
        private OnErrorListener l;

        public MyOnErrorListener(IMediaPlayer im, OnErrorListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public boolean onError(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer, int i, int i1) {
            mState = STATE_ERROR;
            Log.i(TAG, "---->>>>  播发器原始侦听 播放错误 : " + i + " , " + i1);
            if (mIPlayCallback != null) {
                return mIPlayCallback.onError(im, i, i1);
            }
            if (im != null && l != null) {
                return l.onError(im, i, i1);
            }
            return false;
        }
    }

    class MyOnInfoListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener {
        private IMediaPlayer im;
        private OnInfoListener l;

        public MyOnInfoListener(IMediaPlayer im, OnInfoListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public boolean onInfo(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer, int i, int i1) {
            Log.i(TAG, "---->>>>  播发器原始侦听 播放信息 : " + i + " , " + i1);
            if (mIPlayCallback != null) {
                return mIPlayCallback.onInfo(im, i, i1);
            }
            if (im != null && l != null) {
                return l.onInfo(im, i, i1);
            }
            return false;
        }
    }

    class MyOnPreparedListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener {
        private IMediaPlayer im;
        private OnPreparedListener l;

        public MyOnPreparedListener(IMediaPlayer im, OnPreparedListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public void onPrepared(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer) {
            mState = IMediaPlayer.STATE_PREPARED;
            Log.i(TAG, "---->>>>  播发器原始侦听  准备完成");
            if (mIPlayCallback != null) {
                mIPlayCallback.onPrepared(im);
            }
            if (im != null && l != null) {
                l.onPrepared(im);
            }
        }
    }

    class MyOnSeekCompleteListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener {
        private IMediaPlayer im;
        private OnSeekCompleteListener l;

        public MyOnSeekCompleteListener(IMediaPlayer im, OnSeekCompleteListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public void onSeekComplete(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer) {
            Log.i(TAG, "---->>>>  播发器原始侦听  寻求完成");
            if (mIPlayCallback != null) {
                mIPlayCallback.onSeekComplete(im);
            }
            if (im != null && l != null) {
                l.onSeekComplete(im);
            }
        }
    }

    class MyOnVideoSizeChangedListener implements tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener {
        private IMediaPlayer im;
        private OnVideoSizeChangedListener l;

        public MyOnVideoSizeChangedListener(IMediaPlayer im, OnVideoSizeChangedListener l) {
            this.im = im;
            this.l = l;
        }

        @Override
        public void onVideoSizeChanged(tv.danmaku.ijk.media.player.IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
            Log.i(TAG, "---->>>>  播发器原始侦听  视频大小发生改变 ; " + i + " , " + i1 + " , " + i2 + " , " + i3);
            if (mIPlayCallback != null) {
                mIPlayCallback.onVideoSizeChanged(im, i, i1, i2, i3);
            }
            if (im != null && l != null) {
                l.onVideoSizeChanged(im, i, i1, i2, i3);
            }
        }
    }
}
