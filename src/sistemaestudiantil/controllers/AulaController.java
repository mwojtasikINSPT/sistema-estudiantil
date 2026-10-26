package sistemaestudiantil.controllers;

import java.io.IOException;
import java.util.List;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.services.AulaService;

public class AulaController {

    private final AulaService aulaService;

    public AulaController() {
        AulaDAO aulaDAO = new AulaDAO();
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        this.aulaService = new AulaService(
                aulaDAO, inscripcionDAO, asignacionDAO
        );
    }

    public List<AulaDTO> listarAulas() {
        return aulaService.listarAulas();
    }

    public List<AulaDTO> listarTodos() {
        return aulaService.listarTodos();
    }

    public List<AulaDTO> listarInactivos() {
        return aulaService.listarInactivos();
    }

    public String agregarAula(int capacidad) throws IOException {
        return aulaService.agregarAula(capacidad);
    }

    public AulaDTO buscarPorCodigo(String codigo) {
        return aulaService.buscarPorCodigo(codigo);
    }

    public AulaDTO buscarPorCodigoIncluyendoInactivas(String codigo) {
        return aulaService.buscarPorCodigoIncluyendoInactivas(codigo);
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
