import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class HWP {
	
	public static int[] memory=new int[4096];
	public static int opcode;
	public static int[] register=new int[16];
	public static int programmCounter=0;
	public static Stack<Integer> stack=new Stack<Integer>();
	public static Stack<Integer> subroutine=new Stack<Integer>();

	public static void main(String[] args) {
		
			try {
				assembler();
				VM();
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
	}
	
	public static void VM(){
		
		
		int indexRx;
		int indexRy;
		int CMD;
		int wert;
		programmCounter=0;
		long timeBefore;
		long timeAfter;
		Profiler profiler=new Profiler(10000);
		
		do{
			timeBefore=System.currentTimeMillis();
			opcode=memory[programmCounter];
			//System.out.println("opcode "+opcode+" mem "+memory[programmCounter]);
			indexRx=(opcode>>4)&15;
			indexRy=(opcode>>8)&15;
			CMD = opcode&15;
			System.out.println("CMD  "+CMD);
			wert=(opcode>>4);
			System.out.println("Wert: "+wert);
			
			switch(befehl(CMD)){
			case "NOP":  System.out.println("NOP wird ausgeführt");
						 programmCounter++;
						 break;
			case "LOAD": System.out.println("LOAD wird ausgeführt");
						 register[0]=wert;
						 registerAusgabe();
						 programmCounter++;
						 break;
			case "MOV":  System.out.println("MOV wird ausgeführt");
						 System.out.println(Integer.toBinaryString(opcode));
						 System.out.println((opcode>>12)&1);
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
						 registerAusgabe();
						 programmCounter++;
						 break;
			case "ADD":  System.out.println("Add wird ausgeführt");
						 register[indexRx]=register[indexRx]+register[indexRy];
						 System.out.println(register[indexRx]);
						 programmCounter++;
						 break;
			case "SUB":  System.out.println("SUB wird ausgeführt");
						 register[indexRx]=register[indexRx]-register[indexRy];
						 programmCounter++;
						 registerAusgabe();
						 break;
			case "MUL":  System.out.println("MUL wird ausgeführt");
						 register[indexRx]=register[indexRx]*register[indexRy];
						 programmCounter++;
						 break;
			case "DIV":  System.out.println("DIV wird ausgeführt");
						 register[indexRx]=register[indexRx]/register[indexRy];
						 programmCounter++;
						 break;
			case "PUSH": System.out.println("PUSH wird ausgeführt");
						 System.err.println("indexRx: "+indexRx+" wird auf stack geladen!");
						 stack.push(register[indexRx]);
						 programmCounter++;
						 break;
			case "POP":  if(!stack.isEmpty()){
							 System.out.println("POP wird ausgeführt");
							 register[indexRx]=stack.pop();
						 }
						 programmCounter++;
						 break;
			case "JMP": System.out.println("JMP wird ausgeführt");
						 programmCounter=wert;
						 break;
			case "JIZ":  
						 if(register[0]==0){
							 System.out.println("JIZ wird ausgeführt");
							 programmCounter=wert;
						 }else{
							 programmCounter++;
						 }
						 break;
			case "JIH":  System.out.println("JIH wird ausgeführt");
						 if(register[0]>0){
							programmCounter=wert;
						 }else{
							 programmCounter++;
						 }
						 break;
			case "JSR":  System.out.println("JSR wird ausgeführt");
						 subroutine.push(programmCounter+1);
						 programmCounter=wert;
						 
						 break;
			case "RTS":  System.out.println("RTS wird ausgeführt");
						 if(!subroutine.empty()){
							programmCounter=subroutine.pop();
						 }else{
							 registerAusgabe();
							 System.out.println("Summe: "+register[10]);
							 memoryAusgabe();
							 //writeFile(profiler);
							System.exit(0);
						 }
						 break;
						 
						 
			}
			timeAfter=System.currentTimeMillis();
			if(CMD!=0){
				profiler.setValue((timeAfter-timeBefore), (programmCounter-1));
			}
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
			System.out.println("Counter: "+programmCounter);
		}while(true);
	
		
	}
	
	private static void writeFile(Profiler profiler) {
		
		 FileWriter fw=null;
		try {
			fw = new FileWriter("Profile.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		 BufferedWriter bw = new BufferedWriter(fw);
		 
		 try {
			fw.write("Used Time:"+profiler.getValue(0));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private static void registerAusgabe() {
		
		for (int i = 0; i < register.length; i++) {
			System.out.println("Register "+i+"= "+ register[i]);
		}
		
	}
	
	private static void memoryAusgabe(){
		int i=1000;
		
		do{
			System.out.println("Memory an der stelle "+i+"="+memory[i]);
			i++;
		}while(memory[i]!=0);
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
	

	private static int befehlFuerAssembler(String befehl){
		switch(befehl){
		
			case "NOP": return (0b0000);
			
			case "LOAD": return (0b0001);
			
			case "MOV": return (0b0010);
			 		     
			case "ADD": return (0b0011);
						 
			case "SUB": return (0b0100);
		     			 
			case "MUL": return (0b0101);
						 
			case "DIV": return (0b0110);
			
			case "PUSH": return (0b0111);
						 
			case "POP": return (0b1000);
			 		     
			case "JMP": return (0b1001);
						 
			case "JIZ": return (0b1010);
		     			 
			case "JIH": return (0b1011);
						 
			case "JSR": return (0b1100);
						
			case "RTS": return (0b1101);			 
		
			default: return (0b0000);
		}
	}
	

	public static void assembler() throws IOException{
	
		BufferedReader br=null;
		
		br = new BufferedReader(new FileReader(new File("C:/Users/AsimB/workspace/MyVM/src/FibonacciFolge.txt")));
		
		//int i=0;
		String line=null;
		
		
		System.out.println("Assemblercode:");
		while((line=br.readLine())!=null){
			opcode=0;
			System.out.println(line);
			//i++;
			
			for (int i =line.split(" ").length-1; i>=0 ; i--) {
				
				if(i==0){
					opcode=(opcode<<4)+befehlFuerAssembler(line.split(" ")[i]);
				}else if(i==4){
					opcode=(opcode<<2)+Integer.parseInt(line.split(" ")[i]);
				}else if(i==3){
					opcode=(opcode<<1)+Integer.parseInt(line.split(" ")[i]);
				}else{
								
						if(line.split(" ")[i].startsWith("R")){
							opcode=(opcode<<4)+Integer.parseInt(line.split(" ")[i].replaceAll("R", ""));
						}else{
							opcode=(opcode<<4)+Integer.parseInt(line.split(" ")[i]);
						}	
						//System.out.println(opcode);
				}	
			}
			System.out.println(Integer.toBinaryString(opcode));
			
			
//			//System.out.println("opcode an Stelle "+programmCounterForMEM+"="+opcode);
//			if(line.split(" ").length==4){
//				if(!line.split(" ")[3].isEmpty()){
//					if(line.split(" ")[3].startsWith("R")){
//						line3=line.split(" ")[3].replaceAll("R", "");
//						opcode=Integer.parseInt(line3);
//						System.out.println("Line3"+line3);
//					}else{
//						line3=line.split(" ")[3];
//						opcode=Integer.parseInt(line3);
//						System.out.println("Line3"+line3);
//					}	
//					//System.out.println(opcode);
//				}
//			}
//			
//			if(line.split(" ").length>2){
//				
//				if(!line.split(" ")[2].isEmpty()){
//					if(line.split(" ")[2].startsWith("R")){
//						line2=line.split(" ")[2].replaceAll("R", "");
//						opcode=(opcode<<5)+Integer.parseInt(line2);
//					}else{
//						opcode=(opcode<<5)+Integer.parseInt(line.split(" ")[2]);
//					}	
//					//System.out.println(opcode);
//				}
//			}
//			
//			if(line.split(" ").length>1){
//				if(!line.split(" ")[1].isEmpty()){
//					if(line.split(" ")[1].startsWith("R")){
//						line1=line.split(" ")[1].replaceAll("R","");
//						opcode=(opcode<<4)+Integer.parseInt(line1);
//					}else{
//						opcode=(opcode<<4)+Integer.parseInt(line.split(" ")[1]);
//					}	
//					//System.out.println(opcode);
//				}
//			}
//			
//			
//			line0=line.split(" ")[0];
//		    System.out.println("BEFEHL: "+line0);
//			opcode=(opcode<<4)+befehlFuerAssembler(line0);
			System.out.println(opcode);
			memory[programmCounter]=opcode;
			//System.out.println("Speicher an der stelle "+programmCounter+" "+memory[programmCounter]);
			programmCounter++;
		}
		
		br.close();
	}
	

}
