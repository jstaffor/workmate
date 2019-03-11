package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
import jstaffor.android.jobsight.datamodel.viewdata.Location;
import jstaffor.android.jobsight.datamodel.viewdata.MyFile;
import jstaffor.android.jobsight.datamodel.viewdata.Photo;
import jstaffor.android.jobsight.datamodel.viewdata.Sketch;
import jstaffor.android.jobsight.datamodel.viewdata.Text;
import jstaffor.android.jobsight.datamodel.viewdata.VideoRecording;
import jstaffor.android.jobsight.datamodel.viewdata.ViewDataGeneric;
import jstaffor.android.jobsight.datamodel.viewdata.ViewDataStatic;
import jstaffor.android.jobsight.utilities.AccessExternalStorage;
import jstaffor.android.jobsight.utilities.CaptureAudioRecording;
import jstaffor.android.jobsight.utilities.CaptureCurrentLocation;
import jstaffor.android.jobsight.utilities.Permissions;

public class ActivityDownload extends Activity implements View.OnClickListener
{
    private static final String TAG = "ActivityDownload";
    private DataModel dataModel;
    private TextView txt_parent, txt_child;
    private Button btn_download;
    private ListView listview;
    private DatabaseAccess databaseAccess;
    private ArrayList<ViewDataGeneric> lViewDatumGenerics;
    private ArrayAdapter<ViewDataGeneric> arrayAdapterViewDataGeneric;
    private Permissions permissions;
    private AccessExternalStorage accessExternalStorage;
    private File targetDir;
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private boolean isMediaPlayerPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaddata);

        txt_parent = findViewById(R.id.activity_downloaddata_parent);
        txt_child = findViewById(R.id.activity_downloaddata_child);
        btn_download = findViewById(R.id.activity_downloaddata_btn_download);
        listview = findViewById(R.id.activity_downloaddata_listview);
        btn_download.setOnClickListener(this);

        //Get Data from App
        dataModel = DataModelUtilities.turnJSONIntoDataModel(getIntent().getExtras().get(DataModelUtilities.DATA_MODEL).toString());

        //Popluate screen with data from DataModel
        txt_parent.setText(dataModel.getsParent());
        txt_child.setText(dataModel.getsChild());

        //Get Data from Database
        lViewDatumGenerics = retrieveDatabaseData(dataModel.getlParent(), dataModel.getlChild());

        // DataBind ListView with items from ArrayAdapter
        arrayAdapterViewDataGeneric = generateArrayAdapters(lViewDatumGenerics,this);

        listview.setAdapter( arrayAdapterViewDataGeneric );
    }

    @Override
    public void onPause() {
        super.onPause();

        stopAudioRecording();   //If the media player is playing, stop it!

        if (AppSettings.APP_DEBUG_MODE) {
            Log.d(TAG, "void onPause() | stopAudioRecording(); | " + "true");
        }
    }

    @Override
    public void onClick(View view) {

        try
        {

        switch (view.getId()) {
            case R.id.activity_downloaddata_btn_download:
                permissions = new Permissions(this);
                if (permissions.hasPermissions(
                        false, false, false, false, true, true, false, false)) {
                    accessExternalStorage = new AccessExternalStorage(this);
                    boolean isExternalStorageReadable = accessExternalStorage.isExternalStorageReadable();
                    boolean isExternalStorageWritable = accessExternalStorage.isExternalStorageWritable();

                    //***********************************************************************************************
                    //(1) If you cannot read or write to the external storage, don't proceed
                    //***********************************************************************************************
                    if (!isExternalStorageReadable) {
                        Toast toast = Toast.makeText(this, getString(R.string.ex_dir_not_readable), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    if (!isExternalStorageWritable) {
                        Toast toast = Toast.makeText(this, getString(R.string.ex_dir_not_writeable), Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    //***********************************************************************************************
                    //(2) If you cannot create the root dir to start creating an output, don't proceed
                    //***********************************************************************************************
                    // attemptToSave1: dataModel.getsParent() + " | " + dataModel.getsChild() + " | " +Calendar.getInstance().getTime()
                    // attemptToSave2: dataModel.getsChild()
                    // attemptToSave3: Calendar.getInstance().getTime().toString()
                    //***********************************************************************************************
                    boolean attemptToSave1 = false;
                    final String sAttemptToSave1 = dataModel.getsParent() + " | " + dataModel.getsChild() + " | " + Calendar.getInstance().getTime();
                    boolean attemptToSave2 = false;
                    final String sAttemptToSave2 = dataModel.getsChild();
                    boolean attemptToSave3 = false;
                    final String sAttemptToSave3 = Calendar.getInstance().getTime().toString();
                    try {
                        targetDir = accessExternalStorage.getExternalStorageDir(sAttemptToSave1);
                        attemptToSave1 = true;
                    } catch (Exception exception_1) {
                        Log.e(TAG, "onClick(View view) | catch (Exception exception_1) | " + exception_1.getMessage());
                        try {
                            targetDir = accessExternalStorage.getExternalStorageDir(sAttemptToSave2);
                            attemptToSave2 = true;

                        } catch (Exception exception_2) {
                            Log.e(TAG, "onClick(View view) | catch (Exception exception_2) | " + exception_2.getMessage());
                            try {
                                targetDir = accessExternalStorage.getExternalStorageDir(sAttemptToSave3);
                                attemptToSave3 = true;

                            } catch (Exception exception_3) {
                                Log.e(TAG, "onClick(View view) | catch (Exception exception_3) | " + exception_3.getMessage());
                                attemptToSave1 = false;
                                attemptToSave2 = false;
                                attemptToSave3 = false;
                            }
                        }
                    }

                    if (AppSettings.APP_DEBUG_MODE) {
                        Log.d(TAG, "onClick(View view) | attemptToSave1: attemptToSave1  | " + attemptToSave1);
                        Log.d(TAG, "onClick(View view) | attemptToSave1: sAttemptToSave1 | " + sAttemptToSave1);
                        Log.d(TAG, "onClick(View view) | attemptToSave2: attemptToSave2  | " + attemptToSave2);
                        Log.d(TAG, "onClick(View view) | attemptToSave2: sAttemptToSave2 | " + sAttemptToSave2);
                        Log.d(TAG, "onClick(View view) | attemptToSave3: attemptToSave3  | " + attemptToSave3);
                        Log.d(TAG, "onClick(View view) | attemptToSave3: sAttemptToSave3 | " + sAttemptToSave3);
                    }

                    //***********************************************************************************************
                    //(3) If we have a file that corresponds to the root which we will write to, proceed!
                    //***********************************************************************************************
                    if (attemptToSave1 || attemptToSave2 || attemptToSave3) {
                        final boolean success = writeToFolder(lViewDatumGenerics, targetDir); //If no error was thrown, success!

                        if (success) {
                            if (attemptToSave1) {
                                final String[] locationOfDownloadedFiles = new String[]{Environment.getExternalStorageState() + File.separator + sAttemptToSave1};
                                final String messageToDisplay = getString(R.string.files_downloaded_success) + ": " + Environment.getExternalStorageState() + File.separator + sAttemptToSave1;
                                MediaScannerConnection.scanFile(this, locationOfDownloadedFiles, null, null);
                                Toast toast = Toast.makeText(this, messageToDisplay, Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            if (attemptToSave2) {
                                final String[] locationOfDownloadedFiles = new String[]{Environment.getExternalStorageState() + File.separator + sAttemptToSave2};
                                final String messageToDisplay = getString(R.string.files_downloaded_success) + ": " + Environment.getExternalStorageState() + File.separator + sAttemptToSave2;
                                MediaScannerConnection.scanFile(this, locationOfDownloadedFiles, null, null);
                                Toast toast = Toast.makeText(this, messageToDisplay, Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            if (attemptToSave3) {
                                final String[] locationOfDownloadedFiles = new String[]{Environment.getExternalStorageState() + File.separator + sAttemptToSave3};
                                final String messageToDisplay = getString(R.string.files_downloaded_success) + ": " + Environment.getExternalStorageState() + File.separator + sAttemptToSave3;
                                MediaScannerConnection.scanFile(this, locationOfDownloadedFiles, null, null);
                                Toast toast = Toast.makeText(this, messageToDisplay, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(this, getString(R.string.files_downloaded_fail), Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    } else {
                        if (AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "void onClick(View view) | if( attemptToSave1 || attemptToSave2 || attemptToSave3 ) | " + false);

                        Toast toast = Toast.makeText(this, getString(R.string.ex_dir_not_creatable), Toast.LENGTH_SHORT);
                        toast.show();
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

    private boolean writeToFolder(ArrayList<ViewDataGeneric> lViewDatumGenerics, File rootDir)
    {
        if (lViewDatumGenerics == null || rootDir == null)
            throw new IllegalArgumentException("AccessExternalStorage.writeToFolder(ArrayList<ViewDataGeneric> lViewDatumGenerics, File targetDir) - inputs cannot be null");

        boolean success = true;

        for (int position = 0; position < lViewDatumGenerics.size(); position++)
        {
            try
            {
                if (lViewDatumGenerics.get(position) instanceof Text) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.text) + "  |  " + position;

                    accessExternalStorage.createChildText(
                            rootDir,
                            metaDataForReferencing,
                            ((Text) lViewDatumGenerics.get(position)).getDateTime() + getString(R.string.accesslocalstorage_txt_format),
                            ((Text) lViewDatumGenerics.get(position)).getTextToDisplay());

                } else if (lViewDatumGenerics.get(position) instanceof Location) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.location) + "  |  " + position;

                    //(1) Prep values for saving
                    final String origNameOfFile = ((Location) lViewDatumGenerics.get(position)).getTextToDisplay();
                    final String pngNameOfFile = ((Location) lViewDatumGenerics.get(position)).getTextToDisplay() + getString(R.string.accesslocalstorage_png_format);

                    final String writeToFile =
                            getString(R.string.latitude) + " : "+
                                    ((Location) lViewDatumGenerics.get(position)).getsLatitude() +
                                    "\n" +
                             getString(R.string.longitude) + " : "+
                                    ((Location) lViewDatumGenerics.get(position)).getsLongitude() +
                                    "\n" +
                                    CaptureCurrentLocation.getInternetBrowserLinkForGMaps(
                                            ((Location) lViewDatumGenerics.get(position)).getsLatitude(), ((Location) lViewDatumGenerics.get(position)).getsLongitude());
                    ;

                    //(2) Create image
                    if (origNameOfFile.contains(getString(R.string.accesslocalstorage_png_format))) {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                origNameOfFile,
                                ((Location) lViewDatumGenerics.get(position)).getImageLocation());
                    } else {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                pngNameOfFile,
                                ((Location) lViewDatumGenerics.get(position)).getImageLocation());
                    }

                    //(3) Create text file
                    accessExternalStorage.createChildText(
                            rootDir,
                            lViewDatumGenerics.get(position).getDateTime(),
                            ((Location) lViewDatumGenerics.get(position)).getDateTime() + getString(R.string.accesslocalstorage_txt_format),
                            writeToFile);

                } else if (lViewDatumGenerics.get(position) instanceof Photo) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.photo) + "  |  " + position;

                    //(1) Prep values for saving
                    final String origNameOfFile = ((Photo) lViewDatumGenerics.get(position)).getDateTime();
                    final String pngNameOfFile = ((Photo) lViewDatumGenerics.get(position)).getDateTime() + getString(R.string.accesslocalstorage_png_format);

                    //(2) Create image
                    if (origNameOfFile.contains(getString(R.string.accesslocalstorage_png_format))) {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                origNameOfFile,
                                ((Photo) lViewDatumGenerics.get(position)).getImageLocation());
                    } else {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                pngNameOfFile,
                                ((Photo) lViewDatumGenerics.get(position)).getImageLocation());
                    }

                } else if (lViewDatumGenerics.get(position) instanceof Sketch) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.sketch) + "  |  " + position;

                    //(1) Prep values for saving
                    final String origNameOfFile = ((Sketch) lViewDatumGenerics.get(position)).getDateTime();
                    final String pngNameOfFile = ((Sketch) lViewDatumGenerics.get(position)).getDateTime() + getString(R.string.accesslocalstorage_png_format);

                    //(2) Create image
                    if (origNameOfFile.contains(getString(R.string.accesslocalstorage_png_format))) {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                origNameOfFile,
                                ((Sketch) lViewDatumGenerics.get(position)).getImageLocation());
                    } else {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                pngNameOfFile,
                                ((Sketch) lViewDatumGenerics.get(position)).getImageLocation());
                    }

                } else if (lViewDatumGenerics.get(position) instanceof AudioRecording) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.text) + "  |  " + position;

                    //(1) Prep values for saving
                    final String origNameOfAudioFile = ((AudioRecording) lViewDatumGenerics.get(position)).getDateTime();
                    final String mp4NameOfAudioFile = ((AudioRecording) lViewDatumGenerics.get(position)).getDateTime() + getString(R.string.accesslocalstorage_mp4_format);

                    //(1) Create audio
                    if (origNameOfAudioFile.contains(getString(R.string.accesslocalstorage_mp4_format))) {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                origNameOfAudioFile,
                                ((AudioRecording) lViewDatumGenerics.get(position)).getFileLocation());
                    } else {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                mp4NameOfAudioFile,
                                ((AudioRecording) lViewDatumGenerics.get(position)).getFileLocation());
                    }

                } else if (lViewDatumGenerics.get(position) instanceof VideoRecording) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.video) + "  |  " + position;

                    //(1) Prep values for saving
                    final String origNameOfFile = ((VideoRecording) lViewDatumGenerics.get(position)).getDateTime();
                    final String mp4NameOfFile = ((VideoRecording) lViewDatumGenerics.get(position)).getDateTime() + getString(R.string.accesslocalstorage_mp4_format);

                    //(1) Create video
                    if (origNameOfFile.contains(getString(R.string.accesslocalstorage_mp4_format))) {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                origNameOfFile,
                                ((VideoRecording) lViewDatumGenerics.get(position)).getVideoLocation());
                    } else {
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                mp4NameOfFile,
                                ((VideoRecording) lViewDatumGenerics.get(position)).getVideoLocation());
                    }

                } else if (lViewDatumGenerics.get(position) instanceof MyFile) {

                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.file) + "  |  " + position;

                    //(1) Prep values for saving
                    final String origNameOfFile = ((MyFile) lViewDatumGenerics.get(position)).getNameOfFile();

                    //(1) Create video
                        accessExternalStorage.createChildGeneric(
                                rootDir,
                                metaDataForReferencing,
                                origNameOfFile,
                                ((MyFile) lViewDatumGenerics.get(position)).getFileLocation());

                }
            }
            catch(Exception exception)
            {
                Log.e(TAG, "writeToFolder(ArrayList<ViewDataGeneric> lViewDatumGenerics, File targetDir) throws IOException | catch(Exception exception) | " +exception.getMessage());
                success = false;
            }
        }

        return success;
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

                //(1) Reset any lingering functionality
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
                                stopAudioRecording();   //If the media player is playing, stop it!

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