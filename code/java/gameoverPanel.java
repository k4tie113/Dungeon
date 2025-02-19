import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
import java.util.Scanner;
class gameoverPanel extends JPanel implements MouseListener //panel for when user dies and "you are dead" appears
{
	MainLayout dungeon;
	Font deadFontLarge;
	Font deadFontSmall;
	boolean showYouAreDead, showOthers;
	Polygon button;
	Scanner in;
	String[] text;
	JLabel youAreDead, levels;
	public gameoverPanel(MainLayout dl) //constructor initializes custom font and starts timers
	{
		setLayout(null);
		addMouseListener(this);
		try
		{
			deadFontLarge = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(90f);
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
		setUpPage();
	}
	public void setUpPage() //sets boundaries for the polygon backbutton so mousePressed knows if the button inside the polygon is pressed
	{
		youAreDead = new JLabel("you are dead");
		youAreDead.setBounds(130,100,550,150);
		youAreDead.setFont(deadFontLarge);
		youAreDead.setForeground(Color.RED);
		levels = new JLabel("Levels Completed: "+dungeon.levelsCompleted);
		levels.setBounds(215,225,300,50);
		levels.setFont(deadFontSmall);
		levels.setForeground(Color.RED);
		add(youAreDead);
		add(levels);
		button = new Polygon(new int[]{245,455,455,230}, new int[]{330,330,370,370}, 4);
	}
	public void paintComponent(Graphics g) //paints background image and button
	{
		super.paintComponent(g);
		Image BACKGROUND = new ImageIcon("../images/background-gameover.png").getImage();
		g.drawImage(BACKGROUND,0,0,700,500,null);
		createButton(g, 225, 320, 250, 60);
		g.setFont(deadFontSmall);
		g.drawString("Main Menu", 280, 360);
	}
	public void resetText() //resets the text for the JLabel that tells you how much levels you completed. This method is necessary because if the user chooses to restart the game, the next time they fail, the amount of levels they fail may be different than last time
	{
		levels.setText("Levels Completed: "+dungeon.levelsCompleted);
		createLines();
		removeData(); //calls method that removes the username and stats so that the next time someone uses the username it doesn't say "username already taken"
	}
	public void createButton(Graphics g, int x, int y, int width, int height) //creates custom button instead of normal JButton
	{
		Image template = new ImageIcon("../images/buttonTemplate.png").getImage();
		g.drawImage(template, x, y, width, height,null);
	}
	public void createLines() //reads and stores all text of Saves.txt into an array for later use
	{
		readIt();
		int lines =0;
		while(in.hasNextLine())
		{
			lines++; //counting lines
			in.nextLine();
		}
		text = new String[lines];
		readIt();
		for(int i = 0;i<text.length;i++)
		{
			text[i] = in.nextLine();
		}
		readIt();
	}
	public void removeData()//prints to the text file Saves.txt but replaces the current username's stuff so that all of this user's data is deleted since they failed and someone else can play using their same username
	{
		String outFileName = new String("../Saves.txt");
		File outFile = new File("../Saves.txt");
		PrintWriter makesOutput = null;
		try
		{
			makesOutput = new PrintWriter( outFile );
			
		}catch(FileNotFoundException e)
		{
			System.err.println("Cannot create "+outFileName+"file to be written to.");
			System.exit(1);
		}
		for(int i=0;i<text.length;i++)
		{
			if(text[i].length()>dungeon.username.length())
			{
				if(!text[i].substring(0,dungeon.username.length()).equalsIgnoreCase(dungeon.username)) makesOutput.println(text[i]); //if the text is not the current user's then it's fine to be printed out
			}
		}
		makesOutput.close();
	}
	public void readIt()//creates scanner that reads from Saves.txt
	{
		File inFile = new File ("../Saves.txt"); 
		String inFileName = "../Saves.txt"; 
		try
		{ 
			in = new Scanner ( inFile );
		}
		catch ( FileNotFoundException e ) 
		{ 
			System.err.println("Cannot find " + inFileName + " file.");  
			System.exit(1);
		}
	}
	public void mousePressed(MouseEvent e) //if user presses button go to main menu
	{
		if(button.contains(e.getX(), e.getY())){
			dungeon.showHome();
		}
	}
	public void mouseClicked(MouseEvent e){} //required method but does nothing
	public void mouseEntered(MouseEvent e){} //required method but does nothing
	public void mouseExited(MouseEvent e){} //required method but does nothing
	public void mouseReleased(MouseEvent e){} //required method but does nothing
}
