package com.example.mysearchviewdemo.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lenovo on 2017/5/19.
 */

public class MySearchView extends View {

    private Paint paint;
    private Path path;

    public MySearchView(Context context) {
        super(context, null);
        init();
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public MySearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRectF = new RectF();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        draw(canvas, paint);
    }

    public void startAnim() {
        invalidate();
        if (mState == STATE_ANIM_START) return;
        mState = STATE_ANIM_START;
        ValueAnimator valueAnimator = startSearchViewAnim(0, 1, 500, null);
    }

    public void resetAnim() {
        if (mState == STATE_ANIM_STOP) return;
        mState = STATE_ANIM_STOP;
        ValueAnimator valueAnimator = startSearchViewAnim(0, 1, 500, null);
    }

    protected float[] mPos = new float[2];

    private ValueAnimator startSearchViewAnim(float startF, float endF, long time, final PathMeasure pathMeasure) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startF, endF);
        valueAnimator.setDuration(time);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mPro = (float) valueAnimator.getAnimatedValue();
                if (null != pathMeasure)
                    pathMeasure.getPosTan(mPro, mPos, null);
                invalidate();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        if (!valueAnimator.isRunning()) {
            valueAnimator.start();
        }
        mPro = 0;
        return valueAnimator;
    }


    public static final int STATE_ANIM_NONE = 0;
    public static final int STATE_ANIM_START = 1;
    public static final int STATE_ANIM_STOP = 2;
    private String mColor = "#4CAF50";

    @IntDef({STATE_ANIM_NONE, STATE_ANIM_START, STATE_ANIM_STOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    @State
    protected int mState = STATE_ANIM_NONE;

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(Color.parseColor(mColor));
        switch (mState) {
            case STATE_ANIM_NONE:
                drawNormalView(paint, canvas);
                break;
            case STATE_ANIM_START:
                drawStartAnimView(paint, canvas);
                break;
            case STATE_ANIM_STOP:
                drawStopAnimView(paint, canvas);
                break;
        }
    }

    private int cx, cy, cr;
    private RectF mRectF;

    private void drawNormalView(Paint paint, Canvas canvas) {
        cr = getWidth() / 24;
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        mRectF.left = cx - cr;
        mRectF.right = cx + cr;
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;

        canvas.save();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
        paint.setStyle(Paint.Style.STROKE);

        canvas.rotate(45, cx, cy);
        canvas.drawLine(cx + cr, cy, cx + cr * 2, cy, paint);
        canvas.drawArc(mRectF, 0, 360, false, paint);
        canvas.restore();
    }

    protected float mPro = -1;
    private int j = 10;

    private void drawStartAnimView(Paint paint, Canvas canvas) {
        canvas.save();
        if (mPro <= 0.5f) {
            canvas.drawArc(mRectF, 45, -(360 - 360 * 2 * (mPro == -1 ? 1 : mPro)), false, paint);
            canvas.drawLine(mRectF.right - j, mRectF.bottom - j,
                    mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        } else {
            canvas.drawLine(mRectF.right - j + cr * mPro, mRectF.bottom - j + cr * mPro,
                    mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        }

        canvas.drawLine((mRectF.right + cr - j) * (1 - mPro * .8f), mRectF.bottom + cr - j,
                mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        canvas.restore();

        mRectF.left = cx - cr + 240 * mPro;
        mRectF.right = cx + cr + 240 * mPro;
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;
    }

    private void drawStopAnimView(Paint paint, Canvas canvas) {
        canvas.save();
        canvas.drawLine((mRectF.right + cr - j) * (mPro >= 0.2f ? mPro : 0.2f),
                mRectF.bottom + cr - j, mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        if (mPro > 0.5f) {
            canvas.drawArc(mRectF, 45, -((mPro - 0.5f) * 360 * 2), false, paint);
            canvas.drawLine(mRectF.right - j, mRectF.bottom - j,
                    mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        } else {
            canvas.drawLine(mRectF.right - j + cr * (1 - mPro), mRectF.bottom - j +
                    cr * (1 - mPro), mRectF.right + cr - j, mRectF.bottom + cr - j, paint);
        }
        canvas.restore();
        mRectF.left = cx - cr + 240 * (1 - mPro);
        mRectF.right = cx + cr + 240 * (1 - mPro);
        mRectF.top = cy - cr;
        mRectF.bottom = cy + cr;
    }


}
