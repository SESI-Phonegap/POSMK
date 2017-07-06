package chris.sesi.com.minegociomk;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.Adapter.AdapterUnidad;
import chris.sesi.com.model.ModelConsultora;
import chris.sesi.com.utils.UtilsDml;

public class MiUnidad extends AppCompatActivity {
    private Dialog adAlert;
    private List<ModelConsultora> listConsultoras;
    private RecyclerView recyclerViewConsultoras;
    private AdapterUnidad adapterUnidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_unidad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        init();

    }

    public void init(){
        recyclerViewConsultoras = (RecyclerView) findViewById(R.id.recycler_miunidad);
        listConsultoras = new ArrayList<>();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (UtilsDml.checkUnidadExist(getApplication())){
            //Carga tu unidad
            if (UtilsDml.consultaMiUnidad(getApplication(),listConsultoras)){
                setUpRecyclerView();
            } else {
                Toast.makeText(this,getString(R.string.sinConsultorasUnidad),Toast.LENGTH_LONG);
            }
        } else {
            showDialogNuevaUnidad();
        }
    }


    public void setUpRecyclerView(){
        adapterUnidad = new AdapterUnidad(getApplication(),listConsultoras);
        recyclerViewConsultoras.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewConsultoras.setAdapter(adapterUnidad);
        recyclerViewConsultoras.setHasFixedSize(true);
    }

    public void showDialogNuevaUnidad(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_unidad,null);

        final EditText etUnidad = (EditText) view.findViewById(R.id.editTextMiUnidad);
        Button btnGuardar = (Button) view.findViewById(R.id.btn_guardar);
        Button btnCancelar = (Button) view.findViewById(R.id.btn_cancel);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiUnidad.this,MenuPrincipal.class);
                startActivity(intent);
                finish();
                adAlert.dismiss();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UtilsDml.altaUnidad(getApplication(),etUnidad.getText().toString())){
                    adAlert.dismiss();
                } else {
                    Toast.makeText(MiUnidad.this,getString(R.string.errorRegistro),Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setView(view);
        adAlert = builder.create();
        adAlert.setCancelable(false);
        adAlert.setCanceledOnTouchOutside(false);
        adAlert.show();

    }

}
