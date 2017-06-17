package chris.sesi.com.database;

public final class ContractSql {

    private ContractSql(){}

    public static class User{
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_PK_ID = "id_mail_usuario";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_NIVELMK = "nivelmk";
        public static final String COLUMN_NAME_PASS_USER = "pass_user";
        public static final String COLUMN_NAME_USER_MK = "user_mk";
        public static final String COLUMN_NAME_PASS_MK = "pass_mk";

    }

    public static class Clientes{
        public static final String TABLE_NAME = "clientes";
        public static final String COLUMN_NAME_PK_ID = "id_cliente";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_DIRECCION = "direccion";
        public static final String COLUMN_NAME_TELEFONO = "telefono";
        public static final String COLUMN_NAME_OCUPACION = "ocupacion";
    }

    public static class Venta{
        public static final String TABLE_NAME = "venta";
        public static final String COLUMN_NAME_PK_ID = "id_venta";
        public static final String COLUMN_NAME_FK_ID_CLIENTE = "id_cliente";
        public static final String COLUMN_NAME_FECHA = "fecha";
        public static final String COLUMN_NAME_TOTAL = "total";
        public static final String COLUMN_NAME_ESTATUS = "estatus";
        public static final String FOREIGN_KEY_ID_CLIENTE = "FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)";
    }

    public static class Producto{
        public static final String TABLE_NAME = "producto";
        public static final String COLUMN_NAME_PK_ID = "id_producto";
        public static final String COLUMN_NAME_NOMBRE = "nombre";
        public static final String COLUMN_NAME_DESCRIPCION = "descripcion";
        public static final String COLUMN_NAME_VENDIDOS = "vendidos";
    }

    public static class Ticket{
        public static final String TABLE_NAME = "ticket";
        public static final String COLUMN_NAME_FK_ID_PRODUCTO = "id_producto";
        public static final String COLUMN_NAME_FK_ID_VENTA = "id_venta";
        public static final String COLUMN_NAME_CANTIDAD = "cantidad";
        public static final String COLUMN_NAME_PRECIO = "precio";
        public static final String COLUMN_NAME_IMPORTE = "importe";
        public static final String FOREIGN_KEY_ID_PRODUCTO = "FOREIGN KEY (id_producto) REFERENCES producto(id_producto)";
        public static final String FOREIGN_KEY_ID_VENTA = "FOREIGN KEY (id_venta) REFERENCES venta(id_venta)";
    }

    public static class Inventario{
        public static final String TABLE_NAME = "inventario";
        public static final String COLUMN_NAME_FK_ID_PRODUCTO = "id_producto";
        public static final String COLUMN_NAME_PRECIO_COMPRA = "precio_compra";
        public static final String COLUMN_NAME_PRECIO_VENTA = "precio_venta";
        public static final String COLUMN_NAME_STOCK = "stock";
        public static final String COLUMN_NAME_MIN_STOCK = "min_stock";
        public static final String FOREIGN_KEY_ID_PRODUCTO = "FOREIGN KEY (id_producto) REFERENCES producto(id_producto)";
    }

    public static class Abono{
        public static final String TABLE_NAME = "abono";
        public static final String COLIMN_NAME_PK_ID = "id_abono";
        public static final String COLUMN_NAME_FK_ID_VENTA = "id_venta";
        public static final String COLUMN_NAME_MONTO = "monto";
        public static final String COLUMN_NAME_SALDO_ACTUAL = "saldo_actual";
        public static final String COLUMN_NAME_FECHA = "fecha";
        public static final String FOREIGN_KEY_ID_VENTA = "FOREIGN KEY (id_venta) REFERENCES venta(id_venta)";
    }

   public static class Consultoras{
       public static final String TABLE_NAME = "consultoras";
       public static final String COLIMN_NAME_PK_ID = "id_consultora";
       public static final String COLIMN_NAME_NOMBRE = "nombre";
       public static final String COLIMN_NAME_NIVEL = "nivel";
       public static final String COLIMN_NAME_TELEFONO = "telefono";
       public static final String COLIMN_NAME_DIRECCION = "direccion";
   }

    public static class Unidad{
        public static final String TABLE_NAME = "unidad";
        public static final String COLIMN_NAME_UNIDAD_MK = "unidad";
        public static final String COLIMN_NAME_FK_ID_CONSULTORA = "id_consultora";
        public static final String FOREIGN_KEY_ID_CONSULTORA = "FOREIGN KEY (id_consultora) REFERENCES consultoras(id_consultora)";
    }

    public static class PrestamoConsultora{
        public static final String TABLE_NAME = "prestamoconsultora";
        public static final String COLIMN_NAME_PK_ID = "id_prestamo";
        public static final String COLIMN_NAME_FK_ID_CONSULTORA = "id_consultora";
        public static final String COLIMN_NAME_FK_ID_PRODUCTO = "id_producto";
        public static final String COLIMN_NAME_CANTIDAD = "cantidad";
        public static final String COLIMN_NAME_FECHA = "fecha";
        public static final String COLIMN_NAME_ESTATUS = "estatus";
        public static final String FOREIGN_KEY_ID_CONSULTORA = "FOREIGN KEY (id_consultora) REFERENCES consultoras(id_consultora)";
        public static final String FOREIGN_KEY_ID_PRODUCTO = "FOREIGN KEY (id_producto) REFERENCES producto(id_producto)";

    }


}
