package jstaffor.android.jobsight.activities.functionality;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityCreateAndAdd;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.functionality.DatabaseText;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;

public class ActivityTextInput extends Activity implements View.OnClickListener
{
    private static final String TAG = "ActivityTextInput";
    private Button btn_text_clear, btn_text_save;
    private EditText txt_text_capture;
    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textinput);

        // Initialize view
        btn_text_clear = findViewById(R.id.activity_textinput_btn_text_clear);
        btn_text_save = findViewById(R.id.activity_textinput_btn_text_save);
        txt_text_capture = findViewById(R.id.activity_textinput_txt_text_capture);
        btn_text_clear.setOnClickListener(this);
        btn_text_save.setOnClickListener(this);

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
            case R.id.activity_textinput_btn_text_save:

                //(1) Validate group field
                if (validateTextInput())
                {
                    if(new DatabaseText(this).createTextEntry(dataModel.getlChild(),txt_text_capture.getText().toString().trim()))
                    {
                        Toast toast = Toast.makeText(this, getString(R.string.text_saved_success), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        Log.e(TAG, "onClick(View view) | case R.id.activity_textinput_btn_text_save createTextEntry() | " + "false");
                        Toast toast = Toast.makeText(this, getString(R.string.text_saved_fail), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    Intent intent = new Intent(this, ActivityCreateAndAdd.class);
                    intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                    startActivity(intent);
                }
                else
                {
                    if(AppSettings.APP_DEBUG_MODE)
                        Log.d(TAG, "onClick(View view)  | R.id.activity_textinput_btn_text_save  validateTextInput() | " + "false");
                    break;
                }
                break;
            case R.id.activity_textinput_btn_text_clear:
                this.txt_text_capture.setText("");
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

    private boolean validateTextInput()
    {
        if (txt_text_capture.getText().toString().trim().equalsIgnoreCase("")) {
            txt_text_capture.setError(getString(R.string.text_cannotbeblank));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent a = new Intent(this, ActivityCreateAndAdd.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(a);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}