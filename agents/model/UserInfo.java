package com.example.agents.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @Builder.Default
    private String status = "active";
    
    @Column(name = "delete_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteDate;
    
    @Column(name = "admin_user", nullable = false)
    @Builder.Default
    private Boolean adminUser = false;
    
    @Column(name = "email_pec_verified", nullable = false)
    @Builder.Default
    private Boolean emailPecVerified = false;
    
    @Column(name = "temporal_password", nullable = false)
    @Builder.Default
    private Boolean temporalPassword = false;
    
    // Relations
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_user_id", referencedColumnName = "user_id")
    @JsonIgnore
    private Agent agent;
}