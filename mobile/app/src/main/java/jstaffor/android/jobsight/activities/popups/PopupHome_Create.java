package jstaffor.android.jobsight.activities.popups;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import jstaffor.android.jobsight.activities.navigation.ActivityHome;
import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.popups.DatabaseAccessPopupHome_Create;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.utilities.Validation;

public class PopupHome_Create extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "PopupHome_Create";
    private Spinner spn_template_list;
    private AutoCompleteTextView autocom_parent_list;
    private EditText txt_child;
    private Button btn_child_create;

    private DataModel dataModel;

    private DatabaseAccessPopupHome_Create databaseAccess;
    private Validation validation;

    // Empty constructor required for DialogFragment
    public PopupHome_Create() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewPopupHome_Create = inflater.inflate(R.layout.popup_create, container);

        // Initialize view
        autocom_parent_list = viewPopupHome_Create.findViewById(R.id.popup_create_autocom_parent_list);
        autocom_parent_list.setText( DataModel.DEFAULT_VALUE_COLUMN_PARENT_NAME);
        txt_child = viewPopupHome_Create.findViewById(R.id.popup_create_txt_child);
        spn_template_list = viewPopupHome_Create.findViewById(R.id.popup_create_spn_template_list);
        btn_child_create = viewPopupHome_Create.findViewById(R.id.popup_create_btn_child_create);
        btn_child_create.setOnClickListener(this);
        autocom_parent_list.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    autocom_parent_list.setText("");
                    return false;
            }
        });
        //Populate data from database
        databaseAccess = new DatabaseAccessPopupHome_Create(getContext());
        dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);

        //Popluate screen with data from database
        createArrayAdapterParentList();
        createArrayAdapterTemplateList();

        return viewPopupHome_Create;
    }

    @Override
    public void onClick(View view)
    {
        try {
            validation = new Validation(getContext());

            switch (view.getId()) {
                case R.id.popup_create_btn_child_create:

                    final String sParent = Validation.returnUIStringForSaving(autocom_parent_list.getText().toString());
                    final String sChild = Validation.returnUIStringForSaving(txt_child.getText().toString());
                    final String sTemplate = Validation.returnUIStringForSaving(spn_template_list.getSelectedItem().toString());
                    if (sChild.equals("")) {
                        txt_child.setError(getContext().getString(R.string.cannot_be_blank));
                        return;
                    }

                    //************************************
                    // (1) Validate Template
                    //************************************
                    if (DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME.equals(sTemplate)) {
                        dataModel.setsTemplate(DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME);
                        dataModel.setlTemplate(DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING);
                    } else {
                        // setting for 'sTemplate' of DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING if not found
                        dataModel.setlTemplate(validation.doesTemplateCurrentlyExist(sTemplate));
                        dataModel.setsTemplate(sTemplate);
                    }

                    //************************************
                    // (2) Validate Parent
                    //************************************
                    boolean doesParentAlreadyExist = false;
                    if (sParent.equals("")) //Set default values
                    {
                        dataModel.setlParent(DataModel.DEFAULT_VALUE_COLUMN_PARENT_ID);
                        dataModel.setsParent(DataModel.DEFAULT_VALUE_COLUMN_PARENT_NAME);
                        doesParentAlreadyExist = true;
                    } else {
                        final Long lParent = validation.getParentIDForParentName(sParent);
                        dataModel.setlParent(lParent);
                        dataModel.setsParent(sParent);

                        if (lParent == -1L) //it doesn't exist
                            doesParentAlreadyExist = false;
                        else
                            doesParentAlreadyExist = true;
                    }

                    //************************************
                    // (3) Validate Child
                    //************************************
                    if (doesParentAlreadyExist)   //Only check if parent already exists
                    {
                        final Long lChild = validation.getChildIDForParentID(dataModel.getlParent(), sChild);

                        if (lChild == -1L)   //Continue as the child for this parent does not exist
                        {
                            dataModel.setsChild(sChild);
                        } else {
                            txt_child.setError(getContext().getString(R.string.job_already_exists));
                            return;
                        }
                    } else {
                        dataModel.setsChild(sChild);
                    }

                    if (AppSettings.APP_DEBUG_MODE) {
                        Log.d(TAG, "**********BEFORE SAVE TO DATABASE********");
                        Log.d(TAG, "onClick(View view) | dataModel.getsParent() | " + dataModel.getsParent());
                        Log.d(TAG, "onClick(View view) | dataModel.getlParent() | " + dataModel.getlParent());
                        Log.d(TAG, "onClick(View view) | dataModel.getsChild() | " + dataModel.getsChild());
                        Log.d(TAG, "onClick(View view) | dataModel.getlChild() | " + dataModel.getlChild());
                        Log.d(TAG, "onClick(View view) | dataModel.getsTemplate() | " + dataModel.getsTemplate());
                        Log.d(TAG, "onClick(View view) | dataModel.getlTemplate() | " + dataModel.getlTemplate());
                        Log.d(TAG, "**********BEFORE SAVE TO DATABASE********");
                    }

                    //************************************
                    // (4) Start saving to database
                    //************************************
                    if (doesParentAlreadyExist == false) //we have everything about the Parent apart from its ID
                    {
                        final Long newLParent = databaseAccess.createParentInDatabase(sParent, DataModel.USER_GUID);

                        if (newLParent != -1l)  // -1 == @return the row ID of the newly inserted row, or -1 if an error occurred
                        {
                            dataModel.setlParent(newLParent);
                            final Long newLChild = databaseAccess.saveParentAndChildAndTemplateSettingToDatabase(newLParent, dataModel.getsChild(), dataModel.getlTemplate());

                            if (newLChild != -1)   //getChildIDForParentID(Long lInputForParent, String inputForChild) == lChild ID is returned, -1L if not found
                            {
                                dataModel.setlChild(newLChild);
                                final Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_success), Toast.LENGTH_SHORT);
                                ((ActivityHome) getActivity()).decideOnActionToTake(dataModel);
                                this.dismiss();

                            } else {
                                Log.e(TAG, "onClick(View view) if( newLChild == -1 )  | databaseAccess.saveParentAndChildAndTemplateSettingToDatabase(newLParent, dataModel.getsChild(), dataModel.getlTemplate()) | " + " -1");
                                final Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_fail), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            //(6) Finally, dismiss popup
                            Log.e(TAG, "onClick(View view) |  if( newLParent != -1l) | " + "true");
                            Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_fail), Toast.LENGTH_SHORT);
                            toast.show();
                            this.dismiss();
                        }
                    } else {
                        final Long newLChild = databaseAccess.saveParentAndChildAndTemplateSettingToDatabase(dataModel.getlParent(), dataModel.getsChild(), dataModel.getlTemplate());

                        if (newLChild != -1)   //saveParentAndChildAndTemplateSettingToDatabase(Long lInputForParent, String inputForChild) == lChild ID is returned, -1L if not found
                        {
                            dataModel.setlChild(newLChild);
                            final Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_success), Toast.LENGTH_SHORT);
                            ((ActivityHome) getActivity()).decideOnActionToTake(dataModel);
                            this.dismiss();

                        } else {
                            Log.e(TAG, "onClick(View view) | newLChild == -1 | " + "true");
                            final Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_fail), Toast.LENGTH_SHORT);
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
            Toast toast = Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void createArrayAdapterTemplateList() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(dataModel.getMapTempName_TempSetting().keySet()));

        if (AppSettings.APP_DEBUG_MODE) {
            for(String curParent : dataModel.getMapParentName_ParentID().keySet())
                Log.d(TAG, "createArrayAdapterTemplateList() |  dataModel.getMapTempName_TempSetting().keySet() | " +dataModel.getMapTempName_TempSetting().keySet());
        }

        spn_template_list.setAdapter(adapter);
    }

    private void createArrayAdapterParentList() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>(dataModel.getMapParentName_ParentID().keySet()));

        if (AppSettings.APP_DEBUG_MODE) {
            for(String curParent : dataModel.getMapParentName_ParentID().keySet())
                Log.d(TAG, "createArrayAdapterParentList() |  dataModel.getMapParentName_ParentID().keySet() | " +dataModel.getMapParentName_ParentID().keySet());
        }

        autocom_parent_list.setAdapter(adapter);
    }

    public interface InterfacePopupCreateParentChild {
        void decideOnActionToTake(DataModel dataModel);
    }
}