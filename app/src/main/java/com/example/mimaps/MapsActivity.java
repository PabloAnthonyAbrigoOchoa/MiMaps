package com.example.mimaps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mimaps.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, View.OnClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String longitud, latitud;
    SharedPreferences preferences;
    private Button btnFavorito,btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnFavorito = (Button) findViewById(R.id.favorito);
        btnEliminar = (Button) findViewById(R.id.limpiar);
        btnFavorito.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);

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

        longitud = getIntent().getStringExtra("longitud");
        latitud = getIntent().getStringExtra("latitud");

        //Se convierte los datos a Double
        double lon = Double.parseDouble(longitud);
        double lat = Double.parseDouble(latitud);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng miUbicacion = new LatLng(lat,lon);
        mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi Ubicacion"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        CameraUpdate ZoomCam = CameraUpdateFactory.zoomTo(16);
        mMap.animateCamera(ZoomCam);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(MapsActivity.this, "Click Posicion"+latLng.latitude+latLng.longitude, Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Mi Ubicacion").icon(BitmapDescriptorFactory.fromResource(R.drawable.googleg_disabled_color_18)));
        guardarPreferencias(latLng);
    }

    public void guardarPreferencias(LatLng latLng){
        preferences = getSharedPreferences("My Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putFloat("latitud",(float) latLng.latitude);
        editor.putFloat("longitud",(float)latLng.longitude);
        editor.commit();
    }

    public void cargarPreferencias(){
        double lat=preferences.getFloat("latitud",0);
        double log=preferences.getFloat("longitud",0);
        if (lat!=0){
            LatLng puntoPref=new LatLng(lat,log);
            mMap.addMarker(new MarkerOptions().position(puntoPref).title("Mi ubicacion").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(puntoPref));
        }else{
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            alert.setTitle("No tiene ningun sitio Favorito");
            alert.setPositiveButton("OK",null);
            alert.create().show();
        }
        Toast.makeText(MapsActivity.this,
                "mi favorito es: "+lat+log,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v==btnFavorito){
            cargarPreferencias();
        }
        if (v==btnEliminar){
            SharedPreferences.Editor editor=preferences.edit().clear();
        }
    }
}