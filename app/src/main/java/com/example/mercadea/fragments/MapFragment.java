package com.example.mercadea.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mercadea.R;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapFragment extends Fragment {

    private MapView mapView;
    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context ctx = requireContext();
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18.0);

        // Posición inicial del marcador
        GeoPoint startPoint = new GeoPoint(-16.489689, -68.119293);
        mapView.getController().setCenter(startPoint);

        marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Mercadea ubicación");
        mapView.getOverlays().add(marker);

        return view;
    }

    public void updateMarker(double lat, double lon) {
        GeoPoint newPoint = new GeoPoint(lat, lon);
        marker.setPosition(newPoint);
        mapView.getController().animateTo(newPoint);
        mapView.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}
