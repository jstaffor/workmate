package jstaffor.android.jobsight.datamodel.viewdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class VideoRecording extends ViewDataGeneric
{
    private static final String TAG = "VideoRecording";
    public static final String VIEWDATAVIDEORECORDING = "VDVR";

    protected String imageLocation;
    protected Bitmap imageBitmap;
    protected Uri uri;
    protected String videoLocation;

    protected VideoRecording(String dateTime)
    {
        super(dateTime);
    }

    public VideoRecording(String dateTime, String videoLocation, String imageLocation)
    {
        this(dateTime);

        if(videoLocation == null || imageLocation == null )
            throw new IllegalArgumentException("VideoRecording.VideoRecording(String dateTime, String videoLocation, String imageLocation) - no inputs can be null");

        this.videoLocation = videoLocation;
        this.imageLocation = imageLocation;
        this.imageBitmap = BitmapFactory.decodeFile(imageLocation);

        this.uri = Uri.parse(videoLocation);
    }

    public String getVideoLocation() {
        return videoLocation;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri)
    {
        if(uri == null)
            throw new IllegalArgumentException("VideoRecording.setUri(Uri uri) - uri cannot be null");

        this.uri = uri;
    }


    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation)
    {
        if(imageLocation == null)
            throw new IllegalArgumentException("VideoRecording.setImageLocation(String imageLocation) - imageLocation cannot be null");

        this.imageLocation = imageLocation;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        if(imageBitmap == null)
            throw new IllegalArgumentException("VideoRecording.setImageBitmap(Bitmap imageBitmap)- imageBitmap cannot be null");

        this.imageBitmap = imageBitmap;
    }
}