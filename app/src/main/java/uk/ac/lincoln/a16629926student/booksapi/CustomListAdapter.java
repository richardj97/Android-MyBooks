package uk.ac.lincoln.a16629926student.booksapi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private ArrayList<String> description;
    private ArrayList<String> urls;

    public CustomListAdapter(Activity context, ArrayList<String> description, ArrayList<String> urls) {
        super(context, R.layout.custom_list_adapter, description);
        this.context = context;
        this.description = description;
        this.urls = urls;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_list_adapter, null, true);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        TextView descriptionText = (TextView) rowView.findViewById(R.id.textView);
        descriptionText.setText(description.get(position));
        Glide.with(context).load(urls.get(position)).into(imageView);
        return rowView;
    }
}
