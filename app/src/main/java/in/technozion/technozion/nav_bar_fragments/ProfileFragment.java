package in.technozion.technozion.nav_bar_fragments;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.R;


public class ProfileFragment extends Fragment {
    public static final String TAG = ProfileFragment.class.getSimpleName();
//    public static final String profileUrl="http://192.168.87.50/tz-registration-master/profile/index_mobile/9346472";
//    public static final String profileUrl="http://bhuichalo.com/tz15/profile.json";
private static final String ARG_SECTION_NUMBER = "section_number";

    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadProfileData();
    }

    private void loadProfileData() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
   //     Boolean registered=sharedPreferences.getBoolean("registered", false);
        ((TextView)getActivity().findViewById(R.id.textViewTzIdValue)).setText(sharedPreferences.getString("userid", ""));
        ((TextView)getActivity().findViewById(R.id.textViewName)).setText(sharedPreferences.getString("name",""));
        ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(sharedPreferences.getString("collegeid",""));
        ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(sharedPreferences.getString("college",""));
        ((TextView)getActivity().findViewById(R.id.textViewPhoneNumber)).setText(sharedPreferences.getString("phone", ""));
        ((TextView)getActivity().findViewById(R.id.textViewEmail)).setText(sharedPreferences.getString("email", ""));

     //   if (registered){
     //       Log.d("k--profile register",registered.toString());
      //      getActivity().findViewById(R.id.imageViewQrCode).setVisibility(View.VISIBLE);
//            ((ImageView)getActivity().findViewById(R.id.imageViewQrCode)).setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(),"Stored",Toast.LENGTH_SHORT).show();
            ((TextView)getActivity().findViewById(R.id.textViewTechnozionRegistrationPaid)).setText(sharedPreferences.getString("registration",""));
            ((TextView)getActivity().findViewById(R.id.textViewHospitalityRegistrationPaid)).setText(sharedPreferences.getString("hospitality",""));
      //      }else {
//            Toast.makeText(getActivity(),"Loading",Toast.LENGTH_SHORT).show();
            new LoadEventsTask().execute(sharedPreferences.getString("userid", ""));
      //  }
    }

    public class LoadEventsTask extends AsyncTask<String,Void,HashMap<String ,String>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching profile data..");
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected HashMap<String ,String> doInBackground(String... strings) {

            if (strings==null||strings.length==0){
                return null;
            }
            HashMap<String,String> data=new HashMap();
            data.put("userid",strings[0]);
            String jsonstr=null;
            jsonstr=Util.getStringFromURL(URLS.PROFILE_URL,data);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject=new JSONObject(jsonstr);

                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("userid",jsonObject.getString("userid"));
                    if(jsonObject.getString("registration").equals("0")) {
                        Log.d("k--got reg",jsonObject.getString("registration"));
                        hashMap.put("registration", "₹ 400 unpaid");
                    }else {
                        hashMap.put("registration", "paid");
                    }
                    if(jsonObject.getString("hospitality").equals("0")) {
                        hashMap.put("hospitality", "₹ 600 unpaid");
                    }else {
                        hashMap.put("hospitality", "paid");
                    }
                    hashMap.put("name",jsonObject.getString("name"));
                    hashMap.put("collegeid",jsonObject.getString("collegeid"));
                    hashMap.put("college",jsonObject.getString("college"));
                    hashMap.put("phone",jsonObject.getString("phone"));
                    hashMap.put("email",jsonObject.getString("email"));

                    return hashMap;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            JSONObject jsonObject=new JSONObject(jsonstr);
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String ,String> hashMap) {
            super.onPostExecute(hashMap);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (hashMap==null) {
                Toast.makeText(getActivity(), "Could not fetch events, please try again", Toast.LENGTH_SHORT).show();
            } else{
                if (hashMap.get("registration").equalsIgnoreCase("paid")){
                    //TODO SAVE THINGS IN SHAREDPREFERENCES
                    getActivity().findViewById(R.id.imageViewQrCode).setVisibility(View.VISIBLE);
                    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor= sharedPreferences.edit();


                    for(String s:hashMap.keySet()){
                        editor.putString(s,hashMap.get(s));
                    }
//                    if (hashMap.get("hospitality").equalsIgnoreCase("paid"))
//                    editor.putBoolean("registered",true);
                    editor.apply();
//                    editor.putString("registration", hashMap.get("registration"));
//                    editor.putString("hospitality",hashMap.get("hospitality"));
//                    editor.putString("userid",hashMap.get("userid"));
//                    editor.putString("name",hashMap.get("name"));
//                    editor.putString("collegeid",hashMap.get("collegeid"));
//                    editor.putString("college",hashMap.get("college"));
//                    editor.putString("phone",hashMap.get("phone"));
//                    editor.putString("email",hashMap.get("email"));
                }
                ((TextView)getActivity().findViewById(R.id.textViewTzIdValue)).setText(hashMap.get("userid"));
                ((TextView)getActivity().findViewById(R.id.textViewTechnozionRegistrationPaid)).setText(hashMap.get("registration"));
                ((TextView)getActivity().findViewById(R.id.textViewHospitalityRegistrationPaid)).setText(hashMap.get("hospitality"));
                ((TextView)getActivity().findViewById(R.id.textViewName)).setText(hashMap.get("name"));
                ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(hashMap.get("collegeid"));
                ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(hashMap.get("college"));
                ((TextView)getActivity().findViewById(R.id.textViewPhoneNumber)).setText(hashMap.get("phone"));
                ((TextView)getActivity().findViewById(R.id.textViewEmail)).setText(hashMap.get("email"));
            }
        }
    }

}
