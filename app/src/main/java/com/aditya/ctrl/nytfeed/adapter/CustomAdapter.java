package com.aditya.ctrl.nytfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.ctrl.nytfeed.R;
import com.aditya.ctrl.nytfeed.activity.DetailActivity;
import com.aditya.ctrl.nytfeed.fragment.MainFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapter extends BaseAdapter {
	Context context;
	private static LayoutInflater inflater = null;
	ArrayList<HashMap<String, String>> data;
	HashMap<String, String> result = new HashMap<>();

	public CustomAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
		this.context = context;
		data = arraylist;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View itemView = inflater.inflate(R.layout.item_list, null);

		TextView title = (TextView) itemView.findViewById(R.id.tv_title);
		TextView abs = (TextView) itemView.findViewById(R.id.tv_abs);
		TextView source = (TextView) itemView.findViewById(R.id.tv_source);
		TextView pubdate = (TextView) itemView.findViewById(R.id.tv_date);
		ImageView thumbsss = (ImageView) itemView.findViewById(R.id.thumbnail);

		if(data != null){
			result = data.get(position);
			title.setText(result.get(MainFragment.TITLE));
			abs.setText(result.get(MainFragment.ABS));
			source.setText(result.get(MainFragment.SOURCE));
			pubdate.setText(result.get(MainFragment.PUB_DATE));
			Picasso.with(context).load(result.get(MainFragment.IMG)).placeholder(R.drawable.logo).into(thumbsss);
		}

		itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				result = data.get(position);
				Intent intent = new Intent(context, DetailActivity.class)
						.putExtra("URL", result.get(MainFragment.URL))
						.putExtra("SOURCE", result.get(MainFragment.SOURCE));
				context.startActivity(intent);

			}
		});

		return itemView;
	}
}
