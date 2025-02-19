import java.awt.*;
import javax.swing.*;
import java.io.*; //for font errors
import java.awt.Font;
import java.awt.event.*;
class whichplayerPanel extends JPanel implements ActionListener //the panel that appears after user enters username on newgamePanel and presses start. its purpose is to have the user choose one of the two avatars. after this panel is level 1
{
	MainLayout dungeon; //instance of MainLayout = main code so i can access its methods
	Font deadFont;
	JButton selectMale, selectFemale, selectNinja, selectSoldier;
	public whichplayerPanel(MainLayout dl) //constructor adds custom font and calls setUpPage
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
		setUpPage();
	}
	public void setUpPage() //adds JButtons, JLabels, and sets bounds
	{
		setLayout(null);
		selectMale = new JButton("select");
		selectMale.setFont(deadFont);
		selectMale.setBounds(150,205,125,50);
		selectMale.setBackground(Color.gray);
		selectMale.setActionCommand("Male");
		selectMale.addActionListener(this);
		add(selectMale);
		selectFemale = new JButton("select");
		selectFemale.setFont(deadFont);
		selectFemale.setBounds(415,205,125,50);
		selectFemale.setBackground(Color.gray);
		selectFemale.setActionCommand("Female");
		selectFemale.addActionListener(this);
		add(selectFemale);
		setLayout(null);
		selectNinja = new JButton("select");
		selectNinja.setFont(deadFont);
		selectNinja.setBounds(150,440,125,50);
		selectNinja.setBackground(Color.gray);
		selectNinja.setActionCommand("Ninja");
		selectNinja.addActionListener(this);
		add(selectNinja);
		selectSoldier = new JButton("select");
		selectSoldier.setFont(deadFont);
		selectSoldier.setBounds(415,440,125,50);
		selectSoldier.setBackground(Color.gray);
		selectSoldier.setActionCommand("Soldier");
		selectSoldier.addActionListener(this);
		add(selectSoldier);
		for(int i = 1;i<6;i++) //creates jlabels that say stuff like "+1 sword damage". more efficient than creating 4 separate JLabels
		{
			JLabel extras;
			if(i==1){ 
				extras = new JLabel("+13 sword damage");
				extras.setBounds(280,300,140,15);
			}
			else if(i==2){
				extras = new JLabel("-2 bullet damage");
				extras.setBounds(280,315,140,15);
			}
			else if(i==3){
				extras = new JLabel("-5 sword damage");
				extras.setBounds(545,300,140,15);
			}
			else if(i==4){
				extras = new JLabel("+5 bullet damage");
				extras.setBounds(545,315,140,15);
			}
			else{
				extras = new JLabel("+1 speed");
				extras.setBounds(280,330,140,15);
			}
			extras.setForeground(Color.WHITE);
			add(extras);
		}
	}
	public void paintComponent(Graphics g) //draws four avatars in a white frame. also draws the background picture
	{
		super.paintComponent(g);
		Image background = new ImageIcon("../images/background-wp.png").getImage();
		g.drawImage(background,0,0,700,500,null);
		Image frame = new ImageIcon("../images/wp-frame.png").getImage();
		g.drawImage(frame, 150, 20, 125, 175, null);
		g.drawImage(frame, 415, 20, 125, 175, null);
		g.drawImage(frame, 150, 255, 125, 175, null);
		g.drawImage(frame, 415, 255, 125, 175, null);
		Image male = new ImageIcon("../images/male-right.png").getImage();
		g.drawImage(male, 160, 30, 110, 150, null);
		Image female = new ImageIcon("../images/female-right.png").getImage();
		g.drawImage(female, 425, 30, 110, 150, null);
		Image ninja = new ImageIcon("../images/ninja-right.png").getImage();
		g.drawImage(ninja, 160,265,110,150,null);
		Image soldier = new ImageIcon("../images/soldier-right.png").getImage();
		g.drawImage(soldier, 425,265,110,150,null);
	}
	public void actionPerformed(ActionEvent e) //called when the "select" button for either avatar is pressed. now the game can start
	{
		if(e.getActionCommand().equals("Male"))
		{
			dungeon.startGame("../images/male-left.png", "../images/male-right.png", "../images/male-left-walking.png", "../images/male-right-walking.png"); //if user chooses male avatar, this is the string for the male image.
			//dungeon is an instance of MainLayout
		}
		else if(e.getActionCommand().equals("Female"))
		{
			dungeon.startGame("../images/female-left.png", "../images/female-right.png", "../images/female-left-walking.png", "../images/female-right-walking.png"); //if user chooses female avatar, this is the string for the female image
		}
		else if(e.getActionCommand().equals("Ninja"))
		{
			dungeon.startGame("../images/ninja-left.png", "../images/ninja-right.png", "../images/ninja-left-walking.png", "../images/ninja-right-walking.png"); //if user chooses female avatar, this is the string for the female image
		}
		else if(e.getActionCommand().equals("Soldier"))
		{
			dungeon.startGame("../images/soldier-left.png", "../images/soldier-right.png", "../images/soldier-left-walking.png", "../images/soldier-right-walking.png"); //if user chooses female avatar, this is the string for the female image
		}
	}
}
