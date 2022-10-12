package com.remare.communicationchannels.service.impl;

import com.remare.communicationchannels.attachment.AttachmentParent;
import com.remare.communicationchannels.constant.AttachmentParentConstant;
import com.remare.communicationchannels.dto.attachment.AttachmentDto;
import com.remare.communicationchannels.dto.attachment.AttachmentUploadDataDto;
import com.remare.communicationchannels.entity.*;
import com.remare.communicationchannels.exception.user.UserNotFoundException;
import com.remare.communicationchannels.mapper.AttachmentMapper;
import com.remare.communicationchannels.mapper.AttachmentRepository;
import com.remare.communicationchannels.repository.ChannelRepository;
import com.remare.communicationchannels.repository.CommentRepository;
import com.remare.communicationchannels.repository.PostRepository;
import com.remare.communicationchannels.repository.UserRepository;
import com.remare.communicationchannels.service.AttachmentService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ChannelRepository channelRepository;
    private final CommentRepository commentRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper, UserRepository userRepository, PostRepository postRepository, ChannelRepository channelRepository, CommentRepository commentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.channelRepository = channelRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public AttachmentDto add(AttachmentUploadDataDto attachmentUploadDataDto) throws IOException {
        Post post = attachmentUploadDataDto.getPostId() != null ? postRepository.findById(attachmentUploadDataDto.getPostId()).orElseThrow() : null;
        Comment comment = attachmentUploadDataDto.getCommentId() != null ? commentRepository.findById(attachmentUploadDataDto.getCommentId()).orElseThrow() : null;
        Channel channel = attachmentUploadDataDto.getChannelId() != null ? channelRepository.findById(attachmentUploadDataDto.getChannelId()).orElseThrow() : null; // TODO: Add exceptions
        User user = userRepository.findById(attachmentUploadDataDto.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User with id not found")
        );

        String fileCreationPath = attachmentUploadDataDto.getFileCreationPath();

        Attachment newAttachment = new Attachment();
        newAttachment.setUser(user);
        newAttachment.setPost(post);
        newAttachment.setChannel(channel);
        newAttachment.setComment(comment);
        newAttachment.setOriginalName(attachmentUploadDataDto.getOriginalFileName());
        newAttachment.setUrl(fileCreationPath);

        System.out.println("new Attachment postFolder.toAbsolutePath().toString(): " + newAttachment.getUrl());

        Attachment savedAttachment = attachmentRepository.save(newAttachment);
        return attachmentMapper.toDto(savedAttachment);
    }


    @Override
    public AttachmentParent resolveParent(String attachmentParentName) {
        switch (attachmentParentName) {
            case AttachmentParentConstant
                    .POST -> {
                return AttachmentParent.POST;
            }
            case AttachmentParentConstant
                    .CHANNEL -> {
                return AttachmentParent.CHANNEL;
            }
            case AttachmentParentConstant
                    .COMMENT -> {
                return AttachmentParent.COMMENT;
            }
            default -> throw new IllegalArgumentException("Invalid attachment parent");
        }
    }

    @Override
    public String deleteById(Long id) {
        attachmentRepository.deleteById(id);
        return "Attachment successfully deleted";
    }

    @Override
    public AttachmentDto findById(Long id) {
        return attachmentMapper.toDto(attachmentRepository.findById(id).orElseThrow()); // TODO add exception
    }
}
