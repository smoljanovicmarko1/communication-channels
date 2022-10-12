package com.remare.communicationchannelsauthorizationserver.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String username;
  private String password;

  @ManyToOne
  @JoinColumn(name = "user_role_id")
  private UserRole role;
}
