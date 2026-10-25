package sistemaestudiantil.dtos;

public class InscripcionDTO {

    private final String id;
    private final String idEstudiante;
    private final String codAula;
    private final boolean activo;

    public InscripcionDTO(String id, String idEstudiante, String codAula, boolean activo) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.codAula = codAula;
        this.activo = activo;
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
}