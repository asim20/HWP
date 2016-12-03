import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class VM{
	
	public void VirtualMachine(int[] memory){
	
		int[] register=new int[16];
		Stack<Integer> stack=new Stack<Integer>();
		Stack<Integer> subroutine=new Stack<Integer>();
		int indexRx;
		int indexRy;
		int CMD;
		int wert;
		int opcode;
		int programmCounter=0;
		double[] pro=new double[35];
		int proIndex=programmCounter;
		
		do{
			proIndex=programmCounter;
			opcode=memory[programmCounter];
			indexRx=(opcode>>4)&15;
			indexRy=(opcode>>8)&15;
			CMD = opcode&15;
			wert=(opcode>>4);
			
			switch(befehl(CMD)){
			case "NOP":  System.out.println("NOP wird ausgef�hrt");
						 programmCounter++;
						 break;
			case "LOAD": System.out.println("LOAD wird ausgef�hrt");
						 register[0]=wert;
						 //registerAusgabe();
						 programmCounter++;
						 break;
			case "MOV":  System.out.println("MOV wird ausgef�hrt");
						 //System.out.println(Integer.toBinaryString(opcode));
						 if(((opcode>>13)&1)==0 && ((opcode>>12)&1)==0){
							register[indexRx]=register[indexRy];
							System.out.println("reg["+indexRy+"]="+register[indexRy]+"-->"+"reg["+indexRx+"]="+register[indexRx]);
						 }else if(((opcode>>13)&1)==0 && ((opcode>>12)&1)==1){
							 register[indexRx]=memory[register[indexRy]];
							 System.out.println("Memory[register["+indexRy+"]] --> register["+indexRx+"]");
						 }else if(((opcode>>13)&1)==1 && ((opcode>>12)&1)==0){
							 memory[register[indexRx]]=register[indexRy];
							 System.out.println("register["+indexRy+"] --> Memory[register["+indexRx+"]]");
						 }else{
							 memory[register[indexRx]]=memory[register[indexRy]];
							 System.out.println("Memory[register["+indexRx+"]] --> Memory[register["+indexRy+"]]");
						 }
						 //registerAusgabe();
						 programmCounter++;
						 break;
			case "ADD":  System.out.println("Add wird ausgef�hrt");
						 register[indexRx]=register[indexRx]+register[indexRy];
						 programmCounter++;
						 break;
			case "SUB":  System.out.println("SUB wird ausgef�hrt");
						 register[indexRx]=register[indexRx]-register[indexRy];
						 programmCounter++;
						 break;
			case "MUL":  System.out.println("MUL wird ausgef�hrt");
						 register[indexRx]=register[indexRx]*register[indexRy];
						 programmCounter++;
						 break;
			case "DIV":  System.out.println("DIV wird ausgef�hrt");
						 register[indexRx]=register[indexRx]/register[indexRy];
						 programmCounter++;
						 break;
			case "PUSH": System.out.println("PUSH wird ausgef�hrt");
						 stack.push(register[indexRx]);
						 programmCounter++;
						 break;
			case "POP":  if(!stack.isEmpty()){
							 System.out.println("POP wird ausgef�hrt");
							 register[indexRx]=stack.pop();
						 }
						 programmCounter++;
						 break;
			case "JMP": System.out.println("JMP wird ausgef�hrt");
						 programmCounter=wert;
						 break;
			case "JIZ":  
						 if(register[0]==0){
							 System.out.println("JIZ wird ausgef�hrt");
							 programmCounter=wert;
						 }else{
							 programmCounter++;
						 }
						 break;
			case "JIH":  System.out.println("JIH wird ausgef�hrt");
						 if(register[0]>0){
							programmCounter=wert;
						 }else{
							 programmCounter++;
						 }
						 break;
			case "JSR":  System.out.println("JSR wird ausgef�hrt");
						 subroutine.push(programmCounter+1);
						 programmCounter=wert;
						 
						 break;
			case "RTS":  System.out.println("RTS wird ausgef�hrt");
						 if(!subroutine.empty()){
							programmCounter=subroutine.pop();
						 }else{
							 //registerAusgabe();
							 //System.out.println("Summe: "+register[10]);
							 memoryAusgabe(memory);
							 writeFile(pro,memory);
							System.exit(0);
						 }
						 break;
						 
						 
			}
			
			pro[proIndex]+=1;
			
//			if(CMD!=0){
//				profiler.setValue((timeAfter-timeBefore), (programmCounter-1));
//			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			//System.out.println("Counter: "+programmCounter);
		}while(true);
		
	}
	
	private static void memoryAusgabe(int[] memory){
		int i=1000;
		
		do{
			System.out.println("Memory an der stelle "+i+"="+memory[i]);
			i++;
		}while(memory[i]!=0);
	}
	
	private void writeFile(double[] pro,int [] memory) {
		
		 FileWriter fw=null;
		try {
			fw = new FileWriter("Profile.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		 BufferedWriter bw = new BufferedWriter(fw);
		 
		 double summe=0.0;
		 
		 for (int i = 0; i < pro.length; i++) {
			 //System.out.println(pro[i]);
			summe+=pro[i];
		}
			
		 
		try {
			 //double probeSumme=0;
			//System.out.println("Totale Summe "+summe);
			for (int i = 0; i < pro.length; i++) {
				bw.write(""+(Math.round(((pro[i]/summe)*100)*1000)/1000.0+"%"));
				bw.newLine();
				bw.write(befehl(memory[i]&15));
				bw.newLine();
				//probeSumme+=((pro[i]/summe)*100.0);
			}
			//System.out.println(probeSumme);
			
			
			 
			 bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static String befehl(int befehl){
		switch(befehl){
		
			case(0b0000): return "NOP";
			
			case(0b0001): return "LOAD";
			
			case(0b0010): return "MOV";
			 		     
			case(0b0011): return "ADD";
						 
			case(0b0100): return "SUB";
		     			 
			case(0b0101): return "MUL";
						 
			case(0b0110): return "DIV";
			
			case(0b0111): return "PUSH";
						 
			case(0b1000): return "POP";
			 		     
			case(0b1001): return "JMP";
						 
			case(0b1010): return "JIZ";
		     			 
			case(0b1011): return "JIH";
						 
			case(0b1100): return "JSR";
						
			case(0b1101): return "RTS";			 
		
			default: return "NOP";
		}
	}
	
}
