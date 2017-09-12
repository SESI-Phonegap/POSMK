package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.utils.Utils;
import chris.sesi.com.utils.UtilsDml;
import de.hdodenhof.circleimageview.CircleImageView;

public class ClientList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public RecyclerView recyclerView;
    private List<String> items;
    public static List<String> itemsID;
    public static List<String> item_tel;
    private List<String> item_photo;
    private TestAdapter testAdapter;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private final static String CLIENTES = "clientes";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);
        init();
    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_clientes);
        items = new ArrayList<>();
        itemsID = new ArrayList<>();
        item_tel = new ArrayList<>();
        item_photo = new ArrayList<>();
        UtilsDml.consultaClientes(getApplication(),items, item_tel, itemsID, item_photo);
        setUpRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_nuevo_cliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientList.this, FormNuevoCliente.class);
                intent.putExtra(ORIGIN_ACTIVITY,ALTA);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        final List<String> filteredList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            final String text = items.get(i).toLowerCase();
            if (text.contains(newText)) {
                filteredList.add(items.get(i));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(ClientList.this));
        testAdapter = new TestAdapter(filteredList, ClientList.this);
        recyclerView.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();
        return true;
    }

    public void setUpRecyclerView() {
        testAdapter = new TestAdapter(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);
        recyclerView.setHasFixedSize(true);
        ((TestAdapter) recyclerView.getAdapter()).setUndoOn(true);
        Utils.setUpItemTouchHelperLlamar(getApplication(),recyclerView,CLIENTES);
        Utils.setUpAnimationDecoratorHelperLlamar(getApplication(),recyclerView);
    }



    /**
     * RecyclerView adapter enabling undo on a swiped away item.
     */
    public class TestAdapter extends RecyclerView.Adapter {

        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

        List<String> items;
        Context mContext;
        List<String> itemsPendingRemoval;
        int lastInsertedIndex; // so we can add some more items for testing purposes
        boolean undoOn; // is undo on, you can turn it on from the toolbar menu

        private Handler handler = new Handler(); // hanlder for running delayed runnables

        private TestAdapter(List<String> item, Context context) {

            items = item;
            mContext = context;
            itemsPendingRemoval = new ArrayList<>();
            // let's generate some items
            lastInsertedIndex = 15;

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            TestViewHolder viewHolder = (TestViewHolder) holder;
            final String item = items.get(position);
            final String item_telefono = item_tel.get(position);
            final String item_Id = itemsID.get(position);
            String itemPhoto = item_photo.get(position);

            if (itemsPendingRemoval.contains(item)) {
                // we need to show the "undo" state of the row
                viewHolder.itemView.setBackgroundColor(Color.BLUE);
                viewHolder.titleTextView.setVisibility(View.GONE);
                viewHolder.telefonoTextView.setVisibility(View.GONE);
                viewHolder.imageView.setVisibility(View.GONE);


            } else {
                // we need to show the "normal" state
                viewHolder.itemView.setBackgroundColor(Color.WHITE);
                viewHolder.titleTextView.setVisibility(View.VISIBLE);
                viewHolder.imageView.setVisibility(View.VISIBLE);
                viewHolder.telefonoTextView.setVisibility(View.VISIBLE);

                if (Build.VERSION.SDK_INT >= 23){
                    if (itemPhoto != null){
                        if (itemPhoto.equals("")){
                            viewHolder.imageView.setImageResource(R.drawable.ic_person_black_24dp);
                        }else {
                            Bitmap bitmap = BitmapFactory.decodeFile(itemPhoto);
                            viewHolder.imageView.setImageBitmap(bitmap);
                        }
                    }

                } else {
                    if (Uri.parse(itemPhoto).toString().equals("")) {
                        viewHolder.imageView.setImageResource(R.drawable.ic_person_black_24dp);
                    } else {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(itemPhoto));
                            viewHolder.imageView.setImageBitmap(bitmap);
                            // viewHolder.icon.setImageURI(Uri.parse(item_photo));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                viewHolder.titleTextView.setText(item);
                viewHolder.telefonoTextView.setText(item_telefono);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, FormNuevoCliente.class);
                        intent.putExtra("ID", item_Id);
                        intent.putExtra(ORIGIN_ACTIVITY,ACTUALIZAR);
                        context.startActivity(intent);
                    }
                });


            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private void setUndoOn(boolean undoOn) {
            this.undoOn = undoOn;
        }

        public boolean isUndoOn() {
            return undoOn;
        }


        public void llamar(int position) {
            final String itemTel = item_tel.get(position);

            try {
                if (ActivityCompat.checkSelfPermission(ClientList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    explicarUsoPermiso();
                    // Pedir permiso para realizar llamada
                    ActivityCompat.requestPermissions(ClientList.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

                } else {

                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + itemTel)));

                }

                Runnable pendingRemovalRunnable = new Runnable() {
                    @Override
                    public void run() {
                        setUpRecyclerView();
                    }
                };
                handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void explicarUsoPermiso() {


            //Este IF es necesario para saber si el usuario ha marcado o no la casilla [] No volver a preguntar
            if (ActivityCompat.shouldShowRequestPermissionRationale(ClientList.this, Manifest.permission.CALL_PHONE)) {
                Toast.makeText(ClientList.this, "2.1 Explicamos razonadamente porque necesitamos el permiso", Toast.LENGTH_SHORT).show();
                //Explicarle al usuario porque necesitas el permiso (Opcional)

            }
        }


        public void remove(int position) {
            String item = items.get(position);
            if (itemsPendingRemoval.contains(item)) {
                itemsPendingRemoval.remove(item);
            }
            if (items.contains(item)) {
                items.remove(position);
                notifyItemRemoved(position);
            }
        }

       public boolean isPendingRemoval(int position) {
            String item = items.get(position);
            return itemsPendingRemoval.contains(item);
        }
    }


    /**
     * ViewHolder capable of presenting two states: "normal" and "undo" state.
     */
    private static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        View mView;
        CircleImageView imageView;
        TextView telefonoTextView;

        TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.clienta);
            //  undoButton = (Button) itemView.findViewById(R.id.undo_button);
            imageView = (CircleImageView) itemView.findViewById(R.id.list_avatar_clienta);
            telefonoTextView = (TextView) itemView.findViewById(R.id.cliente_tel);
            mView = itemView;
        }

    }


}
