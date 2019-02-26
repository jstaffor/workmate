package jstaffor.android.jobsight.datamodel;

import java.util.Hashtable;
import java.util.Map;

import jstaffor.android.jobsight.database.DatabaseModel;


public class DataModel
{
    private static final String TAG = "DataModel";

    public static final Long DEFAULT_LONG_VALUE = -1L;
    public static final String DEFAULT_STRING_VALUE = "-1";

    //*****USER*****
    public static Long USER_GUID = DatabaseModel.USER.DEFAULT_VALUE_COLUMN_USER_ID;

    //*****CHILD*****
    public static final String CHILD = DatabaseModel.CHILD.TABLE_NAME;

    //*****PARENT*****
    public static final String PARENT = DatabaseModel.PARENT.TABLE_NAME;
    public static final String DEFAULT_VALUE_COLUMN_PARENT_NAME = DatabaseModel.PARENT.DEFAULT_VALUE_COLUMN_PARENT_NAME;
    public static final Long DEFAULT_VALUE_COLUMN_PARENT_ID = DatabaseModel.PARENT.DEFAULT_VALUE_COLUMN_PARENT_ID;

    //*****TEMPLATE*****
    public static final String TEMPLATE = DatabaseModel.TEMPLATE.TABLE_NAME;
    public static final String DEFAULT_VALUE_COLUMN_TEMPLATE_NAME = DatabaseModel.TEMPLATE.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME;
    public static final Long DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING = DatabaseModel.TEMPLATE.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING;
    public static final String DEFAULT_VALUE_COLUMN_FILE_NAME = DatabaseModel.FILE.DEFAULT_VALUE_COLUMN_FILE_NAME;
    public static final Long SETTING_TEXT_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_TEXT_INPUT;
    public static final Long SETTING_PHOTO_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_PHOTO_INPUT;
    public static final Long SETTING_SKETCH_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_SKETCH_INPUT;
    public static final Long SETTING_CURRENTLOCATION_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_CURRENTLOCATION_INPUT;
    public static final Long SETTING_AUDIORECORDING_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_AUDIORECORDING_INPUT;
    public static final Long SETTING_VIDEORECORDING_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_VIDEORECORDING_INPUT;
    public static final Long SETTING_FILE_INPUT = DatabaseModel.TEMPLATE.COLUMN_TEMPLATE_SETTING_FILE_INPUT;

    private String sParent, sChild, sTemplate;

    private Long lParent, lChild, lTemplate;

    private Map<String, Long> mapParentName_ParentID;
    private Map<String, Long> mapTempName_TempSetting;
    private Map<Long, Map<String, Long>> mapParentID_mapChildNameChildID;

    public DataModel()
    {
        mapParentName_ParentID = new Hashtable<String, Long>();
        mapTempName_TempSetting = new Hashtable<String, Long>();
        mapParentID_mapChildNameChildID = new Hashtable<Long, Map<String, Long>>();
        lParent = DEFAULT_LONG_VALUE;
        lChild = DEFAULT_LONG_VALUE;
        lTemplate = DEFAULT_LONG_VALUE;
        sParent = DEFAULT_STRING_VALUE;
        sChild = DEFAULT_STRING_VALUE;
        sTemplate = DEFAULT_STRING_VALUE;
    }

    public String getsParent() {
        return sParent;
    }

    public void setsParent(String sParent) {
        if(sParent == null)
            throw new IllegalArgumentException("DataModel.setsParent(String sParent) - input cannot be null");

        this.sParent = sParent;
    }

    public String getsChild() {
        return sChild;
    }

    public void setsChild(String sChild) {
        if(sChild == null)
            throw new IllegalArgumentException("DataModel.setsChild(String sChild) - input cannot be null");

        this.sChild = sChild;
    }

    public String getsTemplate() {
        return sTemplate;
    }

    public void setsTemplate(String sTemplate) {
        if(sTemplate == null)
            throw new IllegalArgumentException("DataModel.setsTemplate(String sTemplate) - input cannot be null");

        this.sTemplate = sTemplate;
    }

    public Long getlParent() {
        return lParent;
    }

    public void setlParent(Long lParent) {
        if(lParent == null)
            throw new IllegalArgumentException("DataModel.setsParent(String sParent) - input cannot be null");

        this.lParent = lParent;
    }

    public Long getlChild() {
        return lChild;
    }

    public void setlChild(Long lChild) {
        if(lChild == null)
            throw new IllegalArgumentException("DataModel.setlChild(Long lChild)  - input cannot be null");

        this.lChild = lChild;
    }

    public Long getlTemplate() {
        return lTemplate;
    }

    public void setlTemplate(Long lTemplate) {
        if(lTemplate == null)
            throw new IllegalArgumentException("DataModel.setlTemplate(Long lTemplate) - input cannot be null");

        this.lTemplate = lTemplate;
    }

    public Map<String, Long> getMapParentName_ParentID() {
        return mapParentName_ParentID;
    }

    public Map<Long, Map<String, Long>> getMapParentID_mapChildNameChildID() {
        return mapParentID_mapChildNameChildID;
    }

    public Map<String, Long> getMapTempName_TempSetting() {
        return mapTempName_TempSetting;
    }
}
