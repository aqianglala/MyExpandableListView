package com.example.zy1584.myexpandableview;

import android.animation.ValueAnimator;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnInstallButtonClickListener {

    private MainActivity context;
    private ListView mListView;
    private ArrayList<ItemBean> mData = new ArrayList<>();
    private MyAdapter mAdapter;
    private int mLastOpenPosition = -1;
    private MyAdapter.ViewHolder mLastOpenHolder;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initData();
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MyAdapter(mData, this);
        mAdapter.setOnButtonClickListener(this);
        mListView.setAdapter(mAdapter);
    }

    private void initData() {
        List<PackageInfo> packageInfos = getPackageManager().getInstalledPackages(0);
        for (PackageInfo info: packageInfos){
            String name = (String) info.applicationInfo.loadLabel(getPackageManager());
            mData.add(new ItemBean(name));
        }
    }

    private ArrayList<ItemBean> getRecommendList(){
        ArrayList<ItemBean> list = new ArrayList<>();
        int size = new Random().nextInt(3) + 1;
        for (int i = 0;i <size; i++){
            list.add(new ItemBean("推荐"+i));
        }
        Log.e(TAG, "getRecommendList: " + size );
        return list;
    }

    @Override
    public void onClick(int position, ItemBean itemBean, MyAdapter.ViewHolder holder) {
        // 处理此item的下载任务
        Toast.makeText(context, "下载中...", Toast.LENGTH_SHORT).show();
        if (mLastOpenPosition == position) return;
        // 获取推荐数据
        ArrayList<ItemBean> recommendList = getRecommendList();
        if ((recommendList != null && recommendList.size() > 0) &&
                (recommendList != null && recommendList.size() <= 3)){
            // 设置数据
            final LinearLayout ll_container = holder.ll_container;
            ll_container.findViewById(R.id.tv_recommend_title).setVisibility(View.VISIBLE);

            for (int i = 0;i <recommendList.size(); i++){
                ItemBean bean = recommendList.get(i);
                int itemId = getResources().getIdentifier("rl_" + i, "id", getPackageName());
                RelativeLayout itemView = ll_container.findViewById(itemId);

                TextView tv_name = itemView.findViewById(R.id.tv_name);
                tv_name.setText(bean.getName());
                Button btn_install = itemView.findViewById(R.id.btn_install);
                btn_install.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 处理推荐列表项的下载任务
                        Toast.makeText(context, "下载中...", Toast.LENGTH_SHORT).show();
                    }
                });
                itemView.setVisibility(View.VISIBLE);
            }
            if (recommendList.size() > 0 && recommendList.size() < 3){
                for (int i = recommendList.size(); i < 3; i++){
                    int itemId = getResources().getIdentifier("rl_" + i, "id", getPackageName());
                    ll_container.findViewById(itemId).setVisibility(View.GONE);
                }
            }
            hideLastItem(position);
            show(ll_container);
        }else{
            hideLastItem(position);
        }
        mLastOpenPosition = position;
        mLastOpenHolder = holder;
    }

    private static final int DURATION = 150;
    private void hideLastItem(int currentPosition){
        if (mLastOpenPosition != -1 && mLastOpenPosition != currentPosition && isLastOpenViewVisible()){
            if (mLastOpenHolder == null) return;
            final LinearLayout ll_container = mLastOpenHolder.ll_container;
            if (ll_container == null) return;

            ll_container.measure(0, 0);
            int containerHeight = ll_container.getMeasuredHeight();

            ValueAnimator valueAnimator = ValueAnimator.ofInt(containerHeight, 0);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int value = (Integer) valueAnimator.getAnimatedValue();
                    if (value == 0) {
                        ll_container.setVisibility(View.GONE);
                    }
                    ll_container.getLayoutParams().height = value;
                    ll_container.setLayoutParams(ll_container.getLayoutParams());
                }
            });
            valueAnimator.start();
        }
    }

    private void show(final LinearLayout ll_container){
        ll_container.measure(0, 0);
        final int containerHeight = ll_container.getMeasuredHeight();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, containerHeight);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                if (value == 0) {
                    ll_container.setVisibility(View.VISIBLE);
                }
                ll_container.getLayoutParams().height = value;
                ll_container.setLayoutParams(ll_container.getLayoutParams());
            }
        });
        valueAnimator.start();
    }

    private boolean isLastOpenViewVisible() {
        int first = mListView.getFirstVisiblePosition();
        int last = mListView.getLastVisiblePosition();
        if (mLastOpenPosition >= first && mLastOpenPosition <= last) {// 可见
            return true;
        }
        return false;
    }
}
