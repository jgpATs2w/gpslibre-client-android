package eleuteron.gpslibre.location;

import android.app.Service;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import eleuteron.gpslibre.net.GPRSService;
import eleuteron.gpslibre.services.AlarmService;
import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;

public class GPSService extends Service{
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.i(this.getClass().getCanonicalName(),"starting GPSService");
		Utils.StatusAlertUpdate("Inicializando el servicio GPS, en la linea GPS especifica el estado.");

		if(Statics.LocLtnr == null) Statics.LocLtnr = new GPSLocationListener(this);
		Statics.GPSService = this;
		Statics.LocLtnr.firstlocation = true;
		Statics.LocMgr.addGpsStatusListener(new GPSStatusListener());
		Statics.LocMgr.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, Statics.LocLtnr);
		
		setTimeout();
		
		return START_STICKY;
	}
	public Thread timeoutThread = null;
	private void setTimeout(){
		Runnable setTO = new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i<Statics.GPSTimeout; i++){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {e.printStackTrace();}
				}
				
				Log.i("","GPS timeout reached");
				stopGPS(false);
			}
		};
		timeoutThread = new Thread(setTO);
			timeoutThread.start();
	}
    public void stopGPS(boolean GOGPRS){
    	Statics.LocMgr.removeUpdates(Statics.LocLtnr);
    	
    	//Utils.GPSStatusUpdate("Parado "+GOGPRS+" "+Utils.getHM(System.currentTimeMillis()));
    	
    	if(GOGPRS){
    		Utils.StatusAlertUpdate("Coordenadas recibidas! subiendo datos...");
    		startService(new Intent(this, GPRSService.class));
    		if(timeoutThread != null){
        		timeoutThread = null;
        	}
    	}else{
    		//Utils.StatusAlertUpdate("En la ultima conexion no hubo datos del satelite.");
    		stopService(new Intent(this, AlarmService.class));
    	}
    	
    	
    }
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	private final GPSBinder binder = new GPSBinder();
	public class GPSBinder extends Binder{
		GPSService getService(){
			return GPSService.this;
		}
	}
}
 
