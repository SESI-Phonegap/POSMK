package chris.sesi.com.minegociomk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import chris.sesi.com.utils.UtilsDml;

public class FormNuevaConsultora extends AppCompatActivity {
    private EditText edtNombre, edtNivel, edtDireccion, edtTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nueva_consultora);
        edtNombre = (EditText) findViewById(R.id.edt_nombre_consultora);
        edtNivel = (EditText) findViewById(R.id.edt_nivel_consultora);
        edtDireccion = (EditText) findViewById(R.id.edt_direccion_consultora);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_consultora);
        Button btnGuardar = (Button) findViewById(R.id.btn_saveConsultora);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}
