
public class Profiler {

	private long profileArray[][];
	private int groe�e;
	
	Profiler(int groe�e){
		this.groe�e=groe�e;
		profileArray=new long[groe�e][];
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
