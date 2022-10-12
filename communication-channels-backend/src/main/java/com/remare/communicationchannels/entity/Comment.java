package com.remare.communicationchannels.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "text")
  private String text;

  @Column(name = "date_created")
  @CreatedDate
  private Date dateCreated;

  @Column(name = "date_last_modified")
  @LastModifiedDate
  private Date dateLastModified;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "comment_status_id")
  private CommentStatus commentStatus;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private List<Like> likes;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private List<Attachment> attachments;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private List<Comment> comments;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private Comment comment;
}
