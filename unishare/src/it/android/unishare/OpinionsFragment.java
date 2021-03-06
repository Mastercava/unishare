package it.android.unishare;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OpinionsFragment extends Fragment implements ViewInitiator {
	
	public static final String TAG = "OpinionsFragment";
	
	private String courseName;
	private View view;
	private ListView listview;
	
	private CoursesActivity activity;
	private OpinionsAdapter opinionsAdapter;
	
	public OpinionsFragment(){
		
	}
	
	public OpinionsFragment (String courseName){
		this.courseName = courseName;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(view == null)
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
		opinionsAdapter = activity.getOpinionsAdapter();
		if(this.courseName == null)
			this.courseName = activity.getCourseName();
		Log.i(TAG, "l'adapter dell'activity ha dimensione " + activity.getOpinionsAdapter().getCount());
		Button btn = (Button) view.findViewById(R.id.insertOpinionButton);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.createInsertOpinionFragment();
			}
		});
		listview = (ListView) view.findViewById(R.id.opinionsListView);
    	if(opinionsAdapter.getCount() > 0)
    		listview.setAdapter(opinionsAdapter);
    	else{
    		Log.i(TAG, "No opinions for this course");
    		String title = "";
    		String message = "Nessuna opinione presente per questo corso";
    		MyApplication.alertMessage(getActivity(), title, message);
    	}			
		TextView courseNameTextView = (TextView) view.findViewById(R.id.courseName);
		courseNameTextView.setText(courseName);
	}
	
	

}
