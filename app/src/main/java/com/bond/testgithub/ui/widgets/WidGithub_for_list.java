package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bond.testgithub.R;
import com.bond.testgithub.i.ImageFetcherCallBack;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;


public class WidGithub_for_list extends FrameLayout
        implements ImageFetcherCallBack  {
    ImageView imageView;
    TextView textLogin;
    TextView textRepo;
    RecyclerDataItem dataPtr = null; // Any Data

    int curHeight = 0;


    public WidGithub_for_list(Context context) {
        super(context);
        createViews(context);
    }

    public WidGithub_for_list(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews(context);
    }

    public WidGithub_for_list(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews(context);
    }

    void createViews(Context  context) {
        imageView = new ImageView(context);
        //imageView.setImageDrawable(res.getDrawable(rIcon));
        imageView.setImageDrawable(SpecTheme.github_icon);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageView.setOnTouchListener(this);
        addView(imageView, new LayoutParams(
            SpecTheme.dpButtonImgSize, SpecTheme.dpButtonImgSize));

        textLogin = new TextView(context);
        textLogin.setSingleLine(true);
        textLogin.setMaxLines(1);
        textLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textLogin.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textLogin, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textRepo = new TextView(context);
        textRepo.setSingleLine(true);
        textRepo.setMaxLines(1);
        textRepo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textRepo.setTextColor(SpecTheme.STextColor);
        //textView.setOnTouchListener(this);
        addView(textRepo, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        setWillNotDraw(false);
    }

    @Override
    public void setImageFromFetcher(BitmapDrawable img) {
        if (null == img) {
            imageView.setImageDrawable(SpecTheme.github_icon);
        } else {
            imageView.setImageDrawable(img);
        }
        requestLayout();
    }

    public RecyclerDataItem  getDataPtr() {  return dataPtr;  }

    public void setData(RecyclerDataItem data) {
        dataPtr = data;
        imageView.setImageDrawable(SpecTheme.github_icon);
        if (null == data)  {
            textLogin.setText(null);
            textRepo.setText(null);
        }  else  {
            textLogin.setText(data.str1);
            textRepo.setText(data.str2);
            setImageFromFetcher(
                SpecTheme.imageFetcher.loadImage(this,
                    data.img_url, SpecTheme.dpButtonImgSize, 0));
        }
        requestLayout();
    }

    RecyclerDataItem getData() {
        return dataPtr;
    }


    volatile int  hilight_lvl  =  100;
    final Runnable apply_hilight = new Runnable() {
        @Override
        public void run() {
            try {
                if (hilight_lvl > 0) {
                    --hilight_lvl;
                    setHighlighted(hilight_lvl);
                }
                if (hilight_lvl > 0) {
                    SpecTheme.iActivity.runOnGUIthreadDelay(apply_hilight, 20);
                }
            } catch (Exception e) {}
        }
    };




    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hilight_lvl = 100;
                apply_hilight.run();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                hilight_lvl = 0;
                setHighlighted(0);
                break;
        }
        return false;
    }

    public void setHighlighted(int alpha) {
        if (alpha>0) {
            int res = (SpecTheme.HiLightColor & 0x00ffffff) | (alpha << 24);
            setBackgroundColor(res);
        } else {
            setBackground(null);
            hilight_lvl = 0;
            //setBackgroundColor(backColor);
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            int buttonWidht  = MeasureSpec.getSize(widthMeasureSpec);
//            int buttonHeight  = MeasureSpec.getSize(heightMeasureSpec);
        int widht = MeasureSpec.getSize(widthMeasureSpec);
        int text_width = widht - SpecTheme.dpButton2Padding - SpecTheme.dpButtonImgSize;
        int text_width_widthSpec = MeasureSpec.makeMeasureSpec(text_width, MeasureSpec.AT_MOST);

        measureChildWithMargins(textLogin, text_width_widthSpec, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textRepo, text_width_widthSpec, 0,
            heightMeasureSpec, 0);
        int height = textLogin.getMeasuredHeight()
            + textRepo.getMeasuredHeight() +  SpecTheme.dpButtonPadding;
        curHeight = height < SpecTheme.dpButtonTouchSize?
            SpecTheme.dpButtonTouchSize : height;
        /* Скажем наверх насколько мы большие */
        setMeasuredDimension(widht, curHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int buttonWidht = right-left;
        imageView.layout(SpecTheme.dpButtonPadding,
            curHeight - SpecTheme.dpButtonPadding - SpecTheme.dpButtonImgSize,
            SpecTheme.dpButtonPadding + SpecTheme.dpButtonImgSize,
            curHeight - SpecTheme.dpButtonPadding);

        textLogin.layout(SpecTheme.dpButton2Padding + SpecTheme.dpButtonImgSize,
            0,
            buttonWidht,
            textLogin.getMeasuredHeight());

        textRepo.layout(SpecTheme.dpButton2Padding + SpecTheme.dpButtonImgSize,
            curHeight  - textRepo.getMeasuredHeight(),
            buttonWidht,
            curHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(SpecTheme.dpButton2Padding + SpecTheme.dpButtonImgSize,
            curHeight,
            getWidth(),
            curHeight, SpecTheme.paintLine );
    }

}
