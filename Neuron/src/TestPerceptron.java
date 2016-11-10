
import java.util.Random;

public class TestPerceptron {
	/**
	 * 単純パーセプトロンの実装
	 *
	 * @author karura
	 * @param args
	 */
	public static void main(String[] args) {
		// OR計算の教師データ
		// 入力データ配列 x =(入力1,入力2)の配列と,正解データ配列 answer
		final double[][] x = { { 1.0f, 1.0f }, { 1.0f, 0.0f }, { 0.0f, 1.0f }, { 0.0f, 0.0f } };
		final double[] answer = { 1.0f, 1.0f, 1.0f, 0.0f };

		// パーセプトロン作成
		// 初期状態の出力
		Perceptron perceptron = new Perceptron(2);
		System.out.println("[init]  " + perceptron);

		// 学習
		int succeed = 0; // 連続正解回数を初期化
		for (int i = 0; i < 1000; i++) {
			// 行間を空ける
			System.out.println();
			System.out.println(String.format("Trial:%d", i));

			// 使用する教師データを選択
			int k = i % answer.length;

			// 出力値を推定
			double outY = perceptron.output(x[k]);
			System.out.println(String.format("[input] %f , %f", x[k][0], x[k][1]));
			System.out.println(String.format("[output] %f", outY));

			// 評価・判定
			if (answer[k] != outY) {
				// 連続正解回数を初期化
				succeed = 0;

				// 学習
				System.out.println("[learn] before :" + perceptron);
				perceptron.learn(answer[k], outY, x[k]);
				System.out.println("[learn] after  :" + perceptron);

			} else {
				// 連続正解回数を更新
				// すべての教師データで正解を出せたら終了
				if (++succeed >= answer.length) {
					break;
				}
			}
		}

		// すべての教師データで正解を出すか
		// 収束限度回数(1000回)を超えた場合に終了
		System.out.println("[finish] " + perceptron);

	}
}

/**
 * パーセプトロンを表すクラス
 *
 * ■x1 → V1 ■ → y1 θ ■x2 → V2
 *
 * x:入力 y:出力 v:結合加重 θ:閾値 標準デルタ則を利用
 *
 * @author karura
 *
 */
class Perceptron {

	// 内部変数
	private int inputNeuronNum = 0; // 入力の数
	private double[] inputWeights = null; // 入力ごとの結合加重
	private double threshold = 0; // 閾値θ
	private double epsilon = 0.01f; // 学習用の定数ε

	/**
	 * 初期化
	 *
	 * @param inputNeuronNum
	 *            入力ニューロン数
	 */
	public Perceptron(int inputNeuronNum) {
		// 変数初期化
		Random r = new Random();
		this.inputNeuronNum = inputNeuronNum;
		this.inputWeights = new double[inputNeuronNum];
		this.threshold = r.nextDouble(); // 閾値をランダムに生成

		// 結合加重を乱数で初期化
		for (int i = 0; i < inputWeights.length; i++) {
			this.inputWeights[i] = r.nextDouble();
		}

		// 確認メッセージ
		System.out.println("Init Neuron!");
	}

	/**
	 * 学習
	 *
	 * @param t
	 *            教師データ
	 * @param o
	 *            出力値
	 * @param inputValues
	 *            入力データ
	 */
	public void learn(double t, double o, double[] inputValues) {
		// 標準デルタ則にしたがって学習
		for (int i = 0; i < inputNeuronNum; i++) {
			inputWeights[i] += epsilon * (t - o) * inputValues[i];
			// System.out.println( String.format( "%f, %f , %f , %f , %f" ,
			// epsilon , t , o , inputValues[i] , epsilon * ( t - o ) *
			// inputValues[i] ) );
		}
	}

	/**
	 * 計算
	 *
	 * @param inputValues
	 *            入力ニューロンからの入力値
	 * @return 推定値
	 */
	public double output(double[] inputValues) {
		// 入力値の総和を計算
		double sum = 0;
		for (int i = 0; i < inputNeuronNum; i++) {
			sum += inputValues[i] * inputWeights[i];
		}

		// 出力関数は階段関数
		double out = (sum > threshold) ? 1 : 0;

		return out;
	}

	/**
	 * クラス内部確認用の文字列出力
	 */
	@Override
	public String toString() {
		// 出力文字列の作成
		String output = "weight : ";
		for (int i = 0; i < inputNeuronNum; i++) {
			output += inputWeights[i] + " , ";
		}

		return output;

	}
}
