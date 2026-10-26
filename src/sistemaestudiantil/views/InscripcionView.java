package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.AulaController;
import sistemaestudiantil.controllers.InscripcionController;
import sistemaestudiantil.controllers.PersonaController;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.dtos.InscripcionDTO;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class InscripcionView {

    private final InscripcionController controller;
    private final PersonaController estudianteController;
    private final AulaController aulaController;

    public InscripcionView() {
        this.controller = new InscripcionController();
        this.estudianteController = new PersonaController("estudiantes.txt");
        this.aulaController = new AulaController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE INSCRIPCIONES",
                    "1. Inscribir estudiante en aula",
                    "2. Listar inscripciones activas",
                    "3. Listar inscripciones inactivas",
                    "4. Buscar inscripción por ID",
                    "5. Buscar inscripción por estudiante",
                    "6. Listar inscripciones de un aula",
                    "7. Cambiar estudiante de aula",
                    "8. Cancelar inscripción",
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
                        buscarPorEstudiante();
                        break;
                    case 6:
                        buscarPorAula();
                        break;
                    case 7:
                        cambiarAula();
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
        String idEstudiante = ConsolaUtils.leerTexto(
                "ID del estudiante (ejemplo: EST0001, o 0 para cancelar)"
        );

        if (idEstudiante.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        PersonaDTO estudiante = estudianteController.buscarPorId(idEstudiante);

        if (estudiante == null) {
            ConsolaUtils.imprimirError("Estudiante no encontrado.");
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
                "Estudiante: " + estudiante.getNombre() + " "
                + estudiante.getApellido() + " (" + estudiante.getId() + ")"
                + " | Aula: " + aula.getCodAula()
        );

        if (!ConsolaUtils.confirmar("¿Confirmar inscripción?")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        String id = controller.crearInscripcion(idEstudiante, codAula);

        ConsolaUtils.imprimirRegistroCreado("Inscripción", id);
        mostrarInscripcion(controller.buscarPorId(id));
    }

    private void listarActivas() {
        var inscripciones = controller.listarInscripciones();

        if (inscripciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay inscripciones activas.");
            return;
        }

        ConsolaUtils.imprimirTitulo("INSCRIPCIONES ACTIVAS");
        ConsolaUtils.imprimirMensaje(
                "Registros encontrados: " + inscripciones.size()
        );

        for (InscripcionDTO inscripcion : inscripciones) {
            mostrarInscripcion(inscripcion);
        }
    }

    private void listarInactivas() {
        var inscripciones = controller.listarInactivos();

        if (inscripciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay inscripciones inactivas.");
            return;
        }

        ConsolaUtils.imprimirTitulo("INSCRIPCIONES INACTIVAS");
        ConsolaUtils.imprimirMensaje(
                "Registros encontrados: " + inscripciones.size()
        );

        for (InscripcionDTO inscripcion : inscripciones) {
            mostrarInscripcion(inscripcion);
        }
    }

    private void buscarPorId() {
        String id = ConsolaUtils.leerTexto("ID de la inscripción (ejemplo: INS0001)");

        mostrarInscripcion(controller.buscarPorIdIncluyendoInactivas(id));
    }

    private void buscarPorEstudiante() {
        String idEstudiante = ConsolaUtils.leerTexto("ID del estudiante (ejemplo: EST0001)");

        mostrarInscripcion(controller.buscarPorEstudiante(idEstudiante));
    }

    private void buscarPorAula() {
        String codAula = ConsolaUtils.leerTexto("Código del aula (ejemplo: AUL0001)");

        var inscripciones = controller.buscarPorAula(codAula);

        if (inscripciones.isEmpty()) {
            ConsolaUtils.imprimirMensaje(
                    "El aula no tiene inscripciones activas."
            );
            return;
        }

        ConsolaUtils.imprimirTitulo("INSCRIPCIONES DEL AULA " + codAula);
        ConsolaUtils.imprimirMensaje(
                "Registros encontrados: " + inscripciones.size()
        );

        for (InscripcionDTO inscripcion : inscripciones) {
            mostrarInscripcion(inscripcion);
        }
    }

    private void cambiarAula() throws IOException {
        String idEstudiante = ConsolaUtils.leerTexto(
                "ID del estudiante (ejemplo: EST0001, o 0 para cancelar)"
        );

        if (idEstudiante.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        InscripcionDTO actual = controller.buscarPorEstudiante(idEstudiante);

        if (actual == null) {
            ConsolaUtils.imprimirError(
                    "El estudiante no tiene una inscripción activa."
            );
            return;
        }

        mostrarInscripcion(actual);

        String nuevaAula = ConsolaUtils.leerTexto(
                "Código del aula nueva (ejemplo: AUL0002, o 0 para cancelar)"
        );

        if (nuevaAula.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        if (ConsolaUtils.confirmar("¿Confirmar cambio de aula?")) {
            String nuevoId = controller.cambiarAula(idEstudiante, nuevaAula);

            ConsolaUtils.imprimirMensaje(
                    "Cambio realizado correctamente. Nueva inscripción: "
                    + nuevoId
            );
            mostrarInscripcion(controller.buscarPorId(nuevoId));
        }
    }

    private void eliminar() throws IOException {
        String id = ConsolaUtils.leerTexto(
                "ID de la inscripción (ejemplo: INS0001, o 0 para cancelar)"
        );

        if (id.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        InscripcionDTO inscripcion =
                controller.buscarPorIdIncluyendoInactivas(id);

        if (inscripcion == null) {
            ConsolaUtils.imprimirError("Inscripción no encontrada.");
            return;
        }

        if (!inscripcion.isActivo()) {
            mostrarInscripcion(inscripcion);
            ConsolaUtils.imprimirMensaje(
                    "La inscripción ya está cancelada."
            );
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

        PersonaDTO estudiante = estudianteController.listarTodos().stream()
                .filter(persona -> persona.getId().equals(
                inscripcion.getIdEstudiante()
        ))
                .findFirst()
                .orElse(null);

        String datosEstudiante = inscripcion.getIdEstudiante();

        if (estudiante != null) {
            datosEstudiante = estudiante.getNombre() + " "
                    + estudiante.getApellido() + " ("
                    + estudiante.getId() + ")";
        }

        ConsolaUtils.imprimirMensaje(
                "ID: " + inscripcion.getId()
                + " | Estudiante: " + datosEstudiante
                + " | Aula: " + inscripcion.getCodAula()
                + " | Estado: "
                + (inscripcion.isActivo() ? "Activa" : "Inactiva")
        );
    }
}
