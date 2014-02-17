package codigo.labplc.mx.mitaxiusuario.googlemaps;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.drivers.TaxiDriverActivity;
import codigo.labplc.mx.mitaxiusuario.googlemaps.adapters.PlacesAutocompleteAdapter;
import codigo.labplc.mx.mitaxiusuario.googlemaps.dialogues.Dialogues;
import codigo.labplc.mx.mitaxiusuario.googlemaps.location.LocationUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * 
 * @author zace3d
 *
 */
public class MitaxiGoogleMapsActivity extends FragmentActivity implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,OnMapClickListener, OnMarkerDragListener,OnMyLocationButtonClickListener, 
OnItemClickListener {

	private String location;
	
	// A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Handles to UI widgets
    private AutoCompleteTextView actvOrigin;
    private AutoCompleteTextView actvDestination;
    private GoogleMap map;
    
    private Marker[] listOriginDestinationMarkers = new Marker[2];
    
    // Handle to SharedPreferences for this app
    private SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    private SharedPreferences.Editor mEditor;

    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    private boolean mUpdatesRequested = false;
	
    private Dialog progressDialog = null;
    
    ImageView  mitaxi_googlemapas_handle;
    SlidingDrawer  mitaxi_googlemapas_SlidingDrawer;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mitaxi_googlemaps_actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_googlemaps);

		
		initUI();
		
		TripPreferencesActivity tripPreferencesActivity = new TripPreferencesActivity(this);		
		
		
		
		SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.actions,android.R.layout.simple_spinner_dropdown_item);
		OnNavigationListener callback = new OnNavigationListener() {

		    String[] items = getResources().getStringArray(R.array.actions); // List items from res
		    @Override
		    public boolean onNavigationItemSelected(int position, long id) {
		        Log.d("NavigationItemSelected", items[position]); // Debug
		        return true;
		    }
		};

		// Action Bar
		ActionBar actions = getActionBar();
		actions.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actions.setDisplayShowTitleEnabled(false);
		actions.setListNavigationCallbacks(adapter, callback);
		
		
	}
	

	
	
	@SuppressWarnings("deprecation")
	public void initUI() {
        actvOrigin = (AutoCompleteTextView) findViewById(R.id.mitaxi_googlemaps_actv_origin);
        
        // Destino con Google Places
        actvDestination = (AutoCompleteTextView) findViewById(R.id.mitaxi_googlemaps_actv_destination);
        actvDestination.setAdapter(new PlacesAutocompleteAdapter(this, R.layout.places_list_item));
        actvDestination.setOnItemClickListener(this);
        
    //    findViewById(R.id.mitaxi_googlemaps_ibtn_mylocation).setOnClickListener(this);
      //  findViewById(R.id.mitaxi_googlemaps_btn_more).setOnClickListener(this);
        
		// Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);
        
        setUpMapIfNeeded();
        
        // Muestra diÃ¡logo en lo que encuentra coordenadas y pinta en el mapa
        progressDialog = Dialogues.ProgressCircleDialog(this);
        progressDialog.show();
        
       
        mitaxi_googlemapas_handle = (ImageView)findViewById(R.id.handle);
        mitaxi_googlemapas_SlidingDrawer =(SlidingDrawer) findViewById(R.id.mitaxi_googlemapas_SlidingDrawer);
        mitaxi_googlemapas_SlidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				mitaxi_googlemapas_handle.setImageResource(R.drawable.tab_close);
				
			}
		});
        mitaxi_googlemapas_SlidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			
			@Override
			public void onDrawerClosed() {
				mitaxi_googlemapas_handle.setImageResource(R.drawable.tab_open);
				
			}
		});
        
        
	}

	/*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        // If the client is connected
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();

        super.onStop();
    }
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        // Save the current setting for updates
        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();

        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();

        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);

        // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }
	
    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.resolved));
                        
                        // Display the result
                        //mConnectionState.setText(R.string.connected);
                        //mConnectionStatus.setText(R.string.resolved);
                    break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));

                        // Display the result
                        //mConnectionState.setText(R.string.disconnected);
                        //mConnectionStatus.setText(R.string.no_resolution);

                    break;
                }

            // If any other request code was received
            default:
               // Report that this Activity received an unknown requestCode
               Log.d(LocationUtils.APPTAG,
                       getString(R.string.unknown_activity_request_code, requestCode));

               break;
        }
    }

    private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mitaxi_googlemaps_map)).getMap();
			if (map != null) {
				if(setUpMap()) {
					initMap();
				}
			}
		}
	}
	
	public void initMap() {
		map.setMyLocationEnabled(true);
		map.setBuildingsEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
		map.getUiSettings().setCompassEnabled(true); //COMPASS
		map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
		map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
		map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
		map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
		//map.setPadding(10, 100, 20, 50);
		
		//map.setOnMyLocationButtonClickListener(this);
		map.setOnMapClickListener(this);
		//map.setOnMapLongClickListener(this);
		map.setOnMarkerDragListener(this);
	}
	
	public boolean setUpMap() {
		if (!checkReady()) {
            return false;
        } else {
        	return true;
        }
	}
    
	private boolean checkReady() {
        if (map == null) {
            Dialogues.Log(getApplicationContext(), getString(R.string.map_not_ready), Log.INFO);
            return false;
        }
        return true;
    }
	
/*	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.mitaxi_googlemaps_ibtn_mylocation:
			progressDialog.show();
			(new MitaxiGoogleMapsActivity.GetAddressTask(this)).execute(getLocation());
			
			break;
		case R.id.mitaxi_googlemaps_btn_more:
			Intent intent = new Intent(MitaxiGoogleMapsActivity.this, TripPreferencesActivity.class);
			startActivity(intent);
			overridePendingTransition(0, 0);
			break;
		}
	}*/
    
    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    }

    /**
     * Invoked by the "Get Location" button.
     *
     * Calls getLastLocation() to get the current location
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public String getFormatLocation() {

        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            return LocationUtils.getLatLng(this, currentLocation);
            // Display the current location in the UI
            //mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
        }
		return null;
    }
    
    public Location getLocation() {

        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            return mLocationClient.getLastLocation();
        }
		return null;
    }

    /**
     * Invoked by the "Get Address" button.
     * Get the address of the current location, using reverse geocoding. This only works if
     * a geocoding service is available.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    // For Eclipse with ADT, suppress warnings about Geocoder.isPresent()
    @SuppressLint("NewApi")
    public void getAddress() {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
            //mActivityIndicator.setVisibility(View.VISIBLE);

            // Start the background task
            (new MitaxiGoogleMapsActivity.GetAddressTask(this)).execute(currentLocation);
        }
    }

    /**
     * Invoked by the "Start Updates" button
     * Sends a request to start location updates
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void startUpdates(View v) {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    /**
     * Invoked by the "Stop Updates" button
     * Sends a request to remove location updates
     * request them.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void stopUpdates(View v) {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        //mConnectionStatus.setText(R.string.connected);
    	
    	Dialogues.Log(getApplicationContext(), "onConnected", Log.WARN);
    	
    	 location = getFormatLocation();
        Dialogues.Toast(getApplicationContext(), location, Toast.LENGTH_LONG);
        
        if(location != null) {
        	getAddress();
        }
    	
        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
    	Dialogues.Log(getApplicationContext(), "onDisconnected", Log.WARN);
    	
        //mConnectionStatus.setText(R.string.disconnected);
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
    
    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {

        // Report to the UI that the location was updated
        //mConnectionStatus.setText(R.string.location_updated);

        // In the UI, set the latitude and longitude to the value received
        //mLatLng.setText(LocationUtils.getLatLng(this, location));
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        //mConnectionState.setText(R.string.location_requested);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
        //mConnectionState.setText(R.string.location_updates_stopped);
    }

    /**
     * An AsyncTask that calls getFromLocation() in the background.
     * The class uses the following generic types:
     * Location - A {@link android.location.Location} object containing the current location,
     *            passed as the input parameter to doInBackground()
     * Void     - indicates that progress units are not used by this subclass
     * String   - An address passed to onPostExecute()
     */
    protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;
		private Location location;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
            location = params[0];

            // Create a list to contain the result address
            List <Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
                } catch (IOException exception1) {

                    // Log an error and return an error message
                    Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));

                    // print the stack trace
                    exception1.printStackTrace();

                    // Return an error message
                    return (getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
                } catch (IllegalArgumentException exception2) {

                    // Construct a message containing the invalid arguments
                    String errorString = getString(
                            R.string.illegal_argument_exception,
                            location.getLatitude(),
                            location.getLongitude()
                    );
                    // Log the error and print the stack trace
                    Log.e(LocationUtils.APPTAG, errorString);
                    exception2.printStackTrace();

                    //
                    return errorString;
                }
                // If the reverse geocode returned an address
                if (addresses != null && addresses.size() > 0) {

                    // Get the first address
                    Address address = addresses.get(0);

                    // Format the first line of address
                    String addressText = getString(R.string.address_output_string,

                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ?
                                    address.getAddressLine(0) : "",

                            // Locality is usually a city
                            address.getLocality(),

                            // The country of the address
                            address.getCountryName()
                    );

                    // Return the text
                    return addressText;

                // If there aren't any addresses, post a message
                } else {
                  return getString(R.string.no_address_found);
                }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(final String address) {
        	
			new CountDownTimer(2000, 1000) {
				public void onTick(long millisUntilFinished) {}
				
				public void onFinish() {
					if(progressDialog != null)
		        		progressDialog.dismiss();
					
					actvOrigin.setText(address);
		        	
		        	centerInMyCurrentUserLocation(location);
				}
			}.start();
        	
            // Turn off the progress bar
            //mActivityIndicator.setVisibility(View.GONE);

            // Set the address in the UI
            //mAddress.setText(address);
        }
    }
    
    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode,
            this,
            LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
  
	public Marker addMarkerToMap(GoogleMap map, LatLng point, String title, boolean dragable, String snippet, BitmapDescriptor icon) {
		Marker marker = map.addMarker(new MarkerOptions()
				.position(point)
				.title(title)
				.draggable(dragable)
				.icon(icon)
				.snippet(snippet)
				//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).flat(true)
				.alpha(0.7f)
				.anchor(0.5F, 0.5F)
				.rotation(0.0F));

		marker.showInfoWindow();
		
		return marker;
	}

	@Override
	public void onMarkerDrag(Marker marker) {}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		listOriginDestinationMarkers[0] = marker;
		progressDialog.show();
		(new MitaxiGoogleMapsActivity.GetAddressTask(this)).execute(getLocationFromLatLng(marker.getPosition()));
	}
	
	@Override
	public void onMarkerDragStart(Marker marker) {}

	@Override
    public boolean onMyLocationButtonClick() {
		progressDialog.show();
		(new MitaxiGoogleMapsActivity.GetAddressTask(this)).execute(getLocation());
        
        return false;
    }
	
	public void centerInMyCurrentUserLocation(Location location) {
	    if (location != null) {
	    	LatLng latlng = getLatLngFromLocation(location);
	    	//tv_information.setVisibility(View.VISIBLE);
			//tv_information.setText(getString(R.string.pickuporigindestiny_tv_infdestiny_text));
	    	
	    	if(listOriginDestinationMarkers[0] == null) {
				listOriginDestinationMarkers[0] = addMarkerToMap(
						map,
						latlng,
						"Origen",
						true,
						"Aqu’ inicia el viaje!", 
						BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
	    	} else {
	    		listOriginDestinationMarkers[0].setPosition(latlng);
	    	}
	    	
	    	map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, map.getMaxZoomLevel() - 4));
	    }
	}
	
	public LatLng getLatLngFromLocation(Location location) {
		return new LatLng(location.getLatitude(), location.getLongitude());
	}
	
	public Location getLocationFromLatLng(LatLng latLng) {
		Location location = new Location(LocationManager.GPS_PROVIDER);
		location.setLatitude(latLng.latitude);
		location.setLongitude(latLng.longitude);
		
		return location;
	}

	@Override
	public void onMapClick(LatLng point) {
		//map.animateCamera(CameraUpdateFactory.newLatLng(point));
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		String str = (String) adapterView.getItemAtPosition(position);
		Dialogues.Toast(getApplicationContext(), str, Toast.LENGTH_SHORT);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_buscar:
				progressDialog.show();
				(new MitaxiGoogleMapsActivity.GetAddressTask(this)).execute(getLocation());
	            return true;
	        case R.id.action_mas:

	        	Intent intent = new Intent(MitaxiGoogleMapsActivity.this,TaxiDriverActivity.class);
	        	intent.putExtra("location", location);
	        //	actvDestination
	        	intent.putExtra("pasajeros", TripPreferencesActivity.PASAJEROS);
	        	intent.putExtra("libre", TripPreferencesActivity.libre);
	        	intent.putExtra("sitio", TripPreferencesActivity.sitio);
	        	intent.putExtra("radio", TripPreferencesActivity.radio);
	        	intent.putExtra("rosa", TripPreferencesActivity.rosa);
	        	intent.putExtra("discapacitados", TripPreferencesActivity.discapacitados);
	        	intent.putExtra("bicicleta", TripPreferencesActivity.bicicleta);
	        	intent.putExtra("mascotas", TripPreferencesActivity.mascotas);
	        	intent.putExtra("ingles", TripPreferencesActivity.ingles);
	        	startActivity(intent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	
}