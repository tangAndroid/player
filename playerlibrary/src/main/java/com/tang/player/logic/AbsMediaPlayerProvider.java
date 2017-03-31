package com.tang.player.logic;

import com.tang.player.listeners.IMediaPlayer;

/**
 * @author txf
 * @Title
 * @package com.tang.player.logic
 * @date 2017/3/17 0017
 */

public abstract class AbsMediaPlayerProvider implements
        IMediaPlayer.OnCompletionListener,
        IMediaPlayer.OnErrorListener,
        IMediaPlayer.OnPreparedListener,
        IMediaPlayer.OnVideoSizeChangedListener,
        IMediaPlayer.OnBufferingUpdateListener,
        IMediaPlayer.OnInfoListener,
        IMediaPlayer.OnSeekCompleteListener {
    private final String TAG = getClass().getName();
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
    }
    @Override
    public boolean onError(IMediaPlayer var1, int var2, int var3) {
        return false;
    }
    @Override
    public void onPrepared(IMediaPlayer var1) {
    }
    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {

    }
    @Override
    public void onBufferingUpdate(IMediaPlayer var1, int var2) {
    }
    @Override
    public boolean onInfo(IMediaPlayer var1, int var2, int var3) {
        return false;
    }
    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
    }
}
