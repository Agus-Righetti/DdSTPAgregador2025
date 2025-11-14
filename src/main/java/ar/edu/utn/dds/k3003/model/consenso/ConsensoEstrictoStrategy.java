package ar.edu.utn.dds.k3003.model.consenso;

import ar.edu.utn.dds.k3003.app.client.SolicitudesProxy;
import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Fuente;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

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
        Set<HechoDTO> hechosUnicos = fuentes.stream()
                .flatMap(f -> f.getHechos().stream())
                .collect(Collectors.toSet());

        List<String> hechoIds = hechosUnicos.stream()
                .map(HechoDTO::id)
                .toList();

        List<String> blacklistTitulos = new java.util.ArrayList<>();

        Map<String, Boolean> estadosSolicitudes = solicitudesProxy.tienenSolicitudes(hechoIds);

        List<HechoDTO> hechos = hechosUnicos.stream()
                .filter(hecho -> {
                    boolean tieneSolicitudesActivas = estadosSolicitudes.getOrDefault(hecho.id(), true);
                    if(tieneSolicitudesActivas)
                    {
                        blacklistTitulos.add(hecho.titulo());
                    }
                    return !tieneSolicitudesActivas;
                })
                .collect(Collectors.toList());
        System.out.println("Blacklist titulos: " + blacklistTitulos);
        return hechos.stream()
              .filter(hecho -> {
                  boolean algo = !blacklistTitulos.contains(hecho.titulo());
                  blacklistTitulos.add(hecho.titulo());
                    return algo;
              }) .collect(Collectors.toList());
    }
}
