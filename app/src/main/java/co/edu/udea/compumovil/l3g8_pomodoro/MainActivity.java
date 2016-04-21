package co.edu.udea.compumovil.l3g8_pomodoro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;

import co.edu.udea.compumovil.dataBase.ConfigDbManager;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE=10;
    private static int progressVolume=0;
    private static int progressVibrate=0;
    private static TextView showtime;
    private DefaultValues df;
    private ConfigDbManager configDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        df = new DefaultValues();
        configDbManager = new ConfigDbManager(this);
        if (!configDbManager.configExists()){
            configDbManager.insertConfig(df.getCOUNTER(),
                    df.getSHORT(),
                    df.getLONG(),
                    df.getVOLUME(),
                    df.getVIBRATE(),
                    df.getDEBUG());

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        showtime = (TextView) findViewById(R.id.timerShow);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_exit:
                closeApp();
                break;
            case R.id.action_pomodoro:
                Toast.makeText(MainActivity.this, "You are here", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings:
                Intent i = new Intent(this,SettingsActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                finish();
                //setContentView(R.layout.fragment_settings);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setProgressVolume(int aux){
        progressVolume=aux;
    }
    public static void setProgressVibrate(int aux){
        progressVibrate=aux;
    }

    public static int getProgressVolume() {
        return progressVolume;
    }

    public static int getProgressVibrate() {
        return progressVibrate;
    }

   /* public void saveConfig(View view){
        //volume=progressVolume;
        //vibrate=progressVibrate;
        Toast.makeText(MainActivity.this, "Volume: "+progressVolume+"\nVibrate: "+progressVibrate, Toast.LENGTH_SHORT).show();
        shortRest = 5;
        longRest = 15;
    }*/

    public void startPomodoro(View view){
        Intent intent = new Intent(getBaseContext(), PomodoroService.class);
        intent.setAction("StartPomodoro");
        startService(intent);
    }

    public void stopPomodoro(View view){
        Intent intent = new Intent(getBaseContext(), PomodoroService.class);
        TextView textViewClock = (TextView) findViewById(R.id.timerShow);
        textViewClock.setText("00:00");
        intent.setAction("StopPomodoro");
        stopService(intent);
    }


    private void closeApp(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        System.exit(0);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.closeSure)).setPositiveButton(getResources().getString(R.string.yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.no), dialogClickListener).show();

    }
    public static void timerData(String time){
        showtime.setText(time);
    }
}
