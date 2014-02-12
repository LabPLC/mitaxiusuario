package codigo.labplc.mx.mitaxiusuario;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import codigo.labplc.mx.mitaxiusuario.googlemaps.MitaxiGoogleMapsActivity;
import codigo.labplc.mx.mitaxiusuario.registrer.GooglePlusSignInActivity;
import codigo.labplc.mx.mitaxiusuario.registrer.MitaxiRegisterManuallyActivity;

/**
 *Clase que controla si el usuario ya esta registrado, sino lo registra 
 * 
 * @author mikesaurio
 *
 */
public class MitaxiActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
				//solicitamos las preferencias del usuario para saber si esta registrado
				SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
				String uuid = prefs.getString("uuid", null);
				if(uuid == null){//si aun no tiene datos guardados en preferencias lo registramos
					setContentView(R.layout.activity_mitaxi_register);
					initUI();
				}else{//se llama directo a la busqueda de taxista porque ya esta registrado
					Intent intent = new Intent(MitaxiActivity.this,MitaxiGoogleMapsActivity.class);
					startActivity(intent);
					this.finish();
				}
		

	}
	
	/*
	 * Metodo que asigna los escuchas y las instancias a la vista del registro
	 */
	public void initUI() {
		findViewById(R.id.mitaxi_register_btn_manually).setOnClickListener(this);
		findViewById(R.id.mitaxi_register_ibtn_googleplus).setOnClickListener(this);
	}
	
	

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.mitaxi_register_btn_manually://btn para dar el alta manualmente
				Intent intentManually = new Intent(MitaxiActivity.this, MitaxiRegisterManuallyActivity.class);
				startActivity(intentManually);
				overridePendingTransition(0,0);
				this.finish();
			break;
			
			case R.id.mitaxi_register_ibtn_googleplus://btn para dar de alta por medio de g+
				Intent intentGooglePlus = new Intent(MitaxiActivity.this, GooglePlusSignInActivity.class);
				startActivity(intentGooglePlus);
				overridePendingTransition(0,0);
				this.finish();
			break;
		}
	}
}