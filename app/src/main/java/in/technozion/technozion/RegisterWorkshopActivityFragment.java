package in.technozion.technozion;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import in.technozion.technozion.Data.URLS;

import in.technozion.technozion.Data.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterWorkshopActivityFragment extends Fragment implements AdapterView.OnItemSelectedListener {
// public static final String eventlisturl = "http://192.168.253.1/tz-registration-master/workshops/get_all_workshops_mobile";
    // public static final String registerurl = "http://192.168.253.1/tz-registration-master/workshops/registerteam_mobile";

    public static final String eventlisturl = "http://bhuichalo.com/tz15/get_all_workshops_mobile.json";
    public static final String registerurl = "http://bhuichalo.com/tz15/workshop_checkdetails.json";

    //public static final String eventlisturl = "http://192.168.87.50/tz-registration-master/workshops/get_all_workshops_mobile";
    //public static final String registerurl = "http://192.168.87.50/tz-registration-master/workshops/registerteam_mobile";

    //public static final String eventlisturl = "http://bhuichalo.com/tz15/get_all_workshops_mobile.json";
    //public static final String registerurl = "http://bhuichalo.com/tz15/workshop_checkdetails.json";


    Spinner spinner;
    private LinearLayout linearLayout;

    private ArrayList<Integer> ids;
    private ArrayList<String> names;
    private int cost;
    private ArrayList<Integer> costs;
    private int min;
    private ArrayList<Integer> mins;
    private int max;
    private ArrayList<Integer> maxs;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_workshop, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinner = (Spinner) getActivity().findViewById(R.id.spinnerNewEvents);
        linearLayout = (LinearLayout) getActivity().findViewById(R.id.linearLayoutParticipants);
        new LoadEventsTask().execute();

        getActivity().findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> tzIdList = new ArrayList<Integer>();
                int count = linearLayout.getChildCount();
                try {
                    for (int i = 0; i < count; i++) {
                        String text = ((EditText) linearLayout.getChildAt(i)).getText().toString();
                        if (text.equals("")) {
                            if (((EditText) linearLayout.getChildAt(i)).getHint().toString().equals("optional")) {
                                break;
                            }
                            throw new Exception("null");
                        }
                        int tzid = Integer.parseInt(text);
                        Log.d("tzid", "" + tzid);
                        if (tzIdList.contains(tzid)) {
                            throw new Exception("Already exists");
                        }
                        tzIdList.add(tzid);
                    }
                    new RegisterWorkshopTask().execute(tzIdList);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Please check the details", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        index=i;
        Toast.makeText(getActivity(), "" + mins.get(i) + " - " + maxs.get(i), Toast.LENGTH_SHORT).show();
        min = mins.get(i);
        max = maxs.get(i);
        cost = costs.get(i);
        ((TextView) getActivity().findViewById(R.id.textViewMinimumParticipantsValue)).setText("" + min);
        ((TextView) getActivity().findViewById(R.id.textViewMaximumParticipantsValue)).setText("" + max);
        ((TextView) getActivity().findViewById(R.id.textViewWorkshopCostValue)).setText("" + cost);
        linearLayout.removeAllViews();

        View view2 = getActivity().getLayoutInflater().inflate(R.layout.edit_text_tz_id, null);
        ((EditText) view2).setText("9346472");
        view2.setEnabled(false);
        linearLayout.addView(view2);

        for (int j = 1; j < min; j++) {
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.edit_text_tz_id, null);
            ((EditText) view1).setHint("*required");
            linearLayout.addView(view1);
        }
        for (int j = min; j < max; j++) {
            View view1 = getActivity().getLayoutInflater().inflate(R.layout.edit_text_tz_id, null);
            ((EditText) view1).setHint("optional");
            linearLayout.addView(view1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class LoadEventsTask extends AsyncTask<Void, Void, String[]> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("fetching workshop list..");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected String[] doInBackground(Void... voids) {

            String jsonstr = Util.getStringFromURL(eventlisturl);
            if (jsonstr != null) {
                Log.d("GOT FROM HTTP", jsonstr);

            try {

                JSONArray jsonArray = new JSONArray(jsonstr);
                int len = jsonArray.length();
                ids = new ArrayList<>();
                names = new ArrayList<>();
                mins = new ArrayList<>();
                maxs = new ArrayList<>();
                costs = new ArrayList<>();

                for (int i = 0; i < len; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    ids.add(jsonObject.getInt("workshopid"));
                    names.add(jsonObject.getString("wname"));
                    mins.add(jsonObject.getInt("min"));
                    maxs.add(jsonObject.getInt("max"));
                    costs.add(jsonObject.getInt("cost"));
                }
                String[] strings = new String[names.size()];
                for (int i = 0; i < names.size(); i++) {
                    strings[i] = names.get(i);
                }
                return strings;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (strings == null) {
                Toast.makeText(getActivity(), "Could not fetch workshops, please try again", Toast.LENGTH_SHORT).show();
            } else {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, strings);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(RegisterWorkshopActivityFragment.this);
            }
        }
    }


    public class RegisterWorkshopTask extends AsyncTask<ArrayList<Integer>, Void, String> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Varifying Details...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<Integer>... tzIdList) {

            if (tzIdList == null || tzIdList.length == 0 || ids==null || ids.size()==0) {
                return null;
            }
            int len = tzIdList.length;

            JSONObject jsonObject = new JSONObject();
            HashMap<String, String> map = new HashMap<String, String>();

            try {
                jsonObject.put("workshopid", "" + ids.get(index));
                for (int i = 0; i < len; i++) {
                    jsonObject.put("userids", tzIdList[i].toString());
                }
                Log.d("json string to send", jsonObject.toString());

                map.put("data", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            String jsonstr = Util.getStringFromURL(URLS.WORKSHOP_REGISTER_URL, map);
//            String jsonstr = Util.getStringFromURL(registerurl, map);
            if (jsonstr != null) {
                Log.d("GOT FROM HTTP", jsonstr);

                return jsonstr;

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


                //Log.d("Return msg", jsonObjectRec.getString("message"));
                //return jsonObjectRec.getString("message");

                if (string == null) {
                    Toast.makeText(getActivity(), "Error, please try again", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObjectRec = new JSONObject(string);
                    if(jsonObjectRec.getString("status").equals("failure")){
                    Log.d("enter in else if","okk");

                    Toast.makeText(getActivity(),jsonObjectRec.getString("message"),Toast.LENGTH_SHORT).show();

                }else {

                    Log.d("enterd in else part ",string);

                    Intent i = new Intent(getActivity(), WorkshopPaymentDetailsActivity.class);
                    i.putExtra("data", string);
                    startActivity(i);
                    Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
                }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
