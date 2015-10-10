package in.technozion.technozion;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginActivityFragment extends Fragment {

    public LoginActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Animation animation= (TranslateAnimation) getActivity().getResources().getAnimation(R.anim.shake);

//        AnimationUtils.loadAnimation(animation);

//        ((FrameLayout)getActivity().findViewById(R.id.frameLayout)).addView(getActivity().getLayoutInflater().inflate(R.layout.box_login,null));
//       /*

        //Login
        getActivity().findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new LoginTask().execute(((EditText) getActivity().findViewById(R.id.editTextEmail)).getText().toString(), ((EditText) getActivity().findViewById(R.id.editTextPassword)).getText().toString());

//                SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("logged_in", true);
//                editor.apply();
//                Intent intent=new Intent(getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });
        getActivity().findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), RegisterActivity.class));
            }
        });


        //Logout
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putBoolean("logged_in",true);
//        editor.apply();


        //Check
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean logged_in=sharedPreferences.getBoolean("logged_in", false);
        if (logged_in) {
            startActivity(new Intent(getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
//        */
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean logged_in=sharedPreferences.getBoolean("logged_in", false);
        if (logged_in) {
            getActivity().onBackPressed();
//            startActivity(new Intent(getContext(), MainActivity.class));
        }
    }


    public class LoginTask extends AsyncTask<String,Void,HashMap<String ,String>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Logging in..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected HashMap<String ,String> doInBackground(String... strings) {

            if (strings==null||strings.length<2) return null;
            if(strings[0]==null||strings[0].length()==0) return null;
            if(strings[1]==null||strings[1].length()==0) return null;

            HashMap<String,String> hashMapPostData=new HashMap();
            hashMapPostData.put("email", strings[0]);
            hashMapPostData.put("password",strings[1]);

            String jsonstr= Util.getStringFromURL(URLS.LOGIN_URL,hashMapPostData);

            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject=new JSONObject(jsonstr);

                    if(jsonObject.getString("status").equalsIgnoreCase("success")) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("userid", jsonObject.getString("userid"));
                        hashMap.put("name", jsonObject.getString("name"));
                        hashMap.put("collegeid", jsonObject.getString("collegeid"));
                        hashMap.put("college", jsonObject.getString("college"));
                        hashMap.put("phone", jsonObject.getString("phone"));
                        hashMap.put("email", jsonObject.getString("email"));
                        if(jsonObject.getString("registration").equals("1"))
                        hashMap.put("registration", "paid");
                        else
                            hashMap.put("registration", "₹ 400 unpaid");
                        if(jsonObject.getString("hospitality").equals("1"))
                            hashMap.put("hospitality", "paid");
                        else
                            hashMap.put("hospitality", "₹ 600 unpaid");



                        return hashMap;
                    }
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
//                Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                shakeView(getActivity().findViewById(R.id.linearLayoutLogin));
            } else{
                SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor= sharedPreferences.edit();

                for(String s:hashMap.keySet()){
                    editor.putString(s,hashMap.get(s));
                }
                editor.putBoolean("logged_in", true);
//                if(hashMap.get("registration").equalsIgnoreCase("paid") && hashMap.get("hospitality").equalsIgnoreCase("paid"))
//                    editor.putBoolean("registered",true);
                editor.apply();

//                Intent intent=new Intent(getContext(), MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(new Intent(getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        }
    }

    private void shakeView(View view) {
        TranslateAnimation translateAnimation=new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,-0.02f,
                Animation.RELATIVE_TO_SELF,0.02f,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0);
        translateAnimation.setRepeatCount(5);
        translateAnimation.setRepeatMode(TranslateAnimation.REVERSE);
        translateAnimation.setDuration(30);
        view.startAnimation(translateAnimation);
    }
}
