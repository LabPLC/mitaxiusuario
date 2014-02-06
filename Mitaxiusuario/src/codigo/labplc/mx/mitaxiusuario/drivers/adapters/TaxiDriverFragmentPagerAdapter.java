package codigo.labplc.mx.mitaxiusuario.drivers.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TaxiDriverFragmentPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> listFragments;
	
	/**
	 * Constructor que inicializa una lista de fragmentos
	 * 
	 * @param fm 
	 */
	public TaxiDriverFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.listFragments = new ArrayList<Fragment>();
	}
	
	/**
	 * Agrega un objeto a la lista de {@link Fragment}
	 * 
	 * @param fragment objeto que será agregado a la lista
	 */
	public void addFragment(Fragment fragment) {
		this.listFragments.add(fragment);
	}
	
	/**
	 * Obtiene un elemento de la lista
	 * 
	 * @param index posición del objeto a obtener
	 */
	@Override
	public Fragment getItem(int index) {
		return this.listFragments.get(index);
	}

	/**
	 * Obtiene el tamaño de la lista
	 * 
	 * @return número de elementos que contiene la lista
	 */
	@Override
	public int getCount() {
		return this.listFragments.size();
	}
}