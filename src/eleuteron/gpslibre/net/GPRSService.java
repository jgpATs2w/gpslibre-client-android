package eleuteron.gpslibre.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import eleuteron.gpslibre.services.AlarmService;
import eleuteron.gpslibre.services.MainService;
import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;

import android.app.Service;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.provider.Settings;

public class GPRSService extends Service {
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Runnable run = new Runnable(){
			@Override
			public void run() {
				Utils.GPRSStatusUpdate("registrando en red...");
				int i =0; while(!isNetworkAvailable()){i++;
					if(i==1) setAirplaneModeOn(false);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {e.printStackTrace();}
					
					if(i>Statics.GPRSTimeout*2){timeoutReached(); break; }
				}
				Say((connect2Server())? "Conexion OK" : "fallo de conexion");
				if(connect2Server()){
					Say("conexion OK");
					Utils.StatusAlertUpdate("La ultima conexion fue debuti.");
				}else{
					Say("fallo de conexion");
					Utils.StatusAlertUpdate("La ultima conexion fallo conectando al servidor.");
				}

				stopConnection();
			}
			
		};
		new Thread(run).start();
		return START_STICKY;
	}
	private void stopConnection(){
		setAirplaneModeOn(true);
		
		stopService(new Intent(this, AlarmService.class));

		Utils.GPRSStatusUpdate("desconectado");
	}
	private void timeoutReached(){
		setAirplaneModeOn(true);
		Utils.StatusAlertUpdate("En la ultima conexion, no se pudo registrar. Revisa que la red movil este activada y que haya cobertura.");
	}
	public boolean connect2Server(){
		Utils.Say("connecting to server..."); 
		Utils.GPRSStatusUpdate("conectando al servidor...");
    	try{
        	return connect(getUrl());
        	
        }catch (ClientProtocolException e){ Say("client protocol exception!!"); return false;		        	
        }catch (IOException e){Utils.Say(this.getClass(), "ioexception!!"); return false;}
    }
    public boolean connect(String url) throws ClientProtocolException, IOException{
    	Utils.Say("connecting to "+url);
    	HttpClient client = new DefaultHttpClient();
    	HttpGet request = new HttpGet(url);
    	HttpResponse response = client.execute(request);
    	
    	BufferedReader rd = new BufferedReader(new InputStreamReader(
    		response.getEntity().getContent()));
    	String line = ""; String resp = "";
    	while ((line = rd.readLine()) != null) {
    		resp += line;
    	}
    	return resp.contains("OK")? true : false;
    }
    private String getUrl(){
    	String url = Statics.URL+"?id="+Statics.ID+
    			"&bat="+Statics.batt+
    			"&tsr="+System.currentTimeMillis()+
    			"&lat="+Statics.loc.getLatitude()+
    			"&lon="+Statics.loc.getLongitude();
    	
    	return url;
    }

    public boolean isNetworkAvailable() {
        NetworkInfo networkInfo = Statics.ConnMgr.getActiveNetworkInfo();
      
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        //Utils.Say("network not available yet");
        return false;
    }
    private void setAirplaneModeOn(boolean NewState){
    	Utils.Say("Changing Airplane mode to "+(NewState?"ON" : "OFF"));
    	boolean isEnabled = Settings.System.getInt(
      	      Statics.Main.getContentResolver(), 
      	      Settings.System.AIRPLANE_MODE_ON, 0) == 1;
    	
      if(isEnabled == NewState) return;
      
      Settings.System.putInt(Statics.Main.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, isEnabled? 0: 1);
      Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
      intent.putExtra("state", !isEnabled);
      Statics.Main.sendBroadcast(intent);
    }
    private void Say(String m){
    	Utils.Say(this.getClass(), m); 
		Utils.GPRSStatusUpdate(m);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
