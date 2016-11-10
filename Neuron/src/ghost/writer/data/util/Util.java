package ghost.writer.data.util;

import ghost.writer.data.data.WordMap;
import ghost.writer.data.logic.BPFactory;
import ghost.writer.data.logic.WordDataFactory;
import ghost.writer.data.logic.WordDataMap;

public class Util {

	/**
	 * 訓練
	 *
	 * @param マップ情報
	 * @param 指定文字列
	 */
	public static void train(int map[][], String data) {
		double double_map[] = new double[WordMap.unit_width * WordMap.unit_height];
		int temp_index = 0;
		for (int i = 0; i < WordMap.unit_width; ++i) {
			for (int j = 0; j < WordMap.unit_height; ++j) {
				double_map[temp_index] = map[i][j];
				temp_index++;
			}
		}
		String temp_str = WordDataFactory.StringToBinary(data);
		double double_data[] = new double[16 * WordDataMap.MAX_LENTH];
		for (int i = 0; i < 16 * WordDataMap.MAX_LENTH; ++i) {
			double_data[i] = Double.parseDouble(temp_str.charAt(i) + "");
		}
		BPFactory.train(double_map, double_data);
		if (!WordDataFactory.checkContains(data)) {
			WordDataFactory.addData(data);
		}
	}

	/**
	 * 前5件マッチ率高い文字列
	 *
	 * @param map
	 * @return
	 */
	public static String[] getMatchString(int map[][]) {
		double double_map[] = new double[WordMap.unit_width * WordMap.unit_height];
		int temp_index = 0;
		for (int i = 0; i < WordMap.unit_width; ++i) {
			for (int j = 0; j < WordMap.unit_height; ++j) {
				double_map[temp_index] = map[i][j];
				temp_index++;
			}
		}
		double temp_value[] = BPFactory.test(double_map);
		String str1 = "";
		for (int i = 0; i < 16 * WordDataMap.MAX_LENTH; ++i) {
			if (temp_value[i] > 0.5) {
				str1 += 1;
			} else {
				str1 += 0;
			}
		}
		String result[] = new String[] { "未知", "未知", "未知", "未知", "未知" };
		double rank[] = new double[5];
		for (int i = 0; i < WordDataFactory.getSize(); ++i) {
			String str2 = WordDataFactory.StringToBinary(WordDataFactory.getData(i));
			double match_degree = WordDataFactory.matchDegree(str1, str2);
			for (int j = 0; j < 5; ++j) {
				if (match_degree > rank[j]) {
					for (int k = 4; k > j; k--) {
						rank[k] = rank[k - 1];
						result[k] = result[k - 1];
					}
					result[j] = WordDataFactory.getData(i);
					rank[j] = match_degree;
					break;
				}
			}
		}
		for (int i = 0; i < 5; ++i) {
			System.out.print(rank[i] + "|");
		}
		System.out.println();
		return result;
	}

}
