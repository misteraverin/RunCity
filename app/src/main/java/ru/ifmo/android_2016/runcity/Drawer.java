package ru.ifmo.android_2016.runcity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by Averin Maxim on 21.12.2016.
 */

abstract class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_competition) {
            startActivity(new Intent(getApplicationContext(), Competitions.class));
        } else if (id == R.id.nav_questions) {
            startActivity(new Intent(getApplicationContext(), Tasks.class));
        } else if (id == R.id.nav_timer) {
            startActivity(new Intent(getApplicationContext(), Timer.class));
        } else if (id == R.id.nav_email) {
            startActivity(new Intent(getApplicationContext(), EmailProblem.class));
        } else if (id == R.id.nav_exit) {
            startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        }

        return true;
    }
}
