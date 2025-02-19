import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
class LevelOne extends JPanel implements ActionListener, KeyListener, MouseListener //main code for level one. controls all the rooms and the player of the first level
{
	
	MainLayout dungeon;
	int coinsEarned;
	int health;
	int zombiesKilled;
	Player player;
	Timer runtimer; //actionperformed called very frequently so player movement changes a lot
	Timer deadDelay; //delay for 2 seconds before showing game over
	int seconds;
	int fractions; //changes every 0.02 seconds because of runtimer
	Room currentRoom;
	String currentFile;
	String weapon;
	String[] weapons;
	boolean crashed;
	boolean attacked; //while being attacked by zombies
	boolean zombiecrash; //if zombie crashed into object
	//rooms
	Room room1;
	Room room2;
	Room room2_deadend;
	Room room3;
	Room room4;
	Room room5;
	Zombie[] zombies;
	Stats stats;
	public LevelOne(MainLayout dl, String str1, String str2, String str3, String str4) //constructor adds key listener, starts timers, and initializes values including where to find player images
	{
		setLayout(null);
		currentFile = "lvl1-room1.png";
		addKeyListener(this);
		addMouseListener(this);
		dungeon = dl;
		setUpPage(str1, str2, str3, str4);
		fractions = 0; 
		seconds = 0;
		health = 100;
		coinsEarned = 0;
		zombiesKilled = 0;
		attacked = false;
		weapons = new String[]{"sword"};
		weapon = weapons[0];
		deadDelay = new Timer(2000, this);
		runtimer = new Timer(20,this);
		runtimer.start();
	}
	public void paintComponent(Graphics g)//paints the current background image and also the moved player and the array of zombies. also calculates if a zombie is dead or not, and checks the collisions of the player by calling checkPlayerCollisions 
	{
		Image currentIMG = new ImageIcon(("../images/"+currentFile)).getImage();
		g.drawImage(currentIMG,0,0,700,500,null); 
		player.move();
		checkPlayerCollisions(); //has the player bumped into a wall or object
		stats.draw(g, health, weapon, attacked, coinsEarned);
		for(int i = 0;i<zombies.length;i++)//draw zombies from the array
		{
			if(zombies[i].swordHit){ //if zombie is being hit by the sword
				zombies[i].drawSwordHit(g, player.xcoor, player.ycoor, player.left);
				if(zombies[i].health<=0)
				{
					if(zombies[i].alive){
						 coinsEarned+=(60+(10*(int)(Math.random()*3)));
						 zombiesKilled++;
						 dungeon.playDeath();
					 }
					zombies[i].alive = false;
					if(currentRoom.zombieAmt>0) currentRoom.zombieAmt--;
				}
			}
			zombies[i].determineDirection(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT);
			zombies[i].move();
			zombiecrash = currentRoom.checkCollision(zombies[i].xcoor, zombies[i].ycoor, zombies[i].WIDTH, zombies[i].HEIGHT); //has a zombie bumped into a wall or object
			if(zombies[i].xcoor==player.xcoor && zombies[i].ycoor==player.ycoor)
				zombiecrash = true; //zombiecrash means the zombie cannot move because they have hit something
			if(checkZombieOverlapping(i, zombies.length)) zombiecrash = true; //is zombie overlapping another zombie
			zombies[i].draw(g, zombies[i].whichImage, zombiecrash, zombies[i].health);
		}
		player.draw(g, player.whichImage, crashed, weapon); //draw and move are methods in the player class. draw takes graphics g in order to draw, and a string called whichImage, a field variable in the player class to know what image of the player is to be displayed
	}
	public void setUpPage(String str1, String str2, String str3, String str4) //adds room panels to the card layout, also creates the objects in each room to avoid and passes them to the room class
	{
		//creation of room 1
		Polygon[] room1doors = new Polygon[]{new Polygon(new int[]{695,700,700,695}, new int[]{200,200,300,300}, 4)}; //exit locations of the first room
		room1 = new Room(new Polygon[]{new Polygon(new int[]{50,100,100,50}, new int[]{50,50,125,125}, 4), new Polygon(new int[]{0,700,700,600,600,0}, new int[]{0,0,200,200,50,50},6), new Polygon(new int[]{0,50,50,600,600,700,700,0}, new int[]{50,50,450,450,300,300,500,500}, 8)}, room1doors, 0, 0);
		
		//creation of room 2
		Polygon room2object1 = new Polygon(new int[]{0,300,300,250,250,150,150,50,50,0}, new int[]{0,0,50,50,100,100,150,150,200,200},10);
		Polygon room2object2 = new Polygon(new int[]{400,700,700,400,400,550,550,650,650,550,550,400}, new int[]{0,0,500,500,450,450,400,400,100,100,50,50}, 12);
		Polygon room2object3 = new Polygon(new int[]{0,50,50,150,150,300,300,0}, new int[]{300,300,400,400,450,450,500,500},8);
		Polygon room2object4 = new Polygon(new int[]{310,390,390,310}, new int[]{210,210,290,290}, 4);
		Polygon[] room2objects = new Polygon[]{room2object1,room2object2,room2object3,room2object4};
		Polygon[] room2doors = new Polygon[]{new Polygon(new int[]{300,400,400,300}, new int[]{490,490,500,500}, 4), new Polygon(new int[]{300,400,400,300}, new int[]{0,0,10,10}, 4), new Polygon(new int[]{0,10,10,0}, new int[]{200,200,300,300}, 4)};
		room2 = new Room(room2objects, room2doors, 3, 0);
		
		//creation of room that leads to nowhere
		Polygon[] room2dobjects = new Polygon[]{new Polygon(new int[]{400,600,600,570,570,600,600,50,50,300,300,0,0,700,700,400}, new int[]{450,450,400,380,180,150,50,50,450,450,500,500,0,0,500,500}, 16)};
		Polygon[] room2ddoors = new Polygon[]{new Polygon(new int[]{300,400,400,300}, new int[]{490,490,500,500}, 4)};
		room2_deadend = new Room(room2dobjects, room2ddoors, 4, 0);
		
		//creation of room 3
		Polygon room3object1 = new Polygon(new int[]{300,300,100,100,450,450,600,600,700,700,0,0}, new int[]{0,50,50,350,350,400,400,350,350,500,500,0},12);
		Polygon room3object2 = new Polygon(new int[]{400,400,600,600,700,700}, new int[]{0,100,100,250,250,0},6);
		Polygon[] room3objects = new Polygon[]{room3object1, room3object2, new Polygon(new int[]{450,500,500,450}, new int[]{200,200,250,250},4)};
		Polygon[] room3doors = new Polygon[]{new Polygon(new int[]{690,700,700,690}, new int[]{250,250,350,350}, 4), new Polygon(new int[]{300,400,400,300}, new int[]{0,0,10,10},4)};
		room3 = new Room(room3objects, room3doors, 3, 0);
		
		//creation of room 4
		Polygon room4object1 = new Polygon(new int[]{0,0,700,700,550,550,250,250,155,155,100,100}, new int[]{250,0,0,500,500,100,100,180,180,100,100,250},12);
		Polygon room4object2 = new Polygon(new int[]{0,100,100,300,300,350,350,450,450,0}, new int[]{350,350,400,400,350,350,300,300,500,500}, 10);
		Polygon[] room4objects = new Polygon[]{room4object1, room4object2};
		Polygon[] room4doors = new Polygon[]{new Polygon(new int[]{450,550,550,450}, new int[]{490,490,500,500},4), new Polygon(new int[]{0,10,10,0}, new int[]{250,250,350,350}, 4)};
		room4 = new Room(room4objects, room4doors, 3, 0);
		
		//creation of room 5
		Polygon room5object1 = new Polygon(new int[]{450,450,150,150,100,100,150,150,0,0}, new int[]{0,100,100,150,150,400,400,500,500,0}, 10);
		Polygon room5object2 = new Polygon(new int[]{550,700,700,250,250,300,300,600,600,550}, new int[]{0,0,500,500,400,400,350,350,100,100}, 10);
		Polygon[] room5objects = new Polygon[]{room5object1, room5object2, new Polygon(new int[]{450,500,500,450}, new int[]{200,200,250,250}, 4)};
		Polygon[] room5doors = new Polygon[]{new Polygon(new int[]{450,550,550,450}, new int[]{0,0,10,10},4), new Polygon(new int[]{150,250,250,150}, new int[]{460,460,500,500}, 4)};
		room5 = new Room(room5objects, room5doors, 3, 0);
		
		currentRoom = room1;//starts off at room1
		stats = new Stats();
		createZombies();
		player = new Player(str1, str2, str3, str4, 200,150); //spawns the player
	}
	public void actionPerformed(ActionEvent e)//required method. counts time and while the timer is running, constantly repaints and checks if the player has bumped into anything, or responds to the shop button
	{
		if(e.getSource()==deadDelay){  //dead delay = timer which starts when you die. it makes the screen freeze for 2 seconds before showing the game over panel
			deadDelay.stop();
			dungeon.gameOver(0);
		}
		else //the runtimer is the actionevent. runtimer is called every 0.02 of a second so that the player's and the zombie's movements are shown smoothly
		{
			fractions++;
			if(fractions==50) //each fraction is 1/50 of a second
			{
				seconds++;
				fractions = 0;
			}
			if(fractions%10==0){
				player.changeAnimation();
				attacked = checkZombieCollisions();
			}
			if(health<=0)
				fail();
			repaint();
			grabFocus();
		}
	}
	public void keyPressed(KeyEvent e) //required method. if a key is pressed then i call the keyPressed method on the player class so it knows to move
	{
		player.keyPressed(e, player.left);
	}
	public void keyReleased(KeyEvent e) //required method. if a key is released then i call the keyReleased method on the player class so it knows to stop moving
	{
		player.keyReleased(e);
	}
	public void keyTyped(KeyEvent e){} //required method but does nothing
	public void createZombies() //creates an array of zombies for each room based on how many zombies are supposed to be in the room
	{
		zombies = new Zombie[currentRoom.zombieAmt];
		int x = 200;
		int y = 100;
		for(int i = 0;i<zombies.length;i++)
		{
			zombies[i] = new Zombie("../images/zombie-generic-left.png","../images/zombie-generic-right.png", 50, 80, 2, 50, 3);
			x+=zombies[i].WIDTH+40;
			boolean error = true;
			while(error)
			{
				y+=50;
				error = currentRoom.checkCollision(x,y,zombies[i].WIDTH, zombies[i].HEIGHT);
				if(y>350)
				{
					y=100;
					x+=20;
				}
			}
			zombies[i].set(x,y);
		}
	}
	
	public boolean checkZombieCollisions() //checks if zombie has touched player. if so, player's health needs to lower. returns true if zombie has touched, false if not.
	{
		for(int i = 0;i<zombies.length;i++)
		{
			Zombie z = zombies[i];
			Polygon playerBody = new Polygon(new int[]{player.xcoor, player.xcoor+player.WIDTH, player.xcoor+player.WIDTH, player.xcoor}, new int[]{player.ycoor, player.ycoor, player.ycoor+player.HEIGHT, player.ycoor+player.HEIGHT} , 4);
			if(playerBody.contains(z.xcoor+z.WIDTH/2, z.ycoor+z.HEIGHT/4) && zombies[i].alive)
			{
				health-=z.damage;
				return true;
			}
		}
		return false;
	}
	public boolean checkZombieOverlapping(int n, int length) //checks if zombies are overlapping with another in its array. if so, return true.
	{
		for(int j = 0;j<length;j++) //do zombies overlap with each other
		{
			if(j!=n && zombies[j].alive && zombies[n].alive)
			{
				Polygon body = new Polygon(new int[]{zombies[j].xcoor, zombies[j].xcoor+zombies[j].WIDTH, zombies[j].xcoor+zombies[j].WIDTH,zombies[j].xcoor}, new int[]{zombies[j].ycoor,zombies[j].ycoor,zombies[j].ycoor+zombies[j].HEIGHT, zombies[j].ycoor+zombies[j].HEIGHT}, 4);
				int x = zombies[n].xcoor;
				int y = zombies[n].ycoor;
				int w = zombies[n].WIDTH;
				int h = zombies[n].HEIGHT;
				if(body.contains(x+10,y+10) || body.contains(x+w-10,y+10) || body.contains(x+10,y+h-10) || body.contains(x+w-10, y+h-10))
					return true; //overlap
			}
		}
		return false;
	}
	public void mousePressed(MouseEvent e){//if the user clicks while holding a weapon, fires that weapon and determines whether the weapon can harm a zombie
		if(weapon.equals("sword")){
			dungeon.playSword();
			player.drawSwordAnimation(e.getX(), e.getY());
			Polygon body;
			Polygon area;
			for(int i = 0;i<zombies.length;i++) //checking if the sword hits the zombie and if the zombie is close enough
			{
				body = new Polygon(new int[]{zombies[i].xcoor-10, zombies[i].xcoor+zombies[i].WIDTH+10, zombies[i].xcoor+zombies[i].WIDTH+10, zombies[i].xcoor-10}, new int[]{zombies[i].ycoor, zombies[i].ycoor, zombies[i].ycoor+zombies[i].HEIGHT, zombies[i].ycoor+zombies[i].HEIGHT}, 4);
				area = new Polygon(new int[]{player.xcoor-60, player.xcoor+player.WIDTH+60, player.xcoor+player.WIDTH+60, player.xcoor-60}, new int[]{player.ycoor-60, player.ycoor-60, player.ycoor+player.HEIGHT+60, player.ycoor+player.HEIGHT+60}, 4);
				if(body.contains(e.getX(), e.getY()) && area.contains(e.getX(), e.getY()))
					zombies[i].swordHit = true;
			}
		}
	}
	public void mouseClicked(MouseEvent e){} //required method but does nothing
	public void mouseEntered(MouseEvent e){} //required method but does nothing
	public void mouseExited(MouseEvent e){} //required method but does nothing
	public void mouseReleased(MouseEvent e){} //required method but does nothing
	public void fail() //player is dead and so wait 2 seconds before showing game over panel
	{
		runtimer.stop();
		deadDelay.start();
	}
	public void checkPlayerCollisions() //checks if player bumped into wall, object, or if they have entered a door
	{
		if(currentRoom.checkCollision(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)) //checkcollision is a method inside the room class
			crashed=true;
		else
			crashed=false;
		
		if(currentFile.equals("lvl1-room1.png"))//room 1
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){ //has the player crossed any doors in room 1
				currentFile = "lvl1-room2.png";
				currentRoom = room2;
				createZombies();
				player.spawn(55, 210);
			}
		}
		else if(currentFile.equals("lvl1-room2.png"))//room2
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl1-room3.png";
				currentRoom = room3;
				createZombies();
				player.spawn(325, 40);
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl1-room2-dead.png";
				currentRoom = room2_deadend;
				createZombies();
				player.spawn(320, 360);
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==3){
				currentFile = "lvl1-room1.png";
				currentRoom = room1;
				createZombies();
				player.spawn(640, 210);
			}
		}
		else if(currentFile.equals("lvl1-room2-dead.png"))//dead end room connecting to room 2
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl1-room2.png";
				currentRoom = room2;
				player.spawn(325, 40);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl1-room3.png"))//room 3
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl1-room4.png";
				currentRoom = room4;
				createZombies();
				player.spawn(50, 260);
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl1-room2.png";
				currentRoom = room2;
				createZombies();
				player.spawn(325, 375);
			}
		}
		else if(currentFile.equals("lvl1-room4.png"))//room 4
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl1-room5.png";
				currentRoom = room5;
				createZombies();
				player.spawn(470, 40);
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl1-room3.png";
				currentRoom = room3;
				createZombies();
				player.spawn(610, 260);
			}
		}
		else if(currentFile.equals("lvl1-room5.png"))//room 5
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl1-room4.png";
				currentRoom = room4;
				createZombies();
				player.spawn(470, 375);
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){ //the stairs is not technically a door but i mark it anyways so i can tell when the player touches it. now the game ends.
				runtimer.stop();
				dungeon.endLevel(1, seconds, coinsEarned, zombiesKilled, weapons);
			}
		}
	}
}
