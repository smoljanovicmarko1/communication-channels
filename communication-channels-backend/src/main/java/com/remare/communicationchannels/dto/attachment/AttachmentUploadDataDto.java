package com.remare.communicationchannels.dto.attachment;

public class AttachmentUploadDataDto {
  private Long userId;
  private Long postId;
  private Long commentId;
  private Long channelId;

  private String fileCreationPath;
  private String originalFileName;

  public static AttachmentUploadDataDto newInstance() {
    return new AttachmentUploadDataDto();
  }

  public AttachmentUploadDataDto withPostId(Long postId) {
    this.postId = postId;
    return this;
  }

  public AttachmentUploadDataDto withCommentId(Long commentId) {
    this.commentId = commentId;
    return this;
  }

  public AttachmentUploadDataDto withChannelId(Long channelId) {
    this.channelId = channelId;
    return this;
  }

  public String getOriginalFileName() {
    return originalFileName;
  }

  public void setOriginalFileName(String originalFileName) {
    this.originalFileName = originalFileName;
  }

  public String getFileCreationPath() {
    return fileCreationPath;
  }

  public void setFileCreationPath(String fileCreationPath) {
    this.fileCreationPath = fileCreationPath;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public Long getCommentId() {
    return commentId;
  }

  public void setCommentId(Long commentId) {
    this.commentId = commentId;
  }

  public Long getChannelId() {
    return channelId;
  }

  public void setChannelId(Long channelId) {
    this.channelId = channelId;
  }
}
