package sistemaestudiantil.views;

import sistemaestudiantil.utils.ConsolaUtils;

public class MenuPrincipalView {

    private final PersonaView estudiantes = new PersonaView("estudiantes.txt", "Estudiantes");
    private final PersonaView profesores = new PersonaView("profesores.txt", "Profesores");
    private final AulaView aulas = new AulaView();
    private final InscripcionView inscripciones = new InscripcionView();
    private final AsignacionView asignaciones = new AsignacionView();
    private final ConsultaView consultas = new ConsultaView();

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "SISTEMA DE GESTIÓN ACADÉMICA",
                    "1. Gestionar estudiantes",
                    "2. Gestionar profesores",
                    "3. Gestionar aulas",
                    "4. Gestionar inscripciones",
                    "5. Gestionar asignaciones",
                    "6. Consultas académicas",
                    "0. Salir"
            );

            opcion = ConsolaUtils.leerEntero("Seleccione una opción");

            switch (opcion) {
                case 1:
                    estudiantes.mostrarMenu();
                    break;
                case 2:
                    profesores.mostrarMenu();
                    break;
                case 3:
                    aulas.mostrarMenu();
                    break;
                case 4:
                    inscripciones.mostrarMenu();
                    break;
                case 5:
                    asignaciones.mostrarMenu();
                    break;
                case 6:
                    consultas.mostrarMenu();
                    break;
                case 0:
                    ConsolaUtils.imprimirMensaje("Saliendo del sistema...");
                    break;
                default:
                    ConsolaUtils.imprimirError("Opción inválida.");
            }

        } while (opcion != 0);
    }
}