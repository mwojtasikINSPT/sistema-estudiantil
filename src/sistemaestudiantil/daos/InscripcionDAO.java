package sistemaestudiantil.daos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import sistemaestudiantil.dtos.InscripcionDTO;
import sistemaestudiantil.exceptions.EntidadDuplicadaException;
import sistemaestudiantil.exceptions.EntidadNoEncontradaException;
import sistemaestudiantil.interfaces.ICrud;

public class InscripcionDAO implements ICrud<InscripcionDTO, String> {

    private static final String FILE_PATH = "inscripciones.txt";

    public InscripcionDAO() {
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
    public List<InscripcionDTO> listar() {
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
                                    "Registro de inscripción inválido: " + linea
                            );
                        }

                        return new InscripcionDTO(
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
    public void guardarAll(List<InscripcionDTO> lista) throws IOException {
        List<String> lineas = lista.stream()
                .map(i -> String.join(",",
                        i.getId(),
                        i.getIdEstudiante(),
                        i.getCodAula(),
                        String.valueOf(i.isActivo())
                ))
                .collect(Collectors.toList());

        Files.write(Paths.get(FILE_PATH), lineas);
    }

    @Override
    public void agregar(InscripcionDTO nueva) throws IOException {
        List<InscripcionDTO> inscripciones = listar();

        if (inscripciones.stream()
                .anyMatch(i -> i.getId().equals(nueva.getId()))) {
            throw new EntidadDuplicadaException(
                    "Ya existe una inscripción con ese ID"
            );
        }

        if (inscripciones.stream()
                .anyMatch(i -> i.isActivo()
                && i.getIdEstudiante().equals(nueva.getIdEstudiante()))) {
            throw new EntidadDuplicadaException(
                    "El estudiante ya está inscripto en un aula"
            );
        }

        inscripciones.add(nueva);
        guardarAll(inscripciones);
    }

    @Override
    public InscripcionDTO buscarPorId(String id) {
        return listar().stream()
                .filter(i -> i.getId().equals(id) && i.isActivo())
                .findFirst()
                .orElse(null);
    }

    public InscripcionDTO buscarPorEstudiante(String idEstudiante) {
        return listar().stream()
                .filter(i -> i.getIdEstudiante().equals(idEstudiante)
                && i.isActivo())
                .findFirst()
                .orElse(null);
    }

    public List<InscripcionDTO> buscarPorAula(String codAula) {
        return listar().stream()
                .filter(i -> i.getCodAula().equals(codAula)
                && i.isActivo())
                .collect(Collectors.toList());
    }

    @Override
    public void eliminar(String codigo) throws IOException {
        List<InscripcionDTO> inscripciones = listar();
        boolean encontrado = false;

        for (int i = 0; i < inscripciones.size(); i++) {
            InscripcionDTO inscripcion = inscripciones.get(i);

            if (inscripcion.isActivo()
                    && (inscripcion.getId().equals(codigo)
                    || inscripcion.getIdEstudiante().equals(codigo)
                    || inscripcion.getCodAula().equals(codigo))) {

                inscripciones.set(i, new InscripcionDTO(
                        inscripcion.getId(),
                        inscripcion.getIdEstudiante(),
                        inscripcion.getCodAula(),
                        false
                ));

                encontrado = true;
            }
        }

        if (!encontrado) {
            throw new EntidadNoEncontradaException(
                    "No existen inscripciones activas asociadas al código ingresado"
            );
        }

        guardarAll(inscripciones);
    }
}