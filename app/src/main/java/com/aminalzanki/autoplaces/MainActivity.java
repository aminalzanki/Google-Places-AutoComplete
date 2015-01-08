package com.aminalzanki.autoplaces;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class MainActivity extends Activity
        implements TextWatcher, View.OnClickListener, AdapterView.OnItemClickListener
{

    private GoogleMap googleMap;

    private AutoCompleteTextView mAutoCompleteSearch;

    private ImageView mClearIcon;

    private String mAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // Initialize map
        this.initializeMap();

        // Initialize search bar
        this.initSearchBar();
    }

    /**
     * Initialize Google Map
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

        if (myLocationButton != null && myLocationButton
                .getLayoutParams() instanceof RelativeLayout.LayoutParams)
        {
            // MyLocation is inside of RelativeLayout
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

    /**
     * Initialize Search Bar
     */
    private void initSearchBar()
    {
        // Wire up references to the UI elements
        this.mClearIcon = (ImageView) this.findViewById(R.id.clear_icon);
        this.mAutoCompleteSearch = (AutoCompleteTextView) this
                .findViewById(R.id.search_address_text);

        this.mClearIcon.setVisibility(View.GONE);

        // Set adapter for PlacesAutoComplete
        this.mAutoCompleteSearch.setAdapter(
                new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list));

        // Handle onTextChanged
        this.mAutoCompleteSearch.addTextChangedListener(this);

        // Handle onClick
        this.mAutoCompleteSearch.setOnItemClickListener(this);
        this.mClearIcon.setOnClickListener(this);
    }

    /**
     * Clear search bar
     */
    private void clearSearchBar()
    {
        MainActivity.this.mAutoCompleteSearch.getEditableText().clear();
    }

    /**
     * Hide keyboard
     */
    private void hideKeyboard()
    {
        final View currentFocus = this.getCurrentFocus();

        if (currentFocus != null)
        {
            ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                            currentFocus.getWindowToken(), 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        // Don't Care
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        // Clear button will appear if text change
        if (s.length() > 0)
        {
            this.mClearIcon.setVisibility(View.VISIBLE);
        } else
        {
            this.mClearIcon.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        // Don't Care
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.clear_icon:
                this.clearSearchBar();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id)
    {
        this.mAddress = (String) adapterView.getItemAtPosition(position);

        Toast.makeText(this, "Address: " + this.mAddress, Toast.LENGTH_LONG).show();

        // Force close keyboard after item clicked
        this.hideKeyboard();
    }
}
