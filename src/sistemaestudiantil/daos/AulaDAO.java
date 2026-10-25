package sistemaestudiantil.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.dtos.AulaDTO;
import sistemaestudiantil.exceptions.EntidadDuplicadaException;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.interfaces.ICrud;

public class AulaDAO implements ICrud<AulaDTO, String> {

    private static final String FILE_PATH = "aulas.txt";

    public AulaDAO() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createFile(Paths.get(FILE_PATH));
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al crear el archivo: " + FILE_PATH, e
            );
        }
    }

    @Override
    public List<AulaDTO> listar() {
        try {
            return Files.readAllLines(Paths.get(FILE_PATH))
                    .stream()
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);

                        if (datos.length != 3
                                || datos[0].isBlank()
                                || datos[1].isBlank()
                                || (!datos[2].trim().equalsIgnoreCase("true")
                                && !datos[2].trim().equalsIgnoreCase("false"))) {
                            throw new IllegalArgumentException(
                                    "Registro de aula inválido en "
                                    + FILE_PATH + ": " + linea
                            );
                        }

                        int capacidad;

                        try {
                            capacidad = Integer.parseInt(datos[1].trim());
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException(
                                    "Capacidad inválida en "
                                    + FILE_PATH + ": " + linea, e
                            );
                        }

                        if (capacidad <= 0) {
                            throw new IllegalArgumentException(
                                    "La capacidad debe ser mayor que cero"
                            );
                        }

                        return new AulaDTO(
                                datos[0].trim(),
                                capacidad,
                                Boolean.parseBoolean(datos[2].trim())
                        );
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error al leer el archivo: " + FILE_PATH, e
            );
        }
    }

    @Override
    public void guardarAll(List<AulaDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(a -> String.join(",",
                        a.getCodAula(),
                        String.valueOf(a.getCapacidad()),
                        String.valueOf(a.isActivo())
                ))
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get(FILE_PATH), lineas);
        } catch (IOException e) {
            throw new IOException(
                    "Error al guardar el archivo: " + FILE_PATH, e
            );
        }
    }

    @Override
    public void agregar(AulaDTO nueva) throws IOException {
        List<AulaDTO> aulas = listar();

        boolean existe = aulas.stream()
                .anyMatch(a -> a.getCodAula().equals(nueva.getCodAula()));

        if (existe) {
            throw new EntidadDuplicadaException(
                    "El aula ya existe"
            );
        }

        aulas.add(nueva);
        guardarAll(aulas);
    }

    @Override
    public AulaDTO buscarPorId(String codigo) {
        return listar().stream()
                .filter(a -> a.getCodAula().equals(codigo)
                        && a.isActivo())
                .findFirst()
                .orElse(null);
    }

    public void actualizarCapacidad(String codigo, int nuevaCapacidad)
            throws IOException {

        if (nuevaCapacidad <= 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser mayor que cero"
            );
        }

        List<AulaDTO> aulas = listar();

        for (int i = 0; i < aulas.size(); i++) {
            AulaDTO a = aulas.get(i);

            if (a.getCodAula().equals(codigo) && a.isActivo()) {
                aulas.set(i,
                        new AulaDTO(
                                a.getCodAula(),
                                nuevaCapacidad,
                                a.isActivo()
                        )
                );

                guardarAll(aulas);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "El aula no existe o está inactiva"
        );
    }

    @Override
    public void eliminar(String codigo) throws IOException {
        List<AulaDTO> aulas = listar();

        for (int i = 0; i < aulas.size(); i++) {
            AulaDTO a = aulas.get(i);

            if (a.getCodAula().equals(codigo) && a.isActivo()) {
                aulas.set(i,
                        new AulaDTO(
                                a.getCodAula(),
                                a.getCapacidad(),
                                false
                        )
                );

                guardarAll(aulas);
                return;
            }
        }

        throw new EntidadNoEncontradaException(
                "El aula no existe o ya está inactiva"
        );
    }
    
    public void reactivar(String codigo) throws IOException {
    List<AulaDTO> aulas = listar();

    for (int i = 0; i < aulas.size(); i++) {
        AulaDTO aula = aulas.get(i);

        if (aula.getCodAula().equals(codigo) && !aula.isActivo()) {
            aulas.set(i, new AulaDTO(
                    aula.getCodAula(),
                    aula.getCapacidad(),
                    true
            ));

            guardarAll(aulas);
            return;
        }
    }

    throw new EntidadNoEncontradaException(
            "El aula no existe o ya está activa"
    );
}
}