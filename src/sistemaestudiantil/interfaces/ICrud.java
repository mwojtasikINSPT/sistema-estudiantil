package sistemaestudiantil.interfaces;

import java.io.IOException;
import java.util.List;

public interface ICrud<T, ID> {

    void agregar(T entidad) throws IOException;
    
    void guardarAll(List<T> entidades) throws IOException;

    List<T> listar();

    T buscarPorId(ID id);

    void eliminar(ID id) throws IOException;
}