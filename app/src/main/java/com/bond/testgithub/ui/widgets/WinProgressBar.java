package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bond.testgithub.R;

public class WinProgressBar extends LinearLayout {
  ProgressBar progressBarIn;

  public WinProgressBar(Context context) {
    super(context);
    LayoutInflater inflater = LayoutInflater.from(context);
    View mainView = inflater.inflate(R.layout.progresbar_buben, null);
    addView(mainView, new FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT));

    progressBarIn = mainView.findViewById(R.id.progressBar1);
  }

  public void setProgress(int progress) {
    progressBarIn.setProgress(progress);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    measureChildWithMargins(progressBarIn,  widthMeasureSpec, 0,
        heightMeasureSpec, 0);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    int width = r - l;
    int height = b - t;
    progressBarIn.layout(0, 0, width, height);
  }
}
