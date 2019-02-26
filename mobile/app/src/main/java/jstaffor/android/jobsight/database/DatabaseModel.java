package jstaffor.android.jobsight.database;

import android.provider.BaseColumns;
import jstaffor.android.jobsight.datamodel.DataModel;

public final class DatabaseModel
{
    private static final String TAG = "DatabaseModel";
    private DatabaseModel() {}

    public static final String CURRENT_TIMESTAMP = "ts";

    public static class USER implements BaseColumns {
        //Primary
        public static final String TABLE_NAME = "user_";
        public static final String COLUMN_USER_ID = USER.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = USER.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_USER_EMAIL = USER.TABLE_NAME + "email";

        //DEFAULT VALUES
        public final static Long DEFAULT_VALUE_COLUMN_USER_ID = -1L;
    }

    public static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE " + USER.TABLE_NAME + " (" +
                    USER.COLUMN_USER_ID + " LONG PRIMARY KEY," +
                    USER.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    USER.COLUMN_USER_EMAIL + " TEXT)";

    public static class PARENT implements BaseColumns {
        //Primary
        public static final String TABLE_NAME = "parent_";
        public static final String COLUMN_PARENT_ID = PARENT.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = PARENT.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_PARENT_NAME = PARENT.TABLE_NAME + "name";
        //FK
        public static final String COLUMN_USER_ID = USER.TABLE_NAME + "id";
        //DEFAULT VALUES
        public final static String DEFAULT_VALUE_COLUMN_PARENT_NAME = "DEFAULT GROUP";
        public final static Long DEFAULT_VALUE_COLUMN_PARENT_ID = -1L;
    }

    public static final String SQL_CREATE_TABLE_PARENT =
            "CREATE TABLE " + PARENT.TABLE_NAME + " (" +
                    PARENT.COLUMN_PARENT_ID + " LONG PRIMARY KEY," +
                    PARENT.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    PARENT.COLUMN_PARENT_NAME + " TEXT," +
                    PARENT.COLUMN_USER_ID + " LONG)";


    public static class CHILD implements BaseColumns {
        //Primary
        public static final String TABLE_NAME = "child_";
        public static final String COLUMN_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = CHILD.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_CHILD_NAME = CHILD.TABLE_NAME + "name";
        //FK
        public static final String COLUMN_PARENT_ID = PARENT.TABLE_NAME + "id";
        //Other
        public static final String COLUMN_TEMPLATE_SETTING = TEMPLATE.COLUMN_TEMPLATE_SETTING;
        //DEFAULT VALUESCOLUMN_PARENT_ID
        public final static Long DEFAULT_VALUE_COLUMN_CHILD_ID = -1L;
    }

    public static final String SQL_CREATE_TABLE_CHILD =
            "CREATE TABLE " + CHILD.TABLE_NAME + " (" +
                    CHILD.COLUMN_CHILD_ID + " LONG PRIMARY KEY," +
                    CHILD.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    CHILD.COLUMN_CHILD_NAME + " TEXT," +
                    CHILD.COLUMN_PARENT_ID + " LONG," +
                    CHILD.COLUMN_TEMPLATE_SETTING + " LONG)";


    public static class TEMPLATE implements BaseColumns {
        //Primary
        public static final String TABLE_NAME = "template_";
        public static final String COLUMN_TEMPLATE_ID = TEMPLATE.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = TEMPLATE.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_TEMPLATE_NAME = TEMPLATE.TABLE_NAME + "name";
        public static final String COLUMN_TEMPLATE_SETTING = TEMPLATE.TABLE_NAME + "setting";
        //FK
        public static final String COLUMN_USER_ID = USER.TABLE_NAME + "id";

        //DEFAULT VALUES
        public final static String DEFAULT_VALUE_COLUMN_TEMPLATE_NAME = "DEFAULT TEMPLATE";
        public final static Long DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING = 1234567L;
        public static final Long COLUMN_TEMPLATE_SETTING_TEXT_INPUT = 1L;
        public static final Long COLUMN_TEMPLATE_SETTING_PHOTO_INPUT = 2L;
        public static final Long COLUMN_TEMPLATE_SETTING_SKETCH_INPUT = 3L;
        public static final Long COLUMN_TEMPLATE_SETTING_CURRENTLOCATION_INPUT = 4L;
        public static final Long COLUMN_TEMPLATE_SETTING_AUDIORECORDING_INPUT = 5L;
        public static final Long COLUMN_TEMPLATE_SETTING_VIDEORECORDING_INPUT = 6L;
        public static final Long COLUMN_TEMPLATE_SETTING_FILE_INPUT = 7L;
    }

    public static final String SQL_CREATE_TABLE_TEMPLATE =
            "CREATE TABLE " + TEMPLATE.TABLE_NAME + " (" +
                    TEMPLATE.COLUMN_TEMPLATE_ID + " LONG PRIMARY KEY," +
                    TEMPLATE.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    TEMPLATE.COLUMN_TEMPLATE_NAME + " TEXT," +
                    TEMPLATE.COLUMN_TEMPLATE_SETTING + " LONG," +
                    TEMPLATE.COLUMN_USER_ID + " LONG)";

    public static class INVOICE implements BaseColumns {
        //Primary
        public static final String TABLE_NAME = "invoice_";
        public static final String COLUMN_INVOICE_ID = INVOICE.TABLE_NAME + "id";
        public static final String COLUMN_INVOICE_NAME = INVOICE.TABLE_NAME + "name";
        public static final String COLUMN_INVOICE_SUPPLIER_NAME = INVOICE.TABLE_NAME + "supplier_name";
        public static final String COLUMN_INVOICE_SUPPLIER_DETAILS = INVOICE.TABLE_NAME + "supplier_details";
        public static final String COLUMN_INVOICE_CUSTOMER_NAME = INVOICE.TABLE_NAME + "customer_name";
        public static final String COLUMN_INVOICE_CUSTOMER_DETAILS = INVOICE.TABLE_NAME + "customer_details";
        //Other
        public static final String COLUMN_USER_ID = USER.TABLE_NAME + "id";
        public static final String COLUMN_PARENT_ID = PARENT.TABLE_NAME + "id";
        public static final String COLUMN_CHILD_ID = CHILD.TABLE_NAME + "id";

        //DEFAULT VALUES
        public static final String DEFAULT_VALUE_COLUMN_INVOICE_NAME = "Example Invoice";
        public static final String DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_NAME = "Mr. Seán Rían-Robyn";
        public static final String DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_DETAILS = "Lighthouse Lights LTD, Ballybeautiful, Wexford, Ireland. VAT #123456H.";
        public static final String DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_NAME = "Ms. Eire Eireann";
        public static final String DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_DETAILS = "Hook Head Lighthouse, New Ross, Wexford, Ireland. VAT #654321H.";
    }

    public static final String SQL_CREATE_TABLE_INVOICE =
            "CREATE TABLE " + INVOICE.TABLE_NAME + " (" +
                    INVOICE.COLUMN_INVOICE_ID + " LONG PRIMARY KEY," +
                    INVOICE.COLUMN_INVOICE_NAME + " TEXT," +
                    INVOICE.COLUMN_INVOICE_SUPPLIER_NAME + " TEXT," +
                    INVOICE.COLUMN_INVOICE_SUPPLIER_DETAILS + " TEXT," +
                    INVOICE.COLUMN_INVOICE_CUSTOMER_NAME + " TEXT," +
                    INVOICE.COLUMN_INVOICE_CUSTOMER_DETAILS + " TEXT," +
                    INVOICE.COLUMN_USER_ID + " LONG," +
                    INVOICE.COLUMN_PARENT_ID + " LONG," +
                    INVOICE.COLUMN_CHILD_ID + " LONG)";

    //DEFAULT VALUES - Create default group value
    public static final String SQL_CREATE_USER_DEFAULT_VALUES =
            "INSERT INTO " + USER.TABLE_NAME + " (" +
                    USER.COLUMN_USER_ID + ", " +
                    USER.COLUMN_USER_EMAIL + ") VALUES (" +
                    USER.DEFAULT_VALUE_COLUMN_USER_ID + ", 'solouser@jobsight.ie');";

    //DEFAULT VALUES - Create default group value
    public static final String SQL_CREATE_PARENT_DEFAULT_VALUES =
            "INSERT INTO " + PARENT.TABLE_NAME + " (" +
                    PARENT.COLUMN_PARENT_NAME + ", " +
                    PARENT.COLUMN_USER_ID + ") VALUES ('" +
                    PARENT.DEFAULT_VALUE_COLUMN_PARENT_NAME + "',"+
                    USER.DEFAULT_VALUE_COLUMN_USER_ID + ")";

    //DEFAULT VALUES - Create default template value
    public static final String SQL_CREATE_TEMPLATE_DEFAULT_VALUES =
            "INSERT INTO " + TEMPLATE.TABLE_NAME + " (" +
                    TEMPLATE.COLUMN_USER_ID + ", " +
                    TEMPLATE.COLUMN_TEMPLATE_NAME + ", " +
                    TEMPLATE.COLUMN_TEMPLATE_SETTING + ") VALUES ("+
                    USER.DEFAULT_VALUE_COLUMN_USER_ID + ", '" +
                    DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_NAME + "' , " +
                    DataModel.DEFAULT_VALUE_COLUMN_TEMPLATE_SETTING + ");";

    //DEFAULT VALUES - Create default template value
    public static final String SQL_CREATE_INVOICE_DEFAULT_VALUES =
            "INSERT INTO " + INVOICE.TABLE_NAME + " (" +
                    INVOICE.COLUMN_INVOICE_NAME + ", " +
                    INVOICE.COLUMN_INVOICE_SUPPLIER_NAME + ", " +
                    INVOICE.COLUMN_INVOICE_SUPPLIER_DETAILS + ", " +
                    INVOICE.COLUMN_INVOICE_CUSTOMER_NAME + ", " +
                    INVOICE.COLUMN_INVOICE_CUSTOMER_DETAILS + ", " +
                    INVOICE.COLUMN_USER_ID + ", " +
                    INVOICE.COLUMN_PARENT_ID + ", " +
                    INVOICE.COLUMN_CHILD_ID + ") VALUES ('"+
                    INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_NAME + "', '" +
                    INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_NAME + "', '" +
                    INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_DETAILS + "', '" +
                    INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_NAME + "', '" +
                    INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_DETAILS + "', " +
                    USER.DEFAULT_VALUE_COLUMN_USER_ID + ", " +
                    PARENT.DEFAULT_VALUE_COLUMN_PARENT_ID + ", " +
                    CHILD.DEFAULT_VALUE_COLUMN_CHILD_ID + ");";

    //**************************
    //DATA TABLES
    //**************************
    public static class TEXT implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "text_";
        public static final String COLUMN_TEXT_ID = TEXT.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = TEXT.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_TEXT_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_TEXT_DATA = TEXT.TABLE_NAME + "data";
    }

    public static final String SQL_CREATE_TABLE_TEXT =
            "CREATE TABLE " + TEXT.TABLE_NAME + " (" +
                    TEXT.COLUMN_TEXT_ID + " LONG PRIMARY KEY," +
                    TEXT.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    TEXT.COLUMN_TEXT_CHILD_ID + " LONG," +
                    TEXT.COLUMN_TEXT_DATA + " TEXT)";

    public static class PHOTO implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "photo_";
        public static final String COLUMN_PHOTO_ID = PHOTO.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = PHOTO.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_PHOTO_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_PHOTO_LOCATION = PHOTO.TABLE_NAME + "location";
    }

    public static final String SQL_CREATE_TABLE_PHOTO =
            "CREATE TABLE " + PHOTO.TABLE_NAME + " (" +
                    PHOTO.COLUMN_PHOTO_ID + " LONG PRIMARY KEY," +
                    PHOTO.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    PHOTO.COLUMN_PHOTO_CHILD_ID + " LONG," +
                    PHOTO.COLUMN_PHOTO_LOCATION + " TEXT)";

    public static class LOCATION implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "location_";
        public static final String COLUMN_LOCATION_ID = LOCATION.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = LOCATION.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_LOCATION_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_LOCATION_LATITUDE = LOCATION.TABLE_NAME + "latitude";
        public static final String COLUMN_LOCATION_LONGITUDE = LOCATION.TABLE_NAME + "longitude";
        public static final String COLUMN_LOCATION_ADDRESS = LOCATION.TABLE_NAME + "address";
        public static final String COLUMN_IMAGE_LOCATION = LOCATION.TABLE_NAME + "location";
    }

    public static final String SQL_CREATE_TABLE_LOCATION =
            "CREATE TABLE " + LOCATION.TABLE_NAME + " (" +
                    LOCATION.COLUMN_LOCATION_ID + " LONG PRIMARY KEY," +
                    LOCATION.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    LOCATION.COLUMN_LOCATION_CHILD_ID + " LONG," +
                    LOCATION.COLUMN_LOCATION_LATITUDE + " TEXT," +
                    LOCATION.COLUMN_LOCATION_LONGITUDE + " TEXT," +
                    LOCATION.COLUMN_LOCATION_ADDRESS + " TEXT," +
                    LOCATION.COLUMN_IMAGE_LOCATION + " TEXT)";

    public static class SKETCH implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "sketch_";
        public static final String COLUMN_SKETCH_ID = SKETCH.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = SKETCH.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_SKETCH_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_SKETCH_LOCATION = SKETCH.TABLE_NAME + "location";
    }

    public static final String SQL_CREATE_TABLE_SKETCH =
            "CREATE TABLE " + SKETCH.TABLE_NAME + " (" +
                    SKETCH.COLUMN_SKETCH_ID + " LONG PRIMARY KEY," +
                    SKETCH.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    SKETCH.COLUMN_SKETCH_CHILD_ID + " LONG," +
                    SKETCH.COLUMN_SKETCH_LOCATION + " TEXT)";

    public static class AUDIO_RECORDING implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "audio_recording_";
        public static final String COLUMN_AUDIO_RECORDING_ID = AUDIO_RECORDING.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = AUDIO_RECORDING.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_AUDIO_RECORDING_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_AUDIO_RECORDING_LOCATION = AUDIO_RECORDING.TABLE_NAME + "location_audio";
        public static final String COLUMN_AUDIO_RECORDING_IMAGE_LOCATION = AUDIO_RECORDING.TABLE_NAME + "location_image";
    }

    public static final String SQL_CREATE_TABLE_AUDIO_RECORDING =
            "CREATE TABLE " + AUDIO_RECORDING.TABLE_NAME + " (" +
                    AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_ID + " LONG PRIMARY KEY," +
                    AUDIO_RECORDING.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_CHILD_ID + " LONG," +
                    AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_LOCATION + " TEXT," +
                    AUDIO_RECORDING.COLUMN_AUDIO_RECORDING_IMAGE_LOCATION + " TEXT)";

    public static class VIDEO_RECORDING implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "video_";
        public static final String COLUMN_VIDEO_RECORDING_ID = VIDEO_RECORDING.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = VIDEO_RECORDING.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_VIDEO_RECORDING_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_VIDEO_RECORDING_LOCATION = VIDEO_RECORDING.TABLE_NAME + "location";
        public static final String COLUMN_VIDEO_THUMNAIL_LOCATION = VIDEO_RECORDING.TABLE_NAME + "thumbnaillocation";
    }

    public static final String SQL_CREATE_TABLE_VIDEO_RECORDING =
            "CREATE TABLE " + VIDEO_RECORDING.TABLE_NAME + " (" +
                    VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_ID + " LONG PRIMARY KEY," +
                    VIDEO_RECORDING.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_CHILD_ID + " LONG," +
                    VIDEO_RECORDING.COLUMN_VIDEO_RECORDING_LOCATION + " TEXT," +
                    VIDEO_RECORDING.COLUMN_VIDEO_THUMNAIL_LOCATION + " TEXT)";

    public static class FILE implements BaseColumns {
        //Pimary
        public static final String TABLE_NAME = "file_";
        public static final String COLUMN_FILE_ID = FILE.TABLE_NAME + "id";
        public static final String COLUMN_TIMESTAMP = FILE.TABLE_NAME + CURRENT_TIMESTAMP;
        public static final String COLUMN_FILE_RECORDING_CHILD_ID = CHILD.TABLE_NAME + "id";
        public static final String COLUMN_FILE_RECORDING_LOCATION = FILE.TABLE_NAME + "location";
        public static final String COLUMN_FILE_NAME = FILE.TABLE_NAME + "name";

        //DEFAULT VALUES
        public final static String DEFAULT_VALUE_COLUMN_FILE_NAME = "UNKNOWN";
    }

    public static final String SQL_CREATE_TABLE_FILE_RECORDING =
            "CREATE TABLE " + FILE.TABLE_NAME + " (" +
                    FILE.COLUMN_FILE_ID + " LONG PRIMARY KEY," +
                    FILE.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP," +
                    FILE.COLUMN_FILE_RECORDING_CHILD_ID + " LONG," +
                    FILE.COLUMN_FILE_RECORDING_LOCATION + " TEXT," +
                    FILE.COLUMN_FILE_NAME + " TEXT)";
}