package codigo.labplc.mx.mitaxiusuario.registrer.dialogues;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import codigo.labplc.mx.mitaxiusuario.R;

/**
 * Clase que maneja los di‡logos 
 * @author mikesaurio
 *
 */
public class Dialogos {


	private AlertDialog customDialog= null;	//Creamos el dialogo generico


	/**
	 * Dialogo que muestra el para que registrarte con datos
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog mostrarParaQue(Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_paraque, null);
	    builder.setView(view);
	    builder.setCancelable(false);
        //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_paraque_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di‡logo
    }   
	/**
	 * Dialogo que muestra el para que registrarte con twitter
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog mostrarParaQueTwitter(Activity activity)
    {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_paraque_twitter, null);
	    builder.setView(view);
	    builder.setCancelable(false);
        //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_paraque_twitter_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());//regresamos el di‡logo
    }   
	/*	
	*//**
	 * Dialogo que avisa al usuario que el taxi no esta disponible
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **//*
	public Dialog mostrarTaxiNoDisponible(Activity activity)
    {
		customDialog= null;//hacemos null el dialogo
        customDialog = new Dialog(activity,android.R.style.Theme_Light);//instanciamos el dialogo y le damos estilo
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//deshabilitamos el titulo
        customDialog.setCancelable(false);//obligamos al usuario a pulsar el boton para cerrarlo
        customDialog.setContentView(R.layout.dialogo_taxi_no_disponible);//establecemos el contenido de nuestro dialog
        //escucha del boton aceptar
        ((Button) customDialog.findViewById(R.id.dialogo_taxi_no_disponioble_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return customDialog;//regresamos el di‡logo
    }   
	
	*//**
	 * Dialogo para que el usuario reporte una anomalia en el taxi o el chofer
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **//*
	public Dialog mostrarReportaTaxi(final Activity activity)
    {
		customDialog= null;//hacemos null el dialogo
        customDialog = new Dialog(activity,android.R.style.Theme_Light);//instanciamos el dialogo y le damos estilo
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//deshabilitamos el titulo
        customDialog.setCancelable(false);//obligamos al usuario a pulsar el boton para cerrarlo
        customDialog.setContentView(R.layout.dialogo_reporta_taxista);//establecemos el contenido de nuestro dialog
        final RadioGroup radioSonPlacas = (RadioGroup) customDialog.findViewById(R.id.radioSonPlacas);//radioGroup  placas correctas
        final RadioGroup radioEsChofer = (RadioGroup) customDialog.findViewById(R.id.radioEsChofer);//radioGroup chofer correcto
        
        
        //escucha del boton aceptar
        ((Button) customDialog.findViewById(R.id.dialogo_reporta_taxista_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
            	int selectedPlaca = radioSonPlacas.getCheckedRadioButtonId();//obtenemos el id del radio seleccionado
            	RadioButton radioSonPlaca = (RadioButton) customDialog.findViewById(selectedPlaca);//hacemos la instancia de este
            	
            	int selectedChofer = radioEsChofer.getCheckedRadioButtonId();//obtenemos el id del radio seleccionado
            	RadioButton radioEsChofer = (RadioButton) customDialog.findViewById(selectedChofer);//hacemos la instancia de este
            	
            	Toast.makeText(activity,radioSonPlaca.getText()+" , " + radioEsChofer.getText(), Toast.LENGTH_SHORT).show();
         
                customDialog.dismiss();  //cerramos el di‡logo  
            }
        });
            
          //escucha del boton cancelar
            ((Button) customDialog.findViewById(R.id.dialogo_reporta_taxista_btnCancelar)).setOnClickListener(new OnClickListener() {
                 
                @Override
                public void onClick(View view)
                {
                    customDialog.dismiss();  //cerramos el di‡logo  
                }
            
        });
        return customDialog;//regresamos el di‡logo
    }   
	
	
	
	*//**
	 * Dialogo para que el usuario ranke el servicio
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **//*
	public Dialog mostrarEvaluarServicio(final Activity activity)
    {
		

		customDialog= null;//hacemos null el dialogo
        customDialog = new Dialog(activity,android.R.style.Theme_Light);//instanciamos el dialogo y le damos estilo
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//deshabilitamos el titulo
        customDialog.setCancelable(false);//obligamos al usuario a pulsar el boton para cerrarlo
        customDialog.setContentView(R.layout.dialogo_califica_servicio);//establecemos el contenido de nuestro dialog
       final RatingBar ratingBar = (RatingBar) customDialog.findViewById(R.id.dialogo_califica_servicio_ratingBarServicio);
        
        //escucha del boton aceptar
        ((Button) customDialog.findViewById(R.id.dialogo_califica_servicio_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
            	
            	ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            		public void onRatingChanged(RatingBar ratingBar, float rating,
            			boolean fromUser) {
            
            		}
            	});

            	Toast.makeText(activity,"rank: "+ ratingBar.getRating(), Toast.LENGTH_SHORT).show();   
         
                customDialog.dismiss();  //cerramos el di‡logo  
            }
        });
            
          //escucha del boton cancelar
            ((Button) customDialog.findViewById(R.id.dialogo_califica_servicio_btnCancelar)).setOnClickListener(new OnClickListener() {
                 
                @Override
                public void onClick(View view)
                {
                    customDialog.dismiss();  //cerramos el di‡logo  
                }
            
        });
            
          //escucha del boton agregar a Favoritos
            ((Button) customDialog.findViewById(R.id.dialogo_califica_servicio_btnAgregarFavoritos)).setOnClickListener(new OnClickListener() {
                 
                @Override
                public void onClick(View view)
                {
                	Toast.makeText(activity,"Chofer agregado satisfactoriamente", Toast.LENGTH_LONG).show(); //mensaje
                	((Button) customDialog.findViewById(R.id.dialogo_califica_servicio_btnAgregarFavoritos)).setText("Agregado");
                	((Button) customDialog.findViewById(R.id.dialogo_califica_servicio_btnAgregarFavoritos)).setEnabled(false);
                }
            
        });
        return customDialog;//regresamos el di‡logo
    }   
	
	

	*//**
	 * Dialogo que muestra el para que registrarte con datos
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **//*
	public Dialog mostrarCostosTaxi(Activity activity)
    {
		customDialog= null;//hacemos null el dialogo
        customDialog = new Dialog(activity,android.R.style.Theme_Light);//instanciamos el dialogo y le damos estilo
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//deshabilitamos el titulo
        customDialog.setCancelable(false);//obligamos al usuario a pulsar el boton para cerrarlo
        customDialog.setContentView(R.layout.dialogo_costos_taxis);//establecemos el contenido de nuestro dialog
        //escucha del boton aceptar
        ((Button) customDialog.findViewById(R.id.dialogo_costo_taxi_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return customDialog;//regresamos el di‡logo
    }   */
}
