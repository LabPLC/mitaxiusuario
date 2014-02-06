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
	
	/*public class DoHttpConnectionAsyncTask extends AsyncTask<String, Boolean, String> {
		private String url;
		
		public DoHttpConnectionAsyncTask(String url) {
			this.url = url;
		}
		
		@Override
        protected void onPreExecute() {}
		
		@Override
        protected Boolean doInBackground(String... params) {
			String url = params[0];
			
        	HttpClient Client = new DefaultHttpClient();
    		try {
    			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
    					.permitAll().build();
    			StrictMode.setThreadPolicy(policy);
    			HttpGet httpget = new HttpGet(url);
    			HttpResponse hhrpResponse = Client.execute(httpget);
    			HttpEntity httpentiti = hhrpResponse.getEntity();
    			Log.d("RETURN HTTPCLIENT", EntityUtils.toString(httpentiti));
    			return true;
    		} catch (ParseException e) {
    			Log.d("Error ParseEception", e.getMessage() + "");
    			return false;
    		} catch (IOException e) {
    			Log.d("Error IOException", e.getMessage() + "");
    			return false;
    		}
        }        
		
		@Override
        protected void onProgressUpdate(Boolean values) {}
		
        @Override
        protected void onPostExecute(String result) {}
    }*/
	
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