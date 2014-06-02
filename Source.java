/* Ross Andreucetti 
 * ross.andreucetti4@mail.dcu.ie
 * EE509
 * 2211742
*/
public class Source 
{
	private double erlangs;
	private int destination;
	private double lamda;
	private ExpRandGenerator expR;
	
	public Source(double erlangs,int destination)	//constructor
	{
		this.erlangs = erlangs;
		this.destination = destination;	//destination queue
		this.lamda = 12500000 * this.erlangs; //calcs lamda
		expR = new ExpRandGenerator();
	}
	public Event genArrival(double currentTime, int sourceNum)	//generates arrival for future time
	{
		return new Event((expR.genRandom(lamda)+currentTime),Event.ARRIVAL,destination,sourceNum);
	}
}
