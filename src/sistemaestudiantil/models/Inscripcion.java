package sistemaestudiantil.models;

public class Inscripcion {

    private final String id;
    private final String idEstudiante;
    private final String codAula;
    private boolean activo;

    public Inscripcion(String id, String idEstudiante, String codAula) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.codAula = codAula;
        this.activo = true;
    }

    public String getId() {
        return id;
    }

    public String getIdEstudiante() {
        return idEstudiante;
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
        return "Inscripcion{"
                + "id='" + id + '\''
                + ", idEstudiante='" + idEstudiante + '\''
                + ", codAula='" + codAula + '\''
                + ", activo=" + activo
                + '}';
    }
}