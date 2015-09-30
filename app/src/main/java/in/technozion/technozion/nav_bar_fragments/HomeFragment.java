package in.technozion.technozion.nav_bar_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import in.technozion.technozion.adapters.EventsAdapter;

import in.technozion.technozion.MainActivity;
import in.technozion.technozion.R;



public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static HomeFragment newInstance(int sectionNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        new GetNextEventsTask().execute(sharedPreferences.getString("userid",null));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public class GetNextEventsTask extends AsyncTask<String,Void,List<HashMap<String,String>>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching your events..");
            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
//            SharedPreferences sharedPreferences=SharedPreferences

            if (strings==null||strings.length==0){
                return null;
            }
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("userid",strings[0]);
            String jsonstr= Util.getStringFromURL(URLS.HOME_URL,hashMap);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);

                try {
                    JSONArray jsonArray=new JSONArray(jsonstr);
                    List<HashMap<String ,String>> values=new ArrayList<>();

                    int len=jsonArray.length();
                    if (len>2) len=2;
                    for(int i=0;i<len;i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        HashMap<String ,String> hashMap1=new HashMap<>();
                        hashMap1.put("event", jsonObject.getString("event"));
                        hashMap1.put("time", jsonObject.getString("time"));
                        values.add(hashMap1);
                    }

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
                int len=list.size();
                if (len>0){
                    HashMap<String,String> hashMap1=list.get(0);
                    ((TextView)getActivity().findViewById(R.id.textViewnextEvent)).setText(hashMap1.get("event"));
                    ((TextView)getActivity().findViewById(R.id.textViewNextEventTime)).setText(hashMap1.get("time"));
                    if (len>1){
                        HashMap<String,String> hashMap2=list.get(1);
                        ((TextView)getActivity().findViewById(R.id.textViewNextEvent2)).setText(hashMap2.get("event"));
                        ((TextView)getActivity().findViewById(R.id.textViewNextEvent2Time)).setText(hashMap2.get("time"));
                    }
                }
            }
        }
    }

}
