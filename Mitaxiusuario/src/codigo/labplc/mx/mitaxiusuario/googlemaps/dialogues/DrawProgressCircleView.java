package codigo.labplc.mx.mitaxiusuario.googlemaps.dialogues;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

/**
 * 
 * @author zace3d
 * 
 */
public class DrawProgressCircleView extends View {
	private Paint mPaintFill;
	private Paint mPaintStroke;
	private final float RADIUS_INCREMENT = 5;
	private float RADIUS_MAX = 0;
	private float radius = 0;
	
	private int width = 0;
	private int height = 0;
	
	public DrawProgressCircleView(Context context) {
		super(context);
		mPaintFill = new Paint();
		mPaintFill.setStyle(Style.FILL);
		mPaintFill.setAntiAlias(true);
		mPaintFill.setColor(0x449933CC);
		
		mPaintStroke = new Paint();
		mPaintStroke.setStyle(Style.STROKE);
		mPaintStroke.setAntiAlias(true);
		mPaintStroke.setColor(0xFF9933CC);
		mPaintStroke.setStrokeWidth(4);
		
		Point size = getScreenSize(context);
		width = size.x;
		height = size.y;
		
		RADIUS_MAX = width / 2 - RADIUS_INCREMENT;
	}

	private void drawCircle(Canvas canvas, float cx, float cy, float radius, Paint paint) {
		canvas.drawCircle(cx, cy, radius, paint);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#44000000"));
		
		drawCircle(canvas, width / 2, height / 2, radius, mPaintFill);
		
		drawCircle(canvas, width / 2, height / 2, radius, mPaintStroke);
		
		radius += RADIUS_INCREMENT;
		
		if(radius >= RADIUS_MAX) {
			radius = 0;
		}
		invalidate();
	}
	
	public Point getScreenSize(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		return size;
	}
}