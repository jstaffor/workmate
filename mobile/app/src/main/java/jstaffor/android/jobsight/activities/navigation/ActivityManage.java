package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.popups.PopupManage_Create;
import jstaffor.android.jobsight.activities.popups.PopupManage_Delete;
import jstaffor.android.jobsight.activities.popups.PopupManage_Update;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.datamodel.DataModel;

public class ActivityManage extends Activity implements View.OnClickListener,
        PopupManage_Create.InterfacePopupManage_Create,
        PopupManage_Update.InterfacePopupManage_Update,
        PopupManage_Delete.InterfacePopupManage_Delete
{
    private static final String TAG = "ActivityManage";

    private TextView managedata_txt;
    private Button btn_create, btn_update, btn_delete;
    private Spinner spn_parent_list;
    private Spinner spn_child_list;

    private DataModel dataModel;
    public DataModel getDataModel() {return dataModel;}
    private DatabaseAccess databaseAccess;
    private Activity activityManageData = this;
    private int whichButtonWasClicked = -1;

    private final ArrayList<String> dynamicChildArraylistDropdownAdapter = new ArrayList<>();
    private final ArrayList<String> dynamicParentArraylistDropdownAdapter = new ArrayList<>();

    private String sChildorParentorTemplate = "-1"; //Accessed by PopupManage_Create
    public Spinner getSpn_child_list() {return spn_child_list;} //Accessed by PopupManage_Create
    public Spinner getSpn_parent_list() {return spn_parent_list;} //Accessed by PopupManage_Create
    public String getsChildorParentorTemplate() {   return sChildorParentorTemplate;    } //Accessed by PopupManage_Create

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        // Initialize view
        this.managedata_txt = findViewById(R.id.activity_manage_txt);
        this.btn_create = findViewById(R.id.activity_manage_btn_create);
        this.btn_update = findViewById(R.id.activity_manage_btn_update);
        this.btn_delete = findViewById(R.id.activity_manage_btn_delete);
        this.spn_parent_list = findViewById(R.id.activity_manage_spn_parent_list);
        this.spn_child_list = findViewById(R.id.activity_manage_spn_child_list);

        btn_create.setOnClickListener(this);
        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

        databaseAccess = new DatabaseAccess( this );
        dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);

        //Get data from database and load screen data
        if(DataModel.PARENT.equals( getIntent().getExtras().get(DataModel.PARENT) ))
        {
            sChildorParentorTemplate = DataModel.PARENT;    //Vital to set now - informs the popup of context (PARENT, CHILD, TEMPLATE)

            managedata_txt.setText( getString(R.string.text_parent) );
            createArrayAdapterParentAndChildList();
        }
        else if(DataModel.CHILD.equals( getIntent().getExtras().get(DataModel.CHILD) ))
        {
            sChildorParentorTemplate = DataModel.CHILD;    //Vital to set now - informs the popup of context (PARENT, CHILD, TEMPLATE)

            managedata_txt.setText( getString(R.string.text_child) );
            createArrayAdapterParentAndChildList();
        }
        else if(DataModel.TEMPLATE.equals( getIntent().getExtras().get(DataModel.TEMPLATE) ))
        {
            sChildorParentorTemplate = DataModel.TEMPLATE;    //Vital to set now - informs the popup of context (PARENT, CHILD, TEMPLATE)

            managedata_txt.setText( getString(R.string.text_template) );
            this.spn_child_list.setVisibility(View.GONE);   //No need for 2 spinners
            createArrayAdapterTemplateList();
        }
        else {
            Log.e(TAG, "onCreate(Bundle savedInstanceState) - unknown selection");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            switch (view.getId()) {
                case R.id.activity_manage_btn_create:
                    whichButtonWasClicked = R.id.activity_manage_btn_create;
                    final DialogFragment dialogFragmentPopupHomeManageCreate = new PopupManage_Create();
                    final FragmentTransaction ftPopupHomeManageCreate = getFragmentManager().beginTransaction();
                    final Fragment prevPopupHomeManageCreate = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupHomeManageCreate != null) {
                        ftPopupHomeManageCreate.remove(prevPopupHomeManageCreate);
                    }

                    ftPopupHomeManageCreate.addToBackStack(null);
                    dialogFragmentPopupHomeManageCreate.show(ftPopupHomeManageCreate, "dialog");
                    break;
                case R.id.activity_manage_btn_update:
                    whichButtonWasClicked = R.id.activity_manage_btn_update;
                    final DialogFragment dialogFragmentPopupHomeManageUpdate = new PopupManage_Update();
                    final FragmentTransaction ftPopupHomeManageUpdate = getFragmentManager().beginTransaction();
                    final Fragment prevPopupHomeManageUpdate = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupHomeManageUpdate != null) {
                        ftPopupHomeManageUpdate.remove(prevPopupHomeManageUpdate);
                    }

                    ftPopupHomeManageUpdate.addToBackStack(null);
                    dialogFragmentPopupHomeManageUpdate.show(ftPopupHomeManageUpdate, "dialog");
                    break;
                case R.id.activity_manage_btn_delete:
                    whichButtonWasClicked = R.id.activity_manage_btn_delete;
                    final DialogFragment dialogFragmentPopupHomeManageDelete = new PopupManage_Delete();
                    final FragmentTransaction ftPopupHomeManageDelete = getFragmentManager().beginTransaction();
                    final Fragment prevPopupHomeManageDelete = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupHomeManageDelete != null) {
                        ftPopupHomeManageDelete.remove(prevPopupHomeManageDelete);
                    }

                    ftPopupHomeManageDelete.addToBackStack(null);
                    dialogFragmentPopupHomeManageDelete.show(ftPopupHomeManageDelete, "dialog");
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
    public void decideOnActionToTake()
    {
        switch(sChildorParentorTemplate)
        {
            case DataModel.PARENT:
                //(1) Regenerate data from database due to save
                dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);
                //(2) Update screen data
                createArrayAdapterParentAndChildList();
                break;
            case DataModel.CHILD:
                //(1) Regenerate data from database due to save
                dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);
                //(2) Update screen data
                createArrayAdapterParentAndChildList();
                break;
            case DataModel.TEMPLATE:
                //(1) Regenerate data from database due to save
                dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);
                //(2) Update screen data
                createArrayAdapterTemplateList();
                break;
            default:
                Log.e(TAG, "decideOnActionToTake() - unknown selection - sChildorParentorTemplate must be one of : PARENT, CHILD, TEMPLATE");
                throw new IllegalArgumentException();
        }
    }

    /**
     * This is a COMPLICATED method - it can, and will, give you nightmares :)
     * There are no two ways around it - generally speaking, it must be this way
     * Why? Due to the dynamic nature of this screen - ie:dynamic updating of data
     * Once the popup 'PopupManage_Create' finishes for Child, there is new data in the database
     * The dropdown adapters need to take this into account once the User returns to the screen
     * The tricky part to all of this is in 'spn_parent_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()...'
     * This dynamically loads in memory and as such, it's tricky code
     */
    private void createArrayAdapterParentAndChildList()
    {
        //(1) Clear dynamic arraylist adapters
        dynamicParentArraylistDropdownAdapter.clear();
        dynamicChildArraylistDropdownAdapter.clear();

        //(2) Load adapter for the parent
        for(String sParent : dataModel.getMapParentName_ParentID().keySet())
        {
            dynamicParentArraylistDropdownAdapter.add(sParent);

            if(AppSettings.APP_DEBUG_MODE)
                Log.d(TAG, "createArrayAdapterParentAndChildList().dynamicParentArraylistDropdownAdapter.add(sParent) | sParent | " + sParent);
        }
        spn_parent_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dynamicParentArraylistDropdownAdapter));

        //(3) Get the selected value from the parent dropdown
        final Long lParent = dataModel.getMapParentName_ParentID().get(spn_parent_list.getSelectedItem().toString());

        //(4) If there are children for the parent value
        if(dataModel.getMapParentID_mapChildNameChildID().keySet() != null && dataModel.getMapParentID_mapChildNameChildID().containsKey( lParent ))
        {
            //(5) Load adapter for the child
            for(String sChild : dataModel.getMapParentID_mapChildNameChildID().get( lParent ).keySet())
            {
                dynamicChildArraylistDropdownAdapter.add(sChild);

                if(AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "createArrayAdapterParentAndChildList().dynamicChildArraylistDropdownAdapter.add(sChild) | sChild | " + sChild);
            }
        }
        else
        {
            dynamicChildArraylistDropdownAdapter.add(getString(R.string.jobname_nojobsexist));

            if(AppSettings.APP_DEBUG_MODE)
                Log.d(TAG, "createArrayAdapterParentAndChildList().dynamicChildArraylistDropdownAdapter.add(sChild) | getString(R.string.jobname_nojobsexist) | " + "No Job exists");
        }
        spn_child_list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dynamicChildArraylistDropdownAdapter ));

        spn_parent_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                //(1) Pre list for populating by clearing it down
                dynamicChildArraylistDropdownAdapter.clear();

                //(2) Get the selected value from the parent dropdown
                final Long lParent = dataModel.getMapParentName_ParentID().get(spn_parent_list.getSelectedItem().toString());

                //(3) If there are children for the parent value
                if(dataModel.getMapParentID_mapChildNameChildID().keySet() != null && dataModel.getMapParentID_mapChildNameChildID().containsKey( lParent ))
                {
                    //(4) Load adapter for the child
                    for(String sChild : dataModel.getMapParentID_mapChildNameChildID().get( lParent ).keySet())
                    {
                        dynamicChildArraylistDropdownAdapter.add(sChild);

                        if(AppSettings.APP_DEBUG_MODE)
                            Log.d(TAG, "spn_parent_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() dynamicChildArraylistDropdownAdapter.add(sChild) | sChild | " + sChild);
                    }
                    //(5) Only disable button for child, where children do not exist for the given group
                    if(sChildorParentorTemplate == DataModel.CHILD)
                    {
                        btn_update.setEnabled(true);
                        btn_delete.setEnabled(true);

                        if(AppSettings.APP_DEBUG_MODE) {
                            Log.d(TAG, "sChildorParentorTemplate == DataModel.CHILD | btn_update.setEnabled(true) | " + " true");
                            Log.d(TAG, "sChildorParentorTemplate == DataModel.CHILD | btn_delete.setEnabled(true) | " + " true");
                        }
                    }
                }
                else
                {
                    //(4) Load adapter for the child
                    dynamicChildArraylistDropdownAdapter.add(getString(R.string.jobname_nojobsexist));

                    if(AppSettings.APP_DEBUG_MODE)
                        Log.d(TAG, "spn_parent_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() dynamicChildArraylistDropdownAdapter.add(sChild) | getString(R.string.jobname_nojobsexist) | " + "No Job exists");

                    //(5) Only disable button for child, where children do not exist for the given group
                    if(sChildorParentorTemplate == DataModel.CHILD)
                    {
                        btn_update.setEnabled(false);
                        btn_delete.setEnabled(false);

                        if(AppSettings.APP_DEBUG_MODE) {
                            Log.d(TAG, "sChildorParentorTemplate == DataModel.CHILD | btn_update.setEnabled(true) | " + " true");
                            Log.d(TAG, "sChildorParentorTemplate == DataModel.CHILD | btn_delete.setEnabled(true) | " + " true");
                        }
                    }
                }

                spn_child_list.setAdapter(new ArrayAdapter<String>(activityManageData, android.R.layout.simple_dropdown_item_1line, dynamicChildArraylistDropdownAdapter ));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Only disable button for child, where children do not exist for the given group
        if(sChildorParentorTemplate == DataModel.CHILD)
            updateButtons();
    }

    private void createArrayAdapterTemplateList()
    {
        if(AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "createArrayAdapterTemplateList() | dataModel.getMapTempName_TempSetting().keySet() | " + dataModel.getMapTempName_TempSetting().keySet());

        spn_parent_list.setAdapter( new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(dataModel.getMapTempName_TempSetting().keySet())) );
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

    private void updateButtons()
    {
        //If no job exists - don't allow user to proceed
        if(dataModel.getMapParentID_mapChildNameChildID().values().size() == 0)
        {
            btn_update.setEnabled(false);
            btn_delete.setEnabled(false);

            if(AppSettings.APP_DEBUG_MODE) {
                Log.d(TAG, "if(dataModel.getMapParentID_mapChildNameChildID().values().size() == 0) | btn_update.setEnabled(false) | " + " false");
                Log.d(TAG, "if(dataModel.getMapParentID_mapChildNameChildID().values().size() == 0) | btn_delete.setEnabled(false) | " + " false");
            }
        }
        else
        {
            btn_update.setEnabled(true);
            btn_delete.setEnabled(true);

            if(AppSettings.APP_DEBUG_MODE) {
                Log.d(TAG, "if(dataModel.getMapParentID_mapChildNameChildID().values().size() == 0) | btn_update.setEnabled(true) | " + " true");
                Log.d(TAG, "if(dataModel.getMapParentID_mapChildNameChildID().values().size() == 0) | btn_delete.setEnabled(true) | " + " true");
            }
        }
    }
}