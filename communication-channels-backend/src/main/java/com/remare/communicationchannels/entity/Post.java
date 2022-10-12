package com.remare.communicationchannels.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "body")
  private String body;

  @Column(name = "date_created")
  @CreatedDate
  private Date dateCreated;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "channel_id")
  private Channel channel;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id")
  private List<Like> likes;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private List<Comment> comments;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private List<Attachment> attachments;

  @Column(name = "edited")
  private boolean edited;
}
