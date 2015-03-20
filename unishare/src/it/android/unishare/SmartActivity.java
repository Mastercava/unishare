package it.android.unishare;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

public class SmartActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //if(!Utilities.checkNetworkState(this)) Application.toastMessage(this, "Nessuna connessione internet");
	}
	
	void handleResult(ArrayList<Entity> result, String tag, Fragment fragment) {
		
	}

}
