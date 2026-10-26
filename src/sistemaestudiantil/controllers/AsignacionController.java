package sistemaestudiantil.controllers;

import java.io.IOException;
import java.util.List;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.AsignacionDTO;
import sistemaestudiantil.services.AsignacionService;

public class AsignacionController {

    private final AsignacionService asignacionService;

    public AsignacionController() {
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        AulaDAO aulaDAO = new AulaDAO();
        PersonaDAO profesorDAO = new PersonaDAO("profesores.txt");
        this.asignacionService = new AsignacionService(
                asignacionDAO, aulaDAO, profesorDAO
        );
    }

    public String crearAsignacion(String profesor, String aula) throws IOException {
        return asignacionService.crearAsignacion(profesor, aula);
    }

    public List<AsignacionDTO> listarAsignaciones() {
        return asignacionService.listarAsignaciones();
    }

    public List<AsignacionDTO> listarTodos() {
        return asignacionService.listarTodos();
    }

    public List<AsignacionDTO> listarInactivos() {
        return asignacionService.listarInactivos();
    }

    public AsignacionDTO buscarAsignacionPorId(String id) {
        return asignacionService.buscarAsignacionPorId(id);
    }

    public AsignacionDTO buscarPorIdIncluyendoInactivas(String id) {
        return asignacionService.buscarPorIdIncluyendoInactivas(id);
    }

    public AsignacionDTO buscarAsignacionPorAula(String aula) {
        return asignacionService.buscarAsignacionPorAula(aula);
    }

    public AsignacionDTO buscarAsignacionPorProfesor(String profesor) {
        return asignacionService.buscarAsignacionPorProfesor(profesor);
    }

    public void eliminarAsignacion(String codigo) throws IOException {
        asignacionService.eliminarAsignacion(codigo);
    }

    public String reasignarAula(String aula, String nuevoProfesor) throws IOException {
        return asignacionService.reasignarAula(aula, nuevoProfesor);
    }
}
