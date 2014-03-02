package codigo.labplc.mx.mitaxiusuario.googlemaps;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.googlemaps.beans.MyView;
import codigo.labplc.mx.mitaxiusuario.googlemaps.groups.MyViewGroup;

/**
 * 
 * @author zace3d
 * 
 */
public class TripPreferencesActivity  {
	private Activity act;
	public static String PASAJEROS = "1";
	public static boolean discapacitados =false;
	public static boolean bicicleta =false;
	public static boolean mascotas =false;
	public static boolean ingles =false;
	public static boolean libre =false;
	public static boolean sitio =false;
	public static boolean radio =false;
	public static boolean rosa =false;
	public EditText trippreferences_et_referenceorigin;
	
	public TripPreferencesActivity(Activity act){
		this.act=act;
		initUI();
	}
	

	public void initUI() {
		RelativeLayout view =(RelativeLayout)act.findViewById(R.id.include_activity_mitaxi_trippreferences);
		
		 trippreferences_et_referenceorigin = (EditText) view.findViewById(R.id.trippreferences_et_referenceorigin);

		MyViewGroup vgTaxitype = (MyViewGroup) view.findViewById(R.id.trippreferences_vg_taxitype);
		vgTaxitype.initUI(LinearLayout.HORIZONTAL,true);
		addButtonsToMyGroupViewTIPO(vgTaxitype);

		MyViewGroup vgTaxispecial = (MyViewGroup) view.findViewById(R.id.trippreferences_vg_taxispecial);
		vgTaxispecial.initUI(LinearLayout.HORIZONTAL,true);
		addButtonsToMyGroupViewESPECIAL(vgTaxispecial);

		MyViewGroup vgPassengers = (MyViewGroup) view.findViewById(R.id.trippreferences_vg_passengers);
		vgPassengers.initUI(LinearLayout.HORIZONTAL,false);
		addButtonsToMyGroupViewPASAJEROS(vgPassengers);
	}

	public void addButtonsToMyGroupViewTIPO(MyViewGroup vgTaxitype) {
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_libre_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_libre_on),"libre"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(	R.drawable.mi_taxi_assets_taxi_sitio_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_sitio_on),"sitio"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_radio_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_radio_on),"radio"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_rosa_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_taxi_rosa_on),"rosa"));
	}
	
	public void addButtonsToMyGroupViewESPECIAL(MyViewGroup vgTaxitype) {
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_accesible_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_accesible_on),"discapacitados"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(	R.drawable.mi_taxi_assets_bici_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_bici_on),"bicicleta"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_mascota_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_mascota_on),"mascotas"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_english_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_english_on),"ingles"));
	}
	
	public void addButtonsToMyGroupViewPASAJEROS(MyViewGroup vgTaxitype) {
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_1pasajero_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_1pasajero_on),"1"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(	R.drawable.mi_taxi_assets_2pasajeros_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_2pasajeros_on),"2"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_3pasajeros_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_3pasajeros_on),"3"));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_4pasajeros_off),
				act.getResources().getDrawable(R.drawable.mi_taxi_assets_4pasajeros_on),"4"));
	}

	public MyView createImageButton(Drawable drawPressed, Drawable drwaUnpressed,String key) {
		MyView myView = new MyView(new ImageButton(act.getBaseContext()),
				drawPressed, drwaUnpressed, false,key);
		return myView;
	}

	public String  getTripPreferencesActivity(){
		if(trippreferences_et_referenceorigin.getText().toString().equals("")){
			return "Sin+Referencia";
    	}else{
    		
    		return trippreferences_et_referenceorigin.getText().toString().replaceAll(" ", "+");
    	}
		
	}
	
}