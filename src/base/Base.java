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
	int chunkSize;
	FileArray filespace;
	
	private int implications=0;
	private int total=0;
	public Base(FileArray filespace) {
		targetCompressionRatio=.5;
		chunkSize=10;
		this.filespace=filespace;
	}
	public Base(FileArray filespace, double targetCompressionRatio, int chunkSize) {
		this.targetCompressionRatio=targetCompressionRatio;
		this.chunkSize=chunkSize;
		this.filespace=filespace;
	}
	public void compress(FileArray file) {	//TODO Add necessary exceptions to this declaration
		//placeFileHead(file);			//Determined unnecessary - filename should just prefix the file
		for(Bit[] unit : file.toChunkArray(chunkSize)) {	//TODO make this use filearray get() and set() instead
			if(shouldImplicate()) {
				implicate(unit,file);
			} else {
				placeData(unit);
			}
		}
	}
	private boolean shouldImplicate() {
		if(currentCompressionRatio()<targetCompressionRatio) {
			return true;
		}
		if(collision()) {
			return true;
		}
		return false;
	}
	private boolean collision() {
		return false;
	}
	private double currentCompressionRatio() {
		return 1-implications/total;
	}
	private void implicate(Bit[] unit, FileArray file) {
		implications++;
		placeFoot(file);
		Integer error = null;
		if(collision()) {
			try {
				error = probe();
			}catch(Exception e) {
				
			}
		}
		placeHead(file);
		if(error!=null) {
			placeError(error);
		}
	}
	private void place(Bit... unit) {
		
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
	private int probe() throws Exception{
		return 0;
	}
	/**
	 * Returns the tag used to identify this file in this filespace
	 */
	private Bit[] tag(FileArray file) {
		return null;
	}
}
