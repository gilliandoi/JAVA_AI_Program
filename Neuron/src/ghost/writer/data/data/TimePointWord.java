package ghost.writer.data.data;

import java.util.Vector;

public class TimePointWord {

	/**
	 * ノード
	 */
	private Vector<TimePointData> points;

	public TimePointWord() {
		points=new Vector<TimePointData>();
	}

	/**
	 * ノード追加
	 * @param x
	 * @param y
	 * @param z
	 * @param index
	 */
	public void addPoint(int x,int y,int z,int index) {
		points.add(new TimePointData(x, y, z, index));
	}

	public void analysis() {
		for(int i=0;i<points.size();++i) {

		}
	}
}
