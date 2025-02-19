import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
class Room//class for making the process of creating jpanels easier. contains a few helpful methods
{
	Polygon[] objects;
	Polygon[] doors;
	int zombieAmt; //total amt of zombies
	int zombies2; //amt of special zombies that do more damage and have more health
	public Room(Polygon[] oArray, Polygon[] dArray, int t,int other)//pass in an array of objects you need to avoid, so the checkCollision tells you if the player has bumped into one of those objects
	{//also pass in an array of locations of doors
		objects = oArray;
		doors = dArray;
		zombieAmt= t;
		zombies2 = other;
	}
	public boolean checkCollision(int x, int y, int width, int height) //x and y of the player, also the width and height of the player
	{
		for(int i = 0;i<objects.length;i++) //checking to see if object or wall is in the way of player's coordinates. to be more specific, it checks each of the player's image's four corners
		{
			if(objects[i].contains(x+5,y+5))
				return true;
			else if(objects[i].contains(x+width-5,y+5))
				return true;
			else if(objects[i].contains(x+width-5, y+height-5))
				return true;
			else if(objects[i].contains(x+5, y+height-5))
				return true;
			else if(objects[i].contains(x+width,y+(height/2)))
				return true;
			else if(objects[i].contains(x,y+(height/2)))
				return true;
		}
		return false; //false mean player has not crashed and he/she can keep moving
	}
	public int checkDoors(int x, int y, int width, int height) //checks if player is at any door
	{
		for(int i = 0;i<doors.length;i++) //returns which door was passed through. door 1, door 2, etc
		{
			if(doors[i].contains(x+width,y+(height/2)))
				return i+1;
			else if(doors[i].contains(x,y+(height/2)))
				return i+1;
			else if(doors[i].contains(x+(width/2), y))
				return i+1;
			else if(doors[i].contains(x+(width/2),y+height))
				return i+1;
		}
		return 0; //player is not in any door
	}
}
