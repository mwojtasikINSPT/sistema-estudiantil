package sistemaestudiantil.models;

public class Asignacion {

    private final String id;
    private final String idProfesor;
    private final String codAula;
    private boolean activo;

    public Asignacion(String id, String idProfesor, String codAula) {
        this.id = id;
        this.idProfesor = idProfesor;
        this.codAula = codAula;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public String getCodAula() {
        return codAula;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Asignacion{"
                + "id='" + id + '\''
                + ", idProfesor='" + idProfesor + '\''
                + ", codAula='" + codAula + '\''
                + ", activo=" + activo
                + '}';
    }
}