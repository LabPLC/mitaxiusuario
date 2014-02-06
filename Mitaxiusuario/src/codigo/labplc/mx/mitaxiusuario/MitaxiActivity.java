package codigo.labplc.mx.mitaxiusuario;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.googlemaps.MitaxiGoogleMapsActivity;
import codigo.labplc.mx.mitaxiusuario.registrer.GooglePlusSignInActivity;
import codigo.labplc.mx.mitaxiusuario.registrer.MitaxiRegisterManuallyActivity;

public class MitaxiActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//solicitamos las preferencias del usuario
				SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero",Context.MODE_PRIVATE);
				String uuid = prefs.getString("uuid", null);
				//si aun no tiene datos guardados en preferencias
				if(uuid == null){
					setContentView(R.layout.activity_mitaxi_register);
					initUI();
				}else{
					//se llama directo a la busqueda de taxista
					Intent intent = new Intent(MitaxiActivity.this,MitaxiGoogleMapsActivity.class);
					startActivity(intent);
					this.finish();
				}
		

	}

	public void initUI() {
		findViewById(R.id.mitaxi_register_btn_manually).setOnClickListener(this);
		findViewById(R.id.mitaxi_register_ibtn_googleplus).setOnClickListener(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mitaxi_register, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.mitaxi_register_btn_manually:
				Intent intentManually = new Intent(MitaxiActivity.this, MitaxiRegisterManuallyActivity.class);
				startActivity(intentManually);
				overridePendingTransition(0,0);
				this.finish();
			break;
			
			case R.id.mitaxi_register_ibtn_googleplus:
				Intent intentGooglePlus = new Intent(MitaxiActivity.this, GooglePlusSignInActivity.class);
				startActivity(intentGooglePlus);
				overridePendingTransition(0,0);
				this.finish();
			break;
		}
	}
}