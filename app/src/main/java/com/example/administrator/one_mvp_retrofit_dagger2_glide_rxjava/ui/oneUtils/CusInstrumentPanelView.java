package com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.ui.oneUtils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.example.administrator.one_mvp_retrofit_dagger2_glide_rxjava.R;
import com.fjwangjia.android.instrumentpanel.Text;
import com.fjwangjia.android.instrumentpanel.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */

public class CusInstrumentPanelView extends View {
    public static String TAG = "InstrumentPanelView";
    int mTouchSlop;
    Pointer pointer = new Pointer();
    int mTextColor = 0xFFBBBBBB;
    float mTextSize = 24;
    List<Block> mBlockList = new ArrayList<>();
    CurrentScore currentScore;
    boolean isZuni = false;


    public CusInstrumentPanelView(Context context) {
        super(context);
        init(context,null);
    }

    public void setCurrentScore(CurrentScore currentScore){
        this.currentScore = currentScore;
    }

    public CusInstrumentPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public CusInstrumentPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @TargetApi(21)
    public CusInstrumentPanelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    void init(Context context,AttributeSet attrs){
        final ViewConfiguration vc = ViewConfiguration.get(getContext());
        mTouchSlop = vc.getScaledTouchSlop();

        if(attrs!=null){
            TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.InstrumentPanelView);
            mTextColor = a.getColor(R.styleable.InstrumentPanelView_textColor, Color.GRAY);
            mTextSize  = a.getDimension(R.styleable.InstrumentPanelView_textSize,mTextSize);
            a.recycle();
        }
    }


    public void setBlockList(List<Block> blockList){
        mBlockList = blockList;
        invalidate();
    }

    public void addBlock(Block block){
        mBlockList.add(block);
    }

    public void clearBlock(){
        mBlockList.clear();
    }

    public void pointerTo(float rate){
        if(rate >=0){
            isZuni = true;
            pointer.dampingTo(doActionHandler,rate,0);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    String score = "0";

    String rateToString(float rate){
        score = String.format("%.0f",rate*100);
        if (currentScore != null){
            currentScore.getScore(score);
        }
        return score+"分";
    }

    public interface CurrentScore{
        void getScore(String s);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int size = mBlockList.size();
        float iMaxRate = 1f;
        if(size != 0){
            iMaxRate = mBlockList.get(size-1).fRate;
        }

        List<Text> texts = new ArrayList<>();
        texts.add(new Text(mTextSize,rateToString(pointer.currentRate),mTextColor));
        texts.add(new Text(mTextSize,"0%",mTextColor));
        for (Block block : mBlockList){
            texts.add(new Text(mTextSize,block.toString(),mTextColor));
        }

        int textCount = texts.size();
        float space = Utils.dpTopx(getContext(),3);
        int rectLeft = (int) (texts.get(textCount-1).getWidth()+space);
        int rectTop = (int) (texts.get(textCount-1).getHeight()+space);
        int rectRight = (int) (getMeasuredWidth()-texts.get(textCount-1).getWidth()-2*space);
        int rectBottom = (int) (getMeasuredHeight()-texts.get(0).getHeight()-5*space);

        Rect rect = new Rect(
                rectLeft ,
                rectTop ,
                rectRight,
                rectBottom);

        List<Panel> panels = new ArrayList<>();
        for (int i=0;i<mBlockList.size();i++){
            Block block = mBlockList.get(i);
            Panel panel;
            if(i==0){
                panel = new Panel(0,block.fRate,block.iColor,iMaxRate);
            }else {
                Block preBlock = mBlockList.get(i-1);
                panel = new Panel(preBlock.fRate,block.fRate,block.iColor,iMaxRate);
            }

            panel.layout(rect);
            panel.draw(canvas);
            panels.add(panel);
        }

        if(panels.size() == 0){
            return;
        }

        float fLongRadius = panels.get(0).getLongRadius();
        Point circlePoint = panels.get(0).circlePoint;

        pointer.init(Utils.dpTopx(getContext(), 1), circlePoint, fLongRadius / 30, fLongRadius * 7 / 8,iMaxRate);
        pointer.draw(canvas);

        for (int i=0;i<texts.size();i++){
            Text text = texts.get(i);
            float height = text.getHeight();
            float width  = text.getWidth();
            if(i==0){
                //指针底部的数字
//                text.layout(new Rect(
//                        (int)(circlePoint.x-width/2),
//                        (int)(circlePoint.y+space),
//                        (int)(circlePoint.x+width/2),
//                        (int)(circlePoint.y+space+height+pointer.mRadius)));
                text.layout(new Rect());
            }else if(i == 1){
                float radius = fLongRadius + space;
                Point point = Utils.getCircleSide(circlePoint,radius,0f);
                text.layout(new Rect(
                        (int)(point.x-width),
                        (int)(point.y+space),
                        point.x,
                        point.y
                ));
            }
            else
            {
                float radius = fLongRadius + space;
                Point point = Utils.getCircleSide(circlePoint,radius,mBlockList.get(i-2).fRate/iMaxRate);

                //小于90度 和 大于90度 计算坐标有所不同
                if(mBlockList.get(i-2).fRate/iMaxRate<0.5){
                    text.layout(new Rect(
                            (int)(point.x-width),
                            (int)(point.y+space),
                            point.x,
                            point.y
                    ));
                }else {
                    text.layout(new Rect(
                            point.x,
                            point.y,
                            point.x,
                            point.y
                    ));
                }

            }
            text.draw(canvas);
        }


    }

    int mInitialTouchX , mInitialTouchY;
    int mLastTouchX , mLastTouchY;
    public boolean mIsDragPointer = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        final MotionEvent vtev = MotionEvent.obtain(e);
        final int action = vtev.getAction();

        if(pointer == null)return true;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialTouchX = mLastTouchX = (int) (e.getX() + 0.5f);
                mInitialTouchY = mLastTouchY = (int) (e.getY() + 0.5f);

                //判断触点是否在指针上面
                mIsDragPointer =  pointer.isInside(mLastTouchX,mLastTouchY);
                if(mIsDragPointer){
                    pointer.clearDamping();
                }
            } break;

            case MotionEvent.ACTION_MOVE: {

                final int x = (int) (e.getX() + 0.5f);
                final int y = (int) (e.getY() + 0.5f);

                if(!mIsDragPointer){
                    mIsDragPointer =  pointer.isInside(mLastTouchX,mLastTouchY);
                    if(!mIsDragPointer){
                        break;
                    }
                }

                pointer.dragPoint(new Point(x,y));
                invalidate();

            } break;



            case MotionEvent.ACTION_UP: {
                if (mIsDragPointer){
                    if (isZuni){
                        pointer.dampingBack(doActionHandler);
                    }
                    mIsDragPointer = false;
                }
            } break;

            case MotionEvent.ACTION_CANCEL: {
                cancelTouch();
            } break;
        }
        return true;
    }

    private void cancelTouch() {

    }

    private void onPointerUp(MotionEvent e) {

    }
}
