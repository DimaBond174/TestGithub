package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bond.testgithub.R;
import com.bond.testgithub.i.ImageFetcherCallBack;
import com.bond.testgithub.objs.RecyclerDataItem;
import com.bond.testgithub.ui.SpecTheme;

import org.json.JSONObject;

import java.lang.ref.SoftReference;


public class WidGithub_for_detail extends FrameLayout
        implements ImageFetcherCallBack {
    static final String TAG = "WidGithub_for_detail";
    public ImageView imageView;
    public TextView textLogin;
    public TextView textLoginCaption;
    public TextView textRepo;
    public TextView textRepoCaption;
    public TextView textDesc;
    public TextView textDescCaption;
    public TextView textForks;
    public TextView textForksCaption;
    public TextView textStars;
    public TextView textStarsCaption;
    public TextView textCreated;
    public TextView textCreatedCaption;
    RecyclerDataItem dataPtr = null; // Any Data
    int max_width = 0;

    public WidGithub_for_detail(Context context) {
        super(context);
        createViews(context);
    }

    public WidGithub_for_detail(Context context, AttributeSet attrs) {
        super(context, attrs);
        createViews(context);
    }

    public WidGithub_for_detail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        createViews(context);
    }



    void createViews(Context  context) {

//    3. Экран детального вида репозитория. Отображается
//    Имя репозитория = "full_name": "xctom/Nand2Tetris",
//        автор (логин + аватар) = "owner": {  "login": "DimaBond174", "avatar_url": "https://avatars0.githubusercontent.com/u/11298854?v=4"

        imageView = new ImageView(context);
        //imageView.setImageDrawable(res.getDrawable(rIcon));
        imageView.setImageDrawable(SpecTheme.github_icon);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        //imageView.setOnTouchListener(this);
        addView(imageView, new LayoutParams(
            SpecTheme.dpBigAva, SpecTheme.dpBigAva));


        textLogin = new TextView(context);
        textLogin.setSingleLine(true);
        textLogin.setMaxLines(1);
        textLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textLogin.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textLogin, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textLoginCaption = new TextView(context);
        textLoginCaption.setSingleLine(true);
        textLoginCaption.setMaxLines(1);
        textLoginCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textLoginCaption.setTextColor(SpecTheme.STextColor);
        textLoginCaption.setText(context.getText(R.string.caption_login));
        //textView.setOnTouchListener(this);
        addView(textLoginCaption, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textRepo = new TextView(context);
        textRepo.setSingleLine(true);
        textRepo.setMaxLines(1);
        textRepo.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textRepo.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textRepo, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textRepoCaption = new TextView(context);
        textRepoCaption.setSingleLine(true);
        textRepoCaption.setMaxLines(1);
        textRepoCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textRepoCaption.setTextColor(SpecTheme.STextColor);
        textRepoCaption.setText(context.getText(R.string.caption_repo));
        //textView.setOnTouchListener(this);
        addView(textRepoCaption, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

//        , описание: "description": "Secure messenger (media: Internet, Wi-Fi, LPWAN, ..) to overcome censorship and blocking"

        textDesc = new TextView(context);
        textDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textDesc.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textDesc, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textDescCaption = new TextView(context);
        textDescCaption.setSingleLine(true);
        textDescCaption.setMaxLines(1);
        textDescCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textDescCaption.setTextColor(SpecTheme.STextColor);
        textDescCaption.setText(context.getText(R.string.caption_desc));
        //textView.setOnTouchListener(this);
        addView(textDescCaption, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

//        , количество fork: "forks": 0

        textForks = new TextView(context);
        textForks.setSingleLine(true);
        textForks.setMaxLines(1);
        textForks.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textForks.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textForks, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textForksCaption = new TextView(context);
        textForksCaption.setSingleLine(true);
        textForksCaption.setMaxLines(1);
        textForksCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textForksCaption.setTextColor(SpecTheme.STextColor);
        textForksCaption.setText(context.getText(R.string.caption_fork));
        //textView.setOnTouchListener(this);
        addView(textForksCaption, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

//        , количество star: "stargazers_count": 7
        textStars = new TextView(context);
        textStars.setSingleLine(true);
        textStars.setMaxLines(1);
        textStars.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textStars.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textStars, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textStarsCaption = new TextView(context);
        textStarsCaption.setSingleLine(true);
        textStarsCaption.setMaxLines(1);
        textStarsCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textStarsCaption.setTextColor(SpecTheme.STextColor);
        textStarsCaption.setText(context.getText(R.string.caption_stars));
        //textView.setOnTouchListener(this);
        addView(textStarsCaption, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

//        , дата создания: "created_at": "2018-10-15T04:15:13Z"
        textCreated = new TextView(context);
        textCreated.setSingleLine(true);
        textCreated.setMaxLines(1);
        textCreated.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.PTextSize);
        textCreated.setTextColor(SpecTheme.PTextColor);
        //textView.setOnTouchListener(this);
        addView(textCreated, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        textCreatedCaption = new TextView(context);
        textCreatedCaption.setSingleLine(true);
        textCreatedCaption.setMaxLines(1);
        textCreatedCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
            SpecTheme.STextSize);
        textCreatedCaption.setTextColor(SpecTheme.STextColor);
        textCreatedCaption.setText(context.getText(R.string.caption_date));
        //textView.setOnTouchListener(this);
        addView(textCreatedCaption, new LayoutParams(
            LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

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


    public void setData(RecyclerDataItem data) {
        dataPtr = data;
        imageView.setImageDrawable(SpecTheme.github_icon);
        if (null == data)  {
            textLogin.setText(null);
            textRepo.setText(null);
            textDesc.setText(null);
            textForks.setText(null);
            textStars.setText(null);
            textCreated.setText(null);
        }  else  {
            textLogin.setText(data.str1);
            textRepo.setText(data.str2);
            setImageFromFetcher(
                SpecTheme.imageFetcher.loadImage(this,
                    data.img_url, SpecTheme.dpButtonImgSize, 0));
            try {
                JSONObject jsonParsed = null;
                if (null != data.jsonParsed) {
                    jsonParsed = data.jsonParsed.get();
                }
                if (null == jsonParsed) {
                    jsonParsed = new JSONObject(data.json);
                    data.jsonParsed = new SoftReference<>(jsonParsed);
                }
                textDesc.setText(jsonParsed.getString("description"));
                textForks.setText(jsonParsed.getString("forks"));
                textStars.setText(jsonParsed.getString("stargazers_count"));
                textCreated.setText(jsonParsed.getString("created_at"));
            } catch (Exception e) {
                Log.e(TAG, "setData(): ", e);
            }
        }
        requestLayout();
    }

    RecyclerDataItem getData() {
        return dataPtr;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width     = MeasureSpec.getSize(widthMeasureSpec);
        measureChildWithMargins(textLoginCaption,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textRepoCaption,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textDescCaption,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textForksCaption,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textStarsCaption,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textCreatedCaption,
            widthMeasureSpec, 0,
            heightMeasureSpec, 0);
        max_width = textLoginCaption.getMeasuredWidth();
        int w = textRepoCaption.getMeasuredWidth();
        if (w > max_width)  {  max_width = w;  }
        w = textDescCaption.getMeasuredWidth();
        if (w > max_width)  {  max_width = w;  }
        w = textForksCaption.getMeasuredWidth();
        if (w > max_width)  {  max_width = w;  }
        w = textStarsCaption.getMeasuredWidth();
        if (w > max_width)  {  max_width = w;  }
        w = textCreatedCaption.getMeasuredWidth();
        if (w > max_width)  {  max_width = w;  }
        max_width += SpecTheme.dpButton2Padding;
        w = MeasureSpec.makeMeasureSpec(width - max_width, MeasureSpec.AT_MOST);
        measureChildWithMargins(textLogin,  w, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textRepo,  w, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textDesc,  w, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textForks,  w, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textStars,  w, 0,
            heightMeasureSpec, 0);
        measureChildWithMargins(textCreated,  w, 0,
            heightMeasureSpec, 0);
        w = MeasureSpec.makeMeasureSpec(SpecTheme.dpBigAva, MeasureSpec.EXACTLY);
        measureChildWithMargins(imageView,  w, 0,  w, 0);
        int height = textLogin.getMeasuredHeight() + SpecTheme.dpButtonPadding
            + textRepo.getMeasuredHeight() + SpecTheme.dpButtonPadding
            + textDesc.getMeasuredHeight() + SpecTheme.dpButtonPadding
            + textForks.getMeasuredHeight() + SpecTheme.dpButtonPadding
            + textStars.getMeasuredHeight() + SpecTheme.dpButtonPadding
            + textCreated.getMeasuredHeight() + SpecTheme.dpButtonPadding
            + SpecTheme.dpBigAva + SpecTheme.dpButton2Padding;
        /* Скажем наверх насколько мы большие */
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
        int width = right - left;
        int height = bottom - top;
        int half_width = width >> 1;
        int cur_top = SpecTheme.dpButtonPadding + SpecTheme.dpBigAva;
        imageView.layout(half_width - SpecTheme.dpBigAvaHalf, SpecTheme.dpButtonPadding,
            half_width + SpecTheme.dpBigAvaHalf,
            cur_top);
        cur_top += SpecTheme.dpButton2Padding;

        textLoginCaption.layout(SpecTheme.dpButtonPadding, cur_top,
            SpecTheme.dpButtonPadding + textLoginCaption.getMeasuredWidth(),
            cur_top +  textLoginCaption.getMeasuredHeight());
        int cur_bottom = cur_top +  textLogin.getMeasuredHeight();
        textLogin.layout(max_width , cur_top,
            max_width + textLogin.getMeasuredWidth(), cur_bottom );
        cur_top = cur_bottom + SpecTheme.dpButtonPadding;

        textRepoCaption.layout(SpecTheme.dpButtonPadding, cur_top,
            SpecTheme.dpButtonPadding + textRepoCaption.getMeasuredWidth(),
            cur_top +  textRepoCaption.getMeasuredHeight());
        cur_bottom = cur_top +  textRepo.getMeasuredHeight();
        textRepo.layout(max_width, cur_top,
            max_width + textRepo.getMeasuredWidth(), cur_bottom );
        cur_top = cur_bottom + SpecTheme.dpButtonPadding;

        textDescCaption.layout(SpecTheme.dpButtonPadding, cur_top,
            SpecTheme.dpButtonPadding + textDescCaption.getMeasuredWidth(),
            cur_top +  textRepoCaption.getMeasuredHeight());
        cur_bottom = cur_top +  textDesc.getMeasuredHeight();
        textDesc.layout(max_width, cur_top,
            max_width + textDesc.getMeasuredWidth(), cur_bottom );
        cur_top = cur_bottom + SpecTheme.dpButtonPadding;

        textForksCaption.layout(SpecTheme.dpButtonPadding, cur_top,
            SpecTheme.dpButtonPadding + textForksCaption.getMeasuredWidth(),
            cur_top +  textForksCaption.getMeasuredHeight());
        cur_bottom = cur_top +  textForks.getMeasuredHeight();
        textForks.layout(max_width, cur_top,
            max_width + textForks.getMeasuredWidth(), cur_bottom );
        cur_top = cur_bottom + SpecTheme.dpButtonPadding;

        textStarsCaption.layout(SpecTheme.dpButtonPadding, cur_top,
            SpecTheme.dpButtonPadding + textStarsCaption.getMeasuredWidth(),
            cur_top +  textStarsCaption.getMeasuredHeight());
        cur_bottom = cur_top +  textStars.getMeasuredHeight();
        textStars.layout(max_width, cur_top,
            max_width + textForks.getMeasuredWidth(), cur_bottom );
        cur_top = cur_bottom + SpecTheme.dpButtonPadding;

        textCreatedCaption.layout(SpecTheme.dpButtonPadding, cur_top,
            SpecTheme.dpButtonPadding + textCreatedCaption.getMeasuredWidth(),
            cur_top +  textCreatedCaption.getMeasuredHeight());
        cur_bottom = cur_top +  textStars.getMeasuredHeight();
        textCreated.layout(max_width, cur_top,
            max_width + textCreated.getMeasuredWidth(), cur_bottom );
        cur_top = cur_bottom + SpecTheme.dpButtonPadding;
    }

}
