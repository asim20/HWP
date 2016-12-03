import java.io.IOException;

public class HWP {
	
	public static void main(String[] args) {
		
		int[] memory=new int[4096];
		Assembler assembler=new Assembler();
		VM vm=new VM();
		
		try {
			assembler.assembler(memory);
			vm.VirtualMachine(memory);
		} catch (IOException e) {
			e.printStackTrace();
		}	
			
	}
	
}
