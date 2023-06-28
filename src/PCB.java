public class PCB {
    private String name;
    private String max;
    private String need;
    private String allocation;
    private boolean finish;

    public PCB(String name, String max, String need, String allocation, boolean finish) {
        this.name = name;
        this.max = max;
        this.need = need;
        this.allocation = allocation;
        this.finish = finish;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public String getMax() {return max;}
    public void setMax(String max) {this.max = max;}
    public String getNeed() {return need;}
    public void setNeed(String need) {this.need = need;}
    public String getAllocation() {return allocation;}
    public void setAllocation(String allocation) {this.allocation = allocation;}
    public boolean isFinish() {return finish;}
    public void setFinish(boolean finish) {this.finish = finish;}
}
