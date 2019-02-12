package jstaffor.android.jobsight.datamodel.viewdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Sketch extends ViewDataGenericImage
{
    private static final String TAG = "Sketch";
    public static final String VIEWDATASKETCH = "VDS";

    public Sketch(String dateTime, String absolutePathToImage) {
        super(dateTime, absolutePathToImage);
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        if(imageBitmap == null)
            throw new IllegalArgumentException("Sketch.setImageBitmap(Bitmap imageBitmap) - imageBitmap cannot be null");

        this.imageBitmap = imageBitmap;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        if(imageLocation == null)
            throw new IllegalArgumentException("Sketch.setImageLocation(String imageLocation- imageLocation cannot be null");

        this.imageLocation = imageLocation;
    }
}
