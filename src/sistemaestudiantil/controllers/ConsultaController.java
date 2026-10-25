package sistemaestudiantil.controllers;

import java.util.List;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.services.ConsultaService;

public class ConsultaController {

    private final ConsultaService consultaService = new ConsultaService();

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