package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.app.client.SolicitudesProxy;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;

import java.util.List;
import java.util.Set;
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
        // Obtenemos todos los hechos de todas las fuentes sin repeticiones
        Set<HechoDTO> hechosUnicos = fuentes.stream()
                .flatMap(f -> f.getHechos().stream())
                .collect(Collectors.toSet());

        // Filtramos los hechos que no tienen solicitudes de eliminación aceptadas
        // No hay solicitudes cuando un hecho esta activo
        return hechosUnicos.stream()
                .filter(hecho -> !solicitudesProxy.tieneSolicitudes(hecho.id()))
                .collect(Collectors.toList());
    }
}
