package base;

import fileIO.FileArray;
import data.Bit;

/**
 * This class contains the basic algorithm and is responsible for
 * both compression and decompression
 * @author Joshua Kauffman
 */
public class Base {
	double targetCompressionRatio;	//TODO Assign privacy modifiers to fields
	/**
	 * This is the size of the chunks to be used - optimum performance occurs when chunk size approaches infinity
	 * The algorithm breaks even (at best) when chunkSize = 2 and (at worst) when chunkSize = 3. Therefore,
	 * to be useful, set chunkSize at at least 3 and, if possible, much, much higher than 3
	 */
	int chunkSize;
	/**
	 * This is the size of the units, not including incremental metadata. Default is 8 bits, or 1 byte
	 */
	int unitSize;
	FileArray filespace;
	
	private int currentSlot=0;
	private int implications=0;
	private int total=0;
	public Base(FileArray filespace) {
		targetCompressionRatio=.5;
		chunkSize=4;
		unitSize=8;
		this.filespace=filespace;
	}
	public Base(FileArray filespace, double targetCompressionRatio, int chunkSize) {
		this.targetCompressionRatio=targetCompressionRatio;
		this.chunkSize=chunkSize;
		this.filespace=filespace;
	}
	public void compress(FileArray file) {	//TODO Add necessary exceptions to this declaration
		//placeFileHead(file);			//Determined unnecessary - filename should just prefix the file
		//TODO file.prefix(filename);
		int chunkCount = 0;
		for(int i1=0,i2=unitSize;i2<=file.end();i1=i2,i2+=unitSize) {
			Bit[] unit = file.get(i1,i2);
			if(chunkCount >= chunkSize && shouldImplicate(unitSize)) {
				implicate(unit,file);
				chunkCount = 0;
			} else {
				placeData(unit);
				chunkCount++;
			}
		}
	}
	private boolean shouldImplicate(int unitlength) {
		if(currentCompressionRatio()<targetCompressionRatio) {
			return true;
		}
		if(collision(2*unitlength)) {		//length of foot included TODO necessary?
			return true;
		}
		return false;
	}
	private boolean collision(int spacesNeeded) {
		return false;
	}
	private double currentCompressionRatio() {
		return 1-implications/total;
	}
	private void implicate(Bit[] unit, FileArray file) {
		implications++;
		placeFoot(file);
		currentSlot += value(unit);
		int error = 0;
		try {
			error = probe((chunkSize+2)*unitSize);	//head + data + foot
			if(error!=0)error = probe((chunkSize+3)*unitSize);	//head + error + data + foot
		}catch(NoAvailableSlotException e) {
			e.printStackTrace(); 			//TODO Make this trigger an expansion of the filespace and/or a prompt
		}
		placeHead(file);
		if(error!=0) {
			placeError(error);
		}
	}
	private boolean place(Bit... unit) {
		return filespace.set(currentSlot, unit);
	}
	private void placeData(Bit... unit) {
		total++;
		place(Bit.ONE,Bit.ONE);
		place(unit);
	}
	private void placeFoot(FileArray file) {
		total++;
		place(Bit.ZERO,Bit.ONE);
		place(tag(file));
	}
	private void placeHead(FileArray file) {
		total++;
		place(Bit.ZERO,Bit.ONE);
		place(tag(file));
	}
	private void placeError(int error) {
		total++;
		place(Bit.ONE,Bit.ZERO);
		place(Bit.toBitArray(error));
	}
	/**
	 * This will change the current slot to the first available spot - currently set to bidirectional linear probe biased negative
	 * @return the distance traveled
	 * @throws Exception - no available space found in range (this will apply to most, if not all, probing algorithms)
	 */
	private int probe(int spacesNeeded) throws NoAvailableSlotException{
		int distance = 0;		//the current amount by which the current slot must be adjusted
		for(boolean back=true;collision(spacesNeeded);back=!back) {	//continues until a solution is found and keeps track of direction
			currentSlot -= distance;		//fixes last adjustment and returns to original slot position
			distance *= -1;	//reverses direction of probe
			if(back){
				distance--;		//increases distance of probe
			}
			if(distance<-chunkSize)throw new NoAvailableSlotException();
			currentSlot += distance;		//adjusts slot position according to probe
		}
		return distance;		//the amount by which the probe changed the slot to find a solution
	}
	/**
	 * Returns the tag used to identify this file in this filespace
	 */
	private Bit[] tag(FileArray file) {
		return null;
	}
	private int value(Bit... unit) {
		return 0;		//TODO
	}
}
