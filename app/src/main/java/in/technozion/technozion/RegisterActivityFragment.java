package in.technozion.technozion;

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

        String[] gender=getActivity().getResources().getStringArray(R.array.gender);
        ArrayAdapter genderAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,gender);
        ((Spinner) getActivity().findViewById(R.id.spinnerGender)).setAdapter(genderAdapter);
        String[] city=getActivity().getResources().getStringArray(R.array.city);
        ArrayAdapter cityAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,city);
        ((Spinner) getActivity().findViewById(R.id.spinnerCity)).setAdapter(cityAdapter);
        ((Spinner) getActivity().findViewById(R.id.spinnerCity)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city=((TextView)view).getText().toString();
                new FetchCollegeTask().execute(city);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String[] state=getActivity().getResources().getStringArray(R.array.state);
        ArrayAdapter stateAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,state);
        ((Spinner) getActivity().findViewById(R.id.spinnerState)).setAdapter(stateAdapter);

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

                if (name == null || email == null || phone == null || college == null || id == null || password == null) {
                    Log.d("null", "sth is null");
                } else{
                    regdata.put("name", name);
                    regdata.put("email", email);
                    regdata.put("gender", gender);
                    regdata.put("phone", phone);
                    regdata.put("city", city);
                    regdata.put("college", college);
                    regdata.put("id", id);
                    regdata.put("state", state);
                    regdata.put("password", password);
                }
                Log.d("gender", ((TextView)( (Spinner) getActivity().findViewById(R.id.spinnerGender)).getSelectedView()).getText().toString());

                new RegisterTask().execute(regdata);
            }
        });
    }


    public class RegisterTask extends AsyncTask<HashMap<String,String>,Void,String> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Registering..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HashMap<String ,String> ... hashMaps) {
            if (hashMaps==null||hashMaps.length==0){
                return null;
            }
            return Util.getStringFromURL(URLS.REGISTER_URL, hashMaps[0]);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (string==null) {
                Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                if (string.equalsIgnoreCase("successful")) {
                    getActivity().onBackPressed();
                }
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
            String jsonstr= Util.getStringFromURL(URLS.FETCH_COLLEGES_URL);
            if (jsonstr!=null){
                try {
                    JSONArray jsonArray=new JSONArray(jsonstr);
                    int len=jsonArray.length();
                    String colleges[]=new String[len+1];
                    for(int i=0;i<len;i++){
                        colleges[i]=jsonArray.getString(i);
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
