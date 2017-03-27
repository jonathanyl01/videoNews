package com.example.administrator.myvideonews.ui.ui.likes;

import android.content.Context;
import android.util.AttributeSet;

import com.example.administrator.myvideonews.UserManager;
import com.example.administrator.myvideonews.bombapi.BombConst;
import com.example.administrator.myvideonews.bombapi.other.InQuery;
import com.example.administrator.myvideonews.bombapi.result.QueryResult;
import com.example.administrator.myvideonews.ui.ui.base.BaseResourceView;
import com.example.administrator.myvideonews.ui.ui.entity.NewsEntity;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class LikesListView extends BaseResourceView<NewsEntity,LikesItemView> {
    public LikesListView(Context context) {
        super(context);
    }

    public LikesListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikesListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LikesItemView createItemView() {
        LikesItemView likesItemView = new LikesItemView(getContext());
        return likesItemView;
    }

    @Override
    protected Call<QueryResult<NewsEntity>> queryData(int limit, int skip) {


        String userId = UserManager.getInstance().getObjectId();
        //由于服务器原因造成的  可以直接使用

        BombConst bombConst = new BombConst();


        InQuery where = new InQuery(bombConst.getFIELD_LIKES(),bombConst.getTABLE_USER(),userId);

        return newsApi.getLikedList(limit,skip,where);
    }

    @Override
    protected int getLimit() {
        return 15;
    }
    //退出登录时清空收藏列表
    public void clear(){
        adapter.clear();
    }


}
