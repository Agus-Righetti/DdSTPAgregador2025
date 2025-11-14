package ar.edu.utn.dds.k3003.facades;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;

import ar.edu.utn.dds.k3003.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.dtos.HechoConPdisDTO;
import ar.edu.utn.dds.k3003.dtos.PaginacionDTO;
import ar.edu.utn.dds.k3003.enums.ConsensosEnum;

public interface IFachadaAgregador
{

    FuenteDTO agregar(FuenteDTO fuente);
    List<FuenteDTO> fuentes();
    FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException;
    List<HechoConPdisDTO> hechos(String nombreColeccion) throws NoSuchElementException;
    // void addFachadaFuentes(String fuenteId, FachadaFuente fuente);
    void setConsensoStrategy(ConsensosEnum tipoConsenso, String nombreColeccion)
        throws InvalidParameterException;
    void borrarTodasLasFuentes();
    void indexarTodo();
    PaginacionDTO buscar(String palabraClave, List<String> tags, int pagina, int tamanoPagina);
}
