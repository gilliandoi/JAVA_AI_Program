package ghost.writer.data.data;

/**
 * 文字列の保存
 *
 * @author MyGhost
 *
 */
public class WordMap {

	/**
	 * 文字マップ情報
	 */
	private int data[][];

	/**
	 * 文字マップ情報（カット後）
	 */
	private int scan_data[][];

	/**
	 * 文字マップ情報（フォーマット後）
	 */
	private int format_data[][];

	/**
	 * 文字位置情報
	 */
	private int start_w, end_w, start_h, end_h, word_w, word_h;

	/**
	 * 標準的な形のサイズ
	 */
	public static final int unit_width = 50, unit_height = 50;

	/**
	 * マップサイズ
	 */
	private final int width, height;

	/**
	 * 文字マップのフレーム構築
	 *
	 * @param width
	 * @param height
	 */
	public WordMap(int width, int height) {
		data = new int[width][height];
		format_data = new int[unit_width][unit_height];
		this.width = width;
		this.height = height;
	}

	/**
	 * 文字マップ情報取得
	 *
	 * @return 文字マップ情報
	 */
	public int[][] getMap() {
		return data;
	}

	/**
	 * ノードの存在チェック
	 *
	 * @param start_x
	 * @param start_y
	 * @param end_x
	 * @param end_y
	 * @return
	 */
	private boolean checkContain(int start_x, int start_y, int end_x, int end_y) {
		for (int i = start_x; i < end_x; ++i) {
			for (int j = start_y; j < end_y; ++j) {
				if (scan_data[i][j] == 1)
					return true;
			}
		}
		return false;
	}

	/**
	 * フォーマット後のマップデータを表示画面に反映
	 */
	public int[][] copyMap() {
		data = new int[width][height];
		int temp_x, temp_y;
		temp_x = (width - unit_width) / 2;
		temp_y = (height - unit_height) / 2;
		for (int i = 0; i < unit_width; ++i) {
			for (int j = 0; j < unit_height; ++j) {
				data[temp_x + i][temp_y + j] = format_data[i][j];
			}
		}
		return format_data;
	}

	/**
	 * クリア
	 */
	public void resetMap() {
		data = new int[width][height];
		scan_data=null;
		format_data=new int[unit_width][unit_height];
	}

	/**
	 * ノード設定
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean setPoint(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height)
			return false;
		if (data[x][y] != 1) {
			data[x][y] = 1;
			return true;
		}
		return false;
	}

	/**
	 * 文字マップのフレーム取得
	 *
	 * @return 文字マップのフレーム
	 */
	public int[][] getWordMap() {
		scan_data = new int[word_w][word_h];
		for (int i = 0; i < word_w; ++i) {
			for (int j = 0; j < word_h; ++j) {
				scan_data[i][j] = data[start_w + i][start_h + j];
			}
		}
		return scan_data;
	}

	/**
	 * ͨマップフォーマット
	 *
	 * @return フォーマットしたマップ情報
	 */
	public int[][] getFormateMap() {
		float scale_w, scale_h;
		scale_w = (float) word_w / (float) unit_width;
		scale_h = (float) word_h / (float) unit_height;
		for (int i = 0; i < unit_width; ++i) {
			for (int j = 0; j < unit_height; ++j) {
				if (checkContain((int) (i * scale_w), (int) (j * scale_h),
						(int) ((i + 1) * scale_w), (int) ((j + 1) * scale_h))) {
					format_data[i][j] = 1;
				}
			}
		}
		return format_data;
	}

	/**
	 * 文字の横幅取得
	 *
	 * @return 文字の横幅
	 */
	public int getWordWidth() {
		start_w = 0;
		for (int i = 0; i < width; ++i) {
			boolean continue_key = false;
			for (int j = 0; j < height; ++j) {
				if (data[i][j] == 1) {
					continue_key = true;
					break;
				}
			}
			if (continue_key) {
				start_w = i;
				break;
			}
		}
		end_w = 0;
		for (int i = width - 1; i >= 0; --i) {
			boolean continue_key = false;
			for (int j = 0; j < height; ++j) {
				if (data[i][j] == 1) {
					continue_key = true;
					break;
				}
			}
			if (continue_key) {
				end_w = i;
				break;
			}
		}
		word_w = end_w - start_w;
		return word_w;
	}

	/**
	 *文字の高さ取得
	 *
	 * @return 文字の高さ
	 */
	public int getWordHeight() {
		start_h = 0;
		for (int i = 0; i < height; ++i) {
			boolean continue_key = false;
			for (int j = 0; j < width; ++j) {
				if (data[j][i] == 1) {
					continue_key = true;
					break;
				}
			}
			if (continue_key) {
				start_h = i;
				break;
			}
		}
		end_h = 0;
		for (int i = height - 1; i >= 0; --i) {
			boolean continue_key = false;
			for (int j = 0; j < width; ++j) {
				if (data[j][i] == 1) {
					continue_key = true;
					break;
				}
			}
			if (continue_key) {
				end_h = i;
				break;
			}
		}
		word_h = end_h - start_h;
		return word_h;
	}

}
