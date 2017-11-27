package com.chavesjm.crocketttrainingmonitor;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = BluetoothServer.class.getSimpleName();

    private Button openButton;
    private Button sendButton;
    private Button closeButton;
    private TextView myLabel;
    private EditText myTextbox;

    private TextView yaw;
    private TextView pitch;
    private TextView roll;

    private TextView yawInitial;
    private TextView pitchInitial;
    private TextView rollInitial;

    private TextView yawTurn;
    private TextView pitchTurn;
    private TextView rollTurn;

    private TextView betaV;
    private TextView frequencie;
    private TextView difficultyV;

    private SeekBar seekBarBeta;
    private SeekBar seekBarDifficulty;

    private ToneGenerator toneG;

    private RadioButton rbYaw;
    private RadioButton rbPitch;
    private RadioButton rbRoll;


    private BluetoothServer mBluetoothServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openButton = (Button) findViewById(R.id.open);
        sendButton = (Button) findViewById(R.id.send);
        closeButton = (Button) findViewById(R.id.close);
        myLabel = (TextView) findViewById(R.id.label);
        myTextbox = (EditText) findViewById(R.id.entry);

        yaw = (TextView) findViewById(R.id.yaw);
        pitch = (TextView) findViewById(R.id.pitch);
        roll = (TextView) findViewById(R.id.roll);

        yawInitial = (TextView) findViewById(R.id.yaw_initial);
        pitchInitial = (TextView) findViewById(R.id.pitch_initial);
        rollInitial = (TextView) findViewById(R.id.roll_initial);

        yawTurn = (TextView) findViewById(R.id.yaw_turn);
        pitchTurn = (TextView) findViewById(R.id.pitch_turn);
        rollTurn = (TextView) findViewById(R.id.roll_turn);

        betaV = (TextView) findViewById(R.id.beta);
        frequencie = (TextView) findViewById(R.id.frequencie);
        difficultyV = (TextView) findViewById(R.id.difficulty);
        difficultyV.setText("3");

        seekBarBeta = (SeekBar) findViewById(R.id.seekBarBeta);
        seekBarBeta.setMax(1023);

        seekBarDifficulty = (SeekBar) findViewById(R.id.seekBarDifficulty);
        seekBarDifficulty.setMax(10);

        rbPitch = (RadioButton) findViewById(R.id.rb_pitch);
        rbYaw = (RadioButton) findViewById(R.id.rb_yaw);
        rbRoll = (RadioButton) findViewById(R.id.rb_roll);

        mBluetoothServer = new BluetoothServer();
        mBluetoothServer.setListener(mBluetoothServerListener);

        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        //Open Button
        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    mBluetoothServer.start();
                } catch (BluetoothServer.BluetoothServerException e) {
                    e.printStackTrace();
                    writeError(e.getMessage());
                }
            }
        });

        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    mBluetoothServer.send(myTextbox.getText().toString().getBytes());
                    myTextbox.setText("");
                } catch (BluetoothServer.BluetoothServerException e) {
                    e.printStackTrace();
                    writeError(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    writeError(e.getMessage());
                }


            }
        });

        //Close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBluetoothServer.stop();
            }
        });

        seekBarBeta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                try {
                    mBluetoothServer.send((String.valueOf(progress) + "\0").getBytes());
                } catch (BluetoothServer.BluetoothServerException e) {
                    e.printStackTrace();
                    writeError(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    writeError(e.getMessage());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarDifficulty.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                difficultyV.setText(String.valueOf(progress+1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    };

    private void writeMessage(String message) {

        Log.i(TAG, message);

//       //Values
        String values[];
        values = message.split(",");

        if(values.length == 12)
        {
            double frequency =  Double.parseDouble(values[0]);
            double beta = Double.parseDouble(values[1]);

            double yaw = Double.parseDouble(values[2]);
            double pitch = Double.parseDouble(values[3]);
            double roll = Double.parseDouble(values[4]);

            double yaw_initial = Double.parseDouble(values[5]);
            double pitch_initial = Double.parseDouble(values[6]);
            double roll_initial = Double.parseDouble(values[7]);

            double yaw_turn = Double.parseDouble(values[8]);
            double pitch_turn = Double.parseDouble(values[9]);
            double roll_turn = Double.parseDouble(values[10]);

            double difficulty = Double.parseDouble(values[11]);

            this.yaw.setText(String.valueOf((int)yaw));
            this.pitch.setText(String.valueOf((int)pitch));
            this.roll.setText(String.valueOf((int)roll));

            yawInitial.setText(String.valueOf((int)yaw_initial));
            pitchInitial.setText(String.valueOf((int)pitch_initial));
            rollInitial.setText(String.valueOf((int)roll_initial));

            yawTurn.setText(String.valueOf((int)yaw_turn));
            pitchTurn.setText(String.valueOf((int)pitch_turn));
            rollTurn.setText(String.valueOf((int)roll_turn));

            frequencie.setText(String.valueOf((int)frequency));
            betaV.setText(String.valueOf(beta));
            //difficultyV.setText(String.valueOf(difficulty));

            double diff = Double.parseDouble(difficultyV.getText().toString());

            if(rbRoll.isChecked())
            {
                if(roll_turn > diff)
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                else if(roll_turn < diff * -1.0)
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE);
                else
                    toneG.stopTone();
            }
            else if(rbYaw.isChecked())
            {
                if(yaw_turn > diff)
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                else if(yaw_turn < diff * -1.0)
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE);
                else
                    toneG.stopTone();
            }
            else if(rbPitch.isChecked())
            {
                if(pitch_turn > diff)
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
                else if(pitch_turn < diff * -1.0)
                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE);
                else
                    toneG.stopTone();
            }
        }
    }

    private void writeError(String message) {
        writeMessage("ERROR: " + message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBluetoothServer.stop();
        mBluetoothServer = null;
    }

    /**
     * Bluetooth server events listener.
     */
    private BluetoothServer.IBluetoothServerListener mBluetoothServerListener =
            new BluetoothServer.IBluetoothServerListener() {
                @Override
                public void onStarted() {
                    writeMessage("*** Server has started, waiting for client connection ***");
                    closeButton.setEnabled(true);
                    openButton.setEnabled(false);
                }

                @Override
                public void onConnected() {
                    writeMessage("*** Client has connected ***");
                    sendButton.setEnabled(true);
                }

                @Override
                public void onData(String data) {
                    writeMessage(new String(data));
                }

                @Override
                public void onError(String message) {
                    writeError(message);
                }

                @Override
                public void onStopped() {
                    writeMessage("*** Server has stopped ***");
                    sendButton.setEnabled(false);
                    closeButton.setEnabled(false);
                    openButton.setEnabled(true);
                }
            };
}