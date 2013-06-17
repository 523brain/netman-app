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
	public static String SharedName = "SharedData";
	SharedPreferences someData;
	
	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
    		setContentView(R.layout.activity_main);
    	}
    	if (item.toString().equals("Network Calculator")){
    		setContentView(R.layout.netmask);
    		loadNetmask();
    	}
    	if (item.toString().equals("Network Tools")){
    		setContentView(R.layout.ping);
    		loadPing();
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
    	case 20:
    		Toast.makeText(getApplicationContext(), "Fehler bei der IP Eingabe!", Toast.LENGTH_SHORT).show();
    		break;
    	case 21:
    		Toast.makeText(getApplicationContext(), "Fehler bitte Bits oder Netmask angeben!", Toast.LENGTH_SHORT).show();
    		break;
    	case 30:
    		Toast.makeText(getApplicationContext(), "Fehler keine valide IP Eingabe!", Toast.LENGTH_SHORT).show();
    		break;
    	case 31:
    		Toast.makeText(getApplicationContext(), "Fehler keine gültige Bit Eingabe!", Toast.LENGTH_SHORT).show();
    		break;
    	case 32:
    		Toast.makeText(getApplicationContext(), "Fehler keine valide Netmask Eingabe!", Toast.LENGTH_SHORT).show();
    		break;
    	}
    	return super.onCreateDialog(id);
    }
    
    //////////////////////////////////////////
    /////////////   Main VIEW  ///////////////
    //////////////////////////////////////////
    
    public void ClickMainNetcalc(View view){
    	setContentView(R.layout.netmask);
    	loadNetmask();
    }

	public void ClickMainNetTools(View view){
		setContentView(R.layout.ping);
		loadPing();
	}
    
	public void EasterEgg(View view){
		mp = MediaPlayer.create(getApplicationContext(), R.drawable.alarm );
		mp.start();
		mp2 = MediaPlayer.create(getApplicationContext(), R.drawable.laser );
		mp2.start();
	}
	
    //////////////////////////////////////////
    ///////////// Netmask VIEW ///////////////
    //////////////////////////////////////////
    
	public void saveNetmask(){
		//Inputs fields
    	EditText ip = (EditText)findViewById(R.id.EditTextIP);
    	EditText bit = (EditText)findViewById(R.id.EditTextBits);
    			
		someData = getSharedPreferences(SharedName, 0);
		SharedPreferences.Editor editor = someData.edit();
		if (!ip.getText().toString().isEmpty()){
			editor.putString("IP",ip.getText().toString());
		}
		if (!bit.getText().toString().isEmpty()){
			editor.putString("Bit", bit.getText().toString());		
		}
		editor.commit();
	}
	
	public void loadNetmask(){
		//Inputs fields
    	EditText ip = (EditText)findViewById(R.id.EditTextIP);
    	EditText bit = (EditText)findViewById(R.id.EditTextBits);
    	    	    	
    	someData = getSharedPreferences(SharedName, 0);
    	String dataReturned1 = someData.getString("IP",null);
    	String dataReturned2 = someData.getString("Bit",null);
    	
    	if (dataReturned1!=null){
        	ip.setText(dataReturned1);
        }
        if (dataReturned2!=null){
        	bit.setText(dataReturned2);
        }
	}
	
    public boolean netmaskCalculate(View view){
    	//Result Data Objects
    	TextView tv_max_client = (TextView)findViewById(R.id.ResultMaxClient);
    	TextView tv_first_client = (TextView)findViewById(R.id.ResultFirstClient);
    	TextView tv_last_client = (TextView)findViewById(R.id.ResultLastClient);
    	TextView tv_broadcast = (TextView)findViewById(R.id.ResultBroadcast);
    	TextView tvh_first_client = (TextView)findViewById(R.id.ResultHexFirst);
    	TextView tvh_last_client = (TextView)findViewById(R.id.ResultHexLast);
    	TextView tvh_broadcast = (TextView)findViewById(R.id.ResultHexBroadcast);
    	
    	//Inputs fields for calculation
    	EditText ip = (EditText)findViewById(R.id.EditTextIP);
    	EditText bit = (EditText)findViewById(R.id.EditTextBits);
    	EditText mask = (EditText)findViewById(R.id.EditTextNetmask);
    	
    	//Work object
    	String tmp = "";
    	
    	//Begin Validation Check
    	//FELD IP
    	if (ip.getText().toString().isEmpty()){
    		showDialog(20);
    		return false;
    	} else {
    		if(checkIp(ip.getText().toString())==false){
    			showDialog(30);
        		return false;
    		}
    	}
    	//FELD BIT
    	if (bit.getText().toString().isEmpty() && mask.getText().toString().isEmpty()){
    		showDialog(21);
    		return false;
    	}
    	 
    	//FELD Bit
    	if (!bit.getText().toString().isEmpty()){
    		if((Integer.parseInt(bit.getText().toString())>32)||(Integer.parseInt(bit.getText().toString())<1)){
    			showDialog(31);
    			return false;
    		} else {
    			if (mask.getText().toString().isEmpty()){
    				for(int i=1;i<=Integer.parseInt(bit.getText().toString());i++){
    					tmp = tmp + "1";
    				}
    				for (int i=tmp.length();i<32;i++){
    					tmp = tmp + "0";    				
    				}
    				mask.setText(BinTailToDec(splitBinary(tmp.substring(0,24),".")+tmp.substring(24,tmp.length())));
    			}
    		}
    	}    	
    	//FELD SNM
    	if (!mask.getText().toString().isEmpty()){
    		if (checkIp(mask.getText().toString())==false){
    			showDialog(32);
    			return false;
    		}
    		String [] subs = mask.getText().toString().split ("\\.");
			for (String s : subs){
				tmp = tmp +Integer.toBinaryString(Integer.parseInt(s));
				Boolean V = false;
				switch(Integer.parseInt(s)){
				case 0: V = true; break;
				case 128: V = true; break;
				case 192: V = true; break;
				case 224: V = true; break;
				case 240: V = true; break;
				case 248: V = true; break;
				case 252: V = true; break;
				case 255: V = true; break;
				default: V = false; break;
				}
				if (V == false){
					showDialog(32);
					return false;
				}
			} 
			bit.setText(String.valueOf(tmp.substring(0,tmp.indexOf("0")).length()));
    	}
    	//End Validation Check
    	
    	//Begin Berechnung
    	String bIP = "";
    	String [] subs = ip.getText().toString().split ("\\.");
    	for (String s : subs){
    		if(Integer.toBinaryString(Integer.parseInt(s)).length()<8){
    			for (int i=0;i<(8-Integer.toBinaryString(Integer.parseInt(s)).length());i++){
    				bIP = bIP + "0";
    			}
    			bIP = bIP + Integer.toBinaryString(Integer.parseInt(s));
    		} else {
    			bIP = bIP + Integer.toBinaryString(Integer.parseInt(s));
    		}	
    	}
    	
    	String netbin = bIP.substring(0,Integer.parseInt(bit.getText().toString()));
    	netbin = splitBinary(netbin,".");
    	
    	//Max Client
    	DecimalFormat x = new DecimalFormat ("#");
        String maxaddress = x.format(Math.pow(2,(32-Integer.parseInt(bit.getText().toString()))));
        tv_max_client.setText(String.valueOf((Integer.parseInt(maxaddress)-2)));
        
        //First Client
        tmp = "";
    	for (int i=0;i<(32-Integer.parseInt(bit.getText().toString()));i++){
    		tmp = tmp + "0";
    		if (i==7 || i==15 || i==23){
    			tmp = tmp + ".";
    		}
    	}
    	tmp = new StringBuffer(tmp).reverse().toString();
    	if ((netbin.length()+tmp.length())>35){
        	netbin = netbin.substring(0,(netbin.length()-(netbin.length()+tmp.length()-35)));
        }
    	tv_first_client.setText(BinTailToDec(netbin+tmp.substring(0,(tmp.length()-1))+"1"));
    	tvh_first_client.setText(BinTailToHex(netbin+tmp.substring(0,(tmp.length()-1))+"1"));
    	
        //Last Client
        tmp = "";
    	for (int i=0;i<(32-Integer.parseInt(bit.getText().toString()));i++){
    		tmp = tmp + "1";
    		if (i==7 || i==15 || i==23){
    			tmp = tmp + ".";
    		}
    	}
    	tmp = new StringBuffer(tmp).reverse().toString();
    	if ((netbin.length()+tmp.length())>35){
        	netbin = netbin.substring(0,(netbin.length()-(netbin.length()+tmp.length()-35)));
        }
    	tv_last_client.setText(BinTailToDec(netbin+tmp.substring(0,(tmp.length()-1))+"0"));
    	tvh_last_client.setText(BinTailToHex(netbin+tmp.substring(0,(tmp.length()-1))+"0"));
    	
        //Broadcast Client
        tmp = "";
    	for (int i=0;i<(32-Integer.parseInt(bit.getText().toString()));i++){
    		tmp = tmp + "1";
    		if (i==7 || i==15 || i==23){
    			tmp = tmp + ".";
    		}
    	}
    	tmp = new StringBuffer(tmp).reverse().toString();
        if ((netbin.length()+tmp.length())>35){
        	netbin = netbin.substring(0,(netbin.length()-(netbin.length()+tmp.length()-35)));
        }
    	tv_broadcast.setText(BinTailToDec(netbin+tmp));
    	tvh_broadcast.setText(BinTailToHex(netbin+tmp));
    	//Ende Berechnung
    	
    	saveNetmask();
    	return true;
    }
    
    public String splitBinary(String input, String sym){
    	String all = "";
    	String fill = "";
    	String tmp = input;
    	if (tmp.length()<=8){
    		if (tmp.length()<=16){
    			for (int i=tmp.length();i<8;i++){
    				fill = fill + "0";
    			}
    		}
    		all = tmp.substring(0,tmp.length())+fill+".";
    	}
    	if (tmp.length()>8 && tmp.length()<=16){
    		fill = "";
    		if (tmp.length()<=16){
    			for (int i=tmp.length();i<16;i++){
    				fill = fill + "0";
    			}
    		}
    		all = tmp.substring(0,8)+"."+tmp.substring(8,tmp.length())+fill+".";
    	}
    	if (tmp.length()>16){
    		fill = "";
    		if (tmp.length()<=24){
    			for (int i=tmp.length();i<24;i++){
    				fill = fill + "0";
    			}
    		}
    		all = tmp.substring(0,8)+"."+tmp.substring(8,16)+"."+tmp.substring(16,tmp.length())+fill+".";
    	}
    	return all;
    }
    
    public String BinTailToHex(String tail)
    {
        String all ="";
        int j = 0;
        String [] parts = tail.split ("\\.");
        for (String s : parts){
        	j++;
        	if (j>3){
        		all = all + Integer.toHexString(Integer.parseInt(s,2));
        	} else {
        		all = all + Integer.toHexString(Integer.parseInt(s,2))+".";
        	}
        }
        return all.toUpperCase().toString();
    }
    
    public String BinTailToDec(String tail)
    {
        String all ="";
        int j = 0;
        String [] parts = tail.split ("\\.");
        for (String s : parts){
        	j++;
        	if (j>3){
        		all = all + String.valueOf(Integer.parseInt(s,2));
        	} else {
        		all = all + String.valueOf(Integer.parseInt(s,2))+".";
        	}
        }
        return all;
    }
    
    public static boolean checkIp (String sip)
    {
        String [] parts = sip.split ("\\.");
        if ( parts.length != 4 ){
        	return false;
        }
        for (String s : parts){
            int i = Integer.parseInt (s);
            if (i < 0 || i > 255){
                return false;
            }
        }
        return true;
    } 
    
    public void netmaskReset(View view){
    	//Result Data Objects
    	TextView tv_max_client = (TextView)findViewById(R.id.ResultMaxClient);
    	TextView tv_first_client = (TextView)findViewById(R.id.ResultFirstClient);
    	TextView tv_last_client = (TextView)findViewById(R.id.ResultLastClient);
    	TextView tv_broadcast = (TextView)findViewById(R.id.ResultBroadcast);
    	TextView tvh_first_client = (TextView)findViewById(R.id.ResultHexFirst);
    	TextView tvh_last_client = (TextView)findViewById(R.id.ResultHexLast);
    	TextView tvh_broadcast = (TextView)findViewById(R.id.ResultHexBroadcast);
    	
    	//Inputs fields for calculation
    	EditText ip = (EditText)findViewById(R.id.EditTextIP);
    	EditText bit = (EditText)findViewById(R.id.EditTextBits);
    	EditText mask = (EditText)findViewById(R.id.EditTextNetmask);
    	
    	//Reset input & result data
    	tv_max_client.setText("");
    	tv_first_client.setText("");
    	tv_last_client.setText("");
    	tv_broadcast.setText("");
    	tvh_first_client.setText("");
    	tvh_last_client.setText("");
    	tvh_broadcast.setText("");
    	ip.setText("");
    	bit.setText("");
    	mask.setText("");	
    }
    
    //////////////////////////////////////////
    ///////////// PING VIEW //////////////////
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
	
    public void goPing(View view){
    	EditText ip = (EditText)findViewById(R.id.EditTextPingIP);
    	TextView pingtext = (TextView)findViewById(R.id.textViewPingResult);
    	String message = "";
    	try {
    	      InetAddress host = InetAddress.getByName( ip.getText().toString() );
    	      for (int i=0;i<8;i++){
    	    	  long        tm   = System.nanoTime();    	      
        	      if (host.isReachable(5000)){
        	    	  tm = (System.nanoTime() - tm) / 1000000L;
        	      	  message = message + "Ping OK (time = " + tm + " ms). \n";
        	          
        	      } else {
        	    	  message = message + "No responde: Time out.\n";
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
    }
    
}