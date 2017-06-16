package chris.sesi.com.minegociomk;

import android.content.ContentValues;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class Registro extends AppCompatActivity {

    private EditText idEmail;
    private EditText nombre;
    private EditText pass;
    private EditText userMk;
    private EditText passMk;
    private EditText nivelMk;
    private Button btn_registro;
    private String mail;
    private String password;

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

        btn_registro = (Button) findViewById(R.id.r_btnOk);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idEmail.getText().toString().equals("") ||
                        nombre.getText().toString().equals("") ||
                        pass.getText().toString().equals("") ||
                        userMk.getText().toString().equals("") ||
                        passMk.getText().toString().equals("") ||
                        nivelMk.getText().toString().equals("")){

                    Snackbar.make(v,getResources().getString(R.string.camposVacios),Snackbar.LENGTH_LONG).show();

                }else{
                altaUsuario();
                Snackbar.make(v,getResources().getString(R.string.RegistroExitoso),Snackbar.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(),MainActivity.class);
                startActivityForResult(intent,0);

                }
            }
        });

    }

    public void altaUsuario(){
        mail = idEmail.getText().toString();
        String nomb = nombre.getText().toString();
        password = pass.getText().toString();
        String usuarioMk = userMk.getText().toString();
        String passwordMk = passMk.getText().toString();
        String nivel = nivelMk.getText().toString();

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.User.COLUMN_NAME_PK_ID, mail);
        values.put(ContractSql.User.COLUMN_NAME_NOMBRE, nomb);
        values.put(ContractSql.User.COLUMN_NAME_NIVELMK, nivel);
        values.put(ContractSql.User.COLUMN_NAME_PASS_USER, password);
        values.put(ContractSql.User.COLUMN_NAME_USER_MK, usuarioMk);
        values.put(ContractSql.User.COLUMN_NAME_PASS_MK, passwordMk);

        sqLiteDatabase.insert(ContractSql.User.TABLE_NAME, null, values);
        sqLiteDatabase.close();


    }
}
