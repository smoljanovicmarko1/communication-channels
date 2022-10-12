package com.remare.communicationchannels.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "account_activation_token")
@Data
public class AccountActivationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "token")
  private String token;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
