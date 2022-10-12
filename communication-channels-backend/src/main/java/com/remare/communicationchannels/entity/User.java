package com.remare.communicationchannels.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "user")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "date_created")
  private Date dateCreated;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "is_account_non_expired")
  private boolean isAccountNonExpired;

  @Column(name = "is_credentials_non_expired")
  private boolean isCredentialsNonExpired;

  @Column(name = "is_enabled")
  private boolean isEnabled;

  @Column(name = "is_account_non_locked")
  private boolean isAccountNonLocked;

  @Column(name = "profile_picture")
  private String profilePictureUrl;

  @ManyToOne
  @JoinColumn(name = "user_role_id")
  private UserRole role;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "favorites",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "post_id"))
  private List<Post> favorites;

  public List<UserPermission> getUserPermissions() {
    return role.getUserPermissions();
  }
}
