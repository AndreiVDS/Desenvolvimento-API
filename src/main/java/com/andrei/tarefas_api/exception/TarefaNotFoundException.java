package com.andrei.tarefas_api.exception;

public class TarefaNotFoundException extends RuntimeException {
    public TarefaNotFoundException(Long id) {
        super("Tarefa " + id + " não encontrada");
    }
}
