import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.FileNotFoundException; 
import java.util.Scanner; 
import java.io.IOException;
class highscoresPanel extends JPanel implements MouseListener //panel that appears when user presses "high scores" on home screen
{
	int[] xcoor = {470, 670,670,470};
	int[] ycoor = {413,413,447,447};
	Polygon backButton = new Polygon(xcoor,ycoor,4);
	JLabel[] records;
	JLabel[] datesLabel;
	int[] scores;
	String[] usernames;
	String[] dates;
	Scanner in;
	Font deadFont;
	MainLayout dungeon;
	public highscoresPanel(MainLayout dl)//constructor adds mouse listener and custom font, also sets layout to null
	{
		try
		{
			deadFont = Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")).deriveFont(30f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("../deadfontwalking.ttf")));
		}
		catch(IOException | FontFormatException e)
		{
			deadFont = new Font("Times New Roman", Font.PLAIN, 25);
		}
		dungeon = dl;
		addMouseListener(this);
		records = new JLabel[5];
		datesLabel = new JLabel[5];
		for(int i = 0;i<records.length;i++)
		{
			records[i] = new JLabel("");
			datesLabel[i] = new JLabel("");
		}
		setUpPage(); //adds jlabels and other stuff
		setLayout(null);
	}
	public void setUpPage() //sorts the scores in the text file and adds 5 high scores jlabels to panel
	{
		for(int i =0; i<records.length;i++)
		{
			records[i].setText("");
			datesLabel[i].setText("");
			add(records[i]);
			add(datesLabel[i]);
		}
		JLabel title = new JLabel("HIGH SCORES:");
		title.setFont(deadFont);
		title.setForeground(new Color(83,21,26));
		title.setBounds(270,10,200,70);
		add(title);
		getLinesAndSort(); //bubble sorts the scores
		int ycoor = 70;
		for(int i = 0;i<5;i++) //for top 5 scores, add jlabels with the format below
		{
			if(scores.length>i){
				records[i].setText((i+1)+". \""+usernames[i]+"\" - Score: "+scores[i]);  //how the scores are going to appear
				datesLabel[i].setText(dates[i]);
			}
			else{ //if 5 people haven't played it yet, say n.a
				records[i].setText((i+1)+"");
				datesLabel[i].setText("");
			}
			records[i].setFont(deadFont);
			records[i].setForeground(Color.white);
			records[i].setBounds(30,ycoor,500,50);
			datesLabel[i].setFont(deadFont);
			datesLabel[i].setForeground(Color.white);
			datesLabel[i].setBounds(500,ycoor,200,50);
			ycoor+=50;
		}
	}
	public void paintComponent(Graphics g)//paints background image and the back button
	{
		super.paintComponent(g);
		Image BACKGROUND = new ImageIcon("../images/background-highscores.png").getImage();
		g.drawImage(BACKGROUND,0,0,700,500,null);
		Image BACK = new ImageIcon("../images/b-back.png").getImage();
		dungeon.createButton(g, 450, 400, 250, 60, BACK, 490, 415, 155, 30);
	}
	public void createButton(Graphics g, int x, int y, int width, int height, Image img, int x2, int y2, int w2, int h2) //creates custom button instead of normal JButton
	{
		Image template = new ImageIcon("../images/buttonTemplate.png").getImage();
		g.drawImage(template, x, y, width, height,null);
		g.drawImage(img, x2, y2, w2, h2,null);
	}
	public void mousePressed(MouseEvent e) //if "back" is pressed, go back to home page
	{
		if(backButton.contains(e.getX(), e.getY()))
		{
			dungeon.showHome(); //show home panel. dungeon is an instance of MainLayout so I can access showHome()
		}
	}
	public void mouseClicked(MouseEvent e){} //required method but does nothing
	public void mouseEntered(MouseEvent e){} //required method but does nothing
	public void mouseExited(MouseEvent e){} //required method but does nothing
	public void mouseReleased(MouseEvent e){} //required method but does nothing
	
	
	public void readIt()//creates scanner that reads from Scores.txt
	{
		File inFile = new File ("../Scores.txt"); 
		String inFileName = "../Scores.txt"; 
		String  value = ""; 
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
	public void getLinesAndSort() //takes information in text file and bubble sorts the scores from best to worst
	{
		int amtOfLines = 0;
		readIt();
		String line;
		String nonparsed;
		String tempString; //for bubble sort
		int temp;
		while(in.hasNextLine())
		{
			amtOfLines++;
			in.nextLine();
		}
		readIt();
			
		scores = new int[amtOfLines];
		usernames = new String[amtOfLines];
		dates = new String[amtOfLines];
		//lines in the file will be in this format: [username],[score]:[date]
		for(int i = 0;i<amtOfLines;i++)
		{
			line = in.nextLine();
			nonparsed = line.substring(line.indexOf(',')+1,line.indexOf(':'));
			scores[i] = Integer.parseInt(nonparsed);
			usernames[i] = line.substring(0,line.indexOf(','));
			dates[i] = line.substring(line.indexOf(':')+1);
		}
		for(int i = 0;i<scores.length-1;i++) //bubble sort usernames and scores
		{
			for(int j = 0;j<scores.length-1;j++)
			{

				if(scores[j]<scores[j+1]) //bubbles sorts from best to worst
				{
					temp = scores[j];
					scores[j] = scores[j+1];
					scores[j+1] = temp;
					
					tempString = usernames[j];
					usernames[j] = usernames[j+1];
					usernames[j+1] = tempString;
						
					tempString = dates[j];
					dates[j] = dates[j+1];
					dates[j+1] = tempString;
				}
			}
		}
	}
	public void reset(){ //sets up page again and sees the new modified scores.txt. called after user completes the game to update the scores
		setUpPage();
	}
}
