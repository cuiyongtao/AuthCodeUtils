package com.victory.authcodeutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * 作者： Victory
 * 创建时间：  2017/10/16
 * 邮箱：cuiyongtao520@163.com
 * QQ：949021037
 * 说明：
 */

public class AuthCodeUtils extends View {
    private String authCodeText;//文本
    private int authCodeBackground;//背景颜色
    private int authCodeSize;//验证码长度

    private Rect mRect;
    private Paint mPaint;

    //构造方法
    public AuthCodeUtils(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        initializeProperty(context, attributeSet);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attributeSet
     */
    private void initializeProperty(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AuthCodeUtils);
        //获取typedArray长度
        int num = typedArray.getIndexCount();
        for (int i = 0; i < num; i++) {
            //获取属性ＩＤ
            int id = typedArray.getIndex(i);
            switch (id) {
                case R.styleable.AuthCodeUtils_authCodeText:
                    authCodeText = typedArray.getString(id);
                    break;
                case R.styleable.AuthCodeUtils_authCodeTextSize:
                    authCodeSize = typedArray.getDimensionPixelSize(id, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.AuthCodeUtils_authCodeBackground:
                    authCodeBackground = typedArray.getColor(id, Color.RED);
                    break;
            }
        }
        typedArray.recycle();
        //获取文本的长和宽
        mPaint = new Paint();
        mPaint.setTextSize(authCodeSize);
        mRect = new Rect();
        mPaint.getTextBounds(authCodeText, 0, authCodeText.length(), mRect);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                authCodeText = randomText();
                postInvalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(authCodeBackground);
        canvas.drawText(authCodeText, getWidth() / 2 - mRect.width() / 2, getHeight() / 2 + mRect.height() / 2, mPaint);
        final int height = getHeight();
        final int width = getWidth();
        // 绘制小圆点
        int[] point;
        for (int i = 0; i < 100; i++) {
            point = getPoint(height, width);
            /**
             * drawCircle (float cx, float cy, float radius, Paint paint)
             * float cx：圆心的x坐标。
             * float cy：圆心的y坐标。
             * float radius：圆的半径。
             * Paint paint：绘制时所使用的画笔。
             */
            canvas.drawCircle(point[0], point[1], 1, mPaint);
        }

        for (int i = 0; i < 50; i++) {
            point = getPoint(height, width);
            canvas.drawCircle(point[0], point[1], 2, mPaint);
        }

        for (int i = 0; i < 30; i++) {
            point = getPoint(height, width);
            canvas.drawCircle(point[0], point[1], 3, mPaint);
        }

        int[] line;
        for (int i = 0; i < 10; i++) {
            line = getLine(height, width);
            /**
             * startX：起始端点的X坐标。
             *startY：起始端点的Y坐标。
             *stopX：终止端点的X坐标。
             *stopY：终止端点的Y坐标。
             *paint：绘制直线所使用的画笔。
             */
            canvas.drawLine(line[0], line[1], line[2], line[3], mPaint);
        }

    }

    //随机生成小圆点坐标
    private int[] getPoint(int height, int width) {
        int[] tempCheckNum = {0, 0};
        tempCheckNum[0] = (int) (Math.random() * width);
        tempCheckNum[1] = (int) (Math.random() * height);
        return tempCheckNum;
    }

    //随机生成线线
    public int[] getLine(int height, int width) {
        int[] tempCheckNum = {0, 0, 0, 0};
        for (int i = 0; i < 4; i += 2) {
            tempCheckNum[i] = (int) (Math.random() * width);
            tempCheckNum[i + 1] = (int) (Math.random() * height);
        }
        return tempCheckNum;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取控件宽高的显示模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取宽高的尺寸值  固定值的宽度
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;
        int height;

//        MeasureSpec父布局传递给后代的布局要求 包含 确定大小和三种模式
//        EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
//        AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
//        UNSPECIFIED：表示子布局想要多大就多大，很少使用
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(authCodeSize);
            mPaint.getTextBounds(authCodeText, 0, authCodeText.length(), mRect);
            float textWidth = mRect.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(authCodeSize);
            mPaint.getTextBounds(authCodeText, 0, authCodeText.length(), mRect);
            float textHeight = mRect.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }
        //设置测量的宽高
        setMeasuredDimension(width, height);
    }

    //生成随机验证码
    private String randomText() {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            if (true) {
                //字母或数字
                String code = random.nextInt(2) % 2 == 0 ? "code" : "num";
                //字母
                if ("code".equalsIgnoreCase(code)) {
                    //大写或小写字母
                    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                    val += (char) (choice + random.nextInt(26));
                } else if ("num".equalsIgnoreCase(code)) {
                    val += String.valueOf(random.nextInt(10));
                }
            } else {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val.toString();
    }
}

