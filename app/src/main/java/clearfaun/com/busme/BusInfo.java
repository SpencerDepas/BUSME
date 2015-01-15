package clearfaun.com.busme;

/**
 * Created by spencer on 1/15/2015.
 */
public class BusInfo {


    public int stopCode = 0;
    public String busName = "";
    public float distance = 0;
    //constructor


    public void busCode(int codeforStop) {
        stopCode = codeforStop;
    }

    public void busName(String nameOfBus) {
        busName = nameOfBus;
    }

    public void busDistance(float busDistance) {
        distance = busDistance;
    }





    public int getBusCode(){
        return stopCode;
    }

    public String getBusName(){
        return busName;
    }

    public float getDistance() {
        return distance;
    }





}
