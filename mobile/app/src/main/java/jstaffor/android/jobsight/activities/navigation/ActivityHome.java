package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.popups.PopupHome_Add;
import jstaffor.android.jobsight.activities.popups.PopupHome_Create;
import jstaffor.android.jobsight.activities.popups.PopupHome_Download;
import jstaffor.android.jobsight.activities.popups.PopupHome_Manage;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;

//***TODO*** - Figure out what i have to do to use gson apache in this app, ie: licensing

public class ActivityHome extends Activity implements View.OnClickListener,
        PopupHome_Create.InterfacePopupCreateParentChild,
        PopupHome_Add.InterfacePopupOpenParentChild,
        PopupHome_Manage.InterfacePopupHome_Manage
{
    private static final String TAG = "ActivityHome";
    private Button btn_job_create, btn_job_open, btn_job_view, btn_job_invoice, btn_job_download, btn_job_manage;
    private DatabaseAccess databaseAccess;
    private int whichButtonWasClicked = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize App settings
        initializeAppSettings();

        // Initialize view
        btn_job_create = findViewById(R.id.activity_home_btn_job_new);
        btn_job_open = findViewById(R.id.activity_home_btn_job_open);
        btn_job_view = findViewById(R.id.activity_home_btn_job_view);
        btn_job_download = findViewById(R.id.activity_home_btn_job_download);
        btn_job_invoice = findViewById(R.id.activity_home_btn_job_invoice);
        btn_job_manage = findViewById(R.id.activity_home_btn_job_manage);

        btn_job_create.setOnClickListener(this);
        btn_job_open.setOnClickListener(this);
        btn_job_view.setOnClickListener(this);
        btn_job_download.setOnClickListener(this);
        btn_job_invoice.setOnClickListener(this);
        btn_job_manage.setOnClickListener(this);

        databaseAccess = new DatabaseAccess(this);
        databaseAccess.initializeDatamodelUsingDatabaseData(DataModel.USER_GUID);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.activity_home_btn_job_new:
                    whichButtonWasClicked = R.id.activity_home_btn_job_new;
                    final DialogFragment dialogFragmentPopupCreateParentChild = new PopupHome_Create();
                    final FragmentTransaction ftPopupCreateParentChild = getFragmentManager().beginTransaction();
                    final Fragment prevPopupCreateParentChild = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupCreateParentChild != null) {
                        ftPopupCreateParentChild.remove(prevPopupCreateParentChild);
                    }

                    ftPopupCreateParentChild.addToBackStack(null);
                    dialogFragmentPopupCreateParentChild.show(ftPopupCreateParentChild, "dialog");
                    break;
                case R.id.activity_home_btn_job_open:
                    whichButtonWasClicked = R.id.activity_home_btn_job_open;
                    final DialogFragment dialogFragmentPopupOpenParentChild = new PopupHome_Add();
                    final FragmentTransaction ftPopupOpenParentChild = getFragmentManager().beginTransaction();
                    final Fragment prevPopupOpenParentChild = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupOpenParentChild != null) {
                        ftPopupOpenParentChild.remove(prevPopupOpenParentChild);
                    }

                    ftPopupOpenParentChild.addToBackStack(null);
                    dialogFragmentPopupOpenParentChild.show(ftPopupOpenParentChild, "dialog");
                    break;
                case R.id.activity_home_btn_job_view:
                    whichButtonWasClicked = R.id.activity_home_btn_job_view;
                    final DialogFragment dialogFragmentPopupViewParentChild = new PopupHome_Add();
                    final FragmentTransaction ftPopupViewParentChild = getFragmentManager().beginTransaction();
                    final Fragment prevPopupViewParentChild = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupViewParentChild != null) {
                        ftPopupViewParentChild.remove(prevPopupViewParentChild);
                    }

                    ftPopupViewParentChild.addToBackStack(null);
                    dialogFragmentPopupViewParentChild.show(ftPopupViewParentChild, "dialog");
                    break;
                case R.id.activity_home_btn_job_download:
                    whichButtonWasClicked = R.id.activity_home_btn_job_download;
                    final PopupHome_Download dialogFragmentPopupHome_Download = new PopupHome_Download();
                    final FragmentTransaction ftPopupHome_Download = getFragmentManager().beginTransaction();
                    final Fragment prevPopupHome_Download = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupHome_Download != null) {
                        ftPopupHome_Download.remove(prevPopupHome_Download);
                    }

                    ftPopupHome_Download.addToBackStack(null);
                    dialogFragmentPopupHome_Download.show(ftPopupHome_Download, "dialog");
                    break;
                case R.id.activity_home_btn_job_invoice:
                    whichButtonWasClicked = R.id.activity_home_btn_job_invoice;
                    final PopupHome_Add dialogFragmentPopupHome_Invoice = new PopupHome_Add();  //***NOTE: Reusing PopupHome_Add***
                    final FragmentTransaction ftPopupHome_Invoice = getFragmentManager().beginTransaction();
                    final Fragment prevPopupHome_Invoice = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupHome_Invoice != null) {
                        ftPopupHome_Invoice.remove(prevPopupHome_Invoice);
                    }

                    ftPopupHome_Invoice.addToBackStack(null);
                    dialogFragmentPopupHome_Invoice.show(ftPopupHome_Invoice, "dialog");
                    break;
                case R.id.activity_home_btn_job_manage:
                    whichButtonWasClicked = R.id.activity_home_btn_job_manage;
                    final PopupHome_Manage dialogFragmentPopupHomeManage = new PopupHome_Manage();
                    final FragmentTransaction ftPopupManageParentChild = getFragmentManager().beginTransaction();
                    final Fragment prevPopupManageParentChild = getFragmentManager().findFragmentByTag("dialog");

                    if (prevPopupManageParentChild != null) {
                        ftPopupManageParentChild.remove(prevPopupManageParentChild);
                    }

                    ftPopupManageParentChild.addToBackStack(null);
                    dialogFragmentPopupHomeManage.show(ftPopupManageParentChild, "dialog");
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
    public void decideOnActionToTake(DataModel dataModel)
    {
        //Clear memory - the content for this could be quite big
        dataModel.getMapParentID_mapChildNameChildID().clear();
        dataModel.getMapParentName_ParentID().clear();
        dataModel.getMapTempName_TempSetting().clear();

        Intent intent;

        switch(whichButtonWasClicked)
        {
            case R.id.activity_home_btn_job_new:
                intent = new Intent(this, ActivityCreateAndAdd.class);
                intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intent);
                break;
            case R.id.activity_home_btn_job_open:
                intent = new Intent(this, ActivityCreateAndAdd.class);
                intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intent);
                break;
            case R.id.activity_home_btn_job_view:
                intent = new Intent(this, ActivityView.class);
                intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intent);
                break;
            case R.id.activity_home_btn_job_download:
                intent = new Intent(this, ActivityDownload.class);
                intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intent);
                break;
            case R.id.activity_home_btn_job_invoice:
                intent = new Intent(this, ActivityInvoice.class);
                intent.putExtra(DataModelUtilities.DATA_MODEL, DataModelUtilities.turnDataModelIntoJSON(dataModel));
                startActivity(intent);
                break;
            case R.id.activity_home_btn_job_manage:
                intent = new Intent(this, ActivityManage.class);

                if(dataModel.getsParent().equals(DataModel.PARENT))
                    intent.putExtra(DataModel.PARENT, DataModel.PARENT);
                else if(dataModel.getsChild().equals(DataModel.CHILD))
                    intent.putExtra(DataModel.CHILD, DataModel.CHILD);
                else if(dataModel.getsTemplate().equals(DataModel.TEMPLATE))
                    intent.putExtra(DataModel.TEMPLATE, DataModel.TEMPLATE);
                else
                {
                    Log.e(TAG, "decideOnActionToTake(DataModel dataModel) - unknown selection, it must be DataModel.PARENT, DataModel.CHILD or DataModel.TEMPLATE");
                    throw new IllegalArgumentException();
                }

                startActivity(intent);
                break;
            default:
                Log.e(TAG, "decideOnActionToTake(DataModel dataModel) - unknown selection");
                throw new IllegalArgumentException();
        }
    }

    private void initializeAppSettings()
    {
        AppSettings.DEBUG_MODE = Boolean.parseBoolean( getString(R.string.debug_mode) );
    }
}