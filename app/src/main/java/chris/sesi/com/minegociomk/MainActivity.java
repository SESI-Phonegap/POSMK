package chris.sesi.com.minegociomk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import chris.sesi.com.utils.Util;


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

        btn_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _user = usuario.getText().toString();
                final String _pass = pass.getText().toString();
                Util.iniciarSesion(getApplication(),v, _user, _pass);
            }


        });
        Button btn_registro = (Button) findViewById(R.id.btn_sesion_registro);
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivityForResult(intent, 0);
            }
        });

    }

}
