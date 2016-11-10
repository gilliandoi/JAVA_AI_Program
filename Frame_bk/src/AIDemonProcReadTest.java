/*
 AIDemonProcReadTest.java
 すべての種類のデモン手続きのスーパークラス

 when-read procedure は，スロット値を Iterator として
 返さなけらばならない
 */

import java.util.Iterator;

class AIDemonProcReadTest extends AIDemonProc {

	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {
		Object height = inFrame.readSlotValue(inFrameSystem, "height", false);
		if (height instanceof Integer) {
			int h = ((Integer) height).intValue();
			return AIFrame.makeEnum(new Integer((int) (0.9 * (h - 100))));
		}
		return null;
	}

}