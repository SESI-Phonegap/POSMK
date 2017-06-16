package chris.sesi.com.model;

/**
 * Created by Chris on 02/01/2017.
 */

public class ModeloInventario {

   public String id;
   public String nombreProducto;
   public String cantidad;
   public String precio;

   public ModeloInventario(String id,String nombre, String cant, String precio){
        this.id = id;
        this.nombreProducto = nombre;
        this.cantidad = cant;
        this.precio = precio;

    }

    public String getId(){
        return id;
    }
    public String getNombreProducto(){
        return nombreProducto;
    }
    public String getCantidad(){
        return cantidad;
    }
    public String getPrecio(){
        return precio;
    }

}
