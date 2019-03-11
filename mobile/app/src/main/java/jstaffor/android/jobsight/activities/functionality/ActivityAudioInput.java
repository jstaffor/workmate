package jstaffor.android.jobsight.activities.functionality;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityCreateAndAdd;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.functionality.DatabaseAudioRecording;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.utilities.AccessInternalStorage;
import jstaffor.android.jobsight.utilities.AudioVisualizerView;
import jstaffor.android.jobsight.utilities.CaptureAudioRecording;

public class ActivityAudioInput extends Activity implements View.OnClickListener
{
    private static final String TAG = "ActivityAudioInput";
    private Button btn_recording_start, btn_recording_stop_and_save;
    private CaptureAudioRecording captureAudioRecording;
    private DataModel dataModel;
    private Chronometer chronometer;

    private AudioVisualizerView audioVisualizerView;
    private MediaRecorder mediaRecorder;
    private AccessInternalStorage accessInternalStorage;

    //This parameter is vital. It is used in a comms capacity between
    //the thread driving ActivityAudioInput and
    //the thread driving AudioVisualizerThread
    //It's positioning in the code is VITAL so be very careful if you alter it!
    private final AtomicBoolean isAudioBeingCaptured = new AtomicBoolean(false);
    private static final String randomNameForThread = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiorecordinginput);

        // Initialize view
        btn_recording_start = findViewById(R.id.activity_audiorecordinginput_btn_recording_start);
        btn_recording_stop_and_save = findViewById(R.id.activity_audiorecordinginput_btn_recording_stop_and_save);
        audioVisualizerView = findViewById(R.id.activity_audiorecordinginput_visualizer);
        chronometer = findViewById(R.id.activity_audiorecordinginput_chronometer);
        btn_recording_start.setOnClickListener(this);
        btn_recording_stop_and_save.setOnClickListener(this);

        //Get Data
        dataModel = DataModelUtilities.turnJSONIntoDataModel(getIntent().getExtras().get(DataModelUtilities.DATA_MODEL).toString());

        if (AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "onCreate(Bundle savedInstanceState) | DataModelUtilities.turnJSONIntoDataModel() | " + DataModelUtilities.turnDataModelIntoJSON(dataModel));

        accessInternalStorage = new AccessInternalStorage(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        shutDownAudioRecordingAndVisulizer();

        if (AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "void onPause() | shutDownAudioRecordingAndVisulizer(); | " + "true");
    }

    @Override
    public void onClick(View view) {
        try
        {
        switch (view.getId()) {
            case R.id.activity_audiorecordinginput_btn_recording_start:

                try {
                    if(captureAudioRecording == null)
                        captureAudioRecording = new CaptureAudioRecording(this, accessInternalStorage.createChildDirForParentChild(dataModel.getlParent(), dataModel.getlChild()));

                    if (captureAudioRecording.isRecording() != true) {

                        //(1) Retrieve 'mediaRecorder' as we need getMaxAmplitude data
                        mediaRecorder = captureAudioRecording.startRecording();

                        boolean t2 = captureAudioRecording.isRecording();

                        //(2)Positioning of this is critical - the thread driving AudioVisualizerThread relies on this to post updates
                        isAudioBeingCaptured.compareAndSet(false, true);

                        //(2) Start Thread to update AudioVisualizer
                        new AudioVisualizerThread().execute("");

                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "onClick(View view) | activity_audiorecordinginput_btn_recording_start | " + "new AudioVisualizerThread().execute()");

                        //(3) Reset and Start Chronometer
                        chronometer.setBase(SystemClock.elapsedRealtime());
                        chronometer.start();

                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "onClick(View view) | activity_audiorecordinginput_btn_recording_start | " + "chronometer.start()");
                    }
                } catch (Exception exception) {
                    Log.e(TAG, "onClick(View view) | case R.id.activity_audiorecordinginput_btn_recording_start | " + exception.getMessage());
                    Toast toast = Toast.makeText(this, getString(R.string.audiorecording_saved_fail), Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            case R.id.activity_audiorecordinginput_btn_recording_stop_and_save:
                if (captureAudioRecording != null && captureAudioRecording.isRecording()) {
                    //Positioning of this is critical - the thread driving AudioVisualizerThread blows up without it!
                    isAudioBeingCaptured.compareAndSet(true, false);
                    for (Thread t1 : Thread.getAllStackTraces().keySet()) {
                        if (t1.getName().equals(randomNameForThread) && !t1.isInterrupted())
                            t1.interrupt(); //Kill the thread driving class AudioVisualizerThread extends AsyncTask<String, Void, String>
                    }

                    try {
                        final String fileAbsolutePath = captureAudioRecording.stopRecording();

                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "onClick(View view) | captureAudioRecording.stopRecording() | " + fileAbsolutePath);

                        //(1) Shut down everything
                        shutDownAudioRecordingAndVisulizer();

                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "onClick(View view) | case R.id.activity_audiorecordinginput_btn_recording_stop_and_save | " + "shutDownAudioRecordingAndVisulizer()");

                        //(2) Get image that represents the audio
                        final String imageAbsolutePath = accessInternalStorage.saveBitmapToInternalStorage(getBitmap(), dataModel.getlParent(), dataModel.getlChild());

                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "onClick(View view) | accessInternalStorage.saveBitmapToInternalStorage() | " + imageAbsolutePath);

                        if (fileAbsolutePath != null) {
                            //(2) Save to local database - nullpointer indicates issue saving
                            if (new DatabaseAudioRecording(this).createAudioRecordingEntry(dataModel.getlChild(), fileAbsolutePath, imageAbsolutePath)) {
                                Toast toast = Toast.makeText(this, getString(R.string.audiorecording_saved_success), Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                Log.e(TAG, "onClick(View view) | createAudioRecordingEntry() | " + "false");
                                Toast toast = Toast.makeText(this, getString(R.string.audiorecording_saved_fail), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            Log.e(TAG, "onClick(View view) | accessInternalStorage.saveBitmapToInternalStorage() | " + "null");
                            Toast toast = Toast.makeText(this, getString(R.string.audiorecording_saved_fail), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } catch (Exception exception) {
                        Log.e(TAG, "onClick(View view) | case R.id.activity_audiorecordinginput_btn_recording_stop_and_save | " + exception.getMessage());
                        Toast toast = Toast.makeText(this, getString(R.string.audiorecording_saved_fail), Toast.LENGTH_LONG);
                        toast.show();
                    } finally {
                        Intent intent = new Intent(this, ActivityCreateAndAdd.class);
                        intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                        startActivity(intent);
                    }
                }
                break;
            default:
                Log.e(TAG, "onClick(View view) - unknown selection");
                throw new IllegalArgumentException();
        }

    }
                catch (Exception exception)
    {
        Toast toast = Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG);
        toast.show();
    }
}

    public Bitmap getBitmap() {
        Bitmap curBitmap = Bitmap.createBitmap(audioVisualizerView.getWidth(), audioVisualizerView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas curCanvas = new Canvas(curBitmap);
        audioVisualizerView.layout(audioVisualizerView.getLeft(), audioVisualizerView.getTop(), audioVisualizerView.getRight(), audioVisualizerView.getBottom());
        audioVisualizerView.draw(curCanvas);

        if (AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "getBitmap() | curBitmap.getAllocationByteCount() | " + curBitmap.getAllocationByteCount());

        return curBitmap;
    }

    private class AudioVisualizerThread extends AsyncTask<String, Void, String> {

        private static final String TAG = "AudioVisualizerThread";
        private int maxAmplitude = 1;

        @Override
        protected String doInBackground(String... params) {
            try {
                while (isAudioBeingCaptured.get()) {
                    Thread.currentThread().setName(randomNameForThread);
                    Thread.sleep(100);
                    maxAmplitude = mediaRecorder.getMaxAmplitude();

                    if (AppSettings.APP_DEBUG_MODE)
                        Log.d(TAG, "protected String doInBackground(String... params) while(isAudioBeingCaptured.get()) | mediaRecorder.getMaxAmplitude() | " + maxAmplitude);

                    onProgressUpdate();
                }
            } catch (InterruptedException interruptedException) {
                Thread.interrupted();
                Log.d(TAG, "protected String doInBackground(String... params) | catch (InterruptedException interruptedException) | " + interruptedException.getMessage());
            }

            return "success";
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (maxAmplitude != 0) {
                        audioVisualizerView.addAmplitude(maxAmplitude);

                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "protected void onProgressUpdate(Void... values) if (maxAmplitude != 0) | audioVisualizerView.addAmplitude(maxAmplitude) | " + maxAmplitude);
                    }
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (AppSettings.APP_DEBUG_MODE)
                Log.d(TAG, "onKeyDown(int keyCode, KeyEvent event) | (keyCode == KeyEvent.KEYCODE_BACK) | " + "shutDownAudioRecordingAndVisulizer()");

            shutDownAudioRecordingAndVisulizer();

            Intent a = new Intent(this, ActivityCreateAndAdd.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void shutDownAudioRecordingAndVisulizer()
    {
        isAudioBeingCaptured.compareAndSet(true, false);
        for (Thread t1 : Thread.getAllStackTraces().keySet()) {
            if (t1.getName().equals(randomNameForThread) && !t1.isInterrupted())
                t1.interrupt(); //Kill the thread driving class AudioVisualizerThread extends AsyncTask<String, Void, String>
        }

        if (captureAudioRecording != null) {
            try
            {
                if (captureAudioRecording.isRecording())
                {
                    if (AppSettings.APP_DEBUG_MODE)
                        Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | captureAudioRecording.isRecording() | " + "true");

                    final String audioFileGetAbsolutePath = captureAudioRecording.stopRecording();

                    if (AppSettings.APP_DEBUG_MODE)
                        Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | captureAudioRecording.stopRecording() | " + audioFileGetAbsolutePath);
                }

            } catch (Exception e) {
                Log.e(TAG, "shutDownAudioRecordingAndVisulizer() | if (captureAudioRecording != null) - catch (Exception e) | " + e.getMessage());

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | if (captureAudioRecording != null) - catch (Exception e) | " + e.getMessage());
            }
        }

        if (mediaRecorder != null)
        {
            try
            {
                mediaRecorder.release();

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | mediaRecorder.release() | " + "mediaRecorder.release()");

            } catch (Exception e) {
                Log.e(TAG, "shutDownAudioRecordingAndVisulizer() | if (mediaRecorder != null) - catch (Exception e) | " + e.getMessage());

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | if (mediaRecorder != null) - catch (Exception e) | " + e.getMessage());
            }
        }

        if (chronometer != null) {
            try
            {
                chronometer.stop();

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | if (chronometer != null) | " + "chronometer.stop()");

            } catch (Exception e) {
                Log.e(TAG, "shutDownAudioRecordingAndVisulizer() | if (chronometer != null) chronometer.stop() " + e.getMessage());

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "shutDownAudioRecordingAndVisulizer() | if (chronometer != null) chronometer.stop() " + e.getMessage());
            }
        }
    }
}