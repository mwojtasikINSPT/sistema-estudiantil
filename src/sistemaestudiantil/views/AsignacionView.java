package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.AsignacionController;
import sistemaestudiantil.dtos.AsignacionDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class AsignacionView {

    private final AsignacionController controller;

    public AsignacionView() {
        this.controller = new AsignacionController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE ASIGNACIONES",
                    "1. Asignar profesor a aula",
                    "2. Listar asignaciones activas",
                    "3. Buscar asignación por ID",
                    "4. Buscar asignación por profesor",
                    "5. Buscar asignación por aula",
                    "6. Reasignar profesor de un aula",
                    "7. Cancelar asignación",
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
                        buscarPorProfesor();
                        break;
                    case 5:
                        buscarPorAula();
                        break;
                    case 6:
                        reasignar();
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
        String idProfesor = ConsolaUtils.leerTexto("ID del profesor");
        String codAula = ConsolaUtils.leerTexto("Código del aula");

        String id = controller.crearAsignacion(idProfesor, codAula);

        ConsolaUtils.imprimirRegistroCreado("Asignación", id);
    }

    private void listar() {
        var asignaciones = controller.listarAsignaciones();

        if (asignaciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay asignaciones activas.");
            return;
        }

        for (AsignacionDTO asignacion : asignaciones) {
            mostrarAsignacion(asignacion);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID de la asignación");

        mostrarAsignacion(controller.buscarAsignacionPorId(id));
    }

    private void buscarPorProfesor() {
        String idProfesor = ConsolaUtils.leerTexto("ID del profesor");

        mostrarAsignacion(
                controller.buscarAsignacionPorProfesor(idProfesor)
        );
    }

    private void buscarPorAula() {
        String codAula = ConsolaUtils.leerTexto("Código del aula");

        mostrarAsignacion(
                controller.buscarAsignacionPorAula(codAula)
        );
    }

    private void reasignar() throws IOException {
        String codAula = ConsolaUtils.leerTexto("Código del aula");

        AsignacionDTO actual =
                controller.buscarAsignacionPorAula(codAula);

        if (actual == null) {
            ConsolaUtils.imprimirError(
                    "El aula no tiene un profesor asignado."
            );
            return;
        }

        mostrarAsignacion(actual);

        String nuevoProfesor =
                ConsolaUtils.leerTexto("ID del nuevo profesor");

        if (ConsolaUtils.confirmar("¿Confirmar reasignación?")) {
            String nuevoId =
                    controller.reasignarAula(codAula, nuevoProfesor);

            ConsolaUtils.imprimirMensaje(
                    "Reasignación realizada correctamente. "
                    + "Nueva asignación: " + nuevoId
            );
        }
    }

    private void eliminar() throws IOException {
        String id = ConsolaUtils.leerTexto("ID de la asignación");

        AsignacionDTO asignacion =
                controller.buscarAsignacionPorId(id);

        if (asignacion == null) {
            ConsolaUtils.imprimirError("Asignación no encontrada.");
            return;
        }

        mostrarAsignacion(asignacion);

        if (ConsolaUtils.confirmar("¿Confirmar cancelación?")) {
            controller.eliminarAsignacion(id);

            ConsolaUtils.imprimirMensaje(
                    "Asignación cancelada correctamente."
            );
        }
    }

    private void mostrarAsignacion(AsignacionDTO asignacion) {
        if (asignacion == null) {
            ConsolaUtils.imprimirMensaje("Asignación no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + asignacion.getId()
                + " | Profesor: " + asignacion.getIdProfesor()
                + " | Aula: " + asignacion.getCodAula()
                + " | Estado: "
                + (asignacion.isActivo() ? "Activa" : "Inactiva")
        );
    }
}