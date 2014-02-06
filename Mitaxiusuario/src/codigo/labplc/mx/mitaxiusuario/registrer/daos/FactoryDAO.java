package codigo.labplc.mx.mitaxiusuario.registrer.daos;

public class FactoryDAO {
	
	public static UserDAO getUserDAOImp(String url) {
		return new UserDAOImp(url);
	}
}
