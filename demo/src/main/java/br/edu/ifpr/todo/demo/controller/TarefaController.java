package br.edu.ifpr.todo.demo.controller;

import br.edu.ifpr.todo.demo.model.Tarefa;
import br.edu.ifpr.todo.demo.repository.TarefaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import br.edu.ifpr.todo.demo.model.Tarefa;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    
    @Autowired
    private TarefaRepository tarefaRepository;
    
    @PostMapping
    public ResponseEntity<Tarefa> cadastrar(@RequestBody Tarefa tarefa) {

        Tarefa salvo = tarefaRepository.save(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(salvo));
    }


    @GetMapping
    public List<Tarefa> getAllTarefas() {
        System.err.println("AAAAAAAAAAAAAAAAA");

        return tarefaRepository.findAll()
                .stream()
                .collect(Collectors.toList());
    }

}
