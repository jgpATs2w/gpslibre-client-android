package eleuteron.gpslibre.ui;

import eleuteron.gpslibre.services.MainService;
import eleuteron.gpslibre.utils.Statics;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        setEditText();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
    	Toast.makeText(getApplicationContext(), "recargando configuracion", Toast.LENGTH_SHORT).show();
    	saveId();
    	Intent intent = new Intent(this,MainService.class);
    	stopService(intent);
    	startService(intent);
    	startActivity(new Intent(this,Status.class));
    }
    private void saveId(){
    	EditText id = (EditText) findViewById(R.id.menu_settings_id);
    	Statics.ID = id.getText().toString();
    }
    private void setEditText(){
    	final EditText url = (EditText) findViewById(R.id.menu_settings_url);
    		url.setText(Statics.URL);
    		url.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_ENTER) Statics.URL = url.getText().toString();
					return false;
				}
			});
    	final EditText gpsto = (EditText) findViewById(R.id.menu_settings_gpsto);
    		gpsto.setText(String.valueOf(Statics.GPSTimeout));
    		gpsto.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_ENTER) Statics.GPSTimeout = Integer.parseInt(gpsto.getText().toString());
					return false;
				}
			});
    	final EditText gprsto = (EditText) findViewById(R.id.menu_settings_gprsto);
    		gprsto.setText(String.valueOf(Statics.GPRSTimeout));
    		gprsto.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_ENTER) Statics.GPRSTimeout = Integer.parseInt(gprsto.getText().toString());
					return false;
				}
			});
    	final EditText interval = (EditText) findViewById(R.id.menu_settings_interval);
    		interval.setText(String.valueOf(Statics.Interval));
    		interval.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_ENTER) Statics.Interval = Integer.parseInt(interval.getText().toString());
					return false;
				}
			});
    	final EditText id = (EditText) findViewById(R.id.menu_settings_id);
    		id.setText(String.valueOf(Statics.ID));
    }
    
}
