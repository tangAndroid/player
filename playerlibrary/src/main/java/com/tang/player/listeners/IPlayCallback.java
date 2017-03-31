package com.tang.player.listeners;

/**
 * @author txf
 * @Title 视频播放状态统一回调接口
 * @package com.tang.player.listeners
 * @date 2017/3/20 0020
 */

public interface IPlayCallback {
    /**
     * 播放完成
     * */
    void onCompletion(IMediaPlayer iMediaPlayer);
    /**
     * 播放错误
     * */
    boolean onError(IMediaPlayer iMediaPlayer, int var2, int var3);
    /**
     * 准备完成
     * */
    void onPrepared(IMediaPlayer iMediaPlayer);
    /**
     * 视频大小发生改变
     * */
    void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3);
    /**
     * 缓冲更新
     * */
    void onBufferingUpdate(IMediaPlayer iMediaPlayer, int var2);
    /**
     * 视频信息
     * */
    boolean onInfo(IMediaPlayer iMediaPlayer, int var2, int var3);
    /**
     * 寻求完成
     * */
    void onSeekComplete(IMediaPlayer iMediaPlayer);
}
