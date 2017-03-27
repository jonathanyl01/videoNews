package com.example.administrator.myvideonews.ui.ui.base;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.administrator.myvideonews.R;
import com.example.administrator.myvideonews.bombapi.BombClient;
import com.example.administrator.myvideonews.bombapi.NewsApi;
import com.example.administrator.myvideonews.bombapi.result.QueryResult;
import com.example.administrator.myvideonews.commons.ToastUtils;
import com.mugen.Mugen;
import com.mugen.MugenCallbacks;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public abstract class BaseResourceView<Model,ItemView extends BaseItemView<Model>> extends FrameLayout
        implements SwipeRefreshLayout.OnRefreshListener,
        MugenCallbacks{
    public BaseResourceView(Context context) {
        super(context,null);
    }

    public BaseResourceView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public BaseResourceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    //让使用者重写此方法，确定ItemView的视图
    protected abstract ItemView createItemView();
    //从服务器查询数据
    protected abstract Call<QueryResult<Model>> queryData(int limit,int skip);
    //每页从服务器获取多少数据
    protected abstract int getLimit();
    @BindView(R.id.recyclerview)RecyclerView recyclerview;
    @BindView(R.id.swiperefreshLayout)SwipeRefreshLayout swipRefreshLayout;
    @BindView(R.id.progressBar)ProgressBar progressbar;
    protected ModelAdapter adapter;
    //跳过多少条数据
    private int skip = 0;
    //是否已经加载完所有数据
    private boolean loadAll;
    protected NewsApi newsApi;

    private void initView() {
        newsApi = BombClient.getInstance().getNewsApi();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_base,this,true);

        ButterKnife.bind(this);

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ModelAdapter();
        recyclerview.setAdapter(adapter);


        swipRefreshLayout.setOnRefreshListener(this);
        swipRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        Mugen.with(recyclerview,this).start();


    }


    //自动刷新

    public void autoRefresh(){
        swipRefreshLayout.setRefreshing(true);
        onRefresh();

    }
    @Override
    public void onRefresh() {
        //刷新操作

        Call<QueryResult<Model>> call = queryData(getLimit(),0);
        if (call == null){

            swipRefreshLayout.setRefreshing(false);
            return;
        }

        call.enqueue(new Callback<QueryResult<Model>>() {
            @Override
            public void onResponse(Call<QueryResult<Model>> call, Response<QueryResult<Model>> response) {
                swipRefreshLayout.setRefreshing(false);
                //拿到响应数据
                List<Model> datas = response.body().getResults();
                //改变跳过数目
                skip = datas.size();
                //判断是否获取完服务器数据
                loadAll = datas.size()< getLimit();

                adapter.clear();
                adapter.addData((ArrayList<Model>) datas);


            }

            @Override
            public void onFailure(Call<QueryResult<Model>> call, Throwable t) {

                swipRefreshLayout.setRefreshing(false);
                ToastUtils.showShort("onFailure"+t.getMessage());

            }
        });


    }

    @Override
    public void onLoadMore() {
        //加载操作

        //拿到获取数据的请求
        Call<QueryResult<Model>> call = queryData(getLimit(),skip);

        if (call == null){
            ToastUtils.showShort("查询条件异常");
            return;

        }

        progressbar.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<QueryResult<Model>>() {
            @Override
            public void onResponse(Call<QueryResult<Model>> call, Response<QueryResult<Model>> response) {
                progressbar.setVisibility(View.INVISIBLE);

                List<Model> datas = response.body().getResults();

                skip = skip + datas.size();

                loadAll = datas.size()<getLimit();

                adapter.addData((ArrayList<Model>) datas);


            }

            @Override
            public void onFailure(Call<QueryResult<Model>> call, Throwable t) {

                progressbar.setVisibility(View.INVISIBLE);
                ToastUtils.showShort("onFailure"+t.getMessage());
            }
        });



    }

    @Override
    public boolean isLoading() {
        return progressbar.getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return loadAll;
    }

    protected class ModelAdapter extends RecyclerView.Adapter{


        private ArrayList<Model> datas = new ArrayList<>();

        public void clear(){
            datas.clear();
            notifyDataSetChanged();

        }
        public void addData(ArrayList<Model> datas){

            datas.addAll(datas);
            notifyDataSetChanged();


        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemView itemView = createItemView();

            return new RecyclerView.ViewHolder(itemView) {};
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            Model model = datas.get(position);
            ItemView itemView = (ItemView) holder.itemView;
            itemView.BindModel(model);

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }
    }




}
