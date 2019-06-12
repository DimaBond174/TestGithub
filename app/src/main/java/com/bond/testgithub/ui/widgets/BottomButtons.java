package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bond.testgithub.ui.SpecTheme;

/**
 * Created by dbond on 28.10.17.
 * Кнопки снизу экрана
 */

public class BottomButtons extends FrameLayout {
    BottomButton [] bottomButton = null;
    int buttonArrLen = 0;
    int color = 0; //Цвет иконок и кноп

    int backDefColor      = 0xffffffff;
    int backSelectedColor = 0xffB0E0E6; //Подсветка на MouseDown
    float lineThick  = 2; //Толщина линий
    int iconSize   = 20; //Размер иконки
    int halfIconSize   = 10; //Размер иконки
    int fontSize   = 12; //Размер подписи под иконкой
    final int MARGIN  = 5; //Отступы линий
    final int MARGIN_2  = MARGIN+MARGIN; //Отступы линий
    LightingColorFilter lcf = new LightingColorFilter( 0, color);
    BottomButtonsCallback callback = null;
    int buttonWidht = 0; //Ширина кнопок
    int buttonWidhtWMargin = 0; //Ширина кнопок + 2 MARGIN слева и справа
    int widgetWidht  = 0;
    int widgetHeight = 0;
    final Paint paint = new Paint();

    public interface BottomButtonsCallback {
        /**
         * Будет вызван при клике на кнопку
         * @param rIconID - кнопку идентифицировать по ID иконки
         */
        void onBottomButtonClick(int rIconID);
    }

    public BottomButtons(Context context) {
        super(context);
    }

    public BottomButtons(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomButtons(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * Добавление новой кнопки
     * @param rIcon - R.drawable.IDиконки == идентификатор на что кликнули
     * @param rString - R.string.IDподписи
     */
    public void addButton(int rIcon, int rString) {
        int len = null==bottomButton?1:bottomButton.length+1;
        BottomButton [] newarr = new BottomButton[len];
        if (len>1) {
            for (int i=0; i<bottomButton.length; ++i) {
                newarr[i]=bottomButton[i];
            }
        }
        --len;
        newarr[len]=new BottomButton(SpecTheme.context,  rIcon,  rString);
        /* Если не добавить addView, то не будет транслироваться события onClick: */
        addView(newarr[len], new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        bottomButton=newarr;
        buttonArrLen = newarr.length;
    }


    /**
     * Установка цветов
     * @param color - цвет линий и иконок
     * @param backDefColor - цвет фона
     * @param backSelectedColor - цвет подсветки на MouseDown
     * @param lineThick  - толщина линий
     * @param widgetHeight  - высота виджета
     * @param iconSize  - размер иконки
     * @param fontSize  - размер шрифта подписиs
     * @param callback  - что вызвать на клик
     */
    public void init(int color, int backDefColor, int backSelectedColor, float lineThick,
                     int widgetHeight, int iconSize, int fontSize,
                     BottomButtonsCallback callback) {
        this.callback=callback;
        this.color = color;
        this.backDefColor = backDefColor;
        this.backSelectedColor = backSelectedColor;
        this.widgetHeight = widgetHeight;
        this.lineThick = lineThick;
        this.iconSize = iconSize;
        halfIconSize = iconSize/2;
        this.fontSize = fontSize;
        lcf = new LightingColorFilter(0, color);
        if (null!=bottomButton) {
            for (int i = 0; i < bottomButton.length; ++i) {
                bottomButton[i].setParentsColor();
            }
        }
        paint.setColor(color);
        setBackgroundColor(backDefColor);
        setWillNotDraw(false);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widgetWidht  = MeasureSpec.getSize(widthMeasureSpec);
        if (null!=bottomButton) {
            buttonWidhtWMargin = widgetWidht / bottomButton.length;
            buttonWidht = buttonWidhtWMargin - MARGIN_2;
            int widthMeasureSpecInside = MeasureSpec.makeMeasureSpec(buttonWidht, MeasureSpec.EXACTLY);
            int heightMeasureSpecInside = MeasureSpec.makeMeasureSpec(widgetHeight, MeasureSpec.EXACTLY);
            for (int i=0; i<buttonArrLen; ++i) {
                bottomButton[i].measure(widthMeasureSpecInside, heightMeasureSpecInside);
            }
        }

        /* Скажем наверх насколько мы большие */
        setMeasuredDimension(widgetWidht, widgetHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
        if (null!=bottomButton) {
            int b_left=MARGIN;
            int b_right=b_left+buttonWidht;
            //int b_top = MARGIN;
            int b_bottom = widgetHeight - MARGIN;
            for (int i=0; i<buttonArrLen; ++i) {
                bottomButton[i].layout(b_left, MARGIN, b_right, b_bottom);
                b_left+=buttonWidhtWMargin;
                b_right+=buttonWidhtWMargin;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* Границы */
        int height = getHeight();
        int width  = getWidth();
        /* Верхнаяя полоска */
        canvas.drawRect(0, 0, width, lineThick, paint);
        /* Между кнопок полоски */
        if (null!=bottomButton) {
            int b_left=buttonWidhtWMargin;
            float b_right=buttonWidhtWMargin + lineThick;
            int b_top = MARGIN;
            int b_bottom = widgetHeight - MARGIN;
            for (int i=1; i<buttonArrLen; ++i) {
                canvas.drawRect(b_left, b_top, b_right, b_bottom, paint);
                b_left+=buttonWidhtWMargin;
                b_right+=buttonWidhtWMargin;
            }
        }
        /* Кнопки не рисовались так как не доавил через addView
        * так можно нарисовать, но onClick работать не будет: */
//        if (null!=bottomButton) {
//            for (int i=0; i<buttonArrLen; ++i) {
//                bottomButton[i].draw(canvas);
//            }
//        }
    }



    /* Одна кнопка снизу */
    public class BottomButton extends FrameLayout implements OnTouchListener,
        OnClickListener {
        ImageView imageView;
        TextView textView;
        int rIcon;


        public BottomButton(Context context, int rIcon, int rString) {
            super(context);
            this.rIcon=rIcon;
            Resources res = context.getResources();
            imageView = new ImageView(context);
            //imageView.setImageDrawable(res.getDrawable(rIcon));
            imageView.setImageDrawable(AppCompatResources.getDrawable(context, rIcon));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //imageView.setOnTouchListener(this);
            addView(imageView, new FrameLayout.LayoutParams(iconSize, iconSize));

            textView = new TextView(context);
            textView.setSingleLine(true);
            textView.setMaxLines(1);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSize);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setText(res.getText(rString));
            //textView.setOnTouchListener(this);
            addView(textView, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

            setParentsColor();
            setWillNotDraw(false);
            //setClickable(true);
            setOnTouchListener(this);
            setOnClickListener(this);
            //bringToFront();

        }


        @Override
        public void onClick(View v) {
            //Toast.makeText(UiRoot.getInstance().getForDialogCtx(), "BottomButton Click ", Toast.LENGTH_SHORT).show();
            callback.onBottomButtonClick(rIcon);
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //handler.postDelayed(runnable, STEP_DELAY);
                    setHighlighted(true);
                    //return true;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    setHighlighted(false);
                    //userImage.animate().cancel();
                    //handler.removeCallbacks(mRunnable);
                    //return false;
                    break;
            }
            return false;
        }

        public void setParentsColor() {
            imageView.clearColorFilter();
            imageView.setColorFilter(lcf);
            textView.setTextColor(color);
        }


        /* Выделенное сообщение */
        public void setHighlighted(boolean hiLight) {
            if (hiLight) {
                setBackgroundColor(backSelectedColor);
            } else {
               // setBackgroundColor(backDefColor);
                setBackground(null);
            }
            invalidate();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            int widht = MeasureSpec.getSize(widthMeasureSpec);
            measureChildWithMargins(textView, widthMeasureSpec, 0,
                    heightMeasureSpec, 0);
            /* Скажем наверх насколько мы большие */
            setMeasuredDimension(widht, height);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            super.onLayout(changed, left, top, right, bottom);
            int buttonWidht = right-left;
            int buttonHeight = bottom-top;
            int centerHeight = buttonHeight >> 4;
            int centerWidht  = buttonWidht >> 1;
            //int imgSize =imageView.getMeasuredHeight()/2;
            int textHeight = textView.getMeasuredHeight();
            int textWidht = (textView.getMeasuredWidth()) >> 1;
            imageView.layout(centerWidht-halfIconSize,centerHeight,centerWidht+halfIconSize,centerHeight+iconSize);
            //imageView.layout(left+centerWidht-halfIconSize,centerHeight,left+centerWidht+halfIconSize,centerHeight+iconSize);
            textView.layout(centerWidht-textWidht,buttonHeight-textHeight,centerWidht+textWidht,buttonHeight);
            //textView.layout(left+centerWidht-textWidht,buttonHeight-textHeight,left+centerWidht+textWidht,buttonHeight);
            //textView.layout(5,5,textView.getMeasuredWidth(),textView.getMeasuredHeight());
        }

//        final RectF rect = new RectF();
//        @Override
//        protected void onDraw(Canvas canvas) {
//        /* Границы */
//            int height = getHeight();
//            int width = getWidth();
//            rect.set(5, 5, width, height);
//            canvas.drawRoundRect(rect, 10,10, paint);
//
//        }
    }

}
