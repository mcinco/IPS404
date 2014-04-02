package ipsv2.filechooser;

import java.io.Serializable;

public class Option implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name, data, path;

	public Option(String n,String d,String p)
	{
		name = n;
		data = d;
		path = p;
	}
	public String getName()
	{
		return name;
	}
	public String getData()
	{
		return data;
	}
	public String getPath()
	{
		return path;
	}
	public int compareTo(Option o) {
		if(this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
}
