package top.jjust.MaterialDesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import top.jjust.sockettransport.R;

/**
 * Created by lee on 16/5/14.
 */
public class MaterialProcess extends View{
    private Paint p = new Paint();
    int[] point = new int[2];
    int r ;
    public MaterialProcess(Context context) {
        super(context);
    }

    public MaterialProcess(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MaterialSwitch);
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++) {
                switch (i) {
                    case R.styleable.MaterialSwitch_uColor:
                        break;
                    case R.styleable.MaterialSwitch_dColor:
                        break;
                    case R.styleable.MaterialSwitch_uText:
                        break;
                    case R.styleable.MaterialSwitch_dText:
                        break;
                }
            }
        }
        init();
        init();
    }

    private void init() {
        point[0] = getWidth()/2;
        point[1] = getHeight()/2;
        r = Math.min(getWidth(),getHeight())/2;
    }

    public MaterialProcess(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(r/4);
        RectF oval = new RectF(point[0]-r,point[1]-r,point[0]+r,point[1]+r);
        canvas.drawArc(oval,0,180,false,p);
    }
}
