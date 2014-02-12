package codigo.labplc.mx.mitaxiusuario.drivers;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.drivers.beans.TaxiDriver;

public class TaxiDriverPageFragment extends Fragment {
	private static final String TAXIDRIVER_KEY = "taxidriver";
	private static final String INDEX_KEY = "index";
	
	private TaxiDriver taxiDriver;
	private int index;
	
	/**
	 * Crea una nueva instancia de un {@link TaxiDriverPageFragment} con un index y un objeto {@link TaxiDriver} en específico
	 * 
	 * @param taxiDriver objeto de tipo {@link TaxiDriver} que contiene información de un taxista
	 * @param index posición del {@link TaxiDriverPageFragment} en el {@link ViewPager}
	 * @return un objeto de tipo {@link TaxiDriverPageFragment}
	 */
	public static TaxiDriverPageFragment newInstance(TaxiDriver taxiDriver, int index) {
        TaxiDriverPageFragment fragment = new TaxiDriverPageFragment();
        
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAXIDRIVER_KEY, taxiDriver);
        bundle.putInt(INDEX_KEY, index);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        
        return fragment;
    }
	
	/**
	 * {@link onCreate}
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        this.taxiDriver = (TaxiDriver) ((getArguments() != null) ? getArguments().getSerializable(TAXIDRIVER_KEY) : null);
        this.index = (getArguments() != null) ? getArguments().getInt(INDEX_KEY) : -1;
    }
	
	/**
	 * {@link onCreateView}
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.custom_mitaxi_taxidriver, container,
				false);
		
		// Distacia entre el taxista y el usuario
		TextView tvDrivertimedistance = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_drivertimedistance);
		tvDrivertimedistance.setText("A " +taxiDriver.getDistance() + " Km  de ti ( "+ taxiDriver.getTiempo()+" min aproximadamente )");
		//tvDrivertimedistance.setText(getString(R.string.custommitaxi_tv_drivertimedistance, String.valueOf(this.index)));
		
		// Imagen del taxista
        ImageView ivDriverphoto = (ImageView) rootView.findViewById(R.id.customtaxidriver_iv_driverphoto);
        ivDriverphoto.setImageResource(R.drawable.ic_launcher);
        
        // Tipo de taxi
        TextView tvDrivertype = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_drivertype);
        tvDrivertype.setText(getString(R.string.customtaxidriver_tv_drivertype, taxiDriver.getTaxiTypeId()));
        
        // Botón elegir taxista
        Button btnSelectdriver = (Button) rootView.findViewById(R.id.customtaxidriver_btn_selectdriver);
        btnSelectdriver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
        
        // Nombre y apellido del taxista
        TextView tvDrivername = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_drivername);
        tvDrivername.setText(getString(R.string.customtaxidriver_tv_drivername, taxiDriver.getName() + " " + taxiDriver.getLastName()));
        
        // Licencia del taxista
        TextView tvDriverid = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_driverid);
        tvDriverid.setText(getString(R.string.customtaxidriver_tv_driverid, taxiDriver.getId()));
        
        // Vigencia de la licencia del taxista
        TextView tvDriveridvalidity = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_driveridvalidity);
        tvDriveridvalidity.setText(getString(R.string.customtaxidriver_tv_driveridvalidity, taxiDriver.getIdValidity()));
        
        // Antigüedad del taxista como chofer
        TextView tvDriverantiquity = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_driverantiquity);
        tvDriverantiquity.setText(getString(R.string.customtaxidriver_tv_driverantiquity, taxiDriver.getAntiquity()));
        
        // Calificación del taxista
        TextView tvDriverranking = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_driverranking);
        tvDriverranking.setText(getString(R.string.customtaxidriver_tv_driverranking, taxiDriver.getRanking()));
        
        // Placa del auto que usa el taxista
        TextView tvDrivertaxiplaca = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_drivertaxiplaca);
        tvDrivertaxiplaca.setText(getString(R.string.customtaxidriver_tv_drivertaxiplaca, taxiDriver.getPlaca()));
        
        // Modelo del auto que usa el taxista
        TextView tvDrivertaximodel = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_drivertaximodel);
        tvDrivertaximodel.setText(getString(R.string.customtaxidriver_tv_drivertaximodel, taxiDriver.getTaxiModelCar()));
        
        // Número de infracciones que tiene el taxista
        TextView tvDrivernuminfrac = (TextView) rootView.findViewById(R.id.customtaxidriver_tv_drivernuminfrac);
        tvDrivernuminfrac.setText(getString(R.string.customtaxidriver_tv_drivernuminfrac, taxiDriver.getNumInfrac()));
        
		return rootView;
	}
}