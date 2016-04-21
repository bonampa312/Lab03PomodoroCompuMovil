package co.edu.udea.compumovil.l3g8_pomodoro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import co.edu.udea.compumovil.dataBase.ConfigDbManager;

public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBarVolume;
    private SeekBar seekBarVibrate;
    private SeekBar seekBarShortRest;
    private SeekBar seekBarLongRest;
    private TextView shortText;
    private TextView longText;
    private TextView showVibrate;
    private TextView showSound;
    private ConfigDbManager configDbManager;
    private int progressVolume;
    private int progressVibrate;
    private int progressShortRest;
    private int progressLongRest;
    CheckBox debugCheckbox;

    private static final int REQUEST_CODE=11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        debugCheckbox = (CheckBox) findViewById(R.id.enableDebug);


        configDbManager = new ConfigDbManager(this);
        ArrayList<String> auxStrings = configDbManager.getAllConfig();
        int auxDefineVolume = Integer.parseInt(auxStrings.get(3));
        int auxDefineVibrate = Integer.parseInt(auxStrings.get(4));
        int auxDefineShort = Integer.parseInt(auxStrings.get(1));
        int auxDefineLong = Integer.parseInt(auxStrings.get(2));

        if (auxStrings.get(5).equals("0"))
            debugCheckbox.setChecked(false);
        else
            debugCheckbox.setChecked(true);

        setSupportActionBar(toolbar);
        seekBarVolume = (SeekBar) findViewById(R.id.soundkBar);
        showSound = (TextView) findViewById(R.id.soundShow);

        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        seekBarVolume.setMax(am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION));
        seekBarVolume.setProgress(auxDefineVolume);
        showSound.setText("" + auxDefineVolume);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //Cuando se cambia el Progress del SeekBar
                progressVolume = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Cuando se comienza a modificar el Progress del SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Cuando se termina de modificar el SeekBar
                showSound.setText("" + progressVolume);
            }
        });

        seekBarVibrate = (SeekBar) findViewById(R.id.vibratekBar);
        showVibrate = (TextView) findViewById(R.id.vibrateShow);
        seekBarVibrate.setProgress(auxDefineVibrate);
        showVibrate.setText("" + auxDefineVibrate);
        seekBarVibrate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //Cuando se cambia el Progress del SeekBar
                progressVibrate = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Cuando se comienza a modificar el Progress del SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Cuando se termina de modificar el SeekBar
                showVibrate.setText("" + progressVibrate);
            }
        });
        seekBarShortRest = (SeekBar) findViewById(R.id.shortkBar);
        shortText = (TextView) findViewById(R.id.shortRestShow);
        seekBarShortRest.setProgress(auxDefineShort);
        shortText.setText("");
        shortText.setText("" + (seekBarShortRest.getProgress()+3));
        seekBarShortRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //Cuando se cambia el Progress del SeekBar
                progressShortRest = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Cuando se comienza a modificar el Progress del SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Cuando se termina de modificar el SeekBar
                shortText.setText("");
                shortText.setText("" + (progressShortRest+3));
            }
        });
        seekBarLongRest = (SeekBar) findViewById(R.id.longkBar);
        longText = (TextView) findViewById(R.id.longRestShow);
        seekBarLongRest.setProgress(auxDefineLong);
        longText.setText("");
        longText.setText(""+((seekBarLongRest.getProgress()*5)+10));
        seekBarLongRest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                //Cuando se cambia el Progress del SeekBar
                progressLongRest = progresValue;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Cuando se comienza a modificar el Progress del SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Cuando se termina de modificar el SeekBar
                longText.setText("");
                longText.setText("" + ((progressLongRest*5)+10));
            }
        });
        progressLongRest = seekBarLongRest.getProgress();
        progressShortRest = seekBarShortRest.getProgress();
        progressVibrate = seekBarVibrate.getProgress();
        progressVolume = seekBarVolume.getProgress();
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
                Intent i = new Intent(this,MainActivity.class);
                startActivityForResult(i, REQUEST_CODE);
                finish();
                break;
            case R.id.action_settings:
                Toast.makeText(SettingsActivity.this, "You are here", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    public void saveConfig(View view){
        String shortTime = String.valueOf(progressShortRest+3);
        String longTime = String.valueOf((progressLongRest*5)+10);
        String volume = String.valueOf(progressVolume);
        String vibration = String.valueOf(progressVibrate);
        String debugString="";
        if (debugCheckbox.isChecked()){
            debugString="1";
        }else{
            debugString="0";
        }
        configDbManager.insertConfig("0", shortTime , longTime, volume, vibration, debugString);
        Toast.makeText(SettingsActivity.this, "Changes saved", Toast.LENGTH_SHORT).show();
    }

    public void checkStatus(View view){
        Toast.makeText(SettingsActivity.this, "Volume: "+progressVolume+"\nVibrate: "+progressVibrate+"\nShortRest: "+progressShortRest+"\nLongRest: "+progressLongRest, Toast.LENGTH_SHORT).show();
    }
}

