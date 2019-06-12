package com.bond.testgithub.objs;

import org.json.JSONObject;

import java.lang.ref.SoftReference;

public class RecyclerDataItem {
  public String json = null;
  public String str1 = null;
  public String str2 = null;
  public String img_url = null;
  public SoftReference<JSONObject> jsonParsed = null;
  //SoftReference<BitmapDrawable> img = null;
}
