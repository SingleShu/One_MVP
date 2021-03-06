package com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.base.baseUtils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.oneUtils.DragScaleImageView;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by xiaohu on 2016/9/20.
 */
public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;//集合类，layout里包含的View,以view的id作为key，value是view对象
    private Context mContext;//上下文对象

    public BaseRecyclerViewHolder(Context ctx, View itemView) {
        super(itemView);
        mContext = ctx;
        mViews = new SparseArray<View>();
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public HorizontalScrollView getHorizontalScrollView(int viewId){
        return (HorizontalScrollView) getView(viewId);
    }

    public RadioGroup getRadioGroup(int viewId){
        return (RadioGroup) getView(viewId);
    }

    public CheckBox getCheckBox(int viewId){
        return (CheckBox) getView(viewId);
    }

    public FrameLayout getFrameLayout(int viewId){
        return (FrameLayout) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public CircleImageView getCircleImageView(int viewId){
        return (CircleImageView) getView(viewId);
    }

    public DragScaleImageView getDragScaleImageView(int viewId){
        return (DragScaleImageView) getView(viewId);
    }

    public LinearLayout getLinearLayout(int viewId){
        return (LinearLayout) getView(viewId);
    }
    public RelativeLayout getRelativeLayout(int viewId){
        return (RelativeLayout) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public BaseRecyclerViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public BaseRecyclerViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public BaseRecyclerViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        View view = findViewById(viewId);
        view.setOnClickListener(listener);
        return this;
    }

}
