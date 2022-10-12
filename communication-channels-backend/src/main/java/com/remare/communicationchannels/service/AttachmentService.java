package com.remare.communicationchannels.service;

import com.remare.communicationchannels.attachment.AttachmentParent;
import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.dto.attachment.AttachmentUploadDataDto;

import java.io.IOException;

public interface AttachmentService {

  AttachmentDto add(AttachmentUploadDataDto attachmentUploadDataDto) throws IOException;

  AttachmentParent resolveParent(String attachmentParentName);

  String deleteById(Long id);

  AttachmentDto findById(Long id);
}
