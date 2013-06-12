package com.example.netman;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean goOn(View view){
    	//Result Data Objects
    	TextView TextViewMaxClient = (TextView)findViewById(R.id.TextViewMaxClient);
    	TextView textViewFirstClient = (TextView)findViewById(R.id.textViewFirstClient);
    	TextView TextViewLastClient = (TextView)findViewById(R.id.TextViewLastClient);
    	TextView TextViewBroadcast = (TextView)findViewById(R.id.TextViewBroadcast);
    	TextView tv_max_client = (TextView)findViewById(R.id.tv_max_client);
    	TextView tv_first_client = (TextView)findViewById(R.id.tv_first_client);
    	TextView tv_last_client = (TextView)findViewById(R.id.tv_last_client);
    	TextView tv_broadcast = (TextView)findViewById(R.id.tv_boradcast);
    	TextView tvhex = (TextView)findViewById(R.id.tvhex);
    	TextView tvh_first_client = (TextView)findViewById(R.id.tvh_first_client);
    	TextView tvh_last_client = (TextView)findViewById(R.id.tvh_last_client);
    	TextView tvh_broadcast = (TextView)findViewById(R.id.tvh_broadcast);
    	
    	//Inputs fields for calculation
    	EditText ip = (EditText)findViewById(R.id.EditTextIP);
    	EditText bit = (EditText)findViewById(R.id.EditTextBits);
    	EditText mask = (EditText)findViewById(R.id.EditTextNetmask);
    	
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
    	//FELD Bit
    	if (bit.getText().toString().isEmpty()){
    		showDialog(21);
    		return false;
    	} else {
    		if (Integer.parseInt(bit.getText().toString())>32 || Integer.parseInt(bit.getText().toString())<0){
    	    	showDialog(31);
    	    	return false;
    		}
    	}
    	//FELD Netmask
    	if (mask.getText().toString().isEmpty()){
    		showDialog(22);
    		return false;
    	} else {
    		if(checkIp(ip.getText().toString())==false){
    			showDialog(32);
        		return false;
    		}
    	}
    	//End Validation Check
    	
    	String bIP = "";
    	String tmp = "";
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
    	
    	//Set Result Data Visible
    	TextViewMaxClient.setVisibility(1);
    	textViewFirstClient.setVisibility(1);
    	TextViewLastClient.setVisibility(1);
    	TextViewBroadcast.setVisibility(1);
    	tv_max_client.setVisibility(1);
    	tv_first_client.setVisibility(1);
    	tv_last_client.setVisibility(1);
    	tv_broadcast.setVisibility(1);
    	tvhex.setVisibility(1);
    	tvh_first_client.setVisibility(1);
    	tvh_last_client.setVisibility(1);
    	tvh_broadcast.setVisibility(1);
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	// Menu actions
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
    		builder.setMessage("Applikation wird geschlossen!");
    		builder.setCancelable(true);
    		
    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which){
    				Main.this.finish();
    			}
    		});
    		builder.setNegativeButton("Nein, doch nicht!", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int which){
    				Toast.makeText(getApplicationContext(), "Applikation wird fortgesetzt", Toast.LENGTH_LONG).show();
    			}
    		});
    		AlertDialog dialog = builder.create();
    		dialog.show();
    		break;
    	case 20:
    		Toast.makeText(getApplicationContext(), "Fehler bei der IP Eingabe!", Toast.LENGTH_LONG).show();
    		break;
    	case 21:
    		Toast.makeText(getApplicationContext(), "Fehler bei der Bit Eingabe!", Toast.LENGTH_LONG).show();
    		break;
    	case 22:
    		Toast.makeText(getApplicationContext(), "Fehler bei der Netmask Eingabe!", Toast.LENGTH_LONG).show();
    		break;
    	case 30:
    		Toast.makeText(getApplicationContext(), "Fehler keine valide IP Eingabe!", Toast.LENGTH_LONG).show();
    		break;
    	case 31:
    		Toast.makeText(getApplicationContext(), "Fehler keine gültige Bit Eingabe!", Toast.LENGTH_LONG).show();
    		break;
    	case 32:
    		Toast.makeText(getApplicationContext(), "Fehler keine valide Netmask Eingabe!", Toast.LENGTH_LONG).show();
    		break;
    	}
    	return super.onCreateDialog(id);
    }
}