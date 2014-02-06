package codigo.labplc.mx.mitaxiusuario.googlemaps;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
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
	
	public TripPreferencesActivity(Activity act){
		this.act=act;
		initUI();
	}
	

	public void initUI() {
		RelativeLayout view =(RelativeLayout)act.findViewById(R.id.include_activity_mitaxi_trippreferences);
		
		/*view.findViewById(R.id.trippreferences_btn_mytaxidrivers).setOnClickListener(this);
		view.findViewById(R.id.trippreferences_btn_nearesttaxidrivers).setOnClickListener(this);
		view.findViewById(R.id.trippreferences_btn_cancel).setOnClickListener(this);
		view.findViewById(R.id.trippreferences_btn_taketaxi).setOnClickListener(this);*/

		MyViewGroup vgTaxitype = (MyViewGroup) view.findViewById(R.id.trippreferences_vg_taxitype);
		vgTaxitype.initUI(LinearLayout.HORIZONTAL);
		addButtonsToMyGroupView(vgTaxitype);

		MyViewGroup vgTaxispecial = (MyViewGroup) view.findViewById(R.id.trippreferences_vg_taxispecial);
		vgTaxispecial.initUI(LinearLayout.HORIZONTAL);
		addButtonsToMyGroupView(vgTaxispecial);

		MyViewGroup vgPassengers = (MyViewGroup) view.findViewById(R.id.trippreferences_vg_passengers);
		vgPassengers.initUI(LinearLayout.HORIZONTAL);
		addButtonsToMyGroupView(vgPassengers);
	}

	public void addButtonsToMyGroupView(MyViewGroup vgTaxitype) {
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(android.R.drawable.ic_menu_add),
				act.getResources().getDrawable(android.R.drawable.ic_menu_agenda)));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(
						android.R.drawable.ic_menu_always_landscape_portrait),
						act.getResources().getDrawable(android.R.drawable.ic_menu_call)));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(android.R.drawable.ic_menu_camera),
				act.getResources().getDrawable(
						android.R.drawable.ic_menu_close_clear_cancel)));
		vgTaxitype.addView(createImageButton(
				act.getResources().getDrawable(android.R.drawable.ic_menu_compass),
				act.getResources().getDrawable(android.R.drawable.ic_menu_crop)));
	}

	public MyView createImageButton(Drawable drawPressed, Drawable drwaUnpressed) {
		MyView myView = new MyView(new ImageButton(act.getBaseContext()),
				drawPressed, drwaUnpressed, false);
		return myView;
	}

	/*@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// OnClick taxis favoritos
		case R.id.trippreferences_btn_mytaxidrivers:
			act.startActivity(new Intent(act,TaxiDriverActivity.class));
			
			break;

		// OnClick taxis más cercanos en distancia
		case R.id.trippreferences_btn_nearesttaxidrivers:
			act.startActivity(new Intent(act,TaxiDriverActivity.class));
			break;
			
		// Cancelar la operación
		case R.id.trippreferences_btn_cancel:
			act.finish();
			break;

		// Pedir taxi
		case R.id.trippreferences_btn_taketaxi:
			
			break;
		}
	}*/
}