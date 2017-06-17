package chris.sesi.com.Adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chris.sesi.com.minegociomk.FormAgregarStock;
import chris.sesi.com.minegociomk.R;
import chris.sesi.com.utils.UtilsDml;

/**
 * Created by QUALITY on 16/06/2017.
 */

public class TestAdapterCatalogo extends RecyclerView.Adapter {
    static List<String> items;
    List<String> itemId;
    Context mContext;



    TestAdapterCatalogo(List<String> item_list,Context context, List<String> itemsID){
        items = item_list;
        mContext = context;
        itemId = itemsID;

    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TestViewHolder viewHolder = (TestViewHolder) holder;
        final String item = items.get(position);
        final String item_Id = itemId.get(position);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewProductDialog();
            }
        }); */

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



    public static void createNewProductDialog(final Application mContext){
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    Snackbar.make(v,mContext.getResources().getString(R.string.Campovacio),Snackbar.LENGTH_LONG).show();

                }else {
                    UtilsDml.insertProduct(mContext,product);
                    items.add(product);
                //    setUpRecyclerView();
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

    private static class TestViewHolder extends RecyclerView.ViewHolder {

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

}

