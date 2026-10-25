package sistemaestudiantil.utils;

import java.util.Scanner;

public final class ConsolaUtils {

    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsolaUtils() {
    }

    public static void imprimirMenu(String titulo, String... opciones) {
        imprimirTitulo(titulo);

        for (String opcion : opciones) {
            System.out.println(opcion);
        }

        System.out.println();
    }

    public static void imprimirMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public static void imprimirError(String mensaje) {
        System.err.println("ERROR: " + mensaje);
    }

    public static void imprimirTitulo(String titulo) {
        System.out.println();
        System.out.println("========== " + titulo + " ==========");
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return SCANNER.nextLine().trim();
    }

    public static int leerEntero(String mensaje) {
        while (true) {
            String entrada = leerTexto(mensaje);

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                imprimirError("Debe ingresar un número entero válido.");
            }
        }
    }

    public static boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje + " (S/N)");

            if (respuesta.equalsIgnoreCase("S")) {
                return true;
            }

            if (respuesta.equalsIgnoreCase("N")) {
                return false;
            }

            imprimirError("Ingrese S o N.");
        }
    }

    public static void pausar() {
        leerTexto("Presioná ENTER para continuar...");
    }

    public static void imprimirRegistroCreado(String tipo, String id) {
        imprimirMensaje(tipo + " creada correctamente. ID asignado: " + id);
    }
}
