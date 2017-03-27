package com.example.administrator.myvideonews.ui.ui.likes;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.myvideonews.R;
import com.example.administrator.myvideonews.commons.CommonUtils;
import com.example.administrator.myvideonews.ui.ui.base.BaseItemView;
import com.example.administrator.myvideonews.ui.ui.entity.NewsEntity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class LikesItemView extends BaseItemView<NewsEntity> {
    public LikesItemView(Context context) {
        super(context);
    }


    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.tvNewsTitle)
    TextView tvNewsTitle;
    @BindView(R.id.tvCreatedAt)
    TextView tvCreatedAt;
    private NewsEntity newsEntity;
    @Override
    protected void initView() {

        LayoutInflater.from(getContext()).inflate(R.layout.layout_itemlikes,this,true);

        ButterKnife.bind(this);

    }

    @Override
    protected void BindModel(NewsEntity newsEntity) {

        this.newsEntity = newsEntity;
        tvNewsTitle.setText(newsEntity.getNewsTitle());
        tvCreatedAt.setText(CommonUtils.format(newsEntity.getCreatedAt()));

        String url = CommonUtils.encodeUrl(newsEntity.getPreviewUrl());
        Picasso.with(getContext()).load(url).into(ivPreview);
    }
    @OnClick
    public void onClick(){
        // TODO: 2017/3/22 0022 跳转到评论页面 
        
    }
}
