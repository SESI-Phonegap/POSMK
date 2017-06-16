package chris.sesi.com.minegociomk;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
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

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;

public class ClientList extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static RecyclerView recyclerView;
    private Handler handler = new Handler();
    public static final int PENDING_CHANGE_TIMEOUT = 5000;
    private List<String> items;
    public static List<String> itemsID;
    public static List<String> item_tel;
    private TestAdapter testAdapter;


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
        consultaClientes(items, item_tel, itemsID);
        setUpRecyclerView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_nuevo_cliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientList.this, FormNuevoCliente.class);
                startActivityForResult(intent, 0);
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
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();
    }

    /**
     * This is the standard support library way of implementing "swipe to delete" feature. You can do custom drawing in onChildDraw method
     * but whatever you draw will disappear once the swipe is over, and while the items are animating to their new position the recycler view
     * background will be visible. That is rarely an desired effect.
     */
    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = ContextCompat.getDrawable(ClientList.this, R.drawable.c_blue);
                xMark = ContextCompat.getDrawable(ClientList.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ClientList.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                TestAdapter testAdapter = (TestAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TestAdapter adapter = (TestAdapter) recyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {

                    adapter.llamar(swipedPosition);

                } else {
                    adapter.remove(swipedPosition);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getLeft() - (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                //xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = ContextCompat.getDrawable(ClientList.this, R.drawable.c_blue);
                initiated = true;
            }

            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

                if (!initiated) {
                    init();
                }

                // only if animation is in progress
                if (parent.getItemAnimator().isRunning()) {

                    // some items might be animating down and some items might be animating up to close the gap left by the removed item
                    // this is not exclusive, both movement can be happening at the same time
                    // to reproduce this leave just enough items so the first one and the last one would be just a little off screen
                    // then remove one from the middle

                    // find first child with translationY > 0
                    // and last one with translationY < 0
                    // we're after a rect that is not covered in recycler-view views at this point in time
                    View lastViewComingDown = null;
                    View firstViewComingUp = null;

                    // this is fixed
                    int left = 0;
                    int right = parent.getWidth();

                    // this we need to find out
                    int top = 0;
                    int bottom = 0;

                    // find relevant translating views
                    int childCount = parent.getLayoutManager().getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        View child = parent.getLayoutManager().getChildAt(i);
                        if (child.getTranslationY() < 0) {
                            // view is coming down
                            lastViewComingDown = child;
                        } else if (child.getTranslationY() > 0) {
                            // view is coming up
                            if (firstViewComingUp == null) {
                                firstViewComingUp = child;
                            }
                        }
                    }

                    if (lastViewComingDown != null && firstViewComingUp != null) {
                        // views are coming down AND going up to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    } else if (lastViewComingDown != null) {
                        // views are going down to fill the void
                        top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();
                        bottom = lastViewComingDown.getBottom();
                    } else if (firstViewComingUp != null) {
                        // views are coming up to fill the void
                        top = firstViewComingUp.getTop();
                        bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();
                    }

                    background.setBounds(left, top, right, bottom);
                    background.draw(c);

                }
                super.onDraw(c, parent, state);
            }

        });
    }


    /**
     * RecyclerView adapter enabling undo on a swiped away item.
     */
    class TestAdapter extends RecyclerView.Adapter {

        private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

        List<String> items;
        Context mContext;
        List<String> itemsPendingRemoval;
        int lastInsertedIndex; // so we can add some more items for testing purposes
        boolean undoOn; // is undo on, you can turn it on from the toolbar menu

        private Handler handler = new Handler(); // hanlder for running delayed runnables

        public TestAdapter(List<String> item, Context context) {

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
                        Intent intent = new Intent(context, Edit_Client.class);
                        intent.putExtra("ID", item_Id);
                        context.startActivity(intent);
                    }
                });


            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void setUndoOn(boolean undoOn) {
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
    static class TestViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        View mView;
        ImageView imageView;
        TextView telefonoTextView;

        public TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.clienta);
            //  undoButton = (Button) itemView.findViewById(R.id.undo_button);
            imageView = (ImageView) itemView.findViewById(R.id.list_avatar_clienta);
            telefonoTextView = (TextView) itemView.findViewById(R.id.cliente_tel);
            mView = itemView;
        }

    }


    public void consultaClientes(List<String> items, List<String> itemTel, List<String> itemId) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Clientes.COLUMN_NAME_PK_ID,
                ContractSql.Clientes.COLUMN_NAME_NOMBRE,
                ContractSql.Clientes.COLUMN_NAME_TELEFONO};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        Cursor cursor = db.query(
                ContractSql.Clientes.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                itemId.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_PK_ID)));
                items.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_NOMBRE)));
                itemTel.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_TELEFONO)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

    }
}
