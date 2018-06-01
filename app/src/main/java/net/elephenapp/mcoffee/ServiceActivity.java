package net.elephenapp.mcoffee;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class ServiceActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private String  midString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

//        Get Value From Intent
        midString = getIntent().getStringExtra("mid");


//        Create Toobar
        createToobar();

//        Add MenuDrawer Fragment and DetailUserFragment
        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentMenuDrawer, new MenuDrawerFragment())
                    .commit();

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.contentServiceFragment, DetailUserFragment.detailUserInstance(midString))
                    .commit();

        }

    }//Main Method


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        if(item.getItemId() == R.id.itemSignOut){
            saveSharePreferance();


        }

        return super.onOptionsItemSelected(item);
    }

    private void saveSharePreferance() {

        SharedPreferences sharedPreferences = ServiceActivity.this
                .getSharedPreferences("MyCoffee", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mid","");
        editor.commit();

        Intent intent = new Intent(ServiceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_service, menu);
        return true;
    }

    private void createToobar() {
        toolbar = findViewById(R.id.toolbarService);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("This is Title");
        getSupportActionBar().setSubtitle(midString);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = findViewById(R.id.drawerService);
        actionBarDrawerToggle = new ActionBarDrawerToggle(ServiceActivity.this,
                drawerLayout,R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


    }
}// Main Class
