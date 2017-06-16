package chris.sesi.com.model;

/**
 * Created by Chris on 05/01/2017.
 */

public class ModeloTicket {
    public String id_producto;
    public String producto;
    public String cantidad;
    public String precioUnitario;
    public String importe;

    public ModeloTicket(String id_producto,String producto,String cantidad, String precioUnitario, String importe){
        this.id_producto = id_producto;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.importe = importe;
    }

    public String getId_producto(){
        return id_producto;
    }
    public String getProducto(){
        return producto;
    }
    public String getCantidad(){
        return cantidad;
    }
    public String getPrecioUnitario(){
        return precioUnitario;
    }
    public String getImporte(){
        return importe;
    }
}
