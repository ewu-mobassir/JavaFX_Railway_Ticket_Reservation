/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package railwayfull;

/**
 *
 * @author Eram
 */
public class Station {
    private String stationName;
    private int stationId;
    private double distanceFromCentral;
    
    Station(){}
    Station(String stationName,int stationId, double distanceFromCentral){
        this.stationName = stationName;
        this.stationId = stationId;
        this.distanceFromCentral = distanceFromCentral;
    }
    
    public double calcDistance(Station s){
        double d = this.distanceFromCentral-s.distanceFromCentral;
        if(d<0)
            return -(d);
        else
            return d;
    }
    
    public double calcPrice(Station s, String type, Double pricePerKM){
        if(type.equals("Shovon Seat")){
            return this.calcDistance(s)*1*pricePerKM;
        }
        else if(type.equals("Shovon Chair Seat")){
            return this.calcDistance(s)*1.4*pricePerKM;
        }
        else{
            return this.calcDistance(s)*2*pricePerKM;
        }
    }
    
    public boolean getDirection(Station s){
        double d = this.distanceFromCentral-s.distanceFromCentral;
        if(d<0)
            return false;
        else
            return true;
    }

    public String getStationName() {
        return this.stationName;
    }

    public int getStationId() {
        return this.stationId;
    }

    public double getDistanceFromCentral() {
        return this.distanceFromCentral;
    }

    @Override
    public String toString() {
        return  "stationName=" + stationName + ", stationId=" + stationId + ", distanceFromCentral=" + distanceFromCentral;
    }
    
}
