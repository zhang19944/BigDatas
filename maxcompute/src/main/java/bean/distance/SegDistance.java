package bean.distance;

public class SegDistance implements Comparable<SegDistance>{
    private  Double dist;
    private  Long  seg;
    private Long diff;

    public SegDistance(Double dist, Long seg, Long diff) {
        this.dist = dist;
        this.seg = seg;
        this.diff = diff;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public Long getSeg() {
        return seg;
    }

    public void setSeg(Long seg) {
        this.seg = seg;
    }

    public Long getDiff() {
        return diff;
    }

    public void setDiff(Long diff) {
        this.diff = diff;
    }

    public int compareTo(SegDistance o) {
        return this.seg.compareTo(o.seg);
    }
}
