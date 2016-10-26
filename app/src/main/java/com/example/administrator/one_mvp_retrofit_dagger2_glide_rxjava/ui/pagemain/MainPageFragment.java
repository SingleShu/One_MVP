package com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.pagemain;



import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.R;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseMvp.BaseActivity;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseMvp.BaseFragment;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseUtils.TLog;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.issue.IssueActivity;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.oneUtils.BaseFgAdapter;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.oneUtils.Const;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainPageFragment extends BaseFragment implements MainPageMvpView<MainPageBean> {

    @Inject
    MainPagePresenter mPresenter;
    @Bind(R.id.main_page_viewpager)
    ViewPager main_page_viewpager;

    private List<BaseFragment>fragment_vps;
    private BaseFgAdapter baseFgAdapter;


    @Override
    protected void initViewsAndEvents() {
        initViews();
        mPresenter.getMainpageData();
    }

    private void initViews() {
        fragment_vps = new ArrayList<>();
        baseFgAdapter = new BaseFgAdapter(getChildFragmentManager(),fragment_vps);
        main_page_viewpager.setAdapter(baseFgAdapter);
        main_page_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == fragment_vps.size()-1) {
                    Intent intent = new Intent(getActivity(), IssueActivity.class);
                    startActivity(intent);
                    main_page_viewpager.setCurrentItem(fragment_vps.size()-2);
                } else if (position == 0) {
                    main_page_viewpager.setCurrentItem(1);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void iniInjector() {
        ((BaseActivity)getActivity()).activityComponent().inject(this);
        mPresenter.attachView(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main_page;
    }

    @Override
    public void showData(MainPageBean data) {
        for (int index = -1; index < data.getData().size()+1; index++) {
            if (index == -1){
                fragment_vps.add(MainPageFragment_vp.newInstance(Const.PAGE_MAIN_IS_FIRST,""));
            }else if (index == data.getData().size()){
                fragment_vps.add(MainPageFragment_vp.newInstance(Const.PAGE_MAIN_IS_LAST,""));
            }else {
                fragment_vps.add(MainPageFragment_vp.newInstance(Const.PAGE_MAIN_OTHER,data.getData().get(index)));
                TLog.getInstance().i(data.getData().get(index));
            }
        }
        baseFgAdapter.notifyDataSetChanged();
    }
}
