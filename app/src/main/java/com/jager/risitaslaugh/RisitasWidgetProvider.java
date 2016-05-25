package com.jager.risitaslaugh;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Jager on 2016.02.07..
 */
public class RisitasWidgetProvider extends AppWidgetProvider
{

       /*
       Lefut minden widgetpéldány hozzáadásánál
       Ezen kívül két esetben fut le:
       - Ha a config xml-ben az updatePeriodMillis 0-tól különböző értékű
       - És ha erre vonatkozó Broadcast üzenetet definiálunk
       (akkor nem fut le, amikor manuálisan update-eljük a widgetet!!!)
        */
       @Override
       public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
       {
              int n = appWidgetIds.length;
              for (int i = 0; i < n; i++)
              {
                     int widgetID = appWidgetIds[i];
                     config(context, appWidgetManager, widgetID);
              }
       }

       private void config(Context context, AppWidgetManager appWidgetManager, int appWidgetID)
       {
              RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
              Intent svc = new Intent(context, PlayerService.class);
              svc.setAction(PlayerService.START_PLAYER);
              svc.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID);
              int requestcode = appWidgetID;  // a request code-nak egyedinek kell lennie, különben ugyanaz a PendingIntent objektum jön létre, hiába különböznek az extra adatok!
              PendingIntent pendingIntent = PendingIntent.getService(context, requestcode, svc, 0);
              remoteViews.setOnClickPendingIntent(R.id.risitas_img, pendingIntent);
              appWidgetManager.updateAppWidget(appWidgetID, remoteViews);
       }


       // Meghívódik minden alkalommal, amikor egy saját widgetet törülünk a hostból
       @Override
       public void onDeleted(Context context, int[] appWidgetIds)
       {
              super.onDeleted(context, appWidgetIds);
              PlayerService.deleteUnusedWidgetInstances(appWidgetIds);
       }

       // Akkor hívódik, mikor az első widget példányt kitesszük, azaz a legelső widget létrehozásakor
       @Override
       public void onEnabled(Context context)
       {
              super.onEnabled(context);
       }

       // Akkor hívódik, mikor a widgetünk összes példányát kitöröltük
       @Override
       public void onDisabled(Context context)
       {
              super.onDisabled(context);
       }
}
