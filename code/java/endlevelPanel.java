import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
class endlevelPanel extends JPanel implements ActionListener //the panel that appears whenever a user completes level 1 or level 2. it is shown because of the method "endLevel" in MainLayout
{
	MainLayout dungeon;
	Font deadFontLarge;
	Font deadFontSmall;
	Polygon button = new Polygon(new int[]{245,455,455,230}, new int[]{330,330,370,370}, 4);
	int killed, time, level;
	JButton shop;
	JButton nextLevel;
	JLabel complete;
	JLabel zombiesKilled;
	JLabel totalTime;
	JLabel click;
	Timer completeTimer, zombiesKilledTimer, totalTimeTimer; //timers that time the appearance of each of the three JLabels
	public endlevelPanel(MainLayout dl) //constructor initializes two custom fonts, sets the layout, and takes in an instance of mainlayout so we can access mainlayout's methods
	{
		setLayout(null);
		try //uses try block to create font called dead font walking
		{
			deadFontLarge = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(75f);
			deadFontSmall = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")));
		}
		catch(IOException | FontFormatException e)
		{
			deadFontLarge = new Font("Times New Roman", Font.PLAIN, 90);
			deadFontSmall = new Font("Times New Roman", Font.PLAIN, 30);
		}
		dungeon = dl;
	}
	public void setUpPage() //initializes the three JLabels, timers, and buttons but does not show them right away until actionPerformed is called
	{
		completeTimer = new Timer(1300, this);
		zombiesKilledTimer = new Timer(1000,this);
		totalTimeTimer = new Timer(1000,this);
		
		complete = new JLabel("level "+level+" complete"); //JLabel: level [lvl #] complete
		complete.setFont(deadFontLarge);
		complete.setForeground(Color.WHITE);
		complete.setBounds(100,70,600,100);
		complete.setVisible(false);
		add(complete);
		
		zombiesKilled = new JLabel(killed+" zombies killed"); //Jlabel that tells you how much zombies you killed
		zombiesKilled.setFont(deadFontSmall);
		zombiesKilled.setForeground(Color.WHITE);
		zombiesKilled.setBounds(250,190,400,50);
		zombiesKilled.setVisible(false);
		add(zombiesKilled);
		
		int minutes = 0; //calculating how to write out the amount of time
		while(time>60)
		{
			time-=60;
			minutes++;
		}
		totalTime = new JLabel("time: "+minutes+" minutes "+time+" seconds");
		totalTime.setFont(deadFontSmall);
		totalTime.setForeground(Color.WHITE);
		totalTime.setBounds(185,260,400,50);
		totalTime.setVisible(false);
		add(totalTime);
		
		shop = new JButton("buy weapons"); //shop button
		shop.setBounds(240,330,210,50);
		shop.setFont(deadFontSmall);
		shop.addActionListener(this);
		shop.setVisible(false);
		add(shop);
		
		nextLevel = new JButton("next level"); //next level button
		nextLevel.setBounds(240,400,210,50);
		nextLevel.setFont(deadFontSmall);
		nextLevel.addActionListener(this);
		nextLevel.setVisible(false);
		add(nextLevel);
		
		click = new JLabel("click 'next level' to save progress");
		click.setFont(deadFontSmall);
		click.setForeground(Color.WHITE);
		click.setBounds(150,455,500,50);
		click.setVisible(false);
		add(click);
	}
	public void paintComponent(Graphics g) //paints the background image of the panel. nothing else.
	{
		Image background = new ImageIcon("../images/background-weapons.png").getImage();
		g.drawImage(background, 0,0,700,500,null);
	}
	public void reset(int l, int t, int k) //takes in three values that are the level, time, and zombies killed. also starts the three timers again. is called whenever user completes a NEW level.
	{
		level = l;
		time = t;
		killed = k;
		setUpPage();
		completeTimer.start();
	}
	public void actionPerformed(ActionEvent e) //sets stuff visible when their timer rings to make it look cooler. stuff will appear one at a time each a second apart
	{
		if(e.getSource()==completeTimer)
		{
			dungeon.playDrum();
			completeTimer.restart();
			completeTimer.stop();
			complete.setVisible(true);
			zombiesKilledTimer.start();
		}
		else if(e.getSource()==zombiesKilledTimer)
		{
			dungeon.playDrum();
			zombiesKilledTimer.restart();
			zombiesKilledTimer.stop();
			zombiesKilled.setVisible(true);
			totalTimeTimer.start();
		}
		else if(e.getSource()==totalTimeTimer)
		{
			dungeon.playDrum();
			totalTimeTimer.restart();
			totalTimeTimer.stop(); 
			totalTime.setVisible(true);
			shop.setVisible(true);
			nextLevel.setVisible(true);
			click.setVisible(true);
		}
		else if(e.getSource()==shop){ //shop for weapons
			dungeon.showWeapons();
		}
		else if(e.getSource()==nextLevel){ //go to the next level
			
			dungeon.nextLevel();
		}
	}
}
