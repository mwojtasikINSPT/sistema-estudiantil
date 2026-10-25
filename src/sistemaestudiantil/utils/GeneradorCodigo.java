package sistemaestudiantil.utils;

import java.util.List;

public final class GeneradorCodigo {

    private GeneradorCodigo() {
    }

    public static String generarSiguienteId(
            List<String> idsExistentes, String prefijo) {

        String prefijoNormalizado = Validaciones.normalizarTexto(prefijo);

        if (!prefijoNormalizado.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(
                    "El prefijo debe contener exactamente 3 letras"
            );
        }

        if (idsExistentes == null) {
            throw new IllegalArgumentException(
                    "La lista de identificadores no puede ser nula"
            );
        }

        int maxId = 0;

        for (String id : idsExistentes) {
            String codigo = Validaciones.normalizarTexto(id);

            if (Validaciones.esCodigoValido(codigo, prefijoNormalizado)) {
                int numero = Integer.parseInt(codigo.substring(3));
                maxId = Math.max(maxId, numero);
            }
        }

        if (maxId == 9999) {
            throw new IllegalStateException(
                    "Se agotaron los códigos del prefijo "
                    + prefijoNormalizado
            );
        }

        return prefijoNormalizado + String.format("%04d", maxId + 1);
    }
}