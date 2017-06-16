package chris.sesi.com.minegociomk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class Inventario extends AppCompatActivity implements SearchView.OnQueryTextListener{
    RecyclerView recyclerView;
    List<String> items;
    List<String> itemsID;
    TestAdapter testAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_inventario);
        items = new ArrayList<>();
        itemsID = new ArrayList<>();
        consultaInventario(items,itemsID);
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

    public void setUpRecyclerView(){
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
        recyclerView.setLayoutManager(new LinearLayoutManager(Inventario.this));
        testAdapter = new TestAdapter(filteredList,Inventario.this);
        recyclerView.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();
        return true;
    }

    class TestAdapter extends RecyclerView.Adapter {
        List<String> items;
        Context mContext;

        TestAdapter(List<String> item_list,Context context){
            items = item_list;
            mContext = context;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Inventario.TestViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Inventario.TestViewHolder viewHolder = (Inventario.TestViewHolder) holder;
            final String item = items.get(position);
            final String item_Id = itemsID.get(position);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context,Catalogo.class);
                    context.startActivity(intent);

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
                    Intent intent = new Intent(context,FormAgregarStock.class);
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






    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView item;
        View mView;

        TestViewHolder(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_inventario, parent, false));
            icon = (ImageView) itemView.findViewById(R.id.list_icon_inventario);
            item = (TextView) itemView.findViewById(R.id.item_inventario);
            mView = itemView;
        }
    }

    public void consultaInventario(List<String> items, List<String> itemId){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.TABLE_NAME+"."+ContractSql.Producto.COLUMN_NAME_NOMBRE,
                               ContractSql.Inventario.TABLE_NAME+"."+ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO,
                               ContractSql.Inventario.TABLE_NAME+"."+ContractSql.Inventario.COLUMN_NAME_STOCK};

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContractSql.Producto.TABLE_NAME + " INNER JOIN " + ContractSql.Inventario.TABLE_NAME + " ON " +
                ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " = " + ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO );



        //Filtro del query WHERE
         // String selection =  ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " =? ";
       //   String[] selectionArgs = {ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO};
          String orderBy = ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_NOMBRE + " ASC";
          Cursor cursor = queryBuilder.query(
                  db,
                  projection,
                  null,
                  null,
                  null,
                  null,
                  orderBy);

   /*     Cursor cursor = db.query(
                ContractSql.Producto.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);
*/
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                items.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_NOMBRE)));
                itemId.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }



}
