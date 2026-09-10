package com.andrei.tarefas_api;

import com.andrei.tarefas_api.dto.TarefaRequest;
import com.andrei.tarefas_api.dto.TarefaResponse;
import com.andrei.tarefas_api.exception.TarefaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/** Regras de negócio das tarefas. O controller só cuida de HTTP. */
@Service
public class TarefaService {

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    public List<TarefaResponse> listar() {
        return repository.findAll().stream().map(TarefaResponse::from).toList();
    }

    public TarefaResponse buscar(Long id) {
        return repository.findById(id)
                .map(TarefaResponse::from)
                .orElseThrow(() -> new TarefaNotFoundException(id));
    }

    public TarefaResponse criar(TarefaRequest req) {
        Tarefa nova = new Tarefa(req.nome(), req.dataEntrega(), req.responsavel());
        return TarefaResponse.from(repository.save(nova));
    }

    public TarefaResponse atualizar(Long id, TarefaRequest req) {
        Tarefa tarefa = repository.findById(id).orElseThrow(() -> new TarefaNotFoundException(id));
        tarefa.setNome(req.nome());
        tarefa.setDataEntrega(req.dataEntrega());
        tarefa.setResponsavel(req.responsavel());
        return TarefaResponse.from(repository.save(tarefa));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new TarefaNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
