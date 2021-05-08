/* Project 1054 Canvas
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
import javafx.scene.text.*;
import javafx.animation.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.stage.*;
import java.util.*;
import java.io.*;
import javafx.scene.paint.Color;

//parent class extends canvas
public class MazeReader extends Canvas
{  
   //create array to hold block data/color in 21x21 array
   //xCord/yCord keep track of player placement, endPlace keeps track of end goal
   //running keeps track of running program state
   int [][] MRArray = new int [21][21];
   int xCord, yCord = 0, endPlace; 
   boolean running = false; 
   
   //graphicsContext created out here so both draw() and erase() can use it
   //Random gen for running extra add-on
   GraphicsContext gc = getGraphicsContext2D();
   Random gen = new Random();
   
   //constructor takes in the file from client
   public MazeReader(String fileName)
   {
      //set up canvas size
      setWidth(525);
      setHeight(525);
      
      //try and catch FileNotFoundExceptions
      try
      {
         //new read scanner to read in from file, reads into array
         Scanner read = new Scanner(new File(fileName));
         
         for(int i = 0; i<MRArray.length; i++)//rows
            for(int j = 0; j<MRArray[i].length; j++)//columns
               MRArray[j][i] = read.nextInt();
      
      }catch(FileNotFoundException fnfe)
      {
         System.out.println("File not found");
      }

      //draws() all inputs from array
      for(int i = 0; i<MRArray.length; i++)//rows
         for(int j = 0; j<MRArray[i].length; j++)//columns
            draw(MRArray[i][j], i, j);
      
      //finds/sets starting and ending points of player over a loop
      for(int i = 0; i<MRArray[0].length; i++)
      {
         //starting point, draw player
         if (MRArray[i][0] == 0)
         {
            draw(2,i,0);
            setX(i);
         }
         
         //keep track of ending point
         if (MRArray[i][MRArray.length-1] == 0)
            endPlace = i; 
      }
      
   }
   
   //draw method looks for color based on input int and fillsRect at x and y coords
   public void draw(int color, int x, int y)
   {
      if(color == 0)//0=pathway
         gc.setFill(Color.WHITE);
      else if(color == 1)//1=wall
         gc.setFill(Color.BLACK);
      else if (color == 2)//2=player
         gc.setFill(Color.CYAN);
      
      gc.fillRect(x*25,y*25, 25, 25);
      
   }
   //erase() cleans up after player moves so trail isn't left behind
   public void erase(int x, int y)
   {
      gc.setFill(Color.WHITE);
      gc.fillRect(x*25, y*25, 25, 25);
      
   }
   
   //mutators for player's X and Y coords
   public void setX(int newX)
   {
      xCord = newX; 
   }
   
   public void setY(int newY)
   {
      yCord = newY; 
   }
   
   //move() methods try and catch OutOfBounds in case walls aren't set up in file and so won't go off screen
   public void moveUp()
   {
      try
      {
         //looks for available white space in direction player is trying to go
         if(MRArray[xCord][yCord-1] == 0)
         {
            //if white space, then erase player's previous coords, change them, and redraw the player at new position
            erase(xCord, yCord);
            yCord--;
            draw(2, xCord, yCord);
         }
      }catch(IndexOutOfBoundsException ioob)
      {
      }
      
      //If running, have doRun method execute every time the player tries to move or actually moves
      if(running)
         doRun();
      }
   
   public void moveDown()
   {
      try
      {
         if(MRArray[xCord][yCord+1] == 0)
         {
            erase(xCord, yCord);
            yCord++;
            draw(2, xCord, yCord);
         }
      }catch(IndexOutOfBoundsException ioob)
      {
      }
      
      if(running)
         doRun();
      
      //celebrate() only on down because ending position will always be at the bottom 
      //and player will always have to move down to get there  
      //compare xCord with endingPlace just in case 
      if(yCord == 20 && xCord == endPlace)
         celebrate();
   }
   
   public void moveLeft()
   {
      try
      {
         if(MRArray[xCord-1][yCord] == 0)
         {
            erase(xCord, yCord);
            xCord--;
            draw(2, xCord, yCord);
         }
      }catch(IndexOutOfBoundsException ioob)
      {
      }
      
      if(running)
         doRun();
   }
   
   public void moveRight()
   {
      try
      {
         if(MRArray[xCord+1][yCord] == 0)
         {
            erase(xCord, yCord);
            xCord++;
            draw(2, xCord, yCord);
         }
      }catch(IndexOutOfBoundsException ioob)
      {
      }
      
      if(running)
         doRun();

   }
   
   //celebrate() clears screen and shows text that player won
   public void celebrate()
   {
      gc.clearRect(0,0,525,525);
      gc.setFill(Color.CYAN);
      gc.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 40));
      gc.fillText("You Win!", 170, 250);
   }
   
   //mutator activates run program
   public void activateRun()
   {
      running = true; 
   }
   
   //mutator stops run program
   public void stopRun()
   {
      running = false;
   }
   
   //doRun() changes a random block's state anytime the player attempts to move
   public void doRun()
   {
      //randomize positions X and Y, but not the last row of Y so endingPlace won't get messed up
      int x = gen.nextInt(21);
      int y = gen.nextInt(20);
      
      //checks to make sure the player isn't in the position that random wants to change
      if(!(x==xCord && y==yCord))
      {
         //if the position is white, change to black; if black, change to white
         //both alters array and draws accordingly
         if(MRArray[x][y] == 0)
         {
            MRArray[x][y] = 1; 
            draw(1, x, y);
         }
         else if (MRArray[x][y] == 1)
         {
            MRArray[x][y] = 0; 
            draw(0, x, y); 
         }
      }
      
   }
   
   
}
