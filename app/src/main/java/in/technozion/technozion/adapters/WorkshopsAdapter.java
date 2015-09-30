package in.technozion.technozion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import in.technozion.technozion.R;

/**
 * Created by karnika_ag on 9/25/2015.
 */
public class WorkshopsAdapter  extends ArrayAdapter<HashMap<String,String>> {

    private final Context context;
    private final List<HashMap<String,String>> objects;
    private final int resource;


    public WorkshopsAdapter(Context context, int resource, List<HashMap<String, String>> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(resource, parent, false);
        HashMap<String,String> hashMap= objects.get(position);
        ((TextView)rowView.findViewById(R.id.textViewUsername)).setText(hashMap.get("username"));
       // ((TextView)rowView.findViewById(R.id.textViewHospitality)).setText("Hospitality:"+hashMap.get("hospitality"));
        ((TextView)rowView.findViewById(R.id.textViewRegistration)).setText("Registration:"+hashMap.get("registration"));


        return rowView;
    }

}
