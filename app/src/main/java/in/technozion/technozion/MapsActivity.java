package in.technozion.technozion;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import in.technozion.technozion.helper.AbstractRouting;
import in.technozion.technozion.helper.Route;
import in.technozion.technozion.helper.Routing;
import in.technozion.technozion.helper.RoutingListener;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

;

public class MapsActivity extends AppCompatActivity implements RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,MyDialog.Communicator{
    protected GoogleMap map;
    protected LatLng start;
    protected LatLng end;
    private String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private ProgressDialog progressDialog;
    private Polyline polyline;

    //request location
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    //Location mLastLocation;
    Button button1;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(ActivityRecognition.API)
                .addOnConnectionFailedListener(this)
                .build();
/*
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d("Loxation",String.valueOf(mLastLocation));
        }
        */

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

        addMarkers();

    }

    public void buttonMarker()
    {
        button1 = (Button) findViewById(R.id.popButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(MapsActivity.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener)v);
                //registering popup with OnMenuItemClickListener
               /* popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        return true;
                    }
                });*/

                popup.show(); //showing popup menu
            }
        });
    }

    public void func(int id)
    {
        Toast.makeText(this, String.valueOf(id) + "was clicked", Toast.LENGTH_SHORT).show();
    }
    public void func1(int id)
    {
        Toast.makeText(this, String.valueOf(id) + "was un clicked", Toast.LENGTH_SHORT).show();
    }


    public void addMarkers()
    {

        Marker marker=map.addMarker(new MarkerOptions()
                .position(new LatLng(17.984055, 79.530788))
                .title("Hover Mania").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


        final List<Marker> mList=new ArrayList<>();
        mList.add(marker);


        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                for (Marker marker : mList) {
                    if (Math.abs(marker.getPosition().latitude - latLng.latitude) < 0.0005 && Math.abs(marker.getPosition().longitude - latLng.longitude) < 0.0005) {
                        Location mLastLocation = LocationServices.FusedLocationApi
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
        Log.d("lat cordinate", String.valueOf(start.latitude));
        Log.d("long cordinate", String.valueOf(start.longitude));
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
        myDialog.show(fm,"select");

    }

    @Override
    public void onDialogMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        //request info get latlong--- with the event and workshop

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.one:
                item.setChecked(!item.isChecked()); Toast.makeText(this, "one clicked", Toast.LENGTH_SHORT).show();    return true;
            case R.id.two:
                item.setChecked(!item.isChecked());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    }



