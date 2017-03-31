package com.tang.player.logic;

import android.media.AudioManager;
import android.view.SurfaceHolder;

import com.tang.player.beans.MediaBean;
import com.tang.player.listeners.IPlayCallback;

/**
 * @author txf
 * @Title
 * @package com.tang.player.logic
 * @date 2017/3/17 0017
 */

public class MediaPlayerProvider extends AbsMediaPlayerProvider {
    private final String TAG = getClass().getName();
    private static MediaPlayerProvider mMediaPlayerProvider;

    private MediaPlayerProvider() {
    }

    public static MediaPlayerProvider getInstance() {
        if (mMediaPlayerProvider == null)
            mMediaPlayerProvider = new MediaPlayerProvider();
        return mMediaPlayerProvider;
    }

    private MediaPlayer mediaPlayer;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.setAllPlayCallback(null);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void start(IPlayCallback l, SurfaceHolder holder, MediaBean bean) {
        if (mediaPlayer != null) {
            release();
        }
        if (holder == null || bean == null || bean.getUrl() == null || bean.getUrl().length() < 0)
            return;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDisplay(holder);
        mediaPlayer.setAllPlayCallback(l);
        mediaPlayer.setDataSource(bean.getUrl());
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.prepareAsync();
    }
}
