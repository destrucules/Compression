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
	public String toString() {
		return value?"1":"0";
	}
	public static Bit[] toBitArray(int num) {
		Bit[] toR = new Bit[1+(int)(Math.log(num)/Math.log(2))];
		for(int i=0;i<toR.length;i++) {
			int pow = (int)Math.pow(2,i);
			toR[toR.length-i-1]=num%(2*pow)>=pow?Bit.ONE:Bit.ZERO;
		}
		return toR;
	}
}