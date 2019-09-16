package temperature;

import java.util.Date;


public class Temperature implements Comparable<Temperature>{
	   private Date date;
	   private Double temperature;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Temperature(Date date, Double temperature) {
		super();
		this.date = date;
		this.temperature = temperature;
	}
	
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	@Override
	public int compareTo(Temperature o) {

		return this.date.compareTo(o.getDate());
	}
}
