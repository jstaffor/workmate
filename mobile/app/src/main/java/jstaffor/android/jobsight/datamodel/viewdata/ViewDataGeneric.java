package jstaffor.android.jobsight.datamodel.viewdata;

public class ViewDataGeneric
{
    private static final String TAG = "ViewDataGeneric";
    public static final String VIEWDATAGENERIC = "VDG";

    protected String dateTime = "-1";

    public ViewDataGeneric(String dateTime)
    {
        if(dateTime == null)
            throw new IllegalArgumentException("ViewDataGeneric.ViewDataGeneric(String dateTime) - dateTime cannot be null");

            this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        if(dateTime == null)
            throw new IllegalArgumentException("ViewDataGeneric.setDateTime(String dateTime) - dateTime cannot be null");

        this.dateTime = dateTime;
    }
}

