package com.andrei.tarefas_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Corpo aceito em POST /tarefas e PUT /tarefas/{id}. Validado antes de chegar no controller. */
public record TarefaRequest(
        @NotBlank(message = "nome é obrigatório")
        @Size(max = 120, message = "nome deve ter no máximo 120 caracteres")
        String nome,

        @NotNull(message = "dataEntrega é obrigatória")
        @FutureOrPresent(message = "dataEntrega não pode estar no passado")
        LocalDate dataEntrega,

        @NotBlank(message = "responsavel é obrigatório")
        @Size(max = 120, message = "responsavel deve ter no máximo 120 caracteres")
        String responsavel
) {
}
