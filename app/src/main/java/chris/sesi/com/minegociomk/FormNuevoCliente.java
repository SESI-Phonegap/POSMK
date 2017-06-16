package chris.sesi.com.minegociomk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class FormNuevoCliente extends AppCompatActivity {

    EditText nombre_cliente;
    EditText direccion_clinete;
    EditText telefono_cliente;
    EditText ocupacion_cliente;
    Button btn_guardar_cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_nuevo_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nombre_cliente = (EditText) findViewById(R.id.nuevoClienteNombre);
        direccion_clinete = (EditText) findViewById(R.id.nuevoClienteDireccion);
        telefono_cliente = (EditText) findViewById(R.id.nuevoClienteTelefono);
        ocupacion_cliente = (EditText) findViewById(R.id.nuevoClienteOcupacion);
        btn_guardar_cliente = (Button) findViewById(R.id.btn_nuevoClienteOk);

        btn_guardar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre_cliente.getText().toString().equals("")  ||
                   telefono_cliente.getText().toString().equals("") ||
                   direccion_clinete.getText().toString().equals("") ||
                   ocupacion_cliente.getText().toString().equals("")) {
                    Snackbar.make(v,getResources().getString(R.string.camposVacios),Snackbar.LENGTH_LONG).show();

                }else {
                    altaCliente();
                    Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

    }

    public void altaCliente(){
        String nombre = nombre_cliente.getText().toString();
        String direccion = direccion_clinete.getText().toString();
        String telefono = telefono_cliente.getText().toString();
        String ocupacion = ocupacion_cliente.getText().toString();

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Clientes.COLUMN_NAME_NOMBRE,nombre);
        values.put(ContractSql.Clientes.COLUMN_NAME_DIRECCION,direccion);
        values.put(ContractSql.Clientes.COLUMN_NAME_TELEFONO,telefono);
        values.put(ContractSql.Clientes.COLUMN_NAME_OCUPACION,ocupacion);

        sqLiteDatabase.insert(ContractSql.Clientes.TABLE_NAME, null, values);
        sqLiteDatabase.close();

    }

}
