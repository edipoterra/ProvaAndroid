package com.example.imagens;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class ImagensActivity extends ListActivity
{
  protected IMSInterface imInterface;

  private ServiceConnection serviceConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName className, IBinder service) {
      imInterface = IMSInterface.Stub.asInterface((IBinder) service);
      updateImageList();
    }

    public void onServiceDisconnected(ComponentName className) {
      imInterface = null;
    }
  };

  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    this.bindService(new Intent(ImagensActivity.this, IMService.class), 
        serviceConnection, Context.BIND_AUTO_CREATE);
    Button add = (Button) findViewById(R.id.refresh);
    add.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        updateImageList();
      }
    });
  }

  public void updateImageList() {
    try {
      String[] images = imInterface.images();
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
          R.layout.list_item, images);
      setListAdapter(adapter);
    }
    catch(RemoteException e) {
      Log.e(getString(R.string.app_name), e.getMessage());
    }
  }
}
