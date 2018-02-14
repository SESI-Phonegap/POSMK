package chris.sesi.com.data.model;


public class ModeloInventario {

   private String id;
   private String nombreProducto;
   private String cantidad;
   private String precio;

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
