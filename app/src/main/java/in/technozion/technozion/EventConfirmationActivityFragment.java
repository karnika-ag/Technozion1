package in.technozion.technozion;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
public class EventConfirmationActivityFragment extends Fragment {


//    public static final String registerurl="http://192.168.87.50/tz-registration-master/events/registerteam_mobile";
    private String string;

    public EventConfirmationActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_confirmation, container, false);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        string = getActivity().getIntent().getStringExtra("data");
        Log.d("on confirm page", string);
        try{

            JSONObject jsonObject1= new JSONObject(string);
            JSONArray ja =  jsonObject1.getJSONArray("username");
            String names = ja.toString();
            names=names.replace("[","");
            names=names.replace("]","");
            names=names.replace("\"","");
            String[] strings=names.split(",");
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,strings);
            ((ListView)getActivity().findViewById(R.id.ListViewParticipants)).setAdapter(arrayAdapter);
         } catch (JSONException e) {
            e.printStackTrace();
       }

        getActivity().findViewById(R.id.buttonRegisterConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    new RegisterEventTask().execute(string);

            }
        });



    }

    public class RegisterEventTask extends AsyncTask<String,Void,String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("registering..");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... string) {

            if(string==null||string.length==0){
                return null;
            }
            HashMap<String,String> map = new HashMap<String,String>();

             map.put("data", string[0]);
             map.put("confirm","1");
            Log.d("check sending data ",string[0]);



            String jsonstr= Util.getStringFromURL(URLS.EVENT_REGISTER_CONFIRM_URL, map);
            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);


                return jsonstr ;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            try {

                JSONObject jsonObjectRec = new JSONObject(string);
                if (string==null) {
                    Log.d("enter in if","okk");
                    Toast.makeText(getActivity(),"Error, please try again",Toast.LENGTH_SHORT).show();


                } else if(jsonObjectRec.getString("status").equals("success")){
                    Log.d("enter in else if","okk");

                    Toast.makeText(getActivity(),jsonObjectRec.getString("message"),Toast.LENGTH_SHORT).show();

                } else{
                    Log.d("enter in else part","okk");
                    Toast.makeText(getActivity(),"Error, please try again",Toast.LENGTH_SHORT).show();

                }

            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
