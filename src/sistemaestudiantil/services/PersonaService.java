package sistemaestudiantil.services;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.daos.InscripcionDAO;
import sistemaestudiantil.daos.AsignacionDAO;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.models.Persona;
import sistemaestudiantil.models.Estudiante;
import sistemaestudiantil.models.Profesor;
import sistemaestudiantil.utils.GeneradorCodigo;
import sistemaestudiantil.utils.Validaciones;
import sistemaestudiantil.exceptions.EntidadDuplicadaException;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.exceptions.OperacionNoPermitidaException;

public class PersonaService {

    private final PersonaDAO personaDAO;
    private final PersonaDAO otraPersonaDAO;
    private final String prefijo;

    public PersonaService(String ruta, PersonaDAO estudianteDAO,
            PersonaDAO profesorDAO) {
        String archivo = ruta.replace("\\", "/");
        archivo = archivo.substring(archivo.lastIndexOf("/") + 1);

        if (archivo.equalsIgnoreCase("estudiantes.txt")) {
            this.prefijo = "EST";
            this.personaDAO = estudianteDAO;
            this.otraPersonaDAO = profesorDAO;
        } else if (archivo.equalsIgnoreCase("profesores.txt")) {
            this.prefijo = "PRO";
            this.personaDAO = profesorDAO;
            this.otraPersonaDAO = estudianteDAO;
        } else {
            throw new IllegalArgumentException(
                    "Archivo de personas no válido"
            );
        }

    }

    private void validarDniUnicoGlobal(String dni) {
        boolean existeEnOtroArchivo = otraPersonaDAO.listar().stream()
                .anyMatch(persona -> persona.getDni().equals(dni));

        if (existeEnOtroArchivo) {
            throw new EntidadDuplicadaException(
                    "Ya existe un estudiante o profesor con ese DNI"
            );
        }
    }

    private Persona crearModelo(String id, String dni, String nombre,
            String apellido, boolean activo) {

        Persona persona;

        if (prefijo.equals("EST")) {
            persona = new Estudiante(id, dni, nombre, apellido);
        } else {
            persona = new Profesor(id, dni, nombre, apellido);
        }

        persona.setActivo(activo);
        return persona;
    }

    private PersonaDTO convertirADTO(Persona persona) {
        return new PersonaDTO(
                persona.getId(),
                persona.getDni(),
                persona.getNombre(),
                persona.getApellido(),
                persona.isActivo()
        );
    }

    public List<PersonaDTO> listarPersonas() {
        return personaDAO.listar().stream()
                .filter(PersonaDTO::isActivo)
                .collect(Collectors.toList());
    }

    public List<PersonaDTO> listarTodos() {
        return personaDAO.listar();
    }

    public List<PersonaDTO> listarInactivos() {
        return personaDAO.listar().stream()
                .filter(persona -> !persona.isActivo())
                .collect(Collectors.toList());
    }

    public String agregarPersona(String dni, String nombre, String apellido)
            throws IOException {

        dni = Validaciones.validarDni(dni);
        nombre = Validaciones.validarTextoObligatorio(nombre, "nombre");
        apellido = Validaciones.validarTextoObligatorio(apellido, "apellido");

        validarDniUnicoGlobal(dni);

        List<String> ids = personaDAO.listar().stream()
                .map(PersonaDTO::getId)
                .collect(Collectors.toList());

        String id = GeneradorCodigo.generarSiguienteId(ids, prefijo);

        Persona persona = crearModelo(id, dni, nombre, apellido, true);

        personaDAO.agregar(convertirADTO(persona));
        return id;
    }

    public PersonaDTO buscarPorId(String id) {
        return personaDAO.buscarPorId(
                Validaciones.validarCodigo(id, prefijo)
        );
    }

    public PersonaDTO buscarPorIdIncluyendoInactivos(String id) {
        return personaDAO.buscarPorIdIncluyendoInactivos(
                Validaciones.validarCodigo(id, prefijo)
        );
    }

    public PersonaDTO buscarPorDni(String dni) {
        return personaDAO.buscarPorDni(
                Validaciones.validarDni(dni)
        );
    }

    public void eliminarPersona(String id) throws IOException {
        id = Validaciones.validarCodigo(id, prefijo);

        PersonaDTO datos = personaDAO.buscarPorId(id);

        if (datos == null) {
            throw new EntidadNoEncontradaException(
                    "La persona no existe o está inactiva"
            );
        }

        Persona persona = crearModelo(
                datos.getId(),
                datos.getDni(),
                datos.getNombre(),
                datos.getApellido(),
                datos.isActivo()
        );

        switch (persona.getTipoPersona()) {
            case "ESTUDIANTE" -> {
                InscripcionDAO inscripcionDAO = new InscripcionDAO();

                if (inscripcionDAO.buscarPorEstudiante(id) != null) {
                    inscripcionDAO.eliminar(id);
                }
            }

            case "PROFESOR" -> {
                AsignacionDAO asignacionDAO = new AsignacionDAO();

                if (asignacionDAO.buscarPorProfesor(id) != null) {
                    throw new OperacionNoPermitidaException(
                            "No se puede eliminar el profesor porque tiene un aula asignada"
                    );
                }
            }

            default ->
                throw new OperacionNoPermitidaException(
                        "Tipo de persona no reconocido"
                );
        }

        personaDAO.eliminar(id);
    }

    public void actualizarPersona(PersonaDTO actualizada) throws IOException {
        String id = Validaciones.validarCodigo(
                actualizada.getId(), prefijo
        );

        String dni = Validaciones.validarDni(actualizada.getDni());

        String nombre = Validaciones.validarTextoObligatorio(
                actualizada.getNombre(), "nombre"
        );

        String apellido = Validaciones.validarTextoObligatorio(
                actualizada.getApellido(), "apellido"
        );

        validarDniUnicoGlobal(dni);

        Persona persona = crearModelo(
                id, dni, nombre, apellido, true
        );

        personaDAO.actualizar(convertirADTO(persona));
    }

    public void reactivarPersona(String id) throws IOException {
        id = Validaciones.validarCodigo(id, prefijo);
        personaDAO.reactivar(id);
    }
}
