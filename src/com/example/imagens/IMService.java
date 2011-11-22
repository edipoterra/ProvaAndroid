package com.example.imagens;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import java.io.*;
import java.util.*;

public class IMService extends Service {
  protected final String mediaPath = "/sdcard";
  protected ArrayList<String> images = new ArrayList<String>();
  protected final IMSInterface.Stub binder = new IMSInterface.Stub() {
    public String[] images() {
      ArrayList<String> images = getImages();
      return images.toArray(new String[images.size()]);
    }
  };

  public void onCreate() {
    super.onCreate();
    Thread thread = new Thread(new Runnable() {
      public void run() {
        try {
          while(true) {
            File home = new File(mediaPath);
            File fileList[] = home.listFiles(new FilenameFilter() {
              public boolean accept(File dir, String name) {
                return name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png");
              }
            });

            if (fileList != null) {
              images.clear();
              for(File file : fileList) {
                images.add(file.getName());
              }
            }

            Thread.sleep(5000);
          }
        } 
        catch(InterruptedException e) {
          e.printStackTrace();
        }
      }
    });
    thread.start();
  }

  public void onDestroy() {
  }

  public IBinder getBinder() {
    return binder;
  }

  public IBinder onBind(Intent intent) {
    return binder;
  }

  public ArrayList getImages() {
    return images;
  }
}
