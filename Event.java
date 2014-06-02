//package yourPackage...

/**
 * This class is an implementation of the event object to be inserted in the
 * event list. In the current version it contains just an attribute, the clock value
 * at which the event occurs.
 * The student has to define the constructor of this class and other eventual additional attributes
 * It also contains a set of get() and set() methods to retrieve and modify its clock value. 
 * that are necessary to uniquely define the event.
 * 
 * @author Daniele Tafani 10/10/09
 * 
 */
public class Event {

	/**
	 * The clock value of the event.
	 */
	
	public final static int ARRIVAL = 1;
	public final static int DEPARTURE = 2;
	
	
	private double eventClock;
	public int destination;	//destination queue
	public int type; //1 for fresh arrival, 2 for departure events,
	public int source;	//source packet came from, -1 if not applicable

	/**
	 * Constructor of the class: to be developed by the student...
	 * 
	 */
	public Event(double eventClock,int type,int destination,int source) 
	{ 
		this.eventClock = eventClock;
		this.type = type;
		this.destination = destination;
		this.source = source;
		
	}

	/**
	 * Returns the event clock value.
	 * 
	 * @return (double) eventClock : the even clock value (double).
	 */
	public double getEventClock() {
		return eventClock;
	}

	/**
	 * Modifies the event clock value.
	 * 
	 * @param (double) eventClock
	 *            : the new event clock value (double).
	 */
	public void setEventClock(double eventClock) {
		this.eventClock = eventClock;
	}
	public String toString()
	{
		if(type==1)
		{
			return "This is an arrival event schehduled for: " + eventClock +" with destination Queue: "+ destination + " from source : "+source;
		}
		else
		{
			return "This is a departure event schehduled for: " + eventClock;
		}
	}
	
	
	//Add attributes that are necessary to uniquely define the event object.
	// ....
}