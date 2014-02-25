package codigo.labplc.mx.mitaxiusuario.registrer;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.mitaxiusuario.MitaxiActivity;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.registrer.beans.User;
import codigo.labplc.mx.mitaxiusuario.registrer.daos.FactoryDAO;
import codigo.labplc.mx.mitaxiusuario.registrer.daos.UserDAO;
import codigo.labplc.mx.mitaxiusuario.registrer.dialogues.Dialogos;
import codigo.labplc.mx.mitaxiusuario.registrer.dialogues.Dialogues;
import codigo.labplc.mx.mitaxiusuario.registrer.expressions.RegularExpressions;
import codigo.labplc.mx.mitaxiusuario.registrer.validator.EditTextValidator;
import codigo.labplc.mx.mitaxiusuario.registrer.ws.NetworkUtils;
import codigo.labplc.mx.mitaxiusuario.registrer.ws.WSConnection;

public class MitaxiRegisterManuallyActivity extends Activity implements
		OnClickListener {
	
	private EditText etInfousername;
	private EditText etInfouseremail;
	private EditText etInfouserteluser;
	private EditText etInfousertelemergency;
	private EditText etInfousermailemergency;
	private TextView txtparaque;
	
	private boolean[] listHasErrorEditText = {
			false, false, false, false,false
	};
	private Dialog customDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_register_manually);

		initUI();
	}

	public void initUI() {
		etInfousername = (EditText) findViewById(R.id.mitaxiregistermanually_et_infousername);
		etInfouseremail = (EditText) findViewById(R.id.mitaxiregistermanually_et_infouseremail);
		etInfouserteluser = (EditText) findViewById(R.id.mitaxiregistermanually_et_infouserteluser);
		etInfousertelemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_infousertelemergency);
		txtparaque =(TextView)findViewById(R.id.mitaxiregistermanually_btn_paraque);
		etInfousermailemergency = (EditText)findViewById(R.id.mitaxiregistermanually_et_infousermailemergency);
		txtparaque.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			new Dialogos().mostrarParaQue(MitaxiRegisterManuallyActivity.this).show();
			}
		});

		
		etInfousername.setTag(RegularExpressions.KEY_IS_STRING);
		etInfouseremail.setTag(RegularExpressions.KEY_IS_EMAIL);
		etInfouserteluser.setTag(RegularExpressions.KEY_IS_NUMBER);
		etInfousertelemergency.setTag(RegularExpressions.KEY_IS_NUMBER);
		etInfousermailemergency.setTag(RegularExpressions.KEY_IS_EMAIL);
		
		
		
		etInfousername.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfousername, listHasErrorEditText, 0));
		etInfouseremail.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfouseremail, listHasErrorEditText, 1));
		etInfouserteluser.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfouserteluser, listHasErrorEditText, 2));
		etInfousertelemergency.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfousertelemergency, listHasErrorEditText, 3));
		etInfousermailemergency.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
				getBaseContext(), etInfousermailemergency, listHasErrorEditText, 4));
		findViewById(R.id.mitaxiregistermanually_btn_ok).setOnClickListener(
				this);
		findViewById(R.id.mitaxiregistermanually_btn_cancel)
				.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mitaxi_register, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mitaxiregistermanually_btn_ok:
			if (!isAnyEditTextEmpty()) {
				if (!hasErrorEditText()) {
					if (NetworkUtils.isNetworkConnectionOk(getBaseContext())) {
						try {
							saveUserInfo();
						} catch (JSONException e) {
							Dialogues.Toast(getApplicationContext(),
									"error fatal :(",
									Toast.LENGTH_LONG);
						}
					} else {
						Dialogues.Toast(getApplicationContext(),
								getString(R.string.no_internet_connection),
								Toast.LENGTH_LONG);
					}
				} else {
					Dialogues.Toast(getApplicationContext(),
							getString(R.string.edittext_wrong_info),
							Toast.LENGTH_LONG);
				}
			} else {
				Dialogues.Toast(getApplicationContext(),
						getString(R.string.edittext_emtpy), Toast.LENGTH_LONG);
			}
			break;

		case R.id.mitaxiregistermanually_btn_cancel:
			finish();
			break;
		}
	}
	
	public boolean hasErrorEditText() {
		for(boolean hasError : listHasErrorEditText)
			if(hasError)
				return true;
		return false;
	}
	
	public void saveUserInfo() throws JSONException {
		User user = new User();
		user.setName(etInfousername.getText().toString().replaceAll(" ", "."));
		user.setEmail(etInfouseremail.getText().toString());
		user.setTeluser(etInfouserteluser.getText().toString());
		user.setTelemergency(etInfousertelemergency.getText().toString());
		user.setMailenergency(etInfousermailemergency.getText().toString());
		
		if(user.getTeluser() != user.getTelemergency()) {
			String url = WSConnection.buildUrlSaveUser(getBaseContext(), user);
			UserDAO userDAO = FactoryDAO.getUserDAOImp(url);
			String ws_result = null;
			ws_result = userDAO.save(user);
			
			if (ws_result != null) {
				//Dialogues.Toast(getApplicationContext(), ws_result, Toast.LENGTH_LONG);
				
				//revisamos json
					String errorJson;
					String successJson;
					String pk_pasajero;
					 
					 JSONObject json= (JSONObject) new JSONTokener(ws_result).nextValue();
				      JSONObject json2 = json.getJSONObject("message");
					 try {
						 errorJson = (String) json2.get("error");
					 } catch (JSONException e) { errorJson = null; }
					 try {
						 successJson = (String) json2.get("success");
						 pk_pasajero = (String) json2.get("pk_pasajero");
					 } catch (JSONException e) { successJson = null;pk_pasajero = null;}
					 
				//	Dialogues.Toast(getApplicationContext(), "query1: "+ errorJson+"", Toast.LENGTH_LONG);
				// Dialogues.Toast(getApplicationContext(), "query2: "+ successJson+".."+ pk_pasajero + "", Toast.LENGTH_LONG);
				    
						if(pk_pasajero != null){
							
							user.setUUID(pk_pasajero);//agregamos el UUID del usuario
						
							savePreferences(user); //guardamos todo en preferencias
							
							Intent intent = new Intent(MitaxiRegisterManuallyActivity.this, MitaxiActivity.class);
							startActivity(intent);
							MitaxiRegisterManuallyActivity.this.finish();
		
						}else if(errorJson != null){
							//aqui cachamos el tipo de error
						}	 
							} else {
				Dialogues.Toast(getApplicationContext(), getString(R.string.transaction_wrong), Toast.LENGTH_LONG);
			}
		} else {
			Dialogues.Toast(getApplicationContext(), getString(R.string.edittext_error_telephone_repeated), Toast.LENGTH_LONG);
		}
	}
	
	/**
	 * Guarda las preferencias del usuario en la aplicación
	 * 
	 * @param (user) bean que contiene los datos del usuario
	 * @return void
	 */
	public void savePreferences(User user) {
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasPasajero", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("nombre",user.getName());
		editor.putString("correo", user.getName());
		editor.putString("num", user.getTeluser());
		editor.putString("emer", user.getTelemergency());
		editor.putString("uuid", user.getUUID());
		editor.putString("correoemer", user.getMailenergency());
		editor.commit();
	}
	
	/**
	 * Verifica si alguno de los campos {@link EditText} con la información del
	 * usuario está vacio.
	 * 
	 * @return (boolean) <b>true</b> si esta vacio <b>false</b> si no esta vacio
	 */
	public boolean isAnyEditTextEmpty() {
		boolean empty = false;
		if (EditTextValidator.isEditTextEmpty(etInfousername))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfouseremail))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfouserteluser))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfousertelemergency))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfousermailemergency))
			empty = true;
		

		return empty;
	}
	
	

	
}