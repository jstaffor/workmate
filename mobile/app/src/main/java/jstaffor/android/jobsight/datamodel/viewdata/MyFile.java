package jstaffor.android.jobsight.datamodel.viewdata;

import java.io.File;

import jstaffor.android.jobsight.datamodel.DataModel;

public class MyFile extends ViewDataGeneric
{
    private static final String TAG = "MyFile";
    public static final String VIEWDATAFILE = "VDF";

    protected String nameOfFile = DataModel.DEFAULT_VALUE_COLUMN_FILE_NAME;
    protected String fileLocation= "-1";
    protected File file;

    protected MyFile(String dateTime)
    {
        super(dateTime);
    }

    public MyFile(String dateTime, String fileLocation)
    {
        super(dateTime);

        if(fileLocation == null)
            throw new IllegalArgumentException("MyFile.MyFile(String dateTime, String fileLocation) - dateTime cannot be null");

        this.fileLocation = fileLocation;
        this.file = new File(fileLocation);
    }

    public MyFile(String dateTime, String fileLocation, String nameOfFile)
    {
        this(dateTime, fileLocation);

        if(nameOfFile == null)
            throw new IllegalArgumentException("MyFile.MyFile(String dateTime, String fileLocation, String nameOfFile) - nameOfFile cannot be null");

        this.nameOfFile = nameOfFile;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        if(fileLocation == null)
            throw new IllegalArgumentException("MyFile.getFileLocation()  - fileLocation cannot be null");

        this.fileLocation = fileLocation;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File myFile) {
        if(myFile == null)
            throw new IllegalArgumentException("MyFile.setFile(File myFile) - myFile cannot be null");

        this.file = myFile;
    }

    public String getNameOfFile() {
        return nameOfFile;
    }

    public void setNameOfFile(String nameOfFile) {
        if(nameOfFile == null)
            throw new IllegalArgumentException("MyFile.setNameOfFile(String nameOfFile) - nameOfFile cannot be null");

        this.nameOfFile = nameOfFile;
    }
}
