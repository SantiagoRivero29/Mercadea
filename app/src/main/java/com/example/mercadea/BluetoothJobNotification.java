package com.example.mercadea;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BluetoothJobNotification extends JobService {

    private ExecutorService executorService;

    @Override
    public boolean onStartJob(final JobParameters params) {
        executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // Simulamos una tarea en segundo plano
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e("JobBluetooth", "Hilo interrumpido");
                }

                // Volvemos al hilo principal para mostrar el Toast
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BluetoothJobNotification.this, "JobBluetooth ejecutado", Toast.LENGTH_SHORT).show();
                    }
                });

                jobFinished(params, false);
            }
        });

        return true; // tarea en background
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
        }
        return false;
    }
}
