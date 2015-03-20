package it.android.unishare;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends SmartActivity {
	
	private MyApplication application;
	static final int TIME_SHOW_MILLIS = 3000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		application = MyApplication.getInstance(this);
		application.firstFragment(new SplashFragment());
		application.newDelayedActivity(TIME_SHOW_MILLIS, MainActivity.class);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
