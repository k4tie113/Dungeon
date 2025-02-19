import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
class LevelTwo extends JPanel implements ActionListener, KeyListener, MouseListener //main code for level two. controls & paints all the rooms and the player of the first level
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
	int bulletDirection; 
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
	Room room3;
	Room room3_dead1;
	Room room3_dead2;
	Room room4;
	Room room4_dead;
	Room room5;
	Room room6;
	Timer rof_Revolver, rof_Shotgun, rof_AK, rof_Mini; //rate of fire
	Boolean revolverFire, shotgunFire, akFire, miniFire; //whether a gun is able to fire.
	Zombie[] zombies;
	Stats stats;
	Image bulletLeft, bulletRight;
	Gun revolver, shotgun, AK, mini;
	public LevelTwo(MainLayout dl, String str1, String str2, String str3, String str4, int c, String[] w) //constructor adds key listener, starts timers, and initializes values including where to find player images
	{
		miniFire = false;
		akFire = false;
		revolverFire = true;
		shotgunFire = true;
		setLayout(null);
		currentFile = "lvl2-room1.png";
		addKeyListener(this);
		addMouseListener(this);
		dungeon = dl;
		setUpPage(str1, str2, str3, str4);
		fractions = 0; 
		seconds = 0;
		health = 100;
		coinsEarned = c;
		zombiesKilled = 0;
		bulletDirection = 0;
		attacked = false;
		weapons = w;
		weapon = weapons[player.weaponIndex];
		bulletLeft = new ImageIcon("../images/bullet-left.png").getImage();
		bulletRight = new ImageIcon("../images/bullet-right.png").getImage();
		int extraDamage = 0;
		if(str1.equals("../images/soldier-left.png")) extraDamage = 5; //soldiers do extra damage
		else if(str1.equals("../images/ninja-left.png")) extraDamage = -2; //ninjas are bad at using guns (maybe?)
		revolver = new Gun(35+extraDamage);
		shotgun = new Gun(15+extraDamage);
		AK = new Gun(25+extraDamage);
		mini = new Gun(35+extraDamage);
		deadDelay = new Timer(2000, this);
		rof_Revolver = new Timer(700, this); //revolver can fire every 0.7 seconds
		rof_Shotgun = new Timer(500, this);
		rof_AK = new Timer(171, this);
		rof_Mini = new Timer(120, this);
		runtimer = new Timer(20,this);
		runtimer.start();
	}
	public void paintComponent(Graphics g)//paints the current background image and also the moved player and the array of zombies. also calculates if a zombie is dead, or is colliding with an object
	{
		Image currentIMG = new ImageIcon(("../images/"+currentFile)).getImage();
		g.drawImage(currentIMG,0,0,700,500,null); 
		player.move();
		checkPlayerCollisions();
		stats.draw(g, health, weapon, attacked, coinsEarned);
		if(weapon.equals("revolver")) revolver.draw(g);
		else if(weapon.equals("shotgun")) shotgun.draw(g);
		else if(weapon.equals("ak47")) AK.draw(g);
		else if(weapon.equals("minigun")) mini.draw(g);
		for(int i = 0;i<zombies.length;i++)//draw zombies from the array
		{
			if(zombies[i].swordHit){ //if zombie is being hit by the sword
				zombies[i].drawSwordHit(g, player.xcoor, player.ycoor, player.left);
				if(zombies[i].health<=0)
				{
					if(zombies[i].alive){
						coinsEarned+=(20+(10*(int)(Math.random()*3)));
						zombiesKilled++;
						zombies[i].alive = false;
						dungeon.playDeath();
						if(currentRoom.zombieAmt>0) currentRoom.zombieAmt--;
						if(zombies[i].left.equals("../images/zombie-fast-left.png")) currentRoom.zombies2--;
					}
				}
			}
			if(zombies[i].health<=0) //killed by something other than a sword
			{
				if(zombies[i].alive){
					coinsEarned+=(50+(10*(int)(Math.random()*3)));
					zombiesKilled++;
					zombies[i].alive = false;
					dungeon.playDeath();
					if(currentRoom.zombieAmt>0) currentRoom.zombieAmt--;
					if(zombies[i].left.equals("../images/zombie-fast-left.png")) currentRoom.zombies2--;
				}
			}
			zombies[i].determineDirection(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT);
			zombies[i].move();
			zombiecrash = currentRoom.checkCollision(zombies[i].xcoor, zombies[i].ycoor, zombies[i].WIDTH, zombies[i].HEIGHT);
			if(zombies[i].xcoor==player.xcoor && zombies[i].ycoor==player.ycoor)
				zombiecrash = true;
			if(checkZombieOverlapping(i, zombies.length)) zombiecrash = true;
			zombies[i].draw(g, zombies[i].whichImage, zombiecrash, zombies[i].health);
		}
		player.draw(g, player.whichImage, crashed, weapon); //draw and move are methods in the player class. draw takes graphics g in order to draw, and a string called whichImage, a field variable in the player class to know what image of the player is to be displayed
	}
	public void setUpPage(String str1, String str2, String str3, String str4) //adds room panels to the card layout, also creates the objects to avoid that are passed into the room constructor
	{
		Polygon room1object1 = new Polygon(new int[]{0,50,50,350,350,400,400,600,600,700,700,0}, new int[]{0,0,450,450,400,400,350,350,200,200,500,500}, 12);
		Polygon room1object2 = new Polygon(new int[]{50,50,700,700,600,600,450,450,350,350}, new int[]{50,0,0,100,100,50,50,250,250,50}, 10);
		Polygon room1object3 = new Polygon(new int[]{50,150,150,50}, new int[]{50,50,100,100},4);
		room1 = new Room(new Polygon[]{room1object1, room1object2, room1object3}, new Polygon[]{new Polygon(new int[]{690,700,700,690}, new int[]{100,100,200,200}, 4)}, 1, 0);
		
		Polygon room2object1 = new Polygon(new int[]{0,100,100,350,350,700,700,0}, new int[]{200,200,450,450,400,400,500,500}, 8);
		Polygon room2object2 = new Polygon(new int[]{0,200,200,300,300,500,500,700,700,0}, new int[]{100,100,300,300,100,100,300,300,0,0}, 10);
		Polygon room2object3 = new Polygon(new int[]{100,200,200,100}, new int[]{400,400,450,450}, 4);
		Polygon room2object4 = new Polygon(new int[]{450,500,500,450}, new int[]{100,100,180,180}, 4);
		Polygon[] room2doors = new Polygon[]{new Polygon(new int[]{0,10,10,0}, new int[]{100,100,200,200}, 4), new Polygon(new int[]{690,700,700,690}, new int[]{300,300,400,400}, 4)};
		room2 = new Room(new Polygon[]{room2object1, room2object2, room2object3, room2object4}, room2doors, 4, 1);
		
		Polygon room3object1 = new Polygon(new int[]{0,0,100,100,150,150,200,200}, new int[]{0,300,300,150,150,50,50,0}, 8);
		Polygon room3object2 = new Polygon(new int[]{300,700,700,650,650,600,600,550,550,300}, new int[]{0,0,250,250,180,180,100,100,50,50}, 10);
		Polygon room3object3 = new Polygon(new int[]{0,100,100,300,300,400,400,550,550,700,700,0}, new int[]{400,400,450,450,499,499,450,450,350,350,500,500}, 12);
		Polygon[] room3doors = new Polygon[]{new Polygon(new int[]{690,700,700,690}, new int[]{250,250,350,350}, 4), new Polygon(new int[]{300,400,400,300}, new int[]{490,490,500,500}, 4), new Polygon(new int[]{0,10,10,0}, new int[]{300,300,400,400}, 4), new Polygon(new int[]{200,300,300,200}, new int[]{0,0,10,10},4)};
		room3 = new Room(new Polygon[]{room3object1,room3object2,room3object3,new Polygon(new int[]{330,420,420,330}, new int[]{200,200,300,300}, 4)}, room3doors, 5, 2);
		
		Polygon room3d1object1 = new Polygon(new int[]{0,100,100,400,400,500,500,700,700,0}, new int[]{350,350,400,400,250,250,100,100,500,500,}, 10);
		Polygon room3d1object2 = new Polygon(new int[]{0,100,100,700,700,0}, new int[]{250,250,100,100,0,0}, 6);
		room3_dead1 = new Room(new Polygon[]{room3d1object1, room3d1object2}, new Polygon[]{new Polygon(new int[]{0,10,10,0}, new int[]{250,250,350,350}, 4)}, 2, 0);
		
		Polygon room3d2object1 = new Polygon(new int[]{400,400,450,450,550,550,600,600,550,550,700,700}, new int[]{0,100,100,150,150,200,200,400,400,500,500,0}, 12);
		Polygon room3d2object2 = new Polygon(new int[]{0,300,300,200,200,150,150,100,100,550,500,0}, new int[]{0,0,100,100,150,150,200,200,450,450,500,500}, 12);
		room3_dead2 = new Room(new Polygon[]{room3d2object1, room3d2object2, new Polygon(new int[]{150,200,200,150}, new int[]{300,300,400,400},4), new Polygon(new int[]{300,350,350,300}, new int[]{225,225,275,275},4), new Polygon(new int[]{450,500,500,450}, new int[]{250,250,300,300}, 4)}, new Polygon[]{new Polygon(new int[]{300,400,400,300}, new int[]{0,0,10,10}, 4)}, 3, 0);
		
		Polygon room4object1 = new Polygon(new int[]{450,450,500,500,600,600,550,550,400,400,300,300,700,700}, new int[]{0,100,100,150,150,350,350,400,400,450,450,500,500,0}, 14);
		Polygon room4object2 = new Polygon(new int[]{350,0,0,200,200,250,250,1,1,350}, new int[]{0,0,500,500,350,350,200,200,100,100}, 10);
		room4 = new Room(new Polygon[]{room4object1,room4object2}, new Polygon[]{new Polygon(new int[]{200,300,300,200}, new int[]{490,490,500,500}, 4), new Polygon(new int[]{350,450,450,350}, new int[]{0,0,10,10}, 4), new Polygon(new int[]{0,10,10,0}, new int[]{100,100,200,200}, 4)}, 3, 1);
		
		Polygon room4dobject = new Polygon(new int[]{700,0,0,700,700,600,600,450,450,700}, new int[]{0,0,500,500,200,200,350,350,100,100}, 10);
		room4_dead = new Room(new Polygon[]{room4dobject}, new Polygon[]{new Polygon(new int[]{690,700,700,690}, new int[]{100,100,200,200}, 4)}, 1, 1);
		
		Polygon room5object1 = new Polygon(new int[]{700,700,0,0,350,350,100,100,150,150,400,400,500,500,600,600}, new int[]{150,0,0,500,500,400,400,150,150,50,50,100,100,50,50,150}, 16);
		Polygon room5object2 = new Polygon(new int[]{600,700,700,450,450,600}, new int[]{250,250,500,500,400,400}, 6);
		Polygon room5object3 = new Polygon(new int[]{300,400,400,300}, new int[]{180,180,270,270}, 4);
		room5 = new Room(new Polygon[]{room5object1, room5object2, room5object3}, new Polygon[]{new Polygon(new int[]{690,700,700,690}, new int[]{150,150,250,250},4), new Polygon(new int[]{350,450,450,350}, new int[]{490,490,500,500}, 4)}, 4, 2);
		
		Polygon room6object1 = new Polygon(new int[]{0,700,700,0,0,100,100,250,250,650,650,100,100,0}, new int[]{0,0,500,500,250,250,400,400,450,450,100,100,150,150}, 14);
		Polygon room6object2 = new Polygon(new int[]{330,470,470,330}, new int[]{240,240,310,310}, 4);
		room6 = new Room(new Polygon[]{room6object1, room6object2}, new Polygon[]{new Polygon(new int[]{0,10,10,0}, new int[]{150,150,250,250}, 4), new Polygon(new int[]{600,650,650,600}, new int[]{100,100,150,150}, 4)}, 4, 2);
		
		currentRoom = room1;//starts off at room1
		stats = new Stats();
		createZombies();
		player = new Player(str1, str2, str3, str4, 75,360); //spawns the player
	}
	public void actionPerformed(ActionEvent e)//required method. counts time and while the runtimer is running, constantly repaints and checks if the player has bumped into anything. however there are other timers, for example, the ones used for firing the guns at the right time  
	{
		if(e.getSource()==deadDelay){  //dead delay = timer which starts when you die. it makes the screen freeze for 2 seconds before showing the game over panel
			deadDelay.stop();
			dungeon.gameOver(0);
		}
		else if(e.getSource()==rof_Revolver) //rate of fire for the revolver.  
			revolverFire = true;
		else if(e.getSource()==rof_Shotgun) //rate of fire for the shotgun
			shotgunFire = true;
		else if(e.getSource()==rof_AK) //rate of fire for the AK47. unlike the shotgun and revolver, the AK47 is an assault rifle and continuously fires until the player lets go of the mouse
		{
			if(akFire)
			{
				dungeon.playShot();
				if((player.whichImage.equals(player.right) || player.whichImage.equals(player.rightWalking)))
					AK.add(new Bullet(player.xcoor+player.WIDTH+7, player.ycoor+46, bulletRight, 15,bulletDirection));
				else if((player.whichImage.equals(player.left) || player.whichImage.equals(player.leftWalking)))
					AK.add(new Bullet(player.xcoor-12, player.ycoor+46, bulletLeft, -15,bulletDirection));
			}
		}
		else if(e.getSource()==rof_Mini) //like the AK47, the minigun also doesn't stop firing until the user lets go of the mouse
		{
			if(miniFire)
			{
				dungeon.playShot();
				if((player.whichImage.equals(player.right) || player.whichImage.equals(player.rightWalking)))
					mini.add(new Bullet(player.xcoor+player.WIDTH+7, player.ycoor+45, bulletRight, 15,bulletDirection));
				else if((player.whichImage.equals(player.left) || player.whichImage.equals(player.leftWalking)))
					mini.add(new Bullet(player.xcoor-13, player.ycoor+45, bulletLeft, -15,bulletDirection));
			}
		}
		else //the runtimer just repaints every 0.02 seconds so everything moves smoothly
		{
			fractions++;
			if(fractions==50)
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
				
			if(weapon.equals("revolver")){ //firing weapons. fire() is a method in the class Gun
				revolver.takeInObjects(currentRoom, zombies);
				zombies = revolver.fire();
			}
			else if(weapon.equals("shotgun")){
				shotgun.takeInObjects(currentRoom, zombies);
				zombies = shotgun.fire();
			}
			else if(weapon.equals("ak47")){
				AK.takeInObjects(currentRoom, zombies);
				zombies = AK.fire();
			}
			else if(weapon.equals("minigun")){
				mini.takeInObjects(currentRoom, zombies);
				zombies = mini.fire();
			}
			repaint();
			grabFocus();
		}
	}
	public void keyPressed(KeyEvent e) //required method. left and right arrow keys means switch weapons. if a key is pressed then i call the keyPressed method on the player class so it knows to move
	{
		if(e.getKeyCode()==KeyEvent.VK_RIGHT) //switching weapons
		{
			if(player.weaponIndex==weapons.length-1) player.weaponIndex = 0;
			else player.weaponIndex++;
			weapon = weapons[player.weaponIndex];
			if(weapon.equals("revolver"))
				rof_Revolver.start();
			else if(weapon.equals("shotgun"))
				rof_Shotgun.start();
			else if(weapon.equals("ak47"))
				rof_AK.start();
			else if(weapon.equals("minigun"))
				rof_Mini.start();
		}
		else if(e.getKeyCode()==KeyEvent.VK_LEFT) //switching weapons, but moving backwards in the weapons array
		{
			if(player.weaponIndex==0) player.weaponIndex = weapons.length-1;
			else player.weaponIndex--;
			weapon = weapons[player.weaponIndex];
			if(weapon.equals("revolver")){
				rof_Revolver.start();
			}
			else if(weapon.equals("shotgun")){
				rof_Shotgun.start();
			}
			else if(weapon.equals("ak47")){
				rof_AK.start();
			}
			else if(weapon.equals("minigun")){
				rof_Mini.start();
			}
		}
		player.keyPressed(e, player.left);
	}
	public void keyReleased(KeyEvent e) //required method. if a key is released then i call the keyReleased method on the player class so it knows to stop moving
	{
		player.keyReleased(e);
	}
	public void keyTyped(KeyEvent e){} //required method but does nothing
	public void createZombies() //creates an array of zombies for each room based on how many zombies are supposed to be in the room. also spawns the zombies at coordinates
	{
		zombies = new Zombie[currentRoom.zombieAmt];
		int otherAMT = currentRoom.zombies2;
		int x = 200;
		int y = 100;
		for(int i = 0;i<zombies.length;i++)
		{
			if(i<otherAMT) //not yet spawned all special zombies
				zombies[i] = new Zombie("../images/zombie-fast-left.png","../images/zombie-fast-right.png", 50, 80, 2, 80, 3);
			else
				zombies[i] = new Zombie("../images/zombie-generic-left.png","../images/zombie-generic-right.png", 50, 80, 2, 70, 2);
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
	
	public boolean checkZombieCollisions() //checks if zombie has touched player. if so, player's health needs to be lowered.
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
	public boolean checkZombieOverlapping(int n, int length) //checking if zombie is overlapping another zombie. returns true if it is, returns false if nothing is overlapping
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
	public void mousePressed(MouseEvent e){//if the user clicks while holding a weapon, fires that weapon and determines whether the weapon can harm a zombie based on where the bullet is
		if(e.getX()<player.xcoor+player.WIDTH/2) //if the user clicks to the left of the avatar, the player turns left. vice versa
			player.whichImage = player.left;
		else if(e.getX()>player.xcoor+player.WIDTH/2)
			player.whichImage = player.right;
		
		if(e.getY()-(player.ycoor+player.HEIGHT/2)>80) bulletDirection=9; //determining whether the bullet goes in straight line horizontally or slightly up based on where the user clicks
		else if(e.getY()-(player.ycoor+player.HEIGHT/2)<-80) bulletDirection=-9;
		else if(e.getY()-(player.ycoor+player.HEIGHT/2)>50) bulletDirection=6;
		else if(e.getY()-(player.ycoor+player.HEIGHT/2)<-50) bulletDirection=-6;
		else if(e.getY()-(player.ycoor+player.HEIGHT/2)>30) bulletDirection=3;
		else if(e.getY()-(player.ycoor+player.HEIGHT/2)<-30) bulletDirection=-3;
		else bulletDirection=0;
		if(weapon.equals("sword")){ //what to do if the player clicks while holding a sword.
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
		else if(weapon.equals("revolver")){ //fires the revolver by adding a bullet where the revolver is and restarts the fire rate timer for the revolver which is 0.7 seconds (meaning the user can only fire once every 0.7 seconds)
			if(revolverFire && (player.whichImage.equals(player.right) || player.whichImage.equals(player.rightWalking))){
				dungeon.playShot();
				revolver.add(new Bullet(player.xcoor+player.WIDTH-3, player.ycoor+41, bulletRight, 13,bulletDirection));
				revolverFire = false;
				rof_Revolver.restart();
			}
			else if(revolverFire && (player.whichImage.equals(player.left) || player.whichImage.equals(player.leftWalking))){
				dungeon.playShot();
				revolver.add(new Bullet(player.xcoor-player.WIDTH+39, player.ycoor+41, bulletLeft, -13,bulletDirection));
				revolverFire = false;
				rof_Revolver.restart();
			}
		}
		else if(weapon.equals("shotgun")){ //adding 5 bullets to the shotgun with different velocities so bullets spray out
			if(shotgunFire && (player.whichImage.equals(player.right) || player.whichImage.equals(player.rightWalking))){
				dungeon.playShot();
				shotgun.add(new Bullet(player.xcoor+player.WIDTH-2, player.ycoor+44, bulletRight, 17,bulletDirection-2));
				shotgun.add(new Bullet(player.xcoor+player.WIDTH-2, player.ycoor+44, bulletRight, 17,bulletDirection-1));
				shotgun.add(new Bullet(player.xcoor+player.WIDTH-2, player.ycoor+44, bulletRight, 17,bulletDirection));
				shotgun.add(new Bullet(player.xcoor+player.WIDTH-2, player.ycoor+44, bulletRight, 17,bulletDirection+1));
				shotgun.add(new Bullet(player.xcoor+player.WIDTH-2, player.ycoor+44, bulletRight, 17,bulletDirection+2));
				rof_Shotgun.restart();
				shotgunFire = false;
			}
			else if(shotgunFire && (player.whichImage.equals(player.left) || player.whichImage.equals(player.leftWalking))){
				dungeon.playShot();
				shotgun.add(new Bullet(player.xcoor-10, player.ycoor+44, bulletRight, -17,bulletDirection-2));
				shotgun.add(new Bullet(player.xcoor-10, player.ycoor+44, bulletRight, -17,bulletDirection-1));
				shotgun.add(new Bullet(player.xcoor-10, player.ycoor+44, bulletRight, -17,bulletDirection));
				shotgun.add(new Bullet(player.xcoor-10, player.ycoor+44, bulletRight, -17,bulletDirection+1));
				shotgun.add(new Bullet(player.xcoor-10, player.ycoor+44, bulletRight, -17,bulletDirection+2));
				rof_Shotgun.restart();
				shotgunFire = false;
			}
		}
		else if(weapon.equals("ak47")){ //set fire mode to true. unlike the shotgun and the revolver, the AK47 will keep on firing until the user LETS GO of the mouse. the revolver only fires once per click
			
			if((player.whichImage.equals(player.right) || player.whichImage.equals(player.rightWalking)))
				akFire = true;
			else if((player.whichImage.equals(player.left) || player.whichImage.equals(player.leftWalking)))
				akFire = true;
		}
		else if(weapon.equals("minigun")){ //set fire mode to true just like the AK47 but this gun's fire rate is quicker
			if((player.whichImage.equals(player.right) || player.whichImage.equals(player.rightWalking)))
				miniFire = true;
			else if((player.whichImage.equals(player.left) || player.whichImage.equals(player.leftWalking)))
				miniFire = true;
		}
	}
	public void mouseClicked(MouseEvent e){} //required method but does nothing
	public void mouseEntered(MouseEvent e){} //required method but does nothing
	public void mouseExited(MouseEvent e){} //required method but does nothing
	public void mouseReleased(MouseEvent e){//stops the continuous fire of the ak-47 or minigun, two guns that fire very fast and continue firing when user holds down the mouse
		akFire = false;
		miniFire = false;
	} 
	public void fail() //player is dead and so wait 2 seconds before showing game over panel
	{
		runtimer.stop(); //stops everything from moving
		deadDelay.start(); //freezes the screen for 2 seconds before showing the you are dead
	}
	public void checkPlayerCollisions() //checks if player bumped into wall, object, or if they have entered a door. 
	{
		if(currentRoom.checkCollision(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)) //if player has bumped into an object in a room
			crashed=true;
		else
			crashed=false;
		
		if(currentFile.equals("lvl2-room1.png"))//room 1
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){ //if the player is in room 1, what doors have they entered through
				currentFile = "lvl2-room2.png";
				currentRoom = room2;
				player.spawn(40,110);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room2.png"))//room2
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room1.png";
				currentRoom = room1;
				player.spawn(610,110);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl2-room3.png";
				currentRoom = room3;
				player.spawn(40,310);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room3.png"))//room 3
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room3-dead1.png";
				currentRoom = room3_dead1;
				player.spawn(40,260);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl2-room3-dead2.png";
				currentRoom = room3_dead2;
				player.spawn(310,30);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==3){
				currentFile = "lvl2-room2.png";
				currentRoom = room2;
				player.spawn(615,310);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==4){
				currentFile = "lvl2-room4.png";
				currentRoom = room4;
				player.spawn(220,390);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room3-dead1.png"))//room that is a dead end, connecting to room 3
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room3.png";
				currentRoom = room3;
				player.spawn(620,260);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room3-dead2.png"))//room that is a dead end, connecting to room 3
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room3.png";
				currentRoom = room3;
				player.spawn(310,390);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room4.png"))//room 4
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room3.png";
				currentRoom = room3;
				player.spawn(210,40);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==3){
				currentFile = "lvl2-room4-dead.png";
				currentRoom = room4_dead;
				player.spawn(610,110);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl2-room5.png";
				currentRoom = room5;
				player.spawn(370,390);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room4-dead.png"))//room 4 dead end
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room4.png";
				currentRoom = room4;
				player.spawn(40,110);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room5.png"))//room 5
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				currentFile = "lvl2-room4.png";
				currentRoom = room4;
				player.spawn(370,30);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room6.png";
				currentRoom = room6;
				player.spawn(40,160);
				createZombies();
			}
		}
		else if(currentFile.equals("lvl2-room6.png"))//room 6
		{
			if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==1){
				currentFile = "lvl2-room5.png";
				currentRoom = room5;
				player.spawn(590,160);
				createZombies();
			}
			else if(currentRoom.checkDoors(player.xcoor, player.ycoor, player.WIDTH, player.HEIGHT)==2){
				runtimer.stop();
				rof_AK.stop();
				rof_Mini.stop();
				rof_Revolver.stop();
				rof_Shotgun.stop();
				dungeon.endLevel(2, seconds, coinsEarned, zombiesKilled, weapons);
			}

		}
	}
}
