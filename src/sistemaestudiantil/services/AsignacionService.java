package sistemaestudiantil.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.AsignacionDTO;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.exceptions.OperacionNoPermitidaException;
import sistemaestudiantil.models.Asignacion;
import sistemaestudiantil.utils.GeneradorCodigo;
import sistemaestudiantil.utils.Validaciones;

public class AsignacionService {

    private final AsignacionDAO asignacionDAO;
    private final AulaDAO aulaDAO;
    private final PersonaDAO profesorDAO;

    public AsignacionService(AsignacionDAO asignacionDAO, AulaDAO aulaDAO,
            PersonaDAO profesorDAO) {
        this.asignacionDAO = asignacionDAO;
        this.aulaDAO = aulaDAO;
        this.profesorDAO = profesorDAO;
    }

    private void validarProfesor(String idProfesor) {
        if (profesorDAO.buscarPorId(idProfesor) == null) {
            throw new EntidadNoEncontradaException(
                    "El profesor no existe o está inactivo"
            );
        }
    }

    private void validarAula(String codAula) {
        if (aulaDAO.buscarPorId(codAula) == null) {
            throw new EntidadNoEncontradaException(
                    "El aula no existe o está inactiva"
            );
        }
    }

    private String generarId(List<AsignacionDTO> asignaciones) {
        List<String> ids = asignaciones.stream()
                .map(AsignacionDTO::getId)
                .collect(Collectors.toList());

        return GeneradorCodigo.generarSiguienteId(ids, "ASI");
    }

    public String crearAsignacion(String idProfesor, String codAula)
            throws IOException {

        idProfesor = Validaciones.validarCodigo(idProfesor, "PRO");
        codAula = Validaciones.validarCodigo(codAula, "AUL");

        validarProfesor(idProfesor);
        validarAula(codAula);

        if (asignacionDAO.buscarPorProfesor(idProfesor) != null) {
            throw new OperacionNoPermitidaException(
                    "El profesor ya tiene un aula asignada"
            );
        }

        if (asignacionDAO.buscarPorAula(codAula) != null) {
            throw new OperacionNoPermitidaException(
                    "El aula ya tiene un profesor asignado"
            );
        }

        String id = generarId(asignacionDAO.listar());

        Asignacion asignacion = new Asignacion(
                id, idProfesor, codAula
        );

        AsignacionDTO nueva = new AsignacionDTO(
                asignacion.getId(),
                asignacion.getIdProfesor(),
                asignacion.getCodAula(),
                asignacion.isActivo()
        );

        asignacionDAO.agregar(nueva);

        return id;
    }

    public List<AsignacionDTO> listarAsignaciones() {
        return asignacionDAO.listar().stream()
                .filter(AsignacionDTO::isActivo)
                .collect(Collectors.toList());
    }

    public List<AsignacionDTO> listarTodos() {
        return asignacionDAO.listar();
    }

    public List<AsignacionDTO> listarInactivos() {
        return asignacionDAO.listar().stream()
                .filter(asignacion -> !asignacion.isActivo())
                .collect(Collectors.toList());
    }

    public AsignacionDTO buscarAsignacionPorId(String id) {
        id = Validaciones.validarCodigo(id, "ASI");
        return asignacionDAO.buscarPorId(id);
    }

    public AsignacionDTO buscarPorIdIncluyendoInactivas(String id) {
        id = Validaciones.validarCodigo(id, "ASI");
        return asignacionDAO.buscarPorIdIncluyendoInactivas(id);
    }

    public AsignacionDTO buscarAsignacionPorAula(String codAula) {
        codAula = Validaciones.validarCodigo(codAula, "AUL");
        return asignacionDAO.buscarPorAula(codAula);
    }

    public AsignacionDTO buscarAsignacionPorProfesor(String idProfesor) {
        idProfesor = Validaciones.validarCodigo(idProfesor, "PRO");
        return asignacionDAO.buscarPorProfesor(idProfesor);
    }

    public void eliminarAsignacion(String codigo) throws IOException {
        codigo = Validaciones.normalizarTexto(codigo);

        if (!Validaciones.esCodigoValido(codigo, "ASI")
                && !Validaciones.esCodigoValido(codigo, "PRO")
                && !Validaciones.esCodigoValido(codigo, "AUL")) {
            throw new IllegalArgumentException(
                    "El código debe comenzar con ASI, PRO o AUL"
            );
        }

        asignacionDAO.eliminar(codigo);
    }

    public String reasignarAula(String codAula, String idNuevoProfesor)
            throws IOException {

        codAula = Validaciones.validarCodigo(codAula, "AUL");
        idNuevoProfesor = Validaciones.validarCodigo(idNuevoProfesor, "PRO");

        validarAula(codAula);
        validarProfesor(idNuevoProfesor);

        AsignacionDTO anterior = asignacionDAO.buscarPorAula(codAula);

        if (anterior == null) {
            throw new EntidadNoEncontradaException(
                    "El aula no tiene un profesor asignado"
            );
        }

        if (anterior.getIdProfesor().equals(idNuevoProfesor)) {
            throw new OperacionNoPermitidaException(
                    "El aula ya está asignada a ese profesor"
            );
        }

        if (asignacionDAO.buscarPorProfesor(idNuevoProfesor) != null) {
            throw new OperacionNoPermitidaException(
                    "El nuevo profesor ya tiene un aula asignada"
            );
        }

        List<AsignacionDTO> asignaciones = asignacionDAO.listar();

        String nuevoId = generarId(asignaciones);

        List<AsignacionDTO> actualizadas = asignaciones.stream()
                .map(a -> a.getId().equals(anterior.getId())
                        ? new AsignacionDTO(
                                a.getId(),
                                a.getIdProfesor(),
                                a.getCodAula(),
                                false
                        )
                        : a)
                .collect(Collectors.toList());

        actualizadas.add(new AsignacionDTO(
                nuevoId,
                idNuevoProfesor,
                codAula,
                true
        ));

        asignacionDAO.guardarAll(actualizadas);

        return nuevoId;
    }
}
