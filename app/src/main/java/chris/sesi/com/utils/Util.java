package chris.sesi.com.utils;

import android.app.Application;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.view.View;
import java.util.List;
import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;
import chris.sesi.com.minegociomk.MenuPrincipal;
import chris.sesi.com.minegociomk.R;

/**
 * Created by QUALITY on 16/06/2017.
 */

public class Util {

    public static void iniciarSesion(Application context, View view, String user, String pass) {

        String passUser, idEmail;

        Snackbar.make(view, "User: " + user + " Pass: " + pass, Snackbar.LENGTH_LONG).show();
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.User.COLUMN_NAME_PK_ID,
                ContractSql.User.COLUMN_NAME_PASS_USER};
        //Filtro del query WHERE
        String selection = ContractSql.User.COLUMN_NAME_PK_ID + " =? AND " + ContractSql.User.COLUMN_NAME_PASS_USER + " =?";
        String[] selectionArgs = {user, pass};

        String sortOrder = "";  // Orden de la consulta

        Cursor cursor = db.query(
                ContractSql.User.TABLE_NAME,               //Nombre de la tabla
                projection,                                 //Campos requeridos de la tabla
                selection,                                 //Condicion Where
                selectionArgs,                             // Argumentos de la condicion
                null,
                null,
                null);

        if (cursor.moveToFirst()) {
            idEmail = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PK_ID));
            passUser = cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.User.COLUMN_NAME_PASS_USER));
            cursor.close();
            db.close();
            Intent intent = new Intent(context, MenuPrincipal.class);
            context.startActivity(intent);


        } else {
            cursor.close();
            db.close();
            Snackbar.make(view, context.getResources().getString(R.string.Error_Sesion) + "User: " + user + " Pass: " + pass, Snackbar.LENGTH_LONG).show();

        }
    }

    public static void altaUsuario(Application context, String email, String nombre, String contraseña,
                            String userMk, String passMk, String nivelMk ){

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.User.COLUMN_NAME_PK_ID, email);
        values.put(ContractSql.User.COLUMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.User.COLUMN_NAME_NIVELMK, nivelMk);
        values.put(ContractSql.User.COLUMN_NAME_PASS_USER, contraseña);
        values.put(ContractSql.User.COLUMN_NAME_USER_MK, userMk);
        values.put(ContractSql.User.COLUMN_NAME_PASS_MK, passMk);

        sqLiteDatabase.insert(ContractSql.User.TABLE_NAME, null, values);
        sqLiteDatabase.close();


    }

    public static void consultaCatalogo(Application context,List<String> items, List<String> itemsid){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.COLUMN_NAME_NOMBRE,
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

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public static void insertProduct(Application context,String product){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Producto.COLUMN_NAME_NOMBRE,product);
        values.put(ContractSql.Producto.COLUMN_NAME_DESCRIPCION, "");
        values.put(ContractSql.Producto.COLUMN_NAME_VENDIDOS,0);

        db.insert(ContractSql.Producto.TABLE_NAME,null,values);
        db.close();

    }
}
