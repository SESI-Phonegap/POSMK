package chris.sesi.com.view.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import chris.sesi.com.view.utils.Constants;
import chris.sesi.com.view.utils.ImageFilePath;
import chris.sesi.com.view.utils.UtilsDml;
import chris.sesi.com.view.R;
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
    private String selectedImagePath;

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
            final String item_photo = itemsPhoto.get(position);
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
                if (Build.VERSION.SDK_INT >= 23){
                    Bitmap bitmap = BitmapFactory.decodeFile(item_photo);
                    viewHolder.icon.setImageBitmap(bitmap);
                } else {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(item_photo));
                        viewHolder.icon.setImageBitmap(bitmap);
                        // viewHolder.icon.setImageURI(Uri.parse(item_photo));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FormAgregarStock.class);
                    intent.putExtra("ID_Product", item_Id);
                    intent.putExtra("item_Name", item);
                    intent.putExtra("item_photo",item_photo);
                    intent.putExtra(Constants.ORIGIN_ACTIVITY,Constants.ACTUALIZAR);
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
                        if (selectedImagePath == null) {
                            selectedImagePath = "";
                        }
                        UtilsDml.insertProduct(getApplication(), product, selectedImagePath);
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

        CircleImageView icon;
        TextView item;
        View mView;

        TestViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_catalogo, parent, false));
            icon = (CircleImageView) itemView.findViewById(R.id.list_icon_catalogo);
            item = (TextView) itemView.findViewById(R.id.item_catalogo);
            mView = itemView;
        }
    }


    private void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(Catalogo.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(Catalogo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);

            } else {
                /*Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);*/
                galleryFilter(PICK_IMAGE);
            }
        } else {
           /* Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);*/
           galleryFilter(PICK_IMAGE);
        }

    }

    public void galleryFilter(int code){
        List<Intent> targetGalleryIntents = new ArrayList<>();
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        PackageManager pm = getApplicationContext().getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(galleryIntent,0);
        if (!resInfos.isEmpty()){
            for (ResolveInfo resInfo : resInfos){
                String packageName = resInfo.activityInfo.packageName;

                if (!packageName.contains("com.google.android.apps.photos") && !packageName.equals("com.google.android.apps.plus")){
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setPackage(packageName);
                    targetGalleryIntents.add(intent);
                }
            }

            if (!targetGalleryIntents.isEmpty()){
                Collections.sort(targetGalleryIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetGalleryIntents.remove(0), "Abrir Galeria");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetGalleryIntents.toArray(new Parcelable[]{}));
                startActivityForResult(chooserIntent,PICK_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(),"No se encontro la galeria",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            uriPhotoResult = data.getData();

            if (Build.VERSION.SDK_INT >= 23) {
                selectedImagePath = ImageFilePath.getPath(getApplication(), uriPhotoResult);
            } else {
                selectedImagePath = uriPhotoResult.toString();
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriPhotoResult);
                photoCatalogo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
