package com.example.administrator.myvideonews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myvideonews.ui.ui.likes.LinkesFragment;
import com.example.administrator.myvideonews.ui.ui.local.LocalVideoFragment;
import com.example.administrator.myvideonews.ui.ui.news.NewsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btnNews)
    Button btnNews;
    @BindView(R.id.btnLocal)
    Button btnLocal;
    @BindView(R.id.btnLikes)
    Button btnLikes;
    private Unbinder unBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unBind = ButterKnife.bind(this);
        initView();


    }

    private void initView() {

        Log.e("viewPager===",""+viewPager);
        Log.e("adapter===",""+adapter);
        viewPager.setAdapter(adapter);
        //viewpager监听Button切换
        viewPager.addOnPageChangeListener(listener);
        //首次进入默认选中在线新闻btn
        btnNews.setSelected(true);


    }



 FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    return new NewsFragment();
                case 1:
                    return new LocalVideoFragment();
                case 2:
                    return new LinkesFragment();
                default:
                    throw new RuntimeException("未知错误");

            }


        }

        @Override
        public int getCount() {
            return 3;
        }
    };

   private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
       @Override
       public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

       }

       @Override
       public void onPageSelected(int position) {

           //Button，UI改变
           btnNews.setSelected(position == 0);
           btnLocal.setSelected(position == 1);
           btnLikes.setSelected(position == 2);

       }

       @Override
       public void onPageScrollStateChanged(int state) {

       }
   };


    @OnClick({ R.id.btnNews, R.id.btnLocal, R.id.btnLikes})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnNews:
                //不要平滑效果，第二参数穿false
                viewPager.setCurrentItem(0,false);
                break;
            case R.id.btnLocal:
                viewPager.setCurrentItem(1,false);
                break;
            case R.id.btnLikes:
                viewPager.setCurrentItem(2,false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBind.unbind();
    }
}
