package de.htw.cv.mj.classificator;

/**
 * Represents a classification result as a category and distance.
 * @author Marie Mandrela, Philipp Jährling
 */
public class Result implements Comparable<Result> {

	private String category;
	private double distance;
	
	public Result(String category, double distance) {
		this.category = category;
		this.distance = distance;
	}

	public String getCategory() {
		return category;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Result o) {
		return Double.compare(this.distance, o.getDistance());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
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
		Result other = (Result) obj;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		return true;
	}
	
}
