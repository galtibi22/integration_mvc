package smartspace.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Location {
	//@NotNull
	//@Min(0)
	private double x;
	//@NotNull
	//@Min(0)
	private double y;
	
	@Column(name="x")
	public double getX() {
		return x;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	@Column(name="y")
	public double getY() {
		return y;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public Location() {}

	public Location(double x, double y){
		this.x=x;
		this.y=y;
	}
}
