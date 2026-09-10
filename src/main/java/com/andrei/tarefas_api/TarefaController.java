package com.andrei.tarefas_api;

import com.andrei.tarefas_api.dto.TarefaRequest;
import com.andrei.tarefas_api.dto.TarefaResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
@Tag(name = "Tarefas", description = "CRUD de tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Lista todas as tarefas")
    public List<TarefaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma tarefa pelo id")
    public TarefaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    @Operation(summary = "Cria uma tarefa")
    public ResponseEntity<TarefaResponse> criar(@Valid @RequestBody TarefaRequest req) {
        TarefaResponse criada = service.criar(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.id()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma tarefa existente")
    public TarefaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequest req) {
        return service.atualizar(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove uma tarefa")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
