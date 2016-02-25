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
		if(num<=0)return new Bit[0];
		Bit[] toR = new Bit[1+(int)(Math.log(num)/Math.log(2))];
		for(int i=0;i<toR.length;i++) {
			int pow = (int)Math.pow(2,i);
			toR[toR.length-i-1]=num%(2*pow)>=pow?Bit.ONE:Bit.ZERO;
		}
		return toR;
	}
	public static int value(Bit... unit) {
		int toR=0;
		for(int i=0;i<unit.length;i++) {
			toR += unit[unit.length-i-1].toInt()*Math.pow(2, i);
		}
		return toR;
	}
	public static Bit[] toBitArray(String str) {
		Bit[] toR = new Bit[8*str.length()];
		int i=0;
		for(char c : str.toCharArray()) {
			Bit[] charbits = Bit.toBitArray((int)c);
			for(int j=0;j<8-charbits.length;j++,i++) {
				toR[i] = Bit.ZERO;
			}
			for(int j=0;j<charbits.length;j++,i++) {
				toR[i] = charbits[j];
			}
		}
		return toR;
	}
	public static Bit[] toBitArrayNums(String nums) throws ClassCastException{
		String[] numses = nums.split("[\\s*]+");
		nums = "";
		for(String s : numses)nums+=s;
		
		Bit[] toR = new Bit[nums.length()];
		int current=0;
		for(char c : nums.toCharArray()) {
			if(c=='1')toR[current] = Bit.ONE;
			else if(c=='0')toR[current] = Bit.ZERO;
			else throw new ClassCastException();
			current++;
		}
		return toR;
	}
	public static String toString(Bit[] bits) {
		String toR = "";
		for(int i=0,j=8;j<=bits.length;i=j,j+=8) {
			Bit[] current = java.util.Arrays.copyOfRange(bits, i, j);
			toR += (char)value(current);
		}
		return toR;
	}
}