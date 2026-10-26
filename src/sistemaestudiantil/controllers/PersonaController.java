package sistemaestudiantil.controllers;

import java.io.IOException;
import java.util.List;
import sistemaestudiantil.daos.PersonaDAO;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.services.PersonaService;

public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(String ruta) {
        PersonaDAO estudianteDAO = new PersonaDAO("estudiantes.txt");
        PersonaDAO profesorDAO = new PersonaDAO("profesores.txt");
        this.personaService = new PersonaService(
                ruta, estudianteDAO, profesorDAO
        );
    }

    public List<PersonaDTO> listarPersonas() {
        return personaService.listarPersonas();
    }

    public List<PersonaDTO> listarTodos() {
        return personaService.listarTodos();
    }

    public List<PersonaDTO> listarInactivos() {
        return personaService.listarInactivos();
    }

    public String agregarPersona(String dni, String nombre, String apellido)
            throws IOException {
        return personaService.agregarPersona(dni, nombre, apellido);
    }

    public PersonaDTO buscarPorId(String id) {
        return personaService.buscarPorId(id);
    }

    public PersonaDTO buscarPorIdIncluyendoInactivos(String id) {
        return personaService.buscarPorIdIncluyendoInactivos(id);
    }

    public PersonaDTO buscarPorDni(String dni) {
        return personaService.buscarPorDni(dni);
    }

    public void actualizarPersona(PersonaDTO persona) throws IOException {
        personaService.actualizarPersona(persona);
    }

    public void eliminarPersona(String id) throws IOException {
        personaService.eliminarPersona(id);
    }

    public void reactivarPersona(String id) throws IOException {
        personaService.reactivarPersona(id);
    }
}
