package chris.sesi.com.minegociomk;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    private EditText edt_email, edt_nombre, edt_nivel, edt_pass, edt_userMk, edt_passMk;
    private CircleImageView img_profile;
    private Uri uriImageProfile;
    private String cadenaUri;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.profile_fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hablitaComponentes();
            }
        });

        if (UtilsDml.obtenerUsuarioForProfile(getApplication(),edt_email,edt_nombre,edt_nivel,edt_pass,edt_userMk,edt_passMk,uriImageProfile)){
          //  uriImageProfile = Uri.parse(cadenaUri);
            if (uriImageProfile != null){
                if (uriImageProfile.toString().equals("")){
                    img_profile.setImageResource(R.drawable.femeie);
                } else {
                    img_profile.setImageURI(uriImageProfile);
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

}
