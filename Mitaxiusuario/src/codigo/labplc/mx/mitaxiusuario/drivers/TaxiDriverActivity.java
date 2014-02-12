package codigo.labplc.mx.mitaxiusuario.drivers;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.drivers.adapters.TaxiDriverFragmentPagerAdapter;
import codigo.labplc.mx.mitaxiusuario.drivers.animtrans.ZoomOutPageTransformer;
import codigo.labplc.mx.mitaxiusuario.drivers.beans.TaxiDriver;

public class TaxiDriverActivity extends FragmentActivity {
	//private int NUM_PAGES = 0;
	
	private ViewPager mPager;
	private TaxiDriverFragmentPagerAdapter mPagerAdapter;
	private String location;
	
	private ArrayList<TaxiDriver> listTaxiDrivers = new ArrayList<TaxiDriver>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_taxidriver);
		 Bundle bundle = getIntent().getExtras();
		 location =  bundle.getString("location");
		
		Toast.makeText(getBaseContext(), location+"", Toast.LENGTH_LONG).show();
		
		initUI();
	}

	/**
	 * Crea la interfaz gráfica con sus propiedades
	 */
	public void initUI() {
		mPager = (ViewPager) findViewById(R.id.taxidriver_pager);
		
		//NUM_PAGES = listTaxiDrivers.size();
		
		mPagerAdapter = new TaxiDriverFragmentPagerAdapter(getSupportFragmentManager());
		
		try {
			this.addListElementsTaxiDrivers();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int count = 0;
		for(TaxiDriver taxidriver : listTaxiDrivers) {
			mPagerAdapter.addFragment(TaxiDriverPageFragment.newInstance(taxidriver, count++));
		}
		
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
	
	/**
	 * Agrega elementos a la lista de objetos tipo {@link TaxiDriver}
	 * @throws JSONException 
	 */
	public void addListElementsTaxiDrivers() throws JSONException {
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
		String uuid = prefs.getString("uuid", null);
		//arreglamos las coordenadas
		location  = location.replaceFirst(",", ".");
		location  = location.replaceFirst(", ", "@");
		location  = location.replaceFirst(",", ".");
		String slocation[] = location.split("@");
		
		String consulta = "http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getlogin&pk="+uuid+"&lat="+slocation[0]+"&lng="+slocation[1]+"&discapacitados=TRUE&bicicleta=TRUE&mascotas=FALSE&tipo=Sitio";
		String querty = doHttpConnection(consulta);
		//Log.d("**************", querty+"");
	   
		
		  JSONObject json= (JSONObject) new JSONTokener(querty).nextValue();
	      JSONObject json2 = json.getJSONObject("Taxi");
	      JSONObject jsonResponse = new JSONObject(json2.toString());
	      JSONArray cast = jsonResponse.getJSONArray("concesion");
	      for (int i=0; i<cast.length(); i++) {
	          	JSONObject oneObject = cast.getJSONObject(i);
				 try {
					/* marca = (String) oneObject.getString("marca");
					 autoBean.setMarca(marca);
					 submarca = (String)  oneObject.getString("submarca");
					 autoBean.setSubmarca(submarca);
					 anio = (String)  oneObject.getString("anio");
					 autoBean.setAnio(anio);*/
				 } catch (Exception e) { }//return false;}
	      }
		listTaxiDrivers.clear();
		
		
		
		
		listTaxiDrivers.add(new TaxiDriver("Enrique" , "López", "L32154", "2014-05-03", "1999-06-10", 5, "A12323", "Chevrolet Chevy 2010", 5, 1));
		listTaxiDrivers.add(new TaxiDriver("Edgar" , "Zavala", "A0987", "2014-05-03", "1999-06-10", 1, "L32154", "Nissan Tsuru 2013", 1, 2));
		listTaxiDrivers.add(new TaxiDriver("Miguel" , "Morán", "A3456", "2014-05-03", "1999-06-10", 1, "L32154", "Nissan Tsuru 2013", 1, 2));
		listTaxiDrivers.add(new TaxiDriver("Ruu" , "Rámirez", "aaa111", "2014-05-03", "1999-06-10", 1, "L32154", "Nissan Tsuru 2013", 1, 2));
	}
	
	/**
	 * {@link onBackPressed}
	 */
	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
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

}
