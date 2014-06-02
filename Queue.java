/* Ross Andreucetti 
 * ross.andreucetti4@mail.dcu.ie
 * EE509
 * 2211742
*/

import java.text.DecimalFormat;
import java.util.LinkedList;


public class Queue 
{
	private double totalWaitingTime;	//statistical data
	private double totalTransmissionTime;
	private double totalTime;
	private double totalBatchTime;
	
	private double packetsOffered;
	private double packetsDropped;
	private double packetsDroppedPerBatch;
	private double packetsTransmitted;
	
	private int sizeLimit;	//limit of queue size
	public int nextQueue;	//points to next queue by int value
	private String name;
	
	private ExpRandGenerator expR;
	public static double mu = 12500000;	//transmission rate
	
	private LinkedList <Event> packets;	//where packets stored, arrival time mainly
	private LinkedList <Double> batchMeans;//statistical
	private LinkedList <Double> lossRatios;
	
	private boolean isBusy;	//is the queue currently transitting?
	
	public static final int oneA = 0;	//static variables 
	public static final int oneB = 1;
	public static final int twoA = 2;
	public static final int twoB = 3;
	public static final int threeA = 4;
	public static final int threeB = 5;
	public static final int threeC = 6;
	public static final int fourA = 7;
	
	public static final int noDestination = -1;	//for queues who's packets leave network
	
	public Queue(int nextQueue, String name, int sizeLimit)//constructor
	{
		totalWaitingTime = 0;
		totalTransmissionTime = 0;
		totalTime = 0;
		totalBatchTime = 0;
		
		packetsOffered = 0;
		packetsDropped = 0;
		packetsDroppedPerBatch = 0;
		packetsTransmitted = 0;
		
		this.sizeLimit = sizeLimit;
		this.nextQueue = nextQueue;
		this.name = name;
		
		expR = new ExpRandGenerator();
		packets = new LinkedList<Event>();
		batchMeans = new LinkedList<Double>();
		lossRatios = new LinkedList<Double>();
		isBusy = false;	//set to false
	}
	
	public double addPacket(Event e)	//arival
	{
		packetsOffered++;
		if(packetsOffered%5000==0)
		{
			calcLossRatio();
		}
		if(packets.size()==sizeLimit)	//drop packet if full
		{
			
			packetsDropped++;
			packetsDroppedPerBatch++;
			return -1;
		}
		if(!isBusy)	//process packet if not busy
		{
			isBusy = true;
			double transmissionTime = expR.genRandom(mu);
			totalTransmissionTime = totalTransmissionTime + transmissionTime;
			totalBatchTime = totalBatchTime + transmissionTime;
			return transmissionTime;
		}
		else	//add to queue otherwise
		{
			packets.offer(e);
			return -1;
		}
	}
	public double removePacket(double currentTime)
	{
		packetsTransmitted++;
		if(packetsTransmitted%5000==0)
		{
			calcBatchTimes();
		}
		if(packets.size()==0)	//set is busy to false if queue is empty
		{
			isBusy = false;
			return -1;
		}
		else	//process next packet in queue
		{
			Event e = packets.poll();
			double waitingTime = currentTime - e.getEventClock();
			totalWaitingTime = totalWaitingTime + waitingTime;
			double transmissionTime = expR.genRandom(mu);
			totalTransmissionTime = totalTransmissionTime + transmissionTime;
			totalBatchTime = totalBatchTime + waitingTime + transmissionTime;
			return transmissionTime;
		}
	}
	private void calcBatchTimes()
	{
		batchMeans.offer((totalBatchTime/5000));
		totalBatchTime=0;
	}
	public void calcLossRatio()
	{
		lossRatios.offer((packetsDroppedPerBatch/5000));
		packetsDroppedPerBatch = 0;
	}
	
	public void printDetails()
	{
		totalTime = totalWaitingTime + totalTransmissionTime;
		System.out.println("-------------"+name+"-------------");
		System.out.println("Packets Transmitted: "+packetsTransmitted);
		System.out.println("Packets dropped ratio: "+roundOff((packetsDropped/packetsOffered)));
		System.out.println("Packets dropped:" + packetsDropped);
		System.out.println("Average Transmission time:" + roundOff((totalTime/packetsTransmitted)));
	}
	public void printBatchDetails()
	{
		System.out.println("-----------------"+name+"--------------");
		System.out.println("Batch means per 5000 transmitted packets:");
		System.out.println("===========================================");
		while(batchMeans.size()!=0)
			System.out.println(batchMeans.poll());
		System.out.println("===========================================");
		
		System.out.println("Lost packet ratio per 5000 offered packets:");
		System.out.println("===========================================");
		while(lossRatios.size()!=0)
			System.out.println(lossRatios.poll());
		System.out.println("===========================================");
	}
	private double roundOff(double d) //used for rounding off decimals
	{
		DecimalFormat df = new DecimalFormat("#.#####");
		return Double.valueOf(df.format(d));
		
	}
}
