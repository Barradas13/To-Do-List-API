package br.edu.ifpr.todo.demo.controller;

import br.edu.ifpr.todo.demo.model.Tarefa;
import br.edu.ifpr.todo.demo.model.User;import br.edu.ifpr.todo.demo.model.Status;
import br.edu.ifpr.todo.demo.repository.TarefaRepository;
import br.edu.ifpr.todo.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    private final TarefaRepository tarefaRepository;
    private final UserRepository userRepository;

    public TarefaController(TarefaRepository tarefaRepository, UserRepository userRepository) {
        this.tarefaRepository = tarefaRepository;
        this.userRepository = userRepository;
    }

    // Método auxiliar para obter o usuário autenticado
    private Optional<User> getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // O email é o username no Spring Security
        return userRepository.findByEmail(email);
    }

    // Criar tarefa - AGORA ASSOCIADA AO USUÁRIO AUTENTICADO
    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody Tarefa tarefa) {
        Optional<User> usuario = getUsuarioAutenticado();
        
        if (tarefa.getStatus() == null) {
            tarefa.setStatus(Status.AFAZER);
        }
        tarefa.setDataCriacao(LocalDate.now());
        tarefa.setUser(usuario.get()); // 🔐 Associa a tarefa ao usuário autenticado
        
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        return ResponseEntity.status(201).body(novaTarefa);
    }

    // Listar tarefas - APENAS DO USUÁRIO AUTENTICADO
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas(@RequestParam(required = false) Status status, @RequestParam(required = false) Boolean importante) {

        Optional<User> usuario = getUsuarioAutenticado();
        Long usuarioId = usuario.get().getId();

        List<Tarefa> tarefas;

        if (status != null && importante != null) {
            tarefas = tarefaRepository.findByUserIdAndStatusAndImportante(usuarioId, status, importante);
        } else if (status != null) {
            tarefas = tarefaRepository.findByUserIdAndStatus(usuarioId, status);
        } else if (importante != null) {
            tarefas = tarefaRepository.findByUserIdAndImportante(usuarioId, importante);
        } else {
            tarefas = tarefaRepository.findByUserId(usuarioId);
        }

        if (tarefas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tarefas);
    }

    // Buscar tarefa por id - APENAS SE PERTENCER AO USUÁRIO
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        Optional<User> usuario = getUsuarioAutenticado();
        
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        
        if (tarefa.isPresent()) {
            // 🔐 Verifica se a tarefa pertence ao usuário autenticado
            if (!tarefa.get().getUser().getId().equals(usuario.get().getId())) {
                return ResponseEntity.status(403).build(); // Forbidden - não é dono da tarefa
            }
            return ResponseEntity.ok(tarefa.get());
        }
        
        return ResponseEntity.notFound().build();
    }

    // Atualizar tarefa - APENAS SE PERTENCER AO USUÁRIO
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        Optional<User> usuario = getUsuarioAutenticado();
        
        return tarefaRepository.findById(id).map(tarefa -> {
            // 🔐 Verifica se a tarefa pertence ao usuário
            if (!tarefa.getUser().getId().equals(usuario.get().getId())) {
                throw new RuntimeException("Acesso negado a esta tarefa");
            }
            
            tarefa.setNome(tarefaAtualizada.getNome());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setImportante(tarefaAtualizada.getImportante());
            tarefa.setStatus(tarefaAtualizada.getStatus());
            tarefa.setDataPrazo(tarefaAtualizada.getDataPrazo());
            
            return ResponseEntity.ok(tarefaRepository.save(tarefa));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Atualização parcial - APENAS SE PERTENCER AO USUÁRIO
    @PatchMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        Optional<User> usuario = getUsuarioAutenticado();
        
        return tarefaRepository.findById(id).map(tarefa -> {
            // 🔐 Verifica se a tarefa pertence ao usuário
            if (!tarefa.getUser().getId().equals(usuario.get().getId())) {
                throw new RuntimeException("Acesso negado a esta tarefa");
            }

            updates.forEach((key, value) -> {
                switch (key) {
                    case "nome":
                        tarefa.setNome((String) value);
                        break;
                    case "descricao":
                        tarefa.setDescricao((String) value);
                        break;
                    case "importante":
                        tarefa.setImportante((Boolean) value);
                        break;
                    case "status":
                        tarefa.setStatus(Status.valueOf((String) value));
                        break;
                    case "dataPrazo":
                        tarefa.setDataPrazo(LocalDate.parse((String) value));
                        break;
                }
            });

            Tarefa tarefaAtualizada = tarefaRepository.save(tarefa);
            return ResponseEntity.ok(tarefaAtualizada);

        }).orElse(ResponseEntity.notFound().build());
    }

    // Deletar tarefa - APENAS SE PERTENCER AO USUÁRIO
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<User> usuario = getUsuarioAutenticado();
        
        Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
        
        if (tarefaOptional.isPresent()) {
            Tarefa tarefa = tarefaOptional.get();
            
            // 🔐 Verifica se a tarefa pertence ao usuário
            if (!tarefa.getUser().getId().equals(usuario.get().getId())) {
                return ResponseEntity.status(403).build(); // Forbidden
            }
            
            tarefaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }
}