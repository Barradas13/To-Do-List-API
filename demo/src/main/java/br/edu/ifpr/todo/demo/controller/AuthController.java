// AuthController.java
package br.edu.ifpr.todo.demo.controller;

import br.edu.ifpr.todo.demo.dto.AuthResponseDTO;
import br.edu.ifpr.todo.demo.dto.LoginDTO;
import br.edu.ifpr.todo.demo.model.User;
import br.edu.ifpr.todo.demo.repository.UserRepository;
import br.edu.ifpr.todo.demo.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    
    public AuthController(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder, 
                         JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@Valid @RequestBody User user) {
        // Verifica se email já existe
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já está em uso");
        }
        
        // Criptografa a senha
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        
        // Salva o usuário
        User usuarioSalvo = userRepository.save(user);
        
        // Gera token
        String token = jwtService.generateToken(
            org.springframework.security.core.userdetails.User.builder()
                .username(usuarioSalvo.getEmail())
                .password(usuarioSalvo.getSenha())
                .build()
        );
        
        return ResponseEntity.ok(new AuthResponseDTO(token, usuarioSalvo.getEmail(), usuarioSalvo.getNome()));
    }
    
   // AuthController.java - Método login corrigido
@PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
    try {
        // Busca usuário pelo email
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }
        
        User user = userOptional.get();
        
        // Verifica a senha
        if (!passwordEncoder.matches(loginDTO.getSenha(), user.getSenha())) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }
        
        // Gera token usando o UserDetails
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getSenha()) // Já está criptografada
                .roles("USER")
                .build();
        
        String token = jwtService.generateToken(userDetails);
        
        return ResponseEntity.ok(new AuthResponseDTO(token, user.getEmail(), user.getNome()));
        
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro interno do servidor");
    }
}
}