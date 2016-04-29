package com.aditya.ctrl.nytfeed.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.ctrl.nytfeed.R;
import com.aditya.ctrl.nytfeed.activity.DetailActivity;
import com.aditya.ctrl.nytfeed.fragment.MainFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterView extends RecyclerView.Adapter<AdapterView.ViewHolder> {
    private Context context;
    private ArrayList<HashMap<String, String>> data;
    private HashMap<String, String> item_data = new HashMap<>();
    int lastPosition = 0;

    public AdapterView(Context context, ArrayList<HashMap<String, String>> datta) {
        this.context = context;
        this.data = datta;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView h_title, h_abs, h_source, h_pubdate;
        ImageView h_image;

        public ViewHolder(View view) {
            super(view);
            h_title    = (TextView) view.findViewById(R.id.tv_title);
            h_abs      = (TextView) view.findViewById(R.id.tv_abs);
            h_source   = (TextView) view.findViewById(R.id.tv_source);
            h_pubdate  = (TextView) view.findViewById(R.id.tv_date);
            h_image    = (ImageView) view.findViewById(R.id.thumbnail);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, viewGroup, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if(data != null){
            item_data = data.get(position);
            viewHolder.h_title.setText(item_data.get(MainFragment.TITLE));
            viewHolder.h_abs.setText(item_data.get(MainFragment.ABS));
            viewHolder.h_source.setText(item_data.get(MainFragment.SOURCE));
            viewHolder.h_pubdate.setText(item_data.get(MainFragment.PUB_DATE));
            Picasso.with(context).load(item_data.get(MainFragment.IMG)).placeholder(R.drawable.logo).into(viewHolder.h_image);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                item_data = data.get(position);
                Intent intent = new Intent(context, DetailActivity.class)
                        .putExtra("URL", item_data.get(MainFragment.URL))
                        .putExtra("SOURCE", item_data.get(MainFragment.SOURCE));
                context.startActivity(intent);

            }
        });

        setAnimation(viewHolder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bot);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void animate(View view, final int pos) {
        view.animate().cancel();
        view.setTranslationY(300);
        view.setAlpha(0);
        view.animate().alpha(1.0f).translationY(0).setDuration(500).setStartDelay(pos * 100);
    }

}
