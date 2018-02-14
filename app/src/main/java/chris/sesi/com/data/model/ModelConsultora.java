package chris.sesi.com.data.model;



public class ModelConsultora {
    private String id;
    private String nombre;
    private String foto;
    private String nivel;
    private String direccion;
    private String telefono;
    private int status_unidad;

    public ModelConsultora(String id, String nombre, String foto, String nivel, String direccion, String telefono, int status_unidad){
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.nivel = nivel;
        this.direccion = direccion;
        this.telefono = telefono;
        this.status_unidad = status_unidad;
    }

    public ModelConsultora(String id, String  nombre, String foto, String nivel, String telefono, int status_unidad){
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.nivel = nivel;
        this.telefono = telefono;
        this.status_unidad = status_unidad;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }

    public String getNivel() {
        return nivel;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getStatus_unidad() {
        return status_unidad;
    }
}
