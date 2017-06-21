package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import chris.sesi.com.utils.Utils;
import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class FormNuevoCliente extends AppCompatActivity {

    private EditText nombre_cliente;
    private EditText direccion_clinete;
    private EditText telefono_cliente;
    private EditText ocupacion_cliente;
    private CircleImageView photoClient;
    private TextView title;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private Intent intent;
    private String id = "";
    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_UPDATE = 111;
    private Uri uriPhotoResult;
    private Uri uriPhotoClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nuevo_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        photoClient = (CircleImageView) findViewById(R.id.photoClient);
        nombre_cliente = (EditText) findViewById(R.id.nuevoClienteNombre);
        direccion_clinete = (EditText) findViewById(R.id.nuevoClienteDireccion);
        telefono_cliente = (EditText) findViewById(R.id.nuevoClienteTelefono);
        ocupacion_cliente = (EditText) findViewById(R.id.nuevoClienteOcupacion);
        title = (TextView) findViewById(R.id.tvTitleCliente);
        Button btn_guardar_cliente = (Button) findViewById(R.id.btn_nuevoClienteOk);

        if (intent.getStringExtra(ORIGIN_ACTIVITY).equals(ACTUALIZAR)){
            String[] result = new String[5];
            photoClient.setOnClickListener(updateListener);
             id = intent.getStringExtra("ID");
            if (!UtilsDml.ClientDetail(getApplication(),id, result)){
                Toast.makeText(this,getString(R.string.errorRegistro),Toast.LENGTH_LONG).show();
                onBackPressed();
            } else{
                nombre_cliente.setText(result[0]);
                telefono_cliente.setText(result[1]);
                direccion_clinete.setText(result[2]);
                ocupacion_cliente.setText(result[3]);

                if (Uri.parse(result[4]) != null){
                    if (Uri.parse(result[4]).toString().equals("")){
                        photoClient.setImageResource(R.drawable.femeie);
                    } else {
                        photoClient.setImageURI(Uri.parse(result[4]));
                    }
                }

            }


            btn_guardar_cliente.setText(getString(R.string.Actualizar));
            title.setText(getString(R.string.Actualizar));
        } else {
            photoClient.setOnClickListener(altaListener);
        }


        btn_guardar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (intent.getStringExtra(ORIGIN_ACTIVITY).equals(ALTA)){
                   alta(v);
               }else {
                   actualiza(v);
               }
            }
        });

    }

    public void alta(View v){
        if (nombre_cliente.getText().toString().equals("")) {
            Snackbar.make(v, getResources().getString(R.string.camposVacios), Snackbar.LENGTH_LONG).show();

        } else {
            if (uriPhotoResult == null){
                uriPhotoResult = Uri.parse("");
            }
            if (UtilsDml.altaCliente(getApplication(), nombre_cliente.getText().toString(),
                    direccion_clinete.getText().toString(), telefono_cliente.getText().toString(),
                    ocupacion_cliente.getText().toString(),uriPhotoResult)){
                Toast.makeText(this,getString(R.string.RegistroExitoso), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), ClientList.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }

        }
    }

    public void actualiza(View v){
        if (nombre_cliente.getText().toString().equals("")) {
            Snackbar.make(v, getResources().getString(R.string.camposVacios), Snackbar.LENGTH_LONG).show();

        } else {
            if (UtilsDml.ClientUpdate(getApplication(), id, nombre_cliente.getText().toString(),
                    telefono_cliente.getText().toString(),direccion_clinete.getText().toString(),
                    ocupacion_cliente.getText().toString())){
                Toast.makeText(this,getString(R.string.actualizacionExitosa), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,ClientList.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(FormNuevoCliente.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(FormNuevoCliente.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        }else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }

    }

    private void openGalleryUpdate() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(FormNuevoCliente.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(FormNuevoCliente.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE_UPDATE);
            }
        }else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE_UPDATE);
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
            uriPhotoResult = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhotoResult);
                photoClient.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_UPDATE){
            uriPhotoResult = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhotoResult);
                photoClient.setImageBitmap(bitmap);
                if (!UtilsDml.updateImageClient(getApplication(),id,uriPhotoResult.toString())){
                    Toast.makeText(this,getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    View.OnClickListener updateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openGalleryUpdate();

        }
    };

    View.OnClickListener altaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openGallery();

        }
    };
}
