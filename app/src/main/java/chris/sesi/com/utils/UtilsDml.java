package chris.sesi.com.utils;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;
import chris.sesi.com.minegociomk.MainActivity;
import chris.sesi.com.minegociomk.MenuPrincipal;
import chris.sesi.com.minegociomk.R;
import chris.sesi.com.model.ModelConsultora;
import de.hdodenhof.circleimageview.CircleImageView;


public class UtilsDml {

    public static boolean checkUserExist(Application context) {
        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        //   String sortOrder = "";  // Orden de la consulta

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            //     idEmail = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            //     passUser = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PASS_USER));
            cursor.close();
            db.close();
            bIsOk = true;
         /*   Intent intent = new Intent(context, MenuPrincipal.class);
            context.startActivity(intent);*/


        } else {
            cursor.close();
            db.close();
        }

        return bIsOk;
    }

    public static boolean iniciarSesion(Application context, View view, String user, String pass) {
        boolean bIsOk = false;
        // String passUser, idEmail;

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID,
                ContractSql.User.COLUMN_NAME_PASS_USER};
        //Filtro del query WHERE
        String selection = ContractSql.User.COLUMN_NAME_PK_ID + " =? AND " + ContractSql.User.COLUMN_NAME_PASS_USER + " =?";
        String[] selectionArgs = {user, pass};

        //   String sortOrder = "";  // Orden de la consulta

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            //     idEmail = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            //     passUser = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PASS_USER));
            cursor.close();
            db.close();
            bIsOk = true;
         /*   Intent intent = new Intent(context, MenuPrincipal.class);
            context.startActivity(intent);*/


        } else {
            cursor.close();
            db.close();
        }

        return bIsOk;
    }

    public static boolean obtenerUsuario(Application context, String[] data) {

        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID,
                ContractSql.User.COLUMN_NAME_NOMBRE,
                ContractSql.User.COLUMN_NAME_NIVELMK,
                ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            bIsOk = true;
            data[0] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            data[1] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NOMBRE));
            data[2] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NIVELMK));
            data[3] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER));

        } else {
            Toast.makeText(context, context.getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static boolean obtenerUsuarioForProfile(Application context, String[] data) {

        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID,
                ContractSql.User.COLUMN_NAME_NOMBRE,
                ContractSql.User.COLUMN_NAME_NIVELMK,
                ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER,
                ContractSql.User.COLUMN_NAME_PASS_USER,
                ContractSql.User.COLUMN_NAME_USER_MK,
                ContractSql.User.COLUMN_NAME_PASS_MK};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            bIsOk = true;
            data[0] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            data[1] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NOMBRE));
            data[2] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_NIVELMK));
            data[3] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_USER_MK));
            data[4] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PASS_MK));
            data[5] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PASS_USER));
            data[6] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER));

        } else {
            Toast.makeText(context, context.getString(R.string.errorRegistro), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(context, MenuPrincipal.class);
            context.startActivity(intent);
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static boolean altaUsuario(Application context, String email, String nombre, String contraseña,
                                      String userMk, String passMk, String nivelMk) {
        boolean bIsOk = false;

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.User.COLUMN_NAME_PK_ID, email);
        values.put(ContractSql.User.COLUMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.User.COLUMN_NAME_NIVELMK, nivelMk);
        values.put(ContractSql.User.COLUMN_NAME_PASS_USER, contraseña);
        values.put(ContractSql.User.COLUMN_NAME_USER_MK, userMk);
        values.put(ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER, "");
        values.put(ContractSql.User.COLUMN_NAME_PASS_MK, passMk);

        if (sqLiteDatabase.insert(ContractSql.User.TABLE_NAME, null, values) != -1) {
            bIsOk = true;
        }
        sqLiteDatabase.close();
        return bIsOk;
    }

    public static boolean updateImageUser(Application context, String idEmail, String uriImage) {
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER, uriImage);

        String selection = ContractSql.User.COLUMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {idEmail};

        int result = db.update(ContractSql.User.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return result > 0;
    }

    public static boolean altaConsultora(Application context, String nombre, String nivel, String direccion, String telefono, Uri srcFoto) {
        boolean bIsOk = false;

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Consultoras.COLIMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.Consultoras.COLIMN_NAME_NIVEL, nivel);
        values.put(ContractSql.Consultoras.COLIMN_NAME_DIRECCION, direccion);
        values.put(ContractSql.Consultoras.COLIMN_NAME_TELEFONO, telefono);
        values.put(ContractSql.Consultoras.COLIMN_NAME_STATUS_UNIDAD, 0);
        values.put(ContractSql.Consultoras.COLIMN_NAME_IMG_CONSULTORA, srcFoto.toString());

        if (sqLiteDatabase.insert(ContractSql.Consultoras.TABLE_NAME, null, values) != -1) {
            bIsOk = true;
        }
        sqLiteDatabase.close();
        return bIsOk;
    }


    public static void consultaCatalogo(Application context, List<String> items, List<String> itemsid, List<String> itemPhoto) {
        items.clear();
        itemsid.clear();
        itemPhoto.clear();
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.COLUMN_NAME_NOMBRE,
                ContractSql.Producto.COLUMN_NAME_IMG_PRODUCTO,
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
                itemPhoto.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_IMG_PRODUCTO)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public static void insertProduct(Application context, String product, String uriPhoto) {
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContractSql.Producto.COLUMN_NAME_NOMBRE, product);
        values.put(ContractSql.Producto.COLUMN_NAME_IMG_PRODUCTO, uriPhoto);
        values.put(ContractSql.Producto.COLUMN_NAME_DESCRIPCION, "");
        values.put(ContractSql.Producto.COLUMN_NAME_VENDIDOS, 0);

        db.insert(ContractSql.Producto.TABLE_NAME, null, values);
        db.close();

    }

    public static boolean consultaInventario(Application context, List<String> items,
                                             List<String> itemId, List<String> itemPhoto,
                                             List<String> itemCant, List<String> itemCantMin) {
        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.TABLE_NAME + "." + ContractSql.Producto.COLUMN_NAME_NOMBRE,
                ContractSql.Inventario.TABLE_NAME + "." + ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO,
                ContractSql.Inventario.TABLE_NAME + "." + ContractSql.Inventario.COLUMN_NAME_STOCK,
                ContractSql.Inventario.TABLE_NAME + "." + ContractSql.Inventario.COLUMN_NAME_STOCK,
                ContractSql.Inventario.TABLE_NAME + "." + ContractSql.Inventario.COLUMN_NAME_MIN_STOCK,
                ContractSql.Producto.TABLE_NAME + "." + ContractSql.Producto.COLUMN_NAME_IMG_PRODUCTO};

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContractSql.Producto.TABLE_NAME + " INNER JOIN " + ContractSql.Inventario.TABLE_NAME + " ON " +
                ContractSql.Producto.TABLE_NAME + "." + ContractSql.Producto.COLUMN_NAME_PK_ID + " = " + ContractSql.Inventario.TABLE_NAME + "." + ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO);


        //Filtro del query WHERE
        // String selection =  ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " =? ";
        //   String[] selectionArgs = {ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO};
        String orderBy = ContractSql.Producto.TABLE_NAME + "." + ContractSql.Producto.COLUMN_NAME_NOMBRE + " ASC";
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                null,
                null,
                null,
                null,
                orderBy);

   /*     Cursor cursor = db.query(
                ContractSql.Producto.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);
*/
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            bIsOk = true;
            do {
                items.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_NOMBRE)));
                itemId.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO)));
                itemPhoto.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Producto.COLUMN_NAME_IMG_PRODUCTO)));
                itemCant.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_STOCK)));
                itemCantMin.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Inventario.COLUMN_NAME_MIN_STOCK)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static void consultaConsultoras(Application context, List<ModelConsultora> consultoras) {
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Consultoras.COLIMN_NAME_PK_ID,
                ContractSql.Consultoras.COLIMN_NAME_NOMBRE,
                ContractSql.Consultoras.COLIMN_NAME_TELEFONO,
                ContractSql.Consultoras.COLIMN_NAME_STATUS_UNIDAD};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        Cursor cursor = db.query(
                ContractSql.Consultoras.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                consultoras.add(new ModelConsultora(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_PK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NOMBRE)),
                        "",
                        "",
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_TELEFONO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_STATUS_UNIDAD))));


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public static boolean ConsultoraDetail(Application context, String id, EditText nombre_consultora,
                                           EditText telefono_consultora, EditText direccion_consultora, EditText nivel_consultora, CircleImageView photo) {

        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Consultoras.COLIMN_NAME_PK_ID,
                ContractSql.Consultoras.COLIMN_NAME_NOMBRE,
                ContractSql.Consultoras.COLIMN_NAME_TELEFONO,
                ContractSql.Consultoras.COLIMN_NAME_DIRECCION,
                ContractSql.Consultoras.COLIMN_NAME_NIVEL,
                ContractSql.Consultoras.COLIMN_NAME_IMG_CONSULTORA};
        //Filtro del query WHERE
        String selection = ContractSql.Consultoras.COLIMN_NAME_PK_ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                ContractSql.Consultoras.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            bIsOk = true;
            //Recorremos el cursor hasta que no haya más registros
            do {
                nombre_consultora.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NOMBRE)));
                telefono_consultora.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_TELEFONO)));
                direccion_consultora.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_DIRECCION)));
                nivel_consultora.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL)));

                if (Build.VERSION.SDK_INT >= 23){
                    if (cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL)).equals("")){
                        photo.setImageResource(R.drawable.femeie);
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL)));
                        photo.setImageBitmap(bitmap);
                    }

                } else {
                    if (Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL))) != null){
                        if (Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL))).toString().equals("")){
                            photo.setImageResource(R.drawable.femeie);
                        } else {
                            photo.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL))));
                        }
                    }
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }


    public static void consultaClientes(Application context, List<String> items, List<String> itemTel,
                                        List<String> itemId, List<String> item_photo) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Clientes.COLUMN_NAME_PK_ID,
                ContractSql.Clientes.COLUMN_NAME_NOMBRE,
                ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT,
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
                item_photo.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

    }

    public static boolean updateImageClient(Application context, String id, String uriImage) {
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT, uriImage);

        String selection = ContractSql.Clientes.COLUMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Clientes.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return result > 0;
    }

    public static boolean consultoraUpdate(Application context, String id, String nombre, String telefono, String direccion, String nivel) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Consultoras.COLIMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.Consultoras.COLIMN_NAME_TELEFONO, telefono);
        values.put(ContractSql.Consultoras.COLIMN_NAME_DIRECCION, direccion);
        values.put(ContractSql.Consultoras.COLIMN_NAME_NIVEL, nivel);

        String selection = ContractSql.Consultoras.COLIMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Consultoras.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return result > 0;

    }

    public static boolean ClientDetail(Application context, String id, String[] result) {

        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Clientes.COLUMN_NAME_PK_ID,
                ContractSql.Clientes.COLUMN_NAME_NOMBRE,
                ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT,
                ContractSql.Clientes.COLUMN_NAME_TELEFONO,
                ContractSql.Clientes.COLUMN_NAME_DIRECCION,
                ContractSql.Clientes.COLUMN_NAME_OCUPACION};
        //Filtro del query WHERE
        String selection = ContractSql.Clientes.COLUMN_NAME_PK_ID + " = ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(
                ContractSql.Clientes.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            bIsOk = true;
            //Recorremos el cursor hasta que no haya más registros
            do {
                result[0] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_NOMBRE));
                result[1] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_TELEFONO));
                result[2] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_DIRECCION));
                result[3] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_OCUPACION));
                result[4] = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static boolean ClientUpdate(Application context, String id, String nombre, String telefono, String direccion, String ocupacion) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Clientes.COLUMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.Clientes.COLUMN_NAME_TELEFONO, telefono);
        values.put(ContractSql.Clientes.COLUMN_NAME_DIRECCION, direccion);
        values.put(ContractSql.Clientes.COLUMN_NAME_OCUPACION, ocupacion);

        String selection = ContractSql.Clientes.COLUMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Clientes.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return result > 0;

    }


    public static boolean altaCliente(Application context, String nombre, String direccion, String telefono, String ocupacion, Uri uriPhoto) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT, uriPhoto.toString());
        values.put(ContractSql.Clientes.COLUMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.Clientes.COLUMN_NAME_DIRECCION, direccion);
        values.put(ContractSql.Clientes.COLUMN_NAME_TELEFONO, telefono);
        values.put(ContractSql.Clientes.COLUMN_NAME_OCUPACION, ocupacion);

        long result = sqLiteDatabase.insert(ContractSql.Clientes.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        return result > 0;
    }


    public static boolean consultaMiUnidad(Application context, List<ModelConsultora> items) {
        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_NOMBRE,
                ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_NIVEL,
                ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_TELEFONO,
                ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_IMG_CONSULTORA,
                ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_PK_ID,
                ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_STATUS_UNIDAD,
                ContractSql.Unidad.TABLE_NAME + "." + ContractSql.Unidad.COLIMN_NAME_PK_ID};

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContractSql.Consultoras.TABLE_NAME + " INNER JOIN " + ContractSql.Unidad.TABLE_NAME + " ON " +
                ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_PK_ID + " = " +
                ContractSql.Unidad.TABLE_NAME + "." + ContractSql.Unidad.COLIMN_NAME_PK_ID);


        //Filtro del query WHERE
        // String selection =  ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " =? ";
        //   String[] selectionArgs = {ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO};
        String orderBy = ContractSql.Consultoras.TABLE_NAME + "." + ContractSql.Consultoras.COLIMN_NAME_NOMBRE + " ASC";
        Cursor cursor = queryBuilder.query(
                db,
                projection,
                null,
                null,
                null,
                null,
                orderBy);

   /*     Cursor cursor = db.query(
                ContractSql.Producto.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);
*/
        if (cursor.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            bIsOk = true;
            do {
                items.add(new ModelConsultora(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_PK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NOMBRE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_IMG_CONSULTORA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NIVEL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_TELEFONO)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_STATUS_UNIDAD))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static boolean checkUnidadExist(Application context) {
        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Unidad.COLIMN_NAME_PK_ID};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        //   String sortOrder = "";  // Orden de la consulta

        Cursor cursor = db.query(
                ContractSql.Unidad.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            cursor.close();
            db.close();
            bIsOk = true;

        } else {
            cursor.close();
            db.close();
        }

        return bIsOk;
    }

    public static String consultaUnidad(Application context){
        String result = "";
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Unidad.COLIMN_NAME_PK_ID};
        //Filtro del query WHERE
        String selection = "";
        String[] selectionArgs = {};

        //   String sortOrder = "";  // Orden de la consulta

        Cursor cursor = db.query(
                ContractSql.Unidad.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Unidad.COLIMN_NAME_PK_ID));
            cursor.close();
            db.close();


        } else {
            cursor.close();
            db.close();
        }

        return result;
    }

    public static boolean altaUnidad(Application context, String nombre) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContractSql.Unidad.COLIMN_NAME_UNIDAD_MK, nombre);

        long result = sqLiteDatabase.insert(ContractSql.Unidad.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        return result > 0;
    }

    public static boolean addConsultoraUnidad(Application context, String id_consultora, String id_unidad){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ContractSql.UnidadConsultora.COLIMN_NAME_FK_ID_CONSULTORA,id_consultora);
        values.put(ContractSql.UnidadConsultora.COLIMN_NAME_FK_ID_UNIDAD_MK, id_unidad);

        long result = sqLiteDatabase.insert(ContractSql.UnidadConsultora.TABLE_NAME,null,values);
        sqLiteDatabase.close();

        return result > 0;
    }


}
