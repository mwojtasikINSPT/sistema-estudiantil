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
                    "2. Listar activas",
                    "3. Listar inactivas",
                    "4. Buscar por código",
                    "5. Modificar capacidad",
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
                        listarActivas();
                        break;
                    case 3:
                        listarInactivas();
                        break;
                    case 4:
                        buscarPorCodigo();
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
        int capacidad = ConsolaUtils.leerEntero("Capacidad del aula");

        ConsolaUtils.imprimirMensaje("Capacidad: " + capacidad);

        if (!ConsolaUtils.confirmar("¿Confirmar alta del aula?")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        String codigo = controller.agregarAula(capacidad);

        ConsolaUtils.imprimirRegistroCreado("Aula", codigo);
    }

    private void listarActivas() {
        var aulas = controller.listarAulas();

        if (aulas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay aulas activas.");
            return;
        }

        ConsolaUtils.imprimirTitulo("AULAS ACTIVAS");
        ConsolaUtils.imprimirMensaje("Registros encontrados: " + aulas.size());

        for (AulaDTO aula : aulas) {
            mostrarAula(aula);
        }
    }

    private void listarInactivas() {
        var aulas = controller.listarInactivos();

        if (aulas.isEmpty()) {
            ConsolaUtils.imprimirMensaje("No hay aulas inactivas.");
            return;
        }

        ConsolaUtils.imprimirTitulo("AULAS INACTIVAS");
        ConsolaUtils.imprimirMensaje("Registros encontrados: " + aulas.size());

        for (AulaDTO aula : aulas) {
            mostrarAula(aula);
        }
    }

    private void buscarPorCodigo() {
        String codigo = ConsolaUtils.leerTexto(
                "Código del aula (ejemplo: AUL0001, o 0 para cancelar)"
        );

        if (codigo.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        mostrarAula(controller.buscarPorCodigoIncluyendoInactivas(codigo));
    }

    private void modificar() throws IOException {
        String codigo = ConsolaUtils.leerTexto(
                "Código del aula (ejemplo: AUL0001, o 0 para cancelar)"
        );

        if (codigo.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

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
            mostrarAula(controller.buscarPorCodigo(codigo));
        }
    }

    private void eliminar() throws IOException {
        String codigo = ConsolaUtils.leerTexto(
                "Código del aula (ejemplo: AUL0001, o 0 para cancelar)"
        );

        if (codigo.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        AulaDTO aula = controller.buscarPorCodigoIncluyendoInactivas(codigo);

        if (aula == null) {
            ConsolaUtils.imprimirError("Aula no encontrada.");
            return;
        }

        if (!aula.isActivo()) {
            mostrarAula(aula);
            ConsolaUtils.imprimirMensaje(
                    "El aula ya está inactiva."
            );
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
        String codigo = ConsolaUtils.leerTexto(
                "Código del aula a reactivar "
                + "(ejemplo: AUL0001, o 0 para cancelar)"
        );

        if (codigo.equals("0")) {
            ConsolaUtils.imprimirMensaje("Operación cancelada.");
            return;
        }

        if (ConsolaUtils.confirmar("¿Confirmar reactivación?")) {
            controller.reactivarAula(codigo);
            ConsolaUtils.imprimirMensaje("Aula reactivada correctamente.");
            mostrarAula(controller.buscarPorCodigo(codigo));
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
