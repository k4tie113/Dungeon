import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
import java.io.FileNotFoundException; 
import java.util.Scanner; 
import java.io.IOException;
class newgamePanel extends JPanel implements MouseListener, ActionListener //panel that appears after user presses "new game" on the home screen
{
	int[] xcoor = {470, 670,670,470};
	int[] ycoor = {413,413,447,447};
	Polygon backButton = new Polygon(xcoor,ycoor,4);
	JTextField textField;
	JButton start, startSave;
	String name;
	String[] saves; 
	JLabel error;
	MainLayout dungeon;
	Scanner in;
	int amtOfLines = 0;
	String savesName, left, right;
	String[] weapons;
	int coinsEarned, time, level, killed;
	Font deadFont;
	public newgamePanel(MainLayout dl)//constructor adds mouse listener and custom font. takes in an instance of MainLayout so i'm able to go back to home page if a button is pressed
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
		setUpPage(); //adds jlabels and other stuff
		setLayout(null);
	}
	public void setUpPage() //adds JLabels and JTextfield to new game panel
	{
		JLabel title = new JLabel("Enter username:");
		title.setFont(deadFont);
		title.setForeground(Color.white);
		title.setBounds(255,10,200,70);
		add(title);
		textField = new JTextField(10);
		textField.setBounds(225,70,250,50);
		textField.setFont(deadFont);
		add(textField);
		start = new JButton("SUBMIT");
		start.setFont(deadFont);
		start.setBounds(480,70,150,50);
		start.setFocusPainted(false);
		start.setBackground(Color.gray);
		start.addActionListener(this);
		add(start);
		startSave = new JButton("");
		startSave.setFont(deadFont);
		startSave.setBounds(100,150,500,50);
		startSave.setFocusPainted(false);
		startSave.setBackground(Color.gray);
		startSave.addActionListener(this);
		startSave.setVisible(false);
		add(startSave);
		error = new JLabel("");
		error.setFont(deadFont);
		error.setForeground(new Color(184,15,10));
		error.setBounds(120,240,500,50);
		add(error);
	}
	public void paintComponent(Graphics g) //paints background image and the back button
	{
		super.paintComponent(g);
		Image BACKGROUND = new ImageIcon("../images/background-newgame.png").getImage();
		g.drawImage(BACKGROUND,0,0,700,500,null);
		Image BACK = new ImageIcon("../images/b-back.png").getImage();
		createButton(g, 450, 400, 250, 60, BACK, 490, 415, 155, 30);
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
			error.setText("");
			startSave.setVisible(false);
			dungeon.showHome(); //calls main layout to show panel that allows player to choose which avatar. dungeon is an instance of MainLayout so I can access choosePlayer()
		}
	}
	public void mouseClicked(MouseEvent e){} //required method but does nothing
	public void mouseEntered(MouseEvent e){} //required method but does nothing
	public void mouseExited(MouseEvent e){} //required method but does nothing
	public void mouseReleased(MouseEvent e){} //required method but does nothing
	
	public void actionPerformed(ActionEvent e) //called once "enter" button is pressed. however if the username is too short/long a message will appear
	{
		if(textField.getText().length()<1)
		{
			error.setText("Username must be at least 1 character");
		}
		else if(textField.getText().length()>15)
		{
			error.setText("Username must be 15 characters or less");
		}
		else if(e.getSource()==startSave) dungeon.loadSave(left,right,level,coinsEarned,killed,time,weapons,name);
		else
		{
			error.setText("");
			name = (textField.getText());
			if(checkSaves()==1) startSave.setVisible(true);
			else if(checkSaves()==0) dungeon.choosePlayer(name);
			else error.setText("             username already taken");
		}
	}
	public int checkSaves() //reads from the saves.txt file to see if there are any ongoing games with the username. returns -1 for username taken, 0 for everything is fine, 1 for username already exists but the game is not finished and it needs to be continued
	{
		amtOfLines = 0;
		saves = new String[0];
		readIt();
		int numberOfWeapons = 1;
		String allWeaponsString;
		while(in.hasNextLine()) //getting the number of lines in saves.txt
		{
			amtOfLines++;
			in.nextLine();
		}
		readIt();
		saves = new String[amtOfLines];
		for(int i = 0;i<amtOfLines;i++)
		{
			saves[i] = in.nextLine(); //putting the stuff in the file into the array
		}
		readIt();
		for(int i = saves.length-1;i>=0;i--) //going backwards because i want most recent stuff
		{
			savesName = saves[i].substring(0,saves[i].indexOf(':'));
			saves[i] = saves[i].substring(savesName.length());
			if(name.equalsIgnoreCase(savesName))
			{
				level = Integer.parseInt(saves[i].substring(saves[i].indexOf(':')+1,saves[i].indexOf(':')+2));
				if(level==4) //the username's user has already completed the game
				{
					i-=saves.length;
					return -1;
				}
				else
				{
					allWeaponsString = saves[i].substring(saves[i].indexOf('!')+1,saves[i].lastIndexOf('!'));
					for(int k = 0;k<allWeaponsString.length();k++) //getting how many weapons the user names
					{
						if(allWeaponsString.charAt(k)==',')
							numberOfWeapons++;
					}
					weapons = new String[numberOfWeapons];
					for(int j = 0;j<numberOfWeapons;j++)
					{
						if(j!=numberOfWeapons-1)
						{
							weapons[j] = allWeaponsString.substring(0,allWeaponsString.indexOf(','));
							allWeaponsString = allWeaponsString.substring(allWeaponsString.indexOf(',')+1);
						}
						else
							weapons[j] = allWeaponsString.substring(0);
					}
					coinsEarned = Integer.parseInt( saves[i].substring(saves[i].indexOf('!',saves[i].indexOf('!')+1)+1,saves[i].indexOf(';')) );
					killed = Integer.parseInt(saves[i].substring(saves[i].indexOf(';')+1, saves[i].indexOf('*')));
					time = Integer.parseInt(saves[i].substring(saves[i].indexOf('*')+1,saves[i].indexOf('&')));
					left = saves[i].substring(saves[i].indexOf('&')+1,saves[i].indexOf('$'));
					right = saves[i].substring(saves[i].indexOf('$')+1);
					startSave.setText("load saved game: level "+level);
					return 1;
				}
			}
		}
		return 0;
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
}
