package data;

public enum Bit {
	ONE (true),ZERO (false);
	private Bit(boolean value) {
		this.value=value;
	}
	private boolean value;
	public boolean toBoolean() {
		return value;
	}
	public int toInt() {
		return value?1:0;
	}
	public static Bit[] toBitArray(int num) {
		return null;
	}
}
