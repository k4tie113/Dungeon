import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Player//the class for drawing the player of the game and controlling his/her movement. 
{
	int xcoor, ycoor;
	String left, right; //the names of the files for the two images. one has the player facing left, the other right.
	String leftWalking, rightWalking; //names of the files for two images for animating the player
	String whichImage;
	int xmove, ymove;
	int weaponIndex;
	final int WIDTH = 55;
	final int HEIGHT = 80;
	boolean dontDrawSword;
	public Player(String str1, String str2, String str3, String str4, int x1, int y1) //constructor initializes a bunch of values, like where to find the images of the player, and the x and y coordinates
	{
		//str1 is the file name of the image of the player facing left, str2 is of player facing right, and the two integers are the spawn coordinates
		weaponIndex = 0;
		left = str1;
		right = str2;
		leftWalking = str3;
		rightWalking = str4;
		xcoor = x1;
		ycoor = y1;
		whichImage = str2; //the game starts off with the player facing right, which is str2
	}
	public void draw(Graphics g, String imgString, boolean crashed, String w)//draws player at specific coordinates with weapon. takes in the appropriate image to draw, a boolean on whether the player is able to move, and the name of the weapon the player is holding
	{
		if(crashed)//if an object is interfering with the player, I subtract the movement so the player does not move
		{
			xcoor-=xmove;
			ycoor-=ymove;
		}
		Image playerImg = new ImageIcon(imgString).getImage();
		if(dontDrawSword){
			if(whichImage.equals(left.substring(0,left.length()-4)+"-sword.png")) g.drawImage(playerImg, xcoor-40, ycoor, WIDTH+40, HEIGHT,null);
			else g.drawImage(playerImg, xcoor, ycoor, WIDTH+40, HEIGHT,null);
		}
		else
			g.drawImage(playerImg, xcoor, ycoor, WIDTH, HEIGHT,null);
		drawWeapon(g, w);
	}
	public void drawWeapon(Graphics g, String w) //draws the weapon the player is holding and based on what direction the player is facing. takes in a string called W which is the name of the weapon
	{
		if(w.equals("sword")) //draws the sword
		{
			Image sword;
			if((whichImage.equals(right) || whichImage.equals(rightWalking)) && !dontDrawSword){
				sword = new ImageIcon("../images/sword-right.png").getImage();
				g.drawImage(sword, xcoor+WIDTH-14, ycoor+19, 40,40,null);
			}
			else if(!dontDrawSword){
				sword = new ImageIcon("../images/sword-left.png").getImage();
				g.drawImage(sword, xcoor-WIDTH+28, ycoor+19, 40,40,null);
			}
		}
		else if(w.equals("revolver")) //draws the revolver
		{
			Image revolver;
			if((whichImage.equals(right) || whichImage.equals(rightWalking))){
				revolver = new ImageIcon("../images/revolver-right.png").getImage();
				g.drawImage(revolver, xcoor+WIDTH-9, ycoor+41, 25,13,null);
			}
			else
			{
				revolver = new ImageIcon("../images/revolver-left.png").getImage();
				g.drawImage(revolver, xcoor-WIDTH+41, ycoor+41, 25,13,null);
			}
		}
		else if(w.equals("shotgun"))
		{
			Image shotgun;
			if((whichImage.equals(right) || whichImage.equals(rightWalking))){
				shotgun = new ImageIcon("../images/shotgun-right.png").getImage();
				g.drawImage(shotgun, xcoor+2, ycoor+38, 75,20,null);
			}
			else
			{
				shotgun = new ImageIcon("../images/shotgun-left.png").getImage();
				g.drawImage(shotgun, xcoor-WIDTH+37, ycoor+38, 75,20,null);
			}
		}
		else if(w.equals("ak47"))
		{
			Image ak;
			if((whichImage.equals(right) || whichImage.equals(rightWalking))){
				ak = new ImageIcon("../images/ak47-right.png").getImage();
				g.drawImage(ak, xcoor+2, ycoor+42, 75,20,null);
			}
			else
			{
				ak = new ImageIcon("../images/ak47-left.png").getImage();
				g.drawImage(ak, xcoor-WIDTH+37, ycoor+42, 75,20,null);
			}
		}
		else if(w.equals("minigun"))
		{
			Image mini;
			if((whichImage.equals(right) || whichImage.equals(rightWalking))){
				mini = new ImageIcon("../images/minigun-right.png").getImage();
				g.drawImage(mini, xcoor+2, ycoor+34, 75,28,null);
			}
			else
			{
				mini = new ImageIcon("../images/minigun-left.png").getImage();
				g.drawImage(mini, xcoor-WIDTH+37, ycoor+34, 75,28,null);
			}
		}
	}
	public void move() //moves the player based on what xmove and ymove is (adds the velocity to the coordinates). this method is called every 1/50 second in actionPerformed in LevelOne
	{
		xcoor+=xmove;
		ycoor+=ymove;
		
	}
	public void spawn(int x, int y)//spawns the player at specific coordinates. takes in two integers of the x and y coordinate.
	{
		xcoor = x;
		ycoor = y;
	}
	public void changeAnimation() //changes the player's image so it looks like he/she is walking instead of sliding around. 
	{
		if(xmove!=0 || ymove!=0) //player is currently walking
		{
			if(whichImage.equals(left))
			{
				whichImage = leftWalking;
			}
			else if(whichImage.equals(right))
			{
				whichImage = rightWalking;
			}
			else if(whichImage.equals(leftWalking))
			{
				whichImage = left;
			}
			else if(whichImage.equals(rightWalking))
			{
				whichImage = right;
			}
		}
	}
	public void keyPressed(KeyEvent e, String playerImage) //method called in LevelOne. if one of the four moves keys is pressed, i change the xmove and ymove so when move() is called, the coordinates change accordingly
	{
		if(whichImage.equals(left.substring(0,left.length()-4)+"-sword.png")) whichImage = left; //stop the sword stabbing animation from drawSwordAnimation()
		else if(whichImage.equals(right.substring(0,right.length()-4)+"-sword.png")) whichImage = right;
		
		int extraSpeed = 0;
		if(playerImage.equals("../images/ninja-left.png")) extraSpeed = 1; //ninjas can move faster
		dontDrawSword = false;
		if(e.getKeyCode()==KeyEvent.VK_W) //WASD keys for forward, left, down, right
		{
			xmove = 0;
			ymove = -4-extraSpeed;
		}
		else if(e.getKeyCode()==KeyEvent.VK_D)
		{
			if(whichImage.equals(left))
				whichImage = right;
			else if(whichImage.equals(leftWalking))
				whichImage = rightWalking;
			xmove = 4+extraSpeed;
			ymove = 0;
		}
		else if(e.getKeyCode()==KeyEvent.VK_S)
		{
			xmove = 0;
			ymove = 4+extraSpeed;
		}
		else if(e.getKeyCode()==KeyEvent.VK_A)
		{
			if(whichImage.equals(right))
				whichImage = left;
			else if(whichImage.equals(rightWalking))
				whichImage = leftWalking;
			xmove = -4-extraSpeed;
			ymove = 0;
		}
	}
	public void keyReleased(KeyEvent e) //method called in LevelOne. if the four moving keys are released, the player stops moving. 
	{
		if(e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_D)
		{
			xmove = 0;
			ymove = 0;
		}
	}
	public void drawSwordAnimation(int clickX, int clickY) //when the player tries to stab a zombie, it changes the image to the player in a stabbing motion for as long as the player doesn't move
	{
		dontDrawSword = true; //the image of the player stabbing already has a sword in it so i don't want to draw the other sword that is normally drawn when the player is just moving
		if(clickX<xcoor+WIDTH/2)
		{
			whichImage = left.substring(0,left.length()-4)+"-sword.png";
		}
		else if(clickX>=xcoor+WIDTH/2)
		{
			whichImage = right.substring(0,right.length()-4)+"-sword.png";
		}
	}
	//note that i do not have keyTyped because this class does not implement KeyListener. LevelOne does.
}
