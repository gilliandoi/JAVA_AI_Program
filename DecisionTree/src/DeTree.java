
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DeTree {
	// 属性リスト
	private ArrayList<String> attribute = new ArrayList<String>();
	// 属性値リスト
	private ArrayList<ArrayList<String>> attributevalue = new ArrayList<ArrayList<String>>();
	// データリスト
	private ArrayList<String[]> data = new ArrayList<String[]>();
	// 決定変数、属性の個数（決定木の高）
	int decatt;
	public static final String patternString = "@ATTR(.*)[{](.*?)[}]";

	Document xmldoc;
	Element root;

	/**
	 * 決定木初期化
	 */
	public DeTree() {
		// 決定木をxmlファイルで出力する
		xmldoc = DocumentHelper.createDocument();
		root = xmldoc.addElement("ルート");
		root.addElement("決定木").addAttribute("value", "");
	}

	/**
	 * メイン
	 */
	public static void main(String[] args) {
		// 決定木初期化
		DeTree inst = new DeTree();

		// 環境情報データ取得
		inst.readARFF(new File("env.arff"));
		// 決定変数設定
		// TODO 野菜種類
		inst.setDec("野菜");
		LinkedList<Integer> ll = new LinkedList<Integer>();
		for (int i = 0; i < inst.attribute.size(); i++) {
			if (i != inst.decatt)
				ll.add(i);
		}
		ArrayList<Integer> al = new ArrayList<Integer>();
		for (int i = 0; i < inst.data.size(); i++) {
			al.add(i);
		}
		inst.buildDT("決定木", "null", al, ll);
		inst.writeXML("dt.xml");
		return;
	}

	/**
	 * 環境情報データ取得
	 *
	 * @param file
	 */
	public void readARFF(File file) {
		try {
			// 環境情報ファイルを読込む
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			Pattern pattern = Pattern.compile(patternString);

			// 繰り返して、環境情報をデータリストにセット
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					attribute.add(matcher.group(1).trim());
					String[] values = matcher.group(2).split(",");
					ArrayList<String> al = new ArrayList<String>(values.length);
					for (String value : values) {
						al.add(value.trim());
					}
					attributevalue.add(al);
				} else if (line.startsWith("@data")) {
					while ((line = br.readLine()) != null) {
						if (line == "")
							continue;
						String[] row = line.split(",");
						data.add(row);
					}
				} else {
					continue;
				}
			}
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 決定変数の個数設定
	 */
	public void setDec(int n) {
		if (n < 0 || n >= attribute.size()) {
			System.err.println("決定変数指定エラー");
			System.exit(2);
		}
		decatt = n;
	}

	/**
	 * 決定変数の設定
	 */
	public void setDec(String name) {
		int n = attribute.indexOf(name);
		setDec(n);
	}

	/**
	 * 平均情報量計算
	 *
	 * @param arr
	 * @return
	 */
	public double getEntropy(int[] arr) {
		double entropy = 0.0;
		int sum = 0;

		// 平均情報量M(C) = - p1log2p1-p2log2p2 -・・・-pnlog2pn
		for (int i = 0; i < arr.length; i++) {
			entropy -= arr[i] * Math.log(arr[i] + Double.MIN_VALUE) / Math.log(2);
			sum += arr[i];
		}

		entropy += sum * Math.log(sum + Double.MIN_VALUE) / Math.log(2);
		entropy /= sum;
		return entropy;
	}

	/**
	 * リストの平均情報量計算
	 *
	 * @param arr
	 * @param sum
	 * @return
	 */
	public double getEntropy(int[] arr, int sum) {
		double entropy = 0.0;
		for (int i = 0; i < arr.length; i++) {
			entropy -= arr[i] * Math.log(arr[i] + Double.MIN_VALUE) / Math.log(2);
		}
		entropy += sum * Math.log(sum + Double.MIN_VALUE) / Math.log(2);
		entropy /= sum;
		return entropy;
	}

	public boolean infoPure(ArrayList<Integer> subset) {
		String value = data.get(subset.get(0))[decatt];
		for (int i = 1; i < subset.size(); i++) {
			String next = data.get(subset.get(i))[decatt];

			if (!value.equals(next))
				return false;
		}
		return true;
	}

	/**
	 * ノードの平均情報量計算
	 *
	 * @param subset
	 * @param index
	 * @return
	 */
	public double calNodeEntropy(ArrayList<Integer> subset, int index) {
		int sum = subset.size();
		double entropy = 0.0;
		int[][] info = new int[attributevalue.get(index).size()][];
		for (int i = 0; i < info.length; i++)
			info[i] = new int[attributevalue.get(decatt).size()];
		int[] count = new int[attributevalue.get(index).size()];
		for (int i = 0; i < sum; i++) {
			int n = subset.get(i);
			String nodevalue = data.get(n)[index];
			int nodeind = attributevalue.get(index).indexOf(nodevalue);
			count[nodeind]++;
			String decvalue = data.get(n)[decatt];
			int decind = attributevalue.get(decatt).indexOf(decvalue);
			info[nodeind][decind]++;
		}
		for (int i = 0; i < info.length; i++) {
			entropy += getEntropy(info[i]) * count[i] / sum;
		}
		return entropy;
	}

	/**
	 * 決定木構築
	 *
	 * @param name
	 * @param value
	 * @param subset
	 * @param selatt
	 */
	public void buildDT(String name, String value, ArrayList<Integer> subset, LinkedList<Integer> selatt) {
		Element ele = null;
		@SuppressWarnings("unchecked")
		List<Element> list = root.selectNodes("//" + name);
		Iterator<Element> iter = list.iterator();
		while (iter.hasNext()) {
			ele = iter.next();
			if (ele.attributeValue("value").equals(value))
				break;
		}
		if (infoPure(subset)) {
			ele.setText(data.get(subset.get(0))[decatt]);
			return;
		}
		int minIndex = -1;
		double minEntropy = Double.MAX_VALUE;
		for (int i = 0; i < selatt.size(); i++) {
			if (i == decatt)
				continue;
			double entropy = calNodeEntropy(subset, selatt.get(i));
			if (entropy < minEntropy) {
				minIndex = selatt.get(i);
				minEntropy = entropy;
			}
		}
		String nodeName = attribute.get(minIndex);
		selatt.remove(new Integer(minIndex));
		ArrayList<String> attvalues = attributevalue.get(minIndex);
		for (String val : attvalues) {
			ele.addElement(nodeName).addAttribute("value", val);
			ArrayList<Integer> al = new ArrayList<Integer>();
			for (int i = 0; i < subset.size(); i++) {
				if (data.get(subset.get(i))[minIndex].equals(val)) {
					al.add(subset.get(i));
				}
			}
			buildDT(nodeName, val, al, selatt);
		}
	}

	/**
	 * 決定木出力
	 *
	 * @param filename
	 */
	public void writeXML(String filename) {
		try {

			File file = new File(filename);
			if (!file.exists())
				file.createNewFile();
			FileWriter fw = new FileWriter(file);

			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter output = new XMLWriter(fw, format);
			output.write(xmldoc);
			output.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
