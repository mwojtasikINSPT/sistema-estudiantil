package sistemaestudiantil.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.daos.AulaDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.models.Aula;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.exceptions.OperacionNoPermitidaException;
import sistemaestudiantil.utils.GeneradorCodigo;
import sistemaestudiantil.utils.Validaciones;

public class AulaService {

    private final AulaDAO aulaDAO;
    private final InscripcionDAO inscripcionDAO;
    private final AsignacionDAO asignacionDAO;

    public AulaService(AulaDAO aulaDAO, InscripcionDAO inscripcionDAO,
            AsignacionDAO asignacionDAO) {
        this.aulaDAO = aulaDAO;
        this.inscripcionDAO = inscripcionDAO;
        this.asignacionDAO = asignacionDAO;
    }

    public List<AulaDTO> listarAulas() {
        return aulaDAO.listar().stream()
                .filter(AulaDTO::isActivo)
                .collect(Collectors.toList());
    }

    public List<AulaDTO> listarTodos() {
        return aulaDAO.listar();
    }

    public List<AulaDTO> listarInactivos() {
        return aulaDAO.listar().stream()
                .filter(aula -> !aula.isActivo())
                .collect(Collectors.toList());
    }

    public String agregarAula(int capacidad) throws IOException {
        if (capacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero"
            );
        }

        List<String> ids = aulaDAO.listar().stream()
                .map(AulaDTO::getCodAula)
                .collect(Collectors.toList());

        String codigo = GeneradorCodigo.generarSiguienteId(ids, "AUL");

        Aula aula = new Aula(codigo, capacidad);

        AulaDTO nueva = new AulaDTO(
                aula.getCodAula(),
                aula.getCapacidad(),
                aula.isActivo()
        );

        aulaDAO.agregar(nueva);
        return codigo;
    }

    public AulaDTO buscarPorCodigo(String codigo) {
        codigo = Validaciones.validarCodigo(codigo, "AUL");
        return aulaDAO.buscarPorId(codigo);
    }

    public AulaDTO buscarPorCodigoIncluyendoInactivas(String codigo) {
        codigo = Validaciones.validarCodigo(codigo, "AUL");
        return aulaDAO.buscarPorIdIncluyendoInactivas(codigo);
    }

    public void actualizarCapacidad(String codigo, int nuevaCapacidad)
            throws IOException {

        codigo = Validaciones.validarCodigo(codigo, "AUL");

        if (aulaDAO.buscarPorId(codigo) == null) {
            throw new EntidadNoEncontradaException(
                    "El aula no existe o está inactiva"
            );
        }

        if (nuevaCapacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero"
            );
        }

        int inscriptos = inscripcionDAO.buscarPorAula(codigo).size();

        if (nuevaCapacidad < inscriptos) {
            throw new OperacionNoPermitidaException(
                    "La capacidad no puede ser menor que los "
                    + inscriptos + " estudiantes inscriptos"
            );
        }

        aulaDAO.actualizarCapacidad(codigo, nuevaCapacidad);
    }

    public void eliminarAula(String codigo) throws IOException {
        codigo = Validaciones.validarCodigo(codigo, "AUL");

        if (aulaDAO.buscarPorId(codigo) == null) {
            throw new EntidadNoEncontradaException(
                    "El aula no existe o está inactiva"
            );
        }

        if (!inscripcionDAO.buscarPorAula(codigo).isEmpty()) {
            inscripcionDAO.eliminar(codigo);
        }

        if (asignacionDAO.buscarPorAula(codigo) != null) {
            asignacionDAO.eliminar(codigo);
        }

        aulaDAO.eliminar(codigo);
    }

    public void reactivarAula(String codigo) throws IOException {
        codigo = Validaciones.validarCodigo(codigo, "AUL");
        aulaDAO.reactivar(codigo);
    }
}
