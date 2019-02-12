package jstaffor.android.jobsight.datamodel.viewdata;

public class Text extends ViewDataGeneric
{
    private static final String TAG = "Text";
    public static final String VIEWDATATEXT = "VDT";

    protected String textToDisplay = "-1";

    protected Text(String dateTime)
    {
        super(dateTime);
    }

    public Text(String dateTime, String textToDisplay)
    {
        super(dateTime);

        if(textToDisplay == null )
            throw new IllegalArgumentException("Text.Text(String dateTime, String textToDisplay) - no inputs can be null");

        this.textToDisplay = textToDisplay;
    }

    public String getTextToDisplay() {
        return textToDisplay;
    }

    public void setTextToDisplay(String textToDisplay) {
        if(textToDisplay == null )
            throw new IllegalArgumentException("Text.setTextToDisplay(String textToDisplay) - no inputs can be null");

        this.textToDisplay = textToDisplay;
    }
}
