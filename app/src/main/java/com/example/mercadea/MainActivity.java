package com.example.mercadea;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mercadea.fragments.AccountFragment;
import com.example.mercadea.fragments.ChatFragment;
import com.example.mercadea.fragments.HomeFragment;
import com.example.mercadea.fragments.ProductFragment;
import com.example.mercadea.fragments.SellFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNV;
    private static final int JOB_ID = 1;
    private TextView tvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        bottomNV = findViewById(R.id.bottomNV);
        tvMenu = findViewById(R.id.tvMenu);

        // Fragment inicial
        replaceFragment(new HomeFragment());
        bottomNV.setSelectedItemId(R.id.item_inicio);

        bottomNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;

                if (id == R.id.item_inicio) {
                    fragment = new HomeFragment();
                    tvMenu.setText("Inicio");
                } else if (id == R.id.item_chat) {
                    fragment = new ChatFragment();
                    tvMenu.setText("Chat");
                } else if (id == R.id.item_producto) {
                    fragment = new ProductFragment();
                    tvMenu.setText("Producto");
                } else if (id == R.id.item_vender) {
                    fragment = new SellFragment();
                    tvMenu.setText("Vender");
                } else if (id == R.id.item_cuenta) {
                    fragment = new AccountFragment();
                    tvMenu.setText("Cuenta");
                }

                if (fragment != null) {
                    replaceFragment(fragment);
                    return true;
                }

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentlayout, fragment)
                .commit();
    }

    // BroadcastReceiver Bluetooth
    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                String mensaje;

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mensaje = "Bluetooth apagado";
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mensaje = "Bluetooth encendido";
                        break;
                    default:
                        mensaje = "Estado desconocido de Bluetooth";
                        break;
                }

                Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();

                // Programar Job
                JobInfo jobInfo = getJobInfo(MainActivity.this);
                JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
                if (scheduler != null) {
                    scheduler.cancel(JOB_ID);
                    int result = scheduler.schedule(jobInfo);
                    if (result == JobScheduler.RESULT_SUCCESS) {
                        Toast.makeText(context, "Job programado por cambio de Bluetooth", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Fallo al programar el job", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    private static JobInfo getJobInfo(MainActivity mainActivity) {
        ComponentName componentName = new ComponentName(mainActivity, BluetoothJobNotification.class);
        return new JobInfo.Builder(JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
                .setMinimumLatency(200)
                .setOverrideDeadline(5000)
                .setPersisted(false)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Registrar BroadcastReceiver para Bluetooth
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(bluetoothReceiver);
    }
}
