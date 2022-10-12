package com.remare.communicationchannels.repository;

import com.remare.communicationchannels.entity.CommunicationDirection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationDirectionRepository
    extends JpaRepository<CommunicationDirection, Long> {}
