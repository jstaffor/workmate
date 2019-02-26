package jstaffor.android.jobsight.datamodel.viewdata;

import android.graphics.Bitmap;

import java.io.File;

public class AudioRecording extends ViewDataGenericImage
{
    private static final String TAG = "AudioRecording";
    public static final String VIEWDATAAUDIORECORDING = "VDAR";

    protected String fileLocation;
    protected File file;

    public AudioRecording(String dateTime, String absolutePathToFile, String absolutePathToImage)
    {
        super(dateTime, absolutePathToImage);

        if(absolutePathToFile == null)
            throw new IllegalArgumentException("AudioRecording(String dateTime, String fileLocation, String absolutePathToImage)- inputs cannot be null");

        this.fileLocation = absolutePathToFile;
        this.file = new File(absolutePathToFile);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        if(file == null)
            throw new IllegalArgumentException("AudioRecording.setMyFile(MyFile myFile) - myFile cannot be null");

        this.file = file;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap)
    {
        if(imageBitmap == null)
            throw new IllegalArgumentException("AudioRecording.setImageBitmap(Bitmap imageBitmap) - imageBitmap cannot be null");

        this.imageBitmap = imageBitmap;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        if(imageLocation == null)
            throw new IllegalArgumentException("AudioRecording.setImageLocation(String imageLocation- imageLocation cannot be null");

        this.imageLocation = imageLocation;
    }
}