package com.tang.test;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author txf
 * @Title
 * @package com.tang.player
 * @date 2017/3/31 0031
 */

public class JumpDialog extends Dialog implements View.OnClickListener {
    private EditText mEditText;
    private int sign;
    private Toast mToast;
    private String mHintText;

    public JumpDialog(Context context) {
        super(context);
    }

    public JumpDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_jump_layout);
        initViews();
        initListener();
    }
    public void setHint(String hintText) {
        this.mHintText = hintText;
    }
    private void initListener() {
        findViewById(R.id.mButton_confirm).setOnClickListener(this);
        findViewById(R.id.mButton_cancel).setOnClickListener(this);
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public int getSign() {
        return sign;
    }

    private void initViews() {
        mEditText = (EditText) findViewById(R.id.mEditText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mButton_confirm:
                if (mEditText.getText().toString().isEmpty()) {
                    toast("请输入跳转所需数据");
                    return;
                }
                if (l != null)
                    l.confirm(v, mEditText.getText().toString(), getSign());
                break;
            case R.id.mButton_cancel:
                if (l != null)
                    l.cancel(v, getSign());
                cancel();
                break;
        }
    }

    @Override
    public void dismiss() {
        mEditText.setText("");
        stopToase();
        super.dismiss();
    }

    private void toast(String msg) {
        if (mToast == null)
            mToast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        else {
            mToast.setText(msg);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void stopToase() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    onJumpDialogListener l;

    public void setOnJumpDialogListener(onJumpDialogListener l) {
        this.l = l;
    }

    public interface onJumpDialogListener {
        void confirm(View v, String data, int sign);

        void cancel(View v, int sign);
    }

    @Override
    public void show() {
        super.show();
        refreshTheme();
        if (!mHintText.isEmpty())
            mEditText.setHint(mHintText);
    }

    private void refreshTheme() {
        Window window = this.getWindow();
        window.setBackgroundDrawable(new ColorDrawable());
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }
}
