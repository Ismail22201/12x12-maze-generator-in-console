/*
	Group Members:
		1. Mohamad Azriff Firdaus bin Zulkofli 1926183
		2. Rayyanur Rahman 1929067
		3. Ahmed Faisal 1921967
		4. Muhammad Ismail 1922235
	
	CSCI 1301 Section 2
	Semester 1 2020/2021
	
	Reference: None
	(For Exercise 1, we just followed the method given in the instructions. We did not need to search the Internet.)
	(For Exercise 2, we did search the Internet and we found Lee's Algorithm. However, we did not implement it.)
*/

package assignments;

import java.util.Random;
import java.util.Scanner;

public class GenMaze12x12Final
{
	
	int[][][] Cells = new int[14][14][8];// [X Coordinate][Y Coordinate][4 Walls on each side + 4 Possible paths]
	int[][][] ShortestPath = new int[14][14][8];// [X Coordinate][Y Coordinate][4 Walls on each side + 4 Possible paths]
	int[][] Set = new int[14][14];// [X Coordinate][Y Coordinate]
	int[][] Visited = new int[14][14];// [X Coordinate][Y Coordinate]
	int[] Track = new int[144];// Steps taken (144 is the max possible steps)
	int StartX, StartY, EndX, EndY; // Positions requested by the user.
	int k = 1;// Index for Track[].
	
	Scanner get = new Scanner(System.in);
	Random numGen = new Random();
	
	GenMaze12x12Final(){}
	
	void GetStartEnd()
	{	
		System.out.println();
		do
		{
			System.out.print("X coordinate of starting Cell: ");
			StartX = get.nextInt();
			if(StartX < 1 || StartX > 12) System.out.print("Out of bounds! Please try again!\n");
		}
		while(StartX < 1 || StartX > 12);
		
		do
		{
			System.out.print("Y coordinate of starting Cell: ");
			StartY = get.nextInt();
			if(StartY < 1 || StartY > 12) System.out.print("Out of bounds! Please try again!\n");
		}
		while(StartY < 1 || StartY > 12);
		
		do
		{
			System.out.print("X coordinate of ending Cell: ");
			EndX = get.nextInt();
			if(EndX < 1 || EndX > 12) System.out.print("Out of bounds! Please try again!\n");
		}
		while(EndX < 1 || EndX > 12);
		
		do
		{
			System.out.print("Y coordinate of ending Cell: ");
			EndY = get.nextInt();
			if(EndY < 1 || EndY > 12) System.out.print("Out of bounds! Please try again!\n");
		}
		while(EndY < 1 || EndY > 12);
	}
	
	int RandNum1to12()
    {
    	int rand = Math.abs((1)+numGen.nextInt(12));
    	return rand;
    }
	
	int RandNum1to4()
    {
    	int rand = Math.abs((1)+numGen.nextInt(4));
    	return rand;
    }
	
	void InitializeCells()
	{	
		int h = 0;
		
		for(int i = 0; i < 14; i++)
		{
			for(int j = 0; j < 14; j++)
			{
				Set[i][j] = 0;// Initializes all 14X14 cells starting from (0, 0) to (14, 14) to set 0.
				Visited[i][j] = 1;// Initializes all 14X14 cells starting from (0, 0) to (14, 14) to visited cells.
				
				for(int k = 0; k < 8; k++)
				{
					Cells[i][j][k] = 0;// Initializes all walls and possible paths to 0 (0 = Does not exist).
				}
			}
		}
		
		for(int i = 1; i < 13; i++)
		{
			for(int j = 1; j < 13; j++)
			{
				Set[i][j] = h++;// Initializes all 12X12 cells from (1, 1) to (12, 12) to a unique set.
				Visited[i][j] = 0;// Initializes all 12X12 cells from (1, 1) to (12, 12) to unvisited cells.
				
				for(int k = 0; k < 4; k++)
				{
					Cells[i][j][k] = 1;// Initializes all walls of 12X12 cells to 1 (1 = Exists).
				}
			}
		}
		
		for(int i = 0; i < 144; i++)
		{
			Track[i] = 0;// Initializes the whole track track to 0 (0 = Does not exist).
		}
	}
	
	void ChooseAndDestroyWall()
	{	
		int Count;
		do
		{
			int ChooseX = RandNum1to12();// Chooses a random X coordinate of a cell.
			int ChooseY = RandNum1to12();// Chooses a random Y coordinate of a cell.
			int ChooseWall = RandNum1to4();// Chooses a random wall of the cell.
			int PreviousSet = 0, NextSet = 0;
			Count = 0;
			
			switch(ChooseWall)
			{
				case 1:	// 1 = Top wall
					if(Set[ChooseX][ChooseY - 1] == 0)// If the chosen wall is on the side of the maze, then don't destroy.
					{
						break;
					}
					else if(Set[ChooseX][ChooseY] == Set[ChooseX][ChooseY - 1])// If the the cells on either side of the chosen wall is part of the same set, then don't destroy.
					{
						break;
					}
					else
					{
						Cells[ChooseX][ChooseY][0] = 0;// Destroy chosen wall.
						Cells[ChooseX][ChooseY - 1][2] = 0;// Destroy chosen wall. (Destroys the bottom wall of the cell on the top)
						PreviousSet = Set[ChooseX][ChooseY];
						NextSet = Set[ChooseX][ChooseY - 1];
						Set[ChooseX][ChooseY] = Set[ChooseX][ChooseY - 1];// Make the cells on either sides of the chosen wall part of the same set.
					}
					
				case 2: // 2 = Right wall
					if(Set[ChooseX + 1][ChooseY] == 0)// If the chosen wall is on the side of the maze, then don't destroy.
					{
						break;
					}
					else if(Set[ChooseX + 1][ChooseY] == Set[ChooseX][ChooseY])// If the the cells on either side of the chosen wall is part of the same set, then don't destroy.
					{
						break;
					}
					else
					{
						Cells[ChooseX][ChooseY][1] = 0;// Destroy chosen wall.
						Cells[ChooseX + 1][ChooseY][3] = 0;// Destroy chosen wall. (Destroys the left wall of the cell on the right)
						PreviousSet = Set[ChooseX + 1][ChooseY];
						NextSet = Set[ChooseX][ChooseY];
						Set[ChooseX + 1][ChooseY] = Set[ChooseX][ChooseY];// Make the cells on either sides of the chosen wall part of the same set.
					}
					
				case 3: // 3 = Bottom wall
					if(Set[ChooseX][ChooseY + 1] == 0)// If the chosen wall is on the side of the maze, then don't destroy.
					{
						break;
					}
					else if(Set[ChooseX][ChooseY + 1] == Set[ChooseX][ChooseY])// If the the cells on either side of the chosen wall is part of the same set, then don't destroy.
					{
						break;
					}
					else
					{
						Cells[ChooseX][ChooseY][2] = 0;// Destroy chosen wall.
						Cells[ChooseX][ChooseY + 1][0] = 0;// Destroy chosen wall. (Destroys the top wall of the cell on the bottom)
						PreviousSet = Set[ChooseX][ChooseY + 1];
						NextSet = Set[ChooseX][ChooseY];
						Set[ChooseX][ChooseY + 1] = Set[ChooseX][ChooseY];// Make the cells on either sides of the chosen wall part of the same set.
					}
					
				case 4: // 4 = Left wall
					if(Set[ChooseX - 1][ChooseY] == 0)// If the chosen wall is on the side of the maze, then don't destroy.
					{
						break;
					}
					else if(Set[ChooseX][ChooseY] == Set[ChooseX - 1][ChooseY])// If the the cells on either side of the chosen wall is part of the same set, then don't destroy.
					{
						break;
					}
					else
					{
						Cells[ChooseX][ChooseY][3] = 0;// Destroy chosen wall.
						Cells[ChooseX - 1][ChooseY][1] = 0;// Destroy chosen wall. (Destroys the right wall of the cell on the left)
						PreviousSet = Set[ChooseX][ChooseY];
						NextSet = Set[ChooseX - 1][ChooseY];
						Set[ChooseX][ChooseY] = Set[ChooseX - 1][ChooseY];// Make the cells on either sides of the chosen wall part of the same set.
					}
			}
			
			for(int i = 1; i < 13; i++)
			{
				for(int j = 1; j < 13; j++)
				{
					if(Set[i][j] == PreviousSet)
					{
						Set[i][j] = NextSet;// Make all the cells of the set... of the chosen cell... part of the same set. 
					}
				}
			}
						
			for(int i = 1; i < 13; i++)// Check if all the cells in the maze are part of the same set.
			{
				for(int j = 1; j < 13; j++)
				{						
					if(Set[i][j] == NextSet)
					{
						Count++;
					}
					else
					{
						Count = 0;
						break;
					}
				}
				if(Count == 0) break;					
			}

				
		}
		while(!(Count == 144));// Stop looping only if all the cells in the maze are part of the same set.
	}
	
	void GetRandomPath()
	{
		int CurrentX = StartX, CurrentY = StartY;// Current position equal to the position requested by user.
		boolean Stuck = false;// Not stuck in a dead end
		
		while(!(CurrentX == EndX && CurrentY == EndY))// Loop as long as current position does not equal to the position of the destination.
		{
			if(CurrentX == StartX && CurrentY == StartY)// If current position equals to the position requested by user.
			{
				for(int i = 0; i < 14; i++)
				{
					for(int j = 0; j < 14; j++)
					{
						Visited[i][j] = 1;// Initializes all 14X14 cells starting from (0, 0) to (14, 14) to visited cells.
						for(int h = 4; h < 8; h++)
						{
							Cells[i][j][h] = 0;// Initializes all  possible paths to 0 (0 = Does not exist).
						}
					}
				}
				
				for(int i = 1; i < 13; i++)
				{
					for(int j = 1; j < 13; j++)
					{
						Visited[i][j] = 0;// Initializes all 12X12 cells from (1, 1) to (12, 12) to unvisited cells.						
					}
				}
				
				for(int i = 0; i < 144; i++)
				{
					Track[i] = 0;// Initializes all walls and possible paths to 0 (0 = Does not exist).
				}
				
				CurrentX = StartX;// Current X equals to the starting X requested by user.
				CurrentY = StartY;// Current Y equals to the starting Y requested by user.
				Stuck = false;// Not stuck in a dead end.
				Visited[CurrentX][CurrentY] = 1;// Sets current position to visited.
				k = 1;// Starts Track[] at index 1.
			}
			
			if(Cells[CurrentX][CurrentY][0] == 1 || Visited[CurrentX][CurrentY - 1] == 1)// If cell on the top is blocked by a wall or has been visited.
			{
				if(Cells[CurrentX][CurrentY][1] == 1 || Visited[CurrentX + 1][CurrentY] == 1)// If cell on the right is blocked by a wall or has been visited.
				{
					if(Cells[CurrentX][CurrentY][2] == 1 || Visited[CurrentX][CurrentY + 1] == 1)// If cell on the bottom is blocked by a wall or has been visited.
					{
						if(Cells[CurrentX][CurrentY][3] == 1 || Visited[CurrentX - 1][CurrentY] == 1)// If cell on the left is blocked by a wall or has been visited.
						{
							Stuck = true;// Current position is stuck in a dead end or surrounded by visited cells.
							
							switch(Track[k-1])// Backtrack one step.
							{
								case 1:// If previous step was going up.
									Cells[CurrentX][CurrentY][6] = 0;// Remove path going down.
									Cells[CurrentX][CurrentY + 1][4] = 0;// Remove path from the cell on the bottom.
									k--; // Decrement index of Track[] by 1.
									CurrentY += 1;// Move current position down.
									break;
									
								case 2:// If previous step was going right.
									Cells[CurrentX][CurrentY][7] = 0;// Remove path going left.
									Cells[CurrentX - 1][CurrentY][5] = 0;// Remove path from the cell on the left.
									k--;// Decrement index of Track[] by 1.
									CurrentX -= 1;// Move current position to the left.
									break;
									
								case 3:// If previous step was going down.
									Cells[CurrentX][CurrentY][4] = 0;// Remove path going up.
									Cells[CurrentX][CurrentY - 1][6] = 0;// Remove path from the cell on the top.
									k--;// Decrement index of Track[] by 1.
									CurrentY -= 1;// Move current position up.
									break;
									
								case 4:// If previous step was going left.
									Cells[CurrentX][CurrentY][5] = 0;// Remove path going right.
									Cells[CurrentX + 1][CurrentY][7] = 0;// Remove path from the cell on the right.
									k--;// Decrement index of Track[] by 1.
									CurrentX += 1;// Move current position to the right.
									break;
							}
						}								
					}							
				}						
			}
			
			int ChoosePath = RandNum1to4();// Chooses a random direction.
			switch(ChoosePath)
			{
				case 1:// 1 = Go up
					if(Cells[CurrentX][CurrentY][0] == 0)// If top wall does not exist.
					{
						if(Visited[CurrentX][CurrentY - 1] == 0)// If the cell on the top has not been visited.
						{
							Stuck = false;// Not stuck in a dead end.
							Cells[CurrentX][CurrentY][4] = 1;// Path to the the cell on the top exists.
							Cells[CurrentX][CurrentY - 1][6] = 1;// Path to current cell from the cell on the top exists.
							Track[k] = ChoosePath;// Record the path path taken.
							k++;// Increment index of Track[] by 1.
							CurrentY -= 1;// Move current position up.
							Visited[CurrentX][CurrentY] = 1;// Set current position/cell as visited.
						}
					}
					
					break;
					
				case 2:// 2 = Go right
					if(Cells[CurrentX][CurrentY][1] == 0)// If right wall does not exist.
					{
						if(Visited[CurrentX + 1][CurrentY] == 0)// If the cell on the right has not been visited.
						{
							Stuck = false;// Not stuck in a dead end.
							Cells[CurrentX][CurrentY][5] = 1;// Path to the the cell on the right exists.
							Cells[CurrentX + 1][CurrentY][7] = 1;// Path to current cell from the cell on the right exists.
							Track[k] = ChoosePath;// Record the path path taken.
							k++;// Increment index of Track[] by 1.
							CurrentX += 1;// Move current position to the right.
							Visited[CurrentX][CurrentY] = 1;// Set current position/cell as visited.
						}
					}
					
					break;
					
				case 3:// 3 = Go down
					if(Cells[CurrentX][CurrentY][2] == 0)// If bottom wall does not exist.
					{
						if(Visited[CurrentX][CurrentY + 1] == 0)// If the cell on the bottom has not been visited.
						{
							Stuck = false;// Not stuck in a dead end.
							Cells[CurrentX][CurrentY][6] = 1;// Path to the the cell on the bottom exists.
							Cells[CurrentX][CurrentY + 1][4] = 1;// Path to current cell from the cell on the bottom exists.
							Track[k] = ChoosePath;// Record the path path taken.
							k++;// Increment index of Track[] by 1.
							CurrentY += 1;// Move current position down.
							Visited[CurrentX][CurrentY] = 1; // Set current position/cell as visited.
						}
					}
					
					break;
					
				case 4:// 4 = Go left
					if(Cells[CurrentX][CurrentY][3] == 0)// If left wall does not exist.
					{
						if(Visited[CurrentX - 1][CurrentY] == 0)// If the cell on the left has not been visited.
						{
							Stuck = false;// Not stuck in a dead end.
							Cells[CurrentX][CurrentY][7] = 1;// Path to the the cell on the left exists.
							Cells[CurrentX - 1][CurrentY][5] = 1;// Path to current cell from the cell on the left exists.
							Track[k] = ChoosePath;// Record the path path taken.
							k++;// Increment index of Track[] by 1.
							CurrentX -= 1;// Move current position to the left.
							Visited[CurrentX][CurrentY] = 1;// Set current position/cell as visited.
						}
					}
					
					break;
			}
		}		
	}
	
	void GetShortestPath()
	{
		int PathLength = k - 1;// Length of the path is the same as the index of Track[] minus 1.
		int ShortestLenght = PathLength;// Set shortest length as the length of any path generated before the declaration of this method.
		
		for(int i = 0; i < 14; i++)
		{
			for(int j = 0; j < 14; j++)
			{
				for(int k = 0; k < 8; k++)
				{
					ShortestPath[i][j][k] = Cells[i][j][k];// Initialize ShortestPath[][][] with just maze walls.
				}
			}
		}
		
		for(int h = 0; h < 500; h++)// Loop 500 times. Chances are the shortest path of has been generated.
		{
			do
			{
				GetRandomPath();// Generates a random path.
				if(StartX == EndX && StartY == EndY) break;// If destination has bee reached.
			}
			while(PathLength == 0);// Loop if length of the path is 0. (In case start position and destination are the same)
			
			PathLength = k - 1;// Number of steps taken is the same as the index of Track[] minus 1.
			if(PathLength < ShortestLenght)// If the length of the last path generated and stored in Cells[][][] is shorter than the current shortest path.
			{
				ShortestLenght = PathLength;// Set the shortest path length as the length of last path generated and stored in Cells[][][].
				for(int i = 0; i < 14; i++)
				{
					for(int j = 0; j < 14; j++)
					{
						for(int k = 0; k < 8; k++)
						{
							ShortestPath[i][j][k] = Cells[i][j][k];// Set shortest path as the last path generated and stored in Cells[][][].
						}
					}
				}
			}
		}
		
		for(int i = 0; i < 14; i++)
		{
			for(int j = 0; j < 14; j++)
			{
				for(int k = 0; k < 8; k++)
				{
					Cells[i][j][k] = ShortestPath[i][j][k];// Set Cells[][][] to the shortest path (Walls remain the same throughout each execution).
				}
			}
		}
		
		System.out.println("\nNumber of steps taken: " + ShortestLenght);
		System.out.println("(Steps = '---' & '|' in between X)");
	}

	void GenWallsAndPaths()
	{
		for(int j = 1; j < 13; j++)
		{
			for(int i = 1; i < 13; i++)
			{								
				if(Cells[i][j][0] == 1)
				{
					System.out.print("+---");// Top walls.
				}
				else if(Cells[i][j][0] == 0)
				{
					if(Cells[i][j][4] == 1)
					{
						System.out.print("+ | ");// Path going up/down.
					}
					else
					{
						System.out.print("+   ");// No top walls or paths going up/down.
					}				
				}
				
			}
						
			if(j == 1) System.out.println("+ Y");
			else System.out.println("+");
			
			for(int i = 1; i < 13; i++)
			{								
				if(Cells[i][j][3] == 1)
				{
					if(Cells[i][j][4] == 1 && Cells[i][j][6] == 1)
					{
						System.out.print("| X ");// Left wall with path from up/down.
					}
					else if(Cells[i][j][4] == 1 && Cells[i][j][5] == 1)
					{
						System.out.print("| X-");// Left wall with path to/from right.
					}
					else if(Cells[i][j][5] == 1 && Cells[i][j][6] == 1)
					{
						System.out.print("| X-");// Left wall with path to/from right.
					}
					else if(Cells[i][j][4] == 1 || Cells[i][j][6] == 1)
					{
						System.out.print("| X ");// Left wall with path from up/down.
					}
					else if(Cells[i][j][5] == 1)
					{
						System.out.print("| X-");// Left wall with path to/from right.
					}
					else
					{
						System.out.print("|   ");// Left wall with no path.
					}
					
				}
				else if(Cells[i][j][3] == 0)
				{
					if(Cells[i][j][7] == 1 && Cells[i][j][5] == 1)
					{
						System.out.print("--X-");// Path from right to left or left to right.
					}
					else if(Cells[i][j][5] == 1)
					{
						System.out.print("  X-");// Path to/from right.
					}
					else if(Cells[i][j][7] == 1)
					{
						System.out.print("--X ");// Path to/from left.
					}
					else if(Cells[i][j][4] == 1 && Cells[i][j][6] == 1)
					{
						System.out.print("  X ");// Path from up/down.
					}
					else if(Cells[i][j][4] == 1 || Cells[i][j][6] == 1)
					{
						System.out.print("  X ");// Path from up/down.
					}					
					else
					{
						System.out.print("    ");// No path.
					}
				}				
			}			
			System.out.print("|");
			System.out.println(" " + j);
		}
				
		for(int i = 1; i < 13; i++)// Generate the bottom of the maze.
		{
			if(Cells[i][12][2] == 1)
			{
				System.out.print("+---");
			}
			if(Cells[i][12][2] == 0)
			{
				System.out.print("+   ");
			}
		}
		System.out.print("+\n");
		System.out.print("X 1 ");
		for(int i = 2; i < 10; i++) System.out.print("  " + i + " ");
		for(int i = 10; i < 13; i++) System.out.print("  " + i);
		
		System.out.print("\n");
	}
	
	public static void main(String[] args)
	{
		GenMaze12x12Final Maze = new GenMaze12x12Final();
		Maze.InitializeCells();
		Maze.ChooseAndDestroyWall();
		Maze.GenWallsAndPaths();// Actually displays the maze with no paths so that the user may see from where to where would they like to draw a path.
		Maze.GetStartEnd();
		Maze.GetRandomPath();
		Maze.GetShortestPath();
		Maze.GenWallsAndPaths();// Displays the maze with shortest path.	
	}
}