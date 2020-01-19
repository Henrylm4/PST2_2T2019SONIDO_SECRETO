package grupo2.app.proyectopst.models;

import java.util.Date;

public class Registro {
    private Persona p;
    private Date fecha;
    private String id;

    public Registro(Persona p, Date fecha){
        this.p=p;
        this.fecha=fecha;
    }

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
        return "Registro" +
                "= " + p.getNombre() +
                ", fecha=" + fecha.getTime();
    }
}
