package com.example.semana13_gmaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.internal.ICameraUpdateFactoryDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener , GoogleApiClient.OnConnectionFailedListener , GoogleMap.OnMyLocationButtonClickListener,GoogleMap.OnMyLocationClickListener {

    private GoogleMap mMap;

    private  static  final  LatLng PIURA= new LatLng(-5.1944900, -80.6328200);
    private  static  final  LatLng BOGOTA= new LatLng(4.60971, -74.08175);
    private  static  final  LatLng RIO= new LatLng(-22.970722, -43.182365);

    private Marker mPiura,mBogota,mRio;
    private Button btnUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PIURA, 2));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8), 6000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(PIURA)
                .zoom(4)
                .bearing(30)
                .tilt(30)
                .build();


        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //market lima
        mPiura = mMap.addMarker(new MarkerOptions()
                .position(PIURA)

                .title("Piura").draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        mPiura.setTag(0);


//bogota
        mBogota = mMap.addMarker(new MarkerOptions()
                .position(BOGOTA)
                .title("Bogota"));
        mPiura.setTag(0);


//rio
        mRio = mMap.addMarker(new MarkerOptions()
                .position(RIO).snippet("Poblacion:209'300,000")
                .draggable(true)
                .title("Rio de janeiro"));
        mRio.setTag(0);


        mMap.setOnMarkerClickListener(this);
        mMap.addPolygon(new PolygonOptions()
                .add(PIURA, BOGOTA, RIO)
                .strokeColor(Color.BLUE)
                .fillColor(Color.GREEN));


        float[] results = new float[1];
        Location.distanceBetween(PIURA.latitude, PIURA.longitude, BOGOTA.latitude, BOGOTA.latitude, results);


        showDistance();
        btnUbicacion=findViewById(R.id.btnUbicacion);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);


        }
        btnUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermisoUbicacion()) {

                    Toast.makeText(getApplicationContext(), "Brindo permisos", Toast.LENGTH_SHORT).show();

                } else {

                    requestStoragePermission();
                }
            }
        });
    }

    private boolean PermisoUbicacion() {

        int result = ContextCompat.checkSelfPermission(getApplicationContext() ,Manifest.permission.ACCESS_FINE_LOCATION);
     boolean exito = false;
     if(result ==PackageManager.PERMISSION_GRANTED){

         exito= true;
     }
     return  exito;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},0);
    }
















    private void showDistance() {

        double  distance = SphericalUtil.computeDistanceBetween(PIURA,BOGOTA);
        Toast.makeText(getApplicationContext(), "La distancia de PIURA A BOGOTA es de :"+ formatNumber(distance), Toast.LENGTH_SHORT).show();

    }

    private String formatNumber(double distance) {
        String unit = "m";
        if(distance<1){


            distance *= 1000;
            unit ="mmm";

        }else if(distance >1000){
            distance /= 1000;
            unit ="km";



        }

return String.format("%4.3f%s" ,distance , unit);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickount = (Integer)marker.getTag();
        if(clickount!=null){
            clickount=clickount+1;
            marker.setTag(clickount);
            Toast.makeText(this,
                    marker.getTitle()+"ha sido clickeado"+clickount+"veces",
                    Toast.LENGTH_SHORT).show();

        }
        return false;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }
}








