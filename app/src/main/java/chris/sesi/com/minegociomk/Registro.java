package chris.sesi.com.minegociomk;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import chris.sesi.com.utils.Utils;
import chris.sesi.com.utils.UtilsDml;

public class Registro extends AppCompatActivity {

    private EditText idEmail;
    private EditText nombre;
    private EditText pass;
    private EditText userMk;
    private EditText passMk;
    private EditText nivelMk;
    private Button btn_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        idEmail = (EditText) findViewById(R.id.r_email);
        idEmail.addTextChangedListener(textWatcherEmail);
        nombre = (EditText) findViewById(R.id.r_nombre);
        nombre.addTextChangedListener(textWatcherValida);
        pass = (EditText) findViewById(R.id.r_pass);
        pass.addTextChangedListener(textWatcherValida);
        userMk = (EditText) findViewById(R.id.r_userMK);
        passMk = (EditText) findViewById(R.id.r_passMK);
        nivelMk = (EditText) findViewById(R.id.r_nivel);
        nivelMk.addTextChangedListener(textWatcherValida);

        btn_registro = (Button) findViewById(R.id.r_btnOk);
        btn_registro.setEnabled(false);
        btn_registro.setAlpha(0.6f);

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alta(v);
            }
        });

    }

    public void alta(View v) {
        if (idEmail.getText().toString().equals("") ||
                nombre.getText().toString().equals("") ||
                pass.getText().toString().equals("") ||
                nivelMk.getText().toString().equals("")) {

            Snackbar.make(v, getResources().getString(R.string.camposVacios), Snackbar.LENGTH_LONG).show();

        } else {

            if (UtilsDml.altaUsuario(getApplication(), idEmail.getText().toString(), nombre.getText().toString(),
                    pass.getText().toString(), userMk.getText().toString(), passMk.getText().toString(),
                    nivelMk.getText().toString())) {

                Toast.makeText(this, getString(R.string.RegistroExitoso), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            }


        }
    }

    TextWatcher textWatcherEmail = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {

                if (!Utils.validarEmail(s.toString())) {
                    idEmail.setError(getString(R.string.emailInvalido));
                }
            }
            validaCampos();
        }
    };

    TextWatcher textWatcherValida = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
                validaCampos();

        }
    };

    public void validaCampos() {

        if (!nombre.getText().toString().isEmpty() &&
                !pass.getText().toString().isEmpty() &&
                !nivelMk.getText().toString().isEmpty() &&
                Utils.validarEmail(idEmail.getText().toString())) {

            btn_registro.setEnabled(true);
            btn_registro.setAlpha(1);

        }else {
            btn_registro.setEnabled(false);
            btn_registro.setAlpha(0.6f);
        }

    }
}
