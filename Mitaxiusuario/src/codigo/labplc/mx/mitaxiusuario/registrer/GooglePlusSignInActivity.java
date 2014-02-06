package codigo.labplc.mx.mitaxiusuario.registrer;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import codigo.labplc.mx.mitaxiusuario.registrer.googleplus.MomentUtil;
import codigo.labplc.mx.mitaxiusuario.registrer.validator.EditTextValidator;
import codigo.labplc.mx.mitaxiusuario.registrer.ws.NetworkUtils;
import codigo.labplc.mx.mitaxiusuario.registrer.ws.WSConnection;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.plus.PlusClient;

public class GooglePlusSignInActivity extends Activity implements
		OnClickListener, PlusClient.ConnectionCallbacks,
		PlusClient.OnConnectionFailedListener,
		PlusClient.OnAccessRevokedListener {

	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;

	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;
	
	private PlusClient mPlusClient;
	private ConnectionResult mConnectionResult;
	
	private EditText etInfousername;
	private EditText etInfouseremail;
	private EditText etInfouserteluser;
	private EditText etInfousertelemergency;
	private EditText etInfousermailemergency;
	private TextView txtparaque;
	
	private boolean[] listHasErrorEditText = {
			false, false, false, false, false
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_register_googleplus);
		
		initUI();
	}

	public void initUI() {
		mPlusClient = new PlusClient.Builder(this, this, this).setActions(
				MomentUtil.ACTIONS).build();
		
		etInfousername = (EditText) findViewById(R.id.mitaxiregistergoogleplus_et_infousername);
		etInfouseremail = (EditText) findViewById(R.id.mitaxiregistergoogleplus_et_infouseremail);
		etInfouserteluser = (EditText) findViewById(R.id.mitaxiregistergoogleplus_et_infouserteluser);
		etInfousertelemergency = (EditText) findViewById(R.id.mitaxiregistergoogleplus_et_infousertelemergency);
		txtparaque =(TextView)findViewById(R.id.mitaxiregistergoogleplus_btn_paraque);
		etInfousermailemergency=(EditText)findViewById(R.id.mitaxiregistergoogleplus_et_infousermailemergency);
		
txtparaque.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			new Dialogos().mostrarParaQueTwitter(GooglePlusSignInActivity.this).show();
			}
		});
		
		etInfousername.setText("");
		etInfouseremail.setText("");
		etInfouserteluser.setText("");
		etInfousertelemergency.setText("");
		etInfousermailemergency.setText("");
		
		
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
		
		findViewById(R.id.mitaxiregistergoogleplus_btn_ok).setOnClickListener(
				this);
		findViewById(R.id.mitaxiregistergoogleplus_btn_cancel)
				.setOnClickListener(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		mPlusClient.connect();
	}

	@Override
	public void onStop() {
		mPlusClient.disconnect();
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		this.revokeAccessGooglePlus();
		
		super.onBackPressed();
	}

	@SuppressWarnings("deprecation")
	public void signInGooglePlus() {
		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available != ConnectionResult.SUCCESS) {

			showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
			return;
		}

		try {
			mConnectionResult.startResolutionForResult(this,
					REQUEST_CODE_SIGN_IN);
		} catch (IntentSender.SendIntentException e) {
			// Fetch a new result to start.
			mPlusClient.connect();
		}
	}

	public void signOutGooglePlus() {
		if (mPlusClient.isConnected()) {
			mPlusClient.clearDefaultAccount();
			mPlusClient.disconnect();
			mPlusClient.connect();
		}
	}

	public void revokeAccessGooglePlus() {
		if (mPlusClient.isConnected()) {
			mPlusClient.revokeAccessAndDisconnect(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mitaxiregistergoogleplus_btn_ok:
			if (!isAnyEditTextEmpty()) {
				if (!hasErrorEditText()) {
					if (NetworkUtils.isNetworkConnectionOk(getBaseContext())) {
						try {
							saveUserInfo();
						} catch (JSONException e) {
							e.printStackTrace();
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

		case R.id.mitaxiregistergoogleplus_btn_cancel:
			this.revokeAccessGooglePlus();
			
			finish();
			break;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
			return super.onCreateDialog(id);
		}

		int available = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		if (available == ConnectionResult.SUCCESS) {
			return null;
		}
		if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
			return GooglePlayServicesUtil.getErrorDialog(available, this,
					REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
		}
		return new AlertDialog.Builder(this)
				.setMessage(R.string.plus_generic_error).setCancelable(true)
				.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_SIGN_IN
				|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
			if (resultCode == RESULT_OK && !mPlusClient.isConnected()
					&& !mPlusClient.isConnecting()) {
				// This time, connect should succeed.
				mPlusClient.connect();
			}
			if (resultCode == RESULT_CANCELED) {
				finish();
			}
		}
	}

	@Override
	public void onAccessRevoked(ConnectionResult status) {
		if (status.isSuccess()) {
			//mSignInStatus.setText(R.string.revoke_access_status);
		} else {
			//mSignInStatus.setText(R.string.revoke_access_error_status);
			mPlusClient.disconnect();
		}
		mPlusClient.connect();

		finish();
		
		Dialogues.Log(getApplicationContext(), "onAccessRevoked", Log.WARN);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		String currentPersonName = mPlusClient.getCurrentPerson() != null ? mPlusClient
				.getCurrentPerson().getDisplayName()
				: getString(R.string.unknown_person);
		
		String accountName = mPlusClient.getAccountName() != null ? mPlusClient
				.getAccountName()
				: getString(R.string.unknown_email);

		etInfousername.setText(currentPersonName);
		etInfouseremail.setText(accountName);
		
		etInfousername.setEnabled(false);
		etInfouseremail.setEnabled(false);
		etInfouserteluser.requestFocus();
				
		Dialogues.Log(getApplicationContext(), "onConnected", Log.WARN);
	}

	@Override
	public void onDisconnected() {
		mPlusClient.connect();
		
		finish();
		
		Dialogues.Log(getApplicationContext(), "onDisconnected", Log.WARN);
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;

		Dialogues.Log(getApplicationContext(), "onConnectionFailed", Log.WARN);

		if (!mPlusClient.isConnected()) {
			signInGooglePlus();
		} else {
			finish();
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
			    
					if(pk_pasajero != null){
						
						user.setUUID(pk_pasajero);//agregamos el UUID del usuario
					
						savePreferences(user); //guardamos todo en preferencias
						
						Intent intent = new Intent(GooglePlusSignInActivity.this, MitaxiActivity.class);
						startActivity(intent);
						GooglePlusSignInActivity.this.finish();
	
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