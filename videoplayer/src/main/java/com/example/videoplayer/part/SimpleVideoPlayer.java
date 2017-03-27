package com.example.videoplayer.part;


import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.videoplayer.R;
import com.example.videoplayer.full.VideoViewActivity;

import java.io.IOException;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class SimpleVideoPlayer extends FrameLayout {

    //控制进度条长度
    private static final int PROGRFESS_MAX = 1000;
    private String videoPath;
    private MediaPlayer mediaPlayer;
    private boolean isPrepared;
    private boolean isPlaying;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView ivPreView;
    private ImageButton btnToggle;
    private ProgressBar progressBar;

    public SimpleVideoPlayer(Context context) {
        this(context, null);
    }

    public SimpleVideoPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        Vitamio.isInitialized(getContext());
        LayoutInflater.from(getContext()).
                inflate(R.layout.view_simple_video_player,this,true);

        initSurfaceView();
        initControllerViews();


    }

    //设置数据源
    public void setVideoPath(String videoPath) {

        this.videoPath = videoPath;


    }

    //初始化状态
    public void onResume() {
        initMdiaPlayer();
        prepareMediaPlayer();

    }

    //准备MediaPlayer
    private void prepareMediaPlayer() {

        //重置MediaPlayer
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(videoPath);
            //设置循环播放
            mediaPlayer.setLooping(true);
            mediaPlayer.prepareAsync();
            ivPreView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //初始化MediaPlayer

    private void initMdiaPlayer() {
        mediaPlayer = new MediaPlayer(getContext());
        mediaPlayer.setDisplay(surfaceHolder);
        //准备监听
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                startMediaPlayer();
            }
        });
        //audio处理
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_FILE_OPEN_OK) {
                    mediaPlayer.audioInitedOk(mediaPlayer.audioTrackInit());
                    return true;

                }
                return false;
            }
        });
        //视频大小改变监听
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                //参数的宽和高是指视频的宽和高，可以通过参数去设置Surfaceview的宽高
                int layoutWidth = surfaceView.getWidth();
                int layoutHeight = layoutWidth * height / width;
                //更新surfaceview的宽高
                ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
                params.width = layoutWidth;
                params.height = layoutHeight;
                surfaceView.setLayoutParams(params);
            }
        });
    }

    private void startMediaPlayer() {
        ivPreView.setVisibility(View.INVISIBLE);
        btnToggle.setImageResource(R.drawable.ic_pause);
        mediaPlayer.start();
        isPlaying = true;
        //进度条操作
        handler.sendEmptyMessage(0);
    }

    //视频暂停
    private void pauseMediaPlayer() {
        if (isPlaying) {

            mediaPlayer.pause();
        }
        isPlaying = false;
        btnToggle.setImageResource(R.drawable.ic_play_arrow);
        handler.removeMessages(0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isPlaying) {
                int progress = (int) (mediaPlayer.getCurrentPosition() *
                        PROGRFESS_MAX / mediaPlayer.getDuration());

                //发送有个空的消息，不停的调用本身，使其自动更新进度条
                handler.sendEmptyMessageDelayed(0, 200);
            }
        }
    };
    //释放状态

    public void onPause() {
        pauseMediaPlayer();
        releaseMediaPlayer();

    }
    //释放MediaPlayer

    private void releaseMediaPlayer() {
        mediaPlayer.release();
        mediaPlayer = null;
        isPrepared = false;
        progressBar.setProgress(0);

    }


    private void initSurfaceView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        surfaceHolder.setFormat(PixelFormat.RGBA_8888);

    }

    private void initControllerViews() {
        //预览图
        ivPreView = (ImageView) findViewById(R.id.ivPreview);
        //播放，暂停
        btnToggle = (ImageButton) findViewById(R.id.btnToggle);
        btnToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {

                    pauseMediaPlayer();
                } else if (isPrepared) {

                    startMediaPlayer();
                } else {
                    Toast.makeText(getContext(), "can't play now!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        //设置进度条
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(PROGRFESS_MAX);
        //全屏播放按钮
        findViewById(R.id.btnFullScreen).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                VideoViewActivity.open(getContext(),videoPath);

            }
        });


    }
}
