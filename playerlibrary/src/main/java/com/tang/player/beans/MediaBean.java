package com.tang.player.beans;

import java.io.Serializable;

/**
 * @author txf
 * @Title 媒体播放bean对象
 * @package com.tang.player.beans
 * @date 2017/3/15 0015
 */

public class MediaBean implements Serializable{
    public final static int VIDEO_TYPE_0 = 0;//点播
    public final static int VIDEO_TYPE_1 = 1;//直播

    private String url;//视频播放地址
    private String name;//视频标题
    private int videoType;//视频类型{点播 0 或 直播 1}

    public int getVideoType() {
        return videoType;
    }
    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MediaBean{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
