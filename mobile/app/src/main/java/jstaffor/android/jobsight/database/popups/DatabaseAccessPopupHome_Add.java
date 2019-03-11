package jstaffor.android.jobsight.database.popups;

import android.content.Context;
import android.util.Log;

import jstaffor.android.jobsight.appsettings.AppSettings;

public class DatabaseAccessPopupHome_Add extends DatabaseAccessPopup
{
    private static final String TAG = "DBAccessPopupHome_Add";
    public DatabaseAccessPopupHome_Add(Context context)
    {
        super(context);
    }

    public Long getTemplateSettingForChild(Long lChild)
    {
        final long localTemplateSettingForChild = super.getTemplateSettingForChild(lChild);

        if (AppSettings.DATABASE_DEBUG_MODE)
            Log.d(TAG, "getTemplateSettingForChild(Long lChild) | finally | localTemplateSettingForChild = " +localTemplateSettingForChild);

        return localTemplateSettingForChild;
    }
}