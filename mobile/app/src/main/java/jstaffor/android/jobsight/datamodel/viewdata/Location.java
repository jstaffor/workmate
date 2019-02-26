package jstaffor.android.jobsight.datamodel.viewdata;

import android.graphics.Bitmap;

public class Location extends ViewDataGenericImage
{
    private static final String TAG = "Location";
    public static final String VIEWDATALOCATION = "VDL";
    public static final String ADDRESS = "ADDRESS";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    protected String textToDisplay = "-1";
    protected String sLatitude = "-1";
    protected String sLongitude = "-1";

    public Location(String dateTime, String textToDisplay, String sLatitude, String sLongitude, String imageLocation) {
        super(dateTime, imageLocation);

        if (textToDisplay == null || sLatitude == null || sLongitude == null)
            throw new IllegalArgumentException("Location.Location(String dateTime, String textToDisplay, String sLatitude, String sLongitude)- no inputs can be null");

        this.textToDisplay = textToDisplay;
        this.sLatitude = sLatitude;
        this.sLongitude = sLongitude;
    }

    public String getTextToDisplay() {
        return textToDisplay;
    }

    public void setTextToDisplay(String textToDisplay) {
        if (textToDisplay == null)
            throw new IllegalArgumentException("Location.setTextToDisplay(String textToDisplay)- textToDisplay cannot be null");

        this.textToDisplay = textToDisplay;
    }

    public String getsLatitude() {
        return sLatitude;
    }

    public void setsLatitude(String sLatitude) {
        if (sLatitude == null)
            throw new IllegalArgumentException("Location.setsLatitude(String sLatitude) - sLatitude cannot be null");

        this.sLatitude = sLatitude;
    }

    public String getsLongitude() {
        return sLongitude;
    }

    public void setsLongitude(String sLongitude) {
        if (sLongitude == null)
            throw new IllegalArgumentException("Location.setsLongitude(String sLongitude)- sLongitude cannot be null");

        this.sLongitude = sLongitude;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        if (imageBitmap == null)
            throw new IllegalArgumentException("Location.setImageBitmap(Bitmap imageBitmap) - imageBitmap cannot be null");

        this.imageBitmap = imageBitmap;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        if (imageLocation == null)
            throw new IllegalArgumentException("Location.setImageLocation(String imageLocation- imageLocation cannot be null");

        this.imageLocation = imageLocation;
    }
}