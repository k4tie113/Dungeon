//Katie Li
//4.24.2021
//MainLayout.java
//Create a "The Walking Dead" inspired game where the player makes his/her way out of a dungeon by killing zombies
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.sound.sampled.AudioInputStream; //for game sounds
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Mixer;
import java.io.FileWriter; import java.io.PrintWriter; //for appending stuff into file
import java.io.File; import java.io.FileNotFoundException; 
import java.io.IOException; import java.util.Scanner; 
public class MainLayout //main code of the program. contains home panel code, player stats, and methods for showing various panels of the cardlayout
{
	JPanel cards; 
	CardLayout cl;
	
	String username; //all player stuff
	String[] weaponArray;
	int coins;
	int seconds;
	int levelsCompleted;
	int currentLevel;
	int killed; //zombies killed
	String leftIMG, rightIMG, leftIMG2, rightIMG2;
	
	LevelOne lvl1; //instance of panels
	LevelTwo lvl2;
	LevelThree lvl3;
	weaponsPanel weapon;
	endlevelPanel endlevel;
	endgamePanel endgame;
	highscoresPanel hs;
	gameoverPanel gameover;
	newgamePanel newgame;
	//sound stuff
	AudioInputStream musicSound, death1Sound, death2Sound, swordSound, shotSound, drumSound;
	Clip music; //background music
	Clip death1, death2; //zombie death sounds
	Clip sword; //sword cutting noise
	Clip shot; //gunfire
	Clip drum; //deep drum sound effect
	public static Mixer mixer;
	public MainLayout() //constructor initializes starting values of the player, also uses the try catch block to create clips and audio systems for each of the game sounds
	{
		weaponArray = new String[]{"sword"};
		username = "";
		killed = 0;
		seconds = 0;
		coins = 0;
		try{ //creating sound
			File musicFile = new File("../music1.wav"); //creating sound files
			File death1File = new File("../death1.wav");
			File death2File = new File("../death2.wav");
			File swordFile = new File("../sword.wav");
			File shotFile = new File("../shot.wav");
			File drumFile = new File("../drum.wav");
			musicSound = AudioSystem.getAudioInputStream(musicFile); //creating audio systems for each of them corresponding to their sound file
			death1Sound = AudioSystem.getAudioInputStream(death1File);
			death2Sound = AudioSystem.getAudioInputStream(death2File);
			swordSound = AudioSystem.getAudioInputStream(swordFile);
			shotSound = AudioSystem.getAudioInputStream(shotFile);
			drumSound = AudioSystem.getAudioInputStream(drumFile);
			music = AudioSystem.getClip(); //opening all sound files. this is not the same as playing them.
			music.open(musicSound);
			death1 = AudioSystem.getClip();
			death1.open(death1Sound);
			death2= AudioSystem.getClip();
			death2.open(death2Sound);
			sword= AudioSystem.getClip();
			sword.open(swordSound);
			shot= AudioSystem.getClip();
			shot.open(shotSound);
			drum= AudioSystem.getClip();
			drum.open(drumSound);
		}catch(Exception e){
			System.out.println("An error with the sound occured.");
		}
	}
	public static void main(String[] args) //main method creates instance of mainlayout, starts playing the background music, and calls createandshowgui
	{
		MainLayout helloThere = new MainLayout();
		helloThere.loopMusic();
		helloThere.createAndShowGUI();
	}
	public void loopMusic() //loops the clip 'music' which contains the background music 20 times 
	{
		music.setFramePosition(0);
		music.loop(20);
	}
	public void playDrum() //plays a deep drum sound effect. played when text appears one by one on endlevelpanel and endgamepanel (this method is called in endlevelpanel and endgamepanel)
	{
		drum.setFramePosition(0);
		drum.start();
	}
	public void playDeath() //plays a random zombie death sound effect. played when a zombie is killed by the player. death1 and death2 are clips with sound effects. method is called in all the level panels
	{
		int random = (int)(Math.random()*2)+1;
		if(random==1)
		{
			death1.setFramePosition(0);
			death1.start();
		}
		else
		{
			death2.setFramePosition(0);
			death2.start();
		}
	}
	public void playSword() //plays a sword slicing sound effect. played when user clicks mouse while holding sword.method is called in all the level panels
	{
		sword.setFramePosition(0);
		sword.start();
	}
	public void playShot() //plays a gun shot sound effect. called when player fires any gun
	{
		shot.setFramePosition(0);
		shot.start();
	}
	private void createAndShowGUI() //creates and edits frame , and then calls the method addComponentToPane which creates the panels
	{
		JFrame frame = new JFrame("Dungeon of the Dead");
		frame.setSize(700, 525);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.black);
    
		addComponentToPane(frame.getContentPane());
		frame.setResizable(false);
		frame.setLocation(100,100);
		frame.setVisible(true);
	}
	public void addComponentToPane(Container pane) //creates all of the panels except for the end game panel and then adds them to the card layout.
	{
		homePanel home = new homePanel(); 
		instPanel inst = new instPanel(this); //passing in an instance of this class (mainlayout) to each panel in a different class
		controlsPanel controls = new controlsPanel(this);
		hs = new highscoresPanel(this);
		newgame = new newgamePanel(this);
		whichplayerPanel which = new whichplayerPanel(this);
		gameover = new gameoverPanel(this);
		weapon = new weaponsPanel(this);
		cards = new JPanel(new CardLayout());
		cards.add(home, "Home Page");
		cards.add(inst, "Instructions");
		cards.add(controls, "Controls");
		cards.add(hs, "High Scores");
		cards.add(newgame, "New Game");
		cards.add(which, "Which Player");
		cards.add(gameover, "Game Over");
		cards.add(weapon, "Weapons");
		pane.add(cards, BorderLayout.CENTER);
		cl = (CardLayout)(cards.getLayout());
		cl.show(cards, "Home Page"); //home page appears first
	}
	public void createButton(Graphics g, int x, int y, int width, int height, Image img, int x2, int y2, int w2, int h2) //creates custom button instead of normal JButton from a button template that I found online. takes in the coordinates of where the button is
	{
		Image template = new ImageIcon("../images/buttonTemplate.png").getImage();
		g.drawImage(template, x, y, width, height,null);
		g.drawImage(img, x2, y2, w2, h2,null);
	}
	public void showHome() //shows home panel and also resets stats. this method can be accessed by other classes
	{
		cl.show(cards, "Home Page");
		username = "";
		weaponArray = null;
		currentLevel = 1;
		levelsCompleted = 0;
		seconds = 0;
		coins = 0;
	}
	public void choosePlayer(String name) //takes in the username shows the select player panel that asks user to pick an avatar. this method is called from newgamePanel
	{
		username = name;
		cl.show(cards, "Which Player");
	}
	public void startGame(String leftFacingIMG, String rightFacingIMG, String walking1, String walking2) //startGame() is called from the whichplayerPanel class. takes in 4 strings that are the strings of the images (for ex ../images/male-right.png) of the player's chosen avatar
	{
		weapon.reset(); //resets the weapons to just sword
		weaponArray = new String[]{"sword"};
		killed = 0;
		seconds = 0;
		coins = 0;
		leftIMG = leftFacingIMG; //images of player
		rightIMG = rightFacingIMG;
		leftIMG2 = walking1;
		rightIMG2 = walking2;
		lvl1 = new LevelOne(this, leftFacingIMG, rightFacingIMG, walking1, walking2);
		cards.add(lvl1, "Level 1");
		cl.show(cards, "Level 1");
	}
	public void gameOver(int completed) //shows game over panel. is called when user dies in any level.
	{
		playDrum(); //plays sound effect
		levelsCompleted = completed; 
		gameover.resetText();
		cl.show(cards, "Game Over");
	}
	public void showWeapons() //shows the weapon panel where the user can buy weapons. this method is called when a JButton is pressed in endlevelpanel
	{
		weapon.weapons = weaponArray; //gives the current weapon array to the weapons panel so it knows what the user owns
		weapon.getOwnedWeapons();
		weapon.note.setText("");
		cl.show(cards, "Weapons");
	}
	public void takeInWeaponsArray(String[] newWeapons) //after the user buys a weapon, the array of weapons is expanded. this method is called from weaponsPanel (where the user buys weapons) to update the contents of the array
	{
		weaponArray = newWeapons;
	}
	public void goBackToEndLevel() //shows the end level panel. this method is called from weaponsPanel after the user presses 'back'.
	{
		cl.show(cards, "End Level");
	}
	public void endLevel(int level, int time, int coinsEarned, int zombiesKilled, String[] w)//what to do at the end of levels 1 and 2. takes in player's updated stats, and then shows the "end level panel"
	{
		weaponArray = w; //takes in player's data
		seconds+=time;
		coins=coinsEarned;
		killed+=zombiesKilled;
		currentLevel = level;
		endlevel = new endlevelPanel(this);
		endlevel.reset(level, time, zombiesKilled);
		cards.add(endlevel, "End Level");
		cl.show(cards, "End Level");
	}
	public void nextLevel() //called at the end of each level (1 and 2 only). creates the instance of the next level and shows the new panel.
	{
		if(currentLevel==1)
		{
			appendStatus(2); //saves the player's data. 2 means the next level = 2
			lvl2 = new LevelTwo(this, leftIMG, rightIMG, leftIMG2, rightIMG2, coins, weaponArray);
			cards.add(lvl2, "Level 2");
			cl.show(cards, "Level 2");
		}
		else if(currentLevel==2)
		{
			appendStatus(3);
			lvl3 = new LevelThree(this, leftIMG, rightIMG, leftIMG2, rightIMG2, coins, weaponArray);
			cards.add(lvl3, "Level 3");
			cl.show(cards, "Level 3");
		}
	}
	public void endGame(int s, int z) //called once the user successfully completes the final level. creates the panel endgame and shows it. also takes in the amount of time and zombies killed
	{
		seconds+=s;
		killed+=z;
		endgame = new endgamePanel(this);
		endgame.reset(seconds,killed); //endgame takes in stats
		cards.add(endgame, "End Game");
		cl.show(cards, "End Game");
		hs.reset(); //resets the high scores panel now that the user's new score should be there
	}
	public void appendStatus(int spawnLevel) //once user presses next level, the progress is saved. the method prints out stats of the player in a text file, so next time, the player can come back and play
	{
		PrintWriter pw = null; 
		File outFile = new File("../Saves.txt");
		try
		{  
			pw = new PrintWriter( new FileWriter(outFile, true) ); 
		}
		catch ( IOException e)
		{
			System.out.println("There was an error trying to locate Saves.txt. Your progress is not stored.");
			System.exit(1);
		}
		pw.print(""+username+":"+spawnLevel+"!"); //prints out stats in Saves.txt using a specific format
		for(int i = 0;i<weaponArray.length;i++)
		{
			pw.print(weaponArray[i]);
			if(i!=weaponArray.length-1) //printing out the weapons the user owns
				pw.print(",");
		}
		pw.println("!"+coins+";"+killed+"*"+seconds+"&"+leftIMG+"$"+rightIMG); 
		//prints out the information in this format: [username]:[next level]![{weapon name, weapon name, ...}]![total coins];[total killed]*[total time so far]&[image string 1]$[image string 2]
		pw.close();
	}
	public void loadSave(String le, String ri, int l, int c, int k, int t, String[] w, String user) //loads the saved level that the user didn't finish. takes in a bunch of the user's stats: image strings, coins, killed, time, weapons array, username and passes the information to the level
	{
		username = user;
		leftIMG = le;
		leftIMG2 = leftIMG.substring(0,leftIMG.length()-4)+"-walking.png";
		rightIMG = ri;
		rightIMG2 = rightIMG.substring(0,rightIMG.length()-4)+"-walking.png";
		currentLevel = l;
		levelsCompleted = l-1;
		coins = c;
		killed = k;
		seconds = t;
		weaponArray = w;
		if(currentLevel==2)
		{
			lvl2 = new LevelTwo(this, leftIMG, rightIMG, leftIMG2, rightIMG2, coins, weaponArray); //passing info into the level
			cards.add(lvl2, "Level 2");
			cl.show(cards, "Level 2");
		}
		else if(currentLevel==3)
		{
			lvl3 = new LevelThree(this, leftIMG, rightIMG, leftIMG2, rightIMG2, coins, weaponArray);
			cards.add(lvl3, "Level 3");
			cl.show(cards, "Level 3");
		}
	}
	class homePanel extends JPanel implements MouseListener //code for the main page panel. appears first when game is opened
	{
		int[] xArray = {300,500,500,300};
		
		int[] instArray = {132,132,166,166};//since buttons are not JButtons, I used the contains() methods in polygons matching the locations of the four buttons to tell when one is pressed
		
		//polygons
		Polygon instButton = new Polygon(xArray, instArray, 4);
		int[] controlArray = {183,183,217,217};
		Polygon controlButton = new Polygon(xArray, controlArray, 4);
		int[] highscoresArray = {234,234,268,268};
		Polygon highscoresButton = new Polygon(xArray, highscoresArray, 4);
		int[] newgameArray = {283,283,317,317};
		Polygon newgameButton = new Polygon(xArray, newgameArray, 4);
		
		public homePanel()//constructor adds mouse listener to panel
		{
			addMouseListener(this);
		}
		public void mousePressed(MouseEvent e) //which of the four buttons is pressed
		{
			if(instButton.contains(e.getX(), e.getY()))  //instButton is a polygon above. it has the coordinates of where the button is, but i'm using the polygon's contain method because my custom buttons are not JButtons so I can't use actionlistener
			{
				cl.show(cards, "Instructions");
			}
			else if(controlButton.contains(e.getX(), e.getY()))
			{
				cl.show(cards, "Controls");
			}
			else if(highscoresButton.contains(e.getX(), e.getY()))
			{
				cl.show(cards, "High Scores");
			}
			else if(newgameButton.contains(e.getX(), e.getY()))
			{
				newgame.startSave.setVisible(false);
				cl.show(cards, "New Game");
			}
		}
		public void paintComponent(Graphics g) //adds images to home page
		{
			int buttonW = 250;
			int buttonH = 60;
			super.paintComponent(g);
			Image TITLE_BACKGROUND = new ImageIcon("../images/background-title.png").getImage();
			g.drawImage(TITLE_BACKGROUND,0,0,700,500,null);
			Image TITLE_ZOMBIE = new ImageIcon("../images/title-zombie.png").getImage();
			g.drawImage(TITLE_ZOMBIE,490,30,270,700,null);
			Image TITLE_DUNGEON = new ImageIcon("../images/title-dungeon.png").getImage();
			g.drawImage(TITLE_DUNGEON,45,130,240,85,null);
			Image TITLE_OFTHE = new ImageIcon("../images/title-ofthe.png").getImage();
			g.drawImage(TITLE_OFTHE,98,210,120,45,null);
			Image TITLE_DEAD = new ImageIcon("../images/title-dead.png").getImage();
			g.drawImage(TITLE_DEAD,55,260,220,80,null);
			Image B_INSTRUCTIONS = new ImageIcon("../images/b-home-instructions.png").getImage();
			createButton(g,280,120,buttonW, buttonH,B_INSTRUCTIONS,313,140,160,25);
			Image B_CONTROLS = new ImageIcon("../images/b-home-controls.png").getImage();
			createButton(g,280,170,buttonW, buttonH,B_CONTROLS,325,190,140,25);
			Image B_HIGHSCORES = new ImageIcon("../images/b-home-highscores.png").getImage();
			createButton(g,280,220,buttonW, buttonH,B_HIGHSCORES,316,240,160,25);
			Image B_NEWGAME = new ImageIcon("../images/b-home-newgame.png").getImage();
			createButton(g,280,270,buttonW, buttonH,B_NEWGAME,320,290,150,25);
		}
		public void mouseClicked(MouseEvent e){} //required method but does nothing
		public void mouseEntered(MouseEvent e){} //required method but does nothing
		public void mouseExited(MouseEvent e){} //required method but does nothing
		public void mouseReleased(MouseEvent e){} //required method but does nothing
	}//end of home panel class
}
