package com.aminalzanki.autoplaces;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends Activity
{

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Initialize my location
        this.initializeMap();
    }

    /**
     * Show current location
     */
    private void initializeMap()
    {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        this.googleMap = mapFragment.getMap();
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.setMyLocationEnabled(true);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);

        // Find myLocationButton view
        View myLocationButton = mapFragment.getView().findViewById(0x2);

        if (myLocationButton != null && myLocationButton.getLayoutParams() instanceof RelativeLayout.LayoutParams)
        {
            // ZoomControl is inside of RelativeLayout
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myLocationButton
                    .getLayoutParams();
            // Align it to - parent BOTTOM|LEFT
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

            // Update margins, set to 10dp
            final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics());
            params.setMargins(margin, margin, margin, margin);

            myLocationButton.setLayoutParams(params);
        }

    }
}
