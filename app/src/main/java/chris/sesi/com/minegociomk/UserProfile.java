package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import chris.sesi.com.utils.ImageFilePath;
import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private EditText edt_email, edt_nombre, edt_nivel, edt_pass, edt_userMk, edt_passMk;
    private CircleImageView img_profile;
    private String[] result = new String[7];
    private static final int PICK_IMAGE = 100;
    private String _idEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

    }

    public void init(){
        edt_email = (EditText) findViewById(R.id.profile_email);
        edt_nombre = (EditText) findViewById(R.id.profile_nombre);
        edt_nivel = (EditText) findViewById(R.id.profile_nivel);
        edt_pass = (EditText) findViewById(R.id.profile_pass);
        edt_userMk = (EditText) findViewById(R.id.profile_userMK);
        edt_passMk = (EditText) findViewById(R.id.profile_passMK);
        img_profile = (CircleImageView) findViewById(R.id.circleImageViewUser);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openGallery();
                //Toast.makeText(getApplication(),"Click",Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hablitaComponentes();
            }
        });

        if (UtilsDml.obtenerUsuarioForProfile(getApplication(),result)){

            _idEmail = result[0];
            edt_email.setText(result[0]);
            edt_nombre.setText(result[1]);
            edt_nivel.setText(result[2]);
            edt_userMk.setText(result[3]);
            edt_passMk.setText(result[4]);
            edt_pass.setText(result[5]);


            if (Build.VERSION.SDK_INT >= 23){

                if (result[6].equals("")) {
                    img_profile.setImageResource(R.drawable.femeie);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeFile(result[6]);
                    img_profile.setImageBitmap(bitmap);
                }

            } else {
                if (Uri.parse(result[6]) != null){
                    if (Uri.parse(result[6]).toString().equals("")){
                        img_profile.setImageResource(R.drawable.femeie);
                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(result[6]));
                            img_profile.setImageBitmap(bitmap);

                        }catch (IOException e){e.printStackTrace();}
                    }
                }
            }


        }

    }

    public void hablitaComponentes(){
        edt_email.setEnabled(true);
        edt_nombre.setEnabled(true);
        edt_nivel.setEnabled(true);
        edt_pass.setEnabled(true);
        edt_userMk.setEnabled(true);
        edt_passMk.setEnabled(true);
        img_profile.setEnabled(true);
    }

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(UserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(UserProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

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

            String selectedImagePath;
            if (Build.VERSION.SDK_INT >= 23){
                selectedImagePath = ImageFilePath.getPath(getApplication(),imageUri);
            }else {
                selectedImagePath = imageUri.toString();
            }

            if (UtilsDml.updateImageUser(getApplication(), _idEmail, selectedImagePath)) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    img_profile.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }

        }
    }
}
