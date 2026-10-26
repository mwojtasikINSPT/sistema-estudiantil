package sistemaestudiantil.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.dtos.InscripcionDTO;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.exceptions.OperacionNoPermitidaException;
import sistemaestudiantil.models.Inscripcion;
import sistemaestudiantil.utils.GeneradorCodigo;
import sistemaestudiantil.utils.Validaciones;

public class InscripcionService {

    private final InscripcionDAO inscripcionDAO;
    private final PersonaDAO estudianteDAO;
    private final AulaDAO aulaDAO;

    public InscripcionService(InscripcionDAO inscripcionDAO,
            PersonaDAO estudianteDAO, AulaDAO aulaDAO) {
        this.inscripcionDAO = inscripcionDAO;
        this.estudianteDAO = estudianteDAO;
        this.aulaDAO = aulaDAO;
    }

    private void validarEstudiante(String idEstudiante) {
        if (estudianteDAO.buscarPorId(idEstudiante) == null) {
            throw new EntidadNoEncontradaException(
                    "El estudiante no existe o está inactivo"
            );
        }
    }

    private void validarDisponibilidad(String codAula) {
        AulaDTO aula = aulaDAO.buscarPorId(codAula);

        if (aula == null) {
            throw new EntidadNoEncontradaException(
                    "El aula no existe o está inactiva"
            );
        }

        int cantidadInscriptos = inscripcionDAO.buscarPorAula(codAula).size();

        if (cantidadInscriptos >= aula.getCapacidad()) {
            throw new OperacionNoPermitidaException(
                    "El aula alcanzó su capacidad máxima de "
                    + aula.getCapacidad() + " estudiantes"
            );
        }
    }

    private String generarId(List<InscripcionDTO> inscripciones) {
        List<String> ids = inscripciones.stream()
                .map(InscripcionDTO::getId)
                .collect(Collectors.toList());

        return GeneradorCodigo.generarSiguienteId(ids, "INS");
    }

    public String crearInscripcion(String idEstudiante, String codAula)
            throws IOException {

        idEstudiante = Validaciones.validarCodigo(idEstudiante, "EST");
        codAula = Validaciones.validarCodigo(codAula, "AUL");

        validarEstudiante(idEstudiante);

        if (inscripcionDAO.buscarPorEstudiante(idEstudiante) != null) {
            throw new OperacionNoPermitidaException(
                    "El estudiante ya está inscripto en un aula"
            );
        }

        validarDisponibilidad(codAula);

        String id = generarId(inscripcionDAO.listar());

        Inscripcion inscripcion = new Inscripcion(
                id, idEstudiante, codAula
        );

        InscripcionDTO nueva = new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getIdEstudiante(),
                inscripcion.getCodAula(),
                inscripcion.isActivo()
        );

        inscripcionDAO.agregar(nueva);

        return id;
    }

    public List<InscripcionDTO> listarInscripciones() {
        return inscripcionDAO.listar().stream()
                .filter(InscripcionDTO::isActivo)
                .collect(Collectors.toList());
    }

    public List<InscripcionDTO> listarTodos() {
        return inscripcionDAO.listar();
    }

    public List<InscripcionDTO> listarInactivos() {
        return inscripcionDAO.listar().stream()
                .filter(inscripcion -> !inscripcion.isActivo())
                .collect(Collectors.toList());
    }

    public InscripcionDTO buscarPorId(String id) {
        id = Validaciones.validarCodigo(id, "INS");
        return inscripcionDAO.buscarPorId(id);
    }

    public InscripcionDTO buscarPorIdIncluyendoInactivas(String id) {
        id = Validaciones.validarCodigo(id, "INS");
        return inscripcionDAO.buscarPorIdIncluyendoInactivas(id);
    }

    public InscripcionDTO buscarPorEstudiante(String idEstudiante) {
        idEstudiante = Validaciones.validarCodigo(idEstudiante, "EST");
        return inscripcionDAO.buscarPorEstudiante(idEstudiante);
    }

    public List<InscripcionDTO> buscarPorAula(String codAula) {
        codAula = Validaciones.validarCodigo(codAula, "AUL");
        return inscripcionDAO.buscarPorAula(codAula);
    }

    public void eliminarInscripcion(String codigo) throws IOException {
        codigo = Validaciones.normalizarTexto(codigo);

        if (!Validaciones.esCodigoValido(codigo, "INS")
                && !Validaciones.esCodigoValido(codigo, "EST")
                && !Validaciones.esCodigoValido(codigo, "AUL")) {
            throw new IllegalArgumentException(
                    "El código debe comenzar con INS, EST o AUL"
            );
        }

        inscripcionDAO.eliminar(codigo);
    }

    public String cambiarAula(String idEstudiante, String nuevoCodAula)
            throws IOException {

        idEstudiante = Validaciones.validarCodigo(idEstudiante, "EST");
        nuevoCodAula = Validaciones.validarCodigo(nuevoCodAula, "AUL");

        validarEstudiante(idEstudiante);

        InscripcionDTO anterior =
                inscripcionDAO.buscarPorEstudiante(idEstudiante);

        if (anterior == null) {
            throw new EntidadNoEncontradaException(
                    "El estudiante no tiene una inscripción activa"
            );
        }

        if (anterior.getCodAula().equals(nuevoCodAula)) {
            throw new OperacionNoPermitidaException(
                    "El estudiante ya está inscripto en esa aula"
            );
        }

        validarDisponibilidad(nuevoCodAula);

        List<InscripcionDTO> inscripciones = inscripcionDAO.listar();

        String nuevoId = generarId(inscripciones);

        List<InscripcionDTO> actualizadas = inscripciones.stream()
                .map(i -> i.getId().equals(anterior.getId())
                        ? new InscripcionDTO(
                                i.getId(),
                                i.getIdEstudiante(),
                                i.getCodAula(),
                                false
                        )
                        : i)
                .collect(Collectors.toList());

        actualizadas.add(new InscripcionDTO(
                nuevoId,
                idEstudiante,
                nuevoCodAula,
                true
        ));

        inscripcionDAO.guardarAll(actualizadas);

        return nuevoId;
    }
}
