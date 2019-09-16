package battery;

import com.aliyun.odps.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DataCompared  implements WritableComparable<DataCompared>{

    private String pid;
    private String id;
    private Date time;
    private Long soc;
    public Long getSoc() {
		return soc;
	}
	public void setSoc(Long soc) {
		this.soc = soc;
	}
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public void readFields(DataInput in) throws IOException {
		this.pid=in.readUTF();
		try {
			this.time=sdf.parse(in.readUTF());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.id=in.readUTF();
		
	}
	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.pid);
		out.writeUTF(this.id);
		out.writeUTF(sdf.format(this.time));
	}
	@Override
	public int compareTo(DataCompared o) {
		int cmp0=this.pid.compareTo(o.getPid());
		int cmp1=this.id.compareTo(o.getId());
		if(cmp0==0&&cmp1==0){
			cmp0=this.time.compareTo(o.getTime());
		}
		return cmp0;
	}
}
