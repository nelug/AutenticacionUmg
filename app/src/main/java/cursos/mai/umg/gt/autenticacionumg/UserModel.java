package cursos.mai.umg.gt.autenticacionumg;

public class UserModel {

    private String id;
    private String nombre;
    private String apellido;
    private String nombre_completo;
    private String red;

    public UserModel() {}

    public UserModel(String id, String nombre, String apellido, String nombre_completo, String red) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombre_completo = nombre_completo;
        this.red = red;
    }

    public String getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public String getApellido(){
        return apellido;
    }

    public String getNombreCompleto(){
        return nombre_completo;
    }

    public String getRed(){
        return red;
    }

}
