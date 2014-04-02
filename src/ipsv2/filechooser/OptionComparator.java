package ipsv2.filechooser;

import java.util.Comparator;

public class OptionComparator implements Comparator<Option>{

	@Override
	public int compare(Option op1, Option op2) {
		if(op1 != null)
			return op1.getName().toLowerCase().compareTo(op2.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}

}
