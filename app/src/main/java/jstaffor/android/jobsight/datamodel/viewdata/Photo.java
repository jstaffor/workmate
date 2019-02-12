package jstaffor.android.jobsight.datamodel.viewdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Photo extends ViewDataGenericImage
{
    private static final String TAG = "Photo";
    public static final String VIEWDATAPHOTO = "VDP";

    public Photo(String dateTime,String absolutePathToImage)
    {
        super(dateTime, absolutePathToImage);
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        if(imageBitmap == null)
            throw new IllegalArgumentException("Photo.setImageBitmap(Bitmap imageBitmap) - imageBitmap cannot be null");

        this.imageBitmap = imageBitmap;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        if(imageLocation == null)
            throw new IllegalArgumentException("Photo.setImageLocation(String imageLocation- imageLocation cannot be null");

        this.imageLocation = imageLocation;
    }
}
