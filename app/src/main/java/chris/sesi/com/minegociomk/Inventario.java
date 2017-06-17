package chris.sesi.com.minegociomk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import chris.sesi.com.utils.UtilsDml;

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

        if (!UtilsDml.consultaInventario(getApplication(),items,itemsID)){
            Toast.makeText(this,getString(R.string.sinInventario),Toast.LENGTH_LONG).show();
        }
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

    private class TestAdapter extends RecyclerView.Adapter {
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

    private static class TestViewHolder extends RecyclerView.ViewHolder {

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


}
