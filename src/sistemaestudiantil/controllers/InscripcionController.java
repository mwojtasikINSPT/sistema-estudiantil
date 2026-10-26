package sistemaestudiantil.controllers;

import java.io.IOException;
import java.util.List;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.InscripcionDTO;
import sistemaestudiantil.services.InscripcionService;

public class InscripcionController {

    private final InscripcionService inscripcionService;

    public InscripcionController() {
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        PersonaDAO estudianteDAO = new PersonaDAO("estudiantes.txt");
        AulaDAO aulaDAO = new AulaDAO();
        this.inscripcionService = new InscripcionService(
                inscripcionDAO, estudianteDAO, aulaDAO
        );
    }

    public String crearInscripcion(String estudiante, String aula) throws IOException {
        return inscripcionService.crearInscripcion(estudiante, aula);
    }

    public List<InscripcionDTO> listarInscripciones() {
        return inscripcionService.listarInscripciones();
    }

    public List<InscripcionDTO> listarTodos() {
        return inscripcionService.listarTodos();
    }

    public List<InscripcionDTO> listarInactivos() {
        return inscripcionService.listarInactivos();
    }

    public InscripcionDTO buscarPorId(String id) {
        return inscripcionService.buscarPorId(id);
    }

    public InscripcionDTO buscarPorIdIncluyendoInactivas(String id) {
        return inscripcionService.buscarPorIdIncluyendoInactivas(id);
    }

    public InscripcionDTO buscarPorEstudiante(String id) {
        return inscripcionService.buscarPorEstudiante(id);
    }

    public List<InscripcionDTO> buscarPorAula(String codigo) {
        return inscripcionService.buscarPorAula(codigo);
    }

    public void eliminarInscripcion(String codigo) throws IOException {
        inscripcionService.eliminarInscripcion(codigo);
    }

    public String cambiarAula(String estudiante, String nuevaAula) throws IOException {
        return inscripcionService.cambiarAula(estudiante, nuevaAula);
    }
}
