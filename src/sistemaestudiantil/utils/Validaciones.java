package sistemaestudiantil.utils;

public final class Validaciones {

    private Validaciones() {
    }

    public static boolean esDniValido(String dni) {
        return dni != null && dni.matches("\\d{8}");
    }

    public static boolean esTextoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean esNumeroPositivo(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        try {
            return Integer.parseInt(texto.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String normalizarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.trim().toUpperCase();
    }

    public static boolean esCodigoValido(String codigo) {
        return codigo != null
                && codigo.matches("[A-Z]{3}\\d{4}");
    }

    public static boolean esCodigoValido(String codigo, String prefijo) {
        if (!esCodigoValido(codigo) || prefijo == null) {
            return false;
        }

        return codigo.startsWith(normalizarTexto(prefijo))
                && normalizarTexto(prefijo).matches("[A-Z]{3}");
    }

    public static String validarTextoObligatorio(String texto, String campo) {
        String normalizado = normalizarTexto(texto);

        if (!esTextoValido(normalizado)) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " no puede estar vacío"
            );
        }

        if (normalizado.contains(",")) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " no puede contener comas"
            );
        }

        return normalizado;
    }

    public static String validarDni(String dni) {
        String normalizado = dni == null ? "" : dni.trim();

        if (!esDniValido(normalizado)) {
            throw new IllegalArgumentException(
                    "El DNI debe contener exactamente 8 dígitos"
            );
        }

        return normalizado;
    }

    public static String validarCodigo(String codigo, String prefijo) {
        String normalizado = normalizarTexto(codigo);

        if (!esCodigoValido(normalizado, prefijo)) {
            throw new IllegalArgumentException(
                    "Código inválido. Formato esperado: "
                    + normalizarTexto(prefijo) + "0001"
            );
        }

        return normalizado;
    }
}