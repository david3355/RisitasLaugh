package laugh.jager.risitaslaugh;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerService extends Service implements MediaStoppedHandler
{
       public PlayerService()
       {
       }

       private static List<WidgetInstance> widgetInstances = new ArrayList<>();

       private BroadcastReceiver screenOffReceiver = new BroadcastReceiver()
       {
              @Override
              public void onReceive(Context context, Intent intent)
              {
                     stopAllMedia();
                     stopSelf();
              }
       };


       public static final String START_PLAYER = "startplayer";

       @Override
       public int onStartCommand(Intent intent, int flags, int startId)
       {
              registerScreenOffReceiver();
              operate(intent);
              return super.onStartCommand(intent, flags, startId);
       }

       private void operate(Intent intent)
       {
              int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
              WidgetInstance widget = getWidgetByID(widgetId);
              if (widget == null)
              {
                     widget = new WidgetInstance(widgetId, getApplicationContext(), intent, this);
                     widgetInstances.add(widget);
              }
              if (!widget.isPlaying()) widget.playMedia();
              else widget.stopMedia();
       }

       private WidgetInstance getWidgetByID(int widgetID)
       {
              for (WidgetInstance winstance : widgetInstances)
              {
                     if (winstance.getWidgetID() == widgetID) return winstance;
              }
              return null;
       }

       private void stopAllMedia()
       {
              for (WidgetInstance winstance : widgetInstances)
              {
                     if (winstance.isPlaying()) winstance.stopMedia();
              }
       }

       private boolean isPlaying()
       {
              for (WidgetInstance winstance : widgetInstances)
              {
                     if (winstance.isPlaying()) return true;
              }
              return false;
       }

       public static void deleteUnusedWidgetInstances(int[] widgetIDs)
       {
              for (int wid : widgetIDs)
              {
                     removeWidgetInstanceByID(wid);
              }
       }

       private static void removeWidgetInstanceByID(int widgetID)
       {
              Iterator<WidgetInstance> iter = widgetInstances.iterator();
              while (iter.hasNext())
              {
                     WidgetInstance next = iter.next();
                     if (next.getWidgetID() == widgetID)
                     {
                            if (next.isPlaying()) next.stopMedia();
                            iter.remove();
                     }
              }
       }

       private void registerScreenOffReceiver()
       {
              IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
              registerReceiver(screenOffReceiver, filter);
       }

       @Override
       public IBinder onBind(Intent intent)
       {
              return null;
       }

       @Override
       public void onDestroy()
       {
              unregisterReceiver(screenOffReceiver);
              super.onDestroy();
       }

       @Override
       public void mediaStopped(int widgetID)
       {
              if (!isPlaying()) stopSelf();
       }

}
