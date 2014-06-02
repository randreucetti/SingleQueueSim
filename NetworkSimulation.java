/* Ross Andreucetti 
 * ross.andreucetti4@mail.dcu.ie
 * EE509
 * 2211742
*/
//main class
// good idea to output to file if printing batchdetails..
public class NetworkSimulation 
{
	public static double globalTime;	
	public static double packetsArrived;
	public static Source [] sources;
	public static Queue [] queues;
	public static EventListManager eList;
	
	public NetworkSimulation()	//constructor
	{
		globalTime = 0;	//sets variables appropriately
		packetsArrived = 0;
		sources = new Source[6];
		queues = new Queue [8];
		eList = EventListManager.getInstance();
		
		sources[0] = new Source(.7,Queue.oneB);	//initialises the sources
		sources[1] = new Source(.2,Queue.oneB);
		sources[2] = new Source(.3,Queue.oneA);
		sources[3] = new Source(.25,Queue.twoB);
		sources[4] = new Source(.8,Queue.threeA);
		sources[5] = new Source(.2,Queue.threeB);
		
		queues[Queue.oneA] = new Queue(Queue.twoB,"Queue 1A",5);	//inits queues and points them to next queue appropriately
		queues[Queue.oneB] = new Queue(Queue.threeC,"Queue 1B",5);
		queues[Queue.twoA] = new Queue(Queue.noDestination,"Queue 2A",5);
		queues[Queue.twoB] = new Queue(Queue.fourA,"Queue 2B",5);
		queues[Queue.threeA] = new Queue(Queue.twoA,"Queue 3A",5);
		queues[Queue.threeB] = new Queue(Queue.fourA,"Queue 3B",5);
		queues[Queue.threeC] = new Queue(Queue.noDestination,"Queue 3C",5);
		queues[Queue.fourA] = new Queue(Queue.noDestination,"Queue 4A",10);
	}
	public void genInitPackets() //generates one pack from each source to start us off
	{
		for(int i=0;i<sources.length;i++)
		{
			eList.insertEvent(sources[i].genArrival(globalTime,i));
			packetsArrived++;
		}
	}
	
	public static void main(String [] args)
	{
		NetworkSimulation s = new NetworkSimulation();
		s.genInitPackets();
		
		while(packetsArrived <600000)	//set to whatever amount desired
		{
			Event e = eList.poll();	//get event
			globalTime = e.getEventClock();	//update time
			if(e.type==Event.ARRIVAL)	//arrival event
			{
				double returnTime = queues[e.destination].addPacket(e);	//call add packet
				if(returnTime>-1)	//if it triggers departure (packet goes straight to transmission)
					eList.insertEvent(new Event((returnTime+globalTime),2,e.destination,-1));	//schedule departure
				if(e.source>-1)	//if packet came from source (fresh packet)
				{
					eList.insertEvent(sources[e.source].genArrival(globalTime, e.source));	//gen fresh packet
					packetsArrived++;
				}
			}
			if(e.type==Event.DEPARTURE)
			{
				int nextQueue = queues[e.destination].nextQueue;	//checks if departure is to be sent to new queue
				double returnTime = queues[e.destination].removePacket(globalTime);	//removes packet
				if(returnTime>-1)	//checks if this triggers another departure event, new packet sent to transmission
					eList.insertEvent(new Event((returnTime+globalTime),2,e.destination,-1));
				if(nextQueue>-1)	//check if departing packet is heading to another queue
				{
					eList.insertEvent(new Event(globalTime,Event.ARRIVAL,nextQueue,-1));
				}
			}
		}
		for(int i=0;i<queues.length;i++)
		{
			queues[i].printDetails();	//prints general queue info
		}		
		/*for(int i=0;i<queues.length;i++)
		{
			queues[i].printBatchDetails();	//prints batch queue info PRODUCES LOTS OF TEXT 
		}	*/
	}
}
