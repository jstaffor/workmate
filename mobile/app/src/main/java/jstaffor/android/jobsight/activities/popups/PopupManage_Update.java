package jstaffor.android.jobsight.activities.popups;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityManage;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.popups.DatabaseAccessPopupManage_Update;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.utilities.Validation;

public class PopupManage_Update extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "PopupManage_Update";
    private TextView txt_title;
    private EditText txt_name;
    private ToggleButton add_text, add_photo, add_sketch, add_current_loc, add_audio_recording, add_video_recording, add_file;
    private Spinner spn_template_list;
    private Button btn_save;

    private PopupHome_Manage popupManage_Create;
    private String sChild_or_Parent_or_Template;

    private DatabaseAccessPopupManage_Update databaseAccess;
    private Validation validation;

    // Empty constructor required for DialogFragment
    public PopupManage_Update()
    {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View viewPopupManageCreate = inflater.inflate(R.layout.popup_home_manage_crud, container);

        txt_title = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_txt_title);
        txt_name = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_txt_name);
        add_text = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_text);
        add_photo = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_photo);
        add_sketch = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_sketch);
        add_current_loc = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_current_loc);
        add_audio_recording = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_audio_recording);
        add_video_recording = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_video_recording);
        spn_template_list = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_spn_template_list);
        add_file = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_file);
        btn_save = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_btn_save);
        btn_save.setOnClickListener(this);

        // Set selection from mother screen
        switch(((ActivityManage) getActivity()).getsChildorParentorTemplate())
        {
            case DataModel.PARENT:
                this.sChild_or_Parent_or_Template = DataModel.PARENT;
                break;
            case DataModel.CHILD:
                this.sChild_or_Parent_or_Template = DataModel.CHILD;
                break;
            case DataModel.TEMPLATE:
                this.sChild_or_Parent_or_Template = DataModel.TEMPLATE;
                break;
            default:
                Log.e(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) - ((ActivityManage) getActivity()).getsChildorParentorTemplate() must be one of : PARENT, CHILD, TEMPLATE");
                throw new IllegalArgumentException();
        }

        // Decide on what to display based on selection from mother screen
        switch(sChild_or_Parent_or_Template)
        {
            case DataModel.PARENT:
                txt_title.setText(getContext().getString(R.string.parent_update));
                txt_name.setHint( ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString() );
                add_text.setVisibility(View.GONE);
                add_photo.setVisibility(View.GONE);
                add_sketch.setVisibility(View.GONE);
                add_current_loc.setVisibility(View.GONE);
                add_audio_recording.setVisibility(View.GONE);
                add_video_recording.setVisibility(View.GONE);
                add_file.setVisibility(View.GONE);
                spn_template_list.setVisibility(View.GONE);
                break;
            case DataModel.CHILD:
                txt_title.setText(getContext().getString(R.string.child_update));
                txt_name.setHint( ((ActivityManage) getActivity()).getSpn_child_list().getSelectedItem().toString() );
                add_text.setVisibility(View.GONE);
                add_photo.setVisibility(View.GONE);
                add_sketch.setVisibility(View.GONE);
                add_current_loc.setVisibility(View.GONE);
                add_audio_recording.setVisibility(View.GONE);
                add_video_recording.setVisibility(View.GONE);
                add_file.setVisibility(View.GONE);
                spn_template_list.setVisibility(View.GONE);
                break;
            case DataModel.TEMPLATE:
                txt_title.setText(getContext().getString(R.string.template_update));
                txt_name.setHint( ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString() );
                spn_template_list.setVisibility(View.GONE);
                setToggleButtons();
                break;
            default:
                Log.e(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) - sChild_or_Parent_or_Template must be one of : PARENT, CHILD, TEMPLATE");
                throw new IllegalArgumentException();
        }

        return viewPopupManageCreate;
    }

    @Override
    public void onClick(View view)
    {
        try
        {
            if(databaseAccess == null)
                databaseAccess = new DatabaseAccessPopupManage_Update(getContext());

            if(validation == null)
                validation = new Validation(getContext());

            switch (view.getId())
            {
                case R.id.popup_home_manage_crud_btn_save:
                    // Decide on what to save to database based on selection from mother screen
                    switch(sChild_or_Parent_or_Template)
                    {
                        case DataModel.PARENT:
                            final String tempNewSParent = Validation.returnUIStringForSaving( txt_name.getText().toString() );
                            final String tempExistingSParent = ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString();

                            // final Long lParent = ((ActivityManage) getActivity()).getDataModel().getMapParentName_ParentID().get(tempExistingParent);
                            final Long tempExistingLParent = validation.getParentIDForParentName(tempExistingSParent);

                            if(validateParentInput(tempNewSParent))
                            {
                                if( databaseAccess.updateParentInDatabase(tempExistingLParent , tempNewSParent, DataModel.USER_GUID))
                                {
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.parent_update_success), Toast.LENGTH_SHORT);
                                    toast.show();
                                    ((ActivityManage) getActivity()).decideOnActionToTake();
                                } else {
                                    Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.PARENT - if(validateParentInput(tempNewSParent)) | " + "false");
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.parent_update_fail), Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                this.dismiss();
                            }
                            break;
                        case DataModel.CHILD:
                            final String tempNewSChild = Validation.returnUIStringForSaving( txt_name.getText().toString() );
                            final Long tempExistingSParent_ = ((ActivityManage) getActivity()).getDataModel().getMapParentName_ParentID().get(
                                    ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString() );

                            final String tempExistingSChild = ((ActivityManage) getActivity()).getSpn_child_list().getSelectedItem().toString();
                            final Long tempExistingLChild = validation.getChildIDForParentID(tempExistingSParent_, tempExistingSChild );

                            if(validateChildInput(tempExistingSParent_, tempNewSChild))
                            {
                                if( databaseAccess.updateChildInDatabase(tempExistingSParent_, tempExistingLChild, tempNewSChild))
                                {
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_success), Toast.LENGTH_SHORT);
                                    toast.show();
                                    ((ActivityManage) getActivity()).decideOnActionToTake();
                                } else {
                                    Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.CHILD - if( databaseAccess.updateChildInDatabase(tempExistingSParent_, tempExistingLChild, tempNewSChild)) | " + "false");
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.child_create_fail), Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                this.dismiss();
                            }
                            break;
                        case DataModel.TEMPLATE:
                            final String sNewTemplate = Validation.returnUIStringForSaving( txt_name.getText().toString() );
                            final String sSelectedTemplate = ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString();

                            if(validateTemplateInput(sNewTemplate))
                            {
                                final Long lNewTemplateSetting = DataModelUtilities.getTemplateSettingForTemplateSelection(
                                    add_text.isChecked(),
                                    add_photo.isChecked(),
                                    add_sketch.isChecked(),
                                    add_current_loc.isChecked(),
                                    add_audio_recording.isChecked(),
                                    add_video_recording.isChecked(),
                                    add_file.isChecked()
                                );

                                if( databaseAccess.updateTemplateInDatabase(sSelectedTemplate, lNewTemplateSetting, sNewTemplate, DataModel.USER_GUID))
                                {
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.template_create_success), Toast.LENGTH_SHORT);
                                    toast.show();
                                    ((ActivityManage) getActivity()).decideOnActionToTake();
                                } else {
                                    Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.TEMPLATE - if( databaseAccess.updateTemplateInDatabase(sSelectedTemplate, lNewTemplateSetting, sNewTemplate, DataModel.USER_GUID)) | " + "false");
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.template_create_fail), Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                this.dismiss();
                            }
                            break;
                        default:
                            Log.e(TAG, "switch(sChild_or_Parent_or_Template) - must be one of : PARENT, CHILD, TEMPLATE");
                            throw new IllegalArgumentException();
                    }
                    break;
                default:
                    Log.e(TAG, "onClick(View view) - unknown selection");
                    throw new IllegalArgumentException();
            }
        }
        catch (Exception exception)
        {
            Toast toast = Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean validateChildInput(Long lParent, String sChild)
    {
        if (sChild.equals(""))
        {
            txt_name.setError(getContext().getString(R.string.cannot_be_blank));
            return false;
        }

        if( validation.getChildIDForParentID(lParent, sChild) != -1L)
        {
            txt_name.setError(getContext().getString(R.string.job_already_exists));
            return false;
        }

        return true;
    }

    private boolean validateParentInput(String tempString)
    {
        if(((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString().equals(DataModel.DEFAULT_VALUE_COLUMN_PARENT_NAME)) {
            txt_name.setError(getContext().getString(R.string.cannot_edit_default_value) + " - (" + DataModel.DEFAULT_VALUE_COLUMN_PARENT_NAME + ")");
            return false;
        }

        if (tempString.equals("")) {
            txt_name.setError(getContext().getString(R.string.cannot_be_blank));
            return false;
        }

        if(((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString().equals(tempString)) {
            txt_name.setError(getContext().getString(R.string.value_has_not_changed));
            return false;
        }

        if (validation.getParentIDForParentName(tempString) != -1L) {
            txt_name.setError(getContext().getString(R.string.parent_already_exists));
            return false;
        }

        return true;
    }

    private boolean validateTemplateInput(String tempString)
    {
        if (tempString.equals(""))
        {
            txt_name.setError(getContext().getString(R.string.cannot_be_blank));
            return false;
        }

        if(((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString().equals(tempString)) {
            txt_name.setError(getContext().getString(R.string.value_has_not_changed));
            return false;
        }

        if (((ActivityManage) getActivity()).getDataModel().getMapTempName_TempSetting().containsKey(tempString))
        {
            txt_name.setError(getContext().getString(R.string.template_already_exists));
            return false;
        }

        if(((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString().equals(DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME))
        {
            txt_name.setError(getContext().getString(R.string.cannot_edit_default_value) + " - (" + DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME + ")");
            return false;
        }

        if(!add_text.isChecked() && !add_photo.isChecked() && !add_sketch.isChecked() && !add_current_loc.isChecked() && !add_audio_recording.isChecked() && !add_video_recording.isChecked() && !add_file.isChecked())
        {
            txt_name.setError(getContext().getString(R.string.no_option_selected));
            return false;
        }
        return true;
    }

    private void setToggleButtons()
    {
        final String sSelectedTemplate = ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString();
        Long lSelectedTemplateSetting = DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING;

        if(((ActivityManage) getActivity()).getDataModel().getMapTempName_TempSetting() != null &&
                ((ActivityManage) getActivity()).getDataModel().getMapTempName_TempSetting().containsKey(sSelectedTemplate))
        {
            lSelectedTemplateSetting = ((ActivityManage) getActivity()).getDataModel().getMapTempName_TempSetting().get(sSelectedTemplate);
        }

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_TEXT_INPUT.toString()))
            add_text.setChecked(true);

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_PHOTO_INPUT.toString()))
            add_photo.setChecked(true);

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_SKETCH_INPUT.toString()))
            add_sketch.setChecked(true);

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_CURRENTLOCATION_INPUT.toString()))
            add_current_loc.setChecked(true);

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_AUDIORECORDING_INPUT.toString()))
            add_audio_recording.setChecked(true);

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_VIDEORECORDING_INPUT.toString()))
            add_video_recording.setChecked(true);

        if (lSelectedTemplateSetting.toString().contains(DataModel.SETTING_FILE_INPUT.toString()))
            add_file.setChecked(true);
    }

    public interface InterfacePopupManage_Update {
        void decideOnActionToTake();
    }
}