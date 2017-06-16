package chris.sesi.com.minegociomk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;


public class MainActivity extends AppCompatActivity {

    private Button btn_sesion, btn_registro;
    private EditText usuario;
    private EditText pass;
    private String idEmail;
    private String passUser;
    private LinearLayout linearLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayoutMain = (LinearLayout) findViewById(R.id.activity_main);
        usuario = (EditText) findViewById(R.id.sesion_email);
        pass = (EditText) findViewById(R.id.sesion_pass);
        btn_sesion = (Button) findViewById(R.id.btn_isesion);

        btn_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String _usuario = usuario.getText().toString();
                final String _pass = pass.getText().toString();
                iniciarSesion(_usuario, _pass);
            }


        });
        btn_registro = (Button) findViewById(R.id.btn_sesion_registro);
        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    public void iniciarSesion(String user, String pass) {

        Snackbar.make(linearLayoutMain, "User: " + user + " Pass: " + pass, Snackbar.LENGTH_LONG).show();
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID,
                ContractSql.User.COLUMN_NAME_PASS_USER};
        //Filtro del query WHERE
        String selection = ContractSql.User.COLUMN_NAME_PK_ID + " =? AND " + ContractSql.User.COLUMN_NAME_PASS_USER + " =?";
        String[] selectionArgs = {user, pass};

        String sortOrder = "";  // Orden de la consulta

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            idEmail = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            passUser = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PASS_USER));
            cursor.close();
            db.close();
            Intent intent = new Intent(MainActivity.this, MenuPrincipal.class);
            startActivity(intent);


        } else {
            cursor.close();
            db.close();
            Snackbar.make(linearLayoutMain, getResources().getString(R.string.Error_Sesion) + "User: " + user + " Pass: " + pass, Snackbar.LENGTH_LONG).show();

        }


    }

}
