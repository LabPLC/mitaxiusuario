package codigo.labplc.mx.mitaxiusuario.googlemaps.groups;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.googlemaps.TripPreferencesActivity;
import codigo.labplc.mx.mitaxiusuario.googlemaps.beans.MyView;

/**
 * 
 * @author zace3d
 * 
 */
public class MyViewGroup extends LinearLayout {
	private ArrayList<MyView> listViews = new ArrayList<MyView>();
	private LinearLayout llContainer;
	private boolean multiClikc; //indica si permite el multiclick
	
	public MyViewGroup(Context context) {
		super(context);
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initUI(int orientation,boolean multiClikc) {
		this.multiClikc=multiClikc;
		if(orientation == LinearLayout.HORIZONTAL)
			this.addView(initHorizontalUI());
		else if(orientation == LinearLayout.VERTICAL)
			this.addView(initVerticalUI());
	}
	
	private ViewGroup initHorizontalUI() {
		HorizontalScrollView hsv = new HorizontalScrollView(getContext());
		hsv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		llContainer = new LinearLayout(getContext());
		llContainer.setOrientation(LinearLayout.HORIZONTAL);
		
		hsv.addView(llContainer);
		
		return hsv;
	}
	
	private ViewGroup initVerticalUI() {
		HorizontalScrollView vsv = new HorizontalScrollView(getContext());
		vsv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		llContainer = new LinearLayout(getContext());
		llContainer.setOrientation(LinearLayout.VERTICAL);
		
		vsv.addView(llContainer);
		
		return vsv;
	}
	
	public void addView(MyView myView) {
		myView.view.setOnClickListener(viewOnClickListener);
		myView.view.setId(listViews.size());
		myView.view.setBackgroundResource(R.drawable.selector_btn_background);
		myView.view.setImageDrawable(myView.drawUnpressed);
		
		llContainer.addView(myView.view);
		listViews.add(myView);
	}
	
	OnClickListener viewOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int index = v.getId();
			MyView myView = listViews.get(index);
			if(!myView.isSelected){
				myView.isSelected = true;
				myView.view.setBackgroundColor(getResources().getColor(android.R.color.transparent));
				myView.view.setImageDrawable(myView.drawPressed);
				if(myView.id.equals("discapacitados")){
					TripPreferencesActivity.discapacitados= true;
				}else if(myView.id.equals("bicicleta")){
					TripPreferencesActivity.bicicleta= true;
				}else if(myView.id.equals("mascotas")){
					TripPreferencesActivity.mascotas= true;
				}else if(myView.id.equals("ingles")){
					TripPreferencesActivity.ingles= true;
				}else if(myView.id.equals("libre")){
					TripPreferencesActivity.libre= true;
				}else if(myView.id.equals("sitio")){
					TripPreferencesActivity.sitio= true;
				}else if(myView.id.equals("radio")){
					TripPreferencesActivity.radio= true;
				}else if(myView.id.equals("rosa")){
					TripPreferencesActivity.rosa= true;
				}
			}else{
				myView.view.setBackgroundResource(R.drawable.selector_btn_background);
				myView.view.setImageDrawable(myView.drawUnpressed);
				myView.isSelected = false;
				if(myView.id.equals("discapacitados")){
					TripPreferencesActivity.discapacitados= false;
				}else if(myView.id.equals("bicicleta")){
					TripPreferencesActivity.bicicleta= false;
				}else if(myView.id.equals("mascotas")){
					TripPreferencesActivity.mascotas= false;
				}else if(myView.id.equals("ingles")){
					TripPreferencesActivity.ingles= false;
				}else if(myView.id.equals("ingles")){
					TripPreferencesActivity.ingles= false;
				}else if(myView.id.equals("libre")){
					TripPreferencesActivity.libre= false;
				}else if(myView.id.equals("sitio")){
					TripPreferencesActivity.sitio= false;
				}else if(myView.id.equals("radio")){
					TripPreferencesActivity.radio= false;
				}else if(myView.id.equals("rosa")){
					TripPreferencesActivity.rosa= false;
				} 
			}
			if(!multiClikc){
				invalidateViews(index);
				TripPreferencesActivity.PASAJEROS = myView.id;
			}
		}
	};
	
	protected boolean isSelectedView() {
		for(MyView view : listViews) {
			if(view.isSelected)
				return true;
		}
		return false;
	}
	
	protected void invalidateViews(int index) {
		for(MyView myView : listViews) {
			if(myView.view.getId() != index) {
				myView.view.setBackgroundResource(R.drawable.selector_btn_background);
				myView.view.setImageDrawable(myView.drawUnpressed);
				myView.isSelected = false;
			}
		}
	}


}