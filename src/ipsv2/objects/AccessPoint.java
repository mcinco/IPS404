package ipsv2.objects;

public class AccessPoint {

	private String mac;
	private String level;
	private String x;
	private String y;
	private String decription;
	private int rssi;


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
		return "ACCESS POINT"
		+ "\nSSID: "+mac
		+ "\nLevel: "+level
		+ "\nSignal Strength: "+rssi
		+ "\nX: "+x
		+ "\nY: "+y
		+ "\nDescription: "+decription;
	}




}
