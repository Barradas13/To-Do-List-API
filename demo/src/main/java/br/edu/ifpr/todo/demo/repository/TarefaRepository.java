package br.edu.ifpr.todo.demo.repository;

import br.edu.ifpr.todo.demo.model.Tarefa;
import br.edu.ifpr.todo.demo.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    
    // 🔐 Métodos específicos por usuário
    List<Tarefa> findByUserId(Long userId);
    List<Tarefa> findByUserIdAndStatus(Long userId, Status status);
    List<Tarefa> findByUserIdAndImportante(Long userId, Boolean importante);
    List<Tarefa> findByUserIdAndStatusAndImportante(Long userId, Status status, Boolean importante);
}