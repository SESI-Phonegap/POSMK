package chris.sesi.com.view.activity;

import android.Manifest;
import android.animation.Animator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import chris.sesi.com.data.model.ModelUser;
import chris.sesi.com.view.utils.ImageFilePath;
import chris.sesi.com.view.utils.Utils;
import chris.sesi.com.view.utils.UtilsDml;
import chris.sesi.com.view.R;
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
                startActivity(intent);
            }
        });
        fabVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fabVender.getContext(), Venta.class);
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
        View headerView = navigationView.getHeaderView(0);
        nav_user_name = (TextView) headerView.findViewById(R.id.nav_user_name);
        nav_user_category = (TextView) headerView.findViewById(R.id.nav_user_category);
        nav_email = (TextView) headerView.findViewById(R.id.nav_email);
        circleImageViewUser = (CircleImageView) headerView.findViewById(R.id.imageView);
        //obtenerUsuario();
        updateData();
        circleImageViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateData();
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
                /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);*/
                galleryFilter(PICK_IMAGE);
            }
        }else {
            /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);*/
            galleryFilter(PICK_IMAGE);
        }

    }
    public void galleryFilter(int code){
        List<Intent> targetGalleryIntents = new ArrayList<>();
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(galleryIntent,0);
        if (!resInfos.isEmpty()){
            for (ResolveInfo resInfo : resInfos){
                String packageName = resInfo.activityInfo.packageName;

                if (!packageName.contains("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus")){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setPackage(packageName);
                    targetGalleryIntents.add(intent);
                }
            }

            if (!targetGalleryIntents.isEmpty()){
                Collections.sort(targetGalleryIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetGalleryIntents.remove(0), "Abrir Galeria");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetGalleryIntents.toArray(new Parcelable[]{}));
                startActivityForResult(chooserIntent,PICK_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(),"No se encontro la galeria",Toast.LENGTH_LONG).show();
            }
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
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {


            Uri imageUri = data.getData();

            String selectedImagePath;
            if (Build.VERSION.SDK_INT >= 23){
                selectedImagePath = ImageFilePath.getPath(getApplication(),imageUri);
            }else {
                selectedImagePath = imageUri.toString();
            }

            if (UtilsDml.updateImageUser(getApplication(), _idEmail, selectedImagePath)) {
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

            case R.id.miUnidad:
                intent = new Intent(fabVender.getContext(), MiUnidad.class);
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

    public void updateData(){
        ModelUser user = UtilsDml.obtenerUsuario(getApplication());
        if (null != user){
            _idEmail = (user.getIdEmail());
            nav_email.setText(user.getIdEmail());
            nav_user_name.setText(user.getNombre());
            nav_user_category.setText(user.getNivelMk());

            if (Build.VERSION.SDK_INT >= 23){

                    if (user.getSrcPhoto().equals("")) {
                        circleImageViewUser.setImageResource(R.drawable.femeie);
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(user.getSrcPhoto());
                        circleImageViewUser.setImageBitmap(bitmap);
                    }

            } else {
                if (null != Uri.parse(user.getSrcPhoto())) {
                    if (Uri.parse(user.getSrcPhoto()).toString().equals("")) {
                        circleImageViewUser.setImageResource(R.drawable.femeie);
                    } else {
                        try {
                              Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(user.getSrcPhoto()));
                              circleImageViewUser.setImageBitmap(bitmap);
                            //    circleImageViewUser.setImageURI(Uri.parse(result[3]));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

}

