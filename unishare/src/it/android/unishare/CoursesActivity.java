package it.android.unishare;

import java.util.ArrayList;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CoursesActivity extends SmartActivity {
	
	public static final String TAG = "CoursesActivity";
	
	private static final String COURSES_SEARCH_FRAGMENT_INSTANCE = "courses_search_fragment_key";
	private static final String ADAPTER_VALUES = "key_adapter";
	
	private static final String COURSE_SEARCH_TAG = "courseSearch";
	
	private MyApplication application;
	private CoursesSearchFragment coursesSearchFragment;
	private CoursesAdapter adapter;
	
	ArrayList<Entity> adapterValues = new ArrayList<Entity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);
		application = MyApplication.getInstance(this);
		adapter = new CoursesAdapter(this, new ArrayList<Entity>());
        /**
         * Se il Bundle non � null significa che l'Entity � stata ricreata in seguito ad un cambio di configurazione, quindi
         * devo ripristinare il BooksSearchFragment con i valori presenti nell'adapter prima del cambio di configurazione
         */
        if(savedInstanceState != null){
        	coursesSearchFragment = (CoursesSearchFragment)getFragmentManager().getFragment(savedInstanceState, COURSES_SEARCH_FRAGMENT_INSTANCE);
        	Log.i(TAG, "Existing fragment");
			adapterValues = savedInstanceState.getParcelableArrayList(ADAPTER_VALUES);
			this.adapter.addAll(adapterValues);
        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
        	transaction.add(R.id.books_fragment_container, coursesSearchFragment, CoursesSearchFragment.TAG);
        }
        else{
        	coursesSearchFragment = new CoursesSearchFragment();
        	Log.i(TAG, "Fragment not found. Creating new fragment");
        	getFragmentManager().beginTransaction()
        	.add(R.id.books_fragment_container, coursesSearchFragment, CoursesSearchFragment.TAG).commit();
        }       	
	}
	
    @Override
    protected void onResume() {
    	super.onResume();
    	application.setActivity(this);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	ArrayList<Entity> values = new ArrayList<Entity>();
    	/**
    	 * Storing nel Bundle dei valori presenti nell'adapter, in questo modo possono essere ripristinati in seguito
    	 * ad un cambio di configurazione, come il cambio di orientamento del dispositivo
    	 */
    	if(adapter != null){
    		for(int i = 0; i < adapter.getCount(); i++)
        		values.add(adapter.getItem(i));
        	outState.putParcelableArrayList(ADAPTER_VALUES, values);
    	}
    	/**
    	 * Storing del CoursesSearchFragment per poterne ripristinare lo stato in seguito ad un cambio di configurazione.
    	 * I valori presenti nell'adapter vanno salvati a parte poich� non vengono conservati
    	 */
        getFragmentManager().putFragment(outState, COURSES_SEARCH_FRAGMENT_INSTANCE, coursesSearchFragment);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses, menu);
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
	
	public void initializeFragmentUI(String text, ProgressDialog dialog){
    	if(text != null && text != "") {
    		searchCourses(0, text, dialog);
    	}
    }

	@Override
	public void handleResult(ArrayList<Entity> result, String tag) {
		if(tag == COURSE_SEARCH_TAG) {
			adapter.addAll(result);
			coursesSearchFragment = (CoursesSearchFragment) getFragmentManager().findFragmentByTag(CoursesSearchFragment.TAG);			
			coursesSearchFragment.displayResults(result, tag);
		}
		
	}
	
	public CoursesAdapter getAdapter(){
		return this.adapter;
	}
	
	/////////////////////////////////
	// 	   Calls to database       //
	/////////////////////////////////
	
	private void searchCourses(int campusId, String text, ProgressDialog dialog) {
		application.databaseCall("courses_search.php?q=" + text + "&s=" + campusId, COURSE_SEARCH_TAG, dialog);	
	}
}