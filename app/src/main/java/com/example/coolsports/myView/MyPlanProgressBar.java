package com.example.coolsports.myView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.coolsports.R;

public class MyPlanProgressBar extends View {

    //底画笔
    private Paint mCirclePaint;
    //进度笔
    private Paint mProgressPaint;
    //进度圆角
    private Paint mProgressCirclePaint;
    //文本画笔
    private Paint mTextPaint;
    //文本
    private String mTextContent;
    private float mTextSize;
    //文本颜色
    private int mTextColor;
    //圆角半径
    private float mRadiusSmallCircle;
    //底颜色
    private int mCircleColor;
    //覆盖色
    private int mCoverColor;
    //坐标x
    private int mXCenter;
    //坐标y
    private int mYCenter;
    //半径
    private float mRadius;
    //宽度
    private float mCircleWidth;

    private Rect mBound;

    //总进度
    private float mProgress;
    //当前进度
    private float mCurrentProgress;


    public MyPlanProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = getLayoutParams().height;
        if (height == ActionBar.LayoutParams.WRAP_CONTENT) {
            Log.d("TAG", "onMeasure: ");
        }
        if (widthMeasureSpecMode == MeasureSpec.AT_MOST && heightMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (mRadius + mCircleWidth) * 2, (int) (mRadius + mCircleWidth) * 2);
        } else if (widthMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) (mRadius + mCircleWidth) * 2, heightMeasureSpecSize);
        } else if (heightMeasureSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSpecSize, (int) (mRadius + mCircleWidth) * 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;

        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
        canvas.drawCircle(mXCenter, mYCenter - mRadius, mRadiusSmallCircle, mProgressCirclePaint);

        if (mCurrentProgress >= mProgress && mCurrentProgress != 0) {
            mTextContent = "已达标";
        } else {
            mTextContent = "未达标";
        }

        canvas.drawText(mTextContent, mXCenter - (float) (mBound.width() / 2), mYCenter + (float) mBound.height() / 2, mTextPaint);
        @SuppressLint("DrawAllocation") RectF rect = new RectF();
        rect.bottom = mYCenter + mRadius;
        rect.top = mYCenter - mRadius;
        rect.left = mXCenter - mRadius;
        rect.right = mXCenter + mRadius;
        float angle = 0;
        if (mProgress != 0) {
            angle = mCurrentProgress / mProgress * 360F;
        }
        canvas.drawArc(rect, -90, angle, false, mProgressPaint);
        angle -= 90;
        canvas.drawCircle(mXCenter + (float) (Math.cos(Math.PI * angle / 180)) * mRadius, mYCenter + (float) (mRadius * Math.sin(angle * Math.PI / 180)), mRadiusSmallCircle, mProgressCirclePaint);
    }

    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);


        mProgressPaint = new Paint();
        mProgressPaint.setColor(mCoverColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mCircleWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mProgressCirclePaint = new Paint();
        mProgressCirclePaint.setAntiAlias(true);
        mProgressCirclePaint.setAntiAlias(true);
        mProgressCirclePaint.setColor(mCoverColor);
        mProgressCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mTextPaint = new Paint();
        mBound = new Rect();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.getTextBounds(mTextContent, 0, mTextContent.length(), mBound);
    }

    //初始化属性参数
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCustomizedView, 0, 0);

        mCircleColor = typedArray.getColor(R.styleable.MyCustomizedView_circleColor, 0xFFFFFFFF);
        mRadius = typedArray.getDimension(R.styleable.MyCustomizedView_radius, 80);
        mCircleWidth = typedArray.getDimension(R.styleable.MyCustomizedView_strokeWidth, 10);
        mCoverColor = typedArray.getColor(R.styleable.MyCustomizedView_circleCover, 0xFFFFF);
        mTextContent = typedArray.getString(R.styleable.MyCustomizedView_text);
        mTextColor = typedArray.getColor(R.styleable.MyCustomizedView_textColor, 0xFFFF);
        mTextSize = typedArray.getDimension(R.styleable.MyCustomizedView_textSize, 50);
        mRadiusSmallCircle = mCircleWidth / 2;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setCurrentProgress(float progress) {
        mCurrentProgress = progress;
        invalidate();
    }

}
