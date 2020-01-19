package grupo2.app.proyectopst.models;

public class Persona {
    private String id;
    private String Nombre;
    private String Apellido;
    private String Clave;
    private String Cargo;

    public Persona(String Nombre, String Apellido, String Clave){
        this.Nombre=Nombre;
        this.Apellido=Apellido;
        this.Clave=Clave;
    }
    public Persona(String Nombre, String Apellido, String Clave, String Cargo){
        this.Nombre=Nombre;
        this.Apellido=Apellido;
        this.Clave=Clave;
        this.Cargo=Cargo;
    }
    public Persona(){

    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getClave() {
        return Clave;
    }

    public void setClave(String clave) {
        Clave = clave;
    }

    public String getCargo() {
        return Cargo;
    }

    public void setCargo(String cargo) {
        Cargo = cargo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Nombre;
    }
}
