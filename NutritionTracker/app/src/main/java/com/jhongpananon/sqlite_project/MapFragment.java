package com.jhongpananon.sqlite_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 17;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private List<String> mLikelyPlaceNames = new ArrayList<>();
    private List<String> mLikelyPlaceAddresses = new ArrayList<>();
    private List<String> mLikelyPlaceAttributions = new ArrayList<>();
    private List<LatLng> mLikelyPlaceLatLngs = new ArrayList<>();
    private List<String> mLikelyPlaceRatings = new ArrayList<>();
    private List<String> mLikelyPlaceId = new ArrayList<>();

    public static final int TYPE_CAFE = 15;
    private Integer mPlaceType = TYPE_CAFE;

    public static final String LOC_ID = "LOC_ID";
    public static final String LOC_NAME = "LOC_NAME";
    public static final String LOC_ADDR = "LOC_ADDR";
    public static final String LOC_ATTR = "LOC_ATTR";
    public static final String LOC_COORD = "LOC_COORD";
    public static final String LOC_RATING = "LOC_RATING";

    View rootView;

    LocationChangeListener mCallback;

    /**
     * Interface for callback to pass data to another fragment
     */
    public interface LocationChangeListener {
        public void onLocationChange(Bundle bundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (LocationChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        rootView = inflater.inflate(R.layout.fragment_maps, container, false);


        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getActivity());

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity());

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        Button list_location = rootView.findViewById(R.id.list_button);
        list_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setListLocation();
                if (mLikelyPlaceNames.size() > 0) {
                    TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    tab.select();
                } else {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Search a location first!", Snackbar.LENGTH_SHORT)
                            .show();
                }

            }
        });

        getLocationPermission();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                mLikelyPlaceNames.clear();
                mLikelyPlaceAddresses.clear();
                mLikelyPlaceAttributions.clear();
                mLikelyPlaceLatLngs.clear();
                mLikelyPlaceRatings.clear();
                mLikelyPlaceId.clear();
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                mLikelyPlaceNames.add((String) place.getName());
                mLikelyPlaceAddresses.add((String) place.getAddress());
                mLikelyPlaceAttributions.add((String) place.getAttributions());
                mLikelyPlaceLatLngs.add(place.getLatLng());
                mLikelyPlaceRatings.add(String.valueOf(place.getRating()));
                mLikelyPlaceId.add(place.getId());

                showSearchedPlace(place);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }

        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity()!=null) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        //Do your stuff here
//
//    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.current_place_menu, menu);
//        return true;
//    }

    /**
     * Handles a click on the menu option to get a place.
     * @param item The menu item to handle.
     * @return Boolean.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace(TYPE_CAFE);
        }
        return true;
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) getActivity().findViewById(R.id.map), false);

                TextView title = ((TextView) infoWindow.findViewById(R.id.title));
                title.setText(marker.getTitle());

                TextView snippet = ((TextView) infoWindow.findViewById(R.id.snippet));
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            //Move the camera to the user's location and zoom in!
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace(final Integer type) {
        mPlaceType = type;
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<PlaceLikelihoodBufferResponse> placeResult =
                    mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener
                    (new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                        @Override
                        public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();

                                // Set the count, handling cases where less than 5 entries are returned.
                                int count;
                                Log.i("total count", String.valueOf(likelyPlaces.getCount()));
                                if (likelyPlaces.getCount() < M_MAX_ENTRIES) {
                                    count = likelyPlaces.getCount();
                                } else {
                                    count = M_MAX_ENTRIES;
                                }

                                int i = 0;
                                mLikelyPlaceNames = new ArrayList<>();
                                mLikelyPlaceAddresses = new ArrayList<>();
                                mLikelyPlaceAttributions = new ArrayList<>();
                                mLikelyPlaceLatLngs = new ArrayList<>();
                                mLikelyPlaceRatings = new ArrayList<>();
                                mLikelyPlaceId = new ArrayList<>();

                                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                                    List<Integer> placeType = placeLikelihood.getPlace().getPlaceTypes();
                                    for (int j = 0; j < placeType.size(); j++) {
//                                        Log.i("asdf", String.valueOf(placeType.get(j)));
                                        if (placeType.get(j) == mPlaceType) {
                                            // Build a list of likely places to show the user.
//                                        Log.i("asdf", String.valueOf(placeType.get(j)));
                                            mLikelyPlaceNames.add((String) placeLikelihood.getPlace().getName());
                                            mLikelyPlaceAddresses.add((String) placeLikelihood.getPlace()
                                                    .getAddress());
                                            mLikelyPlaceAttributions.add((String) placeLikelihood.getPlace()
                                                    .getAttributions());
                                            mLikelyPlaceLatLngs.add(placeLikelihood.getPlace().getLatLng());
                                            mLikelyPlaceRatings.add(String.valueOf(placeLikelihood.getPlace().getRating()));
                                            mLikelyPlaceId.add(placeLikelihood.getPlace().getId());

                                            i++;
                                            Log.i("count", String.valueOf(i));
                                            if (i > (count - 1)) {
                                                break;
                                            }
                                        }
                                    }
                                }

                                // Release the place likelihood buffer, to avoid memory leaks.
                                likelyPlaces.release();
                                Log.i("len", String.valueOf(mLikelyPlaceNames.size()));
                                for (int k = 0; k < mLikelyPlaceNames.size(); k++) {
                                    Log.i("name", String.valueOf(mLikelyPlaceNames.get(k)));
                                }
                                // Show a dialog offering the user the list of likely places, and add a
                                // marker at the selected place.
                                openPlacesDialog();

                            } else {
                                Log.e(TAG, "Exception: %s", task.getException());
                            }
                        }
                    });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(mDefaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    /**
     * Process what the user searched in Google Map Search Bar, and shows the
     * selected location and predicted results - provided the user has granted location permission.
     */
    private void showSearchedPlace(Place place) {
        if (mMap == null) {
            return;
        }

        if (mLocationPermissionGranted) {
            // Remove markers each search
            mMap.clear();

            Log.i("name", String.valueOf(place.getName()));
            String placeName = String.valueOf(place.getName());
            String arr [] = placeName.split(" ", 3);
            Log.i("split 0", arr[0]);
            String query = arr[0];
//            if (arr.length > 1) {
//                Log.i("split 1", arr[1]);
//                query += " " + arr[1];
//                Log.i("query", query);
//            }
            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<AutocompletePredictionBufferResponse> results = mGeoDataClient.getAutocompletePredictions(query, null, null);
            results.addOnCompleteListener(new OnCompleteListener<AutocompletePredictionBufferResponse>() {
                @Override
                public void onComplete(@NonNull Task<AutocompletePredictionBufferResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AutocompletePredictionBufferResponse predictions = task.getResult();
                        ArrayList<String> resultList = new ArrayList<>(predictions.getCount());
                        Log.i("size", String.valueOf(predictions.getCount()));
                        for (AutocompletePrediction predict : predictions) {
                            resultList.add(predict.getPlaceId());
                            CharSequence cs = predict.getFullText(new CharacterStyle() {
                                @Override
                                public void updateDrawState(TextPaint tp) {

                                }
                            });
                            Log.i(".....", cs.toString());
                        }

                        predictions.release();

//                      mLikelyPlaceNames = new ArrayList<>();
//                      mLikelyPlaceAddresses = new ArrayList<>();
//                      mLikelyPlaceAttributions = new ArrayList<>();
//                      mLikelyPlaceLatLngs = new ArrayList<>();
                        final int listSize = resultList.size();
                        for (int i =0; i < listSize; i++) {
                            final int count = i;
                            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(resultList.get(i));
                            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
                                @Override
                                public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                                    if (task.isSuccessful() && task.getResult() != null) {
                                        PlaceBufferResponse result = task.getResult();
                                        Place place = result.get(0);
                                        Log.i("placeName", (String) place.getName());
                                        Log.i("placeAddr", (String) place.getAddress());

                                        if (!(place.getId().equals(mLikelyPlaceId.get(0)))) {
                                            mLikelyPlaceNames.add((String) place.getName());
                                            mLikelyPlaceAddresses.add((String) place.getAddress());
                                            mLikelyPlaceAttributions.add((String) place.getAttributions());
                                            mLikelyPlaceLatLngs.add(place.getLatLng());
                                            mLikelyPlaceRatings.add(String.valueOf(place.getRating()));
                                            mLikelyPlaceId.add(place.getId());

                                        }
                                        result.release();
                                        if (count == listSize-1) {
                                            Log.i("PLACE COUNT", String.valueOf(mLikelyPlaceNames.size()));
                                            for (int j = 0; j < mLikelyPlaceNames.size(); j++) {
                                                addMarker(j);
                                            }
                                            setListLocation();
                                        }
                                    }

                                }
                            });
                            Log.i("place id", resultList.get(i));
                        }

                    } else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        }
    }


    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("daig", "begin");
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = mLikelyPlaceLatLngs.get(which);
                String markerSnippet = mLikelyPlaceAddresses.get(which);
                Log.i("daig", "almost begin");
                if (mLikelyPlaceAttributions.get(which) != null) {
                    markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions.get(which)
                            + "\n" + mLikelyPlaceRatings.get(which);
                }
                // Add a marker for the selected place, with an info window
                // showing information about that place.
                mMap.addMarker(new MarkerOptions()
                        .title(mLikelyPlaceNames.get(which))
                        .position(markerLatLng)
                        .snippet(markerSnippet));
                Log.i("daig", "mid");
                // Position the map's camera at the location of the marker.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        try {
            // Display the dialog.

            if (mLikelyPlaceNames.size() > 0) {
                CharSequence [] cs = mLikelyPlaceNames.toArray(new CharSequence[mLikelyPlaceNames.size()]);
                AlertDialog dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.pick_place)
                        .setItems(cs, listener)
                        .show();
            }
        }
        catch (Exception e)
        {

        }
        Log.i("daig", "end");
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     * Show where user location is at
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Add markers onto the map
     */
    private void addMarker(int index) {
        Log.i("index", String.valueOf(index));
        // The "which" argument contains the position of the selected item.
        LatLng markerLatLng = mLikelyPlaceLatLngs.get(index);
        String markerSnippet = mLikelyPlaceAddresses.get(index);
        if (mLikelyPlaceAttributions.get(index) != null) {
            markerSnippet = markerSnippet + "\n" + mLikelyPlaceAttributions.get(index)
                    + "\n" + mLikelyPlaceRatings.get(index);
        }
        BitmapDescriptor color;
        if (index == 0) {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        } else {
            color = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }

        // Add a marker for the selected place, with an info window
        // showing information about that place.
        mMap.addMarker(new MarkerOptions()
                .title(mLikelyPlaceNames.get(index))
                .position(markerLatLng)
                .snippet(markerSnippet)
                .icon(color));

        if (index == 0) {
            // Position the map's camera at the location of the marker.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                    DEFAULT_ZOOM));
        }
    }

    /**
     * Pass location information to LocationListFragment by callback
     */
    private void setListLocation() {
        if (mLikelyPlaceNames != null)
        {
            if (mLikelyPlaceNames.size() > 0) {
//                Intent intent = new Intent(this, LocationActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putStringArrayList(LOC_ID, (ArrayList<String>) mLikelyPlaceId);
                bundle.putStringArrayList(LOC_NAME, (ArrayList<String>) mLikelyPlaceNames);
                bundle.putStringArrayList(LOC_ADDR, (ArrayList<String>) mLikelyPlaceAddresses);
                bundle.putStringArrayList(LOC_ATTR, (ArrayList<String>) mLikelyPlaceAttributions);
                bundle.putParcelableArrayList(LOC_COORD, (ArrayList<LatLng>) mLikelyPlaceLatLngs);
                bundle.putStringArrayList(LOC_RATING, (ArrayList<String>) mLikelyPlaceRatings);

                mCallback.onLocationChange(bundle);

            } else {
//                Toast.makeText(getActivity(), "Search a location first!", Toast.LENGTH_SHORT).show();
                Snackbar.make(getActivity().findViewById(android.R.id.content), "Search a location first!", Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
//            Toast.makeText(getActivity(), "Search a location first!", Toast.LENGTH_SHORT).show();
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Search a location first!", Snackbar.LENGTH_SHORT)
                    .show();
        }

    }

}
