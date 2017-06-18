package chris.sesi.com.minegociomk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import chris.sesi.com.utils.UtilsDml;

public class FormNuevoCliente extends AppCompatActivity {

    private EditText nombre_cliente;
    private EditText direccion_clinete;
    private EditText telefono_cliente;
    private EditText ocupacion_cliente;
    private TextView title;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private Intent intent;
    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nuevo_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();

        nombre_cliente = (EditText) findViewById(R.id.nuevoClienteNombre);
        direccion_clinete = (EditText) findViewById(R.id.nuevoClienteDireccion);
        telefono_cliente = (EditText) findViewById(R.id.nuevoClienteTelefono);
        ocupacion_cliente = (EditText) findViewById(R.id.nuevoClienteOcupacion);
        title = (TextView) findViewById(R.id.tvTitleCliente);
        Button btn_guardar_cliente = (Button) findViewById(R.id.btn_nuevoClienteOk);

        if (intent.getStringExtra(ORIGIN_ACTIVITY).equals(ACTUALIZAR)){
             id = intent.getStringExtra("ID");
            if (!UtilsDml.ClientDetail(getApplication(),id,nombre_cliente,telefono_cliente,direccion_clinete,ocupacion_cliente)){
                Toast.makeText(this,getString(R.string.errorRegistro),Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            btn_guardar_cliente.setText(getString(R.string.Actualizar));
            title.setText(getString(R.string.Actualizar));
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
            if (UtilsDml.altaCliente(getApplication(), nombre_cliente.getText().toString(),
                    direccion_clinete.getText().toString(), telefono_cliente.getText().toString(),
                    ocupacion_cliente.getText().toString())){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
