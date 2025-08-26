package com.example.agents.repository;

import com.example.agents.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
    
    // Recherche agent par nom
    Optional<Agent> findByUsername(String username);
    
    // Recherche agent par adresse email
    Optional<Agent> findByEmail(String email);
    
    // Recherche agents par statut 
    List<Agent> findByActive(Boolean active);
    
    // Récupère tous les agents
    @Query("SELECT a FROM Agent a LEFT JOIN FETCH a.roles LEFT JOIN FETCH a.userInfo LEFT JOIN FETCH a.userAddress")
    List<Agent> findAllWithRelations();
    
    // Récupère agent par ID
    @Query("SELECT a FROM Agent a LEFT JOIN FETCH a.roles LEFT JOIN FETCH a.userInfo LEFT JOIN FETCH a.userAddress WHERE a.userId = :userId")
    Optional<Agent> findByUserIdWithRelations(@Param("userId") String userId);
    
    // Vérification avant la création
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}