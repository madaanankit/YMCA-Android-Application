package com.example.finalpay;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadnavheader(navigationView);
        loadFragment(R.id.personal_details);
        toolbar.setTitle("Personal Details");
    }

    private void loadnavheader(NavigationView n){
        View hView =  n.getHeaderView(0);
        TextView nav_user = hView.findViewById(R.id.navHeaderTextView);
        SharedPreferences sh = getSharedPreferences("login", MODE_PRIVATE);
        String roll=sh.getString("roll","");
        nav_user.setText(roll);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        loadFragment(id);
        return true;
    }

    private void loadFragment(int id) {

        Fragment selected = null;
        switch(id){
            case R.id.personal_details:
                selected = new PersonalDetails();
                toolbar.setTitle("Personal Details");
                break;
            case R.id.edu_details:
                selected = new EducationDetails();
                toolbar.setTitle("Education Details");
                break;
            case R.id.certification:
                selected = new Certifications();
                toolbar.setTitle("Certifications");
                break;
            case R.id.opportunities:
                selected = new Opportunities();
                toolbar.setTitle("Opportunities");
                break;
            case R.id.contact_us:
                selected = new ContactUs();
                toolbar.setTitle("Contact Us");
                break;

            case R.id.appliedfor:
                selected = new Applications();
                toolbar.setTitle("Applications");
                break;

            case R.id.logout:
                Intent intent=new Intent(this,StudentRegister.class);
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                startActivity(intent);
                finish();
                break;
        }
        if(selected != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment,selected);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}