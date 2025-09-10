package ar.edu.utn.dds.k3003.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.dds.k3003.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.facades.IFachadaAgregador;
import java.util.List;

@RestController
@RequestMapping("/api/coleccion")
public class ColeccionController {

    private final IFachadaAgregador fachada;

    public ColeccionController(IFachadaAgregador fachada) {
        this.fachada = fachada;
    }

    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<List<HechoDTO>> listarHechosPorColeccion(@PathVariable String nombre) {
        return ResponseEntity.ok(fachada.hechos(nombre));
    }

}