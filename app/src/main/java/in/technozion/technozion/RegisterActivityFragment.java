package in.technozion.technozion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Button;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterActivityFragment extends Fragment {

    private int flag;
    private String string;
    public RegisterActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        string = getActivity().getIntent().getStringExtra("data");

           flag=0;

            String[] gender = getActivity().getResources().getStringArray(R.array.gender);
            ArrayAdapter genderAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, gender);
            ((Spinner) getActivity().findViewById(R.id.spinnerGender)).setAdapter(genderAdapter);
            String[] city = getActivity().getResources().getStringArray(R.array.city);
            ArrayAdapter cityAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, city);
            ((Spinner) getActivity().findViewById(R.id.spinnerCity)).setAdapter(cityAdapter);
            ((Spinner) getActivity().findViewById(R.id.spinnerCity)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String city = ((TextView) view).getText().toString();
                    if (!city.equalsIgnoreCase("select city")) {
                        new FetchCollegeTask().execute(city);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            String[] state = getActivity().getResources().getStringArray(R.array.state);
            ArrayAdapter stateAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, state);
            ((Spinner) getActivity().findViewById(R.id.spinnerState)).setAdapter(stateAdapter);

        if(string != null) {
            flag=1;
            //Toast.makeText(getActivity(), "came from update profiles1 **", Toast.LENGTH_SHORT).show();
            try {
            JSONObject jsonObject= new JSONObject(string);
                String name = jsonObject.getString("name");
                String email=jsonObject.getString("email");
                String phone=jsonObject.getString("phone");
                String sex=jsonObject.getString("sex");
                String college=jsonObject.getString("college");
                String collegeid=jsonObject.getString("collegeid");
                String city1=jsonObject.getString("city");
                String state1=jsonObject.getString("state");
                Log.d("city1",city1);
//             ((TextView) ((Spinner) getActivity().findViewById(R.id.spinnerState)).getSelectedView()).setText(state1);
                View view2=getActivity().findViewById(R.id.editTextName);
                ((EditText) view2).setText(name);
                view2.setEnabled(false);
                View view4=getActivity().findViewById(R.id.editTextEmail);
                ((EditText) view4).setText(email);
                view4.setEnabled(false);

            ((EditText) getActivity().findViewById(R.id.editTextPhone)).setText(phone);
            ((EditText) getActivity().findViewById(R.id.editTextStudentId)).setText(collegeid);
               View view=getActivity().findViewById(R.id.editTextPassword);
                if (view!=null) {
                    view.setVisibility(View.GONE);
                }
                View view3=getActivity().findViewById(R.id.textView48);
                if (view3!=null) {
                    view3.setVisibility(View.GONE);
                }


                Button mButton=(Button)getActivity().findViewById(R.id.buttonRegister);
            mButton.setText("update profile");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

            getActivity().findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            getActivity().findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, String> regdata = new HashMap<String, String>();

                    String name = ((EditText) getActivity().findViewById(R.id.editTextName)).getText().toString();
                    String email = ((EditText) getActivity().findViewById(R.id.editTextEmail)).getText().toString();
                    String gender = ((TextView) ((Spinner) getActivity().findViewById(R.id.spinnerGender)).getSelectedView()).getText().toString();
                    String phone = ((EditText) getActivity().findViewById(R.id.editTextPhone)).getText().toString();
                    String city = ((TextView) ((Spinner) getActivity().findViewById(R.id.spinnerCity)).getSelectedView()).getText().toString();
                    String college = ((TextView) ((Spinner) getActivity().findViewById(R.id.spinnerCollege)).getSelectedView()).getText().toString();
                    if (college.equalsIgnoreCase("other")) {
                        college = ((EditText) getActivity().findViewById(R.id.editTextCollege)).getText().toString();
                    }
                    String id = ((EditText) getActivity().findViewById(R.id.editTextStudentId)).getText().toString();
                    String state = ((TextView) ((Spinner) getActivity().findViewById(R.id.spinnerState)).getSelectedView()).getText().toString();


                    String password = ((EditText) getActivity().findViewById(R.id.editTextPassword)).getText().toString();


                    if (name == null || email == null || phone == null ||city==null || gender==null || state==null|| college == null || id == null  ||(flag == 0 && password == null)) {
                        Log.d("null", "sth is null");
                    } else {

                        if(flag==0) {
                            regdata.put("inputName", name);
                            regdata.put("inputEmail", email);
                            regdata.put("inputGender", gender);
                            regdata.put("inputPhone", phone);
                            regdata.put("inputCity", city);
                            regdata.put("inputCollege", college);
                            regdata.put("inputCollegeId", id);
                            regdata.put("inputState", state);
                            regdata.put("inputPassword", password);
                        }
                        else
                        {
                            regdata.put("inputGender", gender);
                            regdata.put("inputPhone", phone);
                            regdata.put("inputCity", city);
                            regdata.put("inputCollege", college);
                            regdata.put("inputCollegeId", id);
                            regdata.put("inputState", state);
                            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
                            regdata.put("userid",sharedPreferences.getString("userid", ""));
                        }
                        new RegisterTask().execute(regdata);
                    }
//                Log.d("gender", ((TextView) ((Spinner) getActivity().findViewById(R.id.spinnerGender)).getSelectedView()).getText().toString());


                }
            });

    }


    public class RegisterTask extends AsyncTask<HashMap<String,String>,Void,String> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            if(flag==0)
            progressDialog.setMessage("Registering..");
            else
                progressDialog.setMessage("Updating..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HashMap<String ,String> ... hashMaps) {
            if (hashMaps==null||hashMaps.length==0){
                return null;
            }
            String s=new String();
            if(flag==0)
                s=Util.getStringFromURL(URLS.REGISTER_URL, hashMaps[0]);
            else
                s=Util.getStringFromURL(URLS.UPDATE_PROFILE_URL, hashMaps[0]);
            return s;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            try {
                if (string == null) {
                    Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObjectRec = new JSONObject(string);
                    Toast.makeText(getActivity(), jsonObjectRec.getString("message"), Toast.LENGTH_SHORT).show();
                    if (jsonObjectRec.getString("status").equalsIgnoreCase("success")) {
                        getActivity().onBackPressed();
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class FetchCollegeTask extends AsyncTask<String,Void,String[]> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Fetching Colleges..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected String [] doInBackground(String ... strings) {
            if (strings==null||strings.length==0){
                return null;
            }
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("city", strings[0]);
//            String jsonstr= Util.getStringFromURL(URLS.FETCH_COLLEGES_URL, hashMap);
            String jsonstr= Util.getStringFromURL(URLS.FETCH_COLLEGES_URL,hashMap);
            if (jsonstr==null){
                Log.d("colleges","null");
            }else {
                Log.d("colleges", jsonstr);
            }
            if (jsonstr!=null){
                try {
                    JSONArray jsonArray=new JSONArray(jsonstr);
                    int len=jsonArray.length();
                    String colleges[]=new String[len+1];
                    for(int i=0;i<len;i++){
                        colleges[i]=jsonArray.getJSONObject(i).getString("college");
                    }
                    colleges[len]="Other";
                    return colleges;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return new String[]{"Other"};
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (strings==null||strings.length==0) {
                Toast.makeText(getActivity(), "Fetching Colleges Failed", Toast.LENGTH_SHORT).show();
            } else{
//                Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
//                if (string.equalsIgnoreCase("successful")) {
//                    getActivity().onBackPressed();
//                }
                ArrayAdapter collegeAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,strings);
                ((Spinner) getActivity().findViewById(R.id.spinnerCollege)).setAdapter(collegeAdapter);
                ((Spinner) getActivity().findViewById(R.id.spinnerCollege)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String college=((TextView)view).getText().toString();
                        if (college.equalsIgnoreCase("other")){
                            getActivity().findViewById(R.id.editTextCollege).setVisibility(View.VISIBLE);
                        }else {
                            getActivity().findViewById(R.id.editTextCollege).setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
    }
}
