package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.bond.testgithub.R;
import com.bond.testgithub.ui.SpecTheme;


public class WidEditText extends FrameLayout {
    AppCompatEditText editText;

    public WidEditText(Context context) {
        super(context);
        createViews(context);
    }

    public WidEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews(context);
    }

    public WidEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews(context);
    }

    void createViews(Context  context) {

        editText = new AppCompatEditText(context);
        editText.setHint(R.string.str_search);
        editText.setSingleLine(true);
        editText.setMaxLines(1);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        editText.setTextColor(SpecTheme.PTextColor);
        editText.setBackgroundColor(SpecTheme.PWaitBarColor);
        addView(editText);
        editText.setText("");

        editText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width     = MeasureSpec.getSize(widthMeasureSpec);
        int sz = MeasureSpec.makeMeasureSpec(SpecTheme.dpButtonTouchSize, MeasureSpec.EXACTLY);
        int w = MeasureSpec.makeMeasureSpec(width - SpecTheme.dpButton2Padding, MeasureSpec.EXACTLY);
        //editText.measure(w, sz);
        measureChildWithMargins(editText,
            w, 0,
            sz, 0);
        /* Скажем наверх насколько мы большие */
        setMeasuredDimension(width, SpecTheme.dpButtonTouchSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
        editText.layout(SpecTheme.dpButtonPadding,0,
            right - left - SpecTheme.dpButtonPadding,
            SpecTheme.dpButtonTouchSize);
    }

}
