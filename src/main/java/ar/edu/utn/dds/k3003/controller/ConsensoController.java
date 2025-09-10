package ar.edu.utn.dds.k3003.controller;

import java.util.Map;

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

    // @PatchMapping
    // public ResponseEntity<Void> configurarConsenso(@RequestBody Map<String, String> body) {
    //     ConsensosEnum consenso = ConsensosEnum.valueOf(body.get("tipo").toUpperCase());
    //     String coleccion = body.get("coleccion");

    //     fachadaAgregador.setConsensoStrategy(consenso, coleccion);
    //     return ResponseEntity.noContent().build();
    // }

}