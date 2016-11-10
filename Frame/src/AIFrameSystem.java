
/*
 AIFrameSystem.java
 フレームシステム
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AIFrameSystem {

	final static String sTopFrameName = "top_level_frame";

	// すべてのフレームを格納するための辞書．
	// フレーム名をインデックスとして利用．
	private HashMap<String, AIFrame> mFrames = new HashMap<String, AIFrame>();

	/**
	 * AIFrameSystem コンストラクタ
	 */
	public AIFrameSystem() {
		mFrames.put(sTopFrameName, new AIClassFrame(this, null, sTopFrameName));
	}

	/**
	 * createClassFrame クラスフレーム inName を作成する．
	 */
	public void createClassFrame(String inName) {
		createFrame(sTopFrameName, inName, false);
	}

	/**
	 * createClassFrame スーパーフレームとして inSuperName を持つクラスフレーム inName を作成する．
	 *
	 * @param inSuperName
	 *            スーパーフレームのフレーム名
	 * @param inName
	 *            フレーム名
	 */
	public void createClassFrame(String inSuperName, String inName) {
		createFrame(inSuperName, inName, false);
	}

	/**
	 * createInstanceFrame スーパーフレームとして inSuperName を持つインスタンスフレーム inName を作成する．
	 *
	 * @param inSuperName
	 *            スーパーフレームのフレーム名
	 * @param inName
	 *            フレーム名
	 */
	public void createInstanceFrame(String inSuperName, String inName) {
		createFrame(inSuperName, inName, true);
	}

	/*
	 * createFrame フレームを作成する
	 *
	 * @param inSuperName スーパーフレームのフレーム名
	 *
	 * @param inName フレーム名
	 *
	 * @param inIsInstance インスタンスフレームなら true
	 */
	void createFrame(String inSuperName, String inName, boolean inIsInstance) {
		AIClassFrame frame;
		try {
			frame = (AIClassFrame) mFrames.get(inSuperName);
			createFrame(frame, inName, inIsInstance);
		} catch (Throwable err) {
		}
	}

	/*
	 * createFrame フレームを作成する
	 *
	 * @param inSuperName スーパーフレーム
	 *
	 * @param inName フレーム名
	 *
	 * @param inIsInstance インスタンスフレームなら true
	 */

	void createFrame(AIClassFrame inSuperFrame, String inName, boolean inIsInstance) {
		AIFrame frame;
		if (inIsInstance == true) {
			frame = new AIInstanceFrame(this, inSuperFrame, inName);
		} else {
			frame = new AIClassFrame(this, inSuperFrame, inName);
		}
		mFrames.put(inName, frame);
	}

	/**
	 * readSlotValue スロット値を返す
	 *
	 * @param inFrameName
	 *            フレーム名
	 * @param inSlotName
	 *            スロット名
	 * @param inDefault
	 *            デフォルト値を優先したいなら true
	 */
	public Object readSlotValue(String inFrameName, String inSlotName, boolean inDefault) {
		AIFrame frame = (AIFrame) mFrames.get(inFrameName);
		return frame.readSlotValue(this, inSlotName, inDefault);
	}

	/**
	 * readSlotValue スロット値を返す
	 *
	 * @param inFrameName
	 *            フレーム名
	 * @param inSlotName
	 *            スロット名
	 */
	public Object readSlotValue(String inFrameName, String inSlotName) {
		AIFrame frame = (AIFrame) mFrames.get(inFrameName);
		return frame.readSlotValue(this, inSlotName, false);
	}

	/**
	 * readSlotValue スロット値を返す
	 *
	 * @param inFrameName
	 *            フレーム名
	 * @param inSlotName
	 *            スロット名
	 * @param inFacetName
	 *            ファセット名
	 */
	public Object readSlotValue(String inFrameName, String inSlotName, String inFacetName) {
		AIFrame frame = (AIFrame) mFrames.get(inFrameName);
		return frame.readSlotValue(this, inSlotName, false);
	}

	/**
	 * writeSlotValue スロット値を設定する．
	 *
	 * @param inFrameName
	 *            フレーム名
	 * @param inSlotName
	 *            スロット名
	 * @param inSlotValue
	 *            スロット値
	 */
	public void writeSlotValue(String inFrameName, String inSlotName, Object inSlotValue) {
		AIFrame frame = (AIFrame) mFrames.get(inFrameName);
		frame.writeSlotValue(this, inSlotName, inSlotValue);
	}

	// demon procedure の設定

	/**
	 * setWhenConstructedProc when-constructed procedure を設定する．
	 */
	public void setWhenConstructedProc(String inFrameName, String inSlotName, AIWhenConstructedProc inDemonProc) {
		AIFrame frame = (AIFrame) mFrames.get(inFrameName);
		if (frame != null)
			frame.setWhenConstructedProc(inDemonProc);
	}

	public void setWhenConstructedProc(String inFrameName, String inSlotName, String inClassName) {
		try {
			AIWhenConstructedProc demonProc = (AIWhenConstructedProc) Class.forName(inClassName).newInstance();
			AIFrame frame = (AIFrame) mFrames.get(inFrameName);
			if (frame != null)
				frame.setWhenConstructedProc(demonProc);
		} catch (Exception err) {
			System.out.println(err);
		}
	}

	/**
	 * setWhenRequestedProc when-requested procedure を設定する．
	 */
	public void setWhenRequestedProc(String inFrameName, String inSlotName, AIDemonProc inDemonProc) {
		setDemonProc(AISlot.WHEN_REQUESTED, inFrameName, inSlotName, inDemonProc);
	}

	public void setWhenRequestedProcClass(String inFrameName, String inSlotName, String inClassName) {
		setDemonProcClass(AISlot.WHEN_REQUESTED, inFrameName, inSlotName, inClassName);
	}

	/**
	 * setWhenReadProc when-read procedure を設定する．
	 */
	public void setWhenReadProc(String inFrameName, String inSlotName, AIDemonProc inDemonProc) {
		setDemonProc(AISlot.WHEN_READ, inFrameName, inSlotName, inDemonProc);
	}

	public void setWhenReadProcClass(String inFrameName, String inSlotName, String inClassName) {
		setDemonProcClass(AISlot.WHEN_READ, inFrameName, inSlotName, inClassName);
	}

	/**
	 * setWhenWrittenProc when-written procedure を設定する．
	 */
	public void setWhenWrittenProc(String inFrameName, String inSlotName, AIDemonProc inDemonProc) {
		setDemonProc(AISlot.WHEN_WRITTEN, inFrameName, inSlotName, inDemonProc);
	}

	public void setWhenWrittenProcClass(String inFrameName, String inSlotName, String inClassName) {
		setDemonProcClass(AISlot.WHEN_WRITTEN, inFrameName, inSlotName, inClassName);
	}

	/*
	 * setDemonProc demon procedure を設定する．
	 */
	void setDemonProc(int inType, String inFrameName, String inSlotName, AIDemonProc inDemonProc) {
		AIFrame frame = (AIFrame) mFrames.get(inFrameName);
		if (frame != null)
			frame.setDemonProc(inType, inSlotName, inDemonProc);
	}

	/*
	 * setDemonClass demon procedure を設定する．
	 */
	void setDemonProcClass(int inType, String inFrameName, String inSlotName, String inClassName) {
		try {
			AIDemonProc demon = (AIDemonProc) Class.forName(inClassName).newInstance();
			setDemonProc(inType, inFrameName, inSlotName, demon);
		} catch (Exception err) {
			System.out.println(err);
		}
	}

	/**
	 * 指定されたフレーム名のAIフレームを返す
	 */
	public AIFrame getFrame(String frameName) {
		if (mFrames.containsKey(frameName)) {
			return mFrames.get(frameName);
		} else {
			return null;
		}
	}

	/**
	 * frameName1のleankersに新しくframeName2がSlotNameで接続していることを登録する
	 */
	void writeLeankers(String frameName1, String frameName2, String slotName) {
		if (mFrames.containsKey(frameName1)) {
			getFrame(frameName1).putLeanker(slotName, frameName2);
		}
	}

	/**
	 * guiデ使うためにAIFrameを返す
	 *
	 * @return
	 */
	public ArrayList<AIFrame> getFrames() {
		ArrayList<AIFrame> list = new ArrayList<AIFrame>();
		for (Iterator it = mFrames.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			AIFrame value = (AIFrame) entry.getValue();
			list.add(value);

		}
		return list;
	}

	public HashMap<String, AIFrame> getFrameMap() {
		return mFrames;
	}

}
