package com.example.mimaps.ui.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mimaps.MapsActivity;
import com.example.mimaps.MiPosicion;
import com.example.mimaps.R;
import com.example.mimaps.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private Button btnUbicacion;
    private Button btnMap;
    private EditText txtLat;
    private EditText txtLon;
    private EditText txtAlti;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btnUbicacion = root.findViewById(R.id.obtenerUbi);
        btnUbicacion.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        txtLat = root.findViewById(R.id.editLat);
        txtLon = root.findViewById(R.id.editLong);
        txtAlti = root.findViewById(R.id.editAlti);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void verificar(){
        if (!txtAlti.getText().toString().equals("")|| !txtLon.getText().toString().equals("") || !txtLat.getText().toString().equals("")){
            Intent intent = new Intent(getContext(), MapsActivity.class);
            intent.putExtra("latitud", txtLat.getText().toString());
            intent.putExtra("longitud", txtLon.getText().toString());
            intent.putExtra("altitud", txtAlti.getText().toString());
            startActivity(intent);
        }else{
            Toast.makeText(getContext(),"Los campos no estan llenos", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==btnUbicacion){
            miposicion();
        }
        if(v==btnMap){
            verificar();
        }
    }

    public void miposicion(){
        if(ContextCompat.checkSelfPermission((Activity) getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        LocationManager objLocation=null;
        LocationListener objLocListener;

        objLocation=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        objLocListener=new MiPosicion();
        objLocation.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,objLocListener);

        if (objLocation.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            txtLon.setText(MiPosicion.longitud+"");
            txtLat.setText(MiPosicion.latitud+"");
            txtAlti.setText(MiPosicion.altitud+"");
        }else {
            Toast.makeText(getContext(), "Gps desabilitado", Toast.LENGTH_LONG).show();
        }
    }
}