package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.utils.Utils;
import chris.sesi.com.utils.UtilsDml;

public class ConsultoraList extends AppCompatActivity implements SearchView.OnQueryTextListener{
    public RecyclerView recyclerView;
    private List<String> items;
    public static List<String> itemsID;
    public static List<String> item_tel;
    private TestAdapter testAdapter;
    private final static String ORIGIN_ACTIVITY = "origin_activity";
    private final static String ACTUALIZAR = "actualizar";
    private final static String ALTA = "alta";
    private final static String CONSULTORAS = "consultoras";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultora_list);
        init();

    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConsultoraList.this, FormNuevaConsultora.class);
                intent.putExtra(ORIGIN_ACTIVITY,ALTA);
                startActivity(intent);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_consultoras);
        items = new ArrayList<>();
        itemsID = new ArrayList<>();
        item_tel = new ArrayList<>();
        UtilsDml.consultaConsultoras(getApplication(),items,item_tel,itemsID);
        setUpRecyclerView();

    }

    public void setUpRecyclerView() {
        testAdapter = new TestAdapter(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);
        recyclerView.setHasFixedSize(true);
        ((TestAdapter) recyclerView.getAdapter()).setUndoOn(true);
        Utils.setUpItemTouchHelperLlamar(getApplication(),recyclerView,CONSULTORAS);
        Utils.setUpAnimationDecoratorHelperLlamar(getApplication(),recyclerView);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(ConsultoraList.this));
        testAdapter = new TestAdapter(filteredList, ConsultoraList.this);
        recyclerView.setAdapter(testAdapter);
        testAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
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
                viewHolder.imageView.setImageResource(R.drawable.ic_person_black_24dp);
                viewHolder.titleTextView.setText(item);
                viewHolder.telefonoTextView.setText(item_telefono);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, FormNuevaConsultora.class);
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
                if (ActivityCompat.checkSelfPermission(ConsultoraList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    explicarUsoPermiso();
                    // Pedir permiso para realizar llamada
                    ActivityCompat.requestPermissions(ConsultoraList.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

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
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConsultoraList.this, Manifest.permission.CALL_PHONE)) {
                Toast.makeText(ConsultoraList.this, "2.1 Explicamos razonadamente porque necesitamos el permiso", Toast.LENGTH_SHORT).show();
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
        ImageView imageView;
        TextView telefonoTextView;

        TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.clienta);
            //  undoButton = (Button) itemView.findViewById(R.id.undo_button);
            imageView = (ImageView) itemView.findViewById(R.id.list_avatar_clienta);
            telefonoTextView = (TextView) itemView.findViewById(R.id.cliente_tel);
            mView = itemView;
        }

    }


}
