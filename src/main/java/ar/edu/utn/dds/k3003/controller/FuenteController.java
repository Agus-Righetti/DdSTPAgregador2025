package ar.edu.utn.dds.k3003.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.dds.k3003.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.IFachadaAgregador;

@RestController
@RequestMapping("/api/fuentes")
public class FuenteController {

    private final IFachadaAgregador fachadaAgregador;

    public FuenteController(IFachadaAgregador fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    @GetMapping
    public ResponseEntity<List<FuenteDTO>> fuentes() {
        return ResponseEntity.ok(fachadaAgregador.fuentes());
    }

    @PostMapping
    public ResponseEntity<FuenteDTO> agregarFuente(@RequestBody FuenteDTO fuenteDTO) {
        return ResponseEntity.ok(fachadaAgregador.agregar(fuenteDTO));
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> borrarTodasLasFuentes()
    {
        fachadaAgregador.borrarTodasLasFuentes();
        return ResponseEntity.noContent().build();
    }

}
