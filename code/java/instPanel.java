import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.Font;
import java.awt.event.*;
class instPanel extends JPanel implements MouseListener //panel that appears when user presses "instructions" on home screen
{
	Font deadFont;
	int[] xcoor = {470, 670,670,470};
	int[] ycoor = {413,413,447,447};
	Polygon backButton = new Polygon(xcoor,ycoor,4);
	MainLayout dungeon;
	public instPanel(MainLayout dl) //constructor uses a try catch block to initialize custom font
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
		addMouseListener(this);
		setUpPage();
		dungeon = dl;
	}
	public void setUpPage() //adds JLabels and all that stuff
	{
		JLabel title = new JLabel("HOW TO PLAY:");
		title.setFont(deadFont);
		title.setForeground(Color.white);
		title.setLocation(270,30);
		add(title);
		String[] lines = new String[7];
		lines[0] = "mission: escape the dungeon while killing off zombies";
		lines[1] = "try to beat all three levels";
		lines[2] = "advance to the next level by finding stairs";
		lines[3] = "earn coins by killing zombies";
		lines[4] = "use coins to buy better weapons";
		lines[5] = "if you die, the game is over";
		lines[6] = "                  Good Luck!                  ";
		int y = 70;
		for(int i = 0; i<lines.length;i++)
		{
			JLabel temp = new JLabel(lines[i]);
			temp.setFont(deadFont);
			temp.setLocation(30, y);
			temp.setForeground(Color.white);
			add(temp);
			y+=40;
		}
	}
	public void paintComponent(Graphics g) //paints background image for inst panel
	{
		super.paintComponent(g);
		Image BACKGROUND = new ImageIcon("../images/background-inst-controls.png").getImage();
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
	public void mousePressed(MouseEvent e) //if press back button, goes back to home
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
}
