package in.technozion.technozion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import in.technozion.technozion.R;

/**
 * Created by android on 10/20/2015.
 */
public class FAQAdapter extends ArrayAdapter<String>{

    private final Context context;
    private final String[] objects;
    private final int resource;

    public FAQAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;
    }

    @Override
    public int getCount() {
        return objects.length/2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);
        ((TextView)rowView.findViewById(R.id.textViewQuestion)).setText(objects[2*position]);
        ((TextView)rowView.findViewById(R.id.textViewAnswer)).setText(objects[2*position+1]);
        return rowView;
    }
}
