package ghost.writer.data.logic;

import java.io.Serializable;
import java.util.ArrayList;

public class WordDataMap implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<String> data;

	/**
	 * 最大値設定
	 */
	public static final int MAX_LENTH = 10;

	WordDataMap() {
		data = new ArrayList<String>();
	}

	/**
	 * 文字列を戻す
	 *
	 * @param index
	 * @return
	 */
	public String getData(int index) {
		if (index >= data.size() || index < 0) {
			return "指定INDEX範囲外";
		} else {
			return data.get(index);
		}
	}

	/**
	 * 存在チェック
	 *
	 * @param str
	 * @return
	 */
	public boolean checkContains(String str) {
		for (int i = 0; i < data.size(); ++i) {
			if (str.equals(data.get(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * データ辞書サイス取得
	 *
	 * @return
	 */
	public int getSize() {
		return data.size();
	}

	/**
	 * データ辞書に文字列を追加
	 *
	 * @param str
	 */
	public void addData(String str) {
		if (str.length() > MAX_LENTH) {
			str = str.substring(0, MAX_LENTH);
		}
		data.add(str);
	}
}
