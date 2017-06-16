package chris.sesi.com.minegociomk;

import android.animation.Animator;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fabNuevoCliente, fab, fabPrestar, fabAbono, fabVender;
    private LinearLayout fabLayoutNuevoCliente, fabLayoutPrestar, fabLayoutAbono, fabLayoutVender;
    private boolean isFABOpen = false;
    private TextView nav_user_name, nav_user_category;
    private String _nivelMk, _name;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fabLayoutNuevoCliente = (LinearLayout) findViewById(R.id.fabLayoutNcliente);
        fabLayoutPrestar = (LinearLayout) findViewById(R.id.fabLayoutPrestar);
        fabLayoutAbono = (LinearLayout) findViewById(R.id.fabLayoutAbono);
        fabLayoutVender = (LinearLayout) findViewById(R.id.fabLayoutVender);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabNuevoCliente = (FloatingActionButton) findViewById(R.id.fabNuevoCliente);
        fabPrestar = (FloatingActionButton) findViewById(R.id.fabPrestar);
        fabAbono = (FloatingActionButton) findViewById(R.id.fabAbono);
        fabVender = (FloatingActionButton) findViewById(R.id.fabVender);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
            }
        });

        fabNuevoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FormNuevoCliente.class);
                startActivityForResult(intent, 0);
            }
        });
        fabVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fabVender.getContext(), Venta.class);
                startActivityForResult(intent, 0);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) headerView.findViewById(R.id.nav_user_name);
        nav_user_category = (TextView) headerView.findViewById(R.id.nav_user_category);
        obtenerUsuario();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_clientes) {
            Intent intent = new Intent(fabVender.getContext(), ClientList.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_ventas) {


        } else if (id == R.id.nav_inventario) {
            Intent intent = new Intent(fabVender.getContext(), Inventario.class);
            startActivity(intent);

        } else if (id == R.id.nav_catalogo) {
            Intent intent = new Intent(fabVender.getContext(), Catalogo.class);
            startActivity(intent);

        }else if (id == R.id.consultoras){
            Intent intent = new Intent(fabVender.getContext(), FormNuevaConsultora.class);
            startActivity(intent);
        }else if (id == R.id.nav_user) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void showFABMenu() {
        isFABOpen = true;
        fabLayoutNuevoCliente.setVisibility(View.VISIBLE);
        fabLayoutPrestar.setVisibility(View.VISIBLE);
        fabLayoutAbono.setVisibility(View.VISIBLE);
        fabLayoutVender.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(-45);
        fabLayoutPrestar.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayoutAbono.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayoutVender.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
        fabLayoutNuevoCliente.animate().translationY(-getResources().getDimension(R.dimen.standard_190));
    }

    private void closeFABMenu() {
        isFABOpen = false;

        fab.animate().rotationBy(45);
        fabLayoutNuevoCliente.animate().translationY(0);
        fabLayoutPrestar.animate().translationY(0);
        fabLayoutAbono.animate().translationY(0);
        fabLayoutVender.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (!isFABOpen) {
                    fabLayoutNuevoCliente.setVisibility(View.GONE);
                    fabLayoutPrestar.setVisibility(View.GONE);
                    fabLayoutAbono.setVisibility(View.GONE);
                    fabLayoutVender.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    public void obtenerUsuario() {

      /*  Bundle bundle = getIntent().getExtras();
        String usuario = bundle.getString("usuario");
        String pass = bundle.getString("pass"); */
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_NOMBRE,
                ContractSql.User.COLUMN_NAME_NIVELMK};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            _name = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NOMBRE));
            _nivelMk = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NIVELMK));

            nav_user_name.setText(_name);
            nav_user_category.setText(_nivelMk);
            nav_user_category.setVisibility(View.VISIBLE);

        } else {
        }
        cursor.close();
        db.close();
    }
}

