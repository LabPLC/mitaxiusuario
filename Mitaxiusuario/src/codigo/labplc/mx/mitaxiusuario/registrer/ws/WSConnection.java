package codigo.labplc.mx.mitaxiusuario.registrer.ws;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.registrer.beans.User;

public class WSConnection {
	
	public static String buildUrlSaveUser(Context context, User user) {
		return context.getString(R.string.webservice_host) + 
				context.getString(R.string.webservice_file) + "?act=pasajero&type=add"
				+"&nombre="+ user.getName()
				+ "&mail="	+ user.getEmail()
				+ "&tel="+ user.getTeluser()
				+ "&telmer=" + user.getTelemergency()
				+ "&mailmer="+ user.getMailenergency()
				+ "&so=" + user.getOs();
	}
	
	
	public static String doHttpConnection(String url) {
		HttpClient Client = new DefaultHttpClient();
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			HttpGet httpget = new HttpGet(url);
			HttpResponse hhrpResponse = Client.execute(httpget);
			HttpEntity httpentiti = hhrpResponse.getEntity();
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