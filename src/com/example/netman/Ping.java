package com.example.netman;

import java.net.InetAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Ping extends Activity{
	public MediaPlayer mp;
	public static String SharedName = "SharedData";
	SharedPreferences someData;
	
	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ping);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        loadPing();
    }
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	// Menu actions
    	if (item.toString().equals("Home")){
    		Intent home = new Intent(this, Main.class);
    		startActivityForResult(home,2);
    	}
    	if (item.toString().equals("Network Calculator")){
    		Intent netmask = new Intent(this, Netmask.class);
    		startActivityForResult(netmask,2);
    	}
    	if (item.toString().equals("Network Tools")){
    		loadPing();
    	}
    	if (item.toString().equals("Schließen")){
    		showDialog(10);
    	}
    	return true;
    }
	
    // AUSLAGERN BEI NEXT SPRINT //
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
    				finish();
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
    	case 20:
    		myRandomSound(1,6);
    		Toast.makeText(getApplicationContext(), "Fehler bei der IP Eingabe!", Toast.LENGTH_SHORT).show();
    		break;
    	}
    	return super.onCreateDialog(id);
    }

    public void myRandomSound(int low, int high) {
		high++;
		Double randomNo = (Math.random() * (high - low) + low);
		
		switch(randomNo.intValue()){
		case 1:
			mp = MediaPlayer.create(getApplicationContext(), R.drawable.idontthinkso);
			mp.start();
			break;
		case 4: 
			mp = MediaPlayer.create(getApplicationContext(), R.drawable.yourcrazy);
			mp.start();
			break;
		}
	}
    
    public static boolean checkIp (String sip)
    {
        String [] parts = sip.split ("\\.");
        if ( parts.length != 4 ){
        	return false;
        }
        for (String s : parts){
        	if (s.contains("(")||s.contains(")")||s.contains("#")||s.contains(";")||s.contains("+")||s.contains(" ")){
        		return false;
        	}
            int i = Integer.parseInt (s);
            if (i < 0 || i > 255){
                return false;
            }
        }
        return true;
    } 
    // AUSLAGERN ENDE //
    
    //////////////////////////////////////////
    ////////////  Netmask VIEW  //////////////
    //////////////////////////////////////////
    
    public void savePing(){
		//Inputs fields
    	EditText ip = (EditText)findViewById(R.id.EditTextPingIP);
		
		someData = getSharedPreferences(SharedName, 0);
		SharedPreferences.Editor editor = someData.edit();
		editor.putString("PingIP",ip.getText().toString());
		editor.commit();
	}
	
	public void loadPing(){
		//Inputs fields
    	EditText ip = (EditText)findViewById(R.id.EditTextPingIP);
    	    	
    	someData = getSharedPreferences(SharedName, 0);
    	String dataReturned1 = someData.getString("PingIP",null);
    	
        if (dataReturned1!=null){
        	ip.setText(dataReturned1);
        }
	}

	public void resetPing(View view){
    	//Result Data Objects
    	TextView pings = (TextView)findViewById(R.id.textViewPingResult);
    	
    	//Inputs fields for calculation
    	EditText pingip = (EditText)findViewById(R.id.EditTextPingIP);
    	
    	//Reset input & result data
    	pings.setText("");
    	pingip.setText("");
    }
	
    public boolean goPing(View view){
    	EditText ip = (EditText)findViewById(R.id.EditTextPingIP);
    	if (ip.getText().toString().isEmpty() && (checkIp(ip.getText().toString())==false)){
    			myRandomSound(1,6);
    			showDialog(30);
        		return false;
    	}
    	TextView pingtext = (TextView)findViewById(R.id.textViewPingResult);
    	String message = "";
    	try {
    	      InetAddress host = InetAddress.getByName( ip.getText().toString() );
    	      for (int i=0;i<8;i++){
    	    	  long        tm   = System.nanoTime();    	      
        	      if (host.isReachable(2000)){
        	    	  tm = (System.nanoTime() - tm) / 1000000L;
        	      	  message = message + "Ping OK (time = " + tm + " ms). \n";
        	          
        	      } else {
        	    	  message = message + "No responde: Time out.\n";
        	    	  if (i>3){
        	    		  break;
        	    	  }
        	      }
        	      pingtext.setText(message);
        	      pingtext.setVisibility(1);
    	      }    	      
              message = message+"Host Address = "+host.getHostAddress()+"\nHost Name    = "+host.getHostName();
    	      pingtext.setText(message);
    	      
    	} catch( Exception e ) {
    		//e.printStackTrace();
    		Toast.makeText(getApplicationContext(), "Error: Ping Failed", Toast.LENGTH_SHORT).show();
    	}
    	savePing();
    	return true;
    }
    
}