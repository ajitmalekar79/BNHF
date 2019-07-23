package com.ajit.bnhfdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajit.bnhfdemo.Blog.DoctorBlog;
import com.ajit.bnhfdemo.remote.ApiUtils;
import com.ajit.bnhfdemo.remote.UserService;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ajit.bnhfdemo.DoctorProfile.drObj;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button search, btn;

    private LocationManager locationManager;
    //private Button btn;
    private LocationListener locationListener;
    private static double lattitude, longitude;
    private int proximityRadius=10000;
    UserService userService;

    TextView hospNameTV, cityTV;
    Button doctor;

    private BottomSheetBehavior bottomSheetBehavior;

    ArrayList<Doctor> doctorList;
    ArrayList<String> doctorNameList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        EditText editText = findViewById(R.id.editText);

        LinearLayout linearLayout = findViewById(R.id.bottomlinearLayout);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        hospNameTV = findViewById(R.id.hospitalNameinfo);
        cityTV = findViewById(R.id.cityinfo);
        doctor = findViewById(R.id.doctor);

        userService = ApiUtils.getUserService();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });*/

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        search = findViewById(R.id.button);
        btn = findViewById(R.id.btnloc);


        /**
         * Code for InfoWindow
         */

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = list.get(0);

                hospNameTV.setText(marker.getTitle());
                cityTV.setText(address.getAdminArea()+", "+address.getCountryName()+", "+address.getLocality()+", "+address.getPostalCode()+", "+address.getLocale());

                doctorList =new ArrayList<>();
                doctorNameList= new ArrayList<>();




                /*drList.add("Ajit");
                drList.add("Sujit");
                drList.add("vijay");*/

                listDoctor(marker.getPosition().latitude, marker.getPosition().longitude);



                return true;
            }
        });

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListView listView = findViewById(R.id.drList);

                ListAdapter adapter = new ArrayAdapter<>(MapsActivity.this,android.R.layout.simple_list_item_1,doctorNameList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        toast("Position: "+position+" And id: "+id);

                        Intent intent = new Intent(MapsActivity.this,DoctorInfo.class);
                        intent.putExtra("id", doctorList.get(position).getId());
                        intent.putExtra("drId", doctorList.get(position).getDoctorId());
                        intent.putExtra("password", doctorList.get(position).getPassword());
                        startActivity(intent);

                    }
                });
            }
        });


        /*
        ** Code for Search Place
         */

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SearchLoc();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /*
        Method for Self location

         */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mMap.clear();
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(MapsActivity.this, location.getLatitude() +" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                        //setMarker(location.getLatitude(),location.getLongitude());
                        lattitude = location.getLatitude();
                        longitude = location.getLongitude();


                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                };

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET
                        }, 10);
                    }
                    return;
                } else {
                    toast("Done");
                    plotHospital(lattitude,longitude);
                    //configureButton();
                }

                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);



            }
        });


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);

        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    /*
    ** Method to search place
     */

    public void SearchLoc() throws IOException {
        EditText editText = findViewById(R.id.editText);
        String location = editText.getText().toString();

        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();
        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();
        double lat = address.getLatitude();
        double lang = address.getLongitude();
        String city= address.getLocality();
        //setMarker(lat, lang, city);
        plotHospital(lat,lang);
    }

    /**
     * Code for Finding Hospital
     *
     */


    private void plotHospital(double lattitude, double longitude){

        Call<List<Hospital>> call = userService.showHospital(lattitude,longitude);

        Log.d("Hospital Ploting","Lat:"+lattitude+" Longi:"+longitude);
        toast("Hospital Ploting"+"Lat:"+lattitude+" Longi:"+longitude);


        call.enqueue(new Callback<List<Hospital>>() {
            @Override
            public void onResponse(Call<List<Hospital>> call, Response<List<Hospital>> response) {
                if(response.isSuccessful()){
                    List<Hospital> sortedHosp = response.body();

                    Log.d("Response", "Success");
                    toast("isSuccessfull");

                    for (Hospital hosp:sortedHosp){

                        Log.d("IN Sucess", hosp.getLatitude()+","+hosp.getLongitude());
                        setMarker(hosp.getLatitude(),hosp.getLongitude(),hosp.getHospName());
                        toast("IN Sucess"+ hosp.getLatitude()+","+hosp.getLongitude());

                    }

                    }else{
                    toast("Invalid Id.");
                }
            }

            @Override
            public void onFailure(Call<List<Hospital>> call, Throwable t) {


                toast("InFailure.."+t.getMessage());
            }
        });

    }


    private void listDoctor(double lat, double lang){

        Call<List<Doctor>> call = userService.listDoctor(lat,lang);

        call.enqueue(new Callback<List<Doctor>>() {
            @Override
            public void onResponse(Call<List<Doctor>> call, Response<List<Doctor>> response) {
                if(response.isSuccessful()){
                    List<Doctor> listDoct = response.body();

                    Log.d("Response", "Success");


                    //cityTV.setText(listDoct.get(0).getFirstName());
                    for (Doctor doctor:listDoct){

                        toast("Adding Doctor: "+doctor.getFirstName());

                        doctorNameList.add("Dr. "+doctor.getFirstName()+" "+doctor.getLastName());
                        doctorList.add(doctor);
                    }
                    toast("Doctors Added in list");

                }else{
                    toast("Invalid Id.");
                }
            }

            @Override
            public void onFailure(Call<List<Doctor>> call, Throwable t) {


                toast("InFailure.."+t.getMessage());
            }
        });

    }





    void toast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

    public void setMarker(double lat, double lang, String hospName) {

        //mMap.clear();
        //gotoLocation(lat, lang, 6);
        LatLng latLng = new LatLng(lat, lang);
        mMap.addMarker(new MarkerOptions().title(hospName).position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.search_icon)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 6);
        mMap.animateCamera(cameraUpdate);


    }
}
