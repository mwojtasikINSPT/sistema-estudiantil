package sistemaestudiantil.models;

public class Aula {

    private final String codAula;
    private int capacidad;
    private boolean activo;

    public Aula(String codAula, int capacidad) {
        this.codAula = codAula;
        this.capacidad = capacidad;
        this.activo = true;
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

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Aula{"
                + "codAula='" + codAula + '\''
                + ", capacidad=" + capacidad
                + ", activo=" + activo
                + '}';
    }
}