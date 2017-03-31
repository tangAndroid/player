package com.tang.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, JumpDialog.onJumpDialogListener {
    JumpDialog mJumpDialog;
    public final static String JUMP_DATA = "JUMP_DATA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initJumpDialog();
        initListener();
    }
    private void initJumpDialog() {
        mJumpDialog = new JumpDialog(this);
        mJumpDialog.setOnJumpDialogListener(this);
    }
    private void initListener() {
        findViewById(R.id.test01).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test01:
                mJumpDialog.setHint("请输入播放链接");
                mJumpDialog.setSign(1);
                mJumpDialog.show();
                break;
        }
    }

    @Override
    public void confirm(View v, String data, int sign) {
        switch (sign) {
            case 1:
                Intent i = new Intent(this, PlayerActivity.class);
                i.putExtra(JUMP_DATA, data);
                startActivity(i);
                mJumpDialog.cancel();
                break;
        }
    }
    @Override
    public void cancel(View v, int sign) {

    }
}
