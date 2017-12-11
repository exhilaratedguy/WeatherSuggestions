package output;

public class Weather {

    private double temp;
    private float pressure, humidity, sea_level, grnd_level;
    private double temp_min, temp_max, temp_kf;

    public double getTemp_kf(){
        return temp_kf;
    }
    public void setTemp_kf(double temp_kf){
        this.temp_kf = temp_kf;
    }
    public float getSea_level(){
        return sea_level;
    }
    public float getGrnd_level(){
        return grnd_level;
    }
    public void setSea_level(float sea_level){
        this.sea_level = sea_level;
    }
    public void setGrnd_level(float grnd_level){
        this.grnd_level = grnd_level;
    }
    public double getTemp() {
        return temp;
    }
    public void setTemp(double temp) {
        this.temp = temp-273.15;
    }
    public float getPressure() {
        return pressure;
    }
    public void setPressure(float pressure) {
        this.pressure = pressure;
    }
    public float getHumidity() {
        return humidity;
    }
    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
    public double getTemp_min() {
        return temp_min;
    }
    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min-273.15;
    }
    public double getTemp_max() {
        return temp_max;
    }
    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max-273.15;
    }

}
