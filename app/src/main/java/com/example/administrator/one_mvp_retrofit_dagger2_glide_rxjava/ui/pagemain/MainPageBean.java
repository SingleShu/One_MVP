package com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.pagemain;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public class MainPageBean {

    /**
     * res : 0
     * data : ["1509","1505","1506","1504","1497","1503","1498","1502","1501","1500"]
     */

    private int res;
    private List<String> data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
