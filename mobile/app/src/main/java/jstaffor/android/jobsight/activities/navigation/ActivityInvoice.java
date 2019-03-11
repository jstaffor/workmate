package jstaffor.android.jobsight.activities.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.database.functionality.DatabaseInvoice;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.DataModelInvoice;
import jstaffor.android.jobsight.datamodel.utilities.DataModelUtilities;

public class ActivityInvoice extends Activity implements View.OnClickListener {
    private static final String TAG = "ActivityInvoice";

    private EditText txt_date, txt_supplier, txt_customer, txt_other;
    private Spinner spn_select_customer;
    private Button btn_generate_invoice;

    private DataModel dataModel;
    private List<DataModelInvoice> lDataModelInvoice;

    private DatabaseAccess databaseAccess;
    private DatabaseInvoice databaseInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        // Initialize view
        this.txt_date = findViewById(R.id.activity_invoice_txt_date);
        this.spn_select_customer = findViewById(R.id.activity_invoice_spn_select_customer);
        this.txt_supplier = findViewById(R.id.activity_invoice_txt_supplier);
        this.txt_customer = findViewById(R.id.activity_invoice_txt_customer);
        this.txt_other = findViewById(R.id.activity_invoice_txt_other);
        this.btn_generate_invoice = findViewById(R.id.activity_invoice_btn_generate_invoice);

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

        //Clear text fields so User can choose
        txt_supplier.setText("");
        txt_customer.setText("");
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.activity_invoice_btn_generate_invoice:

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

    private void setOnLoadAndOnItemSelectedAdapters()
    {
        if(AppSettings.APP_DEBUG_MODE)
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