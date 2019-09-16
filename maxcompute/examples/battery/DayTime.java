package battery;

import java.util.Date;

public class DayTime  implements Comparable<DayTime> {
	private Date day;

	public Date getDay() {
		return day;
	}

	public DayTime(Date day){
		this.day=day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
   
	@Override
	public int compareTo(DayTime o) {

		return this.day.compareTo(o.getDay());
	}
}
