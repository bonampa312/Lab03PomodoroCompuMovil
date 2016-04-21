package co.edu.udea.compumovil.l3g8_pomodoro;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import co.edu.udea.compumovil.dataBase.ConfigDbManager;

public class PomodoroService extends Service {

    private Context ctx;
    private long mUntil;
    private String time="";
    private TimerCountdown timer=null;
    private DefaultValues df;
    private ConfigDbManager configDbManager;
    public  int counter = 0;
    public  int shortRest = 0;
    public  int longRest = 0;
    public  int volume = 0;
    private int timerDefine=0;
    public  int vibrate = 0;
    public int debug=0;
    int minSec=0;

    private Notification notif;
    private NotificationManager notificationManager;
    public ArrayList<String> databaseValues=null;

    public PomodoroService() {
    }

    public void onCreate(){
        super.onCreate();
        ctx = this;
        df = new DefaultValues();

        configDbManager = new ConfigDbManager(this);
        databaseValues=configDbManager.getAllConfig();
        asignValues();
        if(counter!=0 && (counter%8)==0){
            timerDefine=longRest;
        }
        else if((counter%2)==0){
            timerDefine=25;
        }
        else{
            timerDefine=shortRest;
        }
        if (debug==0){
            minSec=60000;
        }
        else{
            minSec=1000;
        }
        timer = new TimerCountdown(timerDefine*minSec,1000);
        int auxNotifiId = 1;
        notif = presentNotification(auxNotifiId,
                Notification.VISIBILITY_PUBLIC, R.drawable.saitama, "Pomodoro running", "Pomodoro Started", this, volume);
        notificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(auxNotifiId, notif);
    }

    public void asignValues(){

        counter=Integer.parseInt(databaseValues.get(0));
        shortRest=Integer.parseInt(databaseValues.get(1));
        longRest=Integer.parseInt(databaseValues.get(2));
        volume=Integer.parseInt(databaseValues.get(3));
        vibrate=Integer.parseInt(databaseValues.get(4));
        debug=Integer.parseInt(databaseValues.get(5));

    }

    public void start(long durationMillis) { startService(durationMillis, 0); }
    private void startService(long durationMillis, long startTime){
        startTime = (startTime == 0) ? System.currentTimeMillis() : startTime;
        mUntil = startTime + durationMillis;
        timer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(PomodoroService.this, "onBind", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        Toast.makeText(PomodoroService.this, "Pomodoro stopped D:", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("StartPomodoro")){
            startForeground(1,notif);
            start(timerDefine * minSec);
        }
        else{
            stopForeground(true);
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public class TimerCountdown extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public TimerCountdown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long left = mUntil - System.currentTimeMillis();
            long min = Math.abs((long)(left / 60000));
            long sec = Math.abs((long)((left - min) / 1000) % 60);
            time= ((left < 0 ? "-" : "") + min + ":" + sec);
            MainActivity.timerData(time);
        }


        @Override
        public void onFinish() {

            counter=counter+1;  //actualiza el contador para saber que tiempo se ejecuta despues (no se si es +1)
            notificationManager =
                    (NotificationManager) getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if(counter!=0 && (counter%8)==0){
                timerDefine=longRest;
                int auxNotifiId12 = 12;
                notif = presentNotification(auxNotifiId12,
                        Notification.VISIBILITY_PUBLIC, R.drawable.saitama_green, "Long rest :D",
                        "Pomodoro notification", getApplicationContext(), volume);
                notificationManager.notify(auxNotifiId12, notif);
                v.vibrate(vibrate * 100);
            }
            else if((counter%2)==0){
                timerDefine=25;
                int auxNotifiId2 = 2;
                notif = presentNotification(auxNotifiId2,
                        Notification.VISIBILITY_PUBLIC, R.drawable.saitama_black, "Go to work :D",
                        "Pomodoro notification", getApplicationContext(), volume);
                notificationManager.notify(auxNotifiId2, notif);
                v.vibrate(vibrate*100);
            }
            else{
                timerDefine=shortRest;
                int auxNotifiId3 = 3;
                notif = presentNotification(auxNotifiId3,
                        Notification.VISIBILITY_PUBLIC, R.drawable.saitama_red, "Short rest :D",
                        "Pomodoro notification", getApplicationContext(), volume);
                notificationManager.notify(auxNotifiId3, notif);
                v.vibrate(vibrate*100);
            }
            if (debug==0){
                minSec=60000;
            }
            else{
                minSec=1000;
            }
            timer = new TimerCountdown(timerDefine*minSec,1000);
            startService(timerDefine * minSec,0);
        }
    }

    public Notification presentNotification(int notification_id, int visibility, int icon,
                                            String title, String text, Context context, int auxVolume) {

        AudioManager manager = (AudioManager)getSystemService(getApplicationContext().AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, auxVolume, AudioManager.FLAG_PLAY_SOUND);

        android.app.Notification notification = new NotificationCompat.Builder(context)
                .setCategory(android.app.Notification.CATEGORY_MESSAGE)
                .setContentTitle(title)
                .setContentText(text)
                .setCategory(Notification.INTENT_CATEGORY_NOTIFICATION_PREFERENCES)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVisibility(visibility).build();
        return notification;
    }

}