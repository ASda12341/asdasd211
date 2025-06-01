package com.cinefan.app.actividades;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.cinefan.app.R;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
    
    private GoogleMap mapa;
    // coordenadas de la sede ficticia de cinefan en reus
    private static final double LATITUD_CINEFAN = 41.1551;
    private static final double LONGITUD_CINEFAN = 1.1055;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        
        // obtiene el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        
        // crea la ubicacion de cinefan
        LatLng cinefan = new LatLng(LATITUD_CINEFAN, LONGITUD_CINEFAN);
        
        // agrega el marcador personalizado
        mapa.addMarker(new MarkerOptions()
                .position(cinefan)
                .title("CineFan HQ")
                .snippet("Sede central de CineFan")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcador_cinefan)));
        
        // mueve la camara a la ubicacion
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(cinefan, 15));
        
        // habilita controles del mapa
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setCompassEnabled(true);
    }
}