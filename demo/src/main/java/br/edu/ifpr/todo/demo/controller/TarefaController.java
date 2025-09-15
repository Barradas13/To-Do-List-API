package br.edu.ifpr.todo.demo.controller;

import br.edu.ifpr.todo.demo.model.Tarefa;
import br.edu.ifpr.todo.demo.repository.TarefaRepository;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import br.edu.ifpr.todo.demo.model.Status;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/tarefas")
public class TarefaController {

    private final TarefaRepository tarefaRepository;

    public TarefaController(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    // Criar tarefa
    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody Tarefa tarefa) {
        if (tarefa.getStatus() == null) {
            tarefa.setStatus(Status.AFAZER);
        }
        tarefa.setDataCriacao(java.time.LocalDate.now());
        Tarefa novaTarefa = tarefaRepository.save(tarefa);
        return ResponseEntity.status(201).body(novaTarefa);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas(@RequestParam(required = false) Status status, @RequestParam(required = false) Boolean importante) {

        List<Tarefa> tarefas;

        if (status != null && importante != null) {
            tarefas = tarefaRepository.findByStatusAndImportante(status, importante);
        } else if (status != null) {
            tarefas = tarefaRepository.findByStatus(status);
        } else if (importante != null) {
            tarefas = tarefaRepository.findByImportante(importante);
        } else {
            tarefas = tarefaRepository.findAll();
        }

        if (tarefas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tarefas);
    }

    // Buscar tarefa por id
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        Optional<Tarefa> tarefa = tarefaRepository.findById(id);
        return tarefa.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar tarefa
    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        return tarefaRepository.findById(id).map(tarefa -> {
            tarefa.setNome(tarefaAtualizada.getNome());
            tarefa.setDescricao(tarefaAtualizada.getDescricao());
            tarefa.setImportante(tarefaAtualizada.getImportante());
            tarefa.setStatus(tarefaAtualizada.getStatus());
            tarefa.setDataPrazo(tarefaAtualizada.getDataPrazo());
            return ResponseEntity.ok(tarefaRepository.save(tarefa));
        }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        return tarefaRepository.findById(id).map(tarefa -> {

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


    // Deletar tarefa
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!tarefaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tarefaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
