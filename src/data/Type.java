package data;

public enum Type {
	Head(Bit.ZERO,Bit.ONE),Foot(Bit.ZERO,Bit.ONE),Error(Bit.ONE,Bit.ZERO),Data(Bit.ONE,Bit.ONE),Null(Bit.ZERO,Bit.ZERO);
	Bit data,pos;
	private Type(Bit isData, Bit isPositive) {
		data=isData;
		pos=isPositive;
	}
	public Bit[] get() {
		Bit[] toR = new Bit[2];
		toR[0]=data;
		toR[1]=pos;
		return toR;
	}
}
