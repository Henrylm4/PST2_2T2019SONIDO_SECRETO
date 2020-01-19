package grupo2.app.proyectopst.models;

import java.util.Date; //Clase destinada para el registro de ingreso de los usuarios

public class Registro {
    private Persona p; //Atributos
    private Date fecha;
    private String id;

    public Registro(Persona p, Date fecha){ //Constructor de la clase
        this.p=p;
        this.fecha=fecha;
    }
    public Registro() {} //Getters and setters para modificar atributos

    public Persona getP() {
        return p;
    }

    public void setP(Persona p) {
        this.p = p;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "  Usuario:" +
                " " + p.getNombre() +" "+ p.getApellido()+
                ", Fecha:" + fecha.getDate()+"/"+"01"+"/"+"2020 "+ +fecha.getHours() +":" + fecha.getMinutes();
    }
}
