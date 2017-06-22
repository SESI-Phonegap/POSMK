package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class Catalogo extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private List<String> itemsPhoto;
    private List<String> items;
    private List<String> itemsID;
    private TestAdapter testAdapter;
    private static final int PICK_IMAGE = 100;
    private Uri uriPhotoResult;
    private CircleImageView photoCatalogo;

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
        itemsPhoto = new ArrayList<>();
        UtilsDml.consultaCatalogo(getApplication(), items, itemsID, itemsPhoto);
        setUpRecyclerView();
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestAdapterCatalogo.createNewProductDialog();
            }
        });
*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    public void setUpRecyclerView() {
        testAdapter = new TestAdapter(items, this);
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
        //  List<String>filteredIdItemList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            final String text = items.get(i).toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(items.get(i));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(Catalogo.this));
        testAdapter = new TestAdapter(filteredList, Catalogo.this);
        recyclerView.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();
        return true;
    }


    class TestAdapter extends RecyclerView.Adapter {
        List<String> items;
        Context mContext;


        TestAdapter(List<String> item_list, Context context) {
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
            String item_photo = itemsPhoto.get(position);
            Log.d("AAAA-", item_photo);
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
            if (Uri.parse(item_photo).toString().equals("")) {
                viewHolder.icon.setImageResource(R.drawable.ni_image);
            } else {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(item_photo));
                    viewHolder.icon.setImageBitmap(bitmap);
                    // viewHolder.icon.setImageURI(Uri.parse(item_photo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FormAgregarStock.class);
                    intent.putExtra("ID_Product", item_Id);
                    intent.putExtra("item_Name", item);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }


        public void createNewProductDialog() {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Catalogo.this);
            LayoutInflater inflater = Catalogo.this.getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_nuevo_producto_catalogo, null);

            builder.setView(view);

            photoCatalogo = (CircleImageView) view.findViewById(R.id.photoCatalogo);
            photoCatalogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGallery();
                }
            });
            Button agregarProducto = (Button) view.findViewById(R.id.btn_dialog_agregar_producto);
            Button cancelar = (Button) view.findViewById(R.id.btn_dialog_cancelar_producto);
            final TextInputEditText producto = (TextInputEditText) view.findViewById(R.id.item_producto);
            final AlertDialog dialog = builder.create();

            agregarProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String product = producto.getText().toString();
                    if (product.equals("")) {
                        Snackbar.make(v, getResources().getString(R.string.Campovacio), Snackbar.LENGTH_LONG).show();

                    } else {
                        if (uriPhotoResult == null) {
                            uriPhotoResult = Uri.parse("");
                        }
                        UtilsDml.insertProduct(getApplication(), product, uriPhotoResult.toString());
                        items.add(product);
                        UtilsDml.consultaCatalogo(getApplication(), items, itemsID, itemsPhoto);
                        setUpRecyclerView();
                        uriPhotoResult = null;
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

        TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_catalogo, parent, false));
            icon = (ImageView) itemView.findViewById(R.id.list_icon_catalogo);
            item = (TextView) itemView.findViewById(R.id.item_catalogo);
            mView = itemView;
        }
    }

/*    public void consultaCatalogo(List<String> items, List<String> itemsid){
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
    }*/

 /*   public void insertProduct(String product){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Producto.COLUMN_NAME_NOMBRE,product);
        values.put(ContractSql.Producto.COLUMN_NAME_DESCRIPCION, "");
        values.put(ContractSql.Producto.COLUMN_NAME_VENDIDOS,0);

        db.insert(ContractSql.Producto.TABLE_NAME,null,values);
        db.close();


    }*/

    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Catalogo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Catalogo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        } else {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            uriPhotoResult = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhotoResult);
                photoCatalogo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
