package eleuteron.gpslibre.utils;

import java.util.Date;

import android.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class Utils {
	static String TAG = "gpslibre";
	static long tic = System.currentTimeMillis();
	public static void Say(Class c, String m){
    	LogViewAppend(""+(System.currentTimeMillis()-tic)/1000+" "+m+"\n");
    	
    	Log.d(TAG+" "+c.getClass().getSimpleName(), m);
    }
	public static void Say(String m){
    	LogViewAppend(""+(System.currentTimeMillis()-tic)/1000+" "+m+"\n");
    	
    	Log.d(TAG, m);
    }
	private static String gpstext="";
	public static void GPSStatusUpdate(String t){
		gpstext = t;
		Statics.Main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Statics.GPSStatusText.setText(gpstext);
			}
		});
	}
	public static void StatusIdUpdate(){
		Statics.Main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Statics.Main.ViewIDUpdate();
			}
		});
	}
	public static void StatusAlertUpdate(final String t){
		Statics.Main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Statics.Main.ViewAlertUpdate(t);
			}
		});
	}
	private static String gprstext="";
	public static void GPRSStatusUpdate(String t){
		gprstext = t;
		Statics.Main.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Statics.GPRSStatus.setText(gprstext);
			}
		});
	}
	private static String logtext="";
	public static void LogViewAppend(String t){
//		logtext = t;
//		Statics.Main.runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				Statics.LogView.append(logtext+"\n");
//			}
//		});
	}
	public static void Toast(String m){
		android.widget.Toast.makeText(Statics.Main, m, 2).show();
	}
    public static void getBatteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        	@Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                Statics.batt = level;
                
                Say("readen battery level :"+level+"%");
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Statics.Main.registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
    public static String getHM(long t){
    	Date d = new Date();
    	return d.getHours()+":"+d.getMinutes();
    }
}
