package com.example.diarioestudanteretrofit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

/**
 * A MainActivity é responsável por configurar a navegação no aplicativo usando um Drawer Layout
 * e a biblioteca de navegação do Android Jetpack.
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura a barra de ferramentas (Toolbar) como a ActionBar do aplicativo
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configura o Drawer Layout para a navegação lateral
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Recupera o fragmento NavHostFragment, que contém o NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);

        // Obtém o NavController que é responsável por gerenciar a navegação
        NavController navController = navHostFragment.getNavController();

        // Configuração da AppBarConfiguration para determinar os destinos de navegação principais
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)  // Defina os destinos de nível superior (como o Home)
                .setOpenableLayout(drawer)  // Configura o Drawer Layout
                .build();

        // Configura a ActionBar para trabalhar com o NavController
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        // Configura o NavigationView para trabalhar com o NavController
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    /**
     * Este método é chamado quando o botão de navegação para cima (Up) é pressionado.
     * Ele garante que a navegação aconteça corretamente no caso de fragmentos.
     */
    @Override
    public boolean onSupportNavigateUp() {
        // Obtém o NavController do fragmento que está atualmente no topo
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // Navega para cima na pilha de navegação (ou chama o método de navegação para cima do sistema)
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
