package chris.sesi.com.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MiNegocioMk.db";
    private static final int DATABASE_VERSION = 1;

    public AdminSQLiteOpenHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //----------------------TABLA USUARIO------------------
        db.execSQL("CREATE TABLE "+ ContractSql.User.TABLE_NAME +" ("+
                ContractSql.User.COLUMN_NAME_PK_ID + " TEXT PRIMARY KEY, "+
                ContractSql.User.COLUMN_NAME_NOMBRE + " TEXT  , "+
                ContractSql.User.COLUMN_NAME_NIVELMK + " TEXT  , "+
                ContractSql.User.COLUMN_NAME_PASS_USER + " TEXT  , "+
                ContractSql.User.COLUMN_NAME_USER_MK + " TEXT  , "+
                ContractSql.User.COLUMN_NAME_SRC_IMAGEUSER + " TEXT ,"+
                ContractSql.User.COLUMN_NAME_PASS_MK + " TEXT  );"
        );

        //---------------------TABLA CLIENTES ---------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Clientes.TABLE_NAME + " ("+
                ContractSql.Clientes.COLUMN_NAME_PK_ID + " INTEGER PRIMARY KEY, "+
                ContractSql.Clientes.COLUMN_NAME_SRC_IMAGECLIENT + " TEXT , " +
                ContractSql.Clientes.COLUMN_NAME_NOMBRE + " TEXT  , " +
                ContractSql.Clientes.COLUMN_NAME_DIRECCION + " TEXT  , " +
                ContractSql.Clientes.COLUMN_NAME_TELEFONO + " TEXT  , " +
                ContractSql.Clientes.COLUMN_NAME_OCUPACION + " TEXT  );"
        );

        //----------------------TABLA PRODUCTO---------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Producto.TABLE_NAME + " (" +
                ContractSql.Producto.COLUMN_NAME_PK_ID + " INTEGER PRIMARY KEY, " +
                ContractSql.Producto.COLUMN_NAME_NOMBRE + " TEXT  , "+
                ContractSql.Producto.COLUMN_NAME_DESCRIPCION + " TEXT  , " +
                ContractSql.Producto.COLUMN_NAME_VENDIDOS + " INTEGER   );"
        );

        //---------------------TABLA VENTA ------------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Venta.TABLE_NAME + " (" +
                ContractSql.Venta.COLUMN_NAME_PK_ID + " INTEGER PRIMARY KEY, " +
                ContractSql.Venta.COLUMN_NAME_FECHA + " DATETIME  , " +
                ContractSql.Venta.COLUMN_NAME_TOTAL + " REAL  , " +
                ContractSql.Venta.COLUMN_NAME_ESTATUS + " INTEGER NOY , " +
                ContractSql.Venta.COLUMN_NAME_FK_ID_CLIENTE + " INTEGER  , " +
                ContractSql.Venta.FOREIGN_KEY_ID_CLIENTE +");"
        );

        //--------------------TABLA TICKET --------------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Ticket.TABLE_NAME + " (" +
                ContractSql.Ticket.COLUMN_NAME_CANTIDAD + " INTEGER  , " +
                ContractSql.Ticket.COLUMN_NAME_PRECIO + " REAL  , " +
                ContractSql.Ticket.COLUMN_NAME_IMPORTE + " REAL ," +
                ContractSql.Ticket.COLUMN_NAME_FK_ID_VENTA + " INTEGER  , " +
                ContractSql.Ticket.COLUMN_NAME_FK_ID_PRODUCTO + " INTEGER  , " +
                ContractSql.Ticket.FOREIGN_KEY_ID_PRODUCTO + ", " +
                ContractSql.Ticket.FOREIGN_KEY_ID_VENTA + " );"
        );

        //---------------------TABLA INVENTARIO ----------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Inventario.TABLE_NAME + " (" +
                ContractSql.Inventario.COLUMN_NAME_PRECIO_COMPRA + " REAL  , " +
                ContractSql.Inventario.COLUMN_NAME_PRECIO_VENTA + " REAL  , " +
                ContractSql.Inventario.COLUMN_NAME_STOCK + " INTEGER  , " +
                ContractSql.Inventario.COLUMN_NAME_MIN_STOCK + " INTEGER  , " +
                ContractSql.Inventario.COLUMN_NAME_FK_ID_PRODUCTO + " INTEGER  , " +
                ContractSql.Inventario.FOREIGN_KEY_ID_PRODUCTO + " );"
        );

        //---------------------------TABLA ABONO ---------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Abono.TABLE_NAME + " (" +
                ContractSql.Abono.COLIMN_NAME_PK_ID + " INTEGER PRIMARY KEY, " +
                ContractSql.Abono.COLUMN_NAME_FECHA + " DATETIME  , " +
                ContractSql.Abono.COLUMN_NAME_MONTO + " REAL  , " +
                ContractSql.Abono.COLUMN_NAME_SALDO_ACTUAL + " REAL  , " +
                ContractSql.Abono.COLUMN_NAME_FK_ID_VENTA + " INTEGER  , " +
                ContractSql.Abono.FOREIGN_KEY_ID_VENTA + " );"
        );

        //--------------------------TABLA CONSULTORAS---------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Consultoras.TABLE_NAME + " (" +
                ContractSql.Consultoras.COLIMN_NAME_PK_ID + " INTEGER PRIMARY KEY, " +
                ContractSql.Consultoras.COLIMN_NAME_NOMBRE + " TEXT  , " +
                ContractSql.Consultoras.COLIMN_NAME_NIVEL + " TEXT  , " +
                ContractSql.Consultoras.COLIMN_NAME_DIRECCION + " TEXT  , " +
                ContractSql.Consultoras.COLIMN_NAME_TELEFONO + " TEXT   );"
        );

        //----------------------------TABLA UNIDAD MK -----------------------------------
        db.execSQL("CREATE TABLE " + ContractSql.Unidad.TABLE_NAME + " (" +
                ContractSql.Unidad.COLIMN_NAME_UNIDAD_MK + " TEXT  , " +
                ContractSql.Unidad.COLIMN_NAME_FK_ID_CONSULTORA + " INTEGER  , " +
                ContractSql.Unidad.FOREIGN_KEY_ID_CONSULTORA + " );"
        );

        //----------------------------TABLA PRESTAMO CONSULTORA -----------------------------
        db.execSQL("CREATE TABLE " + ContractSql.PrestamoConsultora.TABLE_NAME + " (" +
                ContractSql.PrestamoConsultora.COLIMN_NAME_PK_ID + " INTEGER PRIMARY KEY, " +
                ContractSql.PrestamoConsultora.COLIMN_NAME_FECHA + " DATETIME  , " +
                ContractSql.PrestamoConsultora.COLIMN_NAME_CANTIDAD + " INTEGER  , " +
                ContractSql.PrestamoConsultora.COLIMN_NAME_ESTATUS +  " INTEGER  , " +
                ContractSql.PrestamoConsultora.COLIMN_NAME_FK_ID_CONSULTORA + " INTEGER, " +
                ContractSql.PrestamoConsultora.COLIMN_NAME_FK_ID_PRODUCTO + " INTEGER, " +
                ContractSql.PrestamoConsultora.FOREIGN_KEY_ID_CONSULTORA + " , " +
                ContractSql.PrestamoConsultora.FOREIGN_KEY_ID_PRODUCTO + " );"
        );

        db.execSQL("INSERT INTO " + ContractSql.Producto.TABLE_NAME + " ("+ ContractSql.Producto.COLUMN_NAME_NOMBRE + ", "+ ContractSql.Producto.COLUMN_NAME_DESCRIPCION  + ", "+ ContractSql.Producto.COLUMN_NAME_VENDIDOS + ") "
                + " VALUES ('Paso 1: Crema Protectora Restauradora para Manos Satin Hands®', '', 0);" );
        db.execSQL("INSERT INTO " + ContractSql.Producto.TABLE_NAME + " ("+ ContractSql.Producto.COLUMN_NAME_NOMBRE + ", "+ ContractSql.Producto.COLUMN_NAME_DESCRIPCION  + ", "+ ContractSql.Producto.COLUMN_NAME_VENDIDOS + ") "
                + " VALUES ('Paso 2: Crema Exfoliante Refinadora para Manos con Karité Satin Hands', '', 0);" );
        db.execSQL("INSERT INTO " + ContractSql.Producto.TABLE_NAME + " ("+ ContractSql.Producto.COLUMN_NAME_NOMBRE + ", "+ ContractSql.Producto.COLUMN_NAME_DESCRIPCION  + ", "+ ContractSql.Producto.COLUMN_NAME_VENDIDOS + ") "
                + " VALUES ('Paso 3: Crema para Manos con Karité Satin Hands', '', 0);" );
        db.execSQL("INSERT INTO " + ContractSql.Producto.TABLE_NAME + " ("+ ContractSql.Producto.COLUMN_NAME_NOMBRE + ", "+ ContractSql.Producto.COLUMN_NAME_DESCRIPCION  + ", "+ ContractSql.Producto.COLUMN_NAME_VENDIDOS + ") "
                + " VALUES ('Microexfoliante Refinador TimeWise', '', 0);" );
        db.execSQL("INSERT INTO " + ContractSql.Producto.TABLE_NAME + " ("+ ContractSql.Producto.COLUMN_NAME_NOMBRE + ", "+ ContractSql.Producto.COLUMN_NAME_DESCRIPCION  + ", "+ ContractSql.Producto.COLUMN_NAME_VENDIDOS + ") "
                + " VALUES ('Minimizador de Poros TimeWise', '', 0);" );



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
