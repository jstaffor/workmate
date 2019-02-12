package jstaffor.android.jobsight.datamodel.viewdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ViewDataGenericImage extends ViewDataGeneric
{
    private static final String TAG = "ViewDataGenericImage";
    public static final String VIEWDATAGENERICIMAGE = "VDGI";

    protected String imageLocation = "-1";
    protected Bitmap imageBitmap;

    public ViewDataGenericImage(String dateTime, String imageLocation)
    {
        super(dateTime);

        if(imageLocation == null )
            throw new IllegalArgumentException("ViewDataGenericImage.ViewDataGenericImage(String dateTime, String imageLocation) - no inputs can be null");

        this.imageLocation = imageLocation;

        this.imageBitmap = BitmapFactory.decodeFile(imageLocation);
        if(imageBitmap == null )
            throw new IllegalArgumentException("ViewDataGenericImage.ViewDataGenericImage(String dateTime, String imageLocation) - 'BitmapFactory.decodeFile(imageLocation)' returned null");
    }

    public ViewDataGenericImage(String dateTime, String imageLocation, Bitmap imageBitmap)
    {
        this(dateTime, imageLocation);

        if(imageBitmap == null )
            throw new IllegalArgumentException("Photo.Photo(String dateTime, String imageLocation, Bitmap imageBitmap) - no inputs can be null");

        else
            this.imageBitmap = imageBitmap;
    }
}

