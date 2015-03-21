package it.android.unishare;

import java.util.ArrayList;

import it.android.unishare.BooksSearchFragment.OnBookSelectedListener;
import it.android.unishare.R;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class BooksActivity extends SmartActivity implements OnBookSelectedListener {

	private MyApplication application;
	private BooksSearchFragment booksSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = MyApplication.getInstance(this);
        booksSearchFragment = (BooksSearchFragment)getFragmentManager().findFragmentByTag(BooksSearchFragment.TAG);
        if(booksSearchFragment != null){
        	System.out.println("Fragment esiste");
        	application.firstFragment(booksSearchFragment);
        }
        else
        	application.firstFragment(new BooksSearchFragment());
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	application.setActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    
    public void initializeFragmentUI(String text, Fragment fragment, ProgressDialog dialog){
    	if(text != null && text != "") {
    		application.databaseCall("books.php?q=" + text, "bookSearch", fragment, dialog);
    	}
    }


	@Override
	public void handleResult(ArrayList<Entity> result, String tag, Fragment fragment) {
		if(tag == "bookSearch") {
			BooksSearchFragment bookFragment = (BooksSearchFragment)fragment;
			bookFragment.displayResults(result, tag);
		}
		
	}


	@Override
	public void onBookSelected(Entity book) {
		BooksDetailsFragment booksDetailsFragment = new BooksDetailsFragment(book);
		application.newFragment(booksDetailsFragment);
	}

}
