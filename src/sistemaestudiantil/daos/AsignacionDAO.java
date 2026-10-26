package sistemaestudiantil.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.dtos.AsignacionDTO;
import sistemaestudiantil.exceptions.EntidadDuplicadaException;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.interfaces.ICrud;

public class AsignacionDAO implements ICrud<AsignacionDTO, String> {

    private static final String FILE_PATH = "asignaciones.txt";

    public AsignacionDAO() {
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
    public List<AsignacionDTO> listar() {
        try {
            return Files.readAllLines(Paths.get(FILE_PATH))
                    .stream()
                    .filter(linea -> !linea.trim().isEmpty())
                    .map(linea -> {
                        String[] datos = linea.split(",", -1);

                        if (datos.length != 4
                                || datos[0].isBlank()
                                || datos[1].isBlank()
                                || datos[2].isBlank()
                                || (!datos[3].trim().equalsIgnoreCase("true")
                                && !datos[3].trim().equalsIgnoreCase("false"))) {
                            throw new IllegalArgumentException(
                                    "Registro de asignación inválido: " + linea
                            );
                        }

                        return new AsignacionDTO(
                                datos[0].trim(),
                                datos[1].trim(),
                                datos[2].trim(),
                                Boolean.parseBoolean(datos[3].trim())
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
    public void guardarAll(List<AsignacionDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(a -> String.join(",",
                        a.getId(),
                        a.getIdProfesor(),
                        a.getCodAula(),
                        String.valueOf(a.isActivo())
                ))
                .collect(Collectors.toList());

        Files.write(Paths.get(FILE_PATH), lineas);
    }

    @Override
    public void agregar(AsignacionDTO nueva) throws IOException {
        List<AsignacionDTO> asignaciones = listar();

        if (asignaciones.stream()
                .anyMatch(a -> a.getId().equals(nueva.getId()))) {
            throw new EntidadDuplicadaException(
                    "Ya existe una asignación con ese ID"
            );
        }

        if (asignaciones.stream()
                .anyMatch(a -> a.isActivo()
                && a.getIdProfesor().equals(nueva.getIdProfesor()))) {
            throw new EntidadDuplicadaException(
                    "El profesor ya tiene un aula asignada"
            );
        }

        if (asignaciones.stream()
                .anyMatch(a -> a.isActivo()
                && a.getCodAula().equals(nueva.getCodAula()))) {
            throw new EntidadDuplicadaException(
                    "El aula ya tiene un profesor asignado"
            );
        }

        asignaciones.add(nueva);
        guardarAll(asignaciones);
    }

    @Override
    public AsignacionDTO buscarPorId(String id) {
        return listar().stream()
                .filter(a -> a.getId().equals(id) && a.isActivo())
                .findFirst()
                .orElse(null);
    }

    public AsignacionDTO buscarPorIdIncluyendoInactivas(String id) {
        return listar().stream()
                .filter(asignacion -> asignacion.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public AsignacionDTO buscarPorAula(String codAula) {
        return listar().stream()
                .filter(a -> a.getCodAula().equals(codAula)
                && a.isActivo())
                .findFirst()
                .orElse(null);
    }

    public AsignacionDTO buscarPorProfesor(String idProfesor) {
        return listar().stream()
                .filter(a -> a.getIdProfesor().equals(idProfesor)
                && a.isActivo())
                .findFirst()
                .orElse(null);
    }

    @Override
    public void eliminar(String codigo) throws IOException {
        List<AsignacionDTO> asignaciones = listar();
        boolean encontrado = false;

        for (int i = 0; i < asignaciones.size(); i++) {
            AsignacionDTO asignacion = asignaciones.get(i);

            if (asignacion.isActivo()
                    && (asignacion.getId().equals(codigo)
                    || asignacion.getIdProfesor().equals(codigo)
                    || asignacion.getCodAula().equals(codigo))) {

                asignaciones.set(i, new AsignacionDTO(
                        asignacion.getId(),
                        asignacion.getIdProfesor(),
                        asignacion.getCodAula(),
                        false
                ));

                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new EntidadNoEncontradaException(
                    "No existen asignaciones activas asociadas al código ingresado"
            );
        }

        guardarAll(asignaciones);
    }
}
