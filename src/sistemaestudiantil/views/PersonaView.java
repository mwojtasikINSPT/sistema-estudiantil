package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.PersonaController;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class PersonaView {

    private final PersonaController controller;
    private final String tipo;
    private final String ejemploId;

    public PersonaView(String ruta, String tipo) {
        this.controller = new PersonaController(ruta);
        this.tipo = tipo;
        this.ejemploId = ruta.equalsIgnoreCase("estudiantes.txt")
                ? "EST0001" : "PRO0001";
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE " + tipo.toUpperCase(),
                    "1. Agregar",
                    "2. Listar activos",
                    "3. Listar inactivos",
                    "4. Buscar por ID",
                    "5. Buscar por DNI",
                    "6. Modificar",
                    "7. Eliminar",
                    "8. Reactivar",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        agregar();
                        break;
                    case 2:
                        listarActivos();
                        break;
                    case 3:
                        listarInactivos();
                        break;
                    case 4:
                        buscarPorId();
                        break;
                    case 5:
                        buscarPorDni();
                        break;
                    case 6:
                        modificar();
                        break;
                    case 7:
                        eliminar();
                        break;
                    case 8:
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

        ConsolaUtils.imprimirMensaje(
                "DNI: " + dni + " | Nombre: " + nombre
                + " | Apellido: " + apellido
        );

        if (!ConsolaUtils.confirmar("¿Confirmar alta?")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        String id = controller.agregarPersona(dni, nombre, apellido);
        ConsolaUtils.imprimirRegistroCreado("Persona", id);
    }

    private void listarActivos() {
        var personas = controller.listarPersonas();

        if (personas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay registros activos.");
            return;
        }

        ConsolaUtils.imprimirTitulo(tipo.toUpperCase() + " ACTIVOS");
        ConsolaUtils.imprimirMensaje("Registros encontrados: " + personas.size());

        for (PersonaDTO persona : personas) {
            mostrarPersona(persona);
        }
    }

    private void listarInactivos() {
        var personas = controller.listarInactivos();

        if (personas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay registros inactivos.");
            return;
        }

        ConsolaUtils.imprimirTitulo(tipo.toUpperCase() + " INACTIVOS");
        ConsolaUtils.imprimirMensaje("Registros encontrados: " + personas.size());

        for (PersonaDTO persona : personas) {
            mostrarPersona(persona);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID (ejemplo: " + ejemploId + ")");
        mostrarPersona(controller.buscarPorIdIncluyendoInactivos(id));
    }

    private void buscarPorDni() {
        String dni = ConsolaUtils.leerTexto("DNI");
        mostrarPersona(controller.buscarPorDni(dni));
    }

    private void modificar() throws IOException {
        String id = ConsolaUtils.leerTexto(
                "ID de la persona (ejemplo: " + ejemploId
                + ", o 0 para cancelar)"
        );

        if (id.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

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
            mostrarPersona(controller.buscarPorId(id));
        }
    }

    private void eliminar() throws IOException {
        String id = ConsolaUtils.leerTexto(
                "ID de la persona (ejemplo: " + ejemploId
                + ", o 0 para cancelar)"
        );

        if (id.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        PersonaDTO persona = controller.buscarPorIdIncluyendoInactivos(id);

        if (persona == null) {
            ConsolaUtils.imprimirError("Registro no encontrado.");
            return;
        }

        if (!persona.isActivo()) {
            mostrarPersona(persona);
            ConsolaUtils.imprimirMensaje(
                    "La persona ya está inactiva."
            );
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
                + " | Estado: "
                + (persona.isActivo() ? "Activo" : "Inactivo")
        );
    }

    private void reactivar() throws IOException {
        String id = ConsolaUtils.leerTexto(
                "ID de la persona a reactivar (ejemplo: " + ejemploId
                + ", o 0 para cancelar)"
        );

        if (id.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        controller.reactivarPersona(id);

        ConsolaUtils.imprimirMensaje("Persona reactivada correctamente.");
        mostrarPersona(controller.buscarPorId(id));
    }
}
