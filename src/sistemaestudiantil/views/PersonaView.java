package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.PersonaController;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class PersonaView {

    private final PersonaController controller;
    private final String tipo;

    public PersonaView(String ruta, String tipo) {
        this.controller = new PersonaController(ruta);
        this.tipo = tipo;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE " + tipo.toUpperCase(),
                    "1. Agregar",
                    "2. Listar",
                    "3. Buscar por ID",
                    "4. Buscar por DNI",
                    "5. Modificar",
                    "6. Eliminar",
                    "7. Reactivar",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        agregar();
                        break;
                    case 2:
                        listar();
                        break;
                    case 3:
                        buscarPorId();
                        break;
                    case 4:
                        buscarPorDni();
                        break;
                    case 5:
                        modificar();
                        break;
                    case 6:
                        eliminar();
                        break;
                    case 7:
                        reactivar();
                        break;
                    case 0:
                        break;
                    default:
                        ConsolaUtils.imprimirError("Opción inválida.");
                }
            } catch (IOException | RuntimeException e) {
                ConsolaUtils.imprimirError(e.getMessage());
            }
            if (opcion != 0) {
                ConsolaUtils.pausar();
            }

        } while (opcion != 0);
    }

    private void agregar() throws IOException {
        String dni = ConsolaUtils.leerTexto("DNI");
        String nombre = ConsolaUtils.leerTexto("Nombre");
        String apellido = ConsolaUtils.leerTexto("Apellido");

        String id = controller.agregarPersona(dni, nombre, apellido);
        ConsolaUtils.imprimirRegistroCreado("Persona", id);
    }

    private void listar() {
        var personas = controller.listarPersonas();

        if (personas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay registros activos.");
            return;
        }

        for (PersonaDTO persona : personas) {
            mostrarPersona(persona);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID");
        mostrarPersona(controller.buscarPorId(id));
    }

    private void buscarPorDni() {
        String dni = ConsolaUtils.leerTexto("DNI");
        mostrarPersona(controller.buscarPorDni(dni));
    }

    private void modificar() throws IOException {
        String id = ConsolaUtils.leerTexto("ID de la persona");

        PersonaDTO actual = controller.buscarPorId(id);

        if (actual == null) {
            ConsolaUtils.imprimirError("Registro no encontrado.");
            return;
        }

        mostrarPersona(actual);

        String dni = ConsolaUtils.leerTexto("Nuevo DNI");
        String nombre = ConsolaUtils.leerTexto("Nuevo nombre");
        String apellido = ConsolaUtils.leerTexto("Nuevo apellido");

        if (dni.isEmpty()) {
            dni = actual.getDni();
        }

        if (nombre.isEmpty()) {
            nombre = actual.getNombre();
        }

        if (apellido.isEmpty()) {
            apellido = actual.getApellido();
        }

        PersonaDTO actualizado = new PersonaDTO(
                actual.getId(),
                dni,
                nombre,
                apellido,
                true
        );

        if (ConsolaUtils.confirmar("¿Confirmar modificación?")) {
            controller.actualizarPersona(actualizado);
            ConsolaUtils.imprimirMensaje("Registro actualizado correctamente.");
        }
    }

    private void eliminar() throws IOException {
        String id = ConsolaUtils.leerTexto("ID de la persona");

        PersonaDTO persona = controller.buscarPorId(id);

        if (persona == null) {
            ConsolaUtils.imprimirError("Registro no encontrado.");
            return;
        }

        mostrarPersona(persona);

        if (ConsolaUtils.confirmar("¿Confirmar eliminación?")) {
            controller.eliminarPersona(id);
            ConsolaUtils.imprimirMensaje("Registro eliminado correctamente.");
        }
    }

    private void mostrarPersona(PersonaDTO persona) {
        if (persona == null) {
            ConsolaUtils.imprimirMensaje("Registro no encontrado.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + persona.getId()
                + " | DNI: " + persona.getDni()
                + " | Nombre: " + persona.getNombre()
                + " | Apellido: " + persona.getApellido()
        );
    }

    private void reactivar() throws IOException {
        String id = ConsolaUtils.leerTexto("ID de la persona a reactivar");

        controller.reactivarPersona(id);

        ConsolaUtils.imprimirMensaje("Persona reactivada correctamente.");
    }
}
