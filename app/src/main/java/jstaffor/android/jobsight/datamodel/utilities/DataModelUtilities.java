package jstaffor.android.jobsight.datamodel.utilities;

import com.google.gson.Gson;

import jstaffor.android.jobsight.datamodel.DataModel;

public class DataModelUtilities extends DataModel
{
    private static final String TAG = "DataModelUtilities";
    public final static String DATA_MODEL = "dm";

    /**
     * Turn selected options into a Long setting number for saving
     *
     * Return 'DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING' if an error occurred
     *
     * @param SETTEXT
     * @param SETPHOTO
     * @param SETSKETCH
     * @param SETCURRENTLOCATION
     * @param SETAUDIORECORDING
     * @param SETVIDEORECORDING
     * @return returns a long value for selected options or DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING if an error occurred
     */
    public static Long getTemplateSettingForTemplateSelection(Boolean SETTEXT, Boolean SETPHOTO, Boolean SETSKETCH, Boolean SETCURRENTLOCATION, Boolean SETAUDIORECORDING, Boolean SETVIDEORECORDING, Boolean SETFILE)
    {
        String tempValueForTemplate = "";

        if (SETTEXT)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_TEXT_INPUT;

        if (SETPHOTO)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_PHOTO_INPUT;

        if (SETSKETCH)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_SKETCH_INPUT;

        if (SETCURRENTLOCATION)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_CURRENTLOCATION_INPUT;

        if (SETAUDIORECORDING)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_AUDIORECORDING_INPUT;

        if (SETVIDEORECORDING)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_VIDEORECORDING_INPUT;

        if (SETFILE)
            tempValueForTemplate = tempValueForTemplate + DataModel.SETTING_FILE_INPUT;
        try
        {
            return Long.parseLong(tempValueForTemplate);
        }
        catch(NumberFormatException numberFormatException)
        {
            return DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING;
        }
    }

    public static String turnDataModelIntoJSON(final DataModel dataModel)
    {
        return new Gson().toJson(dataModel);
    }
    public static DataModel turnJSONIntoDataModel(final String jsonDataModel)
    {
        return new Gson().fromJson(jsonDataModel, DataModel.class);
    }
}
