package it.android.unishare;

import java.util.ArrayList;

import it.android.unishare.R;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.inputmethodservice.Keyboard.Key;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class BooksSearchFragment extends Fragment implements ViewInitiator {
	
	/*
	private static final String LIST_INSTANCE_STATE = "key_listview";
	private static final String ADAPTER_VALUES = "key_adapter";
	private static final String SEARCH_FORM = "search_form_content";
	*/
	
	public static final String TAG = "it.android.unishare.BooksSearchFragment";
	
	private BooksActivity activity;
	private View view;
	
	private OnBookSelectedListener bookListener;
	
	//UI elements
	ListView listview;
	EditText searchForm;
	ProgressDialog dialog;
	BooksAdapter adapter;
	
	/*
	Parcelable listviewInstanceState;
	ArrayList<Entity> adapterValues = new ArrayList<Entity>();
	String searchFormContent = "";
	*/
	
	
	public interface OnBookSelectedListener {
        public void onBookSelected(Entity book);
    }

    public BooksSearchFragment() {
    	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment when activity is re-initialized
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	if(view == null){
    		/*if(savedInstanceState != null){
        		listviewInstanceState = savedInstanceState.getParcelable(LIST_INSTANCE_STATE);
        		adapterValues = savedInstanceState.getParcelableArrayList(ADAPTER_VALUES);
        		searchFormContent = savedInstanceState.getString(SEARCH_FORM);
        	}*/
    		view = inflater.inflate(R.layout.books_search_fragment, container, false);
            initializeUI(view);
    	}
    	return view;       
    }

    
    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (BooksActivity) activity;
		try {
            this.bookListener = (OnBookSelectedListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnBookSelectedListener");
        }
    }
    
    /*
    @Override
    public void onSaveInstanceState(Bundle outState){
    	super.onSaveInstanceState(outState);
    	ArrayList<Entity> values = new ArrayList<Entity>();
    	for(int i = 0; i < adapter.getCount(); i++)
    		values.add(adapter.getItem(i));
    	outState.putParcelable(LIST_INSTANCE_STATE, listview.onSaveInstanceState());
    	outState.putParcelableArrayList(ADAPTER_VALUES, values);
    	outState.putString(SEARCH_FORM, searchForm.getText().toString());
    }
    */
    
    @Override
	public void initializeUI(View view) {
    	adapter = new BooksAdapter(activity, new ArrayList<Entity>());
    	System.out.println(adapter.getCount());
    	listview = (ListView) view.findViewById(R.id.ListView1);
    	searchForm = (EditText) view.findViewById(R.id.editText1);
    	Button btn = (Button) view.findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	dialog = new ProgressDialog(getActivity());
	        	dialog.setTitle("Searching");
	            dialog.setMessage("Please wait...");
	            dialog.setIndeterminate(false);
	        	activity.initializeFragmentUI(searchForm.getText().toString(), BooksSearchFragment.this, dialog);
	        }
        });
        /*
        if(listviewInstanceState != null){
        	searchForm.setText(searchFormContent);
    		System.out.println(adapterValues.size());
    		adapter.addAll(adapterValues); 
    		System.out.println(Entity.entityArraylistToString(adapterValues));
    		listview.setAdapter(adapter);
    		//listview.onRestoreInstanceState(listviewInstanceState);
    	}
    	*/	
	}

	public void displayResults(ArrayList<Entity> result, String tag) {
		clearList(adapter);
		fillList(listview, result, adapter);
		MyApplication.alertMessage(activity, "Ricerca di '" + searchForm.getText().toString() + "'", (result.size()) + " risultati trovati");
	}
	
	private void fillList(ListView listview, ArrayList<Entity> result, BooksAdapter adapter) {
		adapter.addAll(result);	
		System.out.println(Entity.entityArraylistToString(result));
		listview.setAdapter(adapter);
    		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Entity book = (Entity)parent.getItemAtPosition(position);
				BooksSearchFragment.this.bookListener.onBookSelected(book);			
			}
				
		});
	}
	
	public static void clearList(BooksAdapter adapter) {
		adapter.clear();
		adapter.notifyDataSetChanged();
	}
    
}
