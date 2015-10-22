package in.technozion.technozion;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import in.technozion.technozion.Data.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sai Teja on 10/6/2015.
 */
public class MyDialog extends DialogFragment implements View.OnClickListener{
    Button yes,no;
    protected Spinner spinner,spinner2;
    Communicator communicator;
    protected ArrayAdapter<CharSequence> adapter;
    protected ArrayAdapter<CharSequence> adapter1;
    protected ArrayAdapter<String> adapter2;
    String ans=null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator= (Communicator) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_dialog,null);
        getDialog().setTitle("Get Directions..");
        yes=(Button)v.findViewById(R.id.yes);
        no=(Button)v.findViewById(R.id.no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this) ;
        spinner = (Spinner) v.findViewById(R.id.planets_spinner);
        spinner2=(Spinner)v.findViewById(R.id.second_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.planets_array, android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 2) {
                    adapter1= ArrayAdapter.createFromResource(getActivity(), R.array.death_array, android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter1);
                } else {
                    new FetchTask().execute("1");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               ans= (String) parent.getItemAtPosition(position);
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }



    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.yes)
        {
            communicator.onDialogMessage(ans);
            dismiss();
        }
        else
        {
            dismiss();
        }
    }


    interface Communicator
    {
        public void onDialogMessage(String message);
    }

    public class FetchTask extends AsyncTask< String,Void,ArrayList<String>> {

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
        protected ArrayList<String> doInBackground(String... strings) {

            if(strings[0]==null) return null;
            HashMap<String,String> data=new HashMap();
            data.put("num", strings[0]);
            String jsonstr= Util.getStringFromURL("http://androidconnect.16mb.com/sampleFetch.php", data);

            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject=new JSONObject(jsonstr);
                    JSONArray jsonArr= jsonObject.getJSONArray("names");
                    ArrayList<String> arrNames=new ArrayList<String>();
                    for(int i=0;i<jsonArr.length();i++)
                        arrNames.add((String)jsonArr.get(i));
                    return arrNames;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void  onPostExecute(ArrayList<String> arrNames) {
            super.onPostExecute(arrNames);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            if(arrNames!=null) {
                adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrNames);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner2.setAdapter(adapter2);
            }
        }
    }


}
