import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.BufferedImage;
class Stats //draws the health, blood overlay, and the current weapon at the top left of the screen. 
{
	int health;
	String weapon;
	boolean showWeapon;
	Font silk;
	public Stats() //constructor uses a try and catch block to initialize font called silkscreen. if it doesn't work then i just use times new roman
	{
		showWeapon = false;
		try //initialize pixel font called silkscreen
		{
			silk = Font.createFont(Font.TRUETYPE_FONT, new File("../silkscreen.ttf")).deriveFont(15f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("../silkscreen.ttf")));
		}
		catch(IOException | FontFormatException e)
		{
			silk = new Font("Times New Roman", Font.PLAIN, 15);
		}
	}
	public void draw(Graphics g, int newHealth, String newWeapon, boolean attacked, int coins) //draws the health, blood background, and the current weapon at the top left of screen. is called from LevelOne
	{
		weapon = newWeapon;
		health = newHealth;
		if(health<0)
			health=0;
		
		Image heart = new ImageIcon("../images/stats-heart.png").getImage(); //draws the health
		g.drawImage(heart, 8, 15, 23, 23, null);
		g.setColor(Color.white);
		if(health<20)
			g.setColor(Color.RED);
		g.setFont(silk);
		g.drawString(""+health, 40, 30);
		g.setColor(Color.white);
		g.drawString("/100", 75, 30);
		
		g.drawString("Weapon:",140,30);
		Image weaponImg = new ImageIcon(("../images/"+weapon+"-right.png")).getImage();
		if(weapon.equals("minigun") || weapon.equals("ak47") || weapon.equals("shotgun")) g.drawImage(weaponImg,225,11,77,30,null); //certain weapons are longer than others
		else g.drawImage(weaponImg,225,10,45,39,null);
		
		Image coin = new ImageIcon("../images/stats-coin.png").getImage();
		g.drawImage(coin,605,13,20,20,null);
		g.drawString(""+coins,630,31);
		if(attacked) //drawing the blood overlay when the player is attacked
		{
			Image blood = new ImageIcon("../images/bloodoverlay.png").getImage();
			g.drawImage(blood, 0, 0, 700, 500, null);
		}
	}
}
