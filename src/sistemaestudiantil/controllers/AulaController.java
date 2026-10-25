package sistemaestudiantil.controllers;

import java.io.IOException;
import java.util.List;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.services.AulaService;

public class AulaController {

    private final AulaService aulaService = new AulaService();

    public List<AulaDTO> listarAulas() {
        return aulaService.listarAulas();
    }

    public String agregarAula(int capacidad) throws IOException {
        return aulaService.agregarAula(capacidad);
    }

    public AulaDTO buscarPorCodigo(String codigo) {
        return aulaService.buscarPorCodigo(codigo);
    }

    public void actualizarCapacidad(String codigo, int capacidad) throws IOException {
        aulaService.actualizarCapacidad(codigo, capacidad);
    }

    public void eliminarAula(String codigo) throws IOException {
        aulaService.eliminarAula(codigo);
    }

    public void reactivarAula(String codigo) throws IOException {
        aulaService.reactivarAula(codigo);
    }
}
