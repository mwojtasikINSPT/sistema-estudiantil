package sistemaestudiantil.views;

import java.util.List;
import sistemaestudiantil.controllers.ConsultaController;
import sistemaestudiantil.dtos.PersonaDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class ConsultaView {

    private final ConsultaController controller;

    public ConsultaView() {
        this.controller = new ConsultaController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "CONSULTAS ACADÉMICAS",
                    "1. Consultar estudiante y aula",
                    "2. Consultar estudiantes de un profesor",
                    "3. Consultar profesor de un estudiante",
                    "0. Volver"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            try {
                switch (opcion) {
                    case 1:
                        consultarEstudianteConAula();
                        break;
                    case 2:
                        consultarEstudiantesPorProfesor();
                        break;
                    case 3:
                        consultarProfesorPorEstudiante();
                        break;
                    case 0:
                        break;
                    default:
                        ConsolaUtils.imprimirError("Opción inválida.");
                }
            } catch (RuntimeException e) {
                ConsolaUtils.imprimirError(e.getMessage());
            }

            if (opcion != 0) {
                ConsolaUtils.pausar();
            }

        } while (opcion != 0);
    }

    private void consultarEstudianteConAula() {
        String idEstudiante = ConsolaUtils.leerTexto("ID del estudiante");

        String resultado = controller.consultarEstudianteConAula(idEstudiante);

        ConsolaUtils.imprimirMensaje(resultado);
    }

    private void consultarEstudiantesPorProfesor() {
        String idProfesor = ConsolaUtils.leerTexto("ID del profesor");

        List<PersonaDTO> estudiantes =
                controller.consultarEstudiantesPorProfesor(idProfesor);

        if (estudiantes.isEmpty()) {
            ConsolaUtils.imprimirMensaje(
                    "El profesor no tiene estudiantes a cargo."
            );
            return;
        }

        ConsolaUtils.imprimirTitulo("ESTUDIANTES A CARGO");

        for (PersonaDTO estudiante : estudiantes) {
            ConsolaUtils.imprimirMensaje(
                    "ID: " + estudiante.getId()
                    + " | DNI: " + estudiante.getDni()
                    + " | Nombre: " + estudiante.getNombre()
                    + " | Apellido: " + estudiante.getApellido()
            );
        }
    }

    private void consultarProfesorPorEstudiante() {
        String idEstudiante = ConsolaUtils.leerTexto("ID del estudiante");

        String resultado =
                controller.consultarProfesorPorEstudiante(idEstudiante);

        ConsolaUtils.imprimirMensaje(resultado);
    }
}