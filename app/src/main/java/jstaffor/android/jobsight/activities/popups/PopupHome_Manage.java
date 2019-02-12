package jstaffor.android.jobsight.activities.popups;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityHome;
import jstaffor.android.jobsight.datamodel.DataModel;

public class PopupHome_Manage extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "PopupHome_Manage";
    //private Spinner spn_parent_list, spn_child_list, spn_template_list;
    private Button btn_parent_open, btn_child_open, btn_template_open, btn_customer_open;

    // Empty constructor required for DialogFragment
    public PopupHome_Manage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewPopupHome_Manage = inflater.inflate(R.layout.popup_manage, container);

        btn_parent_open = viewPopupHome_Manage.findViewById(R.id.popup_manage_btn_parent_open);
        btn_child_open = viewPopupHome_Manage.findViewById(R.id.popup_manage_btn_child_open);
        btn_template_open = viewPopupHome_Manage.findViewById(R.id.popup_manage_btn_template_open);
        btn_customer_open = viewPopupHome_Manage.findViewById(R.id.popup_manage_btn_customer_open);
        btn_parent_open.setOnClickListener(this);
        btn_child_open.setOnClickListener(this);
        btn_template_open.setOnClickListener(this);
        btn_customer_open.setOnClickListener(this);

        return viewPopupHome_Manage;
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            final DataModel dataModel = new DataModel();

            switch (view.getId()){
                case R.id.popup_manage_btn_parent_open:
                    dataModel.setsParent(DataModel.PARENT); //Refer to 'public interface InterfacePopupHome_Manage'
                    ((ActivityHome) getActivity()).decideOnActionToTake( dataModel );
                    this.dismiss();
                    break;
                case R.id.popup_manage_btn_child_open:
                    dataModel.setsChild(DataModel.CHILD);   //Refer to 'public interface InterfacePopupHome_Manage'
                    ((ActivityHome) getActivity()).decideOnActionToTake( dataModel );
                    this.dismiss();
                    break;
                case R.id.popup_manage_btn_template_open:
                    dataModel.setsTemplate(DataModel.TEMPLATE); //Refer to 'public interface InterfacePopupHome_Manage'
                    ((ActivityHome) getActivity()).decideOnActionToTake( dataModel );
                    this.dismiss();
                    break;
                case R.id.popup_manage_btn_customer_open:
                    dataModel.setsTemplate(DataModel.TEMPLATE); //Refer to 'public interface InterfacePopupHome_Manage'
                    ((ActivityHome) getActivity()).decideOnActionToTake( dataModel );
                    this.dismiss();
                    break;
                default:
                    throw new IllegalArgumentException("PopupHome_Manage.onClick(View view) - unknown selection");
            }
        }
        catch (Exception exception)
        {
            Toast toast = Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public interface InterfacePopupHome_Manage {
        /**
         * Options to input:
         *          *     setsChild(DataModel.CHILD) | DataModel.CHILD = DatabaseModel.CHILD.TABLE_NAME;
         *          *     setsParent(DataModel.PARENT) | DataModel.PARENT = DatabaseModel.PARENT.TABLE_NAME;
         *          *     setsTemplate(DataModel.TEMPLATE) | DataModel.TEMPLATE = DatabaseModel.TEMPLATE.TABLE_NAME;
         *
         * @param dataModel
         */
        void decideOnActionToTake(DataModel dataModel);
    }
}