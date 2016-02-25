package fileIO;

import data.Bit;

public class FileArray {
	protected String filename;
	protected int endAddress;
	public FileArray(String filename) {
		this.filename=filename;
		//TODO
	}
	public Bit[] toBitArray() {
		return null;	//TODO
	}
	public Bit get(int address) {
		return null;	//TODO
	}
	public Bit[] get(int i1, int i2) {
		return null;	//TODO
	}
	/**
	 * Sets the bits beginning at address and ending at address+bits.length
	 * to the specified bits. If address+bits.length > endAddress, the file scales
	 * and endAddress is updated
	 * @param address
	 * @param bits
	 * @return whether the operation was successful
	 */
	public boolean set(int address, Bit... bits) {
		return false;	//TODO
	}
	public int end() {
		return endAddress;
	}
	public String filename() {
		return filename;
	}
	public void prefix(Bit... bits) {
		//TODO
	}
	/**
	 * Clears this FileArray so that it can be properly used as a filespace for this compressor
	 */
	public void wipe() {
		//TODO
	}
}
