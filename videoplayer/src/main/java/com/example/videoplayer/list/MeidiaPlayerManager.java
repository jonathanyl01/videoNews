package com.example.videoplayer.list;

import android.content.Context;
import android.view.Surface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class MeidiaPlayerManager {

    private static  MeidiaPlayerManager sInstance;
    private Context context;
    private List<OnPlaybackListener> onPlaybackListener;//接口集合
    private MediaPlayer mediaPlayer;
    private boolean needRelease = false;//是否需要释放
    private String videoId;//视频id（用来区分当前操作谁）




    public static MeidiaPlayerManager getsInstance(Context context) {

        if (sInstance == null){
            sInstance = new MeidiaPlayerManager(context);

        }
        return sInstance;

    }

    private MeidiaPlayerManager(Context context) {
        this.context = context;
        Vitamio.isInitialized(context);
        onPlaybackListener = new ArrayList<>();

    }
    //获取视频id
    public String getVideoId(){

        return videoId;
    }
    //初始化mediaplayer
    public void onResume(){
        mediaPlayer = new MediaPlayer(context);
        //准备监听，设置缓存大小
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer.setBufferSize(1024 * 512);
                mediaPlayer.start();
            }
        });
        //设置播放完监听，停止播放，并且更新UI
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopPlayer();
            }
        });
        //监听视频大小的改变，更新UI
        mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                if (width == 0||height == 0) return;
                changeVideoSize(width,height);
            }
        });
        //监听info，获取缓冲状态，更新UI
        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                switch (what){
                    case MediaPlayer.MEDIA_INFO_FILE_OPEN_OK:
                        //Vitamio音频处理
                        mediaPlayer.audioInitedOk(mediaPlayer.audioTrackInit());
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        startBuffering();//缓冲开始的方法
                        return true;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        endBuffering();//缓冲结束的方法
                        return true;

                }
                return false;


            }
        });

    }

    private void endBuffering() {
        mediaPlayer.start();
        for (OnPlaybackListener listener : onPlaybackListener){
            listener.onStopBuffering(videoId);
        }

    }

    //缓冲开始，更新UI

    private void startBuffering() {

        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        //通知UI更新
        for (OnPlaybackListener listener: onPlaybackListener){
            listener.onStartBuffering(videoId);
        }
    }

    //调整更改视频尺寸
    private void changeVideoSize(int width, int height) {
        //通知UI更新
        for (OnPlaybackListener listener : onPlaybackListener){
            listener.onSizeMeasured(videoId,width,height);
        }

    }

    //释放mediaplayer
    public void onPause(){
        stopPlayer();
        if (needRelease){
            mediaPlayer.release();

        }
        mediaPlayer = null;


    }
    private long startTime;//用于避免用户频繁的开关视频
    //开始播放，更新UI
    public void startPlayer(Surface surface,String path,String videoId){

        //避免用户频繁的操作开关视频
        if (System.currentTimeMillis() - startTime<300)return;
        //当前是否有其他视频正在播放，有则停止播放
        if (this.videoId != null){
            stopPlayer();
        }
        //更新当前视频id
        this.videoId = videoId;
        //通知UI更新
        for (OnPlaybackListener listener : onPlaybackListener){
            listener.onStartPlay(videoId);
        }
        //准备播放

        try {
            mediaPlayer.setDataSource(path);
            needRelease = true;
            mediaPlayer.setSurface(surface);
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //停止播放，更新UI（通过接口CallBack）
    public void stopPlayer(){

        if (videoId == null)return;
        //通知UI更新
        for (OnPlaybackListener listener : onPlaybackListener){
            listener.onStopPlay(videoId);
        }
        //停止播放，并且重置
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();

    }
    //添加播放处理的监听
    public void addPlayerListener(OnPlaybackListener listener){
        onPlaybackListener.add(listener);

    }
    //移除监听
    public void removeAllListener(){
        onPlaybackListener.clear();
    }

    //视图接口
    //视频播放模块完成播放处理，视图层来实现此接口，完成视图层UI更新
    public interface OnPlaybackListener{

        void onStartBuffering(String videoId);//视频缓冲开始
        void onStopBuffering(String videoId);//视频缓冲结束
        void onStartPlay(String videoId);//开始播放
        void onStopPlay(String videoId);//停止播放
        void onSizeMeasured(String videoId,int width,int height);//大小更改


    }


}
