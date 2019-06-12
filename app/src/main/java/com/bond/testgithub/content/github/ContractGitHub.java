package com.bond.testgithub.content.github;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContractGitHub {
  /**
   * The Content Authority is a name for the entire content provider, similar to the relationship
   * between a domain name and its website. A convenient string to use for content authority is
   * the package name for the app, since it is guaranteed to be unique on the device.
   */
  public static final String CONTENT_AUTHORITY = "com.bond.testgithub.content.github";

  /**
   * The content authority is used to create the base of all URIs which apps will use to
   * contact this content provider.
   */
  private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  /**
   * A list of possible paths that will be appended to the base URI for each of the different
   * tables/ resources. Aka REST service
   */
  public static final String PATH_CASE_QUERY1 = "query1";

  /**
   * Create one class for each table that handles all information regarding the table schema and
   * the URIs related to it.
   */
  public static final class CaseStringEntry implements BaseColumns {
    // Content URI represents the base location for the table
    public static final Uri CONTENT_URI =
        BASE_CONTENT_URI.buildUpon().appendPath(PATH_CASE_QUERY1).build();

    // These are special type prefixes that specify if a URI returns a list or a specific item
    public static final String CONTENT_TYPE =
        "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_CASE_QUERY1;
    public static final String CONTENT_ITEM_TYPE =
        "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CASE_QUERY1;

    // Define the table schema
    public static final String TABLE_NAME = "query1_result";
    public static final String COLUMN_NAME = "json";

    // Define a function to build a URI to find a specific item by it's identifier
    public static Uri buildItemUri(long id){
      return ContentUris.withAppendedId(CONTENT_URI, id);
    }
  }

}
