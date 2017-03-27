package com.example.administrator.myvideonews.ui.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myvideonews.R;
import com.example.videoplayer.list.MeidiaPlayerManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/15 0015.
 */
public class NewsFragment extends Fragment {

    @BindView(R.id.newsListView)
    NewsListView newsListView;
    private View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_news, container, false);

        }

        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        newsListView.post(new Runnable() {
            @Override
            public void run() {
                newsListView.autoRefresh();
            }
        });

    }

    //初始化MediaPlayer
    @Override
    public void onResume() {
        super.onResume();
        MeidiaPlayerManager.getsInstance(getContext()).onResume();
    }

    //Mediaplayer释放

    @Override
    public void onPause() {
        super.onPause();
        MeidiaPlayerManager.getsInstance(getContext()).onPause();
    }
    //移除View


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)view.getParent()).removeView(view);

    }
    //清楚所有监听


    @Override
    public void onDestroy() {
        super.onDestroy();
        MeidiaPlayerManager.getsInstance(getContext()).removeAllListener();
    }
}
