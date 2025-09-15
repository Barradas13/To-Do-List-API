package br.edu.ifpr.todo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpr.todo.demo.model.Tarefa;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // exemplo de consultas extras
    Tarefa findByImportante(boolean importante);
}