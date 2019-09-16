package battery;

import java.util.Date;

public class DateSoc  implements Comparable<DateSoc>{
   private Date date;
   private Integer soc;
   private Long count48v;
   private  Long count60v;
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}

	public Long getCount48v() {
		return count48v;
	}

	public DateSoc(Date date, Integer soc, Long count48v, Long count60v) {
		this(date,soc);
		this.count48v = count48v;
		this.count60v = count60v;
	}

	public void setCount48v(Long count48v) {
		this.count48v = count48v;
	}

	public Long getCount60v() {
		return count60v;
	}

	public void setCount60v(Long count60v) {
		this.count60v = count60v;
	}

	public DateSoc(Date date, Integer soc) {
	super();
	this.date = date;
	this.soc = soc;

}
public Integer getSoc() {
	return soc;
}
public void setSoc(Integer soc) {
	this.soc = soc;
}

public int compareTo(DateSoc o) {

	return this.date.compareTo(o.getDate());
}
}
