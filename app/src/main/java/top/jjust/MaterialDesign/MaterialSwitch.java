package top.jjust.MaterialDesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.Date;

import top.jjust.common.Loger;
import top.jjust.sockettransport.R;

/**
 * Created by lee on 16/5/13.
 */
public class MaterialSwitch extends View{
    private Paint uPaint = new Paint();
    private Paint dPaint = new Paint();
    private int uColor = Color.BLACK;
    private int dColor = Color.BLACK;
    private int[] point = new int[2];
    private int[] upoint = new int[2];
    private int[] dpoint = new int[2];
    private int shadow =4;
    private int shadowAlpha = 40;
    private int uoffSet = -50;
    private int doffSet = 50;
    private long startTime = 0;
    private long duration = 500;
    private boolean isRunning = false;
    private boolean up= true;
    private OnUpClickListener onUpClickListrner;
    private OnDownClickListener onDownClickListrner;
    private String uText ="";
    private String dText ="";
    private int r;



    public MaterialSwitch(Context context) {
        super(context);
    }

    public MaterialSwitch(Context context, AttributeSet attrs) {

        super(context, attrs);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialSwitch);
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++) {
                switch (i) {
                    case R.styleable.MaterialSwitch_uColor:
                        uColor = ta.getColor(i, Color.BLACK);
                        break;
                    case R.styleable.MaterialSwitch_dColor:
                        dColor = ta.getColor(i, Color.BLACK);
                        break;
                    case R.styleable.MaterialSwitch_uText:
                        uText = String.valueOf(ta.getText(i));
                        break;
                    case R.styleable.MaterialSwitch_dText:
                        dText = String.valueOf(ta.getText(i));
                        break;
                }
            }
        }
        init();
    }

    /**
     * 测量
     * @param nowTIme
     */
    private void flushPosition(long nowTIme,boolean up) {
        nowTIme = (new Date()).getTime();
        if(up) {
            int road = doffSet - uoffSet;
            uoffSet += ((int) (nowTIme - startTime)) * road / (int) duration;
        }else {
            int road = uoffSet - doffSet;
            doffSet += ((int) (nowTIme - startTime)) * road / (int) duration;
        }
    }
    private void flushdefalut(){
        uoffSet = -50;
        doffSet =50;
    }
    private  void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!isRunning) {
            isRunning = true;
            startTime = (new Date()).getTime();
            Loger.takeLog("y:" + event.getY() + "pointY:" + point[1]);
            if (event.getY() < point[1]) {
                this.up=true;
                handler.sendEmptyMessage(1);
            } else {
                this.up=false;
                handler.sendEmptyMessage(2);
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        uPaint.setAntiAlias(true);
        dPaint.setAntiAlias(true);
        drawCircle(canvas);
    }

    /**
     * 绘图
     * @param canvas
     */
    private void drawCircle(Canvas canvas){
        r = Math.min(getHeight(),getWidth())-10;

        point[0] = getWidth()/2;
        point[1] = getHeight()/2;
        upoint[0] = point[0];
        dpoint[0] = point[0];
        upoint[1]=point[1]+uoffSet;
        dpoint[1]=point[1]+doffSet;
        uPaint.setColor(Color.BLACK);
        uPaint.setAlpha(shadowAlpha);
        RectF oval = new RectF(upoint[0]-r/2-shadow, upoint[1]-r/2-shadow,upoint[0]+r/2+shadow, upoint[1]+r/2+shadow);
        canvas.drawArc(oval,180,180,true,uPaint);
        uPaint.setColor(uColor);
        uPaint.setAlpha(255);
        oval.set(upoint[0]-r/2, upoint[1]-r/2,upoint[0]+r/2, upoint[1]+r/2);
        canvas.drawArc(oval,180,180,true,uPaint);

        dPaint.setStrokeWidth(10);
        dPaint.setStyle(Paint.Style.STROKE);
        dPaint.setColor(Color.BLACK);
        dPaint.setAlpha(shadowAlpha);
        oval.set(dpoint[0]-r/2-shadow, dpoint[1]-r/2-shadow,dpoint[0]+r/2+shadow, dpoint[1]+r/2+shadow);
        canvas.drawArc(oval,0,180,true,dPaint);
        dPaint.setColor(dColor);
        dPaint.setAlpha(255);
        oval.set(dpoint[0]-r/2, dpoint[1]-r/2,dpoint[0]+r/2, dpoint[1]+r/2);
        canvas.drawArc(oval,0,180,true,dPaint);
        //写字
        drawText(canvas);
    }
    private void drawText(Canvas canvas){
        //上字体
        uPaint.setColor(dColor);
        uPaint.setTextSize(r/8);
        canvas.drawText(uText,upoint[0]-uPaint.measureText(uText)/2,upoint[1]-r/6,uPaint);
        //下字体
        dPaint.setColor(uColor);
        dPaint.setTextSize(r/8);
        uPaint.setARGB(255,238 ,238,238);

        canvas.drawText(dText,dpoint[0]-dPaint.measureText(uText)/2,dpoint[1]+r/6,dPaint);
        canvas.drawText(dText,dpoint[0]-dPaint.measureText(uText)/2,dpoint[1]+r/6,uPaint);
    }
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            long nowTIme = (new Date()).getTime();
            switch (msg.what){
                case 1://上半圆运动
                    flushPosition(nowTIme,true);
                    break;
                case 2://下半圆运动
                    flushPosition(nowTIme,false);
                    break;
                case 3://结束运动
                    //flushPosition(nowTIme,false);

                    if(up){
                        if(onUpClickListrner!=null){
                            onUpClickListrner.onclick();
                        }
                    }else {
                        if(onDownClickListrner!=null){
                            onDownClickListrner.onclick();
                        }

                    }
                    handler.sendEmptyMessageDelayed(4,500);
                    break;
                case 4://按钮归位
                    isRunning = false;
                    flushdefalut();
                    break;
            }
            // 刷新页面 执行onDraw()方法
            invalidate();

            if(nowTIme-startTime<=duration){
                handler.sendEmptyMessageDelayed(msg.what, 20);
            }else if(nowTIme-startTime>duration&&isRunning){
                isRunning = false;
                handler.sendEmptyMessage(3);
            }

        };
    };

    /**
     * 点击事件
     */
    public void setOnUpClickListener(OnUpClickListener onUpClickListener){
        this.onUpClickListrner = onUpClickListener;
    }
    public void setOnDownClickListener(OnDownClickListener onDownClickListener){
        this.onDownClickListrner = onDownClickListener;

    }
    public static abstract class OnUpClickListener{
        public abstract void onclick();
    }
    public static abstract class OnDownClickListener{
        public abstract void onclick();
    }

}
