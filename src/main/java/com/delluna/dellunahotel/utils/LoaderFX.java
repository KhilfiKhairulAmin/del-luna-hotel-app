package com.delluna.dellunahotel.utils;

import java.net.URL;

import javafx.scene.image.Image;

public class LoaderFX {
  public static URL getFXML(String FXMLfilename) {
    return LoaderFX.class.getResource("/com/delluna/dellunahotel/views/" + FXMLfilename);
  }

  public static String getCSS(String CSSfilename) {
    return LoaderFX.class.getResource("/com/delluna/dellunahotel/styles/" + CSSfilename).toExternalForm();
  }

  public static Image getImage(String imageFilename) {
    return new Image(LoaderFX.class.getResource("/com/delluna/dellunahotel/images/" + imageFilename).toExternalForm(), false);
  }

  public static Image getImage(String imageFilename, String[] folders) {
    String folderPath = String.join("/", folders) + "/";
    return new Image(LoaderFX.class.getResource("/com/delluna/dellunahotel/images/" + folderPath + imageFilename).toExternalForm(), false);
  }
}