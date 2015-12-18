import java.util.Random;
import java.util.ArrayList;

public class MyAgent extends Agent
{
    Random r;
    private String playerName;
    private int numCols = myGame.getColumnCount(); //The number of columns in the game board
    private int numRows = myGame.getRowCount(); //The number of rows in the game board
    

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        this.setName("Duckula");
        //r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {

        final int CLOSE_TO_WIN = 2; //Two tokens in a line
        final int CAN_WIN = 3; //three tokens in a line
        
        int iCanWin = playerCanWin(iAmRed, CAN_WIN);
        int theyCanWin = playerCanWin(!iAmRed, CAN_WIN);
        int iAmCloseToWin = playerCanWin(iAmRed, CLOSE_TO_WIN);
        int theyAreCloseToWin = playerCanWin(!iAmRed, CLOSE_TO_WIN);
        

        if(iCanWin != -1)
        {
            System.out.println("I can win on column " + iCanWin);
            moveOnColumn(iCanWin);
            return;
        }
            
        if(theyCanWin != -1)
        {
             System.out.println("They can win on column " + theyCanWin);
             moveOnColumn(theyCanWin);
             return;
        }
            
        if(theyAreCloseToWin != -1)
        {
            ArrayList arr = positionToAvoid(iAmRed);
            System.out.println("Column List To Avoid= "+ arr.toString());
            if (arr.indexOf(theyAreCloseToWin) != -1)
            {
                System.out.println("Don't move to " + theyAreCloseToWin);
            }
            else
            {
                System.out.println("They Are Close To Win on Column " + theyAreCloseToWin);
                moveOnColumn(theyAreCloseToWin);
                return;
            }
        }
            
        if(iAmCloseToWin != -1)
        {
            ArrayList arr = positionToAvoid(iAmRed);
            System.out.println("Column List To Avoid = "+ arr.toString());
            if (arr.indexOf(iAmCloseToWin) != -1)
            {
                System.out.println("Don't move to " + iAmCloseToWin);
            }
            else
            {
                System.out.println("I Am Close To Win on Column " + iAmCloseToWin);
                moveOnColumn(iAmCloseToWin);
                return;
            }
        }

        System.out.println("Random Move");
        int rand = randomMove();
        ArrayList arr = positionToAvoid(iAmRed);
        System.out.println("Column List To Avoid = "+ arr.toString());
        int count = 0;
        
        while(arr.indexOf(rand) != -1 && count < numCols)
        {
            rand = randomMove();
            System.out.println("change To Column "+rand);
            count += 1;
        }     
            
        moveOnColumn(rand);
        return;            
        
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }
    

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        r = new Random();
        int i = r.nextInt(numCols);
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(numCols);
        }
        return i;
    }

    /**
     * Returns the column that would allow the Palyer to win or one step away from winnning.
     * 
     * Check to see if Player has a winning move available to it. Implement this method to return what column would
     * allow the Player to win or one step away from winning.
     * @param iAmRed If the color of the player are red. 
     * @param numberInLine Number of token in a line. If you want to check if the player can win in one step, set it to 3. If you want to check if the player can win in two steps, set it to 2.
     * @return the column that would allow the Palyer to win, return -1 if no column can win or is one step away from winning.
     */
    
    public int playerCanWin(boolean iAmRed, int numberInLine)
    {
        int colorSum;
        int filledSum = numberInLine;
        if(iAmRed)
        {
            colorSum = numberInLine;
        }
        else
        {
            colorSum = 0;
        }
        
        for (int i = 0; i < numCols; i++)
        {
            
            for (int j = numRows - 1; j >= 0; j--)
            {
                  if (j - 3 >= 0)
                  {
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i).getSlot(j-1).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i).getSlot(j-2).getIsRed()?1:0; 
                        
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i).getSlot(j-1).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i).getSlot(j-2).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i).getSlot(j-3).getIsFilled()?1:0;
                        
                        if(pos1filled + pos2filled + pos3filled + pos4filled == filledSum && pos1Color + pos2Color + pos3Color == colorSum) //If two or three tokens with same colors are on a vertical line
                        {
                            return i;
                        }
                  }
                  
                  if (i + 3 < numCols)
                  {
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i+1).getSlot(j).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i+2).getSlot(j).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i+3).getSlot(j).getIsFilled()?1:0;
                        int[] posfilled = new int[]{pos1filled, pos2filled, pos3filled, pos4filled};
                        
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i+1).getSlot(j).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i+2).getSlot(j).getIsRed()?1:0;
                        int pos4Color = myGame.getColumn(i+3).getSlot(j).getIsRed()?1:0;
                        
                        if (pos1filled + pos2filled + pos3filled + pos4filled == filledSum && pos1Color + pos2Color + pos3Color + pos4Color == colorSum) //If two or three tokens with same colors are on a horizontal line
                        {
                            for(int ii = 0; ii < 4; ii++)
                            {
                                if(posfilled[ii] == 0)
                                {
                                    if(j == numRows - 1 || (j < numRows - 1 && myGame.getColumn(i+ii).getSlot(j+1).getIsFilled())) // If the slot under the unfilled slot is filled
                                    {
                                        return i+ii;
                                    }
  
                                }
                            }
                        }
                  }
                  
                  if (i + 3 < numCols && j - 3 >= 0)
                  {
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i+1).getSlot(j-1).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i+2).getSlot(j-2).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i+3).getSlot(j-3).getIsFilled()?1:0;
                        int[] posfilled = new int[]{pos1filled, pos2filled, pos3filled, pos4filled};
                        
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i+1).getSlot(j-1).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i+2).getSlot(j-2).getIsRed()?1:0;
                        int pos4Color = myGame.getColumn(i+3).getSlot(j-3).getIsRed()?1:0;
                        
                        if (pos1filled + pos2filled + pos3filled + pos4filled == filledSum && pos1Color + pos2Color + pos3Color + pos4Color == colorSum) //If two or three tokens with same colors are on a anti-diagonal line
                        {
                            for(int ii = 0; ii < 4; ii++)
                            {
                                if(posfilled[ii] == 0)
                                {
                                    if(j - ii == numRows - 1 || (j - ii < numRows - 1  && myGame.getColumn(i+ii).getSlot(j-ii+1).getIsFilled())) // If the slot under the unfilled slot is filled
                                    {
                                        return i+ii;
                                    }
                                }
                            }
                        }
                  }
                  
                  if (i >= 3 && j - 3 >= 0)
                  {
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i-1).getSlot(j-1).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i-2).getSlot(j-2).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i-3).getSlot(j-3).getIsFilled()?1:0;
                        int[] posfilled = new int[]{pos1filled, pos2filled, pos3filled, pos4filled};
                        
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i-1).getSlot(j-1).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i-2).getSlot(j-2).getIsRed()?1:0;
                        int pos4Color = myGame.getColumn(i-3).getSlot(j-3).getIsRed()?1:0;
                        
                        if (pos1filled + pos2filled + pos3filled + pos4filled == filledSum && pos1Color + pos2Color + pos3Color + pos4Color == colorSum) //If two or three tokens with same colors are on a diagonal line
                        {
                            for(int ii = 0; ii < 4; ii++)
                            {
                                if(posfilled[ii] == 0)
                                {
                                    if(j - ii == numRows - 1 || (j - ii < numRows - 1 && myGame.getColumn(i-ii).getSlot(j-ii+1).getIsFilled())) // If the slot under the unfilled slot is filled
                                    {
                                        return i-ii;
                                    }
                                }
                            }
                        }
                  }
            }
        }
        return -1;
    }
    
    /**
     * Returns the column that the agent should avoid.
     * 
     * Check to see if the column the agent is going to fill should be avoided, otherwise opponent will win.
     * 
     * Implement this method to return which column the agent should avoid.
     * 
     * @param iAmRed If the color of the agent is red
     *
     * @return the column that the agent should avoid.
     */
    
    public ArrayList positionToAvoid(boolean iAmRed)
    {
        int colorSum;
        if(iAmRed)
        {
            colorSum = 0;  //Three yellow tokens in a row
        }
        else
        {
            colorSum = 3;  //Three red tokens in a row
        }
        
        ArrayList<Integer> colToAvoid = new ArrayList<Integer>(); //list of column should be avoided.

        for (int i = 0; i < numCols; i++)
        {
            for (int j = numRows - 1; j >= 0; j--)
            {                                    
                  if (i + 3 < numCols)
                  {
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i+1).getSlot(j).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i+2).getSlot(j).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i+3).getSlot(j).getIsFilled()?1:0;
                        int[] posfilled = new int[]{pos1filled, pos2filled, pos3filled, pos4filled};
                        
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i+1).getSlot(j).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i+2).getSlot(j).getIsRed()?1:0;
                        int pos4Color = myGame.getColumn(i+3).getSlot(j).getIsRed()?1:0;
                        
                        if (pos1filled + pos2filled + pos3filled + pos4filled == 3 && pos1Color + pos2Color + pos3Color + pos4Color == colorSum) //If three tokens with opponent color are on a horizontal line
                        {
                            for(int ii = 0; ii < 4; ii++)
                            {
                                if(posfilled[ii] == 0)
                                {
                                    if(j == numRows - 1) // If slot j-i is on the bottom of the panel.
                                    {
                                        continue;
                                    }
                                    else if(myGame.getColumn(i+ii).getSlot(j+1).getIsFilled() == false && j + 1 == numRows - 1) // If slot j is unfilled, j+1 is unfilled and also is the bottom of the panel.
                                    {
                                        colToAvoid.add(i+ii);
                                    }
                                    else if(myGame.getColumn(i+ii).getSlot(j+1).getIsFilled() == false && myGame.getColumn(i+ii).getSlot(j+2).getIsFilled() == true) // If slot j is unfilled, j+1 is unfilled and j+2 is filled.
                                    {
                                        colToAvoid.add(i+ii);
                                    }
  
                                }
                            }
                        }
                  }
                  
                  if (i + 3 < numCols && j - 3 >= 0)
                  {
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i+1).getSlot(j-1).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i+2).getSlot(j-2).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i+3).getSlot(j-3).getIsFilled()?1:0;
                        int[] posfilled = new int[]{pos1filled, pos2filled, pos3filled, pos4filled};
                        
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i+1).getSlot(j-1).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i+2).getSlot(j-2).getIsRed()?1:0;
                        int pos4Color = myGame.getColumn(i+3).getSlot(j-3).getIsRed()?1:0;
                        
                        if (pos1filled + pos2filled + pos3filled + pos4filled == 3 && pos1Color + pos2Color + pos3Color + pos4Color == colorSum) //If three tokens with opponent color are on a anti-diagonal line
                        {
                            for(int ii = 0; ii < 4; ii++)
                            {
                                if(posfilled[ii] == 0)
                                {
                                    if(j - ii == numRows - 1) // If slot j-ii is on the bottom of the panel.
                                    {
                                        continue;
                                    }
                                    else if(myGame.getColumn(i+ii).getSlot(j-ii+1).getIsFilled() == false && j -ii + 1 == numRows - 1) //If slot j-ii is unfilled, j-ii+1 is unfilled and also is the bottom of the panel.
                                    {
                                        colToAvoid.add(i+ii);
                                    }
                                    else if(myGame.getColumn(i+ii).getSlot(j-ii+1).getIsFilled() == false && myGame.getColumn(i+ii).getSlot(j-ii+2).getIsFilled() == true) // If slot j-ii is unfilled, j-ii+1 is unfilled and j-ii+2 is filled.
                                    {
                                        colToAvoid.add(i+ii);
                                    }                                   
                                }
                            }
                        }
                  }
                  
                  if (i >= 3 && j - 3 >= 0)
                  {
                        int pos1filled = myGame.getColumn(i).getSlot(j).getIsFilled()?1:0;
                        int pos2filled = myGame.getColumn(i-1).getSlot(j-1).getIsFilled()?1:0;
                        int pos3filled = myGame.getColumn(i-2).getSlot(j-2).getIsFilled()?1:0;
                        int pos4filled = myGame.getColumn(i-3).getSlot(j-3).getIsFilled()?1:0;
                        int[] posfilled = new int[]{pos1filled, pos2filled, pos3filled, pos4filled};
                        
                        int pos1Color = myGame.getColumn(i).getSlot(j).getIsRed()?1:0;
                        int pos2Color = myGame.getColumn(i-1).getSlot(j-1).getIsRed()?1:0;
                        int pos3Color = myGame.getColumn(i-2).getSlot(j-2).getIsRed()?1:0;
                        int pos4Color = myGame.getColumn(i-3).getSlot(j-3).getIsRed()?1:0;
                        
                        if (pos1filled + pos2filled + pos3filled + pos4filled == 3 && pos1Color + pos2Color + pos3Color + pos4Color == colorSum) //If three tokens with opponent color are on a diagonal line
                        {
                            for(int ii = 0; ii < 4; ii++)
                            {
                                if(posfilled[ii] == 0)
                                {
                                    if(j - ii == numRows - 1) // If slot j-ii is on the bottom of the panel.
                                    {
                                        continue;
                                    }
                                    else if(myGame.getColumn(i-ii).getSlot(j-ii+1).getIsFilled() == false && j -ii + 1 == numRows - 1) // If slot j-ii is unfilled, j-ii+1 is unfilled and also is the bottom of the panel.
                                    {
                                        colToAvoid.add(i-ii);
                                    }
                                    else if(myGame.getColumn(i-ii).getSlot(j-ii+1).getIsFilled() == false && myGame.getColumn(i-ii).getSlot(j-ii+2).getIsFilled() == true) // If slot j-ii is unfilled, j-ii+1 is unfilled and j-ii+2 is filled.
                                    {
                                        colToAvoid.add(i-ii);
                                    } 
                                }
                            }
                        }
                  }
            }
        }
        return colToAvoid;
    }

    /**
     * Set the name of this agent.
     *
     * @param playerName name of this agent
     */

    public void setName(String playerName)
    {
        this.playerName = playerName;
    }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    
    public String getName()
    {
        return playerName;
    }
}
