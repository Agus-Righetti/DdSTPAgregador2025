package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.app.client.SolicitudesProxy;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;

import java.util.List;
import java.util.stream.Collectors;

public class ConsensoEstrictoStrategy implements ConsensoStrategy
{
    private final SolicitudesProxy solicitudesProxy;

    public ConsensoEstrictoStrategy(SolicitudesProxy solicitudesProxy)
    {
        this.solicitudesProxy = solicitudesProxy;
    }

    @Override
    public List<HechoDTO> aplicar(List<Fuente> fuentes)
    {
        // Obtenemos todos los hechos de todas las fuentes
        List<HechoDTO> todosLosHechos = fuentes.stream()
                .flatMap(f -> f.getHechos().stream())
                .toList();

        // Filtramos los hechos que no tienen solicitudes de eliminación aceptadas
        return todosLosHechos.stream()
                .filter(hecho -> solicitudesProxy.estaActivo(hecho.id()))
                .collect(Collectors.toList());
    }
}
