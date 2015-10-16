package in.technozion.technozion.nav_bar_fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.MainActivity;
import in.technozion.technozion.EventConfirmationActivity;
import in.technozion.technozion.R;
import in.technozion.technozion.RegisterActivity;


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_fetch_qr_code){
//            Toast.makeText(getActivity(),"Loading QR Code",Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            new LoadQRCodeTask().execute(sharedPreferences.getString("userid", ""));
        }
        return super.onOptionsItemSelected(item);
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
        getActivity().findViewById(R.id.updateProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                new UpdateProfile().execute(sharedPreferences.getString("userid", ""));
                loadProfileData();


            }
        });
    }

    private void loadProfileData() {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
//        Boolean registered=sharedPreferences.getBoolean("registered", false);
        ((TextView)getActivity().findViewById(R.id.textViewTzIdValue)).setText(sharedPreferences.getString("userid", ""));
        ((TextView)getActivity().findViewById(R.id.textViewName)).setText(sharedPreferences.getString("name",""));
        ((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue)).setText(sharedPreferences.getString("collegeid",""));
        ((TextView)getActivity().findViewById(R.id.textViewCollege)).setText(sharedPreferences.getString("college",""));
        ((TextView)getActivity().findViewById(R.id.textViewPhoneNumber)).setText(sharedPreferences.getString("phone", ""));
        ((TextView)getActivity().findViewById(R.id.textViewEmail)).setText(sharedPreferences.getString("email", ""));


//        if (registered){
//            getActivity().findViewById(R.id.imageViewQrCode).setVisibility(View.VISIBLE);
//            ((ImageView)getActivity().findViewById(R.id.imageViewQrCode)).setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(),"Stored",Toast.LENGTH_SHORT).show();
        ((TextView)getActivity().findViewById(R.id.textViewTechnozionRegistrationPaid)).setText(sharedPreferences.getString("registration",""));
        ((TextView)getActivity().findViewById(R.id.textViewHospitalityRegistrationPaid)).setText(sharedPreferences.getString("hospitality",""));
//            }else {
//            Toast.makeText(getActivity(),"Loading",Toast.LENGTH_SHORT).show();
        new LoadEventsTask().execute(sharedPreferences.getString("userid", ""));
        String qr_code=sharedPreferences.getString("qr_code","");
        if (qr_code.isEmpty()) {
            new LoadQRCodeTask().execute(sharedPreferences.getString("userid", ""));
        }else {
            byte[] decodedString = Base64.decode(qr_code, Base64.NO_WRAP);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            ((ImageView) getActivity().findViewById(R.id.imageViewQrCode)).setImageBitmap(BitmapFactory.decodeStream(inputStream));
        }
//        }
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
            data.put("userid", strings[0]);
            String jsonstr;
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
//                Toast.makeText(getActivity(), "Could not fetch events, please try again", Toast.LENGTH_SHORT).show();
            } else{
                SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor= sharedPreferences.edit();
                for(String s:hashMap.keySet()){
                    editor.putString(s,hashMap.get(s));
                }
                editor.apply();

                if (hashMap.get("registration").equalsIgnoreCase("paid")){
                    //TODO SAVE THINGS IN SHAREDPREFERENCES
//                    getActivity().findViewById(R.id.imageViewQrCode).setVisibility(View.VISIBLE);
                 //   SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
                 //   SharedPreferences.Editor editor= sharedPreferences.edit();


                 //   for(String s:hashMap.keySet()){
                 //       editor.putString(s,hashMap.get(s));
                 //   }
//                    if (hashMap.get("hospitality").equalsIgnoreCase("paid"))
//                    editor.putBoolean("registered",true);
                 //   editor.apply();
//                    editor.putString("registration", hashMap.get("registration"));
//                    editor.putString("hospitality",hashMap.get("hospitality"));
//                    editor.putString("userid",hashMap.get("userid"));
//                    editor.putString("name",hashMap.get("name"));
//                    editor.putString("collegeid",hashMap.get("collegeid"));
//                    editor.putString("college",hashMap.get("college"));
//                    editor.putString("phone",hashMap.get("phone"));
//                    editor.putString("email",hashMap.get("email"));
                }
                TextView textView;
                textView=((TextView) getActivity().findViewById(R.id.textViewTzIdValue));
                if (textView!=null) {
                    textView.setText(hashMap.get("userid"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewTechnozionRegistrationPaid));
                if (textView!=null) {
                    textView.setText(hashMap.get("registration"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewHospitalityRegistrationPaid));
                if (textView!=null) {
                    textView.setText(hashMap.get("hospitality"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewName));
                if (textView!=null) {
                    textView.setText(hashMap.get("name"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewCollegeIdValue));
                if (textView!=null) {
                    textView.setText(hashMap.get("collegeid"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewCollege));
                if (textView!=null) {
                    textView.setText(hashMap.get("college"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewPhoneNumber));
                if (textView!=null) {
                    textView.setText(hashMap.get("phone"));
                }
                textView=((TextView)getActivity().findViewById(R.id.textViewEmail));
                if (textView!=null) {
                    textView.setText(hashMap.get("email"));
                }
            }
        }
    }


    public class LoadQRCodeTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching QR data..");
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {



            if (strings==null||strings.length==0){
                return null;
            }
            HashMap<String,String> data=new HashMap();
            data.put("userid", strings[0]);

            return Util.getStringFromURL(URLS.QR_CODE_URL, data);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            Log.d("qr image:",string);
            if (string==null||string.length()<100) {
//                Toast.makeText(getActivity(), "Could not fetch QR, please try again", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("qr_code",string);
                editor.apply();
                ImageView imageView=((ImageView) getActivity().findViewById(R.id.imageViewQrCode));
                if (imageView!=null) {
                    byte[] decodedString = Base64.decode(string, Base64.NO_WRAP);
                    InputStream inputStream = new ByteArrayInputStream(decodedString);
                    imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                }
            }
        }
    }

    public class UpdateProfile extends AsyncTask<String,Void,String> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching Update Profile data..");
//            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            if (strings==null||strings.length==0){
                return null;
            }
            HashMap<String,String> data=new HashMap();
            data.put("userid", strings[0]);
            Log.d("k-- send userid",strings[0]);
            return Util.getStringFromURL(URLS.UPDATE_PROFILE_FETCHDETAILS_URL, data);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (string==null) {
                Toast.makeText(getActivity(), "Could not fetch ur update profile data, please try again", Toast.LENGTH_SHORT).show();
            } else {

                Log.d("k-- check ",string);

               // Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

                Intent i = new Intent(getActivity(), RegisterActivity.class);
                i.putExtra("data",string);
                startActivity(i);

                }
            }

    }

}