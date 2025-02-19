import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Zombie //class for drawing and moving a zombie. similar to class player but the movement of the zombie is determined by the movement of the player
{
	int xcoor, ycoor, speed, healthBar, damage;
	int health;
	final int HEIGHT;
	final int WIDTH;
	int xmove, ymove;
	String left, right, deadLeft, deadRight, whichImage;
	boolean alive;
	boolean crashed;
	boolean swordHit;
	public Zombie(String str1, String str2, int w, int h, int s, int he, int d) //constructor takes in a bunch of zombie stats like damage, health, and where to find the images of the zombies 
	{
		left = str1; //picture of zombie facing left
		right = str2;
		deadLeft = left.substring(0,left.length()-4)+"-dead.png";
		deadRight = right.substring(0,right.length()-4)+"-dead.png";
		HEIGHT = h;
		WIDTH = w;
		speed = s;
		healthBar = he;
		health = healthBar;
		damage = d;
		whichImage = left; //starts off facing left
		alive = true;
	}
	public void set(int x, int y) //sets the x and y coordinates of where the zombie is
	{
		xcoor = x;
		ycoor = y;
	}
	public void draw(Graphics g, String imgString, boolean c, int h) //draws the zombie based on what direction its facing. takes in a boolean on whether the zombie is able to move or not, and an integer on what the health is right now
	{
		crashed = c;
		health = h;
		if(alive)
		{
			if(crashed)//if an object is interfering with the zombie, I subtract the movement so the zombie does not move
			{
				xcoor-=xmove;
				ycoor-=ymove;
			}
			Image zombieImg = new ImageIcon(imgString).getImage();
			g.drawImage(zombieImg, xcoor, ycoor, WIDTH, HEIGHT,null);
			if(health<0) health=0;
			g.setColor(Color.RED);
			g.fillRect(xcoor,ycoor-3,WIDTH,4);
			g.setColor(Color.GREEN);
			g.fillRect(xcoor,ycoor-3,(int)(((0.0+health)/healthBar)*WIDTH),4);
		}
		else //if zombie is dead and does not move
		{
			if(imgString.equals(right)) whichImage = deadRight;
			else if(imgString.equals(left)) whichImage = deadLeft;
			Image zombieImg = new ImageIcon(whichImage).getImage();
			g.drawImage(zombieImg, xcoor, ycoor+HEIGHT-WIDTH, HEIGHT, WIDTH,null);
		}
	}
	public void move()//just moves the zombie based on what xmove and ymove is. xmove and ymove are sort of the velocity 
	{
		if(alive) //a dead zombie cannot move
		{
			xcoor+=xmove;
			ycoor+=ymove;
		}
	}
	public void determineDirection(int x, int y, int width, int height) //tells zombie which direction to follow player, based on the player's coordinates. it takes in the x, y, width, and height of a player
	{
		int pX = x+(width/2); //coordinates of the middle of the player's image
		int pY = y+(height/2);
		int zX = xcoor+(WIDTH/2); //coordinates of the middle of the zombie's image
		int zY = ycoor+(HEIGHT/2);
		if(Math.abs(pX-zX)>(Math.abs(pY-zY)) && alive){ //horizontal distance between player and zombie > vertical distance
			if(pX<zX){ //zombie needs to go left
				whichImage = left;
				xmove = -speed;
				ymove = 0;
			}
			else{
				whichImage = right;
				xmove = speed;
				ymove = 0;
			}
		}
		else if(alive){
			if(pY<zY){ //zombie needs to go up
				xmove = 0;
				ymove = -speed;
			}
			else{
				xmove = 0;
				ymove = speed;
			}
		}
		
	}
	public void drawSwordHit(Graphics g, int playerX, int playerY, String playerImage) //draws the hit effect when hit by a sword. the image is like a white cutting effect where the zombie is
	{
		Image effect;
		if(xcoor<playerX && alive){ //determines the direction of the sword hit based on where the zombie and player are relative to each other
			effect = new ImageIcon("../images/swordeffect-left.png").getImage();
			g.drawImage(effect,xcoor+WIDTH/2,ycoor+15,25,55,null);
		}
		else if(alive){
			effect = new ImageIcon("../images/swordeffect-right.png").getImage();
			g.drawImage(effect,xcoor,ycoor+15,25,55,null);
		}
		int swordDamage = (int)(Math.random()*10)+10;
		health-=swordDamage;
		if(playerImage.equals("../images/ninja-left.png")) health-=13; //ninjas do extra sword damage :)
		else if(playerImage.equals("../images/soldier-left.png")) health+=5; //soldiers don't do much damage using swords
		swordHit = false;
	}
}
