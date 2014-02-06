package codigo.labplc.mx.mitaxiusuario.drivers;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import codigo.labplc.mx.mitaxiusuario.R;
import codigo.labplc.mx.mitaxiusuario.drivers.adapters.TaxiDriverFragmentPagerAdapter;
import codigo.labplc.mx.mitaxiusuario.drivers.animtrans.ZoomOutPageTransformer;
import codigo.labplc.mx.mitaxiusuario.drivers.beans.TaxiDriver;

public class TaxiDriverActivity extends FragmentActivity {
	//private int NUM_PAGES = 0;
	
	private ViewPager mPager;
	private TaxiDriverFragmentPagerAdapter mPagerAdapter;
	
	private ArrayList<TaxiDriver> listTaxiDrivers = new ArrayList<TaxiDriver>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_taxidriver);

		initUI();
	}

	/**
	 * Crea la interfaz gráfica con sus propiedades
	 */
	public void initUI() {
		mPager = (ViewPager) findViewById(R.id.taxidriver_pager);
		
		//NUM_PAGES = listTaxiDrivers.size();
		
		mPagerAdapter = new TaxiDriverFragmentPagerAdapter(getSupportFragmentManager());
		
		this.addListElementsTaxiDrivers();
		
		int count = 0;
		for(TaxiDriver taxidriver : listTaxiDrivers) {
			mPagerAdapter.addFragment(TaxiDriverPageFragment.newInstance(taxidriver, count++));
		}
		
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
	}
	
	/**
	 * Agrega elementos a la lista de objetos tipo {@link TaxiDriver}
	 */
	public void addListElementsTaxiDrivers() {
		listTaxiDrivers.clear();
		listTaxiDrivers.add(new TaxiDriver("Enrique" , "López", "L32154", "2014-05-03", "1999-06-10", 5, "A12323", "Chevrolet Chevy 2010", 5, 1));
		listTaxiDrivers.add(new TaxiDriver("Edgar" , "Zavala", "A0987", "2014-05-03", "1999-06-10", 1, "L32154", "Nissan Tsuru 2013", 1, 2));
		listTaxiDrivers.add(new TaxiDriver("Miguel" , "Morán", "A3456", "2014-05-03", "1999-06-10", 1, "L32154", "Nissan Tsuru 2013", 1, 2));
		listTaxiDrivers.add(new TaxiDriver("Ruu" , "Rámirez", "aaa111", "2014-05-03", "1999-06-10", 1, "L32154", "Nissan Tsuru 2013", 1, 2));
	}
	
	/**
	 * {@link onBackPressed}
	 */
	@Override
	public void onBackPressed() {
		if (mPager.getCurrentItem() == 0) {
			super.onBackPressed();
		} else {
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}
}
