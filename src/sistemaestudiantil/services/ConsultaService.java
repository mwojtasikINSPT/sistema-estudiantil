package sistemaestudiantil.services;

import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.AsignacionDTO;
import sistemaestudiantil.dtos.InscripcionDTO;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.utils.Validaciones;

public class ConsultaService {

    private final PersonaDAO estudianteDAO;
    private final PersonaDAO profesorDAO;
    private final AulaDAO aulaDAO;
    private final InscripcionDAO inscripcionDAO;
    private final AsignacionDAO asignacionDAO;

    public ConsultaService() {
        estudianteDAO = new PersonaDAO("estudiantes.txt");
        profesorDAO = new PersonaDAO("profesores.txt");
        aulaDAO = new AulaDAO();
        inscripcionDAO = new InscripcionDAO();
        asignacionDAO = new AsignacionDAO();
    }

    public String consultarEstudianteConAula(String idEstudiante) {
        idEstudiante = Validaciones.validarCodigo(idEstudiante, "EST");

        PersonaDTO estudiante = estudianteDAO.buscarPorId(idEstudiante);

        if (estudiante == null) {
            throw new EntidadNoEncontradaException(
                    "El estudiante no existe o está inactivo"
            );
        }

        InscripcionDTO inscripcion =
                inscripcionDAO.buscarPorEstudiante(idEstudiante);

        if (inscripcion == null) {
            return estudiante.getNombre() + " " + estudiante.getApellido()
                    + " no tiene un aula asignada";
        }

        return estudiante.getNombre() + " " + estudiante.getApellido()
                + " - Aula: " + inscripcion.getCodAula();
    }

    public List<PersonaDTO> consultarEstudiantesPorProfesor(String idProfesor) {
        idProfesor = Validaciones.validarCodigo(idProfesor, "PRO");

        if (profesorDAO.buscarPorId(idProfesor) == null) {
            throw new EntidadNoEncontradaException(
                    "El profesor no existe o está inactivo"
            );
        }

        AsignacionDTO asignacion =
                asignacionDAO.buscarPorProfesor(idProfesor);

        if (asignacion == null) {
            throw new EntidadNoEncontradaException(
                    "El profesor no tiene un aula asignada"
            );
        }

        return inscripcionDAO.buscarPorAula(asignacion.getCodAula())
                .stream()
                .map(i -> estudianteDAO.buscarPorId(i.getIdEstudiante()))
                .filter(e -> e != null)
                .collect(Collectors.toList());
    }

    public String consultarProfesorPorEstudiante(String idEstudiante) {
        idEstudiante = Validaciones.validarCodigo(idEstudiante, "EST");

        PersonaDTO estudiante = estudianteDAO.buscarPorId(idEstudiante);

        if (estudiante == null) {
            throw new EntidadNoEncontradaException(
                    "El estudiante no existe o está inactivo"
            );
        }

        InscripcionDTO inscripcion =
                inscripcionDAO.buscarPorEstudiante(idEstudiante);

        if (inscripcion == null) {
            return "El estudiante no tiene un aula asignada";
        }

        AsignacionDTO asignacion =
                asignacionDAO.buscarPorAula(inscripcion.getCodAula());

        if (asignacion == null) {
            return "El aula del estudiante no tiene un profesor asignado";
        }

        PersonaDTO profesor =
                profesorDAO.buscarPorId(asignacion.getIdProfesor());

        if (profesor == null) {
            throw new EntidadNoEncontradaException(
                    "El profesor asignado no existe o está inactivo"
            );
        }

        return "Estudiante: " + estudiante.getNombre() + " "
                + estudiante.getApellido()
                + " - Profesor: " + profesor.getNombre() + " "
                + profesor.getApellido()
                + " - Aula: " + inscripcion.getCodAula();
    }
}