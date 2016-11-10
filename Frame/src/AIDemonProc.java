/*
 AIDemonProc.java
 すべての種類のデモン手続きのスーパークラス
 */

import java.util.Iterator;

abstract class AIDemonProc {

	abstract public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts);

	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues) {
		return eval(inFrameSystem, inFrame, inSlotName, inSlotValues, null);
	}

}