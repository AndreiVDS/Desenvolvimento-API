package com.andrei.tarefas_api;

import com.andrei.tarefas_api.dto.TarefaResponse;
import com.andrei.tarefas_api.exception.TarefaNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)
class TarefaControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    @MockBean
    TarefaService service;

    @Test
    void lista_devolve_200_com_as_tarefas() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new TarefaResponse(1L, "Relatório", LocalDate.now().plusDays(3), "Andrei")));

        mvc.perform(get("/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Relatório"));
    }

    @Test
    void buscar_inexistente_devolve_404() throws Exception {
        when(service.buscar(99L)).thenThrow(new TarefaNotFoundException(99L));

        mvc.perform(get("/tarefas/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Tarefa 99 não encontrada"));
    }

    @Test
    void criar_valido_devolve_201_com_location() throws Exception {
        var body = Map.of("nome", "Deploy", "dataEntrega", LocalDate.now().plusDays(1).toString(),
                "responsavel", "Andrei");
        when(service.criar(any())).thenReturn(
                new TarefaResponse(10L, "Deploy", LocalDate.now().plusDays(1), "Andrei"));

        mvc.perform(post("/tarefas").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/tarefas/10")))
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void criar_sem_nome_devolve_400_com_erros_de_campo() throws Exception {
        var body = Map.of("dataEntrega", LocalDate.now().plusDays(1).toString(), "responsavel", "Andrei");

        mvc.perform(post("/tarefas").contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.nome").exists());

        verify(service, never()).criar(any());
    }

    @Test
    void deletar_devolve_204() throws Exception {
        mvc.perform(delete("/tarefas/1"))
                .andExpect(status().isNoContent());
        verify(service).deletar(eq(1L));
    }
}
