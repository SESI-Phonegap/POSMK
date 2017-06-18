package chris.sesi.com.minegociomk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import chris.sesi.com.utils.UtilsDml;

public class FormNuevaConsultora extends AppCompatActivity {
    private EditText edtNombre, edtNivel, edtDireccion, edtTelefono;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private Intent intent;
    private String id = "";

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

        if (intent.getStringExtra(ORIGIN_ACTIVITY).equals(ACTUALIZAR)){
            id = intent.getStringExtra("ID");
            if (!UtilsDml.ConsultoraDetail(getApplication(),id,edtNombre,edtTelefono,edtDireccion,edtNivel)){
                Toast.makeText(this,getString(R.string.errorRegistro),Toast.LENGTH_LONG).show();
                onBackPressed();
            }
            btnGuardar.setText(getString(R.string.Actualizar));
            tvTitle.setText(getString(R.string.Actualizar));
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
                    edtTelefono.getText().toString())){
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
}
