package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.dtos.ConsensoRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.dds.k3003.enums.ConsensosEnum;
import ar.edu.utn.dds.k3003.facades.IFachadaAgregador;

@RestController
@RequestMapping("/api/consenso")
public class ConsensoController {

    private final IFachadaAgregador fachadaAgregador;

    public ConsensoController(IFachadaAgregador fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }

    @PatchMapping
    public ResponseEntity<Void> configurarConsenso(@RequestBody ConsensoRequestDTO body) {
        ConsensosEnum consenso = body.tipo();
        String coleccion = body.coleccion();

        if (consenso == null) {
            throw new IllegalArgumentException("Falta el tipo de consenso");
        }

        fachadaAgregador.setConsensoStrategy(consenso, coleccion);
        return ResponseEntity.noContent().build();
    }

}