package de.htw.cv.mj.model;

public class Category {

	private String name;
	private double[] features;
		
	public Category(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public double[] getFeatures() {
		return features;
	}
	public void setFeatures(double[] features) {
		this.features = features;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
