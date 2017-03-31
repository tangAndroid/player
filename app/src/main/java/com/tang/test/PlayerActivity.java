package com.tang.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tang.player.beans.MediaBean;
import com.tang.player.ui.TVideoView;


public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        String uri = getIntent().getExtras().getString(MainActivity.JUMP_DATA);
        testVideo(uri);
    }
    private void testVideo(String uri) {
        TVideoView mTVideoView = (TVideoView) findViewById(R.id.mTVideoView);
        MediaBean bean = new MediaBean();
        bean.setUrl(uri);
        mTVideoView.setData(bean);
    }
}
