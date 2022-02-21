package com.example.sport.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.example.sport.R;

public class MyProgressButton extends View {
    //底圆
    private Paint mCirclePaint;
    //圆环底
    private Paint mCircleC3Paint;
    //圆环
    private Paint mRingPaint;
    //文本
    private Paint mTextPaint;
    //文本颜色
    private int mTextColor;
    //文本内容
    private String mTextContent;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    // 圆环底颜色
    private int mCircleC3Color;
    //半径
    private float mRadius;
    //大圆半径
    private float mBigCircleRadius;
    //圆环半径
    private float mRingRadius;
    //圆环宽度
    private float mRingWidth;
    //坐标x
    private int mXCenter;
    //坐标y
    private int mYCenter;
    //总进度
    private int mProgress;
    //完成
    private boolean isFinish;

    @SuppressLint("ClickableViewAccessibility")
    public MyProgressButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        initPaint();

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startPress(300);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mProgress == 300) {
                            if (!isFinish) {
                                isFinish = true;
                                myProgressButtonFinishCallback.isFinish();
                                return false;
                            }
                        }
                        if (mProgress < 300) {
                            if (!isFinish) {
                                stopPress(mProgress);
                                myProgressButtonFinishCallback.isCancel();
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    //初始化画笔
    @SuppressLint("ResourceAsColor")
    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setColor(mCircleColor);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStrokeWidth(mRingWidth - 2);

        mCircleC3Paint = new Paint();
        mCircleC3Paint.setAntiAlias(true);
        mCircleC3Paint.setStyle(Paint.Style.STROKE);
        mCircleC3Paint.setColor(mCircleC3Color);
        mCircleC3Paint.setStrokeWidth(mRingWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(80);
        mTextPaint.setColor(mTextColor);
    }

    //初始化属性参数
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCustomizedView, 0, 0);

        mCircleColor = typedArray.getColor(R.styleable.MyCustomizedView_circleColor, 0xFFFFFFFF);
        mRingColor = typedArray.getColor(R.styleable.MyCustomizedView_ringColor, 0xCCCCCC);
        mCircleC3Color = typedArray.getColor(R.styleable.MyCustomizedView_ringCColor, 0xCCCCCC);
        mRadius = typedArray.getDimension(R.styleable.MyCustomizedView_radius, 80);
        mRingWidth = typedArray.getDimension(R.styleable.MyCustomizedView_strokeWidth, 10);
        mTextColor = typedArray.getColor(R.styleable.MyCustomizedView_textColor, 0xFFFFF);
        mTextContent = typedArray.getString(R.styleable.MyCustomizedView_text);
        mBigCircleRadius = mRadius + mRingWidth * 2;
        mRingRadius = mRadius + mRingWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        canvas.drawCircle(mXCenter, mYCenter, mBigCircleRadius, mCirclePaint);
        canvas.drawText(mTextContent,mXCenter - 80,mYCenter + 30,mTextPaint);

        if (mProgress == 300) {
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius + mXCenter;
            oval.bottom = mRingRadius + mYCenter;
            canvas.drawArc(oval, -90, 360, false, mCirclePaint);
        } else if (mProgress > 0) {
            @SuppressLint("DrawAllocation") RectF rectF = new RectF();
            rectF.left = mXCenter - mRingRadius;
            rectF.right = mXCenter + mRingRadius;
            rectF.top = mYCenter - mRingRadius;
            rectF.bottom = mYCenter + mRingRadius;
            canvas.drawArc(rectF, -90, 360, false, mCircleC3Paint);
            canvas.drawArc(rectF, -90, ((float)mProgress / 300) * 360, false, mRingPaint);
        }
    }

    private ValueAnimator startAnimator;
    private ValueAnimator stopAnimator;

    //按压动画
    private void startPress(int progress) {
        isFinish = false;
        if (stopAnimator != null) {
            if (stopAnimator.isRunning()) {
                stopAnimator.cancel();
            }
        }
        startAnimator = ValueAnimator.ofInt(0, progress);
        startAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (int) valueAnimator.getAnimatedValue();
                invalidate();
                if (mProgress >= 300) {
                    if (!isFinish) {
                        isFinish = true;
                        myProgressButtonFinishCallback.isFinish();
                    }
                }
            }
        });
        startAnimator.setDuration(3000);
        startAnimator.setInterpolator(new OvershootInterpolator());
        startAnimator.start();
    }

    //停止按压动画
    private void stopPress(int progress) {
        if (startAnimator != null) {
            if (startAnimator.isRunning()) {
                startAnimator.cancel();
            }
        }
        stopAnimator = ValueAnimator.ofInt(progress, 0);
        stopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgress = (int) valueAnimator.getAnimatedValue();
                invalidate();//重新绘制view
            }
        });
        stopAnimator.setDuration(3000);
        stopAnimator.setInterpolator(new OvershootInterpolator());
        stopAnimator.start();
    }

    public interface MyProgressButtonFinishCallback {
        void isFinish();

        void isCancel();
    }

    private MyProgressButtonFinishCallback myProgressButtonFinishCallback;

    public void setListener(MyProgressButtonFinishCallback myProgressButtonFinishCallback) {
        this.myProgressButtonFinishCallback = myProgressButtonFinishCallback;
    }
}
