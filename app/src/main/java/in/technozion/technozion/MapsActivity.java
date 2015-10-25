package in.technozion.technozion;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import in.technozion.technozion.Data.URLS;
import in.technozion.technozion.Data.Util;
import in.technozion.technozion.helper.AbstractRouting;
import in.technozion.technozion.helper.Route;
import in.technozion.technozion.helper.Routing;
import in.technozion.technozion.helper.RoutingListener;
import in.technozion.technozion.helper.Promptmap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;



public class MapsActivity extends AppCompatActivity implements RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,MyDialog.Communicator,PopDialog.Communicator{
    protected GoogleMap map;
    protected ArrayList<Marker> toDelete;
    final List<Marker> mList=new ArrayList<>(); ;
    protected LatLng start;
    protected LatLng end;
    private String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private Polyline polyline;
    ArrayList<MarkerItem> arrayMain=null;

    //request location
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    //Location mLastLocation;
    Button button1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.myPrimaryDarkColor));
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addOnConnectionFailedListener(this)
                .build();


        arrayMain=new ArrayList<MarkerItem>();
        buttonMarker();
        MapsInitializer.initialize(this);
        mGoogleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        map = mapFragment.getMap();

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(17.984055, 79.530788));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        map.moveCamera(center);
        map.animateCamera(zoom);

        Promptmap.displayPromptForEnablingGPS(this);
        addMarkers();



    }

    public void buttonMarker()
    {
        button1 = (Button) findViewById(R.id.popButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                showDialog1(v);
            }
        });
    }




    public void addMarkers()
    {
        Log.d("coming","here");
        new FetchJsonLink().execute("4951310");


        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                for (Marker marker : mList) {
                    if (Math.abs(marker.getPosition().latitude - latLng.latitude) < 0.0005 && Math.abs(marker.getPosition().longitude - latLng.longitude) < 0.0005) {
                        Location mLastLocation;
                        mLastLocation = LocationServices.FusedLocationApi
                                .getLastLocation(mGoogleApiClient);

                        if (mLastLocation != null) {
                            start = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                        }
                        end = marker.getPosition();

                        Map<Marker, String> MarkersMap = new WeakHashMap<Marker, String>();
                        MarkersMap.put(marker, "Event");

                        route();
                        break;
                    }
                }

            }
        });
    }


    public void route()
    {
        Location mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if(mLastLocation!= null)
            start=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());

        Marker marker=map.addMarker(new MarkerOptions().position(start).title("You Are Here"));
        mList.add(marker);
        Log.d("lat cordinate", String.valueOf(end.latitude));
        Log.d("long cordinate", String.valueOf(end.longitude));
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching  Information.", true);
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .waypoints(start, end)
                    .build();
            routing.execute();
    }


    @Override
    public void onRoutingFailure() {
        progressDialog.dismiss();
        Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route)
    {
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        map.moveCamera(center);


        if(polyline!=null)
            polyline.remove();

        polyline=null;
        //adds route to the map.
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(getResources().getColor(R.color.primary_dark));
        polyOptions.width(10);
        polyOptions.addAll(mPolyOptions.getPoints());
        polyline=map.addPolyline(polyOptions);

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(start);
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
        map.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(end);
        //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green));
        map.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {

        Log.i(LOG_TAG, "Operation was cancelled.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.v(LOG_TAG, connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void showDialog(View v)
    {
        FragmentManager fm=getFragmentManager();
        MyDialog myDialog=new MyDialog();
        myDialog.show(fm, "select");

    }


    public void showDialog1(View v)
    {
        FragmentManager fm=getFragmentManager();
        PopDialog myDialog=new PopDialog();
        myDialog.show(fm, "pop");

    }

    @Override
    public void onDialogMessage(ArrayList<MarkerItem> items) {
        if(items==null)
        {
            Log.d("arrayMian","array is null");
            return;
        }
        arrayMain.addAll(items);
        for(MarkerItem item:arrayMain)
        {
            Marker marker =map.addMarker(new MarkerOptions().position(new LatLng(item.getEventLat(), item.getEventLong())).title(item.getEventName()).snippet(item.getEventVenue()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mList.add(marker);
        }
    }

    @Override
    public void onDialogMessage1(String s) {
        toDelete=new ArrayList<Marker>();
        for(MarkerItem item:arrayMain)
        {
           for(Marker marker:mList)
           {
               if(item.getEventName().equals(marker.getTitle()))
               {
                   toDelete.add(marker);
               }
           }
        }
        for(Marker m:toDelete)
        {
            m.remove();
            mList.remove(m);
        }

    }

    public void removeMarker(MarkerItem item)
    {
        Marker toRemove=null;
        LatLng latLng=new LatLng(item.getEventLat(),item.getEventLong());
        for(Marker marker:mList)
        {
            if(marker.getPosition()==latLng)
            {
                toRemove=marker;
                marker.setVisible(false);
            }
        }
        mList.remove(toRemove);
    }

    @Override
    public void onDialogMessage(String message) {
        new Fetchc().execute(message);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                item.setChecked(!item.isChecked());    return true;
            case R.id.two:
                item.setChecked(!item.isChecked());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class Fetchc extends AsyncTask<String,Void,HashMap<String ,String>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Getting Event Details");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected HashMap<String ,String> doInBackground(String... strings) {

            if(strings[0]==null)
                return null;

            HashMap<String,String> hashMapPostData=new HashMap();
            hashMapPostData.put("eventname", strings[0]);
            HashMap<String, String> hashMap;
            hashMap = new HashMap<>();
            hashMap.put("name",strings[0]);

            String jsonstr= Util.getStringFromURL("http://technozion.org/tz15/home/get_lat_long",hashMapPostData);

            if (jsonstr!=null) {
                try {
                    JSONArray jsonArray=new JSONArray(jsonstr);
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    Log.d("tagme",jsonArray.toString());

                    hashMap.put("lat", jsonObject.getString("latitude"));
                        hashMap.put("long", jsonObject.getString("longitude"));

                        return hashMap;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String ,String> hashMap) {
            super.onPostExecute(hashMap);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if (hashMap==null) {
                return;
            }


            double latitude=Double.parseDouble(hashMap.get("lat"));
            double longitude=Double.parseDouble(hashMap.get("long"));
            end=new LatLng(latitude,longitude);
            Marker marker=map.addMarker(new MarkerOptions().position(end).title(hashMap.get("name")));
            mList.add(marker);
            route();
        }
    }

    public class FetchJsonLink extends AsyncTask<String,Void,String>{

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Fetching Event Locations");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(params[0]==null)
                return null;
            HashMap<String,String> data=new HashMap();
            data.put("userid", params[0]);
            Log.d("userid",params[0]);
            String jsonstr= Util.getStringFromURL("http://technozion.in/events/get_registered_events_map", data);
            if(jsonstr==null) {
                Log.d("tname","json is null");
                return null;
            }
            Log.d("link1", jsonstr);
            return jsonstr;
        }

        protected void  onPostExecute(String json) {
            super.onPostExecute(json);
            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }
            if(json==null)
            {
                Log.d("tagreg","not working");
                return;
            }

            new FetchTaskCord().execute(json);

            return;
        }

    }

    public class FetchTaskCord extends AsyncTask< String,Void,ArrayList<MarkerItem>> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MapsActivity.this);
            progressDialog.setMessage("Fetching Event Locations");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected ArrayList<MarkerItem> doInBackground(String... strings) {

            if(strings[0]==null) return null;
            HashMap<String,String> data=new HashMap();
            data.put("data", strings[0]);
            Log.d("tag me one",strings[0]);
            String jsonstr= Util.getStringFromURL("http://technozion.org/tz15/home/get_userevents_schedule_mobile", data);

            if (jsonstr!=null) {
                Log.d("GOT FROM HTTP", jsonstr);
                try {
                    JSONArray jsonArr= new JSONArray(jsonstr);
                    ArrayList<MarkerItem> arrNames=new ArrayList<MarkerItem>();
                    Log.d("jsonArray",jsonArr.toString());
                    for(int i=0;i<jsonArr.length();i++) {
                        JSONObject feedObj = (JSONObject) jsonArr.get(i);
                        MarkerItem markerItem = new MarkerItem();
                        markerItem.setMarkerId(Integer.valueOf(feedObj.getString("id")));
                        markerItem.setEventName(feedObj.getString("event_name"));
                        markerItem.setEventVenue(feedObj.getString("place_name"));
                        markerItem.setEventLat(Double.valueOf(feedObj.getString("latitude")));
                        markerItem.setEventLong(Double.valueOf(feedObj.getString("longitude")));

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

            if(arrNames==null)
                Log.d("tagreg","fucked");
            for ( MarkerItem item:arrNames)
            {
                Marker marker =map.addMarker(new MarkerOptions().position(new LatLng(item.getEventLat(),item.getEventLong())).title(item.getEventName()).snippet(item.getEventVenue()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mList.add(marker);
            }

            return;
        }
    }


    }



