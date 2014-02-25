package codigo.labplc.mx.mitaxi.trip;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import codigo.labplc.mx.mitaxi.trip.dialogues.Dialogues;

import com.google.android.gms.maps.model.LatLng;

/**
 * 
 * @author zace3d
 * 
 */
public class TaxiRouteService extends Service {
	public static final String PATH = "codigo.labplc.mx.mitaxi.trip";
	public static final String ID_LIST_TAXI_ROUTE = "listTaxiRoute";
	
	private int TIME_UPDATE_IN_MILIS = 20000;
	
	private ArrayList<LatLng> listTaxiRoute = new ArrayList<LatLng>();

	private Handler handler = new Handler();

	private int index = 1;
	
	public void initListTaxiRoute() {
		listTaxiRoute.add(0, new LatLng(19.53328333, -99.16758333)); // 19° 31.997', -99° 10.055'
		listTaxiRoute.add(1, new LatLng(19.53898333, -99.16808333)); // 19° 32.339', -99° 10.085'
		listTaxiRoute.add(2, new LatLng(19.54571666, -99.17001666)); // 19° 32.743', -99° 10.201'
		listTaxiRoute.add(3, new LatLng(19.55033333, -99.16871666)); // 19° 33.020', -99° 10.123'
		listTaxiRoute.add(4, new LatLng(19.55548333, -99.16881666)); // 19° 33.329', -99° 10.129'
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//Dialogues.Toast(this, "onStartCommand Service", Toast.LENGTH_LONG);
		
		runnable.run();
		
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		//Dialogues.Toast(this, "onCreate Service", Toast.LENGTH_LONG);
		
		initListTaxiRoute();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		//Dialogues.Toast(this, " onStart Service", Toast.LENGTH_LONG);
	}

	@Override
	public void onDestroy() {
		Dialogues.Toast(this, "onDestroy Service", Toast.LENGTH_LONG);
		
		if(handler != null)
			handler.removeCallbacks(runnable);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			
			if(index <= listTaxiRoute.size()) {
				publishResults();
				
				handler.postDelayed(this, TIME_UPDATE_IN_MILIS);
			} else {
				if(handler != null)
					handler.removeCallbacks(runnable);
			}
		}
	};

	public ArrayList<LatLng> createList() {
		ArrayList<LatLng> listAux = new ArrayList<LatLng>();
		for (int i = 0; i < index; i++) {
			listAux.add(listTaxiRoute.get(i));
		}
		index++;
		
		return listAux;
	}
	
	private void publishResults() {
		Intent intent = new Intent(PATH);
		intent.putExtra(ID_LIST_TAXI_ROUTE, createList());
		sendBroadcast(intent);
	}
}