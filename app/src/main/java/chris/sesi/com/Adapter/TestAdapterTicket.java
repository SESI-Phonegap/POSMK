package chris.sesi.com.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chris.sesi.com.minegociomk.R;
import chris.sesi.com.minegociomk.Venta;
import chris.sesi.com.model.ModeloInventario;
import chris.sesi.com.model.ModeloTicket;

/**
 * Created by Chris on 12/01/2017.
 */

public class TestAdapterTicket extends RecyclerView.Adapter {
    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    List<ModeloTicket> items;
    Context mContext;
    List<ModeloTicket> itemsPendingRemoval;

    boolean undoOn = true; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<ModeloTicket, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public TestAdapterTicket(List<ModeloTicket> item_list,Context context){
        items = item_list;
        mContext = context;
        itemsPendingRemoval = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TestViewHolder viewHolder = (TestViewHolder) holder;
        final ModeloTicket modeloTicket = items.get(position);

        if (itemsPendingRemoval.contains(modeloTicket)) {
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.btn_cancelar.setVisibility(View.VISIBLE);
            viewHolder.item.setVisibility(View.GONE);
            viewHolder.cant.setVisibility(View.GONE);
            viewHolder.precio.setVisibility(View.GONE);
            viewHolder.importe.setVisibility(View.GONE);
            viewHolder.btn_cancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(modeloTicket);
                    pendingRunnables.remove(modeloTicket);
                    if (pendingRemovalRunnable != null) handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(modeloTicket);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(items.indexOf(modeloTicket));
                }
            });

        }else {
            String id = modeloTicket.getId_producto();
            String producto = modeloTicket.getProducto();
            String cantidad = modeloTicket.getCantidad();
            String precio = "$ " + modeloTicket.getPrecioUnitario();
            String importe = "$ " + modeloTicket.getImporte();

            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.item.setVisibility(View.VISIBLE);
            viewHolder.cant.setVisibility(View.VISIBLE);
            viewHolder.precio.setVisibility(View.VISIBLE);
            viewHolder.importe.setVisibility(View.VISIBLE);

            viewHolder.item.setText(producto);
            viewHolder.cant.setText(cantidad);
            viewHolder.precio.setText(precio);
            viewHolder.importe.setText(importe);

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

    public void pendingRemoval(int position) {
        final ModeloTicket item = items.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(items.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(item, pendingRemovalRunnable);

        }
    }

    public void remove(int position) {
        ModeloTicket item = items.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        ModeloTicket item = items.get(position);
        return itemsPendingRemoval.contains(item);
    }







    static class TestViewHolder extends RecyclerView.ViewHolder {


        TextView item;
        TextView cant;
        TextView precio;
        TextView importe;
        Button btn_cancelar;
        View mView;

        TestViewHolder(ViewGroup parent){
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_ticket, parent, false));
            item = (TextView) itemView.findViewById(R.id.item_inventario);
            cant = (TextView) itemView.findViewById(R.id.cant_stock);
            precio = (TextView) itemView.findViewById(R.id.precio);
            importe = (TextView) itemView.findViewById(R.id.importe);
            btn_cancelar = (Button) itemView.findViewById(R.id.undo_button);
            mView = itemView;
        }
    }

}
