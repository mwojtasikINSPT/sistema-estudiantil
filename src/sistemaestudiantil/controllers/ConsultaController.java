package sistemaestudiantil.controllers;

import java.util.List;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.services.ConsultaService;

public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController() {
        PersonaDAO estudianteDAO = new PersonaDAO("estudiantes.txt");
        PersonaDAO profesorDAO = new PersonaDAO("profesores.txt");
        InscripcionDAO inscripcionDAO = new InscripcionDAO();
        AsignacionDAO asignacionDAO = new AsignacionDAO();
        this.consultaService = new ConsultaService(
                estudianteDAO, profesorDAO, inscripcionDAO, asignacionDAO
        );
    }

    public String consultarEstudianteConAula(String estudiante) {
        return consultaService.consultarEstudianteConAula(estudiante);
    }

    public List<PersonaDTO> consultarEstudiantesPorProfesor(String profesor) {
        return consultaService.consultarEstudiantesPorProfesor(profesor);
    }

    public String consultarProfesorPorEstudiante(String estudiante) {
        return consultaService.consultarProfesorPorEstudiante(estudiante);
    }
}
