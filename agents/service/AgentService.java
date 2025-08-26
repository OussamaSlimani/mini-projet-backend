package com.example.agents.service;

import com.example.agents.model.Agent;
import com.example.agents.model.Role;
import com.example.agents.model.UserInfo;
import com.example.agents.model.UserAddress;
import com.example.agents.repository.AgentRepository;
import com.example.agents.util.UserIdGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AgentService {
    
    private final AgentRepository agentRepository;
    private final UserIdGenerator userIdGenerator;
    
    // Récupère tous les agents
    @Transactional(readOnly = true)
    public List<Agent> getAllAgents() {
        log.info("Récupération de tous les agents");
        return agentRepository.findAllWithRelations();
    }
    
    // Récupère un agent 
    @Transactional(readOnly = true)
    public Optional<Agent> getAgentById(String userId) {
        log.info("Récupération de l'agent avec l'ID: {}", userId);
        return agentRepository.findByUserIdWithRelations(userId);
    }
    
    // Crée un nouvel agent
    public Agent createAgent(Agent agent) {
        log.info("Création d'un nouvel agent: {}", agent.getUsername());
        
        // Vérifier l'unicité du username et email
        if (agentRepository.existsByUsername(agent.getUsername())) {
            throw new RuntimeException("Username already exists: " + agent.getUsername());
        }
        
        if (agentRepository.existsByEmail(agent.getEmail())) {
            throw new RuntimeException("Email already exists: " + agent.getEmail());
        }
        
        // Générer un userId
        String userId;
        do {
            userId = userIdGenerator.generateUserId();
        } while (agentRepository.existsById(userId));
        
        agent.setUserId(userId);
        agent.setCreatedAt(LocalDateTime.now());
        agent.setActive(true);
        
        // Rôle par défaut => ROLE_USER
        Role defaultRole = Role.builder()
                .name("ROLE_USER")
                .agent(agent)
                .build();
        
        agent.setRoles(Arrays.asList(defaultRole));
        
        // UserInfo par défaut
        if (agent.getUserInfo() == null) {
            UserInfo userInfo = UserInfo.builder()
                    .status("active")
                    .adminUser(false)
                    .emailPecVerified(false)
                    .temporalPassword(false)
                    .agent(agent)
                    .build();
            agent.setUserInfo(userInfo);
        } else {
            agent.getUserInfo().setAgent(agent);
        }
        
        // UserAddress
        if (agent.getUserAddress() != null) {
            agent.getUserAddress().setAgent(agent);
        } else {
            UserAddress userAddress = UserAddress.builder()
                    .zipCode(0)
                    .agent(agent)
                    .build();
            agent.setUserAddress(userAddress);
        }
        
        Agent savedAgent = agentRepository.save(agent);
        log.info("Agent créé avec succès avec l'ID: {}", savedAgent.getUserId());
        return savedAgent;
    }
    
    // Met a jour un Agent
    public Agent updateAgent(String userId, Agent agentDetails) {
        log.info("Mise à jour de l'agent avec l'ID: {}", userId);
        
        Agent existingAgent = agentRepository.findByUserIdWithRelations(userId)
                .orElseThrow(() -> new RuntimeException("Agent not found with ID: " + userId));
        
        // Vérifier l'unicité du username et email (sauf pour l'agent actuel)
        if (!existingAgent.getUsername().equals(agentDetails.getUsername()) 
            && agentRepository.existsByUsername(agentDetails.getUsername())) {
            throw new RuntimeException("Username already exists: " + agentDetails.getUsername());
        }
        
        if (!existingAgent.getEmail().equals(agentDetails.getEmail()) 
            && agentRepository.existsByEmail(agentDetails.getEmail())) {
            throw new RuntimeException("Email already exists: " + agentDetails.getEmail());
        }
        
        // Mise à jour des champs de base
        existingAgent.setUsername(agentDetails.getUsername());
        existingAgent.setFullName(agentDetails.getFullName());
        existingAgent.setGender(agentDetails.getGender());
        existingAgent.setEmail(agentDetails.getEmail());
        existingAgent.setEmailPec(agentDetails.getEmailPec());
        existingAgent.setDateOfBirth(agentDetails.getDateOfBirth());
        existingAgent.setActive(agentDetails.getActive());
        existingAgent.setLastLogin(LocalDateTime.now());
        
        // Mise à jour des rôles - DELETE ALL PREVIOUS ROLES FIRST
        if (agentDetails.getRoles() != null) {
            log.info("Mise à jour des rôles pour l'agent: {}", userId);
            
            // DELETE ALL PREVIOUS ROLES by clearing the collection
            // This works because of orphanRemoval = true in the Agent entity
            if (existingAgent.getRoles() != null && !existingAgent.getRoles().isEmpty()) {
                log.info("Suppression de {} anciens rôles", existingAgent.getRoles().size());
                existingAgent.getRoles().clear(); // This will trigger deletion due to orphanRemoval
            }
            
            // Ajouter les nouveaux rôles
            List<Role> newRoles = agentDetails.getRoles().stream()
                    .map(roleDetail -> {
                        Role newRole = new Role();
                        newRole.setName(roleDetail.getName());
                        newRole.setAgent(existingAgent);
                        return newRole;
                    })
                    .collect(Collectors.toList());
            
            existingAgent.getRoles().addAll(newRoles);
            
            log.info("{} nouveaux rôles ajoutés: {}", newRoles.size(), newRoles.stream()
                    .map(Role::getName)
                    .collect(Collectors.joining(", ")));
        }
        
        // Mise à jour UserInfo
        if (agentDetails.getUserInfo() != null) {
            UserInfo existingUserInfo = existingAgent.getUserInfo();
            
            // Créer UserInfo si elle n'existe pas
            if (existingUserInfo == null) {
                existingUserInfo = UserInfo.builder()
                        .agent(existingAgent)
                        .status("active")
                        .adminUser(false)
                        .emailPecVerified(false)
                        .temporalPassword(false)
                        .build();
                existingAgent.setUserInfo(existingUserInfo);
            }
            
            // Mettre à jour les champs UserInfo
            existingUserInfo.setStatus(agentDetails.getUserInfo().getStatus());
            existingUserInfo.setDeleteDate(agentDetails.getUserInfo().getDeleteDate());
            existingUserInfo.setAdminUser(agentDetails.getUserInfo().getAdminUser());
            existingUserInfo.setEmailPecVerified(agentDetails.getUserInfo().getEmailPecVerified());
            existingUserInfo.setTemporalPassword(agentDetails.getUserInfo().getTemporalPassword());
        }
        
        // Mise à jour UserAddress
        if (agentDetails.getUserAddress() != null) {
            UserAddress existingUserAddress = existingAgent.getUserAddress();
            
            // Créer UserAddress si elle n'existe pas
            if (existingUserAddress == null) {
                existingUserAddress = UserAddress.builder()
                        .agent(existingAgent)
                        .zipCode(0)
                        .build();
                existingAgent.setUserAddress(existingUserAddress);
            }
            
            // Mettre à jour les champs UserAddress
            existingUserAddress.setCountry(agentDetails.getUserAddress().getCountry());
            existingUserAddress.setState(agentDetails.getUserAddress().getState());
            existingUserAddress.setAddressLine(agentDetails.getUserAddress().getAddressLine());
            existingUserAddress.setZipCode(agentDetails.getUserAddress().getZipCode());
        }
        
        Agent updatedAgent = agentRepository.save(existingAgent);
        log.info("Agent mis à jour avec succès: {}", updatedAgent.getUserId());
        return updatedAgent;
    }
    
    // Supprime un agent 
    public boolean deleteAgent(String userId) {
        log.info("Suppression de l'agent avec l'ID: {}", userId);
        
        if (agentRepository.existsById(userId)) {
            agentRepository.deleteById(userId);
            log.info("Agent supprimé avec succès: {}", userId);
            return true;
        } else {
            log.warn("Tentative de suppression d'un agent inexistant: {}", userId);
            return false;
        }
    }
    
    @Transactional(readOnly = true)
    public List<Agent> getActiveAgents() {
        return agentRepository.findByActive(true);
    }
}