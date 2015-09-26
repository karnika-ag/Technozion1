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

public class EventsAdapter extends ArrayAdapter<HashMap<String,String>>{

    private final Context context;
    private final List<HashMap<String,String>> objects;
    private final int resource;

    public EventsAdapter(Context context, int resource, List<HashMap<String, String>> objects) {
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
        ((TextView)rowView.findViewById(R.id.textViewEventName)).setText(hashMap.get("eventName"));
        ((TextView)rowView.findViewById(R.id.textViewTeamId)).setText(hashMap.get("team_id"));
        ((TextView)rowView.findViewById(R.id.textViewStatus)).setText(hashMap.get("status_name"));
        ((TextView)rowView.findViewById(R.id.textViewRegisteredCount)).setText(hashMap.get("count_total"));
        ((TextView)rowView.findViewById(R.id.textViewTeamMembers)).setText(hashMap.get("users"));

        return rowView;
    }
}
