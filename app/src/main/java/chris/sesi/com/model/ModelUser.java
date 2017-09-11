package chris.sesi.com.model;

public class ModelUser {
    private String idEmail;
    private String nombre;
    private String nivelMk;
    private String passUser;
    private String userMk;
    private String passMk;
    private String srcPhoto;

    public ModelUser(){

    }
    public ModelUser(String idEmail, String nombre, String nivelMk, String passUser, String userMk, String passMk, String srcPhoto){
        this.idEmail = idEmail;
        this.nombre = nombre;
        this.nivelMk = nivelMk;
        this.passUser = passUser;
        this.userMk = userMk;
        this.passMk = passMk;
        this.srcPhoto = srcPhoto;
    }

    public String getIdEmail() {
        return idEmail;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNivelMk() {
        return nivelMk;
    }

    public String getPassUser() {
        return passUser;
    }

    public String getUserMk() {
        return userMk;
    }

    public String getPassMk() {
        return passMk;
    }

    public String getSrcPhoto() {
        return srcPhoto;
    }
}
