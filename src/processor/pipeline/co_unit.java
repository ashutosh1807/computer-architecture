package processor.pipeline;

public class co_unit {
	int instruction;
	String opcode="",rs1="",rs2="",rd="",Imm = "";
	public boolean r3(String op) {
		if(op.equals("")||op.length()>5) return false;
		if(op.charAt(4)=='0' && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10110"))
			return true;
		else return false;
		
	}
	public boolean r2i(String op) {
		if(op.equals("")||op.length()>5) return false;
		if( ((op.charAt(4)=='1') || op.equals("10110") || op.equals("11010") ||op.equals("11100") ) && !op.equals("11101"))
			return true;
		else return false;
		
	}
	
	public boolean ri(String op) {
		if(op.equals("")||op.length()>5) return false;
		if(op.equals("11101") || op.equals("11000"))
			return true;
		else return false;
		
	}
	public void setInstruction(int instruction) {
		this.instruction = instruction;
		String instructionString = Integer.toBinaryString(instruction);
		int n = instructionString.length();
		String todo="" ;
		for (int i=0;i<32-n;i++){
			todo = todo + "0" ;
		}
		instructionString = todo + instructionString;
		rs1 = instructionString.substring(5,10);
		rs2 = instructionString.substring(10,15);
		opcode=instructionString.substring(0,5);
		if(r3(opcode)) {
			rd=instructionString.substring(15,20);
		}
		if(r2i(opcode)) {
			rd=instructionString.substring(10,15);
			Imm=instructionString.substring(15,32);
		}
		if(ri(opcode)) {
			rd=instructionString.substring(5,10);
			Imm=instructionString.substring(10,32);
		}
	} 

}
