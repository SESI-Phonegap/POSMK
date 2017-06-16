package chris.sesi.com.minegociomk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class Edit_Client extends AppCompatActivity {

    EditText nombre_cliente;
    EditText direccion_cliente;
    EditText telefono_cliente;
    EditText ocupacion_cliente;
    Button btn_DetailClientUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nombre_cliente = (EditText)findViewById(R.id.DetailClienteNombre);
        direccion_cliente = (EditText) findViewById(R.id.DetailClienteDireccion);
        telefono_cliente = (EditText) findViewById(R.id.DetailClienteTelefono);
        ocupacion_cliente = (EditText) findViewById(R.id.DetailClienteOcupacion);
        btn_DetailClientUpdate = (Button) findViewById(R.id.btn_DetailClienteOk);

        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID");
        ClientDetail(id);

        btn_DetailClientUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ClientUpdate(id)){
                    Snackbar.make(v,getResources().getString(R.string.DatosActualizados),Snackbar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(v,getResources().getString(R.string.ErrorActualiza),Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void ClientDetail(String id){

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Clientes.COLUMN_NAME_PK_ID,
                               ContractSql.Clientes.COLUMN_NAME_NOMBRE,
                               ContractSql.Clientes.COLUMN_NAME_TELEFONO,
                               ContractSql.Clientes.COLUMN_NAME_DIRECCION,
                               ContractSql.Clientes.COLUMN_NAME_OCUPACION};
        //Filtro del query WHERE
        String selection = ContractSql.Clientes.COLUMN_NAME_PK_ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                ContractSql.Clientes.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                nombre_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_NOMBRE)));
                telefono_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_TELEFONO)));
                direccion_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_DIRECCION)));
                ocupacion_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_OCUPACION)));

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();



    }
    public boolean ClientUpdate(String id){
        String nombre = nombre_cliente.getText().toString();
        String tel = telefono_cliente.getText().toString();
        String direcc = direccion_cliente.getText().toString();
        String ocupacion = ocupacion_cliente.getText().toString();

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Clientes.COLUMN_NAME_NOMBRE,nombre);
        values.put(ContractSql.Clientes.COLUMN_NAME_TELEFONO,tel);
        values.put(ContractSql.Clientes.COLUMN_NAME_DIRECCION,direcc);
        values.put(ContractSql.Clientes.COLUMN_NAME_OCUPACION,ocupacion);

        String selection = ContractSql.Clientes.COLUMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Clientes.TABLE_NAME,values,selection,selectionArgs);
        db.close();

        return result > 0;

    }

}
