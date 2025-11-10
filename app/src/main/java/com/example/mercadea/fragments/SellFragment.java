package com.example.mercadea.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mercadea.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class SellFragment extends Fragment implements SensorEventListener {

    private MapView mapView;
    private TextView tvSensor;

    private SensorManager sensorManager;
    private Sensor accelerometer;

    private Marker activeMarker; // marcador que se mueve con acelerómetro
    private final List<Marker> otherMarkers = new ArrayList<>(); // marcadores adicionales
    private boolean markerPlaced = false;

    // Variables para suavizado
    private double lat, lon;
    private static final double SMOOTHING_FACTOR = 0.1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context ctx = getContext();
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));

        View view = inflater.inflate(R.layout.sell_activity, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        tvSensor = (TextView) view.findViewById(R.id.tvSensor);

        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18.0);
        mapView.getController().setCenter(new GeoPoint(-16.489689, -68.119293));

        // Listener de toque en mapa
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    GeoPoint point = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

                    Marker marker = new Marker(mapView);
                    marker.setPosition(point);
                    marker.setTitle(markerPlaced ? "Marcador adicional" : "Mi marcador");
                    mapView.getOverlays().add(marker);
                    mapView.invalidate();

                    if (!markerPlaced) {
                        activeMarker = marker;
                        lat = point.getLatitude();
                        lon = point.getLongitude();
                        markerPlaced = true;
                    } else {
                        otherMarkers.add(marker);
                    }
                }
                return false;
            }
        });

        // Inicializar acelerómetro
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!markerPlaced || activeMarker == null) return;

        float x = event.values[0];
        float y = event.values[1];

        tvSensor.setText("X: " + x + "\nY: " + y);

        // Suavizado del movimiento
        double targetLat = lat - y * 0.0001;
        double targetLon = lon + x * 0.0001;

        lat += (targetLat - lat) * SMOOTHING_FACTOR;
        lon += (targetLon - lon) * SMOOTHING_FACTOR;

        GeoPoint newPoint = new GeoPoint(lat, lon);
        activeMarker.setPosition(newPoint);
        mapView.getController().setCenter(newPoint);
        mapView.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
