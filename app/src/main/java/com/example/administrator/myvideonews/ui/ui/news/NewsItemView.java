package com.example.administrator.myvideonews.ui.ui.news;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.myvideonews.R;
import com.example.administrator.myvideonews.commons.CommonUtils;
import com.example.administrator.myvideonews.ui.ui.base.BaseItemView;
import com.example.administrator.myvideonews.ui.ui.entity.NewsEntity;
import com.example.videoplayer.list.MeidiaPlayerManager;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class NewsItemView extends BaseItemView<NewsEntity> implements MeidiaPlayerManager.OnPlaybackListener ,TextureView.SurfaceTextureListener {
    @BindView(R.id.textureview)

    TextureView textureview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.ivPlay)
    ImageView ivPlay;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.tvCreatedAt)
    Button tvCreatedAt;

    private NewsEntity newsEntity;
    private MeidiaPlayerManager meidiaPlayerManager;
    private Surface surface;

    public NewsItemView(Context context) {
        super(context);
    }

    @Override
    protected void initView() {

        LayoutInflater.from(getContext()).inflate(R.layout.item_news, this, true);
        ButterKnife.bind(this);
        meidiaPlayerManager = MeidiaPlayerManager.getsInstance(getContext());

        meidiaPlayerManager.addPlayerListener(this);
        textureview.setSurfaceTextureListener(this);

    }

    @Override
    protected void BindModel(NewsEntity newsEntity) {
        this.newsEntity = newsEntity;

        tvNewsTitle.setVisibility(View.VISIBLE);
        ivPreview.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);

        tvNewsTitle.setText(newsEntity.getNewsTitle());
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));

        String url = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        Picasso.with(getContext()).load(url).into(ivPreview);


    }

    @OnClick({R.id.textureview, R.id.ivPreview, R.id.ivPlay,R.id.tvCreatedAt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCreatedAt:
                // TODO: 2017/3/25 0025 跳转到评论页面
                break;
            case R.id.textureview:
                meidiaPlayerManager.stopPlayer();
                break;
            case R.id.ivPreview:
                if (surface == null){

                    String path = newsEntity.getVideoUrl();
                    String videoId = newsEntity.getObjectId();
                    meidiaPlayerManager.startPlayer(surface,path,videoId);
                }

                break;
            case R.id.ivPlay:
                break;
        }
    }

    //判断是否当前视频
    private boolean isCurrentVideo(String videoId){
        if (videoId == null||newsEntity == null)return false;
        return videoId.equals(newsEntity.getObjectId());

    }

    @Override
    public void onStartBuffering(String videoId) {

        if (isCurrentVideo(videoId)){

            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStopBuffering(String videoId) {

        if (isCurrentVideo(videoId)){

            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onStartPlay(String videoId) {

        if (isCurrentVideo(videoId)){
            tvNewsTitle.setVisibility(View.INVISIBLE);
            ivPreview.setVisibility(View.INVISIBLE);
            ivPlay.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);

        }

    }

    @Override
    public void onStopPlay(String videoId) {
        tvNewsTitle.setVisibility(View.VISIBLE);
        ivPreview.setVisibility(View.VISIBLE);
        ivPlay.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onSizeMeasured(String videoId, int width, int height) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {


        this.surface = new Surface(surface);



    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        this.surface.release();
        this.surface = null;
        //停止自己
        if (newsEntity.getObjectId().equals(meidiaPlayerManager.getVideoId())){

            meidiaPlayerManager.stopPlayer();

        }
        return false;


    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
