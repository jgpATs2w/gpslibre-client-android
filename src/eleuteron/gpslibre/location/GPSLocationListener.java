package eleuteron.gpslibre.location;

import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSLocationListener implements LocationListener{
	public GPSService parentService;
	public boolean firstlocation = true;
	public GPSLocationListener(GPSService s) {
		parentService = s;
	}
	@Override
    public void onLocationChanged(Location loc){
        Statics.loc = loc;
    
        if(firstlocation){
        	Say("coordenadas recibidas!");
        	parentService.stopGPS(true);
        	firstlocation = false;
        }
    }

    @Override
    public void onProviderDisabled(String provider){
        Utils.GPSStatusUpdate("desactivado");
    }

    @Override
    public void onProviderEnabled(String provider){
    	Utils.GPSStatusUpdate("activo");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}

    private void Say(String m){Utils.Say(this.getClass(), m);}
}
