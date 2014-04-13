package ipsv2.objects;

public class AccessPoint implements Comparable<AccessPoint>{

	private String mac;
	private String level;
	private String x;
	private String y;
	private String decription;
	private int rssi;
	private int freq;

	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}

	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	@Override
	public String toString() {
		//				return "AccessPoint [mac=" + mac + ", level=" + level + ", x=" + x
		//						+ ", y=" + y + ", decription=" + decription + ", rssi=" + rssi
		//						+ "]";
		return ("ACCESS POINT"
				+ "\nSSID: "+mac
				+ "\nLevel: "+level
				+ "\nSignal Strength: "+rssi
				+ "\nX: "+x
				+ "\nY: "+y
				+ "\nFrequency: "+freq
				+ "\nDistance: "+this.getDistance()
				+ "\nDescription: "+decription);
	}

	/*
	 * Free Space Path Loss Formula
	 * Source: http://rvmiller.com/2013/05/part-1-wifi-based-trilateration-on-android/
	 */
	public double getDistance() {
		if (this.getRssi() == 0)
			return 0;

		double exp = (27.55 - (20 * Math.log10(this.getFreq())) + Math.abs(this.getRssi())) / 20.0;

		if (Math.pow(10.0, exp) >= 25)
			return 25;
		else
			return Math.pow(10.0, exp);
	}
	public int getFreq() {
		return freq;
	}
	public void setFreq(int freq) {
		this.freq = freq;
	}

	public int compareTo(AccessPoint o) {
		int compareRSSI = ((AccessPoint) o).getRssi();
		//sort by better signal strength
		return Math.abs(compareRSSI) - Math.abs(this.rssi);
	}
}
