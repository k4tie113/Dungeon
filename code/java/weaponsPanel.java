import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
class weaponsPanel extends JPanel implements ActionListener //the panel that appears when user presses "buy weapons". contains images of 4 weapons and their prices for the user to pick from
{
	MainLayout dungeon;
	String[] weapons;
	Font deadFont;
	Font deadFontLarge;
	JButton back;
	JButton buyRevolver;
	JButton buyShotgun;
	JButton buyAK;
	JButton buyMini;
	boolean ownRevolver, ownShotgun, ownAK, ownMini; //whether the player has the weapon
	JLabel note;
	public weaponsPanel(MainLayout dl) //constructor initializes values like whether the user owns certain weapons, fonts, and also calls the method to add jbuttons and labels
	{
		ownRevolver = false;
		ownShotgun = false;
		ownAK = false;
		ownMini = false;
		try //initialize pixel font called dead font walking
		{
			deadFont = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(22f);
			deadFontLarge = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")));
		}
		catch(IOException | FontFormatException e) //error
		{
			deadFont = new Font("Times New Roman", Font.PLAIN, 22);
			deadFontLarge = new Font("Times New Roman", Font.PLAIN, 25);
		}
		setLayout(null);
		dungeon = dl;
		setUpPage();
	}
	public void setUpPage() //creates JLabels and JButtons for the panel. JButtons are for buying weapons and the JLabel shows up whenever there is an error while trying to buy something, for ex. not enough money
	{
		back = new JButton("BACK");
		back.setBounds(570,420,100,50);
		back.setFont(deadFont);
		back.addActionListener(this);
		add(back);
		note = new JLabel(""); //note is a red jlabel that appears at the bottom of the screen. it says nothing for now but changes into something like "already owned" if the user tries to buy a weapon but owns it already
		note.setBounds(250,350,500,60);
		note.setFont(deadFontLarge);
		note.setForeground(Color.red);
		add(note); 
		buyRevolver = new JButton("BUY REVOLVER");
		buyRevolver.setFont(deadFont);
		buyRevolver.setBounds(450,50,200,40);
		buyRevolver.addActionListener(this);
		add(buyRevolver);
		buyShotgun = new JButton("BUY SHOTGUN");
		buyShotgun.setFont(deadFont);
		buyShotgun.setBounds(450,120,200,40);
		buyShotgun.addActionListener(this);
		add(buyShotgun);
		buyAK = new JButton("BUY AK-47");
		buyAK.setFont(deadFont);
		buyAK.setBounds(450,190,200,40);
		buyAK.addActionListener(this);
		add(buyAK);
		buyMini = new JButton("BUY MINIGUN");
		buyMini.setFont(deadFont);
		buyMini.setBounds(450,260,200,40);
		buyMini.addActionListener(this);
		add(buyMini);
	}
	public void paintComponent(Graphics g) //paints the weapons, the amount of coins the user has, and also the background image. it also calls drawPrice(), the method which draws the price of the weapon
	{
		super.paintComponent(g);
		Image background = new ImageIcon("../images/background-weapons.png").getImage();
		g.drawImage(background, 0,0,700,500,null);
		Image revolver = new ImageIcon("../images/revolver-right.png").getImage();
		g.drawImage(revolver,250,50,80,50,null);
		Image shotgun = new ImageIcon("../images/shotgun-right.png").getImage();
		g.drawImage(shotgun, 250,120,150,50,null);
		Image ak47 = new ImageIcon("../images/ak47-right.png").getImage();
		g.drawImage(ak47,250,190,150,50,null);
		Image mini = new ImageIcon("../images/minigun-right.png").getImage();
		Image coin = new ImageIcon("../images/stats-coin.png").getImage();
		g.setFont(deadFont);
		g.drawImage(coin,15,15,30,30,null);
		g.setColor(Color.white);
		g.drawString(""+dungeon.coins, 55, 40);
		g.drawImage(mini,250,260,180,60, null);
		drawPrice(g,ownRevolver,400,80); //draws the price for the revolver. passes in a y coordinate of where to draw, and also the price of the gun
		drawPrice(g,ownShotgun,700,150);
		drawPrice(g,ownAK,1000,220);
		drawPrice(g,ownMini,1500,300);
	}
	public void drawPrice(Graphics g, boolean owned, int price, int ycoor) //draws the price of the weapon next to the weapon image in green if they have enough money, red if not enough money. draws the word "owned" if user already has the weapon and cannot buy it again
	{
		g.setFont(deadFont);
		if(!owned) //if the user does not own the weapon yet then the price is drawn
		{
			if(dungeon.coins>=price) g.setColor(Color.GREEN); //can buy weapon
			else g.setColor(Color.RED); //coins < price therefore cannot buy
			g.drawString("$"+price,150,ycoor);
		}
		else
		{
			g.setColor(Color.WHITE);
			g.setFont(deadFont);
			g.drawString("OWNED",150,ycoor);
		}
	}
	public void actionPerformed(ActionEvent e) //called when user presses a button to buy a certain weapon. if there are no problems then the weapon is bought and therefore owned. if there is a problem then the red note at the bottom says something like "not enough money"
	{
		if(e.getSource()==back)
		{
			dungeon.takeInWeaponsArray(weapons);
			dungeon.goBackToEndLevel();
		}
		else if(e.getSource()==buyRevolver) //button to buy the revolver
		{
			if(ownRevolver) note.setText("ALREADY OWNED");
			else if(dungeon.coins>=400) //successfully bought the revolver
			{
				weapons = addWeaponToArray("revolver");
				ownRevolver = true;
				dungeon.coins-=400;
				repaint();
			}
			else note.setText("NOT ENOUGH COINS");
		}
		else if(e.getSource()==buyShotgun)
		{
			if(ownShotgun) note.setText("ALREADY OWNED");
			else if(dungeon.coins>=700)
			{
				weapons = addWeaponToArray("shotgun");
				ownShotgun = true;
				dungeon.coins-=700;
				repaint();
			}
			else note.setText("NOT ENOUGH COINS");
		}
		else if(e.getSource()==buyAK)
		{
			if(ownAK) note.setText("ALREADY OWNED");
			else if(dungeon.coins>=1000)
			{
				weapons = addWeaponToArray("ak47");
				ownAK = true;
				dungeon.coins-=1000;
				repaint();
			}
			else note.setText("NOT ENOUGH COINS");
		}
		else if(e.getSource()==buyMini)
		{
			if(ownMini) note.setText("ALREADY OWNED");
			else if(dungeon.coins>=1500)
			{
				weapons = addWeaponToArray("minigun");
				ownMini = true;
				dungeon.coins-=1500;
				repaint();
			}
			else note.setText("NOT ENOUGH COINS");
		}
	}
	public void getOwnedWeapons() //determines which weapons the user owns based on the array of weapons they have. this method is called from mainlayout just before weapon panel is shown
	{
		for(int i = 0;i<weapons.length;i++) //determining what weapons the user owns
		{
			if(weapons[i].equals("revolver")) ownRevolver = true; //if something inside the weapons array = "revolver" then the user owns the revolver
			else if(weapons[i].equals("shotgun")) ownShotgun = true;
			else if(weapons[i].equals("ak47")) ownAK = true;
			else if(weapons[i].equals("minigun")) ownMini = true;
		}
		repaint();
	}
	public String[] addWeaponToArray(String name) //when a user buys a new weapon, the weapon array is edited so that the new weapon's name is included. this method takes in the name of the new weapon and returns the new and fixed array
	{
		String[] temp = new String[weapons.length+1];
		for(int i = 0;i<weapons.length;i++)
		{
			temp[i] = weapons[i];
		}
		temp[weapons.length] = name;
		return temp;
	}
	public void reset() //reset every value in the panel when the user restarts the game. now the user is back to owning just a sword and nothing else
	{
		weapons = new String[]{"sword"};
		ownRevolver = false;
		ownShotgun = false;
		ownAK = false;
		ownMini = false;
	}
}
