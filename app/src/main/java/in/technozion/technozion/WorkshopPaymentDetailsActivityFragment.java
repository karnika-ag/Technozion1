package in.technozion.technozion;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.technozion.technozion.Data.Util;
import in.technozion.technozion.adapters.WorkshopsAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class WorkshopPaymentDetailsActivityFragment extends Fragment {

    public static final String registerurl="http://172.30.132.78/tz-registration-master/workshops/make_payment";
    private String string;
    ListView listViewWorkshopDetailsConfirmation;

    public WorkshopPaymentDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workshop_payment_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        string = getActivity().getIntent().getStringExtra("data");
        Log.d("on confirm page", string);
        try{

            JSONObject jsonObject= new JSONObject(string);
            JSONArray username =  jsonObject.getJSONArray("username");
            JSONArray hospitality =  jsonObject.getJSONArray("hospitality");
            JSONArray registration =  jsonObject.getJSONArray("registration");
            int len=username.length();
            List<HashMap<String,String>> values=new ArrayList<>();


            for(int i=0;i<len;i++)
            {
                HashMap<String,String> value=new HashMap<>();
                value.put("username", username.getString(i));
                value.put("hospitality", hospitality.getString(i));
                value.put("registration", registration.getString(i));
                values.add(value);
            }

            // Log.d("Workshop Cost:", String.valueOf(jsonObject.getInt("workshopCost")));
            Log.d("Hospitality Cost:",String.valueOf(jsonObject.getInt("hospitalityCost")));
            Log.d("Registration Cost:",String.valueOf(jsonObject.getInt("registrationCost")));
            Log.d("Transaction Cost:",String.valueOf(jsonObject.getInt("transactionCharges")));
            Log.d("Total Cost:", String.valueOf(jsonObject.getInt("totalCost")));

            listViewWorkshopDetailsConfirmation= (ListView) getActivity().findViewById(R.id.listViewWorkshopDetailsConfirmation);
            listViewWorkshopDetailsConfirmation.setAdapter(new WorkshopsAdapter(getActivity(), R.layout.workshop_boxes, values));


            // ((TextView)getActivity().findViewById(R.id.textViewWorkshopCost)).setText("Workshop Cost:" + jsonObject.getInt("workshopCost"));
            ((TextView)getActivity().findViewById(R.id.textViewHospitalityCost)).setText("Hospitality Cost:" + jsonObject.getInt("hospitalityCost"));
            ((TextView)getActivity().findViewById(R.id.textViewRegistrationCost)).setText("Registration Cost:" +  jsonObject.getInt("registrationCost"));
            ((TextView)getActivity().findViewById(R.id.textViewTransactionCharges)).setText("Transaction Charges:" +  jsonObject.getInt("transactionCharges"));
            ((TextView)getActivity().findViewById(R.id.textViewTotalCost)).setText("Total Cost:" +  jsonObject.getInt("totalCost"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        getActivity().findViewById(R.id.buttonRegisterConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new RegisterEventTask().execute(string);

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



            String jsonstr= Util.getStringFromURL(registerurl, map);
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

                if (string == null) {
                    Log.d("enter in if", "okk");
                    Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();


                } else {
                    JSONObject jsonObjectRec = new JSONObject(string);
                    if (jsonObjectRec.getString("status").equals("success")) {
                        Log.d("enter in else if", "okk");

                        Toast.makeText(getActivity(), jsonObjectRec.getString("message"), Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d("enter in else part", "okk");
                        Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        }
    }
