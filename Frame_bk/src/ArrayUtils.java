import java.util.Arrays;

public class ArrayUtils {

	/**
	 * 配列同士を結合する
	 * @param src1
	 * @param src2
	 * @return
	 */
	public static <T> T[] concat(T[] src1, T... src2) {
		if (src1 == null) {
			return src2;
		}
		if (src2 == null) {
			return src1;
		}

		T[] dst = Arrays.copyOf(src1, src1.length+src2.length);
		final int base = src1.length;
		final int size = src2.length;
		for (int i = 0; i < size; ++i) {
			dst[base+i] = src2[i];
		}

		return dst;
	}
}
