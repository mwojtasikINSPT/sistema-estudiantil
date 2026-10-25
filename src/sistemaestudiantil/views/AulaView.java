package sistemaestudiantil.views;

import java.io.IOException;
import sistemaestudiantil.controllers.AulaController;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.utils.ConsolaUtils;

public class AulaView {

    private final AulaController controller;

    public AulaView() {
        this.controller = new AulaController();
    }

    public void mostrarMenu() {
        int opcion;

        do {
            ConsolaUtils.imprimirMenu(
                    "GESTIÓN DE AULAS",
                    "1. Agregar",
                    "2. Listar",
                    "3. Buscar por código",
                    "4. Modificar capacidad",
                    "5. Eliminar",
                    "6. Reactivar",
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
                        buscarPorCodigo();
                        break;
                    case 4:
                        modificar();
                        break;
                    case 5:
                        eliminar();
                        break;
                    case 6:
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
        int capacidad = ConsolaUtils.leerEntero("Capacidad del aula");

        String codigo = controller.agregarAula(capacidad);

        ConsolaUtils.imprimirRegistroCreado("Aula", codigo);
    }

    private void listar() {
        var aulas = controller.listarAulas();

        if (aulas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay aulas activas.");
            return;
        }

        for (AulaDTO aula : aulas) {
            mostrarAula(aula);
        }
    }

    private void buscarPorCodigo() {
        String codigo = ConsolaUtils.leerTexto("Código del aula");

        mostrarAula(controller.buscarPorCodigo(codigo));
    }

    private void modificar() throws IOException {
        String codigo = ConsolaUtils.leerTexto("Código del aula");

        AulaDTO actual = controller.buscarPorCodigo(codigo);

        if (actual == null) {
            ConsolaUtils.imprimirError("Aula no encontrada.");
            return;
        }

        mostrarAula(actual);

        int nuevaCapacidad = ConsolaUtils.leerEntero("Nueva capacidad");

        if (ConsolaUtils.confirmar("¿Confirmar modificación?")) {
            controller.actualizarCapacidad(codigo, nuevaCapacidad);
            ConsolaUtils.imprimirMensaje("Capacidad actualizada correctamente.");
        }
    }

    private void eliminar() throws IOException {
        String codigo = ConsolaUtils.leerTexto("Código del aula");

        AulaDTO aula = controller.buscarPorCodigo(codigo);

        if (aula == null) {
            ConsolaUtils.imprimirError("Aula no encontrada.");
            return;
        }

        mostrarAula(aula);

        ConsolaUtils.imprimirMensaje(
                "Al eliminar el aula se cancelarán sus inscripciones "
                + "y la asignación del profesor."
        );

        if (ConsolaUtils.confirmar("¿Confirmar eliminación?")) {
            controller.eliminarAula(codigo);
            ConsolaUtils.imprimirMensaje("Aula eliminada correctamente.");
        }
    }

    private void reactivar() throws IOException {
        String codigo = ConsolaUtils.leerTexto("Código del aula a reactivar");

        if (ConsolaUtils.confirmar("¿Confirmar reactivación?")) {
            controller.reactivarAula(codigo);
            ConsolaUtils.imprimirMensaje("Aula reactivada correctamente.");
        }
    }

    private void mostrarAula(AulaDTO aula) {
        if (aula == null) {
            ConsolaUtils.imprimirMensaje("Aula no encontrada.");
            return;
        }

        ConsolaUtils.imprimirMensaje(
                "Código: " + aula.getCodAula()
                + " | Capacidad: " + aula.getCapacidad()
                + " | Estado: " + (aula.isActivo() ? "Activa" : "Inactiva")
        );
    }
}