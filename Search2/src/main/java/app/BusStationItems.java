package app;

/*
 * 駅ITEM
 */
public class BusStationItems {
	// 駅名
	String name;

	// 座標_X
	Integer x;

	// 座標_Y
	Integer y;

	// 中心点_座標_X
	Integer cx;

	// 中心点_座標_Y
	Integer cy;

	// 座標_X_Vector用
	Integer ax;

	// 座標_Y_Vector用
	Integer ay;

	// ヒューリスティック値
	Integer HValue;

	// G Value
	Double GValue;

	// F Value
	Double FValue;

	// 子駅文字列
	String childStationStr;

	// ポインタ
	BusStationItems pointer;

	/**
	 * @return cx
	 */
	public Integer getCx() {
		return cx;
	}

	/**
	 * @param cx
	 *            セットする cx
	 */
	public void setCx() {
		this.cx = this.x + 9;
	}

	/**
	 * @return cy
	 */
	public Integer getCy() {
		return cy;
	}

	/**
	 * @param cy
	 *            セットする cy
	 */
	public void setCy() {
		this.cy = this.y + 22;
	}

	/**
	 * @return ax
	 */
	public Integer getAx() {
		return ax;
	}

	/**
	 * @param ax
	 *            セットする ax
	 */
	public void setAx() {
		this.ax = this.x + 25;
	}

	/**
	 * @return ay
	 */
	public Integer getAy() {
		return y + 25;
	}

	/**
	 * @param ay
	 *            セットする ay
	 */
	public void setAy() {
		this.ay = ay;
	}

	/**
	 * 駅名を取得する
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 駅名を設定する
	 *
	 * @param name
	 *            セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 座標_Xを取得する
	 *
	 * @return x
	 */
	public Integer getX() {
		return x;
	}

	/**
	 * 座標_Xを設定する
	 *
	 * @param x
	 *            セットする x
	 */
	public void setX(Integer x) {
		this.x = x;
	}

	/**
	 * 座標_Yを取得する
	 *
	 * @return y
	 */
	public Integer getY() {
		return y;
	}

	/**
	 * 座標_Yを設定する
	 *
	 * @param y
	 *            セットする y
	 */
	public void setY(Integer y) {
		this.y = y;
	}

	/**
	 * @return hValue
	 */
	public Integer getHValue() {
		return HValue;
	}

	/**
	 * @return gValue
	 */
	public Double getGValue() {
		return GValue;
	}

	/**
	 * @param gValue
	 *            セットする gValue
	 */
	public void setGValue(Double gValue) {
		GValue = gValue;
	}

	/**
	 * @param hValue
	 *            セットする hValue
	 */
	public void setHValue(Integer hValue) {
		HValue = hValue;
	}

	/**
	 * @return fValue
	 */
	public Double getFValue() {
		return FValue;
	}

	/**
	 * @param fValue セットする fValue
	 */
	public void setFValue(Double fValue) {
		FValue = fValue;
	}

	/**
	 * @return childStationStr
	 */
	public String getChildStationStr() {
		return childStationStr;
	}

	/**
	 * @param childStationStr
	 *            セットする childStationStr
	 */
	public void setChildStationStr(String childStationStr) {
		this.childStationStr = childStationStr;
	}

	/**
	 * @return pointer
	 */
	public BusStationItems getPointer() {
		return pointer;
	}

	/**
	 * @param pointer
	 *            セットする pointer
	 */
	public void setPointer(BusStationItems pointer) {
		this.pointer = pointer;
	}

}
