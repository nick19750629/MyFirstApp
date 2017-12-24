package com.example.nick.myfirstapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Fragment;
import android.app.FragmentManager;

import com.example.nick.constant.constant;
import com.example.nick.db.DatabaseHelper;
import com.example.nick.db.dicOperation;
import com.example.nick.domain.Practice;
import com.example.nick.myfirstapp.fragment.HabitFrag;
import com.example.nick.myfirstapp.fragment.HistoryFrag;
import com.example.nick.myfirstapp.fragment.ImportFrag;
import com.example.nick.myfirstapp.fragment.ImprovementFrag;
import com.example.nick.myfirstapp.fragment.MainFrag;
import com.example.nick.myfirstapp.fragment.NaviFrag;
import com.example.nick.myfirstapp.fragment.SumFrag;
import com.example.nick.myfirstapp.fragment.TestFrag;
import com.example.nick.myfirstapp.fragment.TodoFrag;
import com.example.nick.myfirstapp.fragment.WelcomeFrag;
import com.example.nick.util.baseUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int subject;
    private TextView txt;
    private TextView tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt = (TextView) findViewById(R.id.txtWord);
        tip = (TextView) findViewById(R.id.txtTip);
        txt.setText(MainActivity.this.getString(R.string.app_name));
        tip.setText(MainActivity.this.getString(R.string.welcome));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //TODO invisible
        fab.setVisibility(this.getWindow().getCurrentFocus().INVISIBLE);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Toast.makeText(MainActivity.this,"设置功能 怎么开发？",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this.getApplicationContext(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();

        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
            fragment = new WelcomeFrag();
        } else if (id == R.id.nav_gallery) {
            fragment = new MainFrag();
            Toast.makeText(MainActivity.this,"暂时保留。",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_slideshow) {
            fragment = new MainFrag();
            Toast.makeText(MainActivity.this,"暂时保留。",Toast.LENGTH_LONG).show();
        } else */
        if (id == R.id.nav_manage) {
            fragment = new TodoFrag();
        } else if (id == R.id.nav_history) {
            fragment = new HistoryFrag();
        } else if (id == R.id.nav_habit) {
            fragment = new HabitFrag();
        } else if (id == R.id.nav_recite) {
            fragment = new NaviFrag();
            Bundle bundle = new Bundle();
            bundle.putString("course", constant.SUB_ENGLISH);
            fragment.setArguments(bundle);
            //fragment = new MainFrag();
        } else if (id == R.id.nav_sum) {
            fragment = new SumFrag();
        } else if (id == R.id.nav_practise) {
            fragment = new NaviFrag();
            Bundle bundle = new Bundle();
            bundle.putString("course", constant.SUB_CHINESE);
            fragment.setArguments(bundle);
            //fragment = new YuwenFrag();
        } else if (id == R.id.nav_import) {
            fragment = new ImportFrag();
        } else if (id == R.id.nav_improve) {
            fragment = new ImprovementFrag();
            //Toast.makeText(MainActivity.this,"暂时保留。",Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_share) {
            fragment = new TodoFrag();
        } else if (id == R.id.nav_process) {
            fragment = new TestFrag();
        } else if (id == R.id.nav_send) {
            //sendMail();
            fragment = new TodoFrag();
            //Toast.makeText(MainActivity.this,"暂时保留。",Toast.LENGTH_LONG).show();
        }else{
            fragment = new MainFrag();
        }
        fragmentManager.beginTransaction().replace(R.id.main_container, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
