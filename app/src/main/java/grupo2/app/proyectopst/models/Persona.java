package grupo2.app.proyectopst.models;
//Clase persona, destinada para poder guardar los datos de los usuarios en un objeto
public class Persona {
    private String id;
    private String Nombre; // Parámetros a ingresar
    private String Apellido;
    private String Clave;
    private String Cargo;

    public Persona(String Nombre, String Apellido, String Clave){  //Constructor opcional
        this.Nombre=Nombre;
        this.Apellido=Apellido;
        this.Clave=Clave;
    }
    public Persona(String Nombre, String Apellido, String Clave, String Cargo){ //Constructor completo
        this.Nombre=Nombre;
        this.Apellido=Apellido;
        this.Clave=Clave;
        this.Cargo=Cargo;
    }
    public Persona(){ //Constructor vacío por defecto

    }

    public String getNombre() {
        return Nombre;
    } //Getters and setters para modificar atributos

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
        return Nombre + " "+ Apellido;
    } //To string destinado para mostrar el solo el nombre y apellido
}
