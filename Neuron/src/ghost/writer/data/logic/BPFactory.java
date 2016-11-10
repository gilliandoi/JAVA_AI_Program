package ghost.writer.data.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BPFactory {
	/**
	 * BPニューラルネットワーク
	 */
	private static BP bp;

	/**
	 * 初期化
	 *
	 * @param inputSize
	 * @param hiddenSize
	 * @param outputSize
	 */
	public static void initialization(int inputSize, int hiddenSize, int outputSize) {
		bp = new BP(inputSize, hiddenSize, outputSize);
	}

	/**
	 * ファイルからニューラルネットワークを読込む
	 *
	 * @param file
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void initialization(File file) throws IOException, ClassNotFoundException {
		FileInputStream fi = new FileInputStream(file);
		ObjectInputStream si = new ObjectInputStream(fi);
		bp = (BP) si.readObject();
		si.close();
	}

	/**
	 * ニューラルネットワークをファイルに保存
	 *
	 * @param file
	 * @throws IOException
	 */
	public static void save(File file) throws IOException {
		FileOutputStream fo = new FileOutputStream(file);
		ObjectOutputStream so = new ObjectOutputStream(fo);
		so.writeObject(bp);
		so.close();
	}

	/**
	 * ニューラルネットワークを訓練する
	 *
	 * @param trainData
	 * @param target
	 */
	public static void train(double[] trainData, double[] target) {
		bp.train(trainData, target);
	}

	/**
	 * 予測値戻る
	 *
	 * @param inData
	 * @return
	 */
	public static double[] test(double[] inData) {
		return bp.test(inData);
	}
}
