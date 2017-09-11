package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import chris.sesi.com.utils.ImageFilePath;
import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class FormNuevaConsultora extends AppCompatActivity {
    private EditText edtNombre, edtNivel, edtDireccion, edtTelefono;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private Intent intent;
    private String id = "";
    private CircleImageView photoConsultora;
    private static final int PICK_IMAGE = 100;
    private static final int PICK_IMAGE_UPDATE = 111;
    private Uri uriPhotoResult;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nueva_consultora);
        edtNombre = (EditText) findViewById(R.id.edt_nombre_consultora);
        edtNivel = (EditText) findViewById(R.id.edt_nivel_consultora);
        edtDireccion = (EditText) findViewById(R.id.edt_direccion_consultora);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_consultora);
        Button btnGuardar = (Button) findViewById(R.id.btn_saveConsultora);
        TextView tvTitle = (TextView) findViewById(R.id.registro_title);
        intent = getIntent();

        photoConsultora = (CircleImageView) findViewById(R.id.imgConsultora);

        if (intent.getStringExtra(ORIGIN_ACTIVITY).equals(ACTUALIZAR)){
            id = intent.getStringExtra("ID");
            if (!UtilsDml.ConsultoraDetail(getApplication(),id,edtNombre,edtTelefono,edtDireccion,edtNivel,photoConsultora)){
                Toast.makeText(this,getString(R.string.errorRegistro),Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            btnGuardar.setText(getString(R.string.Actualizar));
            tvTitle.setText(getString(R.string.Actualizar));
            photoConsultora.setOnClickListener(updatePhoto);
        } else {
            photoConsultora.setOnClickListener(altaPhoto);
        }

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.getStringExtra(ORIGIN_ACTIVITY).equals(ALTA)){
                    alta();
                }else {
                    actualiza();
                }
            }
        });
    }

    public void alta(){
        if (edtNombre.getText().toString().isEmpty()) {
            Toast.makeText(getApplication(), getString(R.string.camposVacios), Toast.LENGTH_LONG).show();
        } else {
            if (UtilsDml.altaConsultora(getApplication(), edtNombre.getText().toString(),
                    edtNivel.getText().toString(), edtDireccion.getText().toString(),
                    edtTelefono.getText().toString(),Uri.parse(selectedImagePath))){
                Toast.makeText(getApplication(), getString(R.string.RegistroExitoso), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplication(), getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void actualiza(){
        if (edtNombre.getText().toString().isEmpty()) {
            Toast.makeText(getApplication(), getString(R.string.camposVacios), Toast.LENGTH_LONG).show();
        } else {
            if (UtilsDml.consultoraUpdate(getApplication(),id,edtNombre.getText().toString(),
                    edtTelefono.getText().toString(),edtDireccion.getText().toString(),
                    edtNivel.getText().toString())){
                Toast.makeText(this,getString(R.string.actualizacionExitosa), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this,ConsultoraList.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this,getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }
        }
    }

    View.OnClickListener updatePhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openGalleryUpdate();
        }
    };

    View.OnClickListener altaPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openGallery();
        }
    };

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(FormNuevaConsultora.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(FormNuevaConsultora.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

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

    private void openGalleryUpdate() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(FormNuevaConsultora.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(FormNuevaConsultora.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE_UPDATE);*/
                galleryFilter(PICK_IMAGE_UPDATE);
            }
        }else {
            /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE_UPDATE);*/
            galleryFilter(PICK_IMAGE_UPDATE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            uriPhotoResult = data.getData();

            if (Build.VERSION.SDK_INT >= 23) {
                selectedImagePath = ImageFilePath.getPath(getApplication(), uriPhotoResult);
            } else {
                selectedImagePath = uriPhotoResult.toString();
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhotoResult);
                photoConsultora.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_UPDATE){
            uriPhotoResult = data.getData();

            if (Build.VERSION.SDK_INT >= 23) {
                selectedImagePath = ImageFilePath.getPath(getApplication(), uriPhotoResult);
            } else {
                selectedImagePath = uriPhotoResult.toString();
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhotoResult);
                photoConsultora.setImageBitmap(bitmap);
                if (!UtilsDml.updateImageClient(getApplication(),id,selectedImagePath)){
                    Toast.makeText(this,getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
