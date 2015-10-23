package in.technozion.technozion;

/**
 * Created by Sai Teja on 10/22/2015.
 */
public class MarkerItem {

    int markerId;
    String eventName,eventVenue;
    double eventLat,eventLong;
    String eventtype;


    public MarkerItem()
    {

    }
    public MarkerItem(int markerId,String eventName,String eventVenue,double eventLat,double eventLong,String eventtype)
    {
        this.markerId=markerId;
        this.eventName=eventName;
        this.eventLat=eventLat;
        this.eventLong=eventLong;
        this.eventVenue=eventVenue;
        this.eventtype=eventtype;
    }

    public int getMarkerId(){return markerId;}
    public void setMarkerId(int num){this.markerId=num;}

    public String getEventName(){return eventName;}
    public void setEventName(String name){this.eventName=name;}

    public String getEventVenue(){return eventVenue;}
    public void setEventVenue(String name){this.eventVenue=name;}

    public double getEventLat(){return eventLat;}
    public void setEventLat(double num){this.eventLat=num;}

    public double getEventLong(){return eventLong;}
    public void setEventLong(double num){this.eventLong=num;}

    public String getEventtype(){return eventtype;}

    public void setEventtype(String eventtype) {
        this.eventtype = eventtype;
    }
}
