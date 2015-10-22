package in.technozion.technozion.nav_bar_fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import in.technozion.technozion.Data.ServerUtilities;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.technozion.technozion.Data.ConnectionDetector;
import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.MainActivity;
import in.technozion.technozion.R;



public class HomeFragment extends Fragment {


    ConnectionDetector cd;
    public static String name;
    public static String email;
    public static final String PROPERTY_REG_ID = "441783016674";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    String SENDER_ID = "441783016674";
    static final String TAGGCM = "GCMDemo";
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
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
       View v= inflater.inflate(R.layout.fragment_home, container, false);

        cd = new ConnectionDetector(getActivity().getApplicationContext());

        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getActivity(),
                    "Internet Connection Error",
                    Toast.LENGTH_SHORT);
            return v;
        }
        name="saiteja";
        email="saitej3@gmail.com";
        context = getActivity().getApplicationContext();
        if (true) {

            gcm = GoogleCloudMessaging.getInstance(getActivity());
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAGGCM, "No valid Google Play Services APK found.");
        }

        return v;
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
            TextView textView;
            if (list!=null) {
//                Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();
//            } else{

                int len=list.size();
                if (len>0){
                    HashMap<String,String> hashMap1=list.get(0);
                    textView=((TextView) getActivity().findViewById(R.id.textViewnextEvent));
                    if (textView!=null){
                        textView.setText(hashMap1.get("event"));
                    }
                    textView=((TextView)getActivity().findViewById(R.id.textViewNextEventTime));
                    if (textView!=null) {
                        textView.setText(hashMap1.get("time"));
                    }
                    if (len>1){
                        HashMap<String,String> hashMap2=list.get(1);
                        textView=((TextView)getActivity().findViewById(R.id.textViewNextEvent2));
                        if (textView!=null){
                            textView.setText(hashMap2.get("event"));
                        }
                        textView=((TextView)getActivity().findViewById(R.id.textViewNextEvent2Time));
                        if (textView!=null){
                            textView.setText(hashMap2.get("time"));
                        }
                    }
                }
            }
        }
    }



    //gcm functions
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAGGCM, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAGGCM, "App version changed.");
            return "";
        }
        return registrationId;
    }



    private SharedPreferences getGCMPreferences(Context context) {

        return getActivity().getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    private void registerInBackground() {
        new Reg().execute(null,null,null);
    }



    private void sendRegistrationIdToBackend() {

        ServerUtilities.register(getActivity(), name, email, regid);
    }


    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAGGCM, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    private class Reg extends AsyncTask<Void,Void,String>
    {

        protected String doInBackground(Void... params) {
            String msg = "";
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                }
                regid = gcm.register(SENDER_ID);
                msg = "Device registered, registration ID=" + regid;


                sendRegistrationIdToBackend();


                storeRegistrationId(context, regid);
            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }
            return msg;
        }

        protected void onPostExecute(String msg) {
            Toast.makeText(getActivity(),"redid created",Toast.LENGTH_SHORT).show();
        }
    }

}
