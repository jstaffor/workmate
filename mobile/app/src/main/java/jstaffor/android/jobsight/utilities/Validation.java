package jstaffor.android.jobsight.utilities;

import android.content.Context;

import java.util.List;
import java.util.Map;

import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.datamodel.DataModel;

public class Validation
{
    private static final String TAG = "Validation";
    private DataModel dataModel;
    private Context context;
    private DatabaseAccess databaseAccess;

    public Validation(Context context)
    {
        if(context == null)
            throw new IllegalArgumentException("Validation(Context context) - context cannot be null");

        this.context = context;
        databaseAccess = new DatabaseAccess(context);
        dataModel = databaseAccess.populateDataModelUsingDatabaseData(DataModel.USER_GUID);
    }

    public static String returnUIStringForSaving(String inputForParent)
    {
        if(inputForParent == null)
            throw new IllegalArgumentException("Validation.Validation(Context context) - input cannot be null");

        return inputForParent.trim().toUpperCase();
    }

    /**
     *
     * @param inputForParent
     * @return lParent ID is returned, -1L if not found
     */
    public Long getParentIDForParentName(String inputForParent)
    {
        final String sInputForParent = returnUIStringForSaving(inputForParent);
        Long lParent = -1L;

        if(dataModel.getMapParentName_ParentID() == null)
            throw new IllegalArgumentException("Validation.doesParentCurrentlyExist(String inputForParent) - getMapParentName_ParentID() cannot be null");

        if( dataModel.getMapParentName_ParentID().size() != 0 && dataModel.getMapParentName_ParentID().containsKey(sInputForParent) )
            lParent = dataModel.getMapParentName_ParentID().get(sInputForParent);

        return lParent;
    }

    /**
     *
     * @param inputForChild
     * @return lChild ID is returned, -1L if not found
     */
    public Long getChildIDForParentID(Long lInputForParent, String inputForChild)
    {
        final String sInputForChild = returnUIStringForSaving(inputForChild);

        Long lChild = -1L;

        if(lInputForParent == null)
            throw new IllegalArgumentException("Validation.getChildIDUsingChildName(Long lInputForParent, String sInputForChild) - inputs cannot be null");

        if(dataModel.getMapParentID_mapChildNameChildID() == null)
            throw new IllegalArgumentException("Validation.getChildIDUsingChildName(Long lInputForParent, String sInputForChild) - getMapParentID_ChildIDList() cannot be null");

        if(dataModel.getMapParentID_mapChildNameChildID().get(lInputForParent) != null )
        {
            for(String currentSChild : dataModel.getMapParentID_mapChildNameChildID().get(lInputForParent).keySet() )
            {
                if(sInputForChild.equals(currentSChild))
                {
                    lChild = dataModel.getMapParentID_mapChildNameChildID().get(lInputForParent).get(sInputForChild);
                }
            }
        }

        return lChild;
    }

    /**
     *
     * @param lInputForParent
     * @param lInputForChild
     * @return sChild is returned, {@code null} if not found
     */
    public String getChildNameForParentID(Long lInputForParent, Long lInputForChild)
    {
        String sChild = null;

        if(lInputForParent == null || lInputForChild == null)
            throw new IllegalArgumentException("Validation.getChildIDUsingChildName(Long lInputForParent, Long lInputForChild) - inputs cannot be null");

        if(dataModel.getMapParentID_mapChildNameChildID() == null)
            throw new IllegalArgumentException("Validation.getChildIDUsingChildName(Long lInputForParent,  Long lInputForChild) - getMapParentID_ChildIDList() cannot be null");

        //(1) If 'inputForParent' contains a list of children
        if(dataModel.getMapParentID_mapChildNameChildID().get(lInputForParent) != null && dataModel.getMapParentID_mapChildNameChildID().get(lInputForParent).values().contains(lInputForChild) )
        {
            for(int currentIndex = 0; currentIndex < dataModel.getMapParentID_mapChildNameChildID().get(lInputForParent).keySet().size(); currentIndex++)
            {
                if(dataModel.getMapParentID_mapChildNameChildID().values().toArray()[currentIndex] == lInputForChild)
                {
                    sChild = (String) dataModel.getMapParentID_mapChildNameChildID().keySet().toArray()[currentIndex];
                }
            }
        }

        return sChild;
    }

    /**
     *
     * @param sTemplate
     * @return setting for 'sTemplate' of DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING if not found
     */
    public Long doesTemplateCurrentlyExist(String sTemplate)
    {
        if(sTemplate == null)
            throw new IllegalArgumentException("Validation.doesTemplateCurrentlyExist(String inputForTemplate) - input cannot be null");

        if(dataModel.getMapTempName_TempSetting() == null)
            throw new IllegalArgumentException("Validation.doesParentCurrentlyExist(String inputForParent) - getMapTempName_TempSetting() cannot be null");

        final String validatedParentString = this.returnUIStringForSaving(sTemplate);

        if( dataModel.getMapTempName_TempSetting().get(validatedParentString) != null)
            return dataModel.getMapTempName_TempSetting().get(validatedParentString);
        else
            return DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING;
    }
}
