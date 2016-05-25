package com.jager.risitaslaugh;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Jager on 2016.02.20..
 */
public class WidgetInstance implements MediaPlayer.OnCompletionListener
{
       public WidgetInstance(int widgetID, Context context, Intent intent, MediaStoppedHandler mediaStoppedHandler)
       {
              this.widgetID = widgetID;
              this.context = context;
              this.intent = intent;
              this.mediaStoppedHandler = mediaStoppedHandler;
       }

       private Intent intent;
       private Context context;
       private MediaPlayer player;
       private int widgetID;
       private boolean playing;
       private static Random rnd = new Random();
       private MediaStoppedHandler mediaStoppedHandler;

       private static int[] soundsres = {R.raw.risitas1, R.raw.risitas2, R.raw.risitas3, R.raw.risitas4, R.raw.risitas5, R.raw.risitas6, R.raw.risitas7, R.raw.risitas8, R.raw.risitas9, R.raw.risitas10};

       private int getRandomSoundID()
       {
              return soundsres[rnd.nextInt(soundsres.length)];
       }

       private void updateWidget(Intent intent, int imageID)
       {
              int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0);
              AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
              RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
              remoteViews.setImageViewResource(R.id.risitas_img, imageID);
              appWidgetManager.updateAppWidget(widgetId, remoteViews);
       }

       private void mediaStopped()
       {
              playing = false;
              updateWidget(intent, R.drawable.risitas_serious);
              player.release();
              player = null;
              mediaStoppedHandler.mediaStopped(widgetID);
       }

       public int getWidgetID()
       {
              return widgetID;
       }

       @Override
       public void onCompletion(MediaPlayer mp)
       {
              mediaStopped();
       }

       public void playMedia()
       {
              updateWidget(intent, R.drawable.risitas_laugh);
              int soundID = soundsres[rnd.nextInt(soundsres.length)];
              player = player.create(context, soundID);
              player.setOnCompletionListener(this);
              player.start();
              playing = true;
       }

       public void stopMedia()
       {
              if (player != null && player.isPlaying())
              {
                     player.stop();
                     mediaStopped();
              }
       }

       public boolean isPlaying()
       {
              return (player!= null && player.isPlaying());
       }


}
