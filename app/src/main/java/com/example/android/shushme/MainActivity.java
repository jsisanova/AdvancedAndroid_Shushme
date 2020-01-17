package com.example.android.shushme;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.android.shushme.provider.PlaceContract;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;

import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks,
        OnConnectionFailedListener {

    // Constants
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int PLACE_PICKER_REQUEST = 1;
    private final String API_KEY = "AIzaSyCh-DEToeT4Nb0I02N9C5vVAkL-cBHib1g";

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Called when the activity is starting
     *
     * @param savedInstanceState The Bundle that contains the data supplied in onSaveInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.places_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PlaceListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        // Build up the LocationServices API client
        // Uses the addApi method to request the LocationServices API
        // Also uses enableAutoManage to automatically when to connect/suspend the client
//        GoogleApiClient client = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .addApi(Places.GEO_DATA_API)
//                .enableAutoManage(this, this)
//                .build();

        // Initialize Places.
        Places.initialize(getApplicationContext(), API_KEY);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);
    }

    /***
     * Called when the Google API Client is successfully connected
     *
     * @param connectionHint Bundle of data provided to clients by Google Play services
     */
    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Log.i(TAG, "API Client Connection Successful!");
    }

    /***
     * Called when the Google API Client is suspended
     *
     * @param cause cause The reason for the disconnection. Defined by constants CAUSE_*.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "API Client Connection Suspended!");
    }

    /***
     * Called when the Google API Client failed to connect to Google Play Services
     *
     * @param result A ConnectionResult that can be used for resolving the error
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(TAG, "API Client Connection Failed!");
    }

    /***
     * Button Click event handler to handle clicking the "Add new location" Button
     *
     * @param view
     */
    public void onAddPlaceButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.need_location_permission_message), Toast.LENGTH_LONG).show();
            return;
        }
//        Toast.makeText(this, getString(R.string.location_permissions_granted_message), Toast.LENGTH_LONG).show();

        // TODO (1) Create a PlacePicker.IntentBuilder and call startActivityForResult
//        if (!Places.isInitialized()) {
//            Places.initialize(getApplicationContext(), API_KEY);
//        }
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent,PLACE_PICKER_REQUEST);
    }




//        try {
//            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
//            // when a place is selected or with the user cancels.
//            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//            Intent i = builder.build(this);
//            startActivityForResult(i, PLACE_PICKER_REQUEST);
//        // TODO (2) Handle GooglePlayServices exceptions
//        // , because if device does not have up-to-date Google Play Services, app will crash
//        } catch (GooglePlayServicesRepairableException e) {
//            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
//        } catch (GooglePlayServicesNotAvailableException e) {
//            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
//        } catch (Exception e) {
//            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
//        }
//    }

    // TODO (3) Implement onActivityResult and check that the requestCode is PLACE_PICKER_REQUEST
    /***
     * Called when the Place Picker Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activity
     * @param data        The Intent that carries the result data.
     */
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
//            // TODO (4) In onActivityResult, use PlacePicker.getPlace to extract the Place ID and insert it into the DB
//            Place place = PlacePicker.getPlace(this, data);
//            if (place == null) {
//                Log.i(TAG, "No place selected");
//                return;
//            }
//
//            // Extract the place information from the API
//            String placeName = place.getName().toString();
//            String placeAddress = place.getAddress().toString();
//            String placeID = place.getId();
//
//            // Insert a new place into DB
//            // we choose to store placeID, other place information are prohibited to store more than 30 days
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeID);
//            getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                // Extract the place information from the API
//                String placeName = place.getName().toString();
//                String placeAddress = place.getAddress().toString();
                String placeID = place.getId();

                // Insert a new place into DB
                // we choose to store placeID, other place information are prohibited to store more than 30 days
                ContentValues contentValues = new ContentValues();
                contentValues.put(PlaceContract.PlaceEntry.COLUMN_PLACE_ID, placeID);
                getContentResolver().insert(PlaceContract.PlaceEntry.CONTENT_URI, contentValues);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }





    @Override
    public void onResume() {
        super.onResume();

        // Initialize location permissions checkbox
        CheckBox locationPermissions = (CheckBox) findViewById(R.id.location_permission_checkbox);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationPermissions.setChecked(false);
        } else {
            locationPermissions.setChecked(true);
            locationPermissions.setEnabled(false);
        }
    }

    public void onLocationPermissionClicked(View view) {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_FINE_LOCATION);
    }
}
