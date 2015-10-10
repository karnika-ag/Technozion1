package in.technozion.technozion.nav_bar_fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.MainActivity;
import in.technozion.technozion.R;
import in.technozion.technozion.RegisterConfirmationActivity;
import in.technozion.technozion.WebViewActivity;

public class RegistrationFragment extends Fragment {
    public static final String TAG = RegistrationFragment.class.getSimpleName();
//    public static final String REG_DATA="http://bhuichalo.com/tz15/reg_data.json";
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static RegistrationFragment newInstance(int sectionNumber) {
        RegistrationFragment fragment = new RegistrationFragment();
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
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getActivity());

        ((TextView)getActivity().findViewById(R.id.textViewNameValue)).setText(sh.getString("name", ""));
        ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(sh.getString("college",""));
        ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(sh.getString("collegeid", ""));

        LoadRegistrationData();

        getActivity().findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String userid = sh.getString("userid", "");
                Boolean registration = ((CheckBox) getActivity().findViewById(R.id.checkBoxRegistration)).isChecked();
                Boolean hospitality = ((CheckBox) getActivity().findViewById(R.id.checkBoxHospitality)).isChecked();
                Boolean agree = ((CheckBox) getActivity().findViewById(R.id.checkBoxAgree)).isChecked();
                if (agree != null && agree) {
                    Log.d("k--agree", "enter here");
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userid", userid);
                    hashMap.put("registration", String.valueOf(registration));
                    hashMap.put("hospitality", String.valueOf(hospitality));
                    Log.d("k-- check reg boolean", String.valueOf(registration));
                    Log.d("k-- check hos boolean",String.valueOf(hospitality));
                    new RegisterTask().execute(hashMap);
                } else
                    Toast.makeText(getActivity(), "Please check the terms and condition checkbox", Toast.LENGTH_SHORT).show();

//                new RegisterTask().execute(hashMap);

            }
        });
     /*   getActivity().findViewById(R.id.textViewTerms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(URLS.TERMS_AND_CONDITIONS));
                startActivity(i);
            }
        });*/

    }

    private void LoadRegistrationData() {
//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
//        Boolean data_stored=sharedPreferences.getBoolean("data_stored", false);
//        if (data_stored) {
//            ((TextView)getActivity().findViewById(R.id.textViewName)).setText(sharedPreferences.getString("name",""));
//            ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(sharedPreferences.getString("collegeid",""));
//
//            ((TextView)getActivity().findViewById(R.id.textViewTzIdValue)).setText(sharedPreferences.getString("userid", ""));
//            ((TextView)getActivity().findViewById(R.id.textViewTechnozionRegistrationPaid)).setText(sharedPreferences.getString("registration",""));
//            ((TextView)getActivity().findViewById(R.id.textViewHospitalityRegistrationPaid)).setText(sharedPreferences.getString("hospitality",""));
//            ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(sharedPreferences.getString("college",""));
//            ((TextView)getActivity().findViewById(R.id.textViewPhoneNumber)).setText(sharedPreferences.getString("phone",""));
//            ((TextView)getActivity().findViewById(R.id.textViewEmail)).setText(sharedPreferences.getString("email",""));
//        }else {
            new LoadRegistrationInfoTask().execute();
//        }
    }


    public class LoadRegistrationInfoTask extends AsyncTask<Void,Void,HashMap<String ,String>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching registration data..");
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected HashMap<String ,String> doInBackground(Void... voids) {

            SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String userid = sh.getString("userid","");


            HashMap<String,String> hashMap1 = new HashMap<>();
            hashMap1.put("userid",userid);
            Log.d("k--userid sent", userid);

            String jsonstr= Util.getStringFromURL(URLS.REGISTRATION_DATA,hashMap1);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject1=new JSONObject(jsonstr);

                    //TODO check failure
//                    JSONObject jsonObject1=jsonObject.getJSONObject("data");

                    HashMap<String,String> hashMap=new HashMap<>();

                    hashMap.put("name",jsonObject1.getString("name"));
                    hashMap.put("college",jsonObject1.getString("college"));
                    hashMap.put("collegeid",jsonObject1.getString("collegeid"));
                    hashMap.put("registration",jsonObject1.getString("registration"));
                    hashMap.put("hospitality",jsonObject1.getString("hospitality"));
                    Log.d("k--register hos", jsonObject1.getString("hospitality"));
                    Log.d("k--register res", jsonObject1.getString("registration"));

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
//                Toast.makeText(getActivity(), "Could not fetch registration data, please try again", Toast.LENGTH_SHORT).show();
            } else{
//                ((TextView)getActivity().findViewById(R.id.textViewNameValue)).setText(hashMap.get("name"));
//                ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(hashMap.get("college"));
//                ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(hashMap.get("collegeid"));
                View view;
                CheckBox checkBox;
                if (hashMap.get("hospitality").equals("1")){

                    view=getActivity().findViewById(R.id.textViewRegistered);
                    if (view!=null) {
                        view.setVisibility(View.VISIBLE);
                    }
                    view=getActivity().findViewById(R.id.textViewRegisteredHospitality);
                    if (view!=null) {
                        view.setVisibility(View.VISIBLE);
                    }
                    view=getActivity().findViewById(R.id.checkBoxRegistration);
                    if (view!=null) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    view=getActivity().findViewById(R.id.checkBoxHospitality);
                    if (view!=null) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    view=getActivity().findViewById(R.id.buttonRegister);
                    if (view!=null) {
                        view.setVisibility(View.GONE);
                    }
                    view=getActivity().findViewById(R.id.checkBoxAgree);
                    if (view!=null) {
                        view.setVisibility(View.GONE);
                    }

                }else{
                    if (hashMap.get("registration").equals("1")){
                        view=getActivity().findViewById(R.id.textViewRegistered);
                        view.setVisibility(View.VISIBLE);
                        view=getActivity().findViewById(R.id.checkBoxRegistration);
                        view.setVisibility(View.INVISIBLE);
                        checkBox=((CheckBox)getActivity().findViewById(R.id.checkBoxRegistration));
                        checkBox.setChecked(false);
                        checkBox=((CheckBox)getActivity().findViewById(R.id.checkBoxHospitality));
                        checkBox.setChecked(true);
                        view=getActivity().findViewById(R.id.checkBoxHospitality);
                        view.setEnabled(false);
                    }else {
                        checkBox=((CheckBox)getActivity().findViewById(R.id.checkBoxRegistration));
                        checkBox.setChecked(true);
                        view=getActivity().findViewById(R.id.checkBoxRegistration);
                        view.setEnabled(false);
                    }
                }

//                ((TextView)getActivity().findViewById(R.id.textViewName)).setText(hashMap.get("name"));
//                ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(hashMap.get("collegeid"));
//                ((TextView)getActivity().findViewById(R.id.textViewPhoneNumber)).setText(hashMap.get("phone"));
//                ((TextView)getActivity().findViewById(R.id.textViewEmail)).setText(hashMap.get("email"));
            }
        }
    }
    public class RegisterTask extends AsyncTask<HashMap<String,String>,Void,String> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("registering..");
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HashMap<String,String>... string) {

            String jsonstr= Util.getStringFromURL(URLS.REGISTRATION_URL,string[0]);
            //Log.d("registration received ",jsonstr);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                return jsonstr ;
            }


//
//            String jsonstr= Util.getStringFromURL(URLS.REGISTRATION_URL);
//            if (jsonstr!=null) {
//                Log.d("GOT FROM HTTP", jsonstr);
//                try {
//                    JSONObject jsonObject=new JSONObject(jsonstr);
////
//                    HashMap<String,String> hashMap=new HashMap<>();
////
////                    hashMap.put("userid",jsonObject.getString("userid"));
////                    if(jsonObject.getString("registration").equals("0")) {
////                        hashMap.put("registration", "₹ 400 unpaid");
////                    }else {
////                        hashMap.put("registration", "paid");
////                    }
////                    if(jsonObject.getString("hospitality").equals("0")) {
////                        hashMap.put("hospitality", "₹ 600 unpaid");
////                    }else {
////                        hashMap.put("hospitality", "paid");
////                    }
////                    hashMap.put("name",jsonObject.getString("name"));
////                    hashMap.put("collegeid",jsonObject.getString("collegeid"));
////                    hashMap.put("college",jsonObject.getString("college"));
////                    hashMap.put("phone",jsonObject.getString("phone"));
////                    hashMap.put("email",jsonObject.getString("email"));
//
//                    return hashMap;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            JSONObject jsonObject=new JSONObject(jsonstr);
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (string==null) {
//                Toast.makeText(getActivity(), "Could not connect, please try again", Toast.LENGTH_SHORT).show();
            }  else {

//                Toast.makeText(getActivity(), "Going to launch webViewer", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), WebViewActivity.class);
                i.putExtra("data", string);
                startActivity(i);
//                Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

            }

//            else{
//                if (hashMap.get("registration").equalsIgnoreCase("paid")){
//                    getActivity().findViewById(R.id.imageViewQrCode).setVisibility(View.VISIBLE);
//                    SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
//                    SharedPreferences.Editor editor= sharedPreferences.edit();
//
//
//                    for(String s:hashMap.keySet()){
//                        editor.putString(s,hashMap.get(s));
//                    }
//                    editor.putBoolean("data_stored",true);
//                    editor.apply();
//                }
//                ((TextView)getActivity().findViewById(R.id.textViewTzIdValue)).setText(hashMap.get("userid"));
//                ((TextView)getActivity().findViewById(R.id.textViewTechnozionRegistrationPaid)).setText(hashMap.get("registration"));
//                ((TextView)getActivity().findViewById(R.id.textViewHospitalityRegistrationPaid)).setText(hashMap.get("hospitality"));
//                ((TextView)getActivity().findViewById(R.id.textViewName)).setText(hashMap.get("name"));
//                ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(hashMap.get("collegeid"));
//                ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(hashMap.get("college"));
//                ((TextView)getActivity().findViewById(R.id.textViewPhoneNumber)).setText(hashMap.get("phone"));
//                ((TextView)getActivity().findViewById(R.id.textViewEmail)).setText(hashMap.get("email"));

//            }
//            startActivity(new Intent(getActivity(), RegisterConfirmationActivity.class));
        }
    }
}
