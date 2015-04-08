package it.android.unishare;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class OpinionsFragment extends Fragment implements ViewInitiator {
	
	public static final String TAG = "OpinionsFragment";
	
	private String courseName;
	private View view;
	private ListView listview;
	
	private CoursesActivity activity;
	private ArrayList<Entity> result = new ArrayList<Entity>();
	private OpinionsAdapter opinionsAdapter;
	
	public OpinionsFragment(){
		
	}
	
	public OpinionsFragment (String courseName, ArrayList<Entity> result){
		this.courseName = courseName;
		this.result = result;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*
		if(savedInstanceState != null)
			book = savedInstanceState.getParcelable(BOOK_ENTITY);
		*/
        view = inflater.inflate(R.layout.opinions_fragment, container, false);
        initializeUI(view);
        return view;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (CoursesActivity) activity;
    }
    

	@Override
	public void initializeUI(View view) {
		opinionsAdapter = new OpinionsAdapter(activity, new ArrayList<Entity>());
		opinionsAdapter.addAll(result);
		listview = (ListView) view.findViewById(R.id.opinionsListView);
    	if(opinionsAdapter.getCount() > 0)
    		listview.setAdapter(opinionsAdapter);
    	else 
			Log.i(TAG, "No opinions for this course");
		TextView courseNameTextView = (TextView) view.findViewById(R.id.courseName);
		courseNameTextView.setText(courseName);
	}
	
	

}
