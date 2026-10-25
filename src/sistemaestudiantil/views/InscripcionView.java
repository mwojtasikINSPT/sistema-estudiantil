package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.InscripcionController;
import sistemaestudiantil.dtos.InscripcionDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class InscripcionView {

    private final InscripcionController controller;

    public InscripcionView() {
        this.controller = new InscripcionController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE INSCRIPCIONES",
                    "1. Inscribir estudiante en aula",
                    "2. Listar inscripciones activas",
                    "3. Buscar inscripción por ID",
                    "4. Buscar inscripción por estudiante",
                    "5. Listar inscripciones de un aula",
                    "6. Cambiar estudiante de aula",
                    "7. Cancelar inscripción",
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
                        buscarPorEstudiante();
                        break;
                    case 5:
                        buscarPorAula();
                        break;
                    case 6:
                        cambiarAula();
                        break;
                    case 7:
                        eliminar();
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
        String idEstudiante = ConsolaUtils.leerTexto("ID del estudiante");
        String codAula = ConsolaUtils.leerTexto("Código del aula");

        String id = controller.crearInscripcion(idEstudiante, codAula);

        ConsolaUtils.imprimirRegistroCreado("Inscripción", id);
    }

    private void listar() {
        var inscripciones = controller.listarInscripciones();

        if (inscripciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay inscripciones activas.");
            return;
        }

        for (InscripcionDTO inscripcion : inscripciones) {
            mostrarInscripcion(inscripcion);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID de la inscripción");

        mostrarInscripcion(controller.buscarPorId(id));
    }

    private void buscarPorEstudiante() {
        String idEstudiante = ConsolaUtils.leerTexto("ID del estudiante");

        mostrarInscripcion(controller.buscarPorEstudiante(idEstudiante));
    }

    private void buscarPorAula() {
        String codAula = ConsolaUtils.leerTexto("Código del aula");

        var inscripciones = controller.buscarPorAula(codAula);

        if (inscripciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje(
                    "El aula no tiene inscripciones activas."
            );
            return;
        }

        for (InscripcionDTO inscripcion : inscripciones) {
            mostrarInscripcion(inscripcion);
        }
    }

    private void cambiarAula() throws IOException {
        String idEstudiante = ConsolaUtils.leerTexto("ID del estudiante");

        InscripcionDTO actual = controller.buscarPorEstudiante(idEstudiante);

        if (actual == null) {
            ConsolaUtils.imprimirError(
                    "El estudiante no tiene una inscripción activa."
            );
            return;
        }

        mostrarInscripcion(actual);

        String nuevaAula = ConsolaUtils.leerTexto("Código del aula nueva");

        if (ConsolaUtils.confirmar("¿Confirmar cambio de aula?")) {
            String nuevoId = controller.cambiarAula(idEstudiante, nuevaAula);

            ConsolaUtils.imprimirMensaje(
                    "Cambio realizado correctamente. Nueva inscripción: "
                    + nuevoId
            );
        }
    }

    private void eliminar() throws IOException {
        String id = ConsolaUtils.leerTexto("ID de la inscripción");

        InscripcionDTO inscripcion = controller.buscarPorId(id);

        if (inscripcion == null) {
            ConsolaUtils.imprimirError("Inscripción no encontrada.");
            return;
        }

        mostrarInscripcion(inscripcion);

        if (ConsolaUtils.confirmar("¿Confirmar cancelación?")) {
            controller.eliminarInscripcion(id);

            ConsolaUtils.imprimirMensaje(
                    "Inscripción cancelada correctamente."
            );
        }
    }

    private void mostrarInscripcion(InscripcionDTO inscripcion) {
        if (inscripcion == null) {
            ConsolaUtils.imprimirMensaje("Inscripción no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + inscripcion.getId()
                + " | Estudiante: " + inscripcion.getIdEstudiante()
                + " | Aula: " + inscripcion.getCodAula()
                + " | Estado: "
                + (inscripcion.isActivo() ? "Activa" : "Inactiva")
        );
    }
}
