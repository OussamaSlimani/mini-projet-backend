package com.example.agents.controller;

import com.example.agents.model.Agent;
import com.example.agents.service.AgentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AgentController {
    
    private final AgentService agentService;
    
    // Récupère tous les agents
    @GetMapping("/agents")
    public ResponseEntity<List<Agent>> getAllAgents() {
        try {
            List<Agent> agents = agentService.getAllAgents();
            log.info("Retour de {} agents", agents.size());
            return ResponseEntity.ok(agents);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des agents: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Récupère un agent par ID 
    @GetMapping("/{userId}")
    public ResponseEntity<Agent> getAgentById(@PathVariable String userId) {
        try {
            return agentService.getAgentById(userId)
                    .map(agent -> {
                        log.info("Agent trouvé: {}", userId);
                        return ResponseEntity.ok(agent);
                    })
                    .orElseGet(() -> {
                        log.warn("Agent non trouvé: {}", userId);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'agent {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Crée un agent
    @PostMapping("/agent")
    public ResponseEntity<?> createAgent(@Valid @RequestBody Agent agent) {
        try {
            Agent createdAgent = agentService.createAgent(agent);
            log.info("Agent créé avec succès: {}", createdAgent.getUserId());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAgent);
        } catch (RuntimeException e) {
            log.error("Erreur lors de la création de l'agent: {}", e.getMessage());
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la création de l'agent: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Internal server error\"}");
        }
    }
    
    // Met à jour un agent 
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateAgent(@PathVariable String userId, 
                                        @Valid @RequestBody Agent agentDetails) {
        try {
            Agent updatedAgent = agentService.updateAgent(userId, agentDetails);
            log.info("Agent mis à jour avec succès: {}", userId);
            return ResponseEntity.ok(updatedAgent);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                log.warn("Agent non trouvé pour la mise à jour: {}", userId);
                return ResponseEntity.notFound().build();
            }
            log.error("Erreur lors de la mise à jour de l'agent {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la mise à jour de l'agent {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Internal server error\"}");
        }
    }
    
    // Supprime un agent
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteAgent(@PathVariable String userId) {
        try {
            boolean deleted = agentService.deleteAgent(userId);
            if (deleted) {
                log.info("Agent supprimé avec succès: {}", userId);
                return ResponseEntity.ok().body("{\"message\": \"Agent deleted successfully\"}");
            } else {
                log.warn("Agent non trouvé pour la suppression: {}", userId);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'agent {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Internal server error\"}");
        }
    }
    
    // Récupère les agents actifs
    @GetMapping("/agents/active")
    public ResponseEntity<List<Agent>> getActiveAgents() {
        try {
            List<Agent> activeAgents = agentService.getActiveAgents();
            log.info("Retour de {} agents actifs", activeAgents.size());
            return ResponseEntity.ok(activeAgents);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des agents actifs: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}