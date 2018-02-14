package chris.sesi.com.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import chris.sesi.com.view.R;
import chris.sesi.com.data.model.ModelConsultora;


public class AdapterUnidad extends Adapter {
    private List<ModelConsultora> listConsultoras;
    private Context context;

    public AdapterUnidad(Context context, List<ModelConsultora> listConsultoras){
        this.listConsultoras = listConsultoras;
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestViewHolder viewHolder = (TestViewHolder) holder;
        ModelConsultora consultora = listConsultoras.get(position);

        viewHolder.view.setBackgroundColor(Color.WHITE);
        viewHolder.imgConsultora.setVisibility(View.VISIBLE);
        viewHolder.tvNombre.setVisibility(View.VISIBLE);
        viewHolder.tvTelefono.setVisibility(View.VISIBLE);

        viewHolder.tvNombre.setText(consultora.getNombre());
        viewHolder.tvTelefono.setText(consultora.getTelefono());

        if (Build.VERSION.SDK_INT >= 23){
            if (consultora.getFoto() != null){
                if (consultora.getFoto().equals("")){
                    viewHolder.imgConsultora.setImageResource(R.drawable.ic_person_black_24dp);
                }else {
                    Bitmap bitmap = BitmapFactory.decodeFile(consultora.getFoto());
                    viewHolder.imgConsultora.setImageBitmap(bitmap);
                }
            }
        } else {
            if (Uri.parse(consultora.getFoto()).toString().equals("")) {
                viewHolder.imgConsultora.setImageResource(R.drawable.ic_person_black_24dp);
            } else {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(consultora.getFoto()));
                    viewHolder.imgConsultora.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return listConsultoras.size();
    }



    private static class TestViewHolder extends RecyclerView.ViewHolder{

        ImageView imgConsultora;
        TextView tvNombre;
        TextView tvTelefono;
        View view;

        TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false));
            imgConsultora = (ImageView) itemView.findViewById(R.id.list_avatar_clienta);
            tvNombre = (TextView) itemView.findViewById(R.id.clienta);
            tvTelefono = (TextView) itemView.findViewById(R.id.cliente_tel);
            view = itemView;

        }
    }
}


