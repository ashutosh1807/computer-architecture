package processor.pipeline;

import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	co_unit controlunit = new co_unit();
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
	
	public boolean r2i1(String op) {
		if(op.equals("")||op.length()>5) return false;
		if(op.charAt(4)=='1'  && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10111"))
			return true;
		else return false;
		
	}
	
	public boolean r2i2(String op) {//load or store
		if(op.equals("")||op.length()>5) return false;
		if((op.equals("10110") || op.equals("10111")))
			return true;
		else return false;
		
	}
	
	public boolean r2i3(String op) { // branch
		if(op.equals("")||op.length()>5) return false;
		if((op.charAt(0)=='1' && op.charAt(1)=='1')&&!op.equals("11000")&&!op.equals("11100")  )
			return true;
		else return false;
		
	}
	
	public boolean ri(String op) {
		if(op.equals("")||op.length()>5) return false;
		if(op.equals("11101") || op.equals("11000"))
			return true;
		else return false;
		
	}
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public int convertbin( String s) {
		int n = s.length();
		int i = Integer.parseInt(s,2);
		if (s.charAt(0) == '1') {
			i = i - (int)Math.pow(2, n);

			
			return i;
		}
		else return i;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable())
		{
			//TODO
			

			int instruction = IF_OF_Latch.getInstruction();
			controlunit.setInstruction(instruction);
			String instructionString = Integer.toBinaryString(instruction);
			
			int n = instructionString.length();
			String todo="";
			for (int i=0;i<32-n;i++){
				todo = todo + "0" ;
			}
			instructionString = todo + instructionString;
			System.out.println("OF:"+"\n");
			System.out.print("Instruction code:"+instructionString+"\n");
			System.out.print("Opcode:"+controlunit.opcode+"\n");
			System.out.print("rs1:"+Integer.parseInt(controlunit.rs1,2)+"\n");
			System.out.print("rs2:"+Integer.parseInt(controlunit.rs2,2)+"\n");
			System.out.print("rd:"+Integer.parseInt(controlunit.rd,2)+"\n");
			String opcode="",rs1="",rs2="",rd="",immx = "";
			rs1 = instructionString.substring(5,10);
			rs2 = instructionString.substring(10,15);
			opcode=instructionString.substring(0,5);
			if(r3(opcode)) {
				rd=instructionString.substring(15,20);
			}
			if(r2i(opcode)) {
				rd=instructionString.substring(10,15);
				immx=instructionString.substring(15,32);
			}
			if(ri(opcode)) {
				rd=instructionString.substring(5,10);
				immx=instructionString.substring(10,32);
			}
		
			String rp1 ;
			String rp2 ;
			if(!(opcode.equals("10111"))){ // if store
				rp1 = rs1;
				rp2 = rs2;
			}
			else{
				rp1 = rd;
				rp2 = rs1;
			}
			int operand1 = containingProcessor.getRegisterFile().getValue( Integer.parseInt(rp1,2) );
			int operand2 = containingProcessor.getRegisterFile().getValue( Integer.parseInt(rp2,2) );
			
			
			boolean conflict = false;
			if(r3(opcode)) {
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
					if(rs1.equals("11111")){
						conflict = true;
					}
					if(rs2.equals("11111")){
						conflict = true;
					}
				}
				if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}
					if(rs2.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict = true;
					}System.out.println("*********EX1***********");
				}
				if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}
					if(rs2.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
				if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}
					if(rs2.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********RW***********");
				}
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}
					if(rs2.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********EX2***********");
				}
				if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}
					if(rs2.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
			}
			
			if(r2i1(opcode)) {
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
					if(rs1.equals("11111")){
						conflict = true;
					}
				}
				System.out.println("PPPPPPPPP"+containingProcessor.getEXUnit().controlunit.opcode);
				if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
						System.out.println("BEHEN KA LODA");;
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********EX3***********");
				}
				if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
				if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********RW***********");
				}
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
					System.out.println("^^^^^^^^^^^^^"+rs1);
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
						System.out.println("^^^^^^^^^^^^^"+rs1);
						System.out.println("^^^^^^^^^^^^^^^^^^^^^"+containingProcessor.getEXUnit().controlunit.rd);
					}System.out.println("*********EX4***********");
				}
				if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
			}
			
			if(r2i3(opcode)) {
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
					if(rs1.equals("11111")){
						conflict = true;
					}
					if(rd.equals("11111")){
						conflict = true;
					}
				}
				if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict = true;
					}System.out.println("*********EX5***********");
				}
				if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
				if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}
					 if(rd.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********RW***********");
				}
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********EX6***********");
				}
				if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
			}
			
			if(r2i2(opcode) && opcode.equals("10110") ) {
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
					if(rs1.equals("11111")){
						conflict = true;
					}
				}
				if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********EX7***********");
				}
				if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
				if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********RW***********");
				}
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********EX8***********");
				}
				if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("10111")) {
					if((Integer.parseInt(rs1,2) + convertbin(immx) )== 
									Integer.parseInt((containingProcessor.getEXUnit().controlunit.rd),2) +
									convertbin(containingProcessor.getEXUnit().controlunit.Imm) ) {
						conflict = true;
					}System.out.println("*********EX9***********");
				}
				if(containingProcessor.getMAUnit().controlunit.opcode.equals("10111")) {
					if((Integer.parseInt(rs1,2) + convertbin(immx) )== 
									Integer.parseInt((containingProcessor.getMAUnit().controlunit.rd),2) +
									convertbin(containingProcessor.getMAUnit().controlunit.Imm) ) {
						conflict = true;
					}System.out.println("*********MA***********");
				}
							
			}
			
			if(r2i2(opcode) && opcode.equals("10111")) {
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
					if(rs1.equals("11111")){
						conflict = true;
					}
					if(rd.equals("11111")){
						conflict = true;
					}
				}
				if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict = true;
					}System.out.println("*********EX10***********");
				}
				if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
				if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
					if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getRWUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********RW***********");
				}
				if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********EX11***********");
				}
				if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
					if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}
					if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
						conflict = true;
					}System.out.println("*********MA***********");
				}
			}
			//System.out.println("MAA ka loda"+conflict);
			if(!conflict) {
				OF_EX_Latch.setInstruction(instruction);
				System.out.println("MAA ka loda      instruction set");
				OF_EX_Latch.setimmx(convertbin(immx));
				OF_EX_Latch.setbranchtarget(convertbin(immx) + containingProcessor.getRegisterFile().getProgramCounter()-1);
				containingProcessor.getcontrol_unit().setopcode(instructionString.substring(0,5));
				OF_EX_Latch.setoperand1(operand1);
				OF_EX_Latch.setoperand2(operand2);
				OF_EX_Latch.setrd(Integer.parseInt(rd,2));
				containingProcessor.getIFUnit().conflict = true ;
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);
			}
			else {
				containingProcessor.getIFUnit().conflict = false ;
			}
			/*System.out.print("Branch target:"+Integer.toString(convertbin(immx)+ containingProcessor.getRegisterFile().getProgramCounter()-1));
			System.out.println("reg1:"+rs1);
			System.out.println("reg1:"+rs1);
			System.out.println("rdstring = "+ rd +"\n");
			System.out.print("op1:"+Integer.toString(operand1)+"\n");
			System.out.print("immx:"+Integer.toString(Integer.parseInt(immx,2))+"\n");

			System.out.print("op2:"+Integer.toString(operand2)+"\n");
			System.out.print("rd:"+Integer.toString(Integer.parseInt(rd,2))+"\n");*/


		}
		
	}

}
