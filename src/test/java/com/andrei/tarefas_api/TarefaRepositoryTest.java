package com.andrei.tarefas_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TarefaRepositoryTest {

    @Autowired
    TarefaRepository repository;

    @Test
    void salva_e_recupera_por_id() {
        Tarefa salva = repository.save(new Tarefa("Escrever docs", LocalDate.now().plusDays(2), "Andrei"));

        assertThat(salva.getId()).isNotNull();
        assertThat(repository.findById(salva.getId()))
                .get()
                .satisfies(t -> assertThat(t.getNome()).isEqualTo("Escrever docs"));
    }

    @Test
    void existsById_reflete_a_remocao() {
        Tarefa salva = repository.save(new Tarefa("Temp", LocalDate.now(), "Andrei"));
        assertThat(repository.existsById(salva.getId())).isTrue();

        repository.deleteById(salva.getId());
        assertThat(repository.existsById(salva.getId())).isFalse();
    }
}
