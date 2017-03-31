package com.tang.player.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.tang.player.listeners.IPlayerSurfaceCallback;

/**
 * @author txf
 * @Title
 * @package com.tang.player
 * @date 2017/3/10 0010
 */

public class PlayerSurface extends SurfaceView implements SurfaceHolder.Callback {
    private final String TAG = getClass().getName();
    public static final int SCAN_TYPE_DEFAULT = 0;
    public static final int SCAN_TYPE_CENTER_CROP = 1;
    public static final int SCAN_TYPE_FIT_CENTER = 2;
    public static final int SCAN_TYPE_FIT_XY = SCAN_TYPE_DEFAULT;

    private boolean isReady;
    private SurfaceHolder mHolder;
    private IPlayerSurfaceCallback mPlayerSurfaceListener;
    private int scanType;
    private int mSurfaceWidth;
    private int mSurfaceHeight;
    public PlayerSurface(Context context, IPlayerSurfaceCallback l) {
        super(context);
        init(l);
    }
    public PlayerSurface(Context context, AttributeSet attrs, IPlayerSurfaceCallback l) {
        super(context, attrs);
        init(l);
    }
    public PlayerSurface(Context context, AttributeSet attrs, int defStyleAttr, IPlayerSurfaceCallback l) {
        super(context, attrs, defStyleAttr);
        init(l);
    }
    private void init(IPlayerSurfaceCallback l) {
        mPlayerSurfaceListener = l;
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        setZOrderOnTop(true);
        setZOrderMediaOverlay(true);
    }

    public int getSurfaceHeight() {
        return mSurfaceHeight;
    }

    public int getSurfaceWidth() {
        return mSurfaceWidth;
    }

    public void setScanType(int type) {
        this.scanType = type;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //屏幕大小
        int width = getDefaultSize(mSurfaceWidth, widthMeasureSpec);
        int height = getDefaultSize(mSurfaceHeight, heightMeasureSpec);
        //竖直播放
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            changeZoomType(SCAN_TYPE_FIT_CENTER, mSurfaceWidth, mSurfaceHeight, width, height);

            if (scanType == SCAN_TYPE_DEFAULT) {
                setMeasuredDimension(width, width * 9 / 16);

            } else {
                setMeasuredDimension(width, width * 3 / 4);
            }
            return;
        }
        changeZoomType(scanType, mSurfaceWidth, mSurfaceHeight, width, height);
    }
    private void changeZoomType(int type, int videoWidth, int videoHeight, int screenWidth, int screenHeight) {
        //默认缩放
        if (type == SCAN_TYPE_FIT_XY) {
            setMeasuredDimension(screenWidth, screenHeight);
            return;
        }
        if (videoWidth <= 0 || videoHeight <= 0) {
            setMeasuredDimension(screenWidth, screenHeight);//铺满
            return;
        }
        if (type == SCAN_TYPE_CENTER_CROP) {
            if (videoWidth > screenWidth || videoHeight > screenHeight) {
                float scan_x = videoWidth / (screenWidth * 1.0f);
                float scan_y = videoHeight / (screenHeight * 1.0f);
                if (scan_x < scan_y) {
                    screenWidth = (int) (videoHeight * scan_x);
                } else {
                    screenHeight = (int) (videoWidth * scan_y);
                }
                setMeasuredDimension(screenWidth, screenHeight);
                return;
            }
            if (videoWidth < screenWidth || videoHeight < screenHeight) {//有一边小于屏幕
                //缩放比例
                float scan_x = screenWidth / (videoWidth * 1.0f);
                float scan_y = screenHeight / (videoHeight * 1.0f);

                if (scan_x < scan_y) {
                    screenWidth = (int) (videoHeight * scan_x);
                } else {
                    screenHeight = (int) (videoWidth * scan_y);
                }
            }
            setMeasuredDimension(screenWidth, screenHeight);
            return;
        }

        if (type == SCAN_TYPE_FIT_CENTER) {
            if (videoWidth > screenWidth || videoHeight > screenHeight) {
                float scan_x = videoWidth / (screenWidth * 1.0f);
                float scan_y = videoHeight / (screenHeight * 1.0f);

                if (scan_x < scan_y) {
                    screenHeight = (int) (videoHeight * scan_x);
                } else {
                    screenWidth = (int) (videoWidth * scan_y);
                }
                setMeasuredDimension(screenWidth, screenHeight);
                return;
            }
            if (videoWidth < screenWidth || videoHeight < screenHeight) {//有一边小于屏幕
                //缩放比例
                float scan_x = screenWidth / (videoWidth * 1.0f);
                float scan_y = screenHeight / (videoHeight * 1.0f);
                if (scan_x < scan_y) {
                    screenHeight = (int) (videoHeight * scan_x);
                } else {
                    screenWidth = (int) (videoWidth * scan_y);
                }
            }
            setMeasuredDimension(screenWidth, screenHeight);
            return;
        }
    }
    /**
     * SurfaceHolder 创建完成?
     * */
    public boolean isReady() {
        return isReady;
    }
    /**
     * 根据视频源分辨率 设置适合的surfaceView大小
     * */
    public void updateVideoSize(int width, int height) {
        Log.i(TAG, "--->>UN::SURFACE 计算ui容器大小;采用根据视频大小计算 视频宽度:" + mSurfaceWidth + ", 视频高度:" + mSurfaceHeight);
        this.mSurfaceWidth = width;
        this.mSurfaceHeight = height;
        getHolder().setFixedSize(width, height);
        requestLayout();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        isReady = true;
        Log.i(TAG, "原始侦听  SurfaceHolder 创建成功");
        mPlayerSurfaceListener.surfaceCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "原始侦听  surfaceChanged 表面发生改变" + " format :" + format + " width ; " + width + " height: " + height);
        mPlayerSurfaceListener.surfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHolder = null;
        isReady = false;
        Log.i(TAG, "原始侦听  SurfaceHolder 销毁成功");
        mPlayerSurfaceListener.surfaceDestroyed(holder);
    }
}
