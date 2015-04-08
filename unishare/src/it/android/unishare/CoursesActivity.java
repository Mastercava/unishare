package it.android.unishare;

import it.android.unishare.SearchFragment.OnCourseSelectedListener;

import java.util.ArrayList;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class CoursesActivity extends AdapterActivity implements OnCourseSelectedListener{
	
	public static final String TAG = "CoursesActivity";
	
	private static final String COURSES_SEARCH_FRAGMENT_INSTANCE = "courses_search_fragment_key";
	private static final String COURSE_NAME = "course_name_key";
	private static final String ADAPTER_VALUES = "key_adapter";
	private static final String COURSE_ID = "course_id_key";
	
	private static final String COURSE_SEARCH_TAG = "courseSearch";
	private static final String OPINION_TAG = "opinionSearch";
	
	private MyApplication application;
	private SearchFragment searchFragment;
	private OpinionsFragment opinionsFragment;
	private CoursesAdapter coursesAdapter;
	private OpinionsAdapter opinionsAdapter;
	
	private String courseName;
	private int courseId;
	
	ArrayList<Entity> adapterValues = new ArrayList<Entity>();
	ArrayList<Entity> opinionAdapterValues = new ArrayList<Entity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);
		application = MyApplication.getInstance(this);
		coursesAdapter = new CoursesAdapter(this, new ArrayList<Entity>());
		opinionsAdapter = new OpinionsAdapter(this, new ArrayList<Entity>());
        /**
         * Se il Bundle non è null significa che l'Entity è stata ricreata in seguito ad un cambio di configurazione, quindi
         * devo ripristinare il BooksSearchFragment con i valori presenti nell'adapter prima del cambio di configurazione
         */
        if(savedInstanceState != null){
        	searchFragment = (SearchFragment)getFragmentManager().getFragment(savedInstanceState, COURSES_SEARCH_FRAGMENT_INSTANCE);
        	Log.i(TAG, "Existing fragment");
			adapterValues = savedInstanceState.getParcelableArrayList(ADAPTER_VALUES);
			this.coursesAdapter.addAll(adapterValues);
        	FragmentTransaction transaction = getFragmentManager().beginTransaction();
        	transaction.add(R.id.courses_fragment_container, searchFragment, SearchFragment.TAG);
        	/*
        	if(savedInstanceState.getInt(COURSE_ID)){
        		this.courseName = savedInstanceState.getString(COURSE_NAME);
        		this.courseId = savedInstanceState.getInt(COURSE_ID);
        		ProgressDialog dialog = new ProgressDialog(this);
        		dialog.setTitle("Searching");
        		dialog.setMessage("Please wait...");
        		getOpinion(courseId, dialog);
        	}
        	*/
        }
        else{
        	searchFragment = new SearchFragment();
        	Log.i(TAG, "Fragment not found. Creating new fragment");
        	getFragmentManager().beginTransaction()
        	.add(R.id.courses_fragment_container, searchFragment, SearchFragment.TAG).commit();
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
    	ArrayList<Entity> opinions = new ArrayList<Entity>();
    	/**
    	 * Storing nel Bundle dei valori presenti nell'adapter, in questo modo possono essere ripristinati in seguito
    	 * ad un cambio di configurazione, come il cambio di orientamento del dispositivo
    	 */
    	if(coursesAdapter != null){
    		for(int i = 0; i < coursesAdapter.getCount(); i++)
        		values.add(coursesAdapter.getItem(i));
        	outState.putParcelableArrayList(ADAPTER_VALUES, values);
    	} 	
    	/**
    	 * Storing del CoursesSearchFragment per poterne ripristinare lo stato in seguito ad un cambio di configurazione.
    	 * I valori presenti nell'adapter vanno salvati a parte poichè non vengono conservati
    	 */
        getFragmentManager().putFragment(outState, COURSES_SEARCH_FRAGMENT_INSTANCE, searchFragment);
        if(this.courseName != null){
        	outState.putString(COURSE_NAME, this.courseName);
        	outState.putInt(COURSE_ID, this.courseId);
        }
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
			coursesAdapter.addAll(result);
			searchFragment = (SearchFragment) getFragmentManager().findFragmentByTag(SearchFragment.TAG);			
			searchFragment.displayResults(result, tag);
		}
		if(tag == OPINION_TAG){
			opinionsAdapter.addAll(result);
			opinionsFragment = new OpinionsFragment(courseName, result);
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.courses_fragment_container, opinionsFragment, OpinionsFragment.TAG);
			transaction.addToBackStack(null);
			transaction.commit();	
		}
		
	}
	
	@Override
	public CoursesAdapter getAdapter(){
		return this.coursesAdapter;
	}
	
	@Override
	public void onCourseSelected(String courseId, String courseName, ProgressDialog dialog) {
		this.courseName = courseName;
		this.courseId = Integer.parseInt(courseId);
		getOpinion(this.courseId, dialog);
		
	}
	
	public OpinionsAdapter getOpinionsAdapter(){
		return this.opinionsAdapter;
	}
	
	/////////////////////////////////
	// 	   Calls to database       //
	/////////////////////////////////
	
	private void searchCourses(int campusId, String text, ProgressDialog dialog) {
		application.databaseCall("courses_search.php?q=" + text + "&s=" + campusId, COURSE_SEARCH_TAG, dialog);	
	}
	
	private void getOpinion(int courseId, ProgressDialog dialog){
		application.databaseCall("opinions.php?id=" + courseId, OPINION_TAG, dialog);
	}

}
