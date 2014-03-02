package codigo.labplc.mx.mitaxiusuario.tracking;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.sax.StartElementListener;
import android.util.Log;
import codigo.labplc.mx.mitaxiusuario.trip.MitaxiTripActivity;

/**
 * 
 * @author mikesaurio
 *
 */
public class SocketConnection {
	/**
	 * Declaraci—n de variables
	 */
	SocketIO socket;//socket para la conecci—n con el servidor
	Context act;
	
	public SocketConnection(Context context) {
		this.act=context;
	}


	/**
	 * Metodo que crea la coneccion con el servior
	 * 
	 * @return SocketIO creado y conectado
	 */
	public SocketIO connection() {
		try {
			//cramos el socket
			socket = new SocketIO("http://codigo.labplc.mx:8009");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		//hacemos la conexi—n
		socket.connect(new IOCallback() {
			@Override
			public void onMessage(JSONObject json, IOAcknowledge ack) {
				try {
					System.out.println("Servidor dice:" + json.toString(2));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onMessage(String data, IOAcknowledge ack) {
				System.out.println("Servidor dice: " + data);
			}

			@Override
			public void onError(SocketIOException socketIOException) {
				System.out.println("ERROR en socket");
				socketIOException.printStackTrace();
			}

			@Override
			public void onDisconnect() {
				System.out.println("Conexi—n terminada");
			}

			@Override
			public void onConnect() {
				System.out.println("Conexi—n establicida");
			}

			@TargetApi(19)
			@Override
			public void on(String event, IOAcknowledge ack, Object... args) {
			//	System.out.println("Servidor de eventos activa '" + event + "'");
			//	System.out.println("Servidor de eventos activa Usuario" + args[0] + "'");
				try {
					if(!args[0].toString().equals("false")){
						  	JSONObject jsonObj = new JSONObject(args[0].toString());
			                String disposicion = jsonObj.getString("estado");
			                disposicion  = disposicion.replaceAll(" ", "");
			                String pk_viaje = jsonObj.getString("pk_viaje");
			                
			                String pk_chofer = jsonObj.getString("pk_chofer");
			                
			                String placa = jsonObj.getString("placa");
			                placa  = placa.replaceAll(" ", "");
			                
			                String origen = jsonObj.getString("origen");
			                disposicion  = disposicion.replaceAll(" ", "");
			                
			                String destino = jsonObj.getString("destino");
			                disposicion  = disposicion.replaceAll(" ", "");
			               
			                if(disposicion.equals("cancelado")){
			                	 System.out.println("*******" + "cancelado"+ "");
			                	 
			                }else if(disposicion.equals("aceptado")){
			                	 System.out.println("*******" + "aceptado"+ "");
			     				
			                	String consulta = "http://codigo.labplc.mx/~mikesaurio/taxi.php?act=viaje&type=setestatus&pk_viaje="+pk_viaje+"&estado=curso";
			    				String querty = doHttpConnection(consulta);
			                	
			    				Intent intent = new Intent(act,MitaxiTripActivity.class);
			    				intent.putExtra("pk_viaje", pk_viaje);
			    				intent.putExtra("pk_chofer", pk_chofer);
			    				intent.putExtra("placa", placa);
			    				intent.putExtra("origen", origen);
			    				intent.putExtra("destino", destino);
			                	 act.startActivity(intent);
			                
		                }else if(disposicion.equals("pendiente")){
		                	 System.out.println("*******" + "pendiente"+ "");

		                }
		              }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}    
				
			}
		});
		//regresamos el socket ya construido y conectado
		return socket;

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
