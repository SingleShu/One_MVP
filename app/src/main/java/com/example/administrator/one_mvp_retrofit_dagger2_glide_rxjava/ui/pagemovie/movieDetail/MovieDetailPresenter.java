package com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.pagemovie.movieDetail;

import android.content.Context;

import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseMvp.BasePresenter;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseRetrofit.DataManager;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseUtils.BaseSubscribe;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseUtils.TLog;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.oneUtils.GsonHelper;
import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.oneUtils.PostResultBean;

import org.json.JSONObject;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/11/15.
 */

public class MovieDetailPresenter extends BasePresenter<MovieDetailMvpView<MovieDetailDataBean>> {
    private DataManager dataManager;
    private Context context;
    private MovieDetailMvpView<MovieDetailDataBean> mMvpView;
    private Subscription subscription;

    @Inject
    public MovieDetailPresenter(DataManager dataManager, Context context) {
        this.dataManager = dataManager;
        this.context = context;
    }

    /**
     * 获取电影详情界面和故事
     * @param movieId
     */
    public void getMovieDetailAndStory(final String movieId){
        subscription = dataManager.getMovieDetail(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<JSONObject, Observable<JSONObject>>() {
                    @Override
                    public Observable<JSONObject> call(JSONObject jsonObject) {
                        mMvpView.showData(GsonHelper.getGsonObject().fromJson(jsonObject.toString(),MovieDetailDataBean.class));
                        return dataManager.getMovieStory(movieId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
                    }
                })
                .flatMap(new Func1<JSONObject, Observable<JSONObject>>() {
                    @Override
                    public Observable<JSONObject> call(JSONObject jsonObject) {
                        mMvpView.showData(GsonHelper.getGsonObject().fromJson(jsonObject.toString(),MovieDetailStoryBean.class));
                        return dataManager.getMygrade(movieId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io());
                    }
                })
                .subscribe(new BaseSubscribe<JSONObject>(context) {
                    @Override
                    public void onNextJSONObject(JSONObject jsonObject) {
                        TLog.getInstance().i(jsonObject.toString());
                        mMvpView.showData(GsonHelper.getGsonObject().fromJson(jsonObject.toString(),MovieDetailScoreBean.class));
                    }
                });
    }

    /**
     * 获取电影详情评论
     * @param movieId
     * @param commentId
     */
    public void getMovieComment(String movieId,String commentId){
        subscription = dataManager.getMovieComment(movieId,commentId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribe<JSONObject>(context,false) {
                    @Override
                    public void onNextJSONObject(JSONObject jsonObject) {
                        mMvpView.showData(GsonHelper.getGsonObject().fromJson(jsonObject.toString(),MovieDetailContentBean.class));
                    }
                });
    }

    /**
     * 电影评论点赞
     * @param movieId
     * @param type
     */
    public void postMusicLike(String movieId,String type,String cmtId){
        subscription = dataManager.postMusicPraise(movieId,type,cmtId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribe<JSONObject>(context,false) {
                    @Override
                    public void onNextJSONObject(JSONObject jsonObject) {
                        mMvpView.praise(GsonHelper.getGsonObject().fromJson(jsonObject.toString(), PostResultBean.class));
                    }
                });
    }

    /**
     * 电影评论取消赞
     * @param movieId
     * @param type
     */
    public void postMusicUnPraise(String movieId,String type,String cmtId){
        subscription = dataManager.postMusicUnPraise(movieId,type,cmtId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscribe<JSONObject>(context,false) {
                    @Override
                    public void onNextJSONObject(JSONObject jsonObject) {
                        mMvpView.praise(GsonHelper.getGsonObject().fromJson(jsonObject.toString(), PostResultBean.class));
                    }
                });
    }

    /**
     * 电影故事点赞
     * @param storyid
     * @param movieId
     */
    public void postMovieStoryPraise(String storyid,String movieId){
        subscription = dataManager.postMovieStoryPraise(storyid,movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<JSONObject>(context,false) {
                    @Override
                    public void onNextJSONObject(JSONObject jsonObject) {
                        mMvpView.praise(GsonHelper.getGsonObject().fromJson(jsonObject.toString(), PostResultBean.class));
                    }
                });
    }
    /**
     * 电影故事取消点赞
     * @param storyid
     * @param movieId
     */
    public void postMovieStoryUnPraise(String storyid,String movieId){
        subscription = dataManager.postMovieStoryUnPraise(storyid,movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<JSONObject>(context,false) {
                    @Override
                    public void onNextJSONObject(JSONObject jsonObject) {
                        mMvpView.praise(GsonHelper.getGsonObject().fromJson(jsonObject.toString(), PostResultBean.class));
                    }
                });
    }

    @Override
    public void attachView(MovieDetailMvpView<MovieDetailDataBean> mMvpView) {
        this.mMvpView = mMvpView;
    }

    @Override
    public void detachViews() {
        super.detachViews();
        if (subscription != null){
            subscription.unsubscribe();
            subscription = null;
        }
        mMvpView = null;
    }
}
