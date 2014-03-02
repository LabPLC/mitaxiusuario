package codigo.labplc.mx.mitaxiusuario.trip;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.telephony.gsm.SmsManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.panicbutton.OpenHelp;
import codigo.labplc.mx.mitaxiusuario.registrer.dialogues.Dialogos;
import codigo.labplc.mx.mitaxiusuario.trip.beans.TaxiDriver;
import codigo.labplc.mx.mitaxiusuario.trip.dialogues.Dialogues;
import codigo.labplc.mx.mitaxiusuario.trip.location.AnimationFactory;
import codigo.labplc.mx.mitaxiusuario.trip.location.GMapV2Direction;
import codigo.labplc.mx.mitaxiusuario.trip.location.LocationUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * 
 * @author zace3d
 * 
 */
public class MitaxiTripActivity extends LocationActivity implements OnClickListener {
	private GoogleMap map;

	private ArrayList<LatLng> listTaxiRoute = new ArrayList<LatLng>();
	
	private LatLng userPosition = null,userPositionDestino=null;
	private LatLng taxiPosition = null;

	private GMapV2Direction googleMapsDirections = new GMapV2Direction();

	private TaxiDriver taxiDriver;
	private String pk_viaje;
	private String pk_chofer;
	private String placa;
	private String origen="";
	private String destino;
	private String nombre,appat,apmat,marca,submarca,anio,foto,tipo;
	private String tiempo= "0",distancia="0";
	private String titulo ="Mi ubicación";
	private boolean flagLlegoTaxi= false;
	Intent intentServicePanic;
	private LatLng latLng;
	
	
	 MyResultReceiver resultReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_trip);
		
		
		Bundle bundle = getIntent().getExtras();
		
		pk_viaje = bundle.getString("pk_viaje");	
		pk_chofer = bundle.getString("pk_chofer");
		placa = bundle.getString("placa");
		origen = bundle.getString("origen");
		destino = bundle.getString("destino");
		
		
		resultReceiver = new MyResultReceiver(null);
		 
		intentServicePanic= new Intent(this, OpenHelp.class);
		intentServicePanic.putExtra("receiver", resultReceiver);
		startService(intentServicePanic);
		
		Log.d("pk_viaje", pk_viaje);
		Log.d("pk_chofer", pk_chofer);
		Log.d("placa", placa);
		Log.d("origen", origen);
		Log.d("destino", destino);
		
		//traemos las direcciones, distancia y tiempo
				placa = placa.replaceAll(" ", "");
		
				origen  = origen.replaceAll("[()]", "");
				String sorigen[] = origen.split(",");
				
				destino  = destino.replaceAll("[()]", "");
				String sdestino[] = destino.split(",");
				
				userPosition = new LatLng(Double.parseDouble(sorigen[0]),Double.parseDouble(sorigen[1]));
				userPositionDestino = new LatLng(Double.parseDouble(sdestino[0]),Double.parseDouble(sdestino[1]));
		
				String consulta ="http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getchoferloginpk&pk_chofer="+pk_chofer+"&placa="+placa;
				
				String querty = doHttpConnection(consulta);
			    
			try{
				  JSONObject json= (JSONObject) new JSONTokener(querty).nextValue();
			      JSONObject json2 = json.getJSONObject("message");
			      JSONObject jsonResponse = new JSONObject(json2.toString());
			      JSONArray cast = jsonResponse.getJSONArray("chofer");
			      if(cast.length()<=0){
				    	 Toast.makeText(getBaseContext(), "No existen choferes con esas caracteristicas por ahora", Toast.LENGTH_LONG).show(); 
			      }

			      for (int i=0; i<cast.length(); i++) {
			          	JSONObject oneObject = cast.getJSONObject(i);
			          	
			        tipo = 	oneObject.getString("tipo_taxi");
			        nombre= 	oneObject.getString("nombre");
			        appat=	oneObject.getString("apellido_paterno");
			        apmat = 	oneObject.getString("apellido_materno");
			        foto = 	oneObject.getString("foto");
			        marca =	oneObject.getString("marca");
			        submarca =	oneObject.getString("submarca");
			        anio = 	oneObject.getString("anio");
			      }
		
		taxiDriver = new TaxiDriver(nombre,appat+" "+apmat,foto,placa,marca+" "+submarca+" "+anio,tipo);
			}catch(Exception e){
				
			}
			

		
		this.initUI();
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(TaxiRouteService.PATH));
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
	}

	/**
	 * Broadcast to receive the list of founded locations from the service and paint the direction into the map
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@SuppressWarnings("unchecked")
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				listTaxiRoute = (ArrayList<LatLng>) bundle.getSerializable(TaxiRouteService.ID_LIST_TAXI_ROUTE);

				if (listTaxiRoute != null && listTaxiRoute.size() > 0) {
					latLng = listTaxiRoute.get(listTaxiRoute.size() - 1);

					map.clear();
					drawRouteBetweenTwoPositions(latLng, userPosition);
				}
			}
		}
	};

	/**
	 * Create the interface of the taxi driver route
	 */
	public void initUI() {
		// Imagen del taxista
        ImageView ivDriverphoto = (ImageView) findViewById(R.id.mitaxi_trip_iv_driverpicture);
        String consulta ="http://codigo.labplc.mx/~mikesaurio/picsDriver/"+foto;
        ivDriverphoto.setImageBitmap(getBitmapFromURL(consulta));
        
        
        // Nombre y apellido del taxista
        TextView tvDrivername = (TextView) findViewById(R.id.mitaxi_trip_tv_drivername);
        tvDrivername.setText(getString(R.string.mitaxi_trip_tv_drivername, taxiDriver.getName() + " " + taxiDriver.getLastName()));
        
        // Placa del auto que usa el taxista
        TextView tvDrivertaxiplaca = (TextView) findViewById(R.id.mitaxi_trip_tv_drivertaxiid);
        tvDrivertaxiplaca.setText(getString(R.string.mitaxi_trip_tv_drivertaxiid, taxiDriver.getPlaca()));
        
        // Modelo del auto que usa el taxista
        TextView tvDrivertaximodel = (TextView) findViewById(R.id.mitaxi_trip_tv_drivertaximodel);
        tvDrivertaximodel.setText(getString(R.string.mitaxi_trip_tv_drivertaximodel, taxiDriver.getTaxiModelCar()));
      
       /* TextView tvDrivertaxitipo = (TextView) findViewById(R.id.mitaxi_trip_tv_drivertaxitipo);
        tvDrivertaxiplaca.setText("Tipo: "+taxiDriver.getTipo());*/
       

		
        // Taxista err√≥neo
        TextView tvWrongDriver = (TextView) findViewById(R.id.mitaxi_trip_tv_wrongdriver);
        tvWrongDriver.setText(getString(R.string.mitaxi_trip_tv_wrongdriver));
        tvWrongDriver.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString spannableString = new SpannableString(tvWrongDriver.getText().toString());
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
            	new Dialogos().mostrarReportaTaxi(MitaxiTripActivity.this).show();
            }
        };
        spannableString.setSpan(span, 0, 17, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tvWrongDriver.setText(spannableString);
        
        // Driver position button
        findViewById(R.id.mitaxi_trip_btn_driverPosition).setOnClickListener(this);
        
        // Start Trip button
    final   Button mitaxi_trip_btn_starttrip =(Button) findViewById(R.id.mitaxi_trip_btn_starttrip); //.setOnClickListener(this);
       mitaxi_trip_btn_starttrip.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(!flagLlegoTaxi){
					flagLlegoTaxi = true;
					mitaxi_trip_btn_starttrip.setText("Llegué al destino");
					titulo = "Mi destino";
					drawRouteBetweenTwoPositions(userPosition, userPositionDestino);
					userPosition = userPositionDestino;
			}else{
				stopService(intentServicePanic);
				new Dialogos().mostrarEvaluarServicio(MitaxiTripActivity.this,pk_chofer,pk_viaje).show();
			}
		}
	}); 
       
       
       
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mitaxi_trip_map)).getMap();
			if (map != null) {
				if (setUpMap()) {
					initMap();
				}
			}
		}
	}

	public void initMap() {
		map.setMyLocationEnabled(true);
		map.setBuildingsEnabled(true);
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		map.getUiSettings().setZoomControlsEnabled(true); // ZOOM
		map.getUiSettings().setCompassEnabled(true); // COMPASS
		map.getUiSettings().setZoomGesturesEnabled(true); // GESTURES ZOOM
		map.getUiSettings().setRotateGesturesEnabled(true); // ROTATE GESTURES
		map.getUiSettings().setScrollGesturesEnabled(true); // SCROLL GESTURES
		map.getUiSettings().setTiltGesturesEnabled(true); // TILT GESTURES
		// map.setPadding(10, 100, 20, 50);

		map.setOnMyLocationButtonClickListener(this);
		map.setOnMapClickListener(this);
		map.setOnMarkerClickListener(this);
		// map.setOnMapLongClickListener(this);
		map.setOnMarkerDragListener(this);

		// Start the service to obtain the last taxi driver locations
		this.startService();
	}

	/**
	 * Add a marker to the map with the route time aprox
	 * 
	 * @param time
	 * @param latLng
	 */
	public void addMarkerIntoMap(String time, LatLng latLng) {
		
		LocationUtils.addMarker(map, latLng, "Taxi", false, "A: " + distancia+" aproximadamente: " + tiempo,
				BitmapDescriptorFactory.fromResource(R.drawable.mi_taxi_assets_taxi_on));

		this.taxiPosition = latLng;
		
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, map.getMaxZoomLevel() - 5));
	}

	/**
	 * Start drawing a PolyLine between two points
	 * 
	 * @param startPosition
	 * @param endPosition
	 */
	public void drawRouteBetweenTwoPositions(LatLng startPosition, LatLng endPosition) {
		String consulta2 = "http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getGoogleData&lato="
				+startPosition.latitude+"&lngo="+startPosition.longitude
				+"&latd="+endPosition.latitude+"&lngd="+endPosition.longitude+"&filtro=velocidad";
		String querty2 = doHttpConnection(consulta2);
		querty2= querty2.replaceAll("\"","");
		String[] Squerty2 = querty2.split(",");
		tiempo = Squerty2[0];
		distancia =Squerty2[1];
		
		MarkerOptions marker = new MarkerOptions().position(endPosition).title(titulo);
		marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
		map.addMarker(marker);
		googleMapsDirections.getDocument(this, startPosition, endPosition, GMapV2Direction.MODE_DRIVING);
	}

	/**
	 * Get a list with the direction and draw it into the map
	 * 
	 * @param doc
	 */
	public void drawRouteOnMap(Document doc) {
		ArrayList<LatLng> directionPoint = googleMapsDirections.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(8).color(Color.BLUE);

		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}

		map.addPolyline(rectLine);
	}

	
	
	
	/**
	 * Start the service to obtain the last taxi driver locations
	 */
	public void startService() {
		Intent intent = new Intent(getBaseContext(), TaxiRouteService.class);
		intent.putExtra("pk_chofer", pk_chofer);
		startService(intent);
	}

	/**
	 * Set up the map
	 * 
	 * @return true if map is set up; false otherwise
	 */
	public boolean setUpMap() {
		if (!checkReady()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 
	 * 
	 * @return true if map is ready; false otherwise
	 */
	private boolean checkReady() {
		if (map == null) {
			Dialogues.Log(getApplicationContext(), getString(R.string.map_not_ready), Log.INFO);
			return false;
		}
		return true;
	}

	/**
	 * Get last Location known
	 * 
	 * @return a Location object
	 */
	public Location getLocation() {
		// If Google Play Services is available
		if (servicesConnected()) {

			// Get the current location
			return mLocationClient.getLastLocation();
		}
		return null;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if(findViewById(R.id.mitaxi_trip_ll_driverinfo).getVisibility() == View.GONE)
			AnimationFactory.fadeIn(this, findViewById(R.id.mitaxi_trip_ll_driverinfo));
		
		return false;
	}
	
	@Override
	public void onMapClick(LatLng point) {
		if(findViewById(R.id.mitaxi_trip_ll_driverinfo).getVisibility() == View.VISIBLE)
			AnimationFactory.fadeOut(this, findViewById(R.id.mitaxi_trip_ll_driverinfo));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mitaxi_trip, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.mitaxi_trip_action_settings:
	        	new Dialogos().mostrarCostosTaxi(MitaxiTripActivity.this).show();  	
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.mitaxi_trip_btn_driverPosition:
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(taxiPosition, map.getMaxZoomLevel() - 5));
				break;
				
			/*	
			case R.id.mitaxi_trip_btn_starttrip:
				titulo = "Mi destino";
				drawRouteBetweenTwoPositions(userPosition, userPositionDestino);
				userPosition = userPositionDestino;
				break;*/
		}
	}
	
	/**
	 * metodo que hace la conexion al servidor con una url especifica
	 * @param url(String) ruta del web service
	 * @return (String) resultado del service
	 */
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
			//Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
			return EntityUtils.toString(httpentiti);
		} catch (ParseException e) {
			Log.d("Error ParseEception", e.getMessage() + "");
			return null;
		} catch (IOException e) {
			Log.d("Error IOException", e.getMessage() + "");
			return null;
		}
	}	
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        Log.e("src",src);
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
		    Matrix mat = new Matrix();
	        mat.postRotate(-90);
	        Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0, myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
	        Log.e("Bitmap","returned");
	        return bMapRotate;
	    } catch (IOException e) {
	        e.printStackTrace();
	        Log.e("Exception",e.getMessage());
	        return null;
	    }
	}
	

	class MyResultReceiver extends ResultReceiver
	 {
	  public MyResultReceiver(Handler handler) {
	   super(handler);
	  }

	  @Override
	  protected void onReceiveResult(int resultCode, Bundle resultData) {
		  
			SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
			String emer = prefs.getString("emer", null);
			String mensaje =  "Me encuentro en peligro en el taxi "+placa +
		  			" chofer " +nombre+" "+appat+" "+apmat+" ubicación: "+latLng+" bateria: "+resultData.getString("panic")+"";
		  	Log.d("********mensaje", mensaje+"");
		  	 sendSMS(emer, mensaje);
	  }
	  
	//---sends an SMS message to another device---

	   private void sendSMS(String phoneNumber, String message)
	   {
	       SmsManager sms = SmsManager.getDefault();
	       sms.sendTextMessage(phoneNumber, null, message, null, null);
	    }
	 }
	
}