package chris.sesi.com.model;


public class ModeloTicket {
    private String id_producto;
    private String producto;
    private String cantidad;
    private String precioUnitario;
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
