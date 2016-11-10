/*
 AISlot.java

 フレームにおけるスロットを実現するクラス．
 */

import java.util.ArrayList;
import java.util.Iterator;

public class AISlot {

	public final static int WHEN_REQUESTED = 0;
	public final static int WHEN_READ = WHEN_REQUESTED + 1;
	public final static int WHEN_WRITTEN = WHEN_READ + 1;
	public final static int WITH_RESPECT_TO = WHEN_WRITTEN + 1;
	public final static int DEMON_PROCS = WITH_RESPECT_TO + 1;

	private ArrayList mVals = new ArrayList();
	private AIDemonProc mDemons[];

	AISlot() {
		mDemons = new AIDemonProc[DEMON_PROCS];
	}

	ArrayList getValues(){
		return mVals;
	}

	//
	Iterator getSlotValues() {
		return mVals.iterator();
	}

	Object getSlotValue() {
		Iterator iter = getSlotValues();
		if (iter != null && iter.hasNext() == true)
			return iter.next();
		return null;
	}

	void setSlotValue(Object inSlotValue) {
		mVals = new ArrayList();
		addSlotValue(inSlotValue);
	}

	void addSlotValue(Object inSlotValue) {
		mVals.add(inSlotValue);
	}

	void removeSlotValue(Object inSlotValue) {
		mVals.remove(inSlotValue);
	}

	//
	AIDemonProc getWhenRequestedProc() {
		return mDemons[WHEN_REQUESTED];
	}

	AIDemonProc getWhenReadProc() {
		return mDemons[WHEN_READ];
	}

	AIDemonProc getWhenWrittenProc() {
		return mDemons[WHEN_WRITTEN];
	}

	AIDemonProc getDemonProc(int inType) {
		return mDemons[inType];
	}

	void setDemonProc(int inType, AIDemonProc inDemonProc) {
		mDemons[inType] = inDemonProc;
	}

}