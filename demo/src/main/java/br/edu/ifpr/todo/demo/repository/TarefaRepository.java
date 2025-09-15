package br.edu.ifpr.todo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ifpr.todo.demo.model.Tarefa;
import br.edu.ifpr.todo.demo.model.Status;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByImportante(boolean importante);
    List<Tarefa> findByStatus(Status status);
    List<Tarefa> findByStatusAndImportante(Status status, boolean importante);

}