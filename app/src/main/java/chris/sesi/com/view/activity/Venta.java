package chris.sesi.com.view.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chris.sesi.com.view.adapter.TestAdapterTicket;
import chris.sesi.com.data.database.AdminSQLiteOpenHelper;
import chris.sesi.com.data.database.ContractSql;
import chris.sesi.com.data.model.ModeloInventario;
import chris.sesi.com.data.model.ModeloTicket;
import chris.sesi.com.view.R;
import chris.sesi.com.view.adapter.SpinnerAdapter;

public class Venta extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView recyclerViewTicket;
    List<ModeloInventario> _items;
    List<ModeloTicket> _itemsTicket;
    TestAdapterTicket testAdapterTicket;
    TestAdapter testAdapter;
    Button btn_cobrar;
    Button btn_ventaActual;
    EditText buscar;
    TextView tv_Cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_cobrar = (Button) findViewById(R.id.btn_cobrar);
        btn_ventaActual = (Button) findViewById(R.id.btn_ventaActual);
        buscar = (EditText) findViewById(R.id.venta_buscar);
        tv_Cliente = (TextView) findViewById(R.id.venta_tv_cliente);

        _items = new ArrayList<>();
        _itemsTicket = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_ventaInventario);
        consultaInventario(_items);
        setUpRecyclerView();
        addTextListener();

        tv_Cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_ventaActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogVentaActual();
            }
        });



    }

    public void addTextListener() {
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();
                final List<ModeloInventario> filteredList = new ArrayList<>();
                for(int i = 0; i < _items.size(); i++){
                    final String text = _items.get(i).getNombreProducto().toLowerCase();
                    if(text.contains(s)){
                        filteredList.add(_items.get(i));
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(Venta.this));
                testAdapter = new TestAdapter(filteredList,Venta.this);
                recyclerView.setAdapter(testAdapter);
                testAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
        public void setUpSumaTotal(){
        float total = 0;
        for (int i = 0; i < _itemsTicket.size(); i++) {
            total += Float.parseFloat(_itemsTicket.get(i).importe);
        }
        btn_cobrar.setText(getResources().getString(R.string.Cobrar)+total);
    }
    public void setUpRecyclerView(){

        testAdapter = new TestAdapter(_items,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(testAdapter);
        recyclerView.setHasFixedSize(true);


    }
    public  void setUpRecyclerViewTicket(){

        testAdapterTicket = new TestAdapterTicket(_itemsTicket,this);
        recyclerViewTicket.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTicket.setAdapter(testAdapterTicket);
        recyclerViewTicket.setHasFixedSize(true);
    }
    public void createDialogVentaActual(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Venta.this);
        LayoutInflater inflater = Venta.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_ticket,null);
        builder.setView(view);
        recyclerViewTicket = (RecyclerView) view.findViewById(R.id.recycler_view_ticket);

        setUpRecyclerViewTicket();
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        final AlertDialog dialog = builder.create();

        dialog.show();



    }

    public void consultaClientes(){}
    public void consultaInventario(List<ModeloInventario> item){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(this);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.TABLE_NAME+"." +ContractSql.Producto.COLUMN_NAME_NOMBRE,
                ContractSql.Inventario.TABLE_NAME+"."+ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO,
                ContractSql.Inventario.TABLE_NAME+"."+ContractSql.Inventario.COLUMN_NAME_STOCK,
                ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA};

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContractSql.Producto.TABLE_NAME + " INNER JOIN " + ContractSql.Inventario.TABLE_NAME + " ON " +
                ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " = " + ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO );

        String orderBy = ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_NOMBRE + " ASC";
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                null,
                null,
                null,
                null,
                orderBy);


        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                item.add(new ModeloInventario(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_STOCK)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA))));

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(Venta.this, R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) Venta.this.getResources().getDimension(R.dimen.ic_clear_margin);
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
                TestAdapterTicket testAdapter = (TestAdapterTicket) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                TestAdapterTicket adapter = (TestAdapterTicket) recyclerViewTicket.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {


                    _itemsTicket.remove(swipedPosition);
                    adapter.notifyItemRemoved(swipedPosition);
                   // adapter.pendingRemoval(swipedPosition);

                    Log.d("Mensaje","tamaño : " + _itemsTicket.size() + "Posicion: " +swipedPosition);
                    setUpSumaTotal();
                   // _itemsTicket.get(swipedPosition);
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
                background.setBounds(itemView.getLeft() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerViewTicket);
    }

    /**
     * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions
     * after an item is removed.
     */
    private void setUpAnimationDecoratorHelper() {
        recyclerViewTicket.addItemDecoration(new RecyclerView.ItemDecoration() {

            // we want to cache this and not allocate anything repeatedly in the onDraw method
            Drawable background;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
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


    class TestAdapter extends RecyclerView.Adapter {
        List<ModeloInventario> items;
        Context mContext;

        TestAdapter(List<ModeloInventario> item_list,Context context){
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
            ModeloInventario model = items.get(position);
            final String auxId = model.getId();
            final String auxProducto = model.getNombreProducto();
            final String auxCant = model.getCantidad();
            final String auxPrecio = model.getPrecio();

            final String stock = getResources().getString(R.string.Stock)+ " " + model.getCantidad();
            final String precio = getResources().getString(R.string.simbol_moneda) + " " + model.getPrecio();



            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.item.setVisibility(View.VISIBLE);
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.stock.setVisibility(View.VISIBLE);
            viewHolder.precio.setVisibility(View.VISIBLE);
            viewHolder.item.setText(model.getNombreProducto());
            viewHolder.stock.setText(stock);
            viewHolder.precio.setText(precio);
            viewHolder.icon.setImageResource(R.drawable.a_avator);
            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createDialogAddProducto(auxId,auxProducto,auxCant,auxPrecio);
                }
            });

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void createDialogCobrar(){

        }

        public void createDialogAddProducto(final String id, final String producto, final String stock, final String precioVenta){
            final AlertDialog.Builder builder = new AlertDialog.Builder(Venta.this);
            LayoutInflater inflater = Venta.this.getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_add_producto_venta,null);

            builder.setView(view);

            int cantAdd = Integer.parseInt(stock);
            String[] listOp = new String[cantAdd];
            String cantDisp = getString(R.string.disponible,stock);
            for(int i = 0; i < cantAdd; i++){
                listOp[i] = ""+ (i+1);
            }

            TextView item = (TextView) view.findViewById(R.id.dialog_add_item);
            TextView cant = (TextView) view.findViewById(R.id.dialog_add_cantidad);
            final Spinner spinner_opCantidad = (Spinner) view.findViewById(R.id.dialog_add_spinner);
            Button btn_addProducto = (Button) view.findViewById(R.id.dialog_add_btn_agregar);
            Button btn_cancelar = (Button) view.findViewById(R.id.dialog_add_btn_cancelar);
            spinner_opCantidad.setAdapter(new SpinnerAdapter(Venta.this,R.layout.spinner_row_view_cantidad,listOp));
            final AlertDialog dialog = builder.create();

            item.setText(producto);
            cant.setText(cantDisp);

            btn_addProducto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String cantidad_select = String.valueOf(spinner_opCantidad.getSelectedItem());
                    int aux_cantidadSelect = Integer.parseInt(cantidad_select);
                    float aux_precioVenta = Float.parseFloat(precioVenta);
                    float auxImporte = aux_cantidadSelect * aux_precioVenta;
                    String importe = String.valueOf(auxImporte);
                    _itemsTicket.add(new ModeloTicket(id,producto,cantidad_select,precioVenta,importe));
                    setUpSumaTotal();

               /*     //-------Restar stock en Tiempo de ejecucion ---------------
                    int restaStock = Integer.parseInt(stock) - aux_cantidadSelect;
                    String aux_restaStock = String.valueOf(restaStock);
                    _items.set(itemPosition,new ModeloInventario(id,producto,aux_restaStock,precioVenta));
                    setUpSumaTotal();
                    setUpRecyclerView();
                    Snackbar.make(v,getResources().getString(R.string.Productoagregado),Snackbar.LENGTH_LONG).show();*/
                    dialog.cancel();
                }
            });
            btn_cancelar.setOnClickListener(new View.OnClickListener() {
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
        TextView stock;
        TextView precio;
        View mView;

        TestViewHolder(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_venta_inventario, parent, false));
            icon = (ImageView) itemView.findViewById(R.id.list_icon_inventario);
            item = (TextView) itemView.findViewById(R.id.item_inventario);
            stock = (TextView) itemView.findViewById(R.id.cant_stock);
            precio = (TextView) itemView.findViewById(R.id.precio);
            mView = itemView;
        }
    }




}
