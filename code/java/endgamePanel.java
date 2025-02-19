import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
import java.time.LocalDate;
import java.io.FileWriter; import java.io.PrintWriter;
import java.io.File; import java.io.FileNotFoundException; 
import java.io.IOException; import java.util.Scanner; 
class endgamePanel extends JPanel implements ActionListener //the panel that appears whenever a user completes level 1 or level 2. it is shown because of the method "endLevel" in MainLayout
{
	MainLayout dungeon;
	Font deadFontLarge;
	Font deadFontSmall;
	Polygon button = new Polygon(new int[]{245,455,455,230}, new int[]{330,330,370,370}, 4);
	int killed, time, points;
	JButton main;
	JLabel complete;
	JLabel zombiesKilled;
	JLabel totalTime;
	JLabel pointsLabel;
	Timer completeTimer, zombiesKilledTimer, totalTimeTimer; //timers that time the appearance of each of the three JLabels
	public endgamePanel(MainLayout dl) //constructor initializes two custom fonts, sets the layout, and takes in an instance of mainlayout so we can access mainlayout's methods
	{
		points = 0;
		setLayout(null);
		try //uses try block to create font called dead font walking
		{
			deadFontLarge = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(60f);
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
		
		complete = new JLabel("escaped the dungeon");
		complete.setFont(deadFontLarge);
		complete.setForeground(Color.WHITE);
		complete.setBounds(100,70,600,100);
		complete.setVisible(false);
		add(complete);
		
		zombiesKilled = new JLabel(killed+" total zombies killed"); //Jlabel that tells you how much zombies you killed
		zombiesKilled.setFont(deadFontSmall);
		zombiesKilled.setForeground(Color.WHITE);
		zombiesKilled.setBounds(210,190,400,50);
		zombiesKilled.setVisible(false);
		add(zombiesKilled);
		
		points+=(15*killed);
		if(time<400) points+=(10*(400-time));
		else if(time<600) points+=(5*(600-time));
		
		int minutes = 0; //calculating how to write out the amount of time
		while(time>60)
		{
			time-=60;
			minutes++;
		}
		totalTime = new JLabel("total time: "+minutes+" minutes "+time+" seconds");
		totalTime.setFont(deadFontSmall);
		totalTime.setForeground(Color.WHITE);
		totalTime.setBounds(150,265,550,50);
		totalTime.setVisible(false);
		add(totalTime);
		
		pointsLabel = new JLabel("SCORE: "+points);
		pointsLabel.setBounds(260,340,210,50);
		pointsLabel.setFont(deadFontSmall);
		pointsLabel.setForeground(Color.WHITE);
		pointsLabel.setVisible(false);
		add(pointsLabel);
		
		main = new JButton("main menu"); //next level button
		main.setBounds(240,410,210,50);
		main.setFont(deadFontSmall);
		main.addActionListener(this);
		main.setVisible(false);
		add(main);
		
		appendIt();
	}
	public void paintComponent(Graphics g) //paints the background image of the panel. nothing else.
	{
		Image background = new ImageIcon("../images/background-endgame.png").getImage();
		g.drawImage(background, 0,0,700,500,null);
	}
	public void reset(int t, int k) //takes in three values that are the level, time, and zombies killed. also starts the three timers again. is called whenever user completes a NEW level.
	{
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
			pointsLabel.setVisible(true);
			main.setVisible(true);
		}
		else if(e.getSource()==main){ //go to home page
			dungeon.showHome();
		}
	}
	public void appendIt() //appends the new score into the scores file using the format [username],[points]:[date]. also prints [username]:4 to the saves file (means that this usernames game is completed and that other users cannot use that name) 
	{
		PrintWriter pw = null; 
		PrintWriter pw2 = null; 
		File outFile = new File("../Scores.txt");
		File outFile2 = new File("../Saves.txt");
		try
		{  
			pw = new PrintWriter( new FileWriter(outFile, true) ); 
			pw2 = new PrintWriter( new FileWriter(outFile2, true) );
		}
		catch ( IOException e)
		{ 
			System.out.println("There was a problem saving the game");
			System.exit(1);
		}
		LocalDate date = LocalDate.now();
		pw.println(""+dungeon.username+","+points+":"+date);
		pw.close();
		pw2.println(""+dungeon.username+":4"); //signal that this username's game is finished 
		pw2.close();
	}
}

