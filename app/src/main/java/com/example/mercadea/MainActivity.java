package com.example.mercadea;

import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        bottomNV = findViewById(R.id.bottomNV);

        // Fragment inicial
        replaceFragment(new HomeFragment());
        bottomNV.setSelectedItemId(R.id.item_inicio); // esto marca "Inicio" como seleccionado

        bottomNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = null;

                if (id == R.id.item_inicio) {
                    fragment = new HomeFragment();
                } else if (id == R.id.item_chat) {
                    fragment = new ChatFragment();
                } else if (id == R.id.item_producto) {
                    fragment = new ProductFragment();
                } else if (id == R.id.item_vender) {
                    fragment = new SellFragment();
                } else if (id == R.id.item_cuenta) {
                    fragment = new AccountFragment();
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



    private void verFragmentInicio(){

          TextView tituloTextView =  findViewById(R.id.tvMenu);
        // NOTA: 'requireView()' se usa dentro de un Fragment.
        // Si estás en una Activity, solo usarías 'findViewById(R.id.mi_titulo);'

        // 2. Usar el método 'setText()' para cambiar su contenido.
        tituloTextView.setText("Inicio");

    }


    private void verFragmentChat(){

        TextView tituloTextView =  findViewById(R.id.tvMenu);
        // NOTA: 'requireView()' se usa dentro de un Fragment.
        // Si estás en una Activity, solo usarías 'findViewById(R.id.mi_titulo);'

        // 2. Usar el método 'setText()' para cambiar su contenido.
        tituloTextView.setText("Chat");

    }

    private void verFragmentProducto(){

        TextView tituloTextView =  findViewById(R.id.tvMenu);
        // NOTA: 'requireView()' se usa dentro de un Fragment.
        // Si estás en una Activity, solo usarías 'findViewById(R.id.mi_titulo);'

        // 2. Usar el método 'setText()' para cambiar su contenido.
        tituloTextView.setText("Producto");

    }

    private void verFragmentVender(){

        TextView tituloTextView =  findViewById(R.id.tvMenu);
        // NOTA: 'requireView()' se usa dentro de un Fragment.
        // Si estás en una Activity, solo usarías 'findViewById(R.id.mi_titulo);'

        // 2. Usar el método 'setText()' para cambiar su contenido.
        tituloTextView.setText("Vender");

    }


}
