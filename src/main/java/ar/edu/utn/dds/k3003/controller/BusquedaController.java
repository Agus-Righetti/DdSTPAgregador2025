package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.dtos.PaginacionDTO;
import ar.edu.utn.dds.k3003.facades.IFachadaAgregador;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO Telegram
@RestController
@RequestMapping("/api/busqueda")
public class BusquedaController
{
    private final IFachadaAgregador fachada;

    public BusquedaController(IFachadaAgregador fachada)
    {
        this.fachada = fachada;
    }

    @GetMapping
    public ResponseEntity<PaginacionDTO> buscarHechos(
            @RequestParam(name = "query", required = false) String palabraClave,
            @RequestParam(name = "tags", required = false) List<String> tags, // Acepta múltiples tags
            @RequestParam(name = "page", defaultValue = "0") int pagina,
            @RequestParam(name = "size", defaultValue = "10") int tamanoPagina)
    {

//        if (palabraClave == null || palabraClave.isBlank())
//        {
//            // Manejo de error o retornar lista vacía
//            return ResponseEntity.badRequest().build();
//        }

        System.out.println(tags);

        PaginacionDTO resultados = fachada.buscar(palabraClave, tags, pagina, tamanoPagina);
        return ResponseEntity.ok(resultados);
    }

    @PostMapping("/indexar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void indexarTodo()
    {
        fachada.indexarTodo();
    }
} 