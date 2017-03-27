package com.example.videoplayer.full;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.videoplayer.R;

import io.vov.vitamio.widget.MediaController;

/**
 * Created by Administrator on 2017/3/15 0015.
 */

public class CustomerMediaController extends MediaController {
    //视频播放控制接口
    private MediaController.MediaPlayerControl mediaPlayerControl;
    //音频管理
    private AudioManager audioManager;
    //用于管理视频亮度
    private Window window;
    //最大音量
    private int maxValoume;
    //当前音量
    private int currentVolume;
    //当前亮度
    private float currentBrightness;

    public CustomerMediaController(Context context) {
        super(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxValoume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        window = ((Activity)context).getWindow();

    }

    @Override
    protected View makeControllerView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_custom_video_controller,this);
        initView(view);
        return view;
    }

    //获取播放控制接口
    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        super.setMediaPlayer(player);
        mediaPlayerControl = player;
    }


    //初始化视图，设置监听
    //快进快退的监听
    //屏幕亮度，音量的控制
    private void initView(View view) {
        //快进
        findViewById(R.id.btnFastForward).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //拿到当前播放位置
                long posistion = mediaPlayerControl.getCurrentPosition();
                //快进十秒
                posistion += 10000;
                //如果快进10秒后，大于等于总的视频长度，则到头
                if (posistion>=mediaPlayerControl.getDuration()){

                    posistion = mediaPlayerControl.getDuration();
                }
                //否则移动到快进位置
                mediaPlayerControl.seekTo(posistion);
            }
        });
        //快退
        findViewById(R.id.btnFastRewind).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                long posistion = mediaPlayerControl.getCurrentPosition();

                posistion -= 10000;

                if (posistion<0){

                    posistion = 0;
                }

                mediaPlayerControl.seekTo(posistion);

            }
        });

        //调整视图（左边调亮度，右边调音量）
        final View adjudtView = findViewById(R.id.adjustView);
        //依赖手势识别类，来进行手势处理
        final GestureDetector gestureDetector = new GestureDetector(
                getContext(),new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float startX = e1.getX();//开始X轴的坐标
                float startY = e1.getY();
                float endX = e2.getX();
                float endY = e2.getY();

                float width = adjudtView.getWidth();//调整视图的宽
                float height = adjudtView.getHeight();//调整视图的高
                float percentage = (startY - endY)/height;//高度滑动的百分比

                //左侧调整亮度（调整视图左侧的1/5）
                if (startX<width/5){
                    adjustBrightness(percentage);
                }
                //右侧调整音量
                if (startX>width*4/5){
                    adjustVolume(percentage);

                }

                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        //对adjustView（调整视图），进行touch监听
        adjudtView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当用户按下的时候
                if ((event.getAction()&MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
                    //拿到当前的音量
                    currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //拿到当前的亮度
                    currentBrightness = window.getAttributes().screenBrightness;

                }
                //交给手势识别类去做
                gestureDetector.onTouchEvent(event);
                //在调整过程中，自定义控制器一直显示
                show();

                return true;
            }
        });

    }

    private void adjustVolume(float percentage) {

        //最终音量 = 最大音量 * 改变的白分比 + 当前音量
        int volume = (int) (maxValoume * percentage + currentVolume);
        //如果最终音量大于最大音量，结果为最大音量
        volume = volume>maxValoume?maxValoume:volume;
        //如果最终音量小于0，结果为0
        volume = volume<0?0:volume;
        //设置音量
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,volume,AudioManager.FLAG_SHOW_UI);

    }
    //调整亮度（最小亮度 = 0，最大亮度 = 1.0f）
    private void adjustBrightness(float percentage){
        //最终亮度 = percentage + 当前亮度
        float brightness = percentage + currentBrightness;
        brightness = brightness > 1.0f ? 1.0f : brightness;
        brightness = brightness < 0 ? 0 : brightness;
        //设置亮度
        WindowManager.LayoutParams layoutParam = window.getAttributes();
        layoutParam.screenBrightness = brightness;
        window.setAttributes(layoutParam);


    }
}
