/* Ross Andreucetti 
 * ross.andreucetti4@mail.dcu.ie
 * EE509
 * 2211742
*/
public class SingleSimulation 	//used for single sim
{
	public static double globalTime;
	public static double packetsArrived;
	public static Source source;
	public static Queue queue;
	public static EventListManager eList;
	
	public void init() //starts us off
	{
		globalTime = 0;
		packetsArrived = 0;
		source = new Source(0.8,Queue.noDestination);
		queue = new Queue(Queue.noDestination,"Single Queue",10);
	}
	
	public static void main(String [] args)
	{
		eList = EventListManager.getInstance();
		SingleSimulation s = new SingleSimulation();
		s.init();
		
		Event event1 = source.genArrival(globalTime, Queue.oneA);	//creates one arrival event to start us
		packetsArrived++;
		eList.insertEvent(event1);
		
		while(packetsArrived<200000)	//runs for first 200000 packet arrivals
		{
			Event e = eList.poll();
			globalTime = e.getEventClock();//advances the clock
			if(e.type==1)	//fresh arrival packets
			{
				double returnTime = queue.addPacket(e);
				if(returnTime>-1)//if packet can be transmitted straight away, schedule its departure
					eList.insertEvent(new Event((returnTime+globalTime),2,Queue.noDestination,Queue.oneA));	//inserts depart event
				eList.insertEvent(source.genArrival(globalTime,Queue.oneA));	//schedules new arrival
				packetsArrived++;
			}
			if(e.type==2)	//departure packets
			{
				double returnTime = queue.removePacket(globalTime);
				if(returnTime>-1)	//if a new packet is being procssesed, schehdule its departure
					eList.insertEvent(new Event((returnTime+globalTime),2,Queue.noDestination,Queue.oneA));	//inserts depart event
			}
		}
		queue.printBatchDetails();		
	}
}
