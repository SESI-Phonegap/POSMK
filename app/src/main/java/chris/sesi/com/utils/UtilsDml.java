package chris.sesi.com.utils;

import android.app.Application;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import java.util.List;
import chris.sesi.com.database.AdminSQLiteOpenHelper;
import chris.sesi.com.database.ContractSql;


public class UtilsDml {

    public static boolean checkUserExist(Application context){
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

    public static boolean altaUsuario(Application context, String email, String nombre, String contraseña,
                            String userMk, String passMk, String nivelMk ){
        boolean bIsOk = false;

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.User.COLUMN_NAME_PK_ID, email);
        values.put(ContractSql.User.COLUMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.User.COLUMN_NAME_NIVELMK, nivelMk);
        values.put(ContractSql.User.COLUMN_NAME_PASS_USER, contraseña);
        values.put(ContractSql.User.COLUMN_NAME_USER_MK, userMk);
        values.put(ContractSql.User.COLUMN_NAME_PASS_MK, passMk);

        if (sqLiteDatabase.insert(ContractSql.User.TABLE_NAME, null, values) != -1){
            bIsOk = true;
        }
        sqLiteDatabase.close();
        return  bIsOk;
    }

    public static boolean altaConsultora(Application context, String nombre, String nivel, String direccion, String telefono){
        boolean bIsOk = false;

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Consultoras.COLIMN_NAME_NOMBRE, nombre);
        values.put(ContractSql.Consultoras.COLIMN_NAME_NIVEL, nivel);
        values.put(ContractSql.Consultoras.COLIMN_NAME_DIRECCION, direccion);
        values.put(ContractSql.Consultoras.COLIMN_NAME_TELEFONO, telefono);

        if (sqLiteDatabase.insert(ContractSql.Consultoras.TABLE_NAME, null, values) != -1){
            bIsOk = true;
        }
        sqLiteDatabase.close();
        return  bIsOk;
    }

    public static void consultaCatalogo(Application context,List<String> items, List<String> itemsid){
        items.clear();
        itemsid.clear();
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

    public static boolean consultaInventario(Application context,List<String> items, List<String> itemId){
        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        String[] projection = {ContractSql.Producto.TABLE_NAME+"."+ContractSql.Producto.COLUMN_NAME_NOMBRE,
                ContractSql.Inventario.TABLE_NAME+"."+ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO,
                ContractSql.Inventario.TABLE_NAME+"."+ContractSql.Inventario.COLUMN_NAME_STOCK};

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContractSql.Producto.TABLE_NAME + " INNER JOIN " + ContractSql.Inventario.TABLE_NAME + " ON " +
                ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " = " + ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO );



        //Filtro del query WHERE
        // String selection =  ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_PK_ID + " =? ";
        //   String[] selectionArgs = {ContractSql.Inventario.TABLE_NAME+"."+ ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO};
        String orderBy = ContractSql.Producto.TABLE_NAME+"."+ ContractSql.Producto.COLUMN_NAME_NOMBRE + " ASC";
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
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static void consultaConsultoras(Application context, List<String> items, List<String> itemTel, List<String> itemId){
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Consultoras.COLIMN_NAME_PK_ID,
                ContractSql.Consultoras.COLIMN_NAME_NOMBRE,
                ContractSql.Consultoras.COLIMN_NAME_TELEFONO};
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
                itemId.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_PK_ID)));
                items.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_NOMBRE)));
                itemTel.add(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Consultoras.COLIMN_NAME_TELEFONO)));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public static boolean ConsultoraDetail(Application context, String id, EditText nombre_consultora,
                                       EditText telefono_consultora, EditText direccion_consultora, EditText nivel_consultora){

        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Consultoras.COLIMN_NAME_PK_ID,
                ContractSql.Consultoras.COLIMN_NAME_NOMBRE,
                ContractSql.Consultoras.COLIMN_NAME_TELEFONO,
                ContractSql.Consultoras.COLIMN_NAME_DIRECCION,
                ContractSql.Consultoras.COLIMN_NAME_NIVEL};
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

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }


    public static void consultaClientes(Application context, List<String> items, List<String> itemTel, List<String> itemId) {

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
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

    public static boolean consultoraUpdate(Application context, String id,String nombre, String telefono, String direccion, String nivel){

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Consultoras.COLIMN_NAME_NOMBRE,nombre);
        values.put(ContractSql.Consultoras.COLIMN_NAME_TELEFONO,telefono);
        values.put(ContractSql.Consultoras.COLIMN_NAME_DIRECCION,direccion);
        values.put(ContractSql.Consultoras.COLIMN_NAME_NIVEL,nivel);

        String selection = ContractSql.Consultoras.COLIMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Consultoras.TABLE_NAME,values,selection,selectionArgs);
        db.close();

        return result > 0;

    }

    public static boolean ClientDetail(Application context, String id, EditText nombre_cliente,
                                    EditText telefono_cliente, EditText direccion_cliente, EditText ocupacion_cliente){

        boolean bIsOk = false;
        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        //Columnas requeridas
        String[] projection = {ContractSql.Clientes.COLUMN_NAME_PK_ID,
                ContractSql.Clientes.COLUMN_NAME_NOMBRE,
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
                nombre_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_NOMBRE)));
                telefono_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_TELEFONO)));
                direccion_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_DIRECCION)));
                ocupacion_cliente.setText(cursor.getString(cursor.getColumnIndexOrThrow(ContractSql.Clientes.COLUMN_NAME_OCUPACION)));

            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return bIsOk;
    }

    public static boolean ClientUpdate(Application context, String id,String nombre, String telefono, String direccion, String ocupacion){

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = adminSQLiteOpenHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Clientes.COLUMN_NAME_NOMBRE,nombre);
        values.put(ContractSql.Clientes.COLUMN_NAME_TELEFONO,telefono);
        values.put(ContractSql.Clientes.COLUMN_NAME_DIRECCION,direccion);
        values.put(ContractSql.Clientes.COLUMN_NAME_OCUPACION,ocupacion);

        String selection = ContractSql.Clientes.COLUMN_NAME_PK_ID + " =?";
        String[] selectionArgs = {id};

        int result = db.update(ContractSql.Clientes.TABLE_NAME,values,selection,selectionArgs);
        db.close();

        return result > 0;

    }


    public static boolean altaCliente(Application context, String nombre, String direccion, String telefono, String ocupacion){

        AdminSQLiteOpenHelper adminSQLiteOpenHelper = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase sqLiteDatabase = adminSQLiteOpenHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ContractSql.Clientes.COLUMN_NAME_NOMBRE,nombre);
        values.put(ContractSql.Clientes.COLUMN_NAME_DIRECCION,direccion);
        values.put(ContractSql.Clientes.COLUMN_NAME_TELEFONO,telefono);
        values.put(ContractSql.Clientes.COLUMN_NAME_OCUPACION,ocupacion);

        long result = sqLiteDatabase.insert(ContractSql.Clientes.TABLE_NAME, null, values);
        sqLiteDatabase.close();

        return result > 0;
    }


}
