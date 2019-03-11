package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jstaffor.android.jobsight.BuildConfig;
import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.database.functionality.DatabaseAudioRecording;
import jstaffor.android.jobsight.database.functionality.DatabaseFile;
import jstaffor.android.jobsight.database.functionality.DatabaseLocation;
import jstaffor.android.jobsight.database.functionality.DatabasePhoto;
import jstaffor.android.jobsight.database.functionality.DatabaseSketch;
import jstaffor.android.jobsight.database.functionality.DatabaseText;
import jstaffor.android.jobsight.database.functionality.DatabaseVideoRecording;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.datamodel.viewdata.AudioRecording;
import jstaffor.android.jobsight.datamodel.viewdata.MyFile;
import jstaffor.android.jobsight.datamodel.viewdata.ViewDataGeneric;
import jstaffor.android.jobsight.datamodel.viewdata.Location;
import jstaffor.android.jobsight.datamodel.viewdata.Photo;
import jstaffor.android.jobsight.datamodel.viewdata.Sketch;
import jstaffor.android.jobsight.datamodel.viewdata.VideoRecording;
import jstaffor.android.jobsight.datamodel.viewdata.Text;
import jstaffor.android.jobsight.datamodel.viewdata.ViewDataStatic;
import jstaffor.android.jobsight.utilities.CaptureAudioRecording;
import jstaffor.android.jobsight.utilities.CaptureCurrentLocation;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityView extends Activity
{
    private static final String TAG = "ActivityView";
    private DataModel dataModel;
    private TextView txt_parent, txt_child;
    private ListView listview;
    private DatabaseAccess databaseAccess;
    private ArrayList<ViewDataGeneric> lViewDatumGenerics;
    private ArrayAdapter<ViewDataGeneric> arrayAdapterViewDataGeneric;
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isMediaPlayerPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewdata);

        txt_parent = findViewById(R.id.activity_viewdata_parent);
        txt_child = findViewById(R.id.activity_viewdata_child);
        listview = findViewById(R.id.activity_viewdata_listview);

        //Get Data from App
        dataModel = DataModelUtilities.turnJSONIntoDataModel(getIntent().getExtras().get(DataModelUtilities.DATA_MODEL).toString());

        //Popluate screen with data from DataModel
        txt_parent.setText(dataModel.getsParent());
        txt_child.setText(dataModel.getsChild());

        //Get Data from Database
        lViewDatumGenerics = retrieveDatabaseData(dataModel.getlParent(), dataModel.getlChild());

        // DataBind ListView with items from ArrayAdapter
        arrayAdapterViewDataGeneric = generateArrayAdapters(lViewDatumGenerics,this);

        // DataBind ListView with items from ArrayAdapter
        listview.setAdapter( arrayAdapterViewDataGeneric );
    }

    @Override
    public void onPause() {
        super.onPause();

        stopAudioRecording();

        if (AppSettings.APP_DEBUG_MODE) {
            Log.d(TAG, "void onPause() | stopAudioRecording(); | " + "true");
        }
    }

    private ArrayList<ViewDataGeneric> retrieveDatabaseData(Long lParent, Long lChild)
    {
        final ArrayList<ViewDataGeneric> viewDataGeneric = new ArrayList<ViewDataGeneric>();

        databaseAccess = new DatabaseText(this);
        final List<Text> lText = ((DatabaseText) databaseAccess).getTextDataFromDatabase(lChild);

        databaseAccess = new DatabaseLocation(this);
        final List<Location> lLocation = ((DatabaseLocation) databaseAccess).getLocationDataFromDatabase(lChild);

        databaseAccess = new DatabasePhoto(this);
        final List<Photo> lPhoto = ((DatabasePhoto) databaseAccess).getPhotoDataFromDatabase(lChild);

        databaseAccess = new DatabaseSketch(this);
        final List<Sketch> lSketch = ((DatabaseSketch) databaseAccess).getSketchDataFromDatabase(lChild);

        databaseAccess = new DatabaseVideoRecording(this);
        final List<VideoRecording> lVideoRecording = ((DatabaseVideoRecording) databaseAccess).getVideoRecordingDataFromDatabase(lChild);

        databaseAccess = new DatabaseAudioRecording(this);
        final List<AudioRecording> lAudioRecording = ((DatabaseAudioRecording) databaseAccess).getAudioRecordingDataFromDatabase(lChild);

        databaseAccess = new DatabaseFile(this);
        final List<MyFile> lMyFile = ((DatabaseFile) databaseAccess).getFileDataFromDatabase(lChild);

        for (int i = 0; i < lText.size(); i++) {
            viewDataGeneric.add(lText.get(i));

            if(AppSettings.APP_DEBUG_MODE) {
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lText.get(i).getDateTime() | " + lText.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lText.get(i).getTextToDisplay() | " + lText.get(i).getTextToDisplay());
            }
        }

        for (int i = 0; i < lLocation.size(); i++) {
            viewDataGeneric.add(lLocation.get(i));

            if (AppSettings.APP_DEBUG_MODE) {
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getDateTime() | " + lLocation.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getTextToDisplay() | " + lLocation.get(i).getTextToDisplay());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getsLatitude() | " + lLocation.get(i).getsLatitude());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getsLongitude() | " + lLocation.get(i).getsLongitude());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getImageLocation() | " + lLocation.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getImageBitmap().getAllocationByteCount() | " + lLocation.get(i).getImageBitmap().getAllocationByteCount());
            }
        }

        for (int i = 0; i < lPhoto.size(); i++) {
            viewDataGeneric.add(lPhoto.get(i));

            if (AppSettings.APP_DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lPhoto.get(i).getDateTime() | " + lPhoto.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lPhoto.get(i).getImageLocation() | " + lPhoto.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lPhoto.get(i).getImageBitmap().getAllocationByteCount() | " + lPhoto.get(i).getImageBitmap().getAllocationByteCount());
            }
        }

        for (int i = 0; i < lSketch.size(); i++) {
            viewDataGeneric.add(lSketch.get(i));

            if (AppSettings.APP_DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lSketch.get(i).getDateTime() | " + lSketch.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lSketch.get(i).getImageLocation() | " + lSketch.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lSketch.get(i).getImageBitmap().getAllocationByteCount() | " + lSketch.get(i).getImageBitmap().getAllocationByteCount());
            }
        }

        for (int i = 0; i < lVideoRecording.size(); i++) {
            viewDataGeneric.add(lVideoRecording.get(i));

            if (AppSettings.APP_DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getDateTime() | " + lVideoRecording.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getImageLocation() | " + lVideoRecording.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getVideoLocation() | " + lVideoRecording.get(i).getVideoLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getImageBitmap().getAllocationByteCount() | " + lVideoRecording.get(i).getImageBitmap().getAllocationByteCount());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getUri() | " + lVideoRecording.get(i).getUri());
            }
        }

        for (int i = 0; i < lAudioRecording.size(); i++) {
            viewDataGeneric.add(lAudioRecording.get(i));

            if (AppSettings.APP_DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getDateTime() | " + lAudioRecording.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getImageLocation() | " + lAudioRecording.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getImageBitmap().getAllocationByteCount() | " + lAudioRecording.get(i).getImageBitmap().getAllocationByteCount());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getFileLocation() | " + lAudioRecording.get(i).getFileLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getFile().getAbsolutePath() | " + lAudioRecording.get(i).getFile().getAbsolutePath());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getFile().getName() | " + lAudioRecording.get(i).getFile().getName());
            }
        }

        for (int i = 0; i < lMyFile.size(); i++) {
            viewDataGeneric.add(lMyFile.get(i));

            if (AppSettings.APP_DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getDateTime() | " + lMyFile.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getNameOfFile() | " + lMyFile.get(i).getNameOfFile());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getFile().getName() | " + lMyFile.get(i).getFile().getName());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getFileLocation() | " + lMyFile.get(i).getFileLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getFile().getAbsolutePath() | " + lMyFile.get(i).getFile().getAbsolutePath());
            }
        }

        Collections.sort(viewDataGeneric, new Comparator<ViewDataGeneric>()
        {
            public int compare(ViewDataGeneric o1, ViewDataGeneric o2)
            {
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });

        return viewDataGeneric;
    }

    private ArrayAdapter<ViewDataGeneric> generateArrayAdapters(final ArrayList<ViewDataGeneric> lViewDatumGenerics, final Context context)
    {
        final ArrayAdapter<ViewDataGeneric> arrayAdapterViewDataGeneric = new ArrayAdapter<ViewDataGeneric>(this, R.layout.view_viewdata, lViewDatumGenerics)
        {
            @Override
            public View getView(final int position, View vView_viewdata, ViewGroup parent)
            {
                if (vView_viewdata == null)
                {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    vView_viewdata = layoutInflater.inflate(R.layout.view_viewdata, parent, false);
                }

                final ViewDataStatic.Components components = new ViewDataStatic.Components();

                //https://developer.android.com/training/improving-layouts/smooth-scrolling
                components.dateTime = vView_viewdata.findViewById(R.id.view_viewdata_datetime);
                components.txt = vView_viewdata.findViewById(R.id.view_viewdata_txt);
                components.image = vView_viewdata.findViewById(R.id.view_viewdata_image);

                //Reset any lingering functionality
                components.txt.setVisibility(View.GONE);
                components.image.setVisibility(View.GONE);

                components.image.setOnClickListener(null);
                stopAudioRecording();   //If the media player is playing, stop it!

                components.dateTime.setBackgroundColor(Integer.parseInt(getString(R.string.color_black)));
                components.dateTime.setTextColor(Integer.parseInt(getString(R.string.color_white)));

                //Setup custom view for the particular instanceof lViewDatumGenerics
                if (lViewDatumGenerics.get(position) instanceof Text)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.text) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.txt.setText(((Text) lViewDatumGenerics.get(position)).getTextToDisplay());

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof Location)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.location) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.image.setVisibility(View.VISIBLE);
                    components.txt.setText(((Location) lViewDatumGenerics.get(position)).getTextToDisplay());
                    components.image.setImageBitmap(((Location) lViewDatumGenerics.get(position)).getImageBitmap());
                    components.image.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            stopAudioRecording();   //If the media player is playing, stop it!

                            final String urlToOpen = CaptureCurrentLocation.getInternetBrowserLinkForGMaps(
                                    ((Location) lViewDatumGenerics.get(position)).getsLatitude(), ((Location) lViewDatumGenerics.get(position)).getsLongitude());

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen));
                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag

                            startActivity(intent);
                        }
                    });

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof Photo)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.photo) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.image.setVisibility(View.VISIBLE);
                    components.image.setImageBitmap(((Photo) lViewDatumGenerics.get(position)).getImageBitmap());

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof Sketch)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.sketch) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.image.setVisibility(View.VISIBLE);
                    components.image.setImageBitmap(((Sketch) lViewDatumGenerics.get(position)).getImageBitmap());

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof VideoRecording)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.video) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.image.setVisibility(View.VISIBLE);
                    components.image.setImageBitmap(((VideoRecording) lViewDatumGenerics.get(position)).getImageBitmap());
                    components.image.setOnClickListener(new View.OnClickListener()
                        {

                        public void onClick(View v)
                        {
                            stopAudioRecording();   //If the media player is playing, stop it!

                            final Uri tempUri = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID + ".provider", new File((((VideoRecording) lViewDatumGenerics.get(position)).getVideoLocation())));

                            final Intent intent = new Intent(Intent.ACTION_VIEW, tempUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intent.setDataAndType(tempUri, "video/*");
                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag

                            startActivity(intent);
                        }
                    });

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof AudioRecording)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.audio) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.image.setVisibility(View.VISIBLE);
                    components.txt.setText(CaptureAudioRecording.getDurationOfAudioFile( ((AudioRecording) lViewDatumGenerics.get(position)).getFileLocation() ));
                    components.image.setImageBitmap( ((AudioRecording) lViewDatumGenerics.get(position)).getImageBitmap() );
                    components.image.setOnClickListener(new View.OnClickListener()
                    {
                        public void onClick(View v)
                        {
                            try
                            {
                                stopAudioRecording();

                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaPlayer.setDataSource(((AudioRecording) lViewDatumGenerics.get(position)).getFileLocation());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                                isMediaPlayerPlaying = true;

                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
                                {
                                    @Override
                                    public void onCompletion(MediaPlayer mp)
                                    {
                                        stopAudioRecording();
                                    }
                                });

                            } catch (Exception e)
                            {
                                Log.e(TAG, "if (lViewDatumGenerics.get(position) instanceof AudioRecording) void - onClick(View v) | catch (Exception e) | " + e.getMessage());

                                if (AppSettings.APP_DEBUG_MODE)
                                    Log.d(TAG, "if (lViewDatumGenerics.get(position) instanceof AudioRecording) void - onClick(View v) | catch (Exception e) | " + e.getMessage());
                            }
                        }
                    });

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);
                } else if (lViewDatumGenerics.get(position) instanceof MyFile)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.file) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.txt.setText( ((MyFile) lViewDatumGenerics.get(position)).getNameOfFile() );

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else {
                    Log.e(TAG, "onClick(View view) - unknown selection - populateListOfViewsToDisplay(ArrayList<ViewDataGeneric> lViewDatumGenerics) - class doesn't exist");
                    throw new IllegalArgumentException();
                }

                return vView_viewdata;
            }
        };

        return arrayAdapterViewDataGeneric;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(AppSettings.APP_DEBUG_MODE)
                Log.d(TAG, "onKeyDown(int keyCode, KeyEvent event) | (keyCode == KeyEvent.KEYCODE_BACK) | " + "shutDownAudioRecordingAndVisulizer()");

            stopAudioRecording();
            releaseMediaPlayer();

            final Intent intent = new Intent(this, ActivityHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void stopAudioRecording() {
        if (mediaPlayer != null && isMediaPlayerPlaying)
        {
            try {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    isMediaPlayerPlaying = false;

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "stopAudioRecording() | mediaPlayer.stop() | " + "mediaPlayer.stop()");

            } catch (Exception e) {
                Log.e(TAG, "stopAudioRecording() mediaPlayer.stop() | catch (Exception e) | " + e.getMessage());

                if (AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "stopAudioRecording() mediaPlayer.stop() | catch (Exception e) | " + e.getMessage());
            }
        }
    }

    private void releaseMediaPlayer() {
        try {
            mediaPlayer.release();
        } catch (Exception e) {
            Log.e(TAG, "releaseMediaPlayer() mediaPlayer.release() | catch (Exception e) | " + e.getMessage());

            if (AppSettings.APP_DEBUG_MODE)
                Log.d(TAG, "releaseMediaPlayer() mediaPlayer.release() | catch (Exception e) | " + e.getMessage());
        }
    }
}