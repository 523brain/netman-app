package com.example.netman;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Portscan extends Activity{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.portscan);
		Bundle extras = getIntent().getExtras();
		/*if(extras!=	null) {
			String text	= extras.getString("text");
			EditText editText = (EditText)findViewById(R.id.editText1);
			editText.setText(text);
		}*/

    }
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	public void savePortscan(){
		
	}
	
	public void loadPortscan(){
		
	}
	
	public boolean goPortscan(View view){
		
		savePortscan();
		return true;
	}
}
