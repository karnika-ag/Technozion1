package in.technozion.technozion.nav_bar_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.content.Intent;
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
import in.technozion.technozion.MainActivity;
import in.technozion.technozion.R;
import in.technozion.technozion.RegisterWorkshopActivity;
import in.technozion.technozion.adapters.WorkshopsRegisteredAdapter;


public class WorkshopsFragment extends Fragment {
    public static final String TAG = WorkshopsFragment.class.getSimpleName();
    private ListView listViewRegisteredWorkshops;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static WorkshopsFragment newInstance(int sectionNumber) {
        WorkshopsFragment fragment = new WorkshopsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workshops, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.imageViewAddEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RegisterWorkshopActivity.class));
            }
        });

        listViewRegisteredWorkshops= (ListView) getActivity().findViewById(R.id.listViewRegisteredWorkshops);
        new GetEventsTask().execute();

    }

    public class GetEventsTask extends AsyncTask<Void,Void,List<HashMap<String,String>>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching your registered workshops..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(Void... voids) {

            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String userid = sh.getString("userid", "");
            Log.d("k--workshops",userid);
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userid",userid);
            String jsonstr= Util.getStringFromURL(URLS.REGISTERED_WORKSHOPS_URL,hashMap);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);

            try {
                JSONObject jsonObject=new JSONObject(jsonstr);
                JSONArray jsonArray=jsonObject.getJSONArray("teamids");
                int len=jsonArray.length();
                List<HashMap<String,String>> values=new ArrayList<>();

                for(int i=0;i<len;i++){
                    JSONObject teams=jsonObject.getJSONObject("teams");
                    String team_id=jsonArray.getJSONObject(i).getString("teamid");
                    JSONObject jsonObject1=teams.getJSONObject(team_id);
                    HashMap<String,String> value=new HashMap<>();
                    value.put("team_id", team_id);
                    value.put("workshopName", jsonObject1.getString("workshopName"));
                    if(jsonObject1.getString("status").equals("5"))
                    value.put("status_name","CONFIRM");
                    else
                        value.put("status_name","WAITING LIST");
                    String users=jsonObject1.getJSONArray("users").toString();
                    users=users.replace("[","");
                    users=users.replace("]","");
                    users=users.replace("\"","");
                    users=users.replace(",",", ");
                    value.put("users", users);
                    values.add(value);
                }

                return values;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {
            super.onPostExecute(list);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (list==null) {
//                Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();
            } else{

                if (listViewRegisteredWorkshops!=null) {
                    listViewRegisteredWorkshops.setAdapter(new WorkshopsRegisteredAdapter(getActivity(), R.layout.workshops_registerd, list));
                }
            }
        }
    }

}
