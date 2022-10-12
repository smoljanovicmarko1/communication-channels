package com.remare.communicationchannels.constant;

import java.io.File;

public class FileConstant {

  public static final String CHANNEL_FOLDER = "channel" + File.separator;
  public static final String USER_FOLDER = "user" + File.separator;
  public static final String POST_FOLDER = "post" + File.separator;
  public static final String COMMENT_FOLDER = "comment" + File.separator;

  public static final String ATTACHMENTS_FOLDER_NAME = "attachments";
  public static final String PROFILE_FOLDER_NAME = "profile";

  public static final String ASSETS_FOLDER =
      System.getProperty("user.dir")
          + File.separator
          + "src"
          + File.separator
          + "main"
          + File.separator
          + "resources"
          + File.separator
          + "assets"
          + File.separator;

  public static final String DEFAULT_PROFILE_IMAGE_PATH =
      "default" + File.separator + "default-user-profile-image.png";
}
