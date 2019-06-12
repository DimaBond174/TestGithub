package com.bond.testgithub.objs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.ConditionVariable;
import android.util.Log;

import com.bond.testgithub.common.StaticConsts;
import com.bond.testgithub.i.ImageFetcherCallBack;
import com.bond.testgithub.ui.SpecTheme;

import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Кэш && Утилиты по предоставлению рисунков нужного размера.
 */
//public class ImageFetcher extends ImageResizer {
public class ImageFetcher {
  private static final String TAG = "ImageFetcher";
  private final ConditionVariable conditionVariable = new ConditionVariable();
  private volatile int keepRunID  =  0;
  private volatile Thread localThread  =  null;
  private volatile boolean keepRun=false;

  final Map<ImageFetcherCallBack, ImgKey> mRequestQueue
            = new ConcurrentHashMap<ImageFetcherCallBack,ImgKey>();
  final OnCacheMMRUV<ImgKey,  BitmapDrawable> mRamCache  =
      new OnCacheMMRUV<>(100, 10);



    public void stop() {
        //started=false;
        try {
          keepRun  =  false;
          pauseWorkerThread();
          mRequestQueue.clear();

        } catch (Exception e) {}
    }


    public void start() {
        try {
            resumeWorkerThread();
          keepRun  =  true;
        } catch (Exception e) {
            Log.e(TAG, "start() error: ", e);
        }
        return ;
    }


    /*
     * Получение SmartDrawable по ключу
     */
    private BitmapDrawable getSmart(ImgKey key) {
        BitmapDrawable smart=mRamCache.getData(key);
        return smart;
    }


    /**
     * Отмена предыдущих загрузок в этот ImageView
     * @param callBack
     * @return
     */
    public BitmapDrawable loadNullImage(ImageFetcherCallBack callBack) {
        //if (started) {
            mRequestQueue.remove(callBack);
        //}//if (started)
        return null;
    }//loadImage

    /**
     * Асинхронная загрузка картинки в целевой view.
     * Картинка приводится к размеру View.
     *
     * @param callBack - куда загрузить картинку
     * @param resID - взять из R
     * @param width - ширина до которой урезать рисунок
     */
    public BitmapDrawable loadImage(ImageFetcherCallBack callBack, int resID, int width) {
        BitmapDrawable re=null;
        if (keepRun) {
        /* Ключ поиска в кэш будет включать в себя размеры изображения */
            ImgKey key = new ImgKey(resID, width);
            re = mRamCache.getData(key);
            /* Оставим запрос на результаты преобразований
            * оставляю всегда чтобы в рисунок упал гарантированно последний запрос */
            mRequestQueue.put(callBack, key);
            //sendToWorker(MsgTAGs.M_PROCESS_IMG);
            conditionVariable.open();
        }//if (started)
        return re;
    }//loadImage

    /**
     * Межпотоковая загрузка изображений MsgFetcher<-ImageFetcher
     * Результат возвращается СРАЗУ
     * @param resID
     * @param width
     * @return
     */
    public BitmapDrawable loadImage(int resID, int width) {

        //if (started) {
        /* Ключ поиска в кэш будет включать в себя размеры изображения */
            ImgKey key = new ImgKey(resID, width);
        BitmapDrawable re = mRamCache.getData(key);

            if (null==re) {
                /* т.к. этот запрос идёт не из GUI, то незачем выносить в thread */
                Bitmap bitmap = BitmapFactory.decodeResource(SpecTheme.resources, resID);
                if (null!=bitmap) {
                    int bitmapWidht=bitmap.getWidth();
                    if (key.width!=bitmapWidht) {
                        int smartHeight=bitmap.getHeight()*key.width/bitmapWidht;
                        bitmap = Bitmap.createScaledBitmap(bitmap, key.width,
                                smartHeight, true);
                    }

                    re = new BitmapDrawable(SpecTheme.resources, bitmap);
                            /* Запоминаю для других */
                    mRamCache.insertNode(key, re);
                }
            }
        //}//if (started)
        return re;
    }//loadImage


    /**
     * Асинхронная загрузка картинки в целевой view.
     * Картинка приводится к размеру View.
     *
     * @param callBack - куда загрузить картинку
     * @param path - взять c диска
     * @param width - ширина до которой урезать рисунок
     * @param enlarge - 1==надо увеличивать до width если меньше размер, 0== не надо
     */
    public BitmapDrawable loadImage(ImageFetcherCallBack callBack, String path, int width, int enlarge) {

        //if (started) {
        /* Ключ поиска в кэш будет включать в себя размеры изображения */
            //StringBuilder strkey = new StringBuilder(256);
            //strkey.append(path);// как грузить-то.append('.').append(width);
            ImgKey key = new ImgKey(path, width, enlarge);
        BitmapDrawable re = mRamCache.getData(key);
            /* Оставим запрос на результаты преобразований
            * оставляю всегда чтобы в рисунок упал гарантированно последний запрос */
            mRequestQueue.put(callBack, key);
            //sendToWorker(MsgTAGs.M_PROCESS_IMG);
        conditionVariable.open();
        //}//if (started)
        return re;
    }//loadImage

    public BitmapDrawable loadImage(Bitmap bitmap, int width) {
        BitmapDrawable re = null;
        try {
            /* Преобразую */
            if (null != bitmap) {
                int bitmapWidht = bitmap.getWidth();
                if (width != bitmapWidht) {
                    int smartHeight = bitmap.getHeight() * width / bitmapWidht;
                    bitmap = Bitmap.createScaledBitmap(bitmap, width,
                            smartHeight, true);
                }
                re = new BitmapDrawable(SpecTheme.resources, bitmap);
            }
        } catch (Exception e) {
            Log.e(TAG, "loadImage() BitmapFactory error:" ,e);
        }
        return re;
    }


  private int inc_keepRunID() {
    ++keepRunID;
    if  (keepRunID  >  10000) {
      keepRunID -= 10000;
    }
    return keepRunID;
  }

  public void pauseWorkerThread()  {
    inc_keepRunID();
    conditionVariable.open();
    synchronized (TAG) {
      localThread = null;
    }
    mRequestQueue.clear();
  }

  public synchronized void resumeWorkerThread()  {
    pauseWorkerThread();
    synchronized (TAG) {
      localThread = new Thread(new WorkerThread(inc_keepRunID()));
      localThread.start();
    }
  }

    private class WorkerThread implements Runnable {
      public final int threadID;
      WorkerThread(int threadID_)  {
        threadID = threadID_;
      }

      /* Загрузка рисунков согласно mRequestQueue */
      private void processIMGs() {
        //if (started) {
        try {
          Iterator<Map.Entry<ImageFetcherCallBack,ImgKey>> it = mRequestQueue.entrySet().iterator();
          /* Выход из цикла обеспечен очисткой mRequestQueue при остановка ImageFetcher*/
          while (threadID == keepRunID && it.hasNext()) {
            Map.Entry<ImageFetcherCallBack,ImgKey> pair = it.next();
            ImgKey key = pair.getValue();
            //SmartDrawable smart = getSmart(key);
            BitmapDrawable drawable = mRamCache.getData(key);
            if (null==drawable) {
              try {
                /* Гружу */
//                            Bitmap bitmap = (null == key.str) ?
//                                    BitmapFactory.decodeResource(SpecTheme.resources, key.resID)
//                                    : BitmapFactory.decodeFile(key.str);
                Bitmap bitmap = null;
                if (null == key.str) {
                  bitmap = BitmapFactory.decodeResource(SpecTheme.resources, key.resID);
                } else {
                  URL url = new URL(key.str);
                  bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                  if (key.str.startsWith("/")) {
//                    if ((new File(key.str)).exists()) {
//                      bitmap = BitmapFactory.decodeFile(key.str);
//                    }
//                  } else {
//                    bitmap = BitmapFactory.decodeStream(
//                        mContext.getAssets().open(key.str));
//                  }
                }

                if (null != bitmap) {
                  int bitmapWidht = bitmap.getWidth();

                  if (key.width < bitmapWidht
                      || (key.width > bitmapWidht && 1==key.enlarge)) {
                    int smartHeight = bitmap.getHeight() * key.width / bitmapWidht;
                    bitmap = Bitmap.createScaledBitmap(bitmap, key.width,
                        smartHeight, true);
//                                bitmap = Bitmap.createScaledBitmap(bitmap,
//                                        smart.width,
//                                        smart.height, true);
                  }
                  drawable = new BitmapDrawable(SpecTheme.resources, bitmap);
                  /* Запоминаю для других */
                  mRamCache.insertNode(key, drawable);
                }
              } catch (Exception e) {
                Log.e(TAG, "processIMGs() BitmapFactory error:" ,e);
              }
            }
            //if (null!=drawable) {
            /* Если получилось загрузить - вставляю: */
            final ImageFetcherCallBack callBack = pair.getKey();
            final BitmapDrawable bitmapDrawable = drawable;
            SpecTheme.iActivity.runOnGUIthreadDelay(new Runnable() {
              @Override
              public void run() {
                // Code to run on UI thread
                try {
                  callBack.setImageFromFetcher(bitmapDrawable);
                } catch (Exception e) {}
              }
            }, 0);
            //}
            /* Работа выполнена/нет - чищу: */
            it.remove();
          }//while
        }catch (Exception e) {
          Log.e(TAG,"processIMGs() error:",e);
        }
        //}
      }//processIMGs()

        @Override
        public void run() {
          //Log.w(TAG,"WorkerThread START");
            while (threadID == keepRunID) {
                conditionVariable.close();
                processIMGs();
                conditionVariable.block(StaticConsts.MSEC_KEEP_ALIVE);
            } //while
           // Log.w(TAG,"WorkerThread EXIT");
        }
    } //KeepAliveThread
}
