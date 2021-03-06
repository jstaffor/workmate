package jstaffor.android.jobsight.database.functionality;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.DatabaseAccess;
import jstaffor.android.jobsight.database.DatabaseModel;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.DataModelInvoice;

public class DatabaseInvoice extends DatabaseAccess
{
    private static final String TAG = "DatabaseText";
    public DatabaseInvoice(Context context) {
        super(context);
    }

    /**
     * @param COLUMN_TEXT_CHILD_ID
     * @param COLUMN_TEXT_DATA
     * @return true if save was successful.
     */
    public boolean createTextEntry(Long COLUMN_TEXT_CHILD_ID, String COLUMN_TEXT_DATA)
    {
        if(COLUMN_TEXT_CHILD_ID == null || COLUMN_TEXT_DATA == null)
            throw new IllegalArgumentException("DatabaseText.createTextEntry(Long COLUMN_TEXT_CHILD_ID, String COLUMN_TEXT_DATA) - no inputs can be null");

        //Set default setting in case something goes wrong
        Long newRowGroupId = DataModel.DEFAULT_LONG_VALUE;

        try
        {
            // Open connection to database and insert the new row, returning the primary key value of the new row
            sqliteDatabase = databaseModelHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            values = new ContentValues();
            values.put(DatabaseModel.TEXT.COLUMN_TEXT_CHILD_ID, COLUMN_TEXT_CHILD_ID);
            values.put(DatabaseModel.TEXT.COLUMN_TEXT_DATA, COLUMN_TEXT_DATA);

            newRowGroupId = sqliteDatabase.insert(DatabaseModel.TEXT.TABLE_NAME, null, values);

            if(AppSettings.DATABASE_DEBUG_MODE) {
                Log.d(TAG, "createTextEntry() | Long COLUMN_TEXT_CHILD_ID | " +COLUMN_TEXT_CHILD_ID);
                Log.d(TAG, "createTextEntry() | Long COLUMN_TEXT_DATA | " +COLUMN_TEXT_DATA);
                Log.d(TAG, "createTextEntry() | sqliteDatabase.insert(DatabaseModel.TEXT.TABLE_NAME, null, values) | " +newRowGroupId);
            }
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "createAudioRecordingEntry() | finally | newRowGroupId = " +newRowGroupId);
                listDatabaseValues();
            }
            closeDownDatabaseConnections();
            if(newRowGroupId == -1L) //sqliteDatabase.insert @return the row ID of the newly inserted row, or -1 if an error occurred
                return false;
            else
                return true;
        }
    }

    /**
     * @param lUser
     * @return List<DataModelInvoice> at a minimum, a default 'DataModelInvoice' is returned
     */
    public List<DataModelInvoice> getInvoiceDataFromDatabase(Long lUser)
    {
        if(lUser == null)
            throw new IllegalArgumentException("DatabaseInvoice.getInvoiceDataFromDatabase(Long lUser) - lUser cannot be null");

        final List<DataModelInvoice> lDataModelInvoice= new ArrayList<DataModelInvoice>();

        try {
            //Read the database for user ID
            sqliteDatabase = databaseModelHelper.getReadableDatabase();
            final String[] projection = {
                    DatabaseModel.INVOICE.COLUMN_INVOICE_ID,
                    DatabaseModel.INVOICE.COLUMN_INVOICE_NAME,
                    DatabaseModel.INVOICE.COLUMN_INVOICE_SUPPLIER_NAME,
                    DatabaseModel.INVOICE.COLUMN_INVOICE_SUPPLIER_DETAILS,
                    DatabaseModel.INVOICE.COLUMN_INVOICE_CUSTOMER_NAME,
                    DatabaseModel.INVOICE.COLUMN_INVOICE_CUSTOMER_DETAILS,
                    DatabaseModel.INVOICE.COLUMN_USER_ID,
                    DatabaseModel.INVOICE.COLUMN_PARENT_ID,
                    DatabaseModel.INVOICE.COLUMN_CHILD_ID
            };

            // Filter results WHERE "title" = 'My Title'
            //String selection = DatabaseModel.TEXT.COLUMN_TEXT_CHILD_ID + " = ?";
            //String[] selectionArgs = {lChild.toString()};
            String sortOrder = DatabaseModel.INVOICE.COLUMN_INVOICE_ID + " DESC";

            cursor = sqliteDatabase.query(
                    DatabaseModel.INVOICE.TABLE_NAME,     // The table to query
                    projection,                        // The array of columns to return (pass null to get all)
                    null,                         // The columns for the WHERE clause
                    null,                     // The values for the WHERE clause
                    null,                      // don't group the rows
                    null,                       // don't filter by row groups
                    sortOrder                          // The sort order
            );

            while (cursor.moveToNext())
            {
                lDataModelInvoice.add( new DataModelInvoice(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_INVOICE_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_INVOICE_SUPPLIER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_INVOICE_SUPPLIER_DETAILS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_INVOICE_CUSTOMER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_INVOICE_CUSTOMER_DETAILS)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_INVOICE_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_USER_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_PARENT_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseModel.INVOICE.COLUMN_CHILD_ID))
                ));

                if(AppSettings.DATABASE_DEBUG_MODE) {
                    Log.d(TAG, "getTextDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TIMESTAMP))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TIMESTAMP)));
                    Log.d(TAG, "getTextDataFromDatabase() | cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TEXT_DATA))| " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseModel.TEXT.COLUMN_TEXT_DATA)));
                }
            }

            if(lDataModelInvoice.size() == 0)
                lDataModelInvoice.add(new DataModelInvoice());
        }
        finally
        {
            if (AppSettings.DATABASE_DEBUG_MODE)
            {
                Log.d(TAG, "getInvoiceDataFromDatabase(Long lUser) | finally | lChild = " +lUser);
                listDatabaseValues();
            }

            closeDownDatabaseConnections();
        }

        return lDataModelInvoice;
    }
}
