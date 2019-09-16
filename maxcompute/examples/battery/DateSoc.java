package battery;

import java.util.Comparator;
import java.util.Date;

public class DateSoc  implements Comparable<DateSoc>{
   private Date date;
   private Double soc;
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public DateSoc(Date date, Double soc) {
	super();
	this.date = date;
	this.soc = soc;
}
public Double getSoc() {
	return soc;
}
public void setSoc(Double soc) {
	this.soc = soc;
}
@Override
public int compareTo(DateSoc o) {

	return this.date.compareTo(o.getDate());
}
}
