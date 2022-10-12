package com.remare.communicationchannels.controller;

import com.remare.communicationchannels.attachment.AttachmentParent;
import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.dto.attachment.AttachmentUploadDataDto;
import com.remare.communicationchannels.http.HttpResponse;
import com.remare.communicationchannels.service.AttachmentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("attachment")
public class AttachmentController {

  private final AttachmentService attachmentService;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
  }

  @PostMapping("/add")
  public AttachmentDto add(
      @RequestParam MultipartFile file,
      @RequestParam Long userId,
      @RequestParam Long attachmentParentId,
      @RequestParam String attachmentParentName)
      throws IOException {

    AttachmentParent parent = attachmentService.resolveParent(attachmentParentName.toUpperCase());
    AttachmentUploadDataDto data = parent.createAttachmentUploadData(attachmentParentId, file);
    data.setUserId(userId);

    parent.createFolderIfAbsent(attachmentParentId);
    parent.checkIfFileExists(attachmentParentId, file);
    parent.copyFileToDestination(attachmentParentId, file);

    return attachmentService.add(data);
  }

  @GetMapping("/{parentName}/{parentId}/file/{fileName}")
  public ResponseEntity<Resource> addLike(
      @PathVariable Long parentId, @PathVariable String fileName, @PathVariable String parentName)
      throws IOException {

    AttachmentParent parent = attachmentService.resolveParent(parentName.toUpperCase());

    ByteArrayResource resource = parent.findFileByParentIdAndFileName(parentId, fileName);

    return ResponseEntity.ok()
        .contentLength(resource.contentLength())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(resource);
  }

  @DeleteMapping("{parentName}/{parentId}/{id}/delete")
  public ResponseEntity<HttpResponse> removeAttachment(
      @PathVariable Long id, @PathVariable String parentName, @PathVariable Long parentId)
      throws IOException {

    AttachmentParent parent = attachmentService.resolveParent(parentName.toUpperCase());
    parent.deleteFile(parentId, attachmentService.findById(id).getOriginalName());

    String attachmentDeleteMessage = attachmentService.deleteById(id);

    HttpResponse response =
        new HttpResponse(200, HttpStatus.OK, "DELETED_FILE", attachmentDeleteMessage);

    return ResponseEntity.ok().body(response);
  }
}
