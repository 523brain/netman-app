package com.example.netman;

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
    	TextView tv_boradcast = (TextView)findViewById(R.id.tv_boradcast);
    	TextView tvh_max_client = (TextView)findViewById(R.id.tvh_max_client);
    	TextView tvh_first_client = (TextView)findViewById(R.id.tvh_first_client);
    	TextView tvh_last_client = (TextView)findViewById(R.id.tvh_last_client);
    	TextView tvh_broadcast = (TextView)findViewById(R.id.tvh_broadcast);
    	
    	//Inputs fields for calculation
    	EditText ip = (EditText)findViewById(R.id.EditTextIP);
    	EditText bit = (EditText)findViewById(R.id.EditTextBits);
    	EditText mask = (EditText)findViewById(R.id.EditTextNetmask);
    	
    	//Begin Validation Check
    	//Extra Klasse 
    	if (ip.getText().toString().isEmpty()){
    		showDialog(20);
    		return false;
    	}
    	if (bit.getText().toString().isEmpty()==false){
    		if (Integer.parseInt(bit.getText().toString())>32 || Integer.parseInt(bit.getText().toString())<0){
    	    	showDialog(21);
    	    	return false;
    		}
    	} else {
    		showDialog(21);
    		return false;
    	}
    	if (mask.getText().toString().isEmpty()){
    		showDialog(22);
    		return false;
    	}
    	//End Validation Check
    	
    	//Set Result Data Visible
    	TextViewMaxClient.setVisibility(1);
    	textViewFirstClient.setVisibility(1);
    	TextViewLastClient.setVisibility(1);
    	TextViewBroadcast.setVisibility(1);
    	tv_max_client.setVisibility(1);
    	tv_first_client.setVisibility(1);
    	tv_last_client.setVisibility(1);
    	tv_boradcast.setVisibility(1);
    	tvh_max_client.setVisibility(1);
    	tvh_first_client.setVisibility(1);
    	tvh_last_client.setVisibility(1);
    	tvh_broadcast.setVisibility(1);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	// Menu actions
    	if (item.toString().equals("Schlie�en")){
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
    	}
    	return super.onCreateDialog(id);
    }
}