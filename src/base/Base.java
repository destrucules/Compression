package base;

import fileIO.FileArray;
import data.Bit;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * This class contains the basic algorithm and is responsible for
 * both compression and decompression
 * @author Joshua Kauffman
 */
public class Base {
	private double targetCompressionRatio;
	/**
	 * This is the size of the chunks to be used - optimum performance occurs when chunk size approaches infinity
	 * The algorithm breaks even (at best) when chunkSize = 2 and (at worst) when chunkSize = 3. Therefore,
	 * to be useful, set chunkSize at at least 3 and, if possible, much, much higher than 3
	 */
	private int chunkSize;
	/**
	 * This is the size of the units, not including incremental metadata. Default is 8 bits, or 1 byte
	 */
	private int unitSize;
	private FileArray filespace;
	private DualHashBidiMap<Integer,Bit[]> filebase;
	
	private int currentSlot=0;
	private int implications=0;
	private int total=0;
	public Base(FileArray filespace) {
		targetCompressionRatio=.5;
		chunkSize=4;
		unitSize=8;
		this.filespace=filespace;
		initFilebase();
	}
	public Base(FileArray filespace, double targetCompressionRatio, int chunkSize) {
		this.targetCompressionRatio=targetCompressionRatio;
		this.chunkSize=chunkSize;
		this.filespace=filespace;
		initFilebase();
	}
	private void initFilebase() {
		filebase = new DualHashBidiMap<>();
		boolean inFile=false;	//tells if it is reading a new filename
		java.util.LinkedList<Bit> filename = new java.util.LinkedList<>();
		for(int i1=0,i2=unitSize+2;i2<=filespace.end();i1=i2,i2+=unitSize+2) {
			Bit[] meta = filespace.get(i1, i1+2);
			Bit[] data = filespace.get(i1+2,i2);
			if(meta[0]==Bit.ZERO && meta[1]==Bit.ONE) {	//if a head/foot is found
				int tag = value(data);
				if(inFile) {
					inFile=false;
					//TODO calculate how many bits are extraneous (assuming bytes were used in the filename) and truncate
					filebase.put(tag, filename.toArray(new Bit[0]));
					filename.clear();
				}else if(!filebase.containsKey(tag)) {
					inFile=true;
					filename.clear();
				}
			}else if(inFile) {
				for(Bit b : data) {
					filename.add(b);
				}
			}
		}
	}
	public void compress(FileArray file) {
		placeFileHead(file);
		int chunkCount = 0;
		for(int i1=0,i2=unitSize;i2<=file.end();i1=i2,i2+=unitSize) {
			Bit[] unit = file.get(i1,i2);
			if(chunkCount >= chunkSize && shouldImplicate()) {
				implicate(unit,file);
				chunkCount = 0;
			} else {
				placeData(unit);
				chunkCount++;
			}
		}
	}
	private void placeFileHead(FileArray file) {
		placeHead(file);
		place(file.filename());
		placeFoot(file);
	}
	private boolean shouldImplicate() {
		if(currentCompressionRatio()<targetCompressionRatio) {
			return true;
		}
		if(collision(2*unitSize)) {
			return true;
		}
		return false;
	}
	private boolean collision(int spacesNeeded) {
		Bit[] next = filespace.get(currentSlot,currentSlot+spacesNeeded);
		for(int i=0;i<next.length-1;) {
			if(next[i].equals(Bit.ONE) || next[i+1].equals(Bit.ONE))return true;
			i+=unitSize;
		}
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
		boolean success=true;
		for(int i1=0,i2=unitSize;i1<unit.length;i1=i2,i2+=unitSize) {
			if(i2<unit.length) {
				for(int i=i1;i<i2;i++) {
					success=success&&filespace.set(currentSlot, unit[i]);
					currentSlot++;
				}
			} else {
				for(int i=i1;i<unit.length;i++) {
					success=success&&filespace.set(currentSlot, unit[i]);
					currentSlot++;
				}
				for(int i=unit.length;i<i2;i++) {
					success=success&&filespace.set(currentSlot, Bit.ZERO);
					currentSlot++;
				}
			}
		}
		return success;
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
		return Bit.toBitArray(filebase.getKey(file.filename()));
	}
	private static int value(Bit... unit) {
		int toR=0;
		for(int i=0;i<unit.length;i++) {
			toR += unit[unit.length-i-1].toInt()*Math.pow(2, i);
		}
		return toR;
	}
}
