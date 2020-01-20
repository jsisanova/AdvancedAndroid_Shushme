package com.example.android.shushme;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.android.shushme.provider.PlaceContract;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Constants
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_FINE_LOCATION = 111;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 10;
    private final String API_KEY = BuildConfig.ApiKey;

    // Member variables
    private PlaceListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private PlacesClient placesClient;
    private ArrayList<MyPlace> mData;

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
        mData = new ArrayList<>();

        // TODO (9) Create a boolean SharedPreference to store the state of the "Enable Geofences" switch
        // and initialize the switch based on the value of that SharedPreference

        // TODO (10) Handle the switch's change event and Register/Unregister geofences based on the value of isChecked
        // as well as set a private boolean mIsEnabled to the current switch's state

        // Initialize Places.
        Places.initialize(getApplicationContext(), API_KEY);
        // Create a new Places API client instance.
        placesClient = Places.createClient(this);

        // query all locally stored place IDs
        Cursor cursor = getContentResolver().query(PlaceContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor == null || cursor.getCount() == 0) return;
        ArrayList<String> id = new ArrayList<>();
        while (cursor.moveToNext()){
            getPlaceDetail(cursor.getString(1));
        }


        // TODO (1) Create a Geofencing class with a Context and GoogleApiClient constructor that
        // initializes a private member ArrayList of Geofences called mGeofenceList

        // TODO (2) Inside Geofencing, implement a public method called updateGeofencesList that
        // given a PlaceBuffer will create a Geofence object for each Place using Geofence.Builder
        // and add that Geofence to mGeofenceList

        // TODO (3) Inside Geofencing, implement a private helper method called getGeofencingRequest that
        // uses GeofencingRequest.Builder to return a GeofencingRequest object from the Geofence list

        // TODO (4) Create a GeofenceBroadcastReceiver class that extends BroadcastReceiver and override
        // onReceive() to simply log a message when called. Don't forget to add a receiver tag in the Manifest

        // TODO (5) Inside Geofencing, implement a private helper method called getGeofencePendingIntent that
        // returns a PendingIntent for the GeofenceBroadcastReceiver class

        // TODO (6) Inside Geofencing, implement a public method called registerAllGeofences that
        // registers the GeofencingRequest by calling LocationServices.GeofencingApi.addGeofences
        // using the helper functions getGeofencingRequest() and getGeofencePendingIntent()

        // TODO (7) Inside Geofencing, implement a public method called unRegisterAllGeofences that
        // unregisters all geofences by calling LocationServices.GeofencingApi.removeGeofences
        // using the helper function getGeofencePendingIntent()

        // TODO (8) Create a new instance of Geofencing using "this" as the context and mClient as the client
    }

    private void getPlaceDetail(String id){
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

        // Construct a request object, passing the place ID and fields array.
        FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, placeFields);

        // Add a listener to handle the response, get details for the specified place
        placesClient.fetchPlace(request)
                .addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        Place place = fetchPlaceResponse.getPlace();
                        mData.add(new MyPlace(place.getId(),place.getName(),place.getAddress()));
                        mAdapter.setmData(mData);
                        Log.i(TAG, "Place found: " + place.getName());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e(TAG, "Place not found: " + e.getMessage());
                }
            }
        });
    }


    // TODO (11) Call updateGeofenceList and registerAllGeofences if mIsEnabled is true

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

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), API_KEY);
        }
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }


    // Implement onActivityResult and check that the requestCode is PLACE_PICKER_REQUEST
    /***
     * Called when the Place Picker Activity returns back with a selected place (or after canceling)
     *
     * @param requestCode The request code passed when calling startActivityForResult
     * @param resultCode  The result code specified by the second activity
     * @param data        The Intent that carries the result data.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // In onActivityResult, use getPlaceFromIntent to extract the Place ID and insert it into the DB
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                if (place == null) {
                    Log.i(TAG, "No place selected");
                    return;
                }

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
                // Handle the error.
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



