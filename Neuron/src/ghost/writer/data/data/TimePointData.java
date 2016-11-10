package ghost.writer.data.data;

public class TimePointData {

	/**
	 * ノードの座標ֵ
	 */
	public int x,y,z;

	/**
	 * x,y,z上のVector
	 */
	public double vx,vy,vz;

	/**
	 * ノードの番号
	 */
	public long index;

	public TimePointData(int x,int y,int z,int index) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.index=index;
	}

}
