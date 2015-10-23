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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import in.technozion.technozion.Data.Util;

/**
 * Created by Sai Teja on 10/22/2015.
 */
public class PopDialog extends DialogFragment   {

    CheckBox futureevents,allevents,others;
    Communicator communicator;
    String global;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator=(Communicator)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_popupdialog,null);
        getDialog().setTitle("Pick the events !");
        futureevents= (CheckBox) v.findViewById(R.id.popfutureevents);
        allevents= (CheckBox) v.findViewById(R.id.popallevents);
        others= (CheckBox) v.findViewById(R.id.popothers);

        futureevents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    global="futureevents";
                   new FetchTaskCord().execute("futureevents");
                }
                else
                {
                    global="";
                    communicator.onDialogMessage1("futureevents");
                }
            }
        });
        allevents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    global="allevents";
                    new FetchTaskCord().execute("allevents");
                }
                else
                {
                    global="";
                    communicator.onDialogMessage1("allevents");
                }
            }
        });
        others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    global="others";
                    new FetchTaskCord().execute("others");
                }
                else
                {
                    global="";
                    communicator.onDialogMessage1("others");
                }
            }
        });
//        futureevents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                CheckBox item=((CheckBox) buttonView);
//                item.setChecked(!item.isChecked());
//                if(item.isChecked())
//                {
//                    global="futureevents";
//                    new FetchTaskCord().execute("futureevents");
//
//                }
//                else
//                {
//                    global="";
//                    communicator.onDialogMessage1("futureevents");
//                }
//            }
//        });
//        allevents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                CheckBox item=((CheckBox) buttonView);
//                item.setChecked(!item.isChecked());
//                if(item.isChecked())
//                {
//                    global="allevents";
//                    new FetchTaskCord().execute("allevents");
//
//                }
//                else
//                {
//                    global="";
//                    communicator.onDialogMessage1("allevents");
//                }
//            }
//        });
//        others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                CheckBox item=((CheckBox) buttonView);
//                item.setChecked(!item.isChecked());
//                if(item.isChecked())
//                {
//                    global="others";
//                    new FetchTaskCord().execute("others");
//
//                }
//                else
//                {
//                    global="";
//                    communicator.onDialogMessage1("others");
//                }
//            }
//        });
        return v;
    }


    interface Communicator
    {
        public void onDialogMessage(ArrayList<MarkerItem> items);
        public void onDialogMessage1(String s);
    }


    public class FetchTaskCord extends AsyncTask< String,Void,ArrayList<MarkerItem>> {

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
        protected ArrayList<MarkerItem> doInBackground(String... strings) {

            if(strings[0]==null) return null;
            HashMap<String,String> data=new HashMap();
            data.put("eventType", strings[0]);
            String jsonstr= Util.getStringFromURL("", data);

            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONObject jsonObject=new JSONObject(jsonstr);
                    JSONArray jsonArr= jsonObject.getJSONArray("events");
                    ArrayList<MarkerItem> arrNames=new ArrayList<MarkerItem>();

                    for(int i=0;i<jsonArr.length();i++) {
                        JSONObject feedObj = (JSONObject) jsonArr.get(i);
                        MarkerItem markerItem = new MarkerItem();
                        markerItem.setMarkerId(feedObj.getInt("eventId"));
                        markerItem.setEventName(feedObj.getString("eventName"));
                        markerItem.setEventVenue(feedObj.getString("eventVenue"));
                        markerItem.setEventLat(Double.valueOf(feedObj.getString("lat")));
                        markerItem.setEventLong(Double.valueOf(feedObj.getString("long")));
                        markerItem.setEventtype(global);
                        arrNames.add(markerItem);
                    }
                        return arrNames;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void  onPostExecute(ArrayList<MarkerItem> arrNames) {
            super.onPostExecute(arrNames);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            communicator.onDialogMessage(arrNames);

            return;
        }
    }


}
