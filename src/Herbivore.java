import java.util.ArrayList;
import java.util.Random;

import javafx.scene.paint.Color;
/** 
 * A kind of Animal that eats plants to survive. 
 * @author sherryguo
 *
 */
public class Herbivore extends Animalia {
	
	/** Herbivore constructor. **/
	public Herbivore(Cell cell) {
		super(cell);
		m_cellColor = "Yellow";
		hungerLevel = 0;
	}
	
	/** What happens in this turn. Varies by lifeform.**/
	@Override 
	public void doturn() {
		
		hungerLevel++;
		
		if (hungerLevel == fatalLevel) {
			this.die();
		} else {
			this.move();
			this.giveBirth();
		}
		
	}
	
	/** Herbivores can only move to adjacent squares no occupied by another herbivore.
	 * 	If the square it moves to contains a plant, its hungerlevel is reset,
	 *  and distances itself from doom... **/
	public void move() { 
		
		int oldX;
		int oldY;
		int moveToIndex;
		
		ArrayList<Cell> myNeighbours = m_pos.getNeighbours();
		
		Random rand = new Random();
		moveToIndex = rand.nextInt(myNeighbours.size());
		
		m_newPos = myNeighbours.get(moveToIndex);
		
		// newPosition is initially the same as old position
		if (!(m_newPos.getLifeform() instanceof Animalia)) {
			
			oldX = m_pos.getPosX();
			oldY = m_pos.getPosY();
			
			m_pos.setPosX(m_newPos.getPosX());
			m_pos.setPosY(m_newPos.getPosY());
			
			m_pos.getWorld().setWorldCells(m_pos.getPosX(), m_pos.getPosY(), m_pos);
			m_pos.getWorld().setWorldCells(oldX, oldY, new Cell(oldX, oldY));
			
			if (m_newPos.getLifeform() instanceof Plantae) {
				this.eat();
			}
		}
	}
	
	/** Rules of Life:
	 * - If does not eat within 5 turns, dies. Eats only plants.
	 * - to giveBirth() must have at least 1 mate, 2 spots with food, and 2 free spaces. **/
	@Override
	public void giveBirth() {
		ArrayList<Cell> birthSpaces = new ArrayList<>();
		int matecount = 0;
		int foodcount = 0;
		
		ArrayList<Cell> myNeighbours = m_pos.getNeighbours();
		for (Cell item: myNeighbours) {
			
			if (item.getLifeform() instanceof Plantae) {
				foodcount++;
				continue;
			} else if (item.getLifeform() instanceof Herbivore) {
				matecount++;
				continue;
			} else if (item.getLifeform() instanceof Animalia) {
				continue;
			} 
				birthSpaces.add(item);
		}
		
		if (matecount >= 1 && foodcount >= 2 && birthSpaces.size() >= 2) {
			Random rand = new Random();
			int ln = rand.nextInt(birthSpaces.size());
			
				m_pos.getWorld().setWorldCells(birthSpaces.get(ln).getPosX(), birthSpaces.get(ln).getPosY(),
						new Cell(birthSpaces.get(ln).getPosX(), birthSpaces.get(ln).getPosY(), 
								"herbivore", m_pos.getWorld()));
		}
		
	}
	

}
