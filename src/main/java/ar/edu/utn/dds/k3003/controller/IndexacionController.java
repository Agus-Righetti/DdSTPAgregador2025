package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.client.IndexadorService;
import ar.edu.utn.dds.k3003.dtos.HechoParaIndexarDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// TODO ver manejo con Solicitudes
@RestController
@RequestMapping("/api/indexacion")
public class IndexacionController
{
    private final IndexadorService indexadorService;

    public IndexacionController(IndexadorService indexadorService)
    {
        this.indexadorService = indexadorService;
    }

    @PostMapping("/hechos")
    @ResponseStatus(HttpStatus.CREATED)
    public void indexarHecho(@RequestBody HechoParaIndexarDTO hechoIndexar)
    {
        indexadorService.indexarHecho(hechoIndexar);
    }

    @PatchMapping("/hechos/{hechoId}/borrado-exitoso")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void marcarHechoComoBorrado(@PathVariable String hechoId)
    {
        indexadorService.marcarComoBorrado(hechoId);
    }
}
