import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class Gun //any type of gun in the game. has methods such as fire and draw and controls the bullets and also determines whether one of its rounds hits the zombie
{
	LinkedList<Bullet> bullets = new LinkedList<Bullet>(); //something not taught in class but I'm using it because its very useful for stuff like firing bullets
	LevelTwo lvl2;
	Room room;
	Zombie[] zombies;
	int damage;
	public Gun(int damage) //constructor takes in an int called damage, which is how much damage a single bullet deals on a zombie
	{
		this.damage = damage;
	}
	public void draw(Graphics g) //draws all the bullets in the linked list. draw() is also a method in the bullet class 
	{
		Bullet bullet;
		for(int i = 0;i<bullets.size();i++)
		{
			bullet = bullets.get(i);
			bullet.draw(g);
		}
	}
	public Zombie[] fire() //for every bullet in the bullet link list, the bullet moves and is also checked for whether it hits the walls, or a zombie. since some bullets will hit zombies, the zombie array is modified so i return the new zombie array for LevelTwo to take
	{
		Bullet bullet;
		for(int i = 0;i<bullets.size();i++)
		{
			bullet = bullets.get(i);
			bullet.move();
			if(room.checkCollision(bullet.xcoor+2, bullet.ycoor+2, 3, 1) || (bullet.xcoor>700 || bullet.xcoor<0) || (bullet.ycoor>500 || bullet.ycoor<0)) //bullet hit a wall or object
				remove(bullet);
			for(int k = 0;k<zombies.length;k++)
			{
				Polygon body = new Polygon(new int[]{zombies[k].xcoor, zombies[k].xcoor+zombies[k].WIDTH, zombies[k].xcoor+zombies[k].WIDTH, zombies[k].xcoor}, new int[]{zombies[k].ycoor, zombies[k].ycoor, zombies[k].ycoor+zombies[k].HEIGHT, zombies[k].ycoor+zombies[k].HEIGHT}, 4);
				if(body.contains(bullet.xcoor+3, bullet.ycoor+2) && zombies[k].alive){ //bullet hit a zombie
					remove(bullet);
					zombies[k].health-=damage;
				}
			}
		}
		return zombies;
	}
	public void takeInObjects(Room room, Zombie[] zombies) //called by LevelTwo or LevelThree. it gives the information of the level's zombie array and room so that fire() can check if a bullet hit anything in that room
	{ 
		this.room = room;
		this.zombies = zombies;
	}
	public void add(Bullet b) //when the gun fires, it adds a bullet to the link list of bullets
	{
		bullets.add(b);
	}
	public void remove(Bullet b) //if the bullet hits a wall or a zombie, the bullet disappears and it is removed from the link list
	{
		bullets.remove(b);
	}
}
