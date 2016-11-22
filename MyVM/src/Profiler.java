
public class Profiler {

	private long profileArray[][];
	private int groeﬂe;
	
	Profiler(int groeﬂe){
		this.groeﬂe=groeﬂe;
		profileArray=new long[groeﬂe][];
	}
	
	public void setValue(long usedTime,int index){
		boolean ok=false;
		for(int i=0; ok==false ;i++){
			if(profileArray[index][i]==0){
				profileArray[index][i]=usedTime;
				ok=true;
			}	
		}
	}
	
	public long getValue(int index){
		return profileArray[index][index];
	}
	
}
