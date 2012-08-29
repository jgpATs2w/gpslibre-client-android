package eleuteron.gpslibre.services;

import eleuteron.gpslibre.location.GPSService;
import eleuteron.gpslibre.net.GPRSService;
import eleuteron.gpslibre.ui.R;
import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class AlarmService extends Service{
	public void goTest(){
		if(Statics.TimerTaskRunning){Utils.Toast("Proceso en ejecucion"); return;}
		Utils.Toast("running test!");
		goGPS();
	}
	 public void goGPS(){
		 Utils.Say("starting GPS...");
		 //if(Statics.TimerTaskRunning){Utils.Toast("Proceso en ejecucion"); return;}
	
		 startService(new Intent(this, GPSService.class));
	 }
	 public void goGPRS(){
	     Utils.Say("going to GPRS..");
	    
		startService(new Intent(this, GPRSService.class));
	 }
	 public static void finish(){
		 Statics.TimerTaskRunning = false;
	 }
	NotificationManager mNM;

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        showNotification();
        
        
        Thread thr = new Thread(null, mTask, "AlarmService");
        	thr.start();
    }

    @Override
    public void onDestroy() {
        mNM.cancel(999);

        Toast.makeText(this, "All work done!", Toast.LENGTH_SHORT).show();
        
        finish();
    }

    Runnable mTask = new Runnable() {
        public void run() {
        	if(Statics.TimerTaskRunning){ Log.i(this.getClass().getCanonicalName(), "aborting alarma thread, already running"); return;}
        	Log.i(this.getClass().getCanonicalName(),"starting alarm thread");
        	Statics.IntervalTic = System.currentTimeMillis();
            goGPS();
            
        	Utils.getBatteryLevel();
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void showNotification() {
        CharSequence text = "Haciendo el trabajo..";

        Notification notification = new Notification(R.drawable.stat_sample, text,
                System.currentTimeMillis());

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, eleuteron.gpslibre.ui.Status.class), 0);

        notification.setLatestEventInfo(this, "GPS Libre",
                       text, contentIntent);

        mNM.notify(999, notification);
    }

    private final IBinder mBinder = new Binder() {
        @Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            return super.onTransact(code, data, reply, flags);
        }
    };
    
   

}
