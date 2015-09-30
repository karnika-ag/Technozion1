package in.technozion.technozion.nav_bar_fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.R;
import in.technozion.technozion.RegisterEventActivity;
import in.technozion.technozion.adapters.EventsAdapter;

public class EventsFragment extends Fragment {
    public static final String TAG = EventsFragment.class.getSimpleName();
    private ListView listViewRegisteredEvents;

//    private String registeredeventsurl="http://bhuichalo.com/tz15/reg_events.json";

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static EventsFragment newInstance(int sectionNumber) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.imageViewAddEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterEventActivity.class));
            }
        });

        listViewRegisteredEvents= (ListView) getActivity().findViewById(R.id.listViewRegisteredEvents);
        new GetEventsTask().execute();
    }

    public class GetEventsTask extends AsyncTask<Void,Void,List<HashMap<String,String>>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching your events..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... voids) {

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userid","9346472");

            String jsonstr= Util.getStringFromURL(URLS.REGISTERED_EVENTS_URL,hashMap);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);

            try {
                JSONObject jsonObject=new JSONObject(jsonstr);
                JSONArray jsonArray=jsonObject.getJSONArray("teamids");
                int len=jsonArray.length();
                List<HashMap<String,String>> values=new ArrayList<>();

                for(int i=0;i<len;i++){
                    JSONObject teams=jsonObject.getJSONObject("teams");
//                    Log.d("teams",teams.toString());
                    String team_id=jsonArray.getJSONObject(i).getString("teamid");
//                    Log.d("teamid",team_id.toString());
                    JSONObject jsonObject1=teams.getJSONObject(team_id);
                    HashMap<String,String> value=new HashMap<>();

                    value.put("team_id", team_id);
                    value.put("eventName", jsonObject1.getString("eventName"));
                    value.put("status_name",jsonObject1.getString("status_name"));
                    value.put("count_total", jsonObject1.getString("count") + "/" + jsonObject1.getString("total") + " registered");
                    String users=jsonObject1.getJSONArray("users").toString();
                    users=users.replace("[","");
                    users=users.replace("]","");
                    users=users.replace("\"","");
                    users=users.replace(",",", ");
                    value.put("users", users);
                    values.add(value);
                }

//                values.add(new HashMap<String, String>());
//                values.add(new HashMap<String, String>());
//                values.add(new HashMap<String, String>());
                return values;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
//                Toast.makeText(getActivity(),string,Toast.LENGTH_SHORT).show();
            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            super.onPostExecute(list);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (list==null) {
                Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();
            } else{

                listViewRegisteredEvents.setAdapter(new EventsAdapter(getActivity(),R.layout.event_boxes,list));
            }
        }
    }
}
