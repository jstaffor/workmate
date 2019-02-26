package jstaffor.android.jobsight.activities.popups;

import android.app.DialogFragment;
import android.graphics.Color;
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
import jstaffor.android.jobsight.database.popups.DatabaseAccessPopupManage_Delete;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.utilities.AccessInternalStorage;
import jstaffor.android.jobsight.utilities.Validation;

public class PopupManage_Delete extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "PopupManage_Delete";
    private TextView txt_title;
    private EditText txt_name;
    private ToggleButton add_text, add_photo, add_sketch, add_current_loc, add_audio_recording, add_video_recording, add_file;
    private Spinner spn_template_list;
    private Button btn_save;

    private String sChild_or_Parent_or_Template;

    private DatabaseAccessPopupManage_Delete databaseAccess;
    private AccessInternalStorage accessInternalStorage;
    private Validation validation;

    // Empty constructor required for DialogFragment
    public PopupManage_Delete()
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
        add_file = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_add_file);
        spn_template_list = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_spn_template_list);
        btn_save = viewPopupManageCreate.findViewById(R.id.popup_home_manage_crud_btn_save);
        add_text.setOnClickListener(this);
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

        add_photo.setVisibility(View.GONE);
        add_sketch.setVisibility(View.GONE);
        add_current_loc.setVisibility(View.GONE);
        add_audio_recording.setVisibility(View.GONE);
        add_video_recording.setVisibility(View.GONE);
        add_file.setVisibility(View.GONE);
        spn_template_list.setVisibility(View.GONE);
        btn_save.setEnabled(false);

        // Decide on what to display based on selection from mother screen
        switch(sChild_or_Parent_or_Template)
        {
            case DataModel.PARENT:
                txt_title.setText(getContext().getString(R.string.parent_delete));
                txt_name.setText( ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString() );
                txt_name.setEnabled(false);
                add_text.setText(getContext().getString(R.string.parent_delete_question));
                break;
            case DataModel.CHILD:
                txt_title.setText(getContext().getString(R.string.child_delete));
                txt_name.setText( ((ActivityManage) getActivity()).getSpn_child_list().getSelectedItem().toString() );
                txt_name.setEnabled(false);
                add_text.setText(getContext().getString(R.string.child_delete_question));
                break;
            case DataModel.TEMPLATE:
                txt_title.setText(getContext().getString(R.string.template_delete));
                txt_name.setText( ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString() );
                txt_name.setEnabled(false);
                add_text.setText(getContext().getString(R.string.template_delete_question));
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
                databaseAccess = new DatabaseAccessPopupManage_Delete(getContext());

            if(validation == null)
                validation = new Validation(getContext());

        switch (view.getId())
        {
            case R.id.popup_home_manage_crud_btn_save:
                // Decide on what to save to database based on selection from mother screen
                switch(sChild_or_Parent_or_Template)
                {
                    case DataModel.PARENT:

                        final String sParent = ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString();
                        final Long lParent = ((ActivityManage) getActivity()).getDataModel().getMapParentName_ParentID().get(sParent);

                        if(validateParentInput(sParent))
                        {
                            // 0 == sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.
                            if (databaseAccess.deleteParentFromDatabase(lParent, DataModel.USER_GUID) != 0)
                            {
                                try
                                {
                                    accessInternalStorage = new AccessInternalStorage( getContext() );
                                    final String dirForDeletion = accessInternalStorage.doesDirectoryForParentExist(lParent);

                                    if( dirForDeletion == null )
                                    {
                                        Toast toast = Toast.makeText(getContext(), getString(R.string.parent_delete_success), Toast.LENGTH_SHORT);
                                        toast.show();
                                        ((ActivityManage) getActivity()).decideOnActionToTake();
                                    }
                                    else
                                    {
                                        if( accessInternalStorage.deleteFileOrDir( accessInternalStorage.doesDirectoryForParentExist(lParent) ) )
                                        {
                                            Toast toast = Toast.makeText(getContext(), getString(R.string.parent_delete_success), Toast.LENGTH_SHORT);
                                            toast.show();
                                            ((ActivityManage) getActivity()).decideOnActionToTake();
                                        }
                                        else
                                        {
                                            Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.PARENT - accessInternalStorage.deleteFileOrDir( accessInternalStorage.doesDirectoryForParentExist(lParent)) | " + "false");
                                            Toast toast = Toast.makeText(getContext(), getString(R.string.parent_delete_fail), Toast.LENGTH_SHORT);
                                            toast.show();
                                        }

                                    }
                                }
                                catch (Exception exception)
                                {
                                    Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.PARENT - catch (Exception exception) | " + exception.getMessage());
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.parent_delete_fail), Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            } else {
                                Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.PARENT - databaseAccess.deleteParentFromDatabase(lParent, DataModel.USER_GUID) == 0 | " + "true");
                                Toast toast = Toast.makeText(getContext(), getString(R.string.parent_delete_fail), Toast.LENGTH_SHORT);
                                toast.show();
                            }

                            this.dismiss();
                        }
                        break;
                    case DataModel.CHILD:

                        final String sParent_ = ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString();
                        final Long lParent_ = validation.getParentIDForParentName(sParent_);    //@return lChild ID is returned, -1L if not found

                        final String sChild_ = ((ActivityManage) getActivity()).getSpn_child_list().getSelectedItem().toString();
                        final Long lChild_ = validation.getChildIDForParentID(lParent_, sChild_);   //@return lChild ID is returned, -1L if not found

                        if(lParent_ != -1L && lChild_ != -1L)
                        {
                            // 0 == sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.
                            if (databaseAccess.deleteChildFromDatabase(lParent_, lChild_) != 0)
                            {
                                try
                                {
                                    accessInternalStorage = new AccessInternalStorage( getContext() );
                                    final String dirForDeletion = accessInternalStorage.doesDirectoryForChildExist(lParent_, lChild_);

                                    if( dirForDeletion == null )
                                    {
                                        Toast toast = Toast.makeText(getContext(), getString(R.string.child_delete_success), Toast.LENGTH_SHORT);
                                        toast.show();
                                        ((ActivityManage) getActivity()).decideOnActionToTake();
                                    }
                                    else
                                    {
                                        if(accessInternalStorage.deleteFileOrDir( dirForDeletion ))
                                        {
                                            Toast toast = Toast.makeText(getContext(), getString(R.string.child_delete_success), Toast.LENGTH_SHORT);
                                            toast.show();
                                            ((ActivityManage) getActivity()).decideOnActionToTake();
                                        }
                                        else
                                        {
                                            Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.CHILD - accessInternalStorage.deleteFileOrDir( dirForDeletion ) | " + "false");
                                            Toast toast = Toast.makeText(getContext(), getString(R.string.child_delete_fail), Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }
                                }
                                catch (Exception exception)
                                {
                                    Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.CHILD - catch (Exception exception) | " + exception.getMessage());
                                    Toast toast = Toast.makeText(getContext(), getString(R.string.child_delete_fail), Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            }
                            else {
                                Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.CHILD - databaseAccess.deleteChildFromDatabase(lParent_, lChild_) == 0 | " + "true");
                                Toast toast = Toast.makeText(getContext(), getString(R.string.child_delete_fail), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                        else {
                            Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.CHILD - lParent_ != -1L && lChild_ != -1L | " + "false");
                            Toast toast = Toast.makeText(getContext(), getString(R.string.child_delete_fail), Toast.LENGTH_SHORT);
                            toast.show();
                        }

                        this.dismiss();

                        break;
                    case DataModel.TEMPLATE:

                        final String sTemplate = ((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString();
                        final Long lTemplate = databaseAccess.getTemplateIDForTemplateNameAndUserID(sTemplate, DataModel.USER_GUID);

                        if(validateTemplateInput(sTemplate))
                        {
                            // 0 == sqliteDatabase.delete() - the number of rows affected if a whereClause is passed in, 0 otherwise.
                            if (databaseAccess.deleteTemplateFromDatabase(lTemplate, DataModel.USER_GUID) != 0)
                            {
                                Toast toast = Toast.makeText(getContext(), getString(R.string.template_delete_success), Toast.LENGTH_SHORT);
                                toast.show();
                                ((ActivityManage) getActivity()).decideOnActionToTake();
                            } else {
                                Log.e(TAG, "onClick(View view) | R.id.popup_home_manage_crud_btn_save - case DataModel.TEMPLATE - databaseAccess.deleteTemplateFromDatabase(lTemplate, DataModel.USER_GUID) == 0 | " + "true");
                                Toast toast = Toast.makeText(getContext(), getString(R.string.template_delete_fail), Toast.LENGTH_SHORT);
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
            case R.id.popup_home_manage_crud_add_text:

                // Set selection from mother screen
                switch(((ActivityManage) getActivity()).getsChildorParentorTemplate())
                {
                    case DataModel.PARENT:
                        if(add_text.isChecked()) {
                            add_text.setOnClickListener(null);
                            add_text.setEnabled(false);
                            add_text.setTextColor(Color.RED);
                            btn_save.setEnabled(true);
                            add_text.setText(getContext().getString(R.string.delete_question_onclick));
                        }
                        break;
                    case DataModel.CHILD:
                        if(add_text.isChecked()) {
                            add_text.setOnClickListener(null);
                            add_text.setEnabled(false);
                            add_text.setTextColor(Color.RED);
                            btn_save.setEnabled(true);
                            add_text.setText(getContext().getString(R.string.delete_question_onclick));
                        }
                        break;
                    case DataModel.TEMPLATE:
                        if(add_text.isChecked()) {
                            add_text.setOnClickListener(null);
                            add_text.setEnabled(false);
                            add_text.setTextColor(Color.RED);
                            btn_save.setEnabled(true);
                            add_text.setText(getContext().getString(R.string.delete_question_onclick));
                        }
                        break;
                    default:
                        Log.e(TAG, "switch(((ActivityManage) getActivity()).getsChildorParentorTemplate()) - must be one of : PARENT, CHILD, TEMPLATE");
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
            Toast toast = Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean validateParentInput(String tempString)
    {
        if(tempString.equals(DataModel.DEFAULT_VALUE_COLUMN_PARENT_NAME))
        {
            txt_name.setError(getContext().getString(R.string.cannot_delete_default_value) + " - (" + DataModel.DEFAULT_VALUE_COLUMN_PARENT_NAME + ")");
            return false;
        }

        return true;
    }

    private boolean validateTemplateInput(String tempString)
    {
        if(((ActivityManage) getActivity()).getSpn_parent_list().getSelectedItem().toString().equals(DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME))
        {
            txt_name.setError(getContext().getString(R.string.cannot_delete_default_value) + " - (" + DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME + ")");
            return false;
        }

        return true;
    }

    public interface InterfacePopupManage_Delete{
        void decideOnActionToTake();
    }
}