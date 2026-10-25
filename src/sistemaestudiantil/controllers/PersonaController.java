package sistemaestudiantil.controllers;

import java.io.IOException;
import java.util.List;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.services.PersonaService;

public class PersonaController {

    private final PersonaService personaService;

    public PersonaController(String ruta) {
        this.personaService = new PersonaService(ruta);
    }

    public List<PersonaDTO> listarPersonas() {
        return personaService.listarPersonas();
    }

    public String agregarPersona(String dni, String nombre, String apellido)
            throws IOException {
        return personaService.agregarPersona(dni, nombre, apellido);
    }

    public PersonaDTO buscarPorId(String id) {
        return personaService.buscarPorId(id);
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
