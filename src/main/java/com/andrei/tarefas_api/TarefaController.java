package com.andrei.tarefas_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    // Criar uma nova tarefa
    @PostMapping
    public Tarefa criarTarefa(@RequestBody Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    // Obter todas as tarefas
    @GetMapping
    public List<Tarefa> obterTodasTarefas() {
        return tarefaRepository.findAll();
    }

    // Obter uma tarefa pelo ID
    @GetMapping("/{id}")
    public Optional<Tarefa> obterTarefaPorId(@PathVariable Long id) {
        return tarefaRepository.findById(id);
    }

    // Atualizar uma tarefa existente
    @PutMapping("/{id}")
    public Tarefa atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefa.setNome(tarefaAtualizada.getNome());
                    tarefa.setDataEntrega(tarefaAtualizada.getDataEntrega());
                    tarefa.setResponsavel(tarefaAtualizada.getResponsavel());
                    return tarefaRepository.save(tarefa);
                })
                .orElseGet(() -> {
                    tarefaAtualizada.setId(id);
                    return tarefaRepository.save(tarefaAtualizada);
                });
    }

    // Deletar uma tarefa
    @DeleteMapping("/{id}")
    public void deletarTarefa(@PathVariable Long id) {
        tarefaRepository.deleteById(id);
    }
}