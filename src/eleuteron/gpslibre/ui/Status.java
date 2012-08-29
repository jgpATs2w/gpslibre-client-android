package eleuteron.gpslibre.ui;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import eleuteron.gpslibre.services.MainService;
import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class Status extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
    		case R.id.menu_exit:
    			Utils.Say("exit"); 
    			stopService(new Intent(this,MainService.class));
    			this.finish();
    			return true;
    		case R.id.menu_settings:
    			startActivity(new Intent(this, Settings.class));
    			return true;
    		case R.id.menu_test:
    			Toast.makeText(this, "normal run", Toast.LENGTH_SHORT).show();
    			startService(new Intent(this,MainService.class));
    			return true;
    		case R.id.menu_stop:
    			stopService(new Intent(this,MainService.class));
    			return true;
    		case R.id.menu_start:
    			startService(new Intent(this,MainService.class));
    			return true;
    		default:
    			Say("otro");
    	}
    	return true;
    }
    
    private void init(){    	
    	Statics.Main = this;
    	Statics.GPSStatusText = (TextView) findViewById(R.id.gps_status);
    	Statics.GPRSStatus = (TextView) findViewById(R.id.gprs_status);
    	 	
    	Statics.ConnMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    	Statics.LocMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
    	
    	setRemainingTimer(); setLastTimer();
    	Say("Initialized for first time.");
    	
    }
    private void setRemainingTimer(){
    	final TextView RemainingTimer = (TextView) findViewById(R.id.remainig_time);
    	RemainingTimer.setText(Statics.Interval+":00");
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
				    	if(Statics.IntervalTic == 0) return;
						long i = Statics.Interval*60-(System.currentTimeMillis()-Statics.IntervalTic)/1000;
						long m = i/60;
						long s = i - m*60;
						RemainingTimer.setText(m+":"+s);
					}
				});
			}
		}, 0, 1000);
    }
    private void setLastTimer(){
    	final TextView LastTimer = (TextView) findViewById(R.id.status_last_time);
    	
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
				    	if(Statics.IntervalTic == 0) return;
						
				    	Date d = new Date(Statics.IntervalTic);
				    		
						LastTimer.setText(d.getHours()+":"+d.getMinutes());
					}
				});
			}
		}, 0, 1000);
    }
    private void Say(String m){ Utils.Say(this.getClass(),m);}
    
}
