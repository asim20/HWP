import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Assembler {
	
	private int befehlFuerAssembler(String befehl){
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
	
	public void assembler(int[] memory) throws IOException{
		
		BufferedReader br=null;
		
		br = new BufferedReader(new FileReader(new File("C:/Users/AsimB/workspace/MyVM/src/FibonacciFolge.txt")));
		
		//int i=0;
		String line=null;
		int opcode;
		int programmCounter=0;
		
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
			//System.out.println(Integer.toBinaryString(opcode));
			//System.out.println(opcode);
			memory[programmCounter]=opcode;
			//System.out.println("Speicher an der stelle "+programmCounter+" "+memory[programmCounter]);
			programmCounter++;
		}
		
		br.close();
	}
	
}
