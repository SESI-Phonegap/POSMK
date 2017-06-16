package chris.sesi.com.minegociomk;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class Catalogo extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    List<String> items;
    List<String> itemsID;
    TestAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_catalogo);
        items = new ArrayList<>();
        itemsID = new ArrayList<>();
        consultaCatalogo(items,itemsID);
        setUpRecyclerView();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    private void setUpRecyclerView() {
        testAdapter = new TestAdapter(items,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       newText = newText.toLowerCase();
        final List<String> filteredList = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            final String text = items.get(i).toLowerCase();
            if(text.contains(newText)){
                filteredList.add(items.get(i));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(Catalogo.this));
        testAdapter = new TestAdapter(filteredList,Catalogo.this);
        recyclerView.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();
        return true;
    }






    class TestAdapter extends RecyclerView.Adapter{
        List<String> items;
        Context mContext;



        TestAdapter(List<String> item_list,Context context){
            items = item_list;
            mContext = context;

        }




        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            TestViewHolder viewHolder = (TestViewHolder) holder;
            final String item = items.get(position);
            final String item_Id = itemsID.get(position);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createNewProductDialog();
                }
            });

            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.item.setVisibility(View.VISIBLE);
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.item.setText(item);
            viewHolder.icon.setImageResource(R.drawable.a_avator);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FormAgregarStock.class);
                    intent.putExtra("ID_Product",item_Id);
                    intent.putExtra("item_Name",item);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }



        public void createNewProductDialog(){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Catalogo.this);
            LayoutInflater inflater = Catalogo.this.getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_nuevo_producto_catalogo,null);

            builder.setView(view);

            Button agregarProducto = (Button) view.findViewById(R.id.btn_dialog_agregar_producto);
            Button cancelar = (Button) view.findViewById(R.id.btn_dialog_cancelar_producto);
            final TextInputEditText producto = (TextInputEditText) view.findViewById(R.id.item_producto);
            final AlertDialog dialog = builder.create();

            agregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String product = producto.getText().toString();
                    if(product.equals("")){
                        Snackbar.make(v,getResources().getString(R.string.Campovacio),Snackbar.LENGTH_LONG).show();

                    }else {
                        insertProduct(product);
                        items.add(product);
                        setUpRecyclerView();
                        dialog.cancel();
                    }


                }
            });

            cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();

                }
            });

            dialog.show();

        }




    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView item;
        View mView;

        TestViewHolder(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_catalogo, parent, false));
            icon = (ImageView) itemView.findViewById(R.id.list_icon_catalogo);
            item = (TextView) itemView.findViewById(R.id.item_catalogo);
            mView = itemView;
        }
    }

    public void consultaCatalogo(List<String> items, List<String> itemsid){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.COLUMN_NAME_NOMBRE,
                               ContractSql.Producto.COLUMN_NAME_PK_ID};

        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        Cursor cursor = db.query(
                ContractSql.Producto.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                items.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_NOMBRE)));
                itemsid.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_PK_ID)));

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void insertProduct(String product){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Producto.COLUMN_NAME_NOMBRE,product);
        values.put(ContractSql.Producto.COLUMN_NAME_DESCRIPCION, "");
        values.put(ContractSql.Producto.COLUMN_NAME_VENDIDOS,0);

        db.insert(ContractSql.Producto.TABLE_NAME,null,values);
        db.close();


    }



}
