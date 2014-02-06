package codigo.labplc.mx.mitaxiusuario.registrer.daos;

import codigo.labplc.mx.mitaxiusuario.registrer.beans.User;
import codigo.labplc.mx.mitaxiusuario.registrer.ws.WSConnection;


public class UserDAOImp implements UserDAO {
	private String url;
	
	public UserDAOImp(String url) {
		this.url = url;
	}
	
	@Override
	public String save(User user) {
		//new WSConnection.DoHttpConnectionAsyncTask(url).execute("url");
		
		return WSConnection.doHttpConnection(url);
	}
}
