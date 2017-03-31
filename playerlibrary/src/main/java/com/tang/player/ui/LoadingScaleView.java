package com.tang.player.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tang.player.R;


/**
 * @author txf
 * @Title
 * @package  com.tang.player.ui
 * @date 2016/11/25 0025
 */

public class LoadingScaleView extends LinearLayout {
    private AnimatorSet set;
    private ImageView loadingImg1, loadingImg2, loadingImg3;

    public LoadingScaleView(Context context) {
        super(context);
        init();
    }

    public LoadingScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.loading_scale_layout, this);
        loadingImg1 = (ImageView) findViewById(R.id.view_loadingImg1);
        loadingImg2 = (ImageView) findViewById(R.id.view_loadingImg2);
        loadingImg3 = (ImageView) findViewById(R.id.view_loadingImg3);
    }
    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE)
            startAnimation();
        else
            stopAnimation();
    }
    /**
     * 开始动画
     */
    public void startAnimation() {
        if (loadingImg1 == null || loadingImg2 == null || loadingImg3 == null)
            return;
        AnimatorSet set1 = buildObjectAnimator(loadingImg1, 0.2f, 2.0F);
        set1.setDuration(300);
        AnimatorSet set2 = buildObjectAnimator(loadingImg2, 0.2f, 2.0F);
        set2.setDuration(400);
        AnimatorSet set3 = buildObjectAnimator(loadingImg3, 0.2f, 2.0F);
        set3.setDuration(500);
        set = new AnimatorSet();
        set.playTogether(set1, set2, set3);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                set.start();
            }
        });
        set.start();
    }
    public void stopAnimation() {
        if (set != null) {
            set.removeAllListeners();
            set.cancel();
            set = null;
        }
    }

    private AnimatorSet buildObjectAnimator(View view, float start, float end) {
        //放大+透明动画
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, View.SCALE_X, start, end);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, start, end);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, View.ALPHA, 0.0f, 1.0F);
        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(animator1, animator2, animator3);
        //缩小+透明动画
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(view, View.SCALE_X, end, 0.2f);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(view, View.SCALE_Y, end, 0.2f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(animator4, animator5, animator6);

        AnimatorSet set3 = new AnimatorSet();
        set3.playSequentially(set1, set2);
        set3.setInterpolator(new LinearInterpolator());

        return set3;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }
}
