package base;

import data.Bit;
import fileIO.FileArray;

public class Base {
	public final int unitSize;
	private final FileArray fSpace;
	private final FileArray cSpace;
	private final java.util.TreeMap<Integer,String> filebase;
	
	public Base(int unitSize) {
		this.unitSize=unitSize;
		fSpace = new FileArray("filename area");	//TODO make a better filename
		cSpace = new FileArray("compression area");
		filebase = new java.util.TreeMap<>();
	}
	
	public void compress(FileArray file) {
		int tag = tag(file.filename());
		/* WRITE NAME TO FSPACE
		 * WRITE '@' TO FSPACE
		 * WRITE TAG TO FSPACE */
		
		/* FIND FIRST FREE SLOT IN CSPACE
		 * WRITE TAG TO CSPACE
		 * FOR EACH UNIT
			* JUMP (UNIT VALUE + 1) * NEIGHBORHOOD SIZE
			* DEFINE D
			* ITERATE UNTIL FREE SPACE IS FOUND
			* STORE ITERATED DISTANCE IN D
			* PLACE TAG IN CURRENT LOCATION
			* DEFINE X = TAG AT CURRENT LOCATION
			* WHILE D > NEIGHBORHOOD SIZE
				* ITERATE FROM CURRENT LOCATION - NEIGHBORHOOD SIZE
				* FIND TAG Y WHERE Y CAN SWAP WITH X
				* IF NOT FOUND THEN RESIZE AND REHASH
				* SWAP X AND Y
				* UPDATE D = D - X + Y
		 */
	}
	
	public void decompress(String filename) {
		int tag = tag(filename);
		
		/* DEFINE TO RETURN
		 * DEFINE POS = 0
		 * FIND FIRST INSTANCE OF TAG IN CSPACE
		 	* INCREMENT POS
		 * WHILE TRUE
		 	* DEFINE DIST = 0
		 	* FIND NEXT INSTANCE OF TAG IN CPSACE
		 		* INCREMENT POS
		 		* INCREMENT DIST
		 		* IF NOT FOUND THEN BREAK
		 	* ADD DIST / NEIGHBORHOOD - 1 SIZE TO TO RETURN
		 	* SET POS = POS - (DIST % NEIGHBORHOOD SIZE)
		 * RETURN TO RETURN
		 */
	}
	
	private int tag(String filename) {
		if(!filebase.containsValue(filename)) {
			filebase.put(filebase.lastKey()+1,filename);
			return filebase.lastKey();
		}
		for(int tag : filebase.keySet()) {
			if(filebase.get(tag).equals(filename))return tag;
		}
		throw new NullPointerException("TreeMap scrwed up - not my fault");
	}
	private String tag(int tag) {
		if(!filebase.containsKey(tag))return null;
		return filebase.get(tag);
	}
}
