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
 * Created by karnika_ag on 9/26/2015.
 */
public class WorkshopsRegisteredAdapter extends ArrayAdapter<HashMap<String,String>> {

    private final Context context;
    private final List<HashMap<String,String>> objects;
    private final int resource;


    public WorkshopsRegisteredAdapter(Context context, int resource, List<HashMap<String, String>> objects) {
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
        ((TextView)rowView.findViewById(R.id.textViewWorkshoptName)).setText(hashMap.get("workshopName"));
        ((TextView)rowView.findViewById(R.id.textViewRegistartionStatus)).setText("Teamid:"+hashMap.get("team_id"));
        ((TextView)rowView.findViewById(R.id.textViewTeamId)).setText("Status:"+hashMap.get("status_name"));
        ((TextView)rowView.findViewById(R.id.textViewTeamMembers)).setText(hashMap.get("users"));

        return rowView;
    }
}
