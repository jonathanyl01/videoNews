package com.example.administrator.myvideonews.ui.ui.news;

import android.content.Context;
import android.util.AttributeSet;

import com.example.administrator.myvideonews.bombapi.result.QueryResult;
import com.example.administrator.myvideonews.ui.ui.base.BaseResourceView;
import com.example.administrator.myvideonews.ui.ui.entity.NewsEntity;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/3/25 0025.
 */

public class NewsListView extends BaseResourceView<NewsEntity,NewsItemView> {
    public NewsListView(Context context) {
        super(context);
    }
    public NewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected NewsItemView createItemView() {
        return new NewsItemView(getContext());
    }

    @Override
    protected Call<QueryResult<NewsEntity>> queryData(int limit, int skip) {
        return newsApi.getVideoNewsList(limit,skip);
    }

    @Override
    protected int getLimit() {
        return 5;
    }
}
