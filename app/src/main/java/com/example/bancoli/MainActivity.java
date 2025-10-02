package com.example.bancoli;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment; // Import para NavHostFragment
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.bancoli.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la Toolbar
        setSupportActionBar(binding.toolbar);

        // --- INICIO DE LA CORRECCIÓN ---
        // Obtener el NavHostFragment y luego su NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        // --- FIN DE LA CORRECCIÓN ---
        
        // Define los destinos de nivel superior
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_clientes).build();

        // Conectar el NavController con la Toolbar
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        
        // Conectar el NavController con la BottomNavigationView
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Usamos el mismo método robusto para obtener el NavController aquí también
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
