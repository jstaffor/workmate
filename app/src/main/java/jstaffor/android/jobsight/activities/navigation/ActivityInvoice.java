package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.database.functionality.DatabaseAudioRecording;
import jstaffor.android.jobsight.database.functionality.DatabaseFile;
import jstaffor.android.jobsight.database.functionality.DatabaseInvoice;
import jstaffor.android.jobsight.database.functionality.DatabaseLocation;
import jstaffor.android.jobsight.database.functionality.DatabasePhoto;
import jstaffor.android.jobsight.database.functionality.DatabaseSketch;
import jstaffor.android.jobsight.database.functionality.DatabaseText;
import jstaffor.android.jobsight.database.functionality.DatabaseVideoRecording;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.DataModelInvoice;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;
import jstaffor.android.jobsight.datamodel.viewdata.AudioRecording;
import jstaffor.android.jobsight.datamodel.viewdata.Location;
import jstaffor.android.jobsight.datamodel.viewdata.MyFile;
import jstaffor.android.jobsight.datamodel.viewdata.Photo;
import jstaffor.android.jobsight.datamodel.viewdata.Sketch;
import jstaffor.android.jobsight.datamodel.viewdata.Text;
import jstaffor.android.jobsight.datamodel.viewdata.VideoRecording;
import jstaffor.android.jobsight.datamodel.viewdata.ViewDataGeneric;
import jstaffor.android.jobsight.datamodel.viewdata.ViewDataStatic;
import jstaffor.android.jobsight.utilities.AccessExternalStorage;
import jstaffor.android.jobsight.utilities.CaptureAudioRecording;
import jstaffor.android.jobsight.utilities.Permissions;

public class ActivityInvoice extends Activity implements View.OnClickListener {
    private static final String TAG = "ActivityInvoice";

    private EditText txt_date, txt_supplier, txt_customer, txt_other;
    private Spinner spn_select_customer;
    private Button btn_generate_invoice;
    private ListView invoiceListView;
    private ArrayList<ViewDataGeneric> lViewDatumGenerics;
    private ArrayAdapter<ViewDataGeneric> arrayAdapterViewDataGeneric;

    private DataModel dataModel;
    private List<DataModelInvoice> lDataModelInvoice;

    private DatabaseAccess databaseAccess;
    private DatabaseInvoice databaseInvoice;

    private AccessExternalStorage accessExternalStorage;

    private final String UUID_pdf_file = java.util.UUID.randomUUID().toString()+".pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Initialize view
        this.btn_generate_invoice = findViewById(R.id.activity_invoice_btn_generate_invoice);
        this.txt_date = findViewById(R.id.activity_invoice_txt_date);
        this.spn_select_customer = findViewById(R.id.activity_invoice_spn_select_customer);
        this.txt_supplier = findViewById(R.id.activity_invoice_txt_supplier);
        this.txt_customer = findViewById(R.id.activity_invoice_txt_customer);
        this.txt_other = findViewById(R.id.activity_invoice_txt_other);
        this.invoiceListView = findViewById(R.id.activity_invoice_listview);

        btn_generate_invoice.setOnClickListener(this);
        txt_date.setOnClickListener(this);

        //Get Data from App
        dataModel = DataModelUtilities.turnJSONIntoDataModel(getIntent().getExtras().get(DataModelUtilities.DATA_MODEL).toString());
        lDataModelInvoice = new ArrayList<DataModelInvoice>();
        databaseInvoice = new DatabaseInvoice(this);

        //Get list of invoice customers from the database
        lDataModelInvoice = databaseInvoice.getInvoiceDataFromDatabase(DataModel.USER_GUID);

        //Populate dropdowns
        setOnLoadAndOnItemSelectedAdapters();

        //Get Data from Database
        lViewDatumGenerics = retrieveDatabaseData(dataModel.getlParent(), dataModel.getlChild());

        // DataBind ListView with items from ArrayAdapter
        arrayAdapterViewDataGeneric = generateArrayAdapters(lViewDatumGenerics,this);

        // DataBind ListView with items from ArrayAdapter
        invoiceListView.setAdapter( arrayAdapterViewDataGeneric );

        //Clear text fields so User can choose
        txt_supplier.setText("");
        txt_customer.setText("");
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.activity_invoice_btn_generate_invoice:
                    PdfDocument pdfDocument = generatePDF( findViewById(R.id.activity_invoice) );
                    createPDF( pdfDocument );

                    break;
                case R.id.activity_invoice_txt_date:
                        if(txt_date.getText().toString().equals(""))
                            txt_date.setText(Calendar.getInstance().getTime().toString());
                    break;
                default:
                    Log.e(TAG, "onClick(View view) - unknown selection");
                    throw new IllegalArgumentException();
            }
        } catch (Exception exception) {
            Toast toast = Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void createPDF(PdfDocument pdfDocument)
    {
        accessExternalStorage = new AccessExternalStorage(this);

        Permissions permissions = new Permissions(this);
        try
        {
            File externalStoragePublicDirectoryFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), UUID_pdf_file);

            FileOutputStream fos = new FileOutputStream(externalStoragePublicDirectoryFile);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();
        }
        catch (IOException ioException)
        {
            int i = 0;
        }
    }

    private PdfDocument generatePDF(View viewInvoice)
    {
        final PrintAttributes printAttrs = new PrintAttributes.Builder().
                setColorMode(PrintAttributes.COLOR_MODE_COLOR).
                setMediaSize(PrintAttributes.MediaSize.NA_LETTER).
                setResolution(new PrintAttributes.Resolution(TAG, PRINT_SERVICE, viewInvoice.getWidth(), viewInvoice.getHeight())).
                setMinMargins(PrintAttributes.Margins.NO_MARGINS).
                build();

        final PdfDocument pdfDocument = new PrintedPdfDocument(this, printAttrs);

        final PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewInvoice.getWidth(), viewInvoice.getHeight(), 1).create();

        // create a new page from the PageInfo
        final PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        // repaint the user's text into the page
        viewInvoice.draw(page.getCanvas());

        // do final processing of the page
        pdfDocument.finishPage(page);

        return pdfDocument;
    }

    private ArrayList<ViewDataGeneric> retrieveDatabaseData(Long lParent, Long lChild)
    {
        final ArrayList<ViewDataGeneric> viewDataGeneric = new ArrayList<ViewDataGeneric>();

        databaseAccess = new DatabaseText(this);
        final List<Text> lText = ((DatabaseText) databaseAccess).getTextDataFromDatabase(lChild);

        databaseAccess = new DatabaseLocation(this);
        final List<Location> lLocation = ((DatabaseLocation) databaseAccess).getLocationDataFromDatabase(lChild);

        databaseAccess = new DatabasePhoto(this);
        final List<Photo> lPhoto = ((DatabasePhoto) databaseAccess).getPhotoDataFromDatabase(lChild);

        databaseAccess = new DatabaseSketch(this);
        final List<Sketch> lSketch = ((DatabaseSketch) databaseAccess).getSketchDataFromDatabase(lChild);

        databaseAccess = new DatabaseVideoRecording(this);
        final List<VideoRecording> lVideoRecording = ((DatabaseVideoRecording) databaseAccess).getVideoRecordingDataFromDatabase(lChild);

        databaseAccess = new DatabaseAudioRecording(this);
        final List<AudioRecording> lAudioRecording = ((DatabaseAudioRecording) databaseAccess).getAudioRecordingDataFromDatabase(lChild);

        databaseAccess = new DatabaseFile(this);
        final List<MyFile> lMyFile = ((DatabaseFile) databaseAccess).getFileDataFromDatabase(lChild);

        for (int i = 0; i < lText.size(); i++) {
            viewDataGeneric.add(lText.get(i));

            if(AppSettings.DEBUG_MODE) {
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lText.get(i).getDateTime() | " + lText.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lText.get(i).getTextToDisplay() | " + lText.get(i).getTextToDisplay());
            }
        }

        for (int i = 0; i < lLocation.size(); i++) {
            viewDataGeneric.add(lLocation.get(i));

            if (AppSettings.DEBUG_MODE) {
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getDateTime() | " + lLocation.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getTextToDisplay() | " + lLocation.get(i).getTextToDisplay());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getsLatitude() | " + lLocation.get(i).getsLatitude());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getsLongitude() | " + lLocation.get(i).getsLongitude());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getImageLocation() | " + lLocation.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lLocation.get(i).getImageBitmap().getAllocationByteCount() | " + lLocation.get(i).getImageBitmap().getAllocationByteCount());
            }
        }

        for (int i = 0; i < lPhoto.size(); i++) {
            viewDataGeneric.add(lPhoto.get(i));

            if (AppSettings.DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lPhoto.get(i).getDateTime() | " + lPhoto.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lPhoto.get(i).getImageLocation() | " + lPhoto.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lPhoto.get(i).getImageBitmap().getAllocationByteCount() | " + lPhoto.get(i).getImageBitmap().getAllocationByteCount());
            }
        }

        for (int i = 0; i < lSketch.size(); i++) {
            viewDataGeneric.add(lSketch.get(i));

            if (AppSettings.DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lSketch.get(i).getDateTime() | " + lSketch.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lSketch.get(i).getImageLocation() | " + lSketch.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lSketch.get(i).getImageBitmap().getAllocationByteCount() | " + lSketch.get(i).getImageBitmap().getAllocationByteCount());
            }
        }

        for (int i = 0; i < lVideoRecording.size(); i++) {
            viewDataGeneric.add(lVideoRecording.get(i));

            if (AppSettings.DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getDateTime() | " + lVideoRecording.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getImageLocation() | " + lVideoRecording.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getVideoLocation() | " + lVideoRecording.get(i).getVideoLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getImageBitmap().getAllocationByteCount() | " + lVideoRecording.get(i).getImageBitmap().getAllocationByteCount());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lVideoRecording.get(i).getUri() | " + lVideoRecording.get(i).getUri());
            }
        }

        for (int i = 0; i < lAudioRecording.size(); i++) {
            viewDataGeneric.add(lAudioRecording.get(i));

            if (AppSettings.DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getDateTime() | " + lAudioRecording.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getImageLocation() | " + lAudioRecording.get(i).getImageLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getImageBitmap().getAllocationByteCount() | " + lAudioRecording.get(i).getImageBitmap().getAllocationByteCount());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getFileLocation() | " + lAudioRecording.get(i).getFileLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getFile().getAbsolutePath() | " + lAudioRecording.get(i).getFile().getAbsolutePath());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lAudioRecording.get(i).getFile().getName() | " + lAudioRecording.get(i).getFile().getName());
            }
        }

        for (int i = 0; i < lMyFile.size(); i++) {
            viewDataGeneric.add(lMyFile.get(i));

            if (AppSettings.DEBUG_MODE){
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getDateTime() | " + lMyFile.get(i).getDateTime());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getNameOfFile() | " + lMyFile.get(i).getNameOfFile());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getFile().getName() | " + lMyFile.get(i).getFile().getName());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getFileLocation() | " + lMyFile.get(i).getFileLocation());
                Log.d(TAG, "retrieveDatabaseData(Long lParent, Long lChild) | lMyFile.get(i).getFile().getAbsolutePath() | " + lMyFile.get(i).getFile().getAbsolutePath());
            }
        }

        Collections.sort(viewDataGeneric, new Comparator<ViewDataGeneric>()
        {
            public int compare(ViewDataGeneric o1, ViewDataGeneric o2)
            {
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });

        return viewDataGeneric;
    }

    /**
     * IMPORTANT: this method is a copy and paste from 'ActivityView.generateArrayAdapters(final ArrayList<ViewDataGeneric> lViewDatumGenerics, final Context context)'
     *
     * I've commented out the lines of code that are not required
     *
     * @param lViewDatumGenerics
     * @param context
     * @return
     */
    private ArrayAdapter<ViewDataGeneric> generateArrayAdapters(final ArrayList<ViewDataGeneric> lViewDatumGenerics, final Context context)
    {
        final ArrayAdapter<ViewDataGeneric> arrayAdapterViewDataGeneric = new ArrayAdapter<ViewDataGeneric>(this, R.layout.view_viewdata, lViewDatumGenerics)
        {
            @Override
            public View getView(final int position, View vView_viewdata, ViewGroup parent)
            {
                if (vView_viewdata == null)
                {
                    LayoutInflater layoutInflater = getLayoutInflater();
                    vView_viewdata = layoutInflater.inflate(R.layout.view_viewdata, parent, false);
                }

                final ViewDataStatic.Components components = new ViewDataStatic.Components();

                //https://developer.android.com/training/improving-layouts/smooth-scrolling
                components.dateTime = vView_viewdata.findViewById(R.id.view_viewdata_datetime);
                components.txt = vView_viewdata.findViewById(R.id.view_viewdata_txt);
                components.image = vView_viewdata.findViewById(R.id.view_viewdata_image);

                //Reset any lingering functionality
                components.txt.setVisibility(View.GONE);
                components.image.setVisibility(View.GONE);

                components.image.setOnClickListener(null);
//                stopAudioRecording();   //If the media player is playing, stop it!

                components.dateTime.setBackgroundColor(Integer.parseInt(getString(R.string.color_black)));
                components.dateTime.setTextColor(Integer.parseInt(getString(R.string.color_white)));

                //Setup custom view for the particular instanceof lViewDatumGenerics
                if (lViewDatumGenerics.get(position) instanceof Text)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.text) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.txt.setText(((Text) lViewDatumGenerics.get(position)).getTextToDisplay());

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof Location)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.location) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.image.setVisibility(View.VISIBLE);
                    components.txt.setText(((Location) lViewDatumGenerics.get(position)).getTextToDisplay());
                    components.image.setImageBitmap(((Location) lViewDatumGenerics.get(position)).getImageBitmap());
//                    components.image.setOnClickListener(new View.OnClickListener()
//                    {
//                        public void onClick(View v)
//                        {
//                            stopAudioRecording();   //If the media player is playing, stop it!
//
//                            final String urlToOpen = CaptureCurrentLocation.getInternetBrowserLinkForGMaps(
//                                    ((Location) lViewDatumGenerics.get(position)).getsLatitude(), ((Location) lViewDatumGenerics.get(position)).getsLongitude());
//
//                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen));
//                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
//
//                            startActivity(intent);
//                        }
//                    });

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof Photo)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.photo) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.image.setVisibility(View.VISIBLE);
                    components.image.setImageBitmap(((Photo) lViewDatumGenerics.get(position)).getImageBitmap());

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof Sketch)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.sketch) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.image.setVisibility(View.VISIBLE);
                    components.image.setImageBitmap(((Sketch) lViewDatumGenerics.get(position)).getImageBitmap());

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof VideoRecording)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.video) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.image.setVisibility(View.VISIBLE);
                    components.image.setImageBitmap(((VideoRecording) lViewDatumGenerics.get(position)).getImageBitmap());
//                    components.image.setOnClickListener(new View.OnClickListener()
//                    {
//
//                        public void onClick(View v)
//                        {
//                            stopAudioRecording();   //If the media player is playing, stop it!
//
//                            final Uri tempUri = FileProvider.getUriForFile(context,BuildConfig.APPLICATION_ID + ".provider", new File((((VideoRecording) lViewDatumGenerics.get(position)).getVideoLocation())));
//
//                            final Intent intent = new Intent(Intent.ACTION_VIEW, tempUri);
//                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            intent.setDataAndType(tempUri, "video/*");
//                            intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
//
//                            startActivity(intent);
//                        }
//                    });

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else if (lViewDatumGenerics.get(position) instanceof AudioRecording)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.audio) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.image.setVisibility(View.VISIBLE);
                    components.txt.setText(CaptureAudioRecording.getDurationOfAudioFile( ((AudioRecording) lViewDatumGenerics.get(position)).getFileLocation() ));
                    components.image.setImageBitmap( ((AudioRecording) lViewDatumGenerics.get(position)).getImageBitmap() );
//                    components.image.setOnClickListener(new View.OnClickListener()
//                    {
//                        public void onClick(View v)
//                        {
//                            try
//                            {
//                                stopAudioRecording();
//
//                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                                mediaPlayer.setDataSource(((AudioRecording) lViewDatumGenerics.get(position)).getFileLocation());
//                                mediaPlayer.prepare();
//                                mediaPlayer.start();
//                                isMediaPlayerPlaying = true;
//
//                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
//                                {
//                                    @Override
//                                    public void onCompletion(MediaPlayer mp)
//                                    {
//                                        stopAudioRecording();
//                                    }
//                                });
//
//                            } catch (Exception e)
//                            {
//                                Log.e(TAG, "if (lViewDatumGenerics.get(position) instanceof AudioRecording) void - onClick(View v) | catch (Exception e) | " + e.getMessage());
//
//                                if (AppSettings.DEBUG_MODE)
//                                    Log.d(TAG, "if (lViewDatumGenerics.get(position) instanceof AudioRecording) void - onClick(View v) | catch (Exception e) | " + e.getMessage());
//                            }
//                        }
//                    });

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);
                } else if (lViewDatumGenerics.get(position) instanceof MyFile)
                {
                    final String metaDataForReferencing = (lViewDatumGenerics.get(position)).getDateTime() + "  |  " + getString(R.string.file) + "  |  " + position;
                    components.dateTime.setText( metaDataForReferencing );

                    components.txt.setVisibility(View.VISIBLE);
                    components.txt.setText( ((MyFile) lViewDatumGenerics.get(position)).getNameOfFile() );

                    //https://developer.android.com/training/improving-layouts/smooth-scrolling
                    vView_viewdata.setTag(components);

                } else {
                    Log.e(TAG, "onClick(View view) - unknown selection - populateListOfViewsToDisplay(ArrayList<ViewDataGeneric> lViewDatumGenerics) - class doesn't exist");
                    throw new IllegalArgumentException();
                }

                return vView_viewdata;
            }
        };

        return arrayAdapterViewDataGeneric;
    }

    private void setOnLoadAndOnItemSelectedAdapters()
    {
        if(AppSettings.DEBUG_MODE)
            Log.d(TAG, "createArrayAdapterInvoiceList() | lDataModelInvoice.getMapTempName_TempSetting().keySet() | " + dataModel.getMapTempName_TempSetting().keySet());

        final ArrayList<String> nameOfInvoices = new ArrayList<String>();

        //Add a 'blank' value that clears the fields
        nameOfInvoices.add(getString(R.string.select));

        for(DataModelInvoice currentDataModelInvoice: lDataModelInvoice)
            nameOfInvoices.add(currentDataModelInvoice.getsInvoiceName());

        spn_select_customer.setAdapter( new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameOfInvoices ));

        spn_select_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                final String currentSelected = spn_select_customer.getSelectedItem().toString();
                for(DataModelInvoice currentDataModelInvoice: lDataModelInvoice)
                {
                    if(currentSelected.equals(currentDataModelInvoice.getsInvoiceName()))
                    {
                        txt_customer.setText(currentDataModelInvoice.getsCustomerName() + ". \n" +currentDataModelInvoice.getsCustomerDetails());
                        txt_supplier.setText(currentDataModelInvoice.getsSupplierName() + ". \n" +currentDataModelInvoice.getsSupplierDetails());
                    }
                    else
                    {
                        txt_customer.setText("");
                        txt_supplier.setText("");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}