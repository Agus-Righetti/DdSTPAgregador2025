package ar.edu.utn.dds.k3003.dtos;

import ar.edu.utn.dds.k3003.enums.ConsensosEnum;

public record ConsensoRequestDTO(ConsensosEnum tipo, String coleccion) {
}


