package eleuteron.gpslibre.services;

import eleuteron.gpslibre.utils.Statics;
import eleuteron.gpslibre.utils.Utils;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

public class MainService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public PendingIntent mAlarmSender;
	@Override
	public void onCreate() {
		Utils.Say(this.getClass(), "Creating service...");
		mAlarmSender = PendingIntent.getService(this, 0, new Intent(this, AlarmService.class), 0);
	}

    WakeLock wakelock;
    AlarmManager am;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Utils.Say(this.getClass(), "starting MainService");
    	PowerManager pwr = (PowerManager)getSystemService(POWER_SERVICE);
    	wakelock = pwr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
    		wakelock.acquire();
    	long firstTime = SystemClock.elapsedRealtime();
       
    	am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        firstTime, Statics.Interval*60*1000, mAlarmSender);
        
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        wakelock.release();
        am.cancel(mAlarmSender);
        Toast.makeText(this, "Main Service Stopped", Toast.LENGTH_SHORT).show();
    }
}
