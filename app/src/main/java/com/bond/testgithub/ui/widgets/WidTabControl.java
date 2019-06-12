package com.bond.testgithub.ui.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class WidTabControl extends FrameLayout {
  ArrayList<View> buttons = new ArrayList<>();

  public WidTabControl(@NonNull Context context, ArrayList<View> buttons) {
    super(context);
    this.buttons = buttons;
  }
} // WidTabControl
