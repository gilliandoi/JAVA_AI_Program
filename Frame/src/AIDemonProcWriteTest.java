/*
 AIDemonProcWriteTest.java
 ���ׂĂ̎�ނ̃f�����葱���̃X�[�p�[�N���X
 */

import java.util.Iterator;

class AIDemonProcWriteTest extends AIDemonProc {

	public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
			String inSlotName, Iterator inSlotValues, Object inOpts) {
		Object obj = AIFrame.getFirst(inSlotValues);
		inFrame.setSlotValue(inSlotName, "hello " + obj);
		return null;
	}

}