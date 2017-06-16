package chris.sesi.com.minegociomk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class FormAgregarStock extends AppCompatActivity {

    TextInputEditText addStock;
    TextInputEditText minStock;
    TextInputEditText pCompra;
    TextInputEditText pVenta;
    TextView cantActualStock;
    TextView nombreProducto;
    Button btn_agregar_stock;

    List<String> _id;
    List<String> _stock;
    List<String> _minStock;
    List<String> _pCompra;
    List<String> _pVenta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_agregar_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addStock = (TextInputEditText) findViewById(R.id.item_stock);
        minStock = (TextInputEditText) findViewById(R.id.item_minstock);
        pCompra = (TextInputEditText) findViewById(R.id.item_compra);
        pVenta = (TextInputEditText) findViewById(R.id.item_venta);
        btn_agregar_stock = (Button) findViewById(R.id.btn_agregar_stock);
        cantActualStock = (TextView) findViewById(R.id.form_stock_cant_stock);
        nombreProducto = (TextView) findViewById(R.id.form_stock_nombre_producto) ;
        _id = new ArrayList<>();
        _stock = new ArrayList<>();
        _minStock = new ArrayList<>();
        _pCompra = new ArrayList<>();
        _pVenta = new ArrayList<>();

        Intent intent = getIntent();
        final String id = intent.getStringExtra("ID_Product");
        String item_name = intent.getStringExtra("item_Name");

        final boolean result = consultaStock(id);
        nombreProducto.setText(item_name);

        btn_agregar_stock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result){
                    //UPDATE del stock
                    if("".equals(addStock.getText().toString()) ||
                       "".equals(minStock.getText().toString()) ||
                       "".equals(pCompra.getText().toString())  ||
                       "".equals(pVenta.getText().toString())){
                        Snackbar.make(v,getResources().getString(R.string.camposVacios),Snackbar.LENGTH_LONG).show();
                    }else{
                        String idProducto = id;
                        String inStock = _stock.get(0);
                        if(stockUpdate(idProducto,inStock)){
                            Snackbar.make(v,getResources().getString(R.string.DatosActualizados),Snackbar.LENGTH_SHORT).show();
                        }else{
                            Snackbar.make(v,getResources().getString(R.string.ErrorActualiza),Snackbar.LENGTH_SHORT).show();
                        }
                    }


                }else{
                    //Registra el producto por primera vez en el inventario
                    String idProducto = id;
                    if("".equals(addStock.getText().toString()) ||
                       "".equals(minStock.getText().toString()) ||
                       "".equals(pCompra.getText().toString())  ||
                       "".equals(pVenta.getText().toString())){
                        Snackbar.make(v,getResources().getString(R.string.camposVacios),Snackbar.LENGTH_LONG).show();
                    }else{
                        nuevoRegistroInventario(idProducto);
                        Intent intent = new Intent(v.getContext(), Inventario.class);
                        startActivityForResult(intent, 0);
                    }

                }
            }
        });

    }

    public void nuevoRegistroInventario(String id){
        String aux_addstock = addStock.getText().toString();
        String aux_minstock = minStock.getText().toString();
        String aux_pcompra = pCompra.getText().toString();
        String aux_pventa = pVenta.getText().toString();

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Inventario.COLUMN_NAME_PRECIO_COMPRA,aux_pcompra);
        values.put(ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA,aux_pventa);
        values.put(ContractSql.Inventario.COLUMN_NAME_STOCK,aux_addstock);
        values.put(ContractSql.Inventario.COLUMN_NAME_MIN_STOCK,aux_minstock);
        values.put(ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO,id);

        db.insert(ContractSql.Inventario.TABLE_NAME,null,values);
        db.close();

    }

    public boolean stockUpdate(String id, String instock){
        String aux_addstock = addStock.getText().toString();
        String aux_minstock = minStock.getText().toString();
        String aux_pcompra = pCompra.getText().toString();
        String aux_pventa = pVenta.getText().toString();

        int aux_insotck = Integer.parseInt(instock);
        int aux_addstock_int = Integer.parseInt(aux_addstock);
        int stockUpdate = aux_insotck + aux_addstock_int;

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Inventario.COLUMN_NAME_PRECIO_COMPRA,aux_pcompra);
        values.put(ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA,aux_pventa);
        values.put(ContractSql.Inventario.COLUMN_NAME_STOCK,stockUpdate);
        values.put(ContractSql.Inventario.COLUMN_NAME_MIN_STOCK,aux_minstock);

        //Condicion WHERE
        String selection = ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Inventario.TABLE_NAME,values,selection,selectionArgs);
        db.close();

        return  result > 0;
    }

    public boolean consultaStock(String id){
        boolean result = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection ={ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO,
                              ContractSql.Inventario.COLUMN_NAME_STOCK,
                              ContractSql.Inventario.COLUMN_NAME_MIN_STOCK,
                              ContractSql.Inventario.COLUMN_NAME_PRECIO_COMPRA,
                              ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA};
        //Filtro del query WHERE
        String selection = ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO + " =? ";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(ContractSql.Inventario.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            // si existe registro en el inventario
            result = true;

                _id.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO)));
                _stock.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_STOCK)));
                _minStock.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_MIN_STOCK)));
                _pCompra.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_PRECIO_COMPRA)));
                _pVenta.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA)));
                cantActualStock.setText(_stock.get(0));
                addStock.setText("0");
                minStock.setText(_minStock.get(0));
                pCompra.setText(_pCompra.get(0));
                pVenta.setText(_pVenta.get(0));

        }else {
            //si no existe registro establece a 0 la cantidad en stock
            _stock.add("0");
            cantActualStock.setText(_stock.get(0));
        }
        cursor.close();
        db.close();




        return result;

    }

}
