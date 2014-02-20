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
import codigo.labplc.mx.mitaxiusuario.drivers.beans.BeanChoferes;
import codigo.labplc.mx.mitaxiusuario.drivers.beans.TaxiDriver;
import codigo.labplc.mx.mitaxiusuario.googlemaps.TripPreferencesActivity;

public class TaxiDriverActivity extends FragmentActivity {
	//private int NUM_PAGES = 0;
	
	private ViewPager mPager;
	private TaxiDriverFragmentPagerAdapter mPagerAdapter;
	private String location,pasajeros;
	boolean libre,sitio,radio,rosa,discapacitados,bicicleta,mascotas,ingles;
	private ArrayList<BeanChoferes> beanTaxiDriver = new ArrayList<BeanChoferes>();
	
	private ArrayList<TaxiDriver> listTaxiDrivers = new ArrayList<TaxiDriver>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_taxidriver);
		 Bundle bundle = getIntent().getExtras();
		 location =  bundle.getString("location");
		 pasajeros =  bundle.getString("pasajeros");
		 libre =  bundle.getBoolean("libre");
		 sitio =  bundle.getBoolean("sitio");
		 radio =  bundle.getBoolean("radio");
		 rosa =  bundle.getBoolean("rosa");
		 discapacitados =  bundle.getBoolean("discapacitados");
		 bicicleta =  bundle.getBoolean("bicicleta");
		 mascotas =  bundle.getBoolean("mascotas");
		 ingles =  bundle.getBoolean("ingles");
		 
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
		Log.d("********************",  location+"");
		location  = location.replaceFirst(",", ".");
		location  = location.replaceFirst(". ", "@");
		location  = location.replaceFirst(",", ".");
		String slocation[] = location.split("@");
		
		Log.d("********************",  slocation[0]+"");
		
		String consulta = "http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getlogin&pk="+uuid+"&lat="+slocation[0]+"&lng="+slocation[1]+"&discapacitados="+discapacitados+"&bicicleta="+bicicleta+"&mascotas="+mascotas+"&libre="+libre+"&sitio="+sitio+"&radio="+radio+"&rosa="+rosa;
		//Log.d("********************", consulta+"");
		
		String querty = doHttpConnection(consulta);
	   
		
		  JSONObject json= (JSONObject) new JSONTokener(querty).nextValue();
	      JSONObject json2 = json.getJSONObject("message");
	      JSONObject jsonResponse = new JSONObject(json2.toString());
	      JSONArray cast = jsonResponse.getJSONArray("chofer");
	    //  Log.d("********************", cast.length()+"");
	      if(cast.length()<=0){
		    	 Toast.makeText(getBaseContext(), "No existen choferes con esas caracteristicas por ahora", Toast.LENGTH_LONG).show(); 
		    	 TaxiDriverActivity.this.finish();
	      }
	      listTaxiDrivers.clear();
	      beanTaxiDriver.clear();
	      for (int i=0; i<cast.length(); i++) {
	          	JSONObject oneObject = cast.getJSONObject(i);
	          	BeanChoferes td = new BeanChoferes();
	          

	          	     td.setPk_usuario(uuid);
					 td.setPk_chofer(oneObject.getString("pk_chofer"));
					 td.setPlaca(oneObject.getString("placa").replaceAll(" ", ""));
					 td.setLatitud(oneObject.getString("latitud"));
					 td.setLongitud(oneObject.getString("longitud"));
					 td.setNombre(oneObject.getString("nombre"));
					 td.setApellido_paterno(oneObject.getString("apellido_paterno"));
					 td.setApellido_materno(oneObject.getString("apellido_materno"));
					 td.setAntiguedad(oneObject.getString("antiguedad"));
					 td.setVigencia(oneObject.getString("vigencia"));
					 td.setTelefono(oneObject.getString("telefono"));
					 td.setLicencia(oneObject.getString("licencia"));
					 td.setRanking(oneObject.getString("ranking"));			
					 td.setMarca(oneObject.getString("marca"));
					 td.setSubmarca(oneObject.getString("submarca"));
					 td.setAnio(oneObject.getString("anio"));
					 td.setTipo_taxi(oneObject.getString("tipo_taxi"));			
					 td.setDiscapacitados(oneObject.getString("discapacitados"));
					 td.setBicicleta(oneObject.getString("bicicleta"));
					 td.setMascotas(oneObject.getString("mascotas"));
					 
				
				String consulta2 = "http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getGoogleData&lato="+slocation[0]+"&lngo="+slocation[1]+"&latd="+ td.getLongitud()+"&lngd="+ td.getLatitud()+"&filtro=velocidad";
				String querty2 = doHttpConnection(consulta2);
				querty2  = querty2.replaceAll("\"", "");
				String sdistance[] = querty2.split(",");	 
					 
				String placa  = td.getPlaca().replaceAll(" ", "");
				String consulta3 = "http://datos.labplc.mx/movilidad/vehiculos/"+placa+".json";
				String querty3 = doHttpConnection(consulta3);
				int infracciones = 0;
				
				JSONObject json3= (JSONObject) new JSONTokener(querty3).nextValue();
			      JSONObject json23 = json3.getJSONObject("consulta");
			      JSONObject jsonResponse3 = new JSONObject(json23.toString());
			      JSONArray cast3 = jsonResponse3.getJSONArray("infracciones");
			      for (int i3=0; i3<cast3.length(); i3++) {
			    	  infracciones+=1;
			      }
				
			beanTaxiDriver.add(td);
			listTaxiDrivers.add(new TaxiDriver(td.getNombre() , td.getApellido_paterno()+ " "+td.getApellido_materno() , 
					td.getLicencia(), td.getAntiguedad(), td.getVigencia(), 1,
					td.getPlaca(),td.getMarca()+" "+td.getSubmarca()+" " + td.getAnio(),infracciones+"",
					td.getTipo_taxi(),sdistance[0],sdistance[1],td.getPk_chofer(),td.getPk_usuario(),
					slocation[0]+","+slocation[1],
					slocation[0]+","+slocation[1],
					pasajeros,mascotas,discapacitados,bicicleta));
	     
			
	      }
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
