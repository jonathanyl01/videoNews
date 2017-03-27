package com.example.videoplayer.full;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videoplayer.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class VideoViewActivity extends AppCompatActivity {

    private static final String KEY_VIDEO_PATH = "video_path";

    public static void open(Context context,String videoPath){

        Intent intent = new Intent(context,VideoViewActivity.class);
        intent.putExtra(KEY_VIDEO_PATH,videoPath);
        context.startActivity(intent);

    }



    private VideoView videoView;
    private ImageView ivLoading;
    private TextView tvBufferInfo;
    private MediaPlayer mediaPlayer;
    private int bufferPercent;
    private int downloadSpeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        setContentView(R.layout.activity_video_view);
        //初始化缓冲相关视图
        initBufferView();
        //初始化videoview
        initVideoView();
    }
    //设置数据源


    @Override
    protected void onResume() {
        super.onResume();
        videoView.setVideoPath(getIntent().getStringExtra(KEY_VIDEO_PATH));
    }
    //停止播放


    @Override
    protected void onPause() {
        super.onPause();
        videoView.stopPlayback();
    }

    private void initVideoView() {

        Vitamio.isInitialized(this);
        videoView = (VideoView) findViewById(R.id.videoView);

        videoView.setMediaController(new CustomerMediaController(this));
        videoView.setKeepScreenOn(true);//设置屏幕长亮
        videoView.requestFocus();//拿到焦点
        //缓冲第一步，准备监听，设置缓冲大小
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                mediaPlayer.setBufferSize(1024 * 512);
            }
        });
        //缓冲第二部，信息监听，监听缓冲状态，拿到缓冲速度
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                switch (what){
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        showBufferView();
                        if (videoView.isPlaying()){
                            videoView.pause();

                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        hideBufferView();
                        videoView.start();
                        break;
                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                        downloadSpeed = extra;
                        upDateBufferView();
                        break;

                }
                return false;
            }
        });

        //缓冲第三部，拿到缓冲百分比

        videoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                bufferPercent = percent;
                upDateBufferView();
            }
        });

    }

    private void upDateBufferView() {
        String info = bufferPercent + "%" + downloadSpeed + "kb/s";
        tvBufferInfo.setText(info);

    }

    private void showBufferView() {
        tvBufferInfo.setVisibility(View.VISIBLE);
        ivLoading.setVisibility(View.VISIBLE);
        downloadSpeed = 0;
        bufferPercent = 0;

    }

    private void hideBufferView(){
        tvBufferInfo.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);

    }

    private void initBufferView() {

        tvBufferInfo = (TextView) findViewById(R.id.tvBufferInfo);
        ivLoading = (ImageView) findViewById(R.id.ivLoading);
        tvBufferInfo.setVisibility(View.INVISIBLE);
        ivLoading.setVisibility(View.INVISIBLE);
    }
}
