package ipsv2.objects;
import AccessPoint;
import ScanResult;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Triangulation {

	private static BufferedReader br;
	private static String file = "/home/micah/workspace/Triangulation/APs.csv";
	private static String levelNo = "";

	//ArrayLists
	private static ArrayList<AccessPoint> aps = new ArrayList<AccessPoint>();				//stores all loaded access points
	private static ArrayList<ScanResult> wlist = new ArrayList<ScanResult>();
	private static ArrayList<String> coordinates = new ArrayList<String>();

	//Location Coordinates
	private static String X = "";
	private static String Y = "";

	/**
	 * Loads all access points in the CSV file loaded from filechooser,
	 * then saves it in an arraylist of AccessPoints with the mac address,
	 * floor level, X and Y coordinates, description, level and frequency
	 * IFF loaded level is the user's chosen level and the access point
	 * was detected at the time.
	 */
	private static void loadAPs() {
		try {
			br = new BufferedReader(new FileReader(file));

			while(((file = br.readLine()) != null)) {
				AccessPoint a = new AccessPoint();
				String[] result = file.split(",");
				a.setMac(result[0]);
				a.setLevel(result[1]);
				a.setX(result[2]);
				a.setY(result[3]);
				a.setDecription(result[4]);
				for (ScanResult sr : wlist){
					if (sr.BSSID.equalsIgnoreCase(a.getMac())){
						a.setRssi(sr.level);
						a.setFreq(sr.frequency);
						//Log.i("freq", Integer.toString(sr.frequency));
						//Log.i("dBm", Integer.toString(sr.level));
					}
				}
				if (a.getLevel().equals("2") && a.getRssi() != 0)
					aps.add(a);
			}
			br.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void findLocation() {
		if (aps.size() == 0){
			X = "Not found";
			Y = "Not found";
		}
		else if (aps.size() == 1){
			oneAP();
		}
		else if (aps.size() == 2){
			twoAPs();
		}
		else if (aps.size() == 3){
			threeAPs();
		}
		else if (aps.size() > 3){
			System.out.println("Size of aps: "+aps.size());
			Collections.sort(aps);
			ArrayList<AccessPoint> top3 = new ArrayList<AccessPoint>(aps.subList(aps.size() -3, aps.size()));
			aps = top3;
			threeAPs();
		}

	}

	/*
	 * Check if two "circles" intersect
	 */
	public static boolean isIntersecting(double x1, double y1, double x2, double y2, double r1, double r2){
		double a = (r1+r2)*(r1+r2);
		double dx = x1 - x2;
		double dy = y1 - y2;
		if (a > (dx*dx)+(dy*dy))
			return true;
		else
			return false;
	}
	
	private static double getMidpointX(double x1, double x2, double r1, double r2, double d){
		double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2))/(2*d);
		return x1 + (d1 * (x2 - x1))/d;
	}

	private static double getMidpointY(double y1, double y2, double r1, double r2, double d) {
		double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2))/(2*d);
		return y1 + (d1 * (y2 - y1))/d;
	}

	private static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2-y1, 2) + Math.pow(x2-x1,  2));		
	}

	private static void threeAPs() {
		//Values from AccessPoint data
		double x1, x2, x3, y1, y2, y3 , r1, r2, r3 = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		x1 = Double.parseDouble(aps.get(0).getX());
		y1 = Double.parseDouble(aps.get(0).getY());
		x2 = Double.parseDouble(aps.get(1).getX());
		y2 = Double.parseDouble(aps.get(1).getY());
		x3 = Double.parseDouble(aps.get(2).getX());
		y3 = Double.parseDouble(aps.get(2).getY());
		r1 = aps.get(0).getDistance();
		r2 = aps.get(1).getDistance();
		r3 = aps.get(2).getDistance();

		boolean oneNtwo = isIntersecting(x1, y1, x2, y2, r1, r2);
		boolean twoNthree = isIntersecting(x2, y2, x3, y3, r2, r3);
		boolean oneNthree = isIntersecting(x1, y1, x3, y3, r1, r3);

		if (oneNtwo && twoNthree && oneNthree){
			double d1 = getDistance(x1, y1, x2, y2);
			double d2 = getDistance(x2, y2, x3, y3);
			double d3 = getDistance(x1, y1, x3, y3);

			//oneNtwo - midpoint of the two possible coordinates
			double dx1 = getMidpointX(x1, x2, r1, r2, d1);
			double dy1 = getMidpointY(y1, y2, r1, r2, d1);
			//twoNthree - midpoint of the two possible coordinates
			double dx2 = getMidpointX(x2, x3, r2, r3, d2);
			double dy2 = getMidpointY(y2, y3, r2, r3, d2);
			//oneNthree - midpoint of the two possible coordinates
			double dx3 = getMidpointX(x1, x3, r1, r3, d3);
			double dy3 = getMidpointY(y1, y3, r1, r3, d3);

			String finalX = df.format((dx1 + dx2 + dx3)/3); 
			String finalY = df.format((dy1 + dy2 + dy3)/3); 
			System.out.println("X: "+finalX+", Y: "+finalY);
		}
		else {
			String dx = df.format((x1 + x2 + x3)/3); 
			String dy = df.format((y1 + y2 + y3)/3); 
			System.out.println("X: "+dx+", Y: "+dy);
		}
	}

	/*
	 * http://fypandroid.wordpress.com/2011/07/03/how-to-calculate-the-intersection-of-two-circles-java/
	 */
	private static void twoAPs() {
		//Values from AccessPoint data
		double x1, x2, y1, y2, r1, r2 = 0;
		DecimalFormat df = new DecimalFormat("#.##");
		x1 = Double.parseDouble(aps.get(0).getX());
		y1 = Double.parseDouble(aps.get(0).getY());
		x2 = Double.parseDouble(aps.get(1).getX());
		y2 = Double.parseDouble(aps.get(1).getY());
		r1 = aps.get(0).getDistance();
		r2 = aps.get(1).getDistance();
		//x1 = -9; x2 = 5; y1 = 1; y2 = -5; r1 = 7; r2 = 18;
		boolean intersects = isIntersecting(x1, y1, x2, y2, r1, r2);
		double d = getDistance(x1, y1, x2, y2);
		/* 
		 * If they intersect, take the midpoint between two possible coordinates.
		 * If not, take the midpoint between the two access points. 
		 */
		if (!intersects){
			X = Double.toString(Math.abs(x1+x2)/2);
			Y = Double.toString(Math.abs(y1+y2)/2);
			System.out.println("Doesn't intersect");
			System.out.println("X: "+X+", Y: "+Y);
		}
		else if (intersects){

			double d1 = (Math.pow(r1, 2) - Math.pow(r2, 2) + Math.pow(d, 2))/(2*d);
			System.out.println("d1: "+d1);
			double h = Math.sqrt(Math.pow(r1, 2) - Math.pow(d1, 2));
			double x3 = x1 + (d1 * (x2 - x1))/d;
			double y3 = y1 + (d1 * (y2 - y1))/d;
			System.out.println("Intersects");
			//Get intersecting coordinates
			String dx =  df.format(x3 + (h*(y2-y1))/d);
			String dy = df.format(y3 - (h*(x2-x1))/d);
			String dx2 = df.format(x3 - (h*(y2-y1))/d);
			String dy2 = df.format(y3 + (h*(x2-x1))/d);
			coordinates.add("("+dx+", "+dy+")");
			coordinates.add("("+dx2+", "+dy2+")");
			coordinates.add("Midpoint: ("+df.format(x3)+", "+df.format(y3)+")");
		}
		System.out.println("Two AccessPoints detected:");
		for (String s : coordinates)
			System.out.println(s);
	}

	/*
	 * http://board.flashkit.com/board/showthread.php?773919-trying-to-find-coordinates-for-points-around-a-circle
	 */
	private static void oneAP() {
		DecimalFormat df = new DecimalFormat("#.##");
		double angle = Math.PI * 2/6;
		for (int i = 0; i < 6; ++i){
			double theta = angle * i;
			double radius = aps.get(0).getDistance();
			String X = df.format(Math.cos(theta) * radius + Double.parseDouble(aps.get(0).getX()));
			String Y = df.format(Math.sin(theta) * radius + Double.parseDouble(aps.get(0).getY()));
			coordinates.add("("+X+", "+Y+")");
		}
		System.out.println("Only one AccessPoint detected:");
		for (String s : coordinates)
			System.out.println(s);
	}

	private static void scanAPs(){

		ScanResult sr = new ScanResult("08:17:35:9d:26:90", -42, 2462);
		ScanResult sr2 = new ScanResult("08:17:35:9d:30:20", -74, 2437);
		ScanResult sr3 = new ScanResult("08:17:35:9d:27:00", -34, 2462);
		ScanResult sr4 = new ScanResult("08:17:35:9d:10:c0", -67, 2462);
		ScanResult sr5 = new ScanResult("08:17:35:9c:8a:70", -56, 2462);
		wlist.add(sr);
		wlist.add(sr2);
		wlist.add(sr3);
		wlist.add(sr4);
		wlist.add(sr5);

	}

	public static void main (String [] args){
		Scanner reader = new Scanner(System.in);
		System.out.println("Type in the floor level (2 or 3 only): ");
		levelNo = reader.next();

		scanAPs();
		loadAPs();
		findLocation();

		if (aps.size() == 0){
			System.out.println("No Access Points detected.");
		}

		//System.out.println("X: "+X+", Y: "+Y);
		System.out.printf("================\n");
		for (AccessPoint a : aps)
			System.out.printf("\n"+a.toString()+"\n");
	}
}
