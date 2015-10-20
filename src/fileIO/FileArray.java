package fileIO;

import data.Bit;

public class FileArray {
	public Bit[] filename;
	private int endAddress;
	public FileArray() {		//TODO make this do stuff
		
	}
	public Bit[] toBitArray() {
		return null;
	}
	public Bit[][] toChunkArray(int chunkSize){
		return null;
	}
	public Bit get(int address) {
		return null;
	}
	public Bit[] get(int i1, int i2) {
		return null;
	}
	public boolean set(int address, Bit... bits) {
		return false;
	}
	public int end() {
		return endAddress;
	}
}
