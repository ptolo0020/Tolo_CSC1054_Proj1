/*Project 1054 Main
   Tolo, Peter
   */

import javafx.event.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.canvas.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.stage.*;
import java.util.*;
import javafx.scene.paint.Color;
import java.io.*;

public class MazeRunner extends Application
{
   //scanner to read in file
   Scanner scan = new Scanner(System.in);
   
   //Root flowpane, Mazereader is the canvas
   FlowPane root = new FlowPane();
   MazeReader mzr;
   
   //create stage outside so both KeyHandler and start method can use it
   Stage stage = new Stage();
   
   //start method 
   public void start(Stage stage)
   {
      //instantiate stage from outside to @param
      this.stage = stage; 
      
      //get file, use as @param for mazeReader canvas
      System.out.print("Enter maze file: ");
      mzr = new MazeReader(scan.next());
      
      //setup root + add canvas
      root.setPrefSize(525,525);
      root.getChildren().add(mzr);
      
      //setup keyListener
      mzr.setOnKeyPressed(new KeyListener());
      
      //create scene/set stage
      Scene scene = new Scene(root, 525, 525);
      stage.setScene(scene);
      stage.setTitle("Press \"R\" to start MazeRunning");
      stage.show(); 
      
      //Canvas requests focus of buttons
      mzr.requestFocus();  
   }
   
   //KeyListener listens for arrowkeys for movement, and R and S to start/stop running program    
   public class KeyListener implements EventHandler<KeyEvent>
   {
      public void handle(KeyEvent e)
      {
         if(e.getCode()==KeyCode.UP)
            mzr.moveUp();
         if(e.getCode()==KeyCode.DOWN)
            mzr.moveDown();
         if(e.getCode()==KeyCode.LEFT)
            mzr.moveLeft();
         if(e.getCode()==KeyCode.RIGHT)
            mzr.moveRight();
         
         if(e.getCode()==KeyCode.R)
         {
            //we can access stage here because we created it in parent class
            mzr.activateRun();
            stage.setTitle("You are now running. Press \"S\" to stop. ");
         }
         
         if(e.getCode()==KeyCode.S)
         {
            mzr.stopRun();
            stage.setTitle("No longer running. Press \"R\" to start running again. ");
         }
         
      }
   }
   
   //launch in main
   public static void main(String [] args)
   {
      launch(args);
   }
}