package com.tang.player.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tang.player.beans.MediaBean;

/**
 * @author txf
 * @Title
 *
 * @package com.tang.player
 * @date 2017/3/15 0015
 */

public class TVideoView extends FrameLayout{
    private PlayerWidget mPlayerWidget;
    private MediaBean mMediaBean;
    public TVideoView(Context context) {
        super(context);
        init();
    }

    public TVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        //添加视频播放界面
        addPlayerWidget();
    }
    public void setData(MediaBean bean) {
        mMediaBean = bean;
        mPlayerWidget.startPlay(bean);
    }
    private void addPlayerWidget() {
        mPlayerWidget = new PlayerWidget(getContext());
        FrameLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        this.addView(mPlayerWidget, lp);
    }

}
