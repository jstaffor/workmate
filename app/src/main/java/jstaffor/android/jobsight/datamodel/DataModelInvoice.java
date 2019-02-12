package jstaffor.android.jobsight.datamodel;

import jstaffor.android.jobsight.database.DatabaseModel;

public class DataModelInvoice
{
    public static final Long DEFAULT_LONG_VALUE = -1L;
    public static final String DEFAULT_VALUE_COLUMN_INVOICE_NAME = DatabaseModel.INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_NAME;
    public static final String DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_NAME = DatabaseModel.INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_NAME;
    public static final String DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_DETAILS = DatabaseModel.INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_DETAILS;
    public static final String DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_NAME = DatabaseModel.INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_NAME;
    public static final String DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_DETAILS = DatabaseModel.INVOICE.DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_DETAILS;

    private String sInvoiceName, sSupplierName, sSupplierDetails, sCustomerName, sCustomerDetails;

    private  Long lPrimaryKey, lUser, lParent, lChild;

    public DataModelInvoice()
    {
        sInvoiceName = DEFAULT_VALUE_COLUMN_INVOICE_NAME;
        sSupplierName = DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_NAME;
        sSupplierDetails = DEFAULT_VALUE_COLUMN_INVOICE_SUPPLIER_DETAILS;
        sCustomerName = DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_NAME;
        sCustomerDetails = DEFAULT_VALUE_COLUMN_INVOICE_CUSTOMER_DETAILS;
        lPrimaryKey = DEFAULT_LONG_VALUE;
        lUser =  DatabaseModel.USER.DEFAULT_VALUE_COLUMN_USER_ID;
        lParent = DatabaseModel.PARENT.DEFAULT_VALUE_COLUMN_PARENT_ID;
        lChild = DatabaseModel.CHILD.DEFAULT_VALUE_COLUMN_CHILD_ID;
    }

    public DataModelInvoice(String sInvoiceName, String sSupplierName, String sSupplierDetails, String sCustomerName, String sCustomerDetails,
                            Long lPrimaryKey, Long lUser, Long lParent, Long lChild)
    {
        setsInvoiceName(sInvoiceName);
        setsSupplierName(sSupplierName);
        setsSupplierDetails(sSupplierDetails);
        setsCustomerName(sCustomerName);
        setsCustomerDetails(sCustomerDetails);
        setlPrimaryKey(lPrimaryKey);
        setlUser(lUser);
        setlParent(lParent);
        setlChild(lChild);
    }
    public Long getlPrimaryKey() {
        return lPrimaryKey;
    }

    public void setlPrimaryKey(Long lPrimaryKey) {
        if(lPrimaryKey == null)
            throw new IllegalArgumentException("DataModelInvoice.setlPrimaryKey(Long lPrimaryKey) - input cannot be null");
        this.lPrimaryKey = lPrimaryKey;
    }

    public Long getlUser() {
        return lUser;
    }

    public void setlUser(Long lUser) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setlUser(Long lUser) - input cannot be null");
        this.lUser = lUser;
    }

    public Long getlParent() {
        return lParent;
    }

    public void setlParent(Long lParent) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setlParent(Long lParent) - input cannot be null");
        this.lParent = lParent;
    }

    public Long getlChild() {
        return lChild;
    }

    public void setlChild(Long lChild) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setlChild(Long lChild) - input cannot be null");
        this.lChild = lChild;
    }

    public String getsInvoiceName() {
        return sInvoiceName;
    }

    public void setsInvoiceName(String sInvoiceName) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setsInvoiceName(String sInvoiceName) - input cannot be null");

        this.sInvoiceName = sInvoiceName;
    }

    public String getsSupplierName() {
        return sSupplierName;
    }

    public void setsSupplierName(String sSupplierName) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setsSupplierName(String sSupplierName) - input cannot be null");
        this.sSupplierName = sSupplierName;
    }

    public String getsSupplierDetails() {
        return sSupplierDetails;
    }

    public void setsSupplierDetails(String sSupplierDetails) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setsSupplierDetails(String sSupplierDetails) - input cannot be null");
        this.sSupplierDetails = sSupplierDetails;
    }

    public String getsCustomerName() {
        return sCustomerName;
    }

    public void setsCustomerName(String sCustomerName) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setsCustomerName(String sCustomerName) - input cannot be null");
        this.sCustomerName = sCustomerName;
    }

    public String getsCustomerDetails() {
        return sCustomerDetails;
    }

    public void setsCustomerDetails(String sCustomerDetails) {
        if(sInvoiceName == null)
            throw new IllegalArgumentException("DataModelInvoice.setsCustomerDetails(String sCustomerDetails) - input cannot be null");
        this.sCustomerDetails = sCustomerDetails;
    }
}
