package chris.sesi.com.minegociomk;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import java.io.IOException;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;
import chris.sesi.com.utils.Utils;
import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fabNuevoCliente, fab, fabPrestar, fabAbono, fabVender;
    private LinearLayout fabLayoutNuevoCliente, fabLayoutPrestar, fabLayoutAbono, fabLayoutVender;
    private boolean isFABOpen = false;
    private TextView nav_user_name, nav_user_category, nav_email;
    private String _idEmail, _nivelMk, _name;
    private DrawerLayout drawer;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private CircleImageView circleImageViewUser;
    private static final int PICK_IMAGE = 100;
    private Uri uriImageUser;


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
                intent.putExtra(ORIGIN_ACTIVITY, ALTA);
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
        nav_email = (TextView) headerView.findViewById(R.id.nav_email);
        circleImageViewUser = (CircleImageView) headerView.findViewById(R.id.imageView);
        //obtenerUsuario();
        String[] result = new String[4];
        if (UtilsDml.obtenerUsuario(getApplication(),result)){
            _idEmail = (result[0]);
            nav_email.setText(result[0]);
            nav_user_name.setText(result[1]);
            nav_user_category.setText(result[2]);
            if (Uri.parse(result[3]) != null){
                if (Uri.parse(result[3]).toString().equals("")){
                    circleImageViewUser.setImageResource(R.drawable.femeie);
                }else {
                    circleImageViewUser.setImageURI(Uri.parse(result[3]));
                }
            }
        }


        circleImageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MenuPrincipal.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MenuPrincipal.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        }else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
          //  Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
            openGallery();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            if (UtilsDml.updateImageUser(getApplication(), _idEmail, imageUri.toString())) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    circleImageViewUser.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }

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
        Intent intent;
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_clientes:
                intent = new Intent(fabVender.getContext(), ClientList.class);
                startActivity(intent);
                break;

            case R.id.nav_ventas:

                break;

            case R.id.nav_inventario:
                intent = new Intent(fabVender.getContext(), Inventario.class);
                startActivity(intent);
                break;

            case R.id.nav_catalogo:
                intent = new Intent(fabVender.getContext(), Catalogo.class);
                startActivity(intent);
                break;

            case R.id.consultoras:
                intent = new Intent(fabVender.getContext(), ConsultoraList.class);
                startActivity(intent);
                break;

            case R.id.nav_user_profile:
                intent = new Intent(this,UserProfile.class);
                startActivity(intent);
                break;

            case R.id.nav_cerrarSesion:
                Utils.savePreferenceSesionAutomatico(getApplicationContext(), false);
                Utils.cerrarSesion(getApplication());
                finish();
                break;

            case R.id.nav_compartir:
                Utils.compartirRedesSociales(getApplication());
                break;
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

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID,
                ContractSql.User.COLUMN_NAME_NOMBRE,
                ContractSql.User.COLUMN_NAME_NIVELMK,
                ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER};
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
            _idEmail = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            _name = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NOMBRE));
            _nivelMk = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NIVELMK));
            uriImageUser = Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER)));

            if (uriImageUser != null) {
                if (uriImageUser.toString().equals("")){
                    circleImageViewUser.setImageResource(R.drawable.femeie);
                }else {
                    circleImageViewUser.setImageURI(uriImageUser);
                }
            }

            nav_email.setText(_idEmail);
            nav_user_name.setText(_name);
            nav_user_category.setText(_nivelMk);
            nav_user_category.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(this, getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        cursor.close();
        db.close();
    }
}

