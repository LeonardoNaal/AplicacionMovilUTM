package com.fsociety.linkutmbetty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class UserMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PublicacionesFragment.OnFragmentInteractionListener, ActividadesFragment.OnFragmentInteractionListener, UsuarioPubFragment.OnFragmentInteractionListener, SitiosFragment.OnFragmentInteractionListener, UserPublicacionesFragment.OnFragmentInteractionListener,PublicacionesEspecificasFragment.OnFragmentInteractionListener, FragmentoDeLista.OnFragmentInteractionListener {
    ListView listaUsuario;
    TextView txtMatricula;
    ArrayList<publicacion> image;
    public String dato;
    public int grado;
    public String grupo;
    public String carrera;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {//ver si contiene datos
            dato = (String) extras.get("Matricula");//Obtengo la matriculs
            grado=(int)extras.get("grado");
            grupo=(String) extras.get("grupo");
            carrera=(String)extras.get("carrera");
        }

        //Asignar matricula a textview
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Referencia al botón redondo
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Este código será reemplazado por un intent para llevar a otra actividad (AgregarPublicación)
                Intent intent = new Intent(UserMainActivity.this, ActividadPrueba.class);
                intent.putExtra("Matricula", dato);
                intent.putExtra("grado",grado);
                intent.putExtra("grupo",grupo);
                intent.putExtra("carrera",carrera);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        //LayoutInflater.from(this).inflate(R.layout.nav_header_user_main, null);
        txtMatricula = (TextView) header.findViewById(R.id.txtMat);
        txtMatricula.setText(dato);

        Fragment fragment = null;
        Class fragmentClass = UsuarioPubFragment.class;
        try {
            final Bundle arguments = new Bundle();
            arguments.putString("id", dato);
            fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_user_main, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            System.exit(0);
        } else {
            System.exit(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
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
            Intent intent = new Intent(UserMainActivity.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    Boolean fragmentSelect = false;

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_camera) {
            fragment = new UserPublicacionesFragment();
            final Bundle argument = new Bundle();
            argument.putString("idUser", dato);
            argument.putInt("grado",grado);
            argument.putString("grupo",grupo);
            argument.putString("carrera",carrera);
            fragment.setArguments(argument);
            fragmentSelect = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_user_main, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());

        }else if(id == R.id.mis_pub){
            fragment = null;
            Class fragmentClass = UsuarioPubFragment.class;
            try {
                final Bundle arguments = new Bundle();
                arguments.putString("id", dato);
                arguments.putInt("grado",grado);
                arguments.putString("grupo",grupo);
                arguments.putString("carrera",carrera);
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(arguments);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_user_main, fragment).commit();

        }else if(id==R.id.pubs_mi){
            fragment = null;
            Class fragmentClass = PublicacionesEspecificasFragment.class;
            try {
                final Bundle arguments = new Bundle();
                arguments.putString("id", dato);
                arguments.putInt("grado",grado);
                arguments.putString("grupo",grupo);
                arguments.putString("carrera",carrera);
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(arguments);
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_user_main, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());
        }
        else if (id == R.id.nav_gallery) {
            fragment = new ActividadesFragment();
            fragmentSelect = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_user_main, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());

        } else if (id == R.id.nav_slideshow) {
            fragment = new SitiosFragment();
            fragmentSelect = true;;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_user_main, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());

        } else if (id == R.id.nav_slideshow) {
            fragment = new SitiosFragment();
            fragmentSelect = true;
            getSupportFragmentManager().beginTransaction().replace(R.id.content_user_main, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());

        } else if (id == R.id.nav_manage) {
            //Horarios
            fragment = new FragmentoDeLista();
            try
            {
                final Bundle arguments = new Bundle();
                arguments.putInt("Grado",grado);
                arguments.putString("Grupo",grupo);
                arguments.putString("Carrera",carrera);
                fragment.setArguments(arguments);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            fragmentSelect = true;
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_user_main, fragment).commit();
            getSupportActionBar().setTitle(item.getTitle());

        } else if (id == R.id.nav_share) {
            //Cerrar sesión
            Intent intent = new Intent(UserMainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(item.getTitle());
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
