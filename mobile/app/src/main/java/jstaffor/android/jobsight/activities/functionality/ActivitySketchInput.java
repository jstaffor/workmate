package jstaffor.android.jobsight.activities.functionality;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityCreateAndAdd;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.functionality.DatabaseSketch;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.utilities.AccessInternalStorage;
import jstaffor.android.jobsight.utilities.CaptureSketchInput;

public class ActivitySketchInput extends Activity implements View.OnClickListener
{
    private static final String TAG = "ActivitySketchInput";
    private Button btn_signature_save, btn_signature_clear;
    private LinearLayout lActivitySketchInputLinearLayout;
    private CaptureSketchInput captureSketchInput;
    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketchinput);

        // Initialize view
        btn_signature_save = findViewById(R.id.activity_sketchinput_btn_signature_save);
        btn_signature_clear = findViewById(R.id.activity_sketchinput_btn_signature_clear);
        lActivitySketchInputLinearLayout = findViewById(R.id.activity_sketchinput_linlay_sketchinput);

        btn_signature_save.setOnClickListener(this);
        btn_signature_clear.setOnClickListener(this);

        captureSketchInput = new CaptureSketchInput(this, null, ((Activity) this).getIntent());
        lActivitySketchInputLinearLayout.addView(captureSketchInput, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //Get Data
        dataModel = DataModelUtilities.turnJSONIntoDataModel( getIntent().getExtras().get(DataModelUtilities.DATA_MODEL).toString());

        if(AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "onCreate(Bundle savedInstanceState) | DataModelUtilities.turnJSONIntoDataModel() | " + DataModelUtilities.turnDataModelIntoJSON( dataModel ));
    }

    @Override
    public void onClick(View view) {
        try
        {
        switch (view.getId()) {
            case R.id.activity_sketchinput_btn_signature_save:

                if(saveSignatureToLocalStorageAndStoreLocationInDatabase(captureSketchInput.getBitmap()))
                {
                    Toast toast = Toast.makeText(this, getString(R.string.sketch_saved_success), Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    Log.e(TAG, "onClick(View view) | case R.id.activity_sketchinput_btn_signature_save saveSignatureToLocalStorageAndStoreLocationInDatabase(captureSketchInput.getBitmap()) | " + "false");
                    Toast toast = Toast.makeText(this, getString(R.string.sketch_saved_fail), Toast.LENGTH_SHORT);
                    toast.show();
                }

                Intent intent = new Intent(this, ActivityCreateAndAdd.class);
                intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intent);

                break;
            case R.id.activity_sketchinput_btn_signature_clear:
                captureSketchInput.clearCanvasAndBitmap();

                if(AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "onClick(View view) | case R.id.activity_sketchinput_btn_signature_clear | " + "captureSketchInput.clearCanvasAndBitmap()");

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

    private boolean saveSignatureToLocalStorageAndStoreLocationInDatabase(Bitmap bSignatureBitmap)
    {
        boolean success = false;

        try
        {
            //(1) Save to local database - nullpointer indicates issue saving
            AccessInternalStorage accessInternalStorage = new AccessInternalStorage(this);

            success = new DatabaseSketch(this).createSketchEntry(dataModel.getlChild(), accessInternalStorage.saveBitmapToInternalStorage(bSignatureBitmap, dataModel.getlParent(), dataModel.getlChild()));
        }
        catch (Exception exception)
        {
            Log.e(TAG, "saveSignatureToLocalStorageAndStoreLocationInDatabase(Bitmap bSignatureBitmap) | catch (Exception exception) | " +exception);
            success = false;
        }

        if(AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "saveSignatureToLocalStorageAndStoreLocationInDatabase(Bitmap bSignatureBitmap) | createSketchEntry(dataModel.getlChild(), accessLocalStorage.saveBitmapToInternalStorage(bSignatureBitmap, dataModel.getlParent(), dataModel.getlChild()) | " + success);

        return success;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent a = new Intent(this, ActivityCreateAndAdd.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}