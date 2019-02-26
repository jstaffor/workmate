package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.functionality.ActivityAudioInput;
import jstaffor.android.jobsight.activities.functionality.ActivitySketchInput;
import jstaffor.android.jobsight.activities.functionality.ActivityTextInput;
import jstaffor.android.jobsight.activities.functionality.PopupLocationInput;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.functionality.DatabaseFile;
import jstaffor.android.jobsight.database.functionality.DatabasePhoto;
import jstaffor.android.jobsight.database.functionality.DatabaseVideoRecording;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.datamodel.viewdata.Location;
import jstaffor.android.jobsight.utilities.AccessInternalStorage;
import jstaffor.android.jobsight.utilities.CaptureCurrentLocation;
import jstaffor.android.jobsight.utilities.Permissions;

public class ActivityCreateAndAdd extends Activity implements View.OnClickListener
{
    private static final String TAG = "ActivityCreateAndAdd";
    private TextView txt_parent, txt_child;
    private Button btn_add_text, btn_add_photo, btn_add_current_loc, btn_add_sketch, btn_add_audio_recording, btn_add_video_recording, btn_add_file;
    private DataModel dataModel;
    public DataModel getDataModel() {return dataModel;}
    private Permissions permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_create);

        // Initialize view
        this.txt_parent = findViewById(R.id.activity_home_create_txt_parent);
        this.txt_child = findViewById(R.id.activity_home_create_txt_child);
        this.btn_add_text = findViewById(R.id.activity_home_create_btn_add_text);
        this.btn_add_photo = findViewById(R.id.activity_home_create_btn_add_photo);
        this.btn_add_current_loc = findViewById(R.id.activity_home_create_btn_add_current_loc);
        this.btn_add_sketch = findViewById(R.id.activity_home_create_btn_add_sketch);
        this.btn_add_audio_recording = findViewById(R.id.activity_home_create_btn_add_audio_recording);
        this.btn_add_video_recording = findViewById(R.id.activity_home_create_btn_add_video_recording);
        this.btn_add_file = findViewById(R.id.activity_home_create_btn_add_file);

        //Get Data
        dataModel = DataModelUtilities.turnJSONIntoDataModel(getIntent().getExtras().get(DataModelUtilities.DATA_MODEL).toString());

        permissions = new Permissions(this);

        // Setup Screen to hide buttons to satisfy template selection
        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_TEXT_INPUT.toString()))
            this.btn_add_text.setOnClickListener(this);
        else
            this.btn_add_text.setVisibility(View.GONE);

        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_PHOTO_INPUT.toString()))
            this.btn_add_photo.setOnClickListener(this);
        else
            this.btn_add_photo.setVisibility(View.GONE);

        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_CURRENTLOCATION_INPUT.toString()))
            this.btn_add_current_loc.setOnClickListener(this);
        else
            this.btn_add_current_loc.setVisibility(View.GONE);

        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_SKETCH_INPUT.toString()))
            this.btn_add_sketch.setOnClickListener(this);
        else
            this.btn_add_sketch.setVisibility(View.GONE);

        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_AUDIORECORDING_INPUT.toString()))
            this.btn_add_audio_recording.setOnClickListener(this);
        else
            this.btn_add_audio_recording.setVisibility(View.GONE);

        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_VIDEORECORDING_INPUT.toString()))
            this.btn_add_video_recording.setOnClickListener(this);
        else
            this.btn_add_video_recording.setVisibility(View.GONE);

        if ((dataModel.getlTemplate().toString()).contains(DataModel.SETTING_FILE_INPUT.toString()))
            this.btn_add_file.setOnClickListener(this);
        else
            this.btn_add_file.setVisibility(View.GONE);

        //Popluate screen with data from DataModel
        txt_parent.setText(dataModel.getsParent());
        txt_child.setText(dataModel.getsChild());
    }

    @Override
    public void onClick(View view)
    {
        try
        {
        switch (view.getId()) {
            case R.id.activity_home_create_btn_add_text:
                final Intent intentActivityTextInput = new Intent(this, ActivityTextInput.class);
                intentActivityTextInput.setFlags(intentActivityTextInput.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                intentActivityTextInput.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intentActivityTextInput);
                break;
            case R.id.activity_home_create_btn_add_photo:
                if (permissions.hasPermissions(
                        true, false, false, false, true, false, false, false)) {
                    final Intent intentACTION_IMAGE_CAPTURE = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intentACTION_IMAGE_CAPTURE.setFlags(intentACTION_IMAGE_CAPTURE.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    startActivityForResult(intentACTION_IMAGE_CAPTURE, Integer.parseInt( DataModel.SETTING_PHOTO_INPUT.toString()));
                }
                break;
            case R.id.activity_home_create_btn_add_current_loc:
                if (permissions.hasPermissions(
                        false, true, true, false, false, false, true, true))
                    captureLocation();
                break;
            case R.id.activity_home_create_btn_add_sketch:
                final Intent intentActivitySketchInput= new Intent(
                        this, ActivitySketchInput.class);
                intentActivitySketchInput.setFlags(intentActivitySketchInput.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                intentActivitySketchInput.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intentActivitySketchInput);
                break;
            case R.id.activity_home_create_btn_add_audio_recording:
                if (permissions.hasPermissions(
                        false, false, false, true, false, false, false, false)) {
                    final Intent intentActivitySoundRecordingInput = new Intent(this, ActivityAudioInput.class);
                    intentActivitySoundRecordingInput.setFlags(intentActivitySoundRecordingInput.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    intentActivitySoundRecordingInput.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                    startActivity(intentActivitySoundRecordingInput);
                }
                break;
            case R.id.activity_home_create_btn_add_video_recording:
                if (permissions.hasPermissions(
                        true, false, false, false, false, false, false, false)) {
                    Intent intentActivityVideoRecordingInput = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intentActivityVideoRecordingInput.setFlags(intentActivityVideoRecordingInput.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    startActivityForResult(intentActivityVideoRecordingInput, Integer.parseInt( DataModel.SETTING_VIDEORECORDING_INPUT.toString()));
                }
                break;
            case R.id.activity_home_create_btn_add_file:
                if (permissions.hasPermissions(
                        false, false, false, false, false, false, false, false)) {
                    Intent intentActivityFileInput = new Intent(Intent.ACTION_GET_CONTENT);
                    intentActivityFileInput.setType("*/*");
                    intentActivityFileInput.addCategory(Intent.CATEGORY_OPENABLE);
                    intentActivityFileInput.setFlags(intentActivityFileInput.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                    startActivityForResult(Intent.createChooser(intentActivityFileInput, getString(R.string.select_a_file)), Integer.parseInt( DataModel.SETTING_FILE_INPUT.toString()));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == Integer.parseInt( DataModel.SETTING_PHOTO_INPUT.toString()) && resultCode == RESULT_OK) {
            try {
                //(1) Save to local database - nullpointer indicates issue saving
                AccessInternalStorage accessInternalStorage = new AccessInternalStorage(this);
                Toast toast1 = Toast.makeText(this, "SizeOfImage: " +((Bitmap) intent.getExtras().get("data")).getAllocationByteCount(), Toast.LENGTH_SHORT);
                toast1.show();

                final String absolutePath = accessInternalStorage.saveBitmapToInternalStorage((Bitmap) intent.getExtras().get("data"), dataModel.getlParent(), dataModel.getlChild());

                if(AppSettings.DEBUG_MODE)
                    Log.d(TAG, "onActivityResult accessInternalStorage.saveBitmapToInternalStorage() | absolutePath | " + absolutePath);

                if (absolutePath != null)
                {
                    //(2) Save to local database
                    if (new DatabasePhoto(this).createPhotoEntry(dataModel.getlChild(), absolutePath)) {
                        Toast toast = Toast.makeText(this, getString(R.string.photo_saved_success), Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) new DatabaseVideoRecording(this).createPhotoEntry() | createPhotoEntry() | " + " false");
                        Toast toast = Toast.makeText(this, getString(R.string.photo_saved_fail), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Log.e(TAG, "onActivityResult saveBitmapToInternalStorage() | absolutePath | " + " null");
                    Toast toast = Toast.makeText(this, getString(R.string.photo_saved_fail), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception exception) {
                Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent)  | DataModel.SETTING_PHOTO_INPUT | " + exception.getMessage());
                Toast toast = Toast.makeText(this, getString(R.string.photo_saved_fail), Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (requestCode == Integer.parseInt( DataModel.SETTING_VIDEORECORDING_INPUT.toString()) && resultCode == RESULT_OK) {
            try {
                //(1) Save to local database - nullpointer indicates issue saving
                AccessInternalStorage accessInternalStorage = new AccessInternalStorage(this);

                //(2)Get absolute path to saved video
                final String absolutePathToCurrentFile = accessInternalStorage.getRealPathFromURI(intent.getData());

                if(AppSettings.DEBUG_MODE)
                    Log.d(TAG, "onActivityResult accessInternalStorage.getRealPathFromURI() | absolutePathToCurrentFile | " + absolutePathToCurrentFile);

                //(3) Create a myFile so that we can copy one myFile to another
                final File x = new File(absolutePathToCurrentFile);

                //(4) Copy myFile internally and delete original myFile
                final String absolutePathToNewFile = accessInternalStorage.copyFileFrom_x_to_y_(x, accessInternalStorage.getVideoFileWithRandomName( dataModel.getlParent(), dataModel.getlChild() ));

                if(AppSettings.DEBUG_MODE)
                    Log.d(TAG, "onActivityResult accessInternalStorage.getRealPathFromURI() | absolutePathToNewFile | " + absolutePathToNewFile);

                //(5) Create Thumbnail
                final String imageLocation = accessInternalStorage.saveBitmapToInternalStorage( ThumbnailUtils.createVideoThumbnail(absolutePathToNewFile, MediaStore.Video.Thumbnails.MICRO_KIND), dataModel.getlParent(), dataModel.getlChild());

                if(AppSettings.DEBUG_MODE)
                    Log.d(TAG, "onActivityResult accessInternalStorage.saveBitmapToInternalStorage() | imageLocation | " + imageLocation);

                if (absolutePathToNewFile != null && imageLocation != null)
                {
                    //(6) Delete the myFile and clear the cache
                    final boolean hasFileBeenDeleted = x.delete();

                    if(hasFileBeenDeleted)
                    {
                        if(AppSettings.DEBUG_MODE)
                            Log.d(TAG, "onActivityResult x.delete() | hasFileBeenDeleted | " + hasFileBeenDeleted);

                        MediaScannerConnection.scanFile( this, new String[]{absolutePathToCurrentFile}, null, null);

                        //(7) Save to local database
                        if (new DatabaseVideoRecording(this).createVideoRecordingEntry(dataModel.getlChild(), absolutePathToNewFile, imageLocation))
                        {
                            Toast toast = Toast.makeText(this, getString(R.string.videorecording_saved_success), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else
                        {
                            Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) new DatabaseVideoRecording(this).createVideoRecordingEntry() | createVideoRecordingEntry() | " + " false");
                            Toast toast = Toast.makeText(this, getString(R.string.videorecording_saved_fail), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    else
                    {
                        Log.e(TAG, "onActivityResult x.delete() | hasFileBeenDeleted | " + hasFileBeenDeleted);
                        Toast toast = Toast.makeText(this, getString(R.string.videorecording_saved_fail), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                {
                    Log.e(TAG, "onActivityResult imageLocation != null | imageLocation | " + " null");
                    Toast toast = Toast.makeText(this, getString(R.string.videorecording_saved_fail), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception exception) {
                Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) | DataModel.SETTING_VIDEORECORDING_INPUT | " + exception.getMessage());
                Toast toast = Toast.makeText(this, getString(R.string.videorecording_saved_fail), Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        if (requestCode == Integer.parseInt( DataModel.SETTING_FILE_INPUT.toString()) && resultCode == RESULT_OK)
        {
            //(1) Save to local database - nullpointer indicates issue saving
            AccessInternalStorage accessInternalStorage = new AccessInternalStorage(this);

            InputStream fileInputStream = null;
            OutputStream fileOutputStream = null;
            boolean saveSuccessful = false;

            //(1)Obtain MEME for myFile
            String memeForFile = accessInternalStorage.getMimeTypeUsingUri(intent.getData());
            String nameOfFile = accessInternalStorage.getFileNameFromUri(intent.getData());
            if(nameOfFile == null || nameOfFile.trim().length() == 0)
            {
                if (memeForFile == null || memeForFile.trim().length() == 0)
                    nameOfFile = DataModel.DEFAULT_VALUE_COLUMN_FILE_NAME;
                else
                    nameOfFile = memeForFile;
            }

            if(AppSettings.DEBUG_MODE){
                Log.d(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) DataModel.SETTING_FILE_INPUT | memeForFile | " + memeForFile);
                Log.d(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) DataModel.SETTING_FILE_INPUT | nameOfFile | " + nameOfFile);
            }

            File fileToSave = null;

            try
            {
                //(2) Create a myFile internally for saving the external myFile too
                if(memeForFile != null && memeForFile.trim().length() !=0 )
                    fileToSave = new File(accessInternalStorage.createChildDirForParentChild(dataModel.getlParent(), dataModel.getlChild()), UUID.randomUUID().toString() + memeForFile);
                else
                    fileToSave = new File(accessInternalStorage.createChildDirForParentChild(dataModel.getlParent(), dataModel.getlChild()), UUID.randomUUID().toString());

                if(AppSettings.DEBUG_MODE)
                    Log.d(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) DataModel.SETTING_FILE_INPUT | fileToSave | " + fileToSave);

                //(2) Out a stream for inputting data
                fileOutputStream = new FileOutputStream(fileToSave);

                //(3) Get a handle on the external myFile
                fileInputStream = this.getContentResolver().openInputStream(intent.getData());

                final byte[] buf = new byte[1024];
                int lenght;
                while ((lenght = fileInputStream.read(buf)) > 0)
                {
                    fileOutputStream.write(buf, 0, lenght);
                }

                saveSuccessful = true;

            } catch (Exception exception) {
                Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent)  | DataModel.SETTING_FILE_INPUT | " + exception.getMessage());
                Toast toast = Toast.makeText(this, getString(R.string.file_saved_fail), Toast.LENGTH_SHORT);
                toast.show();
            }
            finally {
                if( fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (Exception exception) {
                        Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) | fileInputStream.close() | " + exception.getMessage());
                    }
                }

                if( fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (Exception exception) {
                        Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) | fileOutputStream.close() | " + exception.getMessage());
                    }
                }
                //Ensure garbage collection gets it for lunch :)
                fileInputStream= null;
                fileOutputStream= null;
            }

            try
            {
                if (saveSuccessful && fileToSave != null && fileToSave.getAbsolutePath() != null)
                {
                    if (new DatabaseFile(this).createFileEntry(dataModel.getlChild(), fileToSave.getAbsolutePath(), nameOfFile))
                    {
                        Toast toast = Toast.makeText(this, getString(R.string.file_saved_success), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) | databaseAccess.createFileEntry(dataModel.getlChild(), fileToSave.getAbsolutePath(), nameOfFile) | " + " false");
                        Toast toast = Toast.makeText(this, getString(R.string.file_saved_fail), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
                else
                {
                    Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) | saveSuccessful && fileToSave != null && fileToSave.getAbsolutePath() != null |" + " this check failed");
                    Toast toast = Toast.makeText(this, getString(R.string.file_saved_fail), Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (Exception exception) {
                Log.e(TAG, "onActivityResult(int requestCode, int resultCode, Intent intent) | saveSuccessful && fileToSave != null && fileToSave.getAbsolutePath() != null |" + exception.getMessage());
                Toast toast = Toast.makeText(this, getString(R.string.file_saved_fail), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    private void captureLocation()
    {
        try
        {
            //(1) Capture the location
            CaptureCurrentLocation captureCurrentLocation = new CaptureCurrentLocation(this);

            //(2) Create and populates DTO with data for popup
            Bundle args = new Bundle();

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "Location.ADDRESS: " + captureCurrentLocation.getAddressFromCoordinates(captureCurrentLocation.getLatitude(), captureCurrentLocation.getLongitude()) );
                Log.d(TAG, "Location.LATITUDE: " + String.valueOf(captureCurrentLocation.getLatitude()) );
                Log.d(TAG, "Location.LONGITUDE: " + String.valueOf(captureCurrentLocation.getLongitude()) );
            }

            args.putLong(DataModel.CHILD, dataModel.getlChild());
            args.putString(Location.ADDRESS, captureCurrentLocation.getAddressFromCoordinates(captureCurrentLocation.getLatitude(), captureCurrentLocation.getLongitude()));
            args.putString(Location.LATITUDE, String.valueOf(captureCurrentLocation.getLatitude()) );
            args.putString(Location.LONGITUDE, String.valueOf(captureCurrentLocation.getLongitude()) );

            //(3) Create and trigger popup
            final PopupLocationInput dialogPopupLocationInput = new PopupLocationInput();
            final FragmentTransaction ftPopupLocationInput = getFragmentManager().beginTransaction();
            final Fragment prevPopupLocationInput = getFragmentManager().findFragmentByTag("dialog");

            if (prevPopupLocationInput != null) {
                ftPopupLocationInput.remove(prevPopupLocationInput);
            }
            dialogPopupLocationInput.setArguments(args);
            ftPopupLocationInput.addToBackStack(null);
            dialogPopupLocationInput.show(ftPopupLocationInput, "dialog");

        } catch (SecurityException securityException) {
            Log.e(TAG, "captureLocation() | catch (SecurityException securityException)  | " + securityException.getMessage());
            Toast toast = Toast.makeText(this, getString(R.string.device_permissions_error), Toast.LENGTH_SHORT);
            toast.show();
        } catch (RemoteException remoteException) {
            Log.e(TAG, "captureLocation() | catch (RemoteException remoteException)  | " + remoteException.getMessage());
            Toast toast = Toast.makeText(this, getString(R.string.no_gps_or_network_error), Toast.LENGTH_SHORT);
            toast.show();
        } catch (CaptureCurrentLocation.CaptureCurrentLocationException captureCurrentLocationException) {
            Log.e(TAG, "captureLocation() | catch (CaptureCurrentLocation.CaptureCurrentLocationException captureCurrentLocationException)  | " + captureCurrentLocationException.getMessage());
            Toast toast = Toast.makeText(this, getString(R.string.capture_location_error), Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception exception) {
            Log.e(TAG, "captureLocation() | catch (Exception exception)  | " + exception.getMessage());
            Toast toast = Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this, ActivityHome.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}