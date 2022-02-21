package com.example.sport.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sport.R;

public class GradientProgressBar extends View {

    //底画笔
    private Paint mCirclePaint;
    //进度笔
    private Paint mProgressPaint;
    //底颜色
    private int mCircleColor;
    //覆盖色
    private int mCoverColor;
    //文本画笔1
    private Paint mTextPaint1;
    //文本画笔2
    private Paint mTextPaint2;
    //文字颜色
    private int mTextColor;
    //文本
    private String mText1;
    private String mText2 = "目标0.00km";
    //文本大小
    private float mTextSize;
    //坐标x
    private int mXCenter;
    //坐标y
    private int mYCenter;
    //半径
    private float mRadius;
    //宽度
    private float mCircleWidth;
    //刻度坐标
    private float[] mKdPaths;
    //指针长度
    private float mKdLength;
    //总进度
    private double mSumProgress;
    //当前进度
    private double mCurrentProgress;

    private Rect mBound1;
    private Rect mBound2;

    public GradientProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        //绘制底刻度
        for(int i = 0; i < 90; i++) {
            mKdPaths = getDialPaths(mXCenter,mYCenter,mRadius,mRadius - mKdLength, i * 4 - 90);
            canvas.drawLines(mKdPaths,mCirclePaint);
        }

        //绘制当前进度
        float startX = mXCenter - (float)mBound1.width() / 2;
        float startY = mYCenter;
        canvas.drawText(mText1, startX, startY, mTextPaint1);

        //绘制总进度
        startX = mXCenter - (float)mBound2.width() / 2;
        canvas.drawText(mText2,startX, (float) (startY + 200.0),mTextPaint2);

        int progress = 0;
        if(mCurrentProgress >= mSumProgress) {
            if(gradientProgressBarCallBack != null) {
                gradientProgressBarCallBack.isFinish();
            }
            progress = 90;
        }else {
            progress = (int) (mCurrentProgress / mSumProgress * 90);
        }
        for(int i = 0; i < 90 - progress; i++) {
            mKdPaths = getDialPaths(mXCenter,mYCenter,mRadius,mRadius - mKdLength, -i * 4 - 90);
            canvas.drawLines(mKdPaths,mProgressPaint);
        }
    }

    //初始化画笔
    @SuppressLint("ResourceAsColor")
    private void initPaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(mCircleWidth);
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(mCoverColor);
        mProgressPaint.setStrokeWidth(mCircleWidth - 1);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setDither(true);

        mBound1 = new Rect();
        mTextPaint1 = new Paint();
        mTextPaint1.setColor(mTextColor);
        mTextPaint1.setTextSize(mTextSize);
        mTextPaint1.setAntiAlias(true);
        mTextPaint1.setDither(true);
        mTextPaint1.getTextBounds(mText1,0, mText1.length(), mBound1);

        mBound2 = new Rect();
        mTextPaint2 = new Paint();
        mTextPaint2.setColor(R.color.text_color_3);
        mTextPaint2.setTextSize(50);
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setDither(true);
        mTextPaint2.getTextBounds(mText2,0,mText2.length(),mBound2);
    }

    //初始化属性参数
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyCustomizedView, 0, 0);

        mCircleColor = typedArray.getColor(R.styleable.MyCustomizedView_circleColor,0xFFFFF);
        mCircleWidth = typedArray.getDimension(R.styleable.MyCustomizedView_strokeWidth,60);
        mRadius = typedArray.getDimension(R.styleable.MyCustomizedView_radius,80);
        mCoverColor = typedArray.getColor(R.styleable.MyCustomizedView_circleCover, 0xFFFF);
        mKdLength = typedArray.getDimension(R.styleable.MyCustomizedView_kdLength,10);
        mTextColor = typedArray.getColor(R.styleable.MyCustomizedView_textColor,0xFFFF);
        mText1 = typedArray.getString(R.styleable.MyCustomizedView_text);
        mTextSize = typedArray.getDimension(R.styleable.MyCustomizedView_textSize,100);
    }

    //设置总进度
    public void setSumProgress(double progress) {
        mSumProgress = progress;
        mText2 = "目标" + progress + "km";
        mTextPaint2.getTextBounds(mText2,0,mText2.length(),mBound2);
        invalidate();
    }

    //更新进度
    public void updateProgress(double currentProgress) {
        mCurrentProgress = currentProgress;
        mText1 = currentProgress + "km";
        mTextPaint1.getTextBounds(mText1,0, mText1.length(), mBound1);
        invalidate();
    }


    private void setListener(GradientProgressBarCallBack callBack) {
        this.gradientProgressBarCallBack = callBack;
    }

    private GradientProgressBarCallBack gradientProgressBarCallBack;

    public interface GradientProgressBarCallBack{
        void isFinish();
    }


    /**
     * 通过改变角度值,获取不同角度方向的外圆一点到圆心连线过内圆一点的路径坐标集合
     * @param x0 圆心x
     * @param y0 圆心y
     * @param outRadius 外圆半径
     * @param innerRadius 内圆半径
     * @param angle 角度
     * @return 返回
     */
    private float[] getDialPaths(int x0, int y0, float outRadius, float innerRadius, int angle){
        float[] paths = new float[4];
        paths[0]  = (float) (x0 + outRadius * Math.cos(angle * Math.PI / 180));
        paths[1]  = (float) (y0 + outRadius * Math.sin(angle * Math.PI / 180));
        paths[2]  = (float) (x0 + innerRadius * Math.cos(angle * Math.PI / 180));
        paths[3]  = (float) (y0 + innerRadius * Math.sin(angle * Math.PI / 180));
        return paths;
    }
}
