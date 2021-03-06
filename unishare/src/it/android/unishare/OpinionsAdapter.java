package it.android.unishare;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class OpinionsAdapter extends ArrayAdapter<Entity> {

	public OpinionsAdapter(Context context, ArrayList<Entity> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		Entity entity = getItem(position);
		
		if (convertView == null) {
	          convertView = LayoutInflater.from(getContext()).inflate(R.layout.opinions_row_layout, parent, false);
	       }
	       // Lookup view for data population
	       TextView date = (TextView) convertView.findViewById(R.id.date);
	       TextView opinion = (TextView) convertView.findViewById(R.id.opinion);
	       RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
	       // Populate the data into the template view using the data object
	       date.setText(entity.get("data"));
	       opinion.setText(entity.get("commento"));
	       ratingBar.setRating(Float.parseFloat(entity.get("voto")));
	       // Return the completed view to render on screen
	       return convertView;

	}
	
	

}
