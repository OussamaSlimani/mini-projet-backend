package com.example.agents.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agent {
    
    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;
    
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Username is required")
    private String username;
    
    @Column(name = "full_name", nullable = false)
    @NotBlank(message = "Full name is required")
    private String fullName;
    
    @Column(nullable = false)
    @NotBlank(message = "Gender is required")
    private String gender;
    
    @Column(unique = true, nullable = false)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Column(name = "email_pec")
    @Email(message = "Email PEC should be valid")
    private String emailPec;
    
    @Column(name = "date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    @Column(name = "last_login")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
    
    // Relations
    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Role> roles;
    
    @OneToOne(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserInfo userInfo;
    
    @OneToOne(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserAddress userAddress;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}