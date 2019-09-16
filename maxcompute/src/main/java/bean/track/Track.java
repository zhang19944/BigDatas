package bean.track;

public class Track {
        protected String sPlaceName;
        protected String eLocation;
        protected long eTime;
        protected int costTime;
        protected int soc;
        protected String ePlaceName;
        protected long sTime;
        protected String sLocation;
        protected double energy;
        public void setSPlaceName(String sPlaceName) {
            this.sPlaceName = sPlaceName;
        }
        public String getSPlaceName() {
            return sPlaceName;
        }

        public void setELocation(String eLocation) {
            this.eLocation = eLocation;
        }
        public String getELocation() {
            return eLocation;
        }

        public void setETime(long eTime) {
            this.eTime = eTime;
        }
        public long getETime() {
            return eTime;
        }

        public void setCostTime(int costTime) {
            this.costTime = costTime;
        }
        public int getCostTime() {
            return costTime;
        }

        public void setSoc(int soc) {
            this.soc = soc;
        }
        public int getSoc() {
            return soc;
        }

        public void setEPlaceName(String ePlaceName) {
            this.ePlaceName = ePlaceName;
        }
        public String getEPlaceName() {
            return ePlaceName;
        }

        public void setSTime(long sTime) {
            this.sTime = sTime;
        }
        public long getSTime() {
            return sTime;
        }

        public void setSLocation(String sLocation) {
            this.sLocation = sLocation;
        }
        public String getSLocation() {
            return sLocation;
        }

        public void setEnergy(double energy) {
            this.energy = energy;
        }
        public double getEnergy() {
            return energy;
        }

    }
