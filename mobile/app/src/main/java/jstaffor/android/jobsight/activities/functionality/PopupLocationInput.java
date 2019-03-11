package jstaffor.android.jobsight.activities.functionality;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import jstaffor.android.jobsight.R;
import jstaffor.android.jobsight.activities.navigation.ActivityCreateAndAdd;
import jstaffor.android.jobsight.appsettings.AppSettings;
import jstaffor.android.jobsight.database.functionality.DatabaseLocation;
import jstaffor.android.jobsight.datamodel.DataModel;
import jstaffor.android.jobsight.datamodel.viewdata.Location;
import jstaffor.android.jobsight.utilities.AccessInternalStorage;
import jstaffor.android.jobsight.utilities.CaptureCurrentLocation;

public class PopupLocationInput extends DialogFragment implements View.OnClickListener
{
    private static final String TAG = "PopupLocationInput";
    private TextView txt_latitudelongitude;
    private EditText txt_address;
    private WebView locationinput_webview;
    private Button btn_create;
    private static WebSettings webSettings;
    private AccessInternalStorage accessInternalStorage;

    // Empty constructor required for DialogFragment
    public PopupLocationInput() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewPopupLocationInput = inflater.inflate(R.layout.popup_locationinput, container);

        // Initialize view
        txt_latitudelongitude = viewPopupLocationInput.findViewById(R.id.popup_locationinput_latitudelongitude);
        txt_address = viewPopupLocationInput.findViewById(R.id.popup_locationinput_address);
        locationinput_webview = viewPopupLocationInput.findViewById(R.id.popup_locationinput_webview);
        btn_create = viewPopupLocationInput.findViewById(R.id.popup_locationinput_btn_create);
        btn_create.setOnClickListener(this);

        if(AppSettings.APP_DEBUG_MODE) {
            Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) | Location.LATITUDE) | " +getArguments().getString(Location.LATITUDE));
            Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) | Location.LONGITUDE) | " +getArguments().getString(Location.LONGITUDE));
            Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) | Location.ADDRESS) | " +(getArguments().getString(Location.ADDRESS)).trim());
        }

        //Populate data from database
        txt_latitudelongitude.setText((getString(R.string.latitude) + ": " + getArguments().getString(Location.LATITUDE) + " | " + getString(R.string.longitude) + ": " + getArguments().getString(Location.LONGITUDE)).trim());
        txt_address.setText((getArguments().getString(Location.ADDRESS)).trim());

        webSettings = locationinput_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        locationinput_webview.setWebViewClient(new WebViewClient());
        locationinput_webview.loadUrl(CaptureCurrentLocation.getInternetBrowserLinkForGMaps(getArguments().getString(Location.LATITUDE), getArguments().getString(Location.LONGITUDE)));

        return viewPopupLocationInput;
    }

    @Override
    public void onClick(View view) {
        try
        {
        switch (view.getId()) {
            case R.id.popup_locationinput_btn_create:

                accessInternalStorage = new AccessInternalStorage(getContext());

                //(2) Get image that represents the audio

                final String imageAbsolutePath = accessInternalStorage.saveBitmapToInternalStorage(getBitmap(), ((ActivityCreateAndAdd) getActivity()).getDataModel().getlParent(), ((ActivityCreateAndAdd) getActivity()).getDataModel().getlChild());

                if(AppSettings.APP_DEBUG_MODE)
                    Log.d(TAG, "onClick(View view) | saveBitmapToInternalStorage(getBitmap(), ((ActivityCreateAndAdd) getActivity()).getDataModel().getlParent(), ((ActivityCreateAndAdd) getActivity()).getDataModel().getlChild()) | " +imageAbsolutePath);

                //(2) Get image that represents the audio

                if (new DatabaseLocation(getContext()).createLocationEntry(
                        getArguments().getLong(DataModel.CHILD),
                        txt_address.getText().toString().trim(),
                        getArguments().getString(Location.LATITUDE),
                        getArguments().getString(Location.LONGITUDE),
                        imageAbsolutePath)) {
                    Toast toast = Toast.makeText(getContext(), getString(R.string.location_saved_success), Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Log.e(TAG, "onClick(View view) | case R.id.popup_locationinput_btn_create new DatabaseLocation(getContext()).createLocationEntry() | " + "false");
                    Toast toast = Toast.makeText(getContext(), getString(R.string.location_saved_fail), Toast.LENGTH_SHORT);
                    toast.show();
                }

                this.dismiss();
                break;
            default:
                Log.e(TAG, "onClick(View view) - unknown selection");
                throw new IllegalArgumentException();
        }
    }
        catch (Exception exception)
        {
            Toast toast = Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public Bitmap getBitmap()
    {
        Bitmap curBitmap = Bitmap.createBitmap(locationinput_webview.getWidth(), locationinput_webview.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas curCanvas = new Canvas(curBitmap);
        locationinput_webview.layout(locationinput_webview.getLeft(), locationinput_webview.getTop(), locationinput_webview.getRight(), locationinput_webview.getBottom());
        locationinput_webview.draw(curCanvas);

        if(AppSettings.APP_DEBUG_MODE)
            Log.d(TAG, "getBitmap() | curBitmap.getAllocationByteCount() | " + curBitmap.getAllocationByteCount());

        return curBitmap;
    }
}