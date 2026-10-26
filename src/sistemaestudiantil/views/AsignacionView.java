package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.AsignacionController;
import sistemaestudiantil.controllers.AulaController;
import sistemaestudiantil.controllers.PersonaController;
import sistemaestudiantil.dtos.AsignacionDTO;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class AsignacionView {

    private final AsignacionController controller;
    private final PersonaController profesorController;
    private final AulaController aulaController;

    public AsignacionView() {
        this.controller = new AsignacionController();
        this.profesorController = new PersonaController("profesores.txt");
        this.aulaController = new AulaController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE ASIGNACIONES",
                    "1. Asignar profesor a aula",
                    "2. Listar asignaciones activas",
                    "3. Listar asignaciones inactivas",
                    "4. Buscar asignación por ID",
                    "5. Buscar asignación por profesor",
                    "6. Buscar asignación por aula",
                    "7. Reasignar profesor de un aula",
                    "8. Cancelar asignación",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        agregar();
                        break;
                    case 2:
                        listarActivas();
                        break;
                    case 3:
                        listarInactivas();
                        break;
                    case 4:
                        buscarPorId();
                        break;
                    case 5:
                        buscarPorProfesor();
                        break;
                    case 6:
                        buscarPorAula();
                        break;
                    case 7:
                        reasignar();
                        break;
                    case 8:
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
        String idProfesor = ConsolaUtils.leerTexto(
                "ID del profesor (ejemplo: PRO0001, o 0 para cancelar)"
        );

        if (idProfesor.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        PersonaDTO profesor = profesorController.buscarPorId(idProfesor);

        if (profesor == null) {
            ConsolaUtils.imprimirError("Profesor no encontrado.");
            return;
        }

        String codAula = ConsolaUtils.leerTexto(
                "Código del aula (ejemplo: AUL0001, o 0 para cancelar)"
        );

        if (codAula.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        AulaDTO aula = aulaController.buscarPorCodigo(codAula);

        if (aula == null) {
            ConsolaUtils.imprimirError("Aula no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "Profesor: " + profesor.getNombre() + " "
                + profesor.getApellido() + " (" + profesor.getId() + ")"
                + " | Aula: " + aula.getCodAula()
        );

        if (!ConsolaUtils.confirmar("¿Confirmar asignación?")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        String id = controller.crearAsignacion(idProfesor, codAula);

        ConsolaUtils.imprimirRegistroCreado("Asignación", id);
        mostrarAsignacion(controller.buscarAsignacionPorId(id));
    }

    private void listarActivas() {
        var asignaciones = controller.listarAsignaciones();

        if (asignaciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay asignaciones activas.");
            return;
        }

        ConsolaUtils.imprimirTitulo("ASIGNACIONES ACTIVAS");
        ConsolaUtils.imprimirMensaje(
                "Registros encontrados: " + asignaciones.size()
        );

        for (AsignacionDTO asignacion : asignaciones) {
            mostrarAsignacion(asignacion);
        }
    }

    private void listarInactivas() {
        var asignaciones = controller.listarInactivos();

        if (asignaciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay asignaciones inactivas.");
            return;
        }

        ConsolaUtils.imprimirTitulo("ASIGNACIONES INACTIVAS");
        ConsolaUtils.imprimirMensaje(
                "Registros encontrados: " + asignaciones.size()
        );

        for (AsignacionDTO asignacion : asignaciones) {
            mostrarAsignacion(asignacion);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID de la asignación (ejemplo: ASI0001)");

        mostrarAsignacion(controller.buscarPorIdIncluyendoInactivas(id));
    }

    private void buscarPorProfesor() {
        String idProfesor = ConsolaUtils.leerTexto("ID del profesor (ejemplo: PRO0001)");

        mostrarAsignacion(
                controller.buscarAsignacionPorProfesor(idProfesor)
        );
    }

    private void buscarPorAula() {
        String codAula = ConsolaUtils.leerTexto("Código del aula (ejemplo: AUL0001)");

        mostrarAsignacion(
                controller.buscarAsignacionPorAula(codAula)
        );
    }

    private void reasignar() throws IOException {
        String codAula = ConsolaUtils.leerTexto(
                "Código del aula (ejemplo: AUL0001, o 0 para cancelar)"
        );

        if (codAula.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

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
                ConsolaUtils.leerTexto(
                        "ID del nuevo profesor "
                        + "(ejemplo: PRO0002, o 0 para cancelar)"
                );

        if (nuevoProfesor.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        if (ConsolaUtils.confirmar("¿Confirmar reasignación?")) {
            String nuevoId =
                    controller.reasignarAula(codAula, nuevoProfesor);

            ConsolaUtils.imprimirMensaje(
                    "Reasignación realizada correctamente. "
                    + "Nueva asignación: " + nuevoId
            );
            mostrarAsignacion(controller.buscarAsignacionPorId(nuevoId));
        }
    }

    private void eliminar() throws IOException {
        String id = ConsolaUtils.leerTexto(
                "ID de la asignación (ejemplo: ASI0001, o 0 para cancelar)"
        );

        if (id.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        AsignacionDTO asignacion =
                controller.buscarPorIdIncluyendoInactivas(id);

        if (asignacion == null) {
            ConsolaUtils.imprimirError("Asignación no encontrada.");
            return;
        }

        if (!asignacion.isActivo()) {
            mostrarAsignacion(asignacion);
            ConsolaUtils.imprimirMensaje(
                    "La asignación ya está cancelada."
            );
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

        PersonaDTO profesor = profesorController.listarTodos().stream()
                .filter(persona -> persona.getId().equals(
                asignacion.getIdProfesor()
        ))
                .findFirst()
                .orElse(null);

        String datosProfesor = asignacion.getIdProfesor();

        if (profesor != null) {
            datosProfesor = profesor.getNombre() + " "
                    + profesor.getApellido() + " ("
                    + profesor.getId() + ")";
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + asignacion.getId()
                + " | Profesor: " + datosProfesor
                + " | Aula: " + asignacion.getCodAula()
                + " | Estado: "
                + (asignacion.isActivo() ? "Activa" : "Inactiva")
        );
    }
}
