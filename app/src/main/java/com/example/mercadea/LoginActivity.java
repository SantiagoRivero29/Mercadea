package com.example.mercadea;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);

        // Asigna el botón desde el XML
        buttonLogin = findViewById(R.id.btnLogin);

        // Evento de click
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí podrías validar el login si tuvieras un formulario
                // Si todo está bien, lanzamos la MainActivity

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // 🔸 Opcional: para que no se pueda volver con el botón atrás
            }
        });
    }
}
