package fr.themsou.utils;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.util.Arrays;

public class Builders {

    public static ImageView buildImage(String imgPath, int width, int height) {
        ImageView imageView = new ImageView(new Image(imgPath));

        if(width == 0 && height == 0) return imageView;

        if(width == 0){
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
        }else if(height == 0){
            imageView.setFitWidth(width);
            imageView.setPreserveRatio(true);
        }else{
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
        }
        return imageView;
    }
    public static ImageView buildImage(FileInputStream file, int width, int height) {
        ImageView imageView = new ImageView(new Image(file));

        if(width == 0 && height == 0) return imageView;

        if(width == 0){
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
        }else if(height == 0){
            imageView.setFitWidth(width);
            imageView.setPreserveRatio(true);
        }else{
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
        }
        return imageView;
    }

    public static void setPosition(Region element, double x, double y, double width, double height, boolean force){

        if(x >= 0){
            element.setLayoutX(x);
        }if(y >= 0){
            element.setLayoutY(y);
        }
        element.setPrefSize(width, height);

        if(force){
            element.setStyle("-fx-min-width: " + width + ";");
            element.setStyle("-fx-min-height: " + height + ";");
            element.minWidthProperty().bind(new SimpleDoubleProperty(width));
            element.minHeightProperty().bind(new SimpleDoubleProperty(height));
        }
    }

    public static void setHBoxPosition(Region element, double width, double height, Insets margin){

        if(width == -1){
            HBox.setHgrow(element, Priority.ALWAYS);
            element.setMaxWidth(Double.MAX_VALUE);
        }else if(width != 0){
            element.setPrefWidth(width);
            element.minWidthProperty().bind(new SimpleDoubleProperty(width));
        }
        if(height == -1){
            VBox.setVgrow(element, Priority.ALWAYS);
        }else if(height != 0){
            element.setPrefHeight(height);
            element.minHeightProperty().bind(new SimpleDoubleProperty(height));
        }
        HBox.setMargin(element, margin);
    }
    public static void setHBoxPosition(Region element, double width, double height, double margin){
        setHBoxPosition(element, width, height, new Insets(margin, margin, margin, margin));
    }
    public static void setHBoxPosition(Region element, double width, double height, double marginLeftRight, double marginTopBottom){
        setHBoxPosition(element, width, height, new Insets(marginTopBottom, marginLeftRight, marginTopBottom, marginLeftRight));
    }

    public static void setVBoxPosition(Region element, double width, double height, Insets margin){

        if(width == -1){
            HBox.setHgrow(element, Priority.ALWAYS);
            element.setMaxWidth(Double.MAX_VALUE);
        }else if(width != 0){
            element.setPrefWidth(width);
            element.minWidthProperty().bind(new SimpleDoubleProperty(width));
        }
        if(height == -1){
            VBox.setVgrow(element, Priority.ALWAYS);
        }else if(height != 0){
            element.setPrefHeight(height);
            element.minHeightProperty().bind(new SimpleDoubleProperty(height));
        }
        VBox.setMargin(element, margin);
    }
    public static void setVBoxPosition(Region element, double width, double height, double margin){
        setVBoxPosition(element, width, height, new Insets(margin, margin, margin, margin));
    }
    public static void setVBoxPosition(Region element, double width, double height, double marginLeftRight, double marginTopBottom){
        setVBoxPosition(element, width, height, new Insets(marginTopBottom, marginLeftRight, marginTopBottom, marginLeftRight));
    }

    public static void setMenuSize(Menu menu){

        for(MenuItem subMenu : menu.getItems()){
            subMenu.setStyle("-fx-font-size: 13;");
            if(subMenu instanceof Menu){
                setMenuSize((Menu) subMenu);
            }
        }
    }
    public static void setMenuSize(ContextMenu menu){

        for(MenuItem subMenu : menu.getItems()){
            subMenu.setStyle("-fx-font-size: 13;");
            if(subMenu instanceof Menu){
                setMenuSize((Menu) subMenu);
            }
        }
    }

    public static void secureAlert(Dialog alert){
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Builders.class.getResource("/logo.png")+""));
        alert.setOnShowing(new EventHandler<DialogEvent>() {
            @Override public void handle(DialogEvent e) {
                new Thread(new Runnable() {
                    @Override  public void run() {

                        try{
                            Thread.sleep(400);
                        }catch(InterruptedException ex){ ex.printStackTrace();  }

                        Platform.runLater(new Runnable(){
                            @Override public void run(){
                                if(alert.isShowing()){
                                    if(alert.getDialogPane().getScene().getWindow().getWidth() < 100){
                                        alert.getDialogPane().getScene().getWindow().setWidth(500);
                                        alert.getDialogPane().getScene().getWindow().setHeight(200);
                                    }
                                }
                            }
                        });

                    }
                }, "AlertResizer").start();
            }
        });
    }

    public static String[] cleanArray(String[] array) {
        return Arrays.stream(array).filter(x -> !x.isBlank()).toArray(String[]::new);
    }

    public static Tooltip genToolTip(String text){
        return new Tooltip(new TextWrapper(text, null, 350).wrap());

    }


}
