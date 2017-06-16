package chris.sesi.com.minegociomk;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import chris.sesi.com.utils.Util;

public class Registro extends AppCompatActivity {

    private EditText idEmail;
    private EditText nombre;
    private EditText pass;
    private EditText userMk;
    private EditText passMk;
    private EditText nivelMk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idEmail = (EditText) findViewById(R.id.r_email);
        nombre = (EditText) findViewById(R.id.r_nombre);
        pass = (EditText) findViewById(R.id.r_pass);
        userMk = (EditText) findViewById(R.id.r_userMK);
        passMk = (EditText) findViewById(R.id.r_passMK);
        nivelMk = (EditText) findViewById(R.id.r_nivel);

        Button btn_registro = (Button) findViewById(R.id.r_btnOk);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alta(v);
            }
        });

    }

    public void  alta(View v){
        if(idEmail.getText().toString().equals("") ||
                nombre.getText().toString().equals("") ||
                pass.getText().toString().equals("") ||
                nivelMk.getText().toString().equals("")){

            Snackbar.make(v,getResources().getString(R.string.camposVacios),Snackbar.LENGTH_LONG).show();

        }else{
            Util.altaUsuario(getApplication(),idEmail.getText().toString(),nombre.getText().toString(),
                    pass.getText().toString(),userMk.getText().toString(),passMk.getText().toString(),
                    nivelMk.getText().toString());
            Snackbar.make(v,getResources().getString(R.string.RegistroExitoso),Snackbar.LENGTH_LONG).show();
            Intent intent = new Intent(v.getContext(),MainActivity.class);
            startActivityForResult(intent,0);

        }
    }
}
