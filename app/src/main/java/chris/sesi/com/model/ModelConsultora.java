package chris.sesi.com.model;



public class ModelConsultora {
    private String id;
    private String nombre;
    private String foto;
    private String nivel;
    private String direccion;
    private String telefono;

    public ModelConsultora(String id, String nombre, String foto, String nivel, String direccion, String telefono){
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.nivel = nivel;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public ModelConsultora(String id, String  nombre, String foto, String nivel, String telefono){
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
        this.nivel = nivel;
        this.telefono = telefono;
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
}
