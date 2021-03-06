package com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseMvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.BaseEventBus;

import butterknife.ButterKnife;

/**
 * Created by ShuWen on 2016/10/15.
 */

public abstract class BaseFragment extends Fragment {
    protected View mContentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注入控制器
        iniInjector();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            mContentView = inflater.inflate(getContentViewLayoutID(),null);
            ButterKnife.bind(this, mContentView);
            //初始化方法
            initViewsAndEvents();
            return mContentView;
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }


    /**
     * 初始化视图和事件
     */
    protected abstract void initViewsAndEvents();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    /**
     * 依赖注入
     */
    protected abstract void iniInjector();

    /**
     * 返回View的id
     * @return
     */
    protected abstract int getContentViewLayoutID();

    //EventBus的使用，用于组件之间的数据交互
    public void onEventMainThread(BaseEventBus event) {
    }
}
