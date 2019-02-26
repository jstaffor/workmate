package jstaffor.android.jobsight.utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import jstaffor.android.jobsight.appsettings.AppSettings;

public class CaptureAudioRecording
{
    private static final String TAG = "CaptureAudioRecording";
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private boolean isRecording = false;
    private Context context;
    private String absoluteLocationForFile;

    public CaptureAudioRecording(Context context, String absoluteLocationForFile)
    {
        super();

        if(context == null || absoluteLocationForFile == null)
            throw new IllegalArgumentException("CaptureAudioRecording.CaptureAudioRecording(Context context, String absoluteLocationForFile) - inputs cannot be null");

        this.context = context;
        this.absoluteLocationForFile = absoluteLocationForFile;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public MediaRecorder startRecording() throws IllegalStateException, IOException
    {
        // https://stackoverflow.com/questions/11005859/record-audio-via-mediarecorder
        mediaRecorder = new MediaRecorder();
        ContextWrapper cw = new ContextWrapper(context);
        audioFile = new File(absoluteLocationForFile, (UUID.randomUUID().toString()+".mp4"));
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setAudioEncodingBitRate(16);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

        mediaRecorder.prepare();
        mediaRecorder.start();

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "startRecording() throws IllegalStateException, IOException | mediaRecorder.start() | " + "mediaRecorder.start()");

        isRecording = true;

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "startRecording() throws IllegalStateException, IOException | mediaRecorder | " + mediaRecorder);

        return mediaRecorder;
    }

    public String stopRecording() throws IllegalStateException
    {
        mediaRecorder.stop();

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "startRecording() throws IllegalStateException, IOException | mediaRecorder.stop() | " + "mediaRecorder.stop()");

        mediaRecorder.release();

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "startRecording() throws IllegalStateException, IOException | mediaRecorder.release() | " + "mediaRecorder.release()");

        isRecording = false;

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "startRecording() throws IllegalStateException, IOException | isRecording | " + isRecording);

        return audioFile.getAbsolutePath();
    }

    public static String getDurationOfAudioFile(String absolutePath)
    {
        if(absolutePath == null)
            throw new IllegalArgumentException("CaptureAudioRecording.getDurationOfAudioFile(String absolutePath)- absolutePath cannot be null");

        String finalTimerString = "";

        try
        {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(absolutePath);
            Long milliseconds = Long.parseLong(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

            String secondsString = "";

            // Convert total duration into time
            int hours = (int) (milliseconds / (1000 * 60 * 60));
            int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

            // Add hours if there
            if (hours > 0) {
                finalTimerString = hours + ":";
            }

            // Prepending 0 to seconds if it is one digit
            if (seconds < 10) {
                secondsString = "0" + seconds;
            } else {
                secondsString = "" + seconds;
            }

            finalTimerString = finalTimerString + minutes + ":" + secondsString;
        }
        catch (Exception exception)
        {
            String s = exception.getMessage();  //I really do not care!
            finalTimerString = "??m : ??s";
        }

        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "getDurationOfAudioFile(String absolutePath) | finalTimerString + minutes + : + secondsString | " + finalTimerString);

        return finalTimerString;
    }
}
