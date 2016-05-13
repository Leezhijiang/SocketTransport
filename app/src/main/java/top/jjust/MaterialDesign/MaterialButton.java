package top.jjust.MaterialDesign;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import top.jjust.common.Loger;
import top.jjust.sockettransport.R;

/**
 * Created by lee on 16-5-11.
 */
public class MaterialButton extends View{
    private double[] CirclePoint = new double[2];
    private Paint p = new Paint();
    private boolean up = true;
    private int color;
    private int rectColor;
    /**
     * 代码new
     * @param context
     */
    public MaterialButton(Context context) {
        super(context);
    }

    /**
     * xml调用
     * @param context
     * @param attrs
     */
    public MaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialButton);
        int count = ta.getIndexCount();
        for(int i =0;i<count;i++){
            switch (i){
                case R.styleable.MaterialButton_up:
                    up = ta.getBoolean(i,true);
                    break;
                case R.styleable.MaterialButton_mcolor:
                    color = ta.getColor(i, Color.BLACK);
                    break;
                case R.styleable.MaterialButton_RectColor:
                    rectColor = ta.getColor(i,Color.BLACK);
                    break;
            }
        }
        initButton();
    }

    /**
     * 初始化方法
     */
    private void initButton() {
        p.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Animation anim = new TranslateAnimation(getX(),getX(),getY(),getY()-getHeight());
        anim.setDuration(1000);
        startAnimation(anim);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(up) {
            p.setColor(rectColor);
            p.setAlpha(255);
            canvas.drawRect(getWidth() / 2 - 15, 0, getWidth() / 2 + 15, getHeight() - 10, p);
            p.setAlpha(150);
            canvas.drawRect(getWidth() / 2 - 20, 0, getWidth() / 2 + 20, getHeight() - 10, p);
            p.setColor(Color.BLACK);
            p.setAlpha(60);
            canvas.drawRect(getWidth() / 2 - 25, 0, getWidth() / 2 + 22, getHeight() - 10, p);
            float r = (getWidth() - 20) / 2;
            p.setColor(color);
            p.setAlpha(255);
            canvas.drawCircle(getWidth() / 2, getHeight() - r, r - 10, p);
            p.setAlpha(150);
            canvas.drawCircle(getWidth() / 2, getHeight() - r, r-5, p);
            p.setColor(Color.BLACK);
            p.setAlpha(60);
            canvas.drawCircle(getWidth() / 2, getHeight() - r, r -3, p);
        }else {
            p.setColor(rectColor);
            p.setAlpha(255);
            canvas.drawRect(getWidth() / 2 - 15, 10, getWidth() / 2 + 15, getHeight(), p);
            p.setAlpha(150);
            canvas.drawRect(getWidth() / 2 - 20, 10, getWidth() / 2 + 20, getHeight(), p);
            p.setColor(Color.BLACK);
            p.setAlpha(60);
            canvas.drawRect(getWidth() / 2 - 25, 10, getWidth() / 2 + 22, getHeight(), p);
            p.setColor(color);
            float r = (getWidth() - 20) / 2;
            p.setAlpha(150);
            canvas.drawCircle(getWidth() / 2, r, r - 5, p);
            p.setAlpha(255);
            canvas.drawCircle(getWidth() / 2, r, r - 10, p);
            p.setColor(Color.BLACK);
            p.setAlpha(60);
            canvas.drawCircle(getWidth() / 2,r, r-3, p);
        }
    }
}
