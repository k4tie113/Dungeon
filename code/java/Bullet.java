import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Bullet //a single bullet fired by shooting a gun. has an image of a bullet and contains coordinates of the bullet only
{
	int xcoor, ycoor;
	int xmove, ymove; //velocity
	Image bullet;
	public Bullet(int x, int y, Image img, int xVelocity, int yVelocity) //constructor initializes taken values such as horizontal and vertical velocity, and x and y locations
	{
		xmove = xVelocity;
		ymove = yVelocity;
		xcoor = x;
		ycoor = y;
		bullet = img; //the image of the bullet
	}
	public void move() //adds the velocities to the x and y coordinates. the velocities are based on which direction the bullet should be heading in, for example a negative x velocity means going left
	{
		xcoor+=xmove;
		ycoor+=ymove;
	}
	public void draw(Graphics g) //just draws the bullet at x and y coordinates with width 7 and height 4. this method is called by an instance of the Gun class
	{
		g.drawImage(bullet, xcoor, ycoor, 7, 4,null);
	}
	
}
