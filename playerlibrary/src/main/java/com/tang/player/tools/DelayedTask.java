package com.tang.player.tools;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author txf
 * @Title
 * @package com.wisdomag.tools
 * @date 2016/11/18 0018
 */

public class DelayedTask {

    private CarouselTimerTask mTimerTask;
    private Timer mTimer;

    public interface OnDelayedTaskListener {
        void onHandleMessage(int msg);
    }

    private OnDelayedTaskListener l;

    public void setOnDelayedTaskListener(OnDelayedTaskListener l) {
        this.l = l;
    }

    public DelayedTask(OnDelayedTaskListener t) {
        this.l = t;
    }

    //开始延时任务
    public void startForwardTimer(long delay, int msg) {
        stopForwardTimer();
        if (mTimerTask == null) {
            mTimerTask = new CarouselTimerTask();
        }
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimerTask.setMsg(msg);
        mTimer.schedule(mTimerTask, delay);
    }

    public void stopForwardTimer() {
        if (mTimerTask != null) {
            mTimerTask.cancel(); // 将原任务从队列中移除
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private class CarouselTimerTask extends TimerTask {
        int msg;

        public void setMsg(int msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            //释放计时器
            mHandler.sendEmptyMessage(msg);
        }
    }

    /**/
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (l != null)
                l.onHandleMessage(msg.what);
        }
    };
}
