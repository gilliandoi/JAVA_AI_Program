/*
 AIFrame.java

 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class AIFrame {

	private boolean mIsInstance;
	private String mName;
	private Map<String, AISlot> mSlots = new HashMap<String, AISlot>();
	private AIWhenConstructedProc mWhenConstructedProc = null;
	private ArrayList<String> supers = new ArrayList<String>();

	//改変部
	/*
	 * 自分に対してリンクを持っているフレームおよびそのスロット名を保持するハッシュマップ
	 * キーがリンクしているフレーム名
	 * バリュがスロット名
	 */
	private Map<String, String> leankers = new HashMap<String, String>();

	/*
	 * AIFrame コンストラクタ
	 */
	AIFrame(AIFrameSystem inFrameSystem, AIClassFrame inSuperFrame,
			String inName, boolean inIsInstance) {
		mName = inName;
		mIsInstance = inIsInstance;
		if (inSuperFrame != null)
			setSlotValue(getSuperSlotName(), inSuperFrame);
		evalWhenConstructedProc(inFrameSystem, this);
	}

	/*
	 * AIFrame コンストラクタ
	 */
	AIFrame(AIFrameSystem inFrameSystem, Iterator inSuperFrames, String inName,
			boolean inIsInstance) {
		mName = inName;
		mIsInstance = inIsInstance;
		while (inSuperFrames.hasNext() == true) {
			AIFrame frame = (AIFrame) inSuperFrames.next();
			addSlotValue(getSuperSlotName(), frame);
		}
		evalWhenConstructedProc(inFrameSystem, this);
	}


	/*
	 * setWhenConstructedProc when-constructed proc を登録
	 */
	public void setWhenConstructedProc(AIWhenConstructedProc inProc) {
		mWhenConstructedProc = inProc;
	}

	/*
	 * getWhenConstructedProc when-constructed proc を返す
	 */
	public AIWhenConstructedProc getWhenConstructedProc() {
		return mWhenConstructedProc;
	}

	/*
	 * evalWhenConstructedProc when-constructed proc を評価
	 */
	void evalWhenConstructedProc(AIFrameSystem inFrameSystem, AIFrame inFrame) {
		Iterator supers = getSupers();
		if (supers != null) {
			while (supers.hasNext() == true) {
				AIClassFrame frame = (AIClassFrame) supers.next();
				frame.evalWhenConstructedProc(inFrameSystem, inFrame);
			}
		}
		if (mWhenConstructedProc != null)
			mWhenConstructedProc.eval(inFrameSystem, inFrame);
	}

	/*
	 * isInstance このフレームがインスタンスフレームなら true を返す
	 */
	public boolean isInstance() {
		return mIsInstance;
	}

	/*
	 * getSupers このフレームのスーパーフレームを返す
	 */
	public Iterator getSupers() {
		return getSlotValues(getSuperSlotName());
	}

	/**
	 * readSlotValue スロット inSlotName に格納されているスロット値を返す．
	 * 複数のスロット値が格納されているときは，最初のオブジェクトを返す．
	 *
	 * スロット値の優先度 1. 自分の when-requested procedure 2. スーパークラスの when-requested
	 * procedure 3. 自分の when-read procedure 4. スーパークラスの when-read procedure 5.
	 * 自分のスロット値 6. スーパークラスのスロット値
	 */
	public Object readSlotValue(AIFrameSystem inFrameSystem, String inSlotName,
			boolean inDefault) {
		return getFirst(readSlotValues(inFrameSystem, inSlotName, inDefault));
	}

	/**
	 * readSlotValues スロット inSlotName に格納されているスロット値を返す．
	 */
	public Iterator readSlotValues(AIFrameSystem inFrameSystem,
			String inSlotName, boolean inDefault) {
		Iterator obj = null;

		if (inDefault == false) {
			AISlot slot = getSlot(inSlotName);
			if (slot != null)
				obj = slot.getSlotValues();
		}

		if (obj == null)
			obj = readSlotValuesWithWhenRequestedProc(inFrameSystem, inSlotName);

		if (obj == null) {
			Iterator supers = getSupers();
			while (supers.hasNext() == true) {
				AIClassFrame frame = (AIClassFrame) supers.next();
				obj = frame.getSlotValues(inSlotName);
				if (obj != null)
					break;
			}
		}

		return readSlotValuesWithWhenReadProc(inFrameSystem, inSlotName, obj);
	}

	/**
	 * readSlotValuesWithWhenRequestedProc スロット inSlotName に格納されているスロット値を返す．
	 */
	Iterator readSlotValuesWithWhenRequestedProc(AIFrameSystem inFrameSystem,
			String inSlotName) {
		return readSlotValuesWithWhenRequestedProc(inFrameSystem, this,
				inSlotName);
	}

	protected Iterator readSlotValuesWithWhenRequestedProc(
			AIFrameSystem inFrameSystem, AIFrame inFrame, String inSlotName) {
		Iterator obj = null;
		AISlot slot = getSlot(inSlotName);

		obj = evalWhenRequestedProc(inFrameSystem, inFrame, slot, inSlotName);
		if (obj != null)
			return obj;

		Iterator supers = getSupers();
		if (supers != null) {
			while (supers.hasNext() == true) {
				AIClassFrame frame = (AIClassFrame) supers.next();
				slot = frame.getSlot(inSlotName);
				obj = frame.evalWhenRequestedProc(inFrameSystem, inFrame, slot,
						inSlotName);
				if (obj != null)
					return obj;
			}
		}

		return null;
	}

	protected Iterator evalWhenRequestedProc(AIFrameSystem inFrameSystem,
			AIFrame inFrame, AISlot inSlot, String inSlotName) {
		if (inSlot != null && inSlot.getWhenRequestedProc() != null) {
			AIDemonProc demon = inSlot.getWhenRequestedProc();
			if (demon != null)
				return (Iterator) demon.eval(inFrameSystem, inFrame,
						inSlotName, null);
		}
		return null;
	}

	/**
	 * readSlotValuesWithWhenReadProc スロット inSlotName に格納されているスロット値を返す．
	 */
	Iterator readSlotValuesWithWhenReadProc(AIFrameSystem inFrameSystem,
			String inSlotName, Iterator inSlotValue) {
		return readSlotValuesWithWhenReadProc(inFrameSystem, this, inSlotName,
				inSlotValue);
	}

	protected Iterator readSlotValuesWithWhenReadProc(
			AIFrameSystem inFrameSystem, AIFrame inFrame, String inSlotName,
			Iterator inSlotValue) {
		AISlot slot;

		Iterator supers = getSupers();
		if (supers != null) {
			while (supers.hasNext() == true) {
				AIClassFrame frame = (AIClassFrame) supers.next();
				slot = frame.getSlot(inSlotName);
				inSlotValue = frame.evalWhenReadProc(inFrameSystem, inFrame,
						slot, inSlotName, inSlotValue);
			}
		}

		slot = getSlot(inSlotName);
		return evalWhenReadProc(inFrameSystem, inFrame, slot, inSlotName,
				inSlotValue);
	}

	protected Iterator evalWhenReadProc(AIFrameSystem inFrameSystem,
			AIFrame inFrame, AISlot inSlot, String inSlotName,
			Iterator inSlotValue) {
		if (inSlot != null && inSlot.getWhenReadProc() != null) {
			AIDemonProc demon = inSlot.getWhenReadProc();
			if (demon != null)
				inSlotValue = (Iterator) demon.eval(inFrameSystem, inFrame,
						inSlotName, inSlotValue);
		}

		return inSlotValue;
	}

	/**
	 * writeSlotValue スロット inSlotName にスロット値 inSlotValue を設定する．
	 */
	public void writeSlotValue(AIFrameSystem inFrameSystem, String inSlotName,
			Object inSlotValue) {
		AISlot slot = getSlot(inSlotName);
		if (slot == null) {
			slot = new AISlot();
			mSlots.put(inSlotName, slot);
		}

		slot.setSlotValue(inSlotValue);

		writeSlotValueWithWhenWrittenProc(inFrameSystem, inSlotName,
				inSlotValue);
	}

	void writeSlotValueWithWhenWrittenProc(AIFrameSystem inFrameSystem,
			String inSlotName, Object inSlotValue) {
		Iterator supers = getSupers();
		if (supers != null) {
			while (supers.hasNext() == true) {
				AIClassFrame frame = (AIClassFrame) supers.next();
				frame.writeSlotValueWithWhenWrittenProc(inFrameSystem,
						inSlotName, inSlotValue);
			}
		}

		AISlot slot = getSlot(inSlotName);
		if (slot != null) {
			AIDemonProc demon = slot.getWhenWrittenProc();
			if (demon != null)
				demon.eval(inFrameSystem, this, inSlotName,
						makeEnum(inSlotValue));
		}
	}

	// ----------------------------------------------------------------------
	public Object getSlotValue(String inSlotName) {
		Iterator iter = getSlotValues(inSlotName);
		if (iter != null && iter.hasNext() == true)
			return iter.next();
		return null;
	}

	public Iterator getSlotValues(String inSlotName) {
		AISlot slot = getSlot(inSlotName);
		if (slot == null)
			return null;
		return slot.getSlotValues();
	}

	public void setSlotValue(String inSlotName, Object inSlotValue) {
		AISlot slot = getSlot(inSlotName);
		if (slot == null) {
			slot = new AISlot();
			mSlots.put(inSlotName, slot);
		}
		slot.setSlotValue(inSlotValue);
	}

	public void addSlotValue(String inSlotName, Object inSlotValue) {
		AISlot slot = getSlot(inSlotName);
		if (slot == null) {
			slot = new AISlot();
			mSlots.put(inSlotName, slot);
		}
		slot.addSlotValue(inSlotValue);
	}

	public void removeSlotValue(String inSlotName, Object inSlotValue) {
		AISlot slot = getSlot(inSlotName);
		if (slot != null)
			slot.removeSlotValue(inSlotValue);
	}

	public void setDemonProc(int inType, String inSlotName,
			AIDemonProc inDemonProc) {
		AISlot slot = getSlot(inSlotName);
		if (slot == null) {
			slot = new AISlot();
			mSlots.put(inSlotName, slot);
		}
		slot.setDemonProc(inType, inDemonProc);
	}

	// ------------------------------------------------------------------
	// utils
	// ------------------------------------------------------------------

	/*
	 * getSuperSlotName スーパーフレームを格納しているスロットの名前を返す．
	 */
	String getSuperSlotName() {
		if (isInstance() == true)
			return "is-a";
		return "ako";
	}

	/*
	 * getSlot スロット名が inSlotName であるスロットを返す．
	 */
	AISlot getSlot(String inSlotName) {
		return (AISlot) mSlots.get(inSlotName);
	}



	/*
	 * getFirst inEnum 中の最初のオブジェクトを返す
	 */
	public static Object getFirst(Iterator inEnum) {
		if (inEnum != null && inEnum.hasNext() == true)
			return inEnum.next();
		return null;
	}

	/*
	 * makeEnum
	 */
	public static Iterator makeEnum(Object inObj) {
		ArrayList list = new ArrayList();
		list.add(inObj);
		return list.iterator();
	}

	//改変部
	/**
	 * is-a,akoのときスパークラスの名前を保存する
	 * @param Name
	 */
	public void addSuper(String Name){
		supers.add(Name);
	}

	//改変部
	/**
	 * 指定された名前のスロットがすでにあるか返す
	 */
	public boolean hasSlot(String slotName){
		return mSlots.containsKey(slotName);
	}

	//改変部
	/**
	 * フレームのスロット数を返す
	 */
	public int getSlotSize(){
		return mSlots.size();
	}

	//改変部
	/**
	 * フレームの指定された位置のスロット名を返す
	 */
	public String getSlotkey(int n){
		int count=0;
		for (Iterator it = mSlots.entrySet().iterator(); it.hasNext();) {
		    Map.Entry entry = (Map.Entry)it.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    if(count == n){
		    	return (String)key;
		    }
		    	count++;
		}
		return null;
	}

	/**
	 * leankersに新しいリンク情報を追加する
	 */
	public void putLeanker(String slotName,String frameName){
		//System.out.println(slotName+frameName);
		leankers.put(frameName,slotName);
	}

	/**
	 * leankersの数を返す
	 */
	public int getLeankersSize(){
		return leankers.size();
	}

	/**
	 * フレームの指定された位置のleankersのキーを返す
	 */
	public String getLeankersSlotKey(int n){
		int count=0;
		for (Iterator it = leankers.entrySet().iterator(); it.hasNext();) {
		    Map.Entry entry = (Map.Entry)it.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    if(count == n){
		    	return (String)key;
		    }
		    	count++;
		}
		return null;
	}

	/**
	 * フレームの指定された位置のleankersのバリュを返す
	 */
	public String getLeankersSlotValue(int n){
		int count=0;
		for (Iterator it = leankers.entrySet().iterator(); it.hasNext();) {
		    Map.Entry entry = (Map.Entry)it.next();
		    Object key = entry.getKey();
		    Object value = entry.getValue();
		    if(count == n){
		    	return (String)value;
		    }
		    	count++;
		}
		return null;
	}

		/**
		 * フレームの指定された位置のleankersの特定のスロット名のスロット値を返す
		 */
		public ArrayList<String> getLeankersSlotNames(String slotName){
			ArrayList<String> list = new ArrayList<String>();
			for (Iterator it = leankers.entrySet().iterator(); it.hasNext();) {
			    Map.Entry entry = (Map.Entry)it.next();
			    String key = (String) entry.getKey();
			    String value = (String) entry.getValue();
			    //System.out.println("key:"+key+"value"+value);
			    if(value.equals(slotName)){
			    	list.add(key);
			    }
			}
			return list;
		}

	/**
	 * 名前を返す
	 */
	public String getName(){
		return mName;
	}

	public ArrayList getmVals(String slotName){
		return mSlots.get(slotName).getValues();
	}

}