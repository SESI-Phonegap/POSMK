package chris.sesi.com.view.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import chris.sesi.com.data.model.ModelUser;
import chris.sesi.com.view.utils.Utils;
import chris.sesi.com.view.utils.UtilsDml;
import chris.sesi.com.view.R;


public class MainActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = (EditText) findViewById(R.id.sesion_email);
        pass = (EditText) findViewById(R.id.sesion_pass);
        Button btn_sesion = (Button) findViewById(R.id.btn_isesion);
        Button btn_registro = (Button) findViewById(R.id.btn_sesion_registro);
        final CheckBox chbSesion = (CheckBox) findViewById(R.id.checkBox_sesion);

        if (UtilsDml.checkUserExist(getApplication())){
            btn_registro.setEnabled(false);
            btn_registro.setAlpha(0.4f);
        }

        if (Utils.getPreferenceDefaultSesionAutomatic(this)){
            Intent intent = new Intent(getApplication(), MenuPrincipal.class);
            startActivity(intent);
            finish();
        }

        btn_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _user = usuario.getText().toString();
                final String _pass = pass.getText().toString();
                if (UtilsDml.iniciarSesion(getApplication(), _user, _pass)){
                    if (chbSesion.isChecked()){
                        Utils.savePreferenceSesionAutomatico(getApplication(),true);
                    }
                    Intent intent = new Intent(getApplication(), MenuPrincipal.class);
                    startActivity(intent);
                    finish();
                }else {
                    Snackbar.make(v, getResources().getString(R.string.Error_Sesion), Snackbar.LENGTH_LONG).show();

                }
            }


        });

        ModelUser user = UtilsDml.obtenerUsuario(getApplication());
        if (null != user){
            btn_registro.setEnabled(false);
            btn_registro.setAlpha(0.5f);

        } else {
            btn_registro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), Registro.class);
                    startActivity(intent);
                }
            });
        }

    }

}
