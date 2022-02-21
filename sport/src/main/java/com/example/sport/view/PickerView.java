package com.example.sport.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class PickerView extends View {
    public static final String TAG = "PickerView";
    //minTextSize和text之间间距之比
    public static final float MARGIN_ALPHA = 1.56f;
    //自动回滚到中间的速度
    public static final byte SPEED = 2;
    public static final byte PERIOD = 10;
    public static final byte CODE = 0X10;
    private List<String> mDataList;
    /**
     * 选中的位置，这个位置是mDataList的中心位置，一直不变
     */
    private int mCurrentSelected;
    private Paint mPaint;

    private float mMaxTextSize = 100;
    private float mMinTextSize = 60;

    private float mMaxTextAlpha = 255;
    private float mMinTextAlpha = 120;

    private int mTextMargin = 10;
    private int mColorText = 0xffffff;

    private int mViewHeight;
    private int mViewWidth;
    private float mLastDownY;
    //滑动的距离
    private float mMoveLen = 0;
    private boolean isInit = false;
    private onSelectListener mSelectListener;
    private volatile boolean isInAnimate;
    private Handler updateHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            if (Math.abs(mMoveLen) < SPEED) {
                mMoveLen = 0;
                performSelect();
            } else {
                // 这里mMoveLen / Math.abs(mMoveLen)是为了保有mMoveLen的正负号，以实现上滚或下滚
                mMoveLen = mMoveLen - mMoveLen / Math.abs(mMoveLen) * SPEED;
                sendEmptyMessageDelayed(CODE, PERIOD);
            }
            invalidate();
        }
    };

    public PickerView(Context context) {
        this(context, null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnSelectListener(onSelectListener listener) {
        mSelectListener = listener;
    }

    private void performSelect() {
        if (mSelectListener != null)
            mSelectListener.onSelect(mDataList.get(mCurrentSelected));
    }

    public void setData(List<String> datas) {
        if (datas != null) {
            mDataList.addAll(datas);
        }
        mCurrentSelected = mDataList.size() / 2;
        performSelect();
        invalidate();
    }

    private void moveHeadToTail() {
        String head = mDataList.get(0);
        mDataList.remove(0);
        mDataList.add(head);
    }

    private void moveTailToHead() {
        String tail = mDataList.get(mDataList.size() - 1);
        mDataList.remove(mDataList.size() - 1);
        mDataList.add(0, tail);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        // 按照View的高度计算字体大小
//        mMaxTextSize = mViewHeight / 4.0f;
//        mMinTextSize = mMaxTextSize / 2f;
        isInit = true;
        invalidate();
    }

    private void init() {
        mDataList = new ArrayList<>();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.argb(100,100,100,100));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInit) drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        if (mDataList.isEmpty()) return;
        // 先绘制选中的text再往上往下绘制其余的text
        mPaint.setTextSize(mMaxTextSize);
        mPaint.setAlpha((int) mMaxTextAlpha);
        // text居中绘制，注意baseline的计算才能达到居中，y值是text中心坐标
        float x = (float) (mViewWidth / 2.0);
        float y = (float) (mViewHeight / 2.0 + mMoveLen);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0));//这里是经过简化后的公式
        canvas.drawText(mDataList.get(mCurrentSelected), x, baseline, mPaint);
        // 绘制上方data
        for (int i = 1; (mCurrentSelected - i) >= 0; i++) {
            drawOtherText(canvas, i, -1);
        }
        // 绘制下方data
        for (int i = 1; (mCurrentSelected + i) < mDataList.size(); i++) {
            drawOtherText(canvas, i, 1);
        }
    }

    /**
     * @param canvas
     * @param position 距离mCurrentSelected的差值
     * @param type     1表示向下绘制，-1表示向上绘制
     */
    private void drawOtherText(Canvas canvas, int position, int type) {
        float d = (float) (MARGIN_ALPHA * mMinTextSize * position + type * mMoveLen);
        float size = mMinTextSize;
        mPaint.setTextSize(size);
        mPaint.setAlpha((int) (mMinTextAlpha));
        float y = (float) (mViewHeight / 2.0 + type * d);
        Paint.FontMetricsInt fmi = mPaint.getFontMetricsInt();
        float baseline = (float) (y - (fmi.bottom / 2.0 + fmi.top / 2.0)) + type * mTextMargin;
        canvas.drawText(mDataList.get(mCurrentSelected + type * position), (float) (mViewWidth / 2.0), baseline, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                doDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                doMove(event);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        return true;
    }

    private void doDown(MotionEvent event) {
        updateHandler.removeMessages(CODE);
        mLastDownY = event.getY();
    }

    private void doMove(MotionEvent event) {
        mMoveLen += (event.getY() - mLastDownY);
        if (mMoveLen > MARGIN_ALPHA * mMinTextSize / 2) {
            // 往下滑超过离开距离
            moveTailToHead();
            mMoveLen = mMoveLen - MARGIN_ALPHA * mMinTextSize;
        } else if (mMoveLen < -MARGIN_ALPHA * mMinTextSize / 2) {
            // 往上滑超过离开距离
            moveHeadToTail();
            mMoveLen = mMoveLen + MARGIN_ALPHA * mMinTextSize;
        }
        mLastDownY = event.getY();
        invalidate();
    }

    private void doUp(MotionEvent event) {
        // 抬起手后mCurrentSelected的位置由当前位置move到中间选中位置
        if (Math.abs(mMoveLen) < 0.0001) {
            mMoveLen = 0;
            return;
        }
        //这段是为了修正瞬滑导致回滚较多现象，屏蔽亦可
        if (Math.abs(mMoveLen) > MARGIN_ALPHA * mMinTextSize / 2) {
            int m = (int) (Math.abs(mMoveLen) / (MARGIN_ALPHA * mMinTextSize / 2));
            for (int i = 0; i < m; i++) {
                if (mMoveLen > 0) moveTailToHead();
                else moveHeadToTail();
            }
            if (mMoveLen > 0) mMoveLen -= MARGIN_ALPHA * mMinTextSize * m / 2;
            else mMoveLen += MARGIN_ALPHA * mMinTextSize * m / 2;
        }
        updateHandler.removeMessages(CODE);
        updateHandler.sendEmptyMessage(CODE);
    }

    /**
     * @param up 是否向上滑动
     */
    public void autoScroll(boolean up) {
        if (isInAnimate) return;
        isInAnimate = true;
        final float[] values = up ? new float[]{MARGIN_ALPHA * mMinTextSize + 1, 0} : new float[]{0, MARGIN_ALPHA * mMinTextSize + 1};
        ValueAnimator va = ValueAnimator.ofFloat(values);
        va.setDuration(100);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                doMove(MotionEvent.obtain(System.currentTimeMillis(), 0, 0, 0f, v, 0));
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                doUp(null);
                isInAnimate = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                doDown(MotionEvent.obtain(System.currentTimeMillis(), 0, 0, 0f, values[0], 0));
            }
        });
        va.start();
    }

    public interface onSelectListener {
        void onSelect(String text);
    }
}
