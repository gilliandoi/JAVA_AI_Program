
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

public class OurFrameSystem extends AIFrameSystem {
	private static final String[] CLASS_FRAME_FILES = {"dbpedia_classes.txt", "kt_classes.txt"};
	//private static final String[] CLASS_FRAME_FILES = {"kt_classes.txt"};
	private static final String[] INSTANCE_FRAME_FILES = {"nagoya_instances.txt", "kt_instances.txt"};
	//private static final String[] INSTANCE_FRAME_FILES = {"kt_instances.txt"};
	public OurFrameSystem() {
		// 初期フレームを読み込む
		setupFrames();

		setupDemon();

	}

	/**
	 * 初期フレームを読み込む
	 */
	private void setupFrames() {
		// ファイルから読み込む
		for (String filename : CLASS_FRAME_FILES) {
			try {
				loadFromFile(new File(filename), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (String filename : INSTANCE_FRAME_FILES) {
			try {
				loadFromFile(new File(filename), false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	/**
	 * ファイルからフレームを読み込み追加する
	 * @param file
	 * @param treatAsClass is-a スロットの定義が無いフレームをクラスとして取り扱う場合 true, インスタンスとして扱う場合 false
	 * @throws IOException
	 */
	private void loadFromFile(File file, boolean treatAsClass) throws IOException {
		new Parser().parse(file, treatAsClass);
	}



	/**
	 * ファイルかラクラスフレーム, インスタンスフレームを読み込むためのパーサ
	 */
	private class Parser {
		HashMap<String,ParseData> parseMap = new HashMap<String,ParseData>();

		public void parse(File file, boolean treatAsClass) throws IOException {
			// ファイルから読み込み
			parseFile(file);

			// フレームを作成して追加
			addFrames(treatAsClass);

			//フレームにスロットを追加
			addSlot();
		}

		private void parseFile(File file) throws IOException {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				String buf = null;
				while ((buf = reader.readLine()) != null) {
					StringTokenizer st = new StringTokenizer(buf);
					if (st.countTokens() != 3) {
						System.out.println("ignoring "+buf);
						continue;
					}

					final String inName = st.nextToken();
					final String slotName = st.nextToken();
					final String slotValue = st.nextToken();


					ParseData parseData = parseMap.get(inName);
					if (parseData == null) {
						parseData = new ParseData();
						parseMap.put(inName, parseData);
					}

					//変更
					if(slotName.equalsIgnoreCase("is-a") && slotValue.equalsIgnoreCase("Class")){
						createClassFrame(inName);
					}else{
					parseData.add(slotName, slotValue);
				}
				}
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		}

		private void addFrames(boolean treatAsClass) {
			Iterator<String> it = parseMap.keySet().iterator();

			while (it.hasNext()) {
				String inName = it.next();
				ParseData parseData = parseMap.get(inName);

				// フレームのタイプ (クラスかインスタンスか)
				boolean isClass = parseData.superNames == null ? treatAsClass : 0 <= Arrays.binarySearch(parseData.superNames, "Class");

				// フレームを作る
				if (isClass) {
					if (parseData.superClasses == null) {
						createClassFrame(inName);
					} else {
						for (String inSuperClass : parseData.superClasses) {
							createClassFrame(inSuperClass, inName);
							writeLeankers(inSuperClass,inName,"Ako");
						}
					}
				} else {
					if (parseData.superNames == null) {
						//System.out.println("Found no \"is-a\" slot value for "+inName+", ignoring this frame.");
						continue;
					} else {
						for (String inSuperName : parseData.superNames) {
							createInstanceFrame(inSuperName, inName);
							writeLeankers(inSuperName,inName,"is-a");
						}
					}
				}

			}
		}


		private void addSlot() {
			Iterator<String> it = parseMap.keySet().iterator();

			while (it.hasNext()) {
				String inName = it.next();

				ParseData parseData = parseMap.get(inName);

				if (parseData.superNames == null) {
					//System.out.println("Found no \"is-a\" slot value for "+inName+", ignoring this frame.");
					continue;
				}

				// スロットに値を入れる
				Set<Entry<String,String[]>> entrySet = parseData.slotValues.entrySet();
				for (Entry<String,String[]> e : entrySet) {
					final String slotName = e.getKey();
					final String[] slotValues = e.getValue();

					for (String slotValue : slotValues) {
						if(slotName.equals("is-a") || slotName.equals("ako")){
							getFrame(inName).addSuper(slotValue);
						}
						if(getFrame(inName).hasSlot(slotName)){
							getFrame(inName).addSlotValue(slotName, slotValue);
						}else{
						writeSlotValue(inName, slotName, slotValue);
						}
						writeLeankers(slotValue,inName,slotName);
					}
				}

				it.remove();

			}
		}
		/**
		 *  パース途中のデータを保持しておくためのクラス
		 */
		private class ParseData {
			String[] superNames;
			String[] superClasses;
			HashMap<String,String[]> slotValues = new HashMap<String,String[]>();

			void add(String key, final String value) {
				if (key.equalsIgnoreCase("is-a")) {
					superNames = ArrayUtils.concat(superNames, value);
				} else if (key.equalsIgnoreCase("Ako")) {
					superClasses = ArrayUtils.concat(superClasses, value);
				} else {
					String[] values = slotValues.get(key);
					values = ArrayUtils.concat(values, value);
					slotValues.put(key, values);
				}
			}
		}
	}

	/**
	 * 家族親戚を求めるDemonを追加
	 */
	private void setupDemon(){
		setWhenRequestedProc("人間", "兄", new AIDemonProcs.OldBrother());
		setWhenRequestedProc("人間", "弟", new AIDemonProcs.YoungBrother());
		setWhenRequestedProc("人間", "姉", new AIDemonProcs.OldSister());
		setWhenRequestedProc("人間", "妹", new AIDemonProcs.YoungSister());
		setWhenRequestedProc("人間", "祖父", new AIDemonProcs.Grandpa());
		setWhenRequestedProc("人間", "祖母", new AIDemonProcs.Grandma());
		setWhenRequestedProc("人間", "息子", new AIDemonProcs.Son());
		setWhenRequestedProc("人間", "娘", new AIDemonProcs.Daughter());
		setWhenRequestedProc("人間", "叔父", new AIDemonProcs.Uncle());
		setWhenRequestedProc("人間", "伯父", new AIDemonProcs.Uncle());
		setWhenRequestedProc("人間", "叔母", new AIDemonProcs.Aunt());
		setWhenRequestedProc("人間", "伯母", new AIDemonProcs.Aunt());
		setWhenRequestedProc("人間", "従兄弟", new AIDemonProcs.Cousin());
		setWhenRequestedProc("人間", "孫", new AIDemonProcs.Grandchi());
		setWhenRequestedProc("人間", "甥", new AIDemonProcs.Nephew());
		setWhenRequestedProc("人間", "姪", new AIDemonProcs.Niece());
	}

	public static void main(String[] args) {
		OurFrameSystem ofs = new OurFrameSystem();

	}
}
