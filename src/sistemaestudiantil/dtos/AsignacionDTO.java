package sistemaestudiantil.dtos;

public class AsignacionDTO {

    private final String id;
    private final String idProfesor;
    private final String codAula;
    private final boolean activo;

    public AsignacionDTO(String id, String idProfesor, String codAula, boolean activo) {
        this.id = id;
        this.idProfesor = idProfesor;
        this.codAula = codAula;
        this.activo = activo;
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
}