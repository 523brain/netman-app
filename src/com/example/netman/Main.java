package com.example.netman;

import java.net.InetAddress;
import java.text.DecimalFormat;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	public MediaPlayer mp,mp2;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(resultCode==2){
	        setResult(2);
	        finish();
	    }
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	// Menu actions
    	if (item.toString().equals("Home")){
    		setContentView(R.layout.activity_main);
    	}
    	if (item.toString().equals("Network Calculator")){
    		Intent netmask = new Intent(this, Netmask.class);
    		startActivityForResult(netmask,2);
    	}
    	if (item.toString().equals("Network Tools")){
    		Intent ping = new Intent(this, Ping.class);
    		startActivityForResult(ping,2);
    	}
    	if (item.toString().equals("Schließen")){
    		showDialog(10);
    	}
    	return true;
    }
    
    @Override
    protected Dialog onCreateDialog(int id){
    	switch (id){
    	case 10:
    		Builder builder = new AlertDialog.Builder(this);
    		builder.setMessage("NetMan wirklich Beenden?");
    		builder.setCancelable(true);
    		
    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which){
    				setResult(2);
    				Main.this.finish();
    			}
    		});
    		builder.setNegativeButton("Nein, doch nicht!", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which){
    				Toast.makeText(getApplicationContext(), "NetMan wird fortgesetzt", Toast.LENGTH_SHORT).show();
    			}
    		});
    		AlertDialog dialog = builder.create();
    		dialog.show();
    		break;
    	}
    	return super.onCreateDialog(id);
    }
    
    //////////////////////////////////////////
    /////////////   Main VIEW  ///////////////
    //////////////////////////////////////////
    
    public void ClickMainNetcalc(View view){
    	Intent netmask = new Intent(this, Netmask.class);
		startActivityForResult(netmask,2);
    }

	public void ClickMainNetTools(View view){
		Intent ping = new Intent(this, Ping.class);
		startActivityForResult(ping,2);
	}
    
	public void EasterEgg(View view){
		mp = MediaPlayer.create(getApplicationContext(), R.drawable.alarm );
		mp.start();
		mp2 = MediaPlayer.create(getApplicationContext(), R.drawable.laser );
		mp2.start();
	}
		   
}