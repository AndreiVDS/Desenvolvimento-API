package com.andrei.tarefas_api.dto;

import com.andrei.tarefas_api.Tarefa;

import java.time.LocalDate;

/** O que a API devolve. Nunca expomos a entidade JPA diretamente. */
public record TarefaResponse(
        Long id,
        String nome,
        LocalDate dataEntrega,
        String responsavel
) {
    public static TarefaResponse from(Tarefa t) {
        return new TarefaResponse(t.getId(), t.getNome(), t.getDataEntrega(), t.getResponsavel());
    }
}
