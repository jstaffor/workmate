package jstaffor.android.jobsight.activities.popups;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityHome;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.popups.DatabaseAccessPopupHome_Add;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.utilities.Validation;

public class PopupHome_Download extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "PopupHome_Download";
    private Spinner spn_parent_list, spn_child_list;
    private Button btn_child_open;
    private DataModel dataModel;

    private DatabaseAccessPopupHome_Add databaseAccess;
    private Validation validation;

    // Empty constructor required for DialogFragment
    public PopupHome_Download() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewPopupHome_Add = inflater.inflate(R.layout.popup_add, container);   //popup_add used for (1)PopupHome_Add and (2)PopupHome_Download

        // Initialize view
        spn_parent_list = viewPopupHome_Add.findViewById(R.id.popup_add_spn_parent_list);
        spn_child_list = viewPopupHome_Add.findViewById(R.id.popup_add_spn_child_list);
        btn_child_open = viewPopupHome_Add.findViewById(R.id.popup_add_btn_child_open);

        //Populate data from database
        databaseAccess = new DatabaseAccessPopupHome_Add(getContext());
        dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);

        validation = new Validation(getContext());

        //If no child exists - don't allow user to proceed
        if(dataModel.getMapParentID_mapChildNameChildID().values().size() != 0)
        {
            btn_child_open.setOnClickListener(this);

            if(AppSettings.DEBUG_MODE)
                Log.d(TAG, "onCreate(Bundle savedInstanceState) | dataModel.getMapParentID_mapChildNameChildID().values().size() != 0 | " + "true");
        }
        else {
            btn_child_open.setEnabled(false);

            if (AppSettings.DEBUG_MODE)
                Log.d(TAG, "onCreate(Bundle savedInstanceState) | dataModel.getMapParentID_mapChildNameChildID().values().size() != 0 | " + "false");
        }

        //Apply adapters
        setOnLoadAndOnItemSelectedAdapters();

        return viewPopupHome_Add;
    }

    @Override
    public void onClick(View view) {
        try
        {
            switch (view.getId()) {
                case R.id.popup_add_btn_child_open:

                    dataModel.setsParent(Validation.returnUIStringForSaving(spn_parent_list.getSelectedItem().toString()));
                    dataModel.setlParent(validation.getParentIDForParentName(dataModel.getsParent()));

                    dataModel.setsChild(Validation.returnUIStringForSaving(spn_child_list.getSelectedItem().toString()));
                    dataModel.setlChild(validation.getChildIDForParentID(dataModel.getlParent(), dataModel.getsChild()));

                    dataModel.setlTemplate(databaseAccess.getTemplateSettingForChild(dataModel.getlChild()));

                    if (AppSettings.DEBUG_MODE)
                    {
                        Log.d(TAG, "onClick(View view) | dataModel.getsParent() | " +dataModel.getsParent());
                        Log.d(TAG, "onClick(View view) | dataModel.getlParent() | " +dataModel.getlParent());
                        Log.d(TAG, "onClick(View view) | dataModel.getsChild() | " +dataModel.getsChild());
                        Log.d(TAG, "onClick(View view) | dataModel.getlChild() | " +dataModel.getlChild());
                        Log.d(TAG, "onClick(View view) | dataModel.getlTemplate() | " +dataModel.getlTemplate());
                        Log.d(TAG, "onClick(View view) | ((ActivityHome) getActivity()).decideOnActionToTake(dataModel)| " );
                    }

                    ((ActivityHome) getActivity()).decideOnActionToTake(dataModel);

                    this.dismiss();
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

    private void setOnLoadAndOnItemSelectedAdapters()
    {
        //**************
        //onload Adapter
        //**************
        final ArrayAdapter<String> adapterParent = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(dataModel.getMapParentName_ParentID().keySet()));
        spn_parent_list.setAdapter(adapterParent);

        if (AppSettings.DEBUG_MODE) {
            for(String curParent : dataModel.getMapParentName_ParentID().keySet())
                Log.d(TAG, "setOnLoadAndOnItemSelectedAdapters() | dataModel.getMapParentName_ParentID().keySet() | " + curParent);
        }

        //*****************************
        //OnItemSelectedAdapter Adapter
        //*****************************
        //Apply adapters
        if(dataModel.getMapParentID_mapChildNameChildID().keySet().size() != 0)
        {
            spn_parent_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    //Get the Parent ID
                    dataModel.setlParent(validation.getParentIDForParentName(spn_parent_list.getSelectedItem().toString()));

                    // If the Parent ID HAS a list, use this to populate
                    if(dataModel.getMapParentID_mapChildNameChildID().containsKey(dataModel.getlParent()))
                    {
                        if (AppSettings.DEBUG_MODE) {
                            for(String curParent : dataModel.getMapParentName_ParentID().keySet())
                                Log.d(TAG, "onItemSelected(AdapterView<?> parent, View view, int position, long id) |  if(dataModel.getMapParentID_mapChildNameChildID().containsKey(dataModel.getlParent())) | " +curParent);
                        }

                        spn_child_list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(
                                dataModel.getMapParentID_mapChildNameChildID().get(dataModel.getlParent()).keySet()  )));

                        dataModel.setlChild(validation.getChildIDForParentID( validation.getParentIDForParentName(spn_parent_list.getSelectedItem().toString()), spn_child_list.getSelectedItem().toString()) );
                    }
                    else
                    {
                        if (AppSettings.DEBUG_MODE)
                            Log.d(TAG, "onItemSelected(AdapterView<?> parent, View view, int position, long id) |  if(dataModel.getMapParentID_mapChildNameChildID().containsKey(dataModel.getlParent())) | " + getString(R.string.jobname_nojobsexist));

                        String error[] = {getContext().getString(R.string.jobname_nojobsexist)};
                        spn_child_list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, error ));

                        dataModel.setlChild(DataModel.DEFAULT_LONG_VALUE); //Default not set value (see constructor for DataModel)
                    }

                    if(dataModel.getlChild()!= DataModel.DEFAULT_LONG_VALUE)
                    {
                        btn_child_open.setEnabled(true);
                        if (AppSettings.DEBUG_MODE)
                            Log.d(TAG, "onItemSelected(AdapterView<?> parent, View view, int position, long id) | if(dataModel.getlChild()!= DataModel.DEFAULT_LONG_VALUE) | " + " btn_child_open.setEnabled(true)");

                    }
                    else {
                        btn_child_open.setEnabled(false);
                        if (AppSettings.DEBUG_MODE)
                            Log.d(TAG, "onItemSelected(AdapterView<?> parent, View view, int position, long id) |  if(dataModel.getlChild()!= DataModel.DEFAULT_LONG_VALUE) | " + "btn_child_open.setEnabled(false)");

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        else
        {
            if (AppSettings.DEBUG_MODE)
                Log.d(TAG, "setOnLoadAndOnItemSelectedAdapters() | if(dataModel.getMapParentID_mapChildNameChildID().keySet().size() != 0) | " + getString(R.string.jobname_nojobsexist));

            String error[] = {getContext().getString(R.string.jobname_nojobsexist)};
            spn_child_list.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, error ));
        }
    }

    public interface InterfacePopupOpenParentChild {
        void decideOnActionToTake(DataModel dataModel);
    }
}