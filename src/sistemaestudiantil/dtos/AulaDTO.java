package sistemaestudiantil.dtos;

public class AulaDTO {

    private final String codAula;
    private final int capacidad;
    private final boolean activo;

    public AulaDTO(String codAula, int capacidad, boolean activo) {
        this.codAula = codAula;
        this.capacidad = capacidad;
        this.activo = activo;
    }

    public String getCodAula() {
        return codAula;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isActivo() {
        return activo;
    }
}