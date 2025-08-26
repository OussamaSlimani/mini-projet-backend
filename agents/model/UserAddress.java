package com.example.agents.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user_address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String country;
    
    private String state;
    
    @Column(name = "address_line")
    private String addressLine;
    
    @Column(name = "zip_code")
    @Builder.Default
    private Integer zipCode = 0;
    
    // Relations
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_user_id", referencedColumnName = "user_id")
    @JsonIgnore
    private Agent agent;
}