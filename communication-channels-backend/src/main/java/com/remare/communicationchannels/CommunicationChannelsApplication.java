package com.remare.communicationchannels;

import com.remare.communicationchannels.constant.FileConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@SuppressWarnings("ResultOfMethodCallIgnored")
@SpringBootApplication
@EnableJpaAuditing
public class CommunicationChannelsApplication {

  public static void main(String[] args) {
    SpringApplication.run(CommunicationChannelsApplication.class, args);
    new File(FileConstant.USER_FOLDER).mkdirs();
    new File(FileConstant.CHANNEL_FOLDER).mkdirs();
    new File(FileConstant.POST_FOLDER).mkdirs();
  }
}
