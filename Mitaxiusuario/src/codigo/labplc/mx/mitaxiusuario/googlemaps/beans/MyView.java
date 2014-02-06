package codigo.labplc.mx.mitaxiusuario.googlemaps.beans;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

/**
 * 
 * @author zace3d
 * 
 */
public class MyView {
	public ImageButton view;
	public Drawable drawPressed;
	public Drawable drawUnpressed;
	public boolean isSelected = false;
	
	public MyView(ImageButton view, Drawable drawPressed, Drawable drawUnpressed,
			boolean isSelected) {
		super();
		this.view = view;
		this.drawPressed = drawPressed;
		this.drawUnpressed = drawUnpressed;
		this.isSelected = isSelected;
	}
}
