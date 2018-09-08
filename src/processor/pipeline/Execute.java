package processor.pipeline;

import processor.Processor;

public class Execute {
	Processor containingProcessor;
	OF_EX_LatchType OF_EX_Latch;
	EX_MA_LatchType EX_MA_Latch;
	EX_IF_LatchType EX_IF_Latch;
	ALU alu=new ALU();
	co_unit controlunit = new co_unit();
	public Execute(Processor containingProcessor, OF_EX_LatchType oF_EX_Latch, EX_MA_LatchType eX_MA_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.OF_EX_Latch = oF_EX_Latch;
		this.EX_MA_Latch = eX_MA_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performEX()
	{
		if(OF_EX_Latch.isEX_enable()){
			System.out.println("EX:"+"\n");
			int isbranchtaken =0,branchPC=0;
			
			int instruction = OF_EX_Latch.getInstruction();
			controlunit.setInstruction(instruction);
			System.out.print("Instruction code:"+instruction+"\n");
			System.out.print("Opcode:"+controlunit.opcode+"\n");
			System.out.print("rs1:"+Integer.parseInt(controlunit.rs1,2)+"\n");
			System.out.print("rs2:"+Integer.parseInt(controlunit.rs2,2)+"\n");
			System.out.print("rd:"+Integer.parseInt(controlunit.rd,2)+"\n");
			EX_MA_Latch.setInstruction(instruction);
			
			int op1 = OF_EX_Latch.getoperand1() ;
			int op2 =  OF_EX_Latch.getoperand2();

			String opcode=containingProcessor.getcontrol_unit().opcode;
			int imm =	OF_EX_Latch.getimmx();
			System.out.println("OPCODE:"+opcode+"\n");

			int alures=0;
			if(containingProcessor.getcontrol_unit().isimm() || opcode.equals("11101")){
				alu.setop1(op1);
				alu.setop2(imm);
				alures=alu.eval(opcode);
				System.out.println("alures:"+alures+"\n");
				System.out.println("imm:"+imm+"\n");
				if(opcode.equals("00111")) {containingProcessor.getRegisterFile().setValue(31, op1%imm);}
				EX_MA_Latch.setop2(op2);
				EX_MA_Latch.setaluRes(alures);
				EX_MA_Latch.setMA_enable(true);
				
				
			}
			else if(!opcode.equals("11000") && !opcode.equals("11001") && !opcode.equals("11010") && !opcode.equals("11011") && !opcode.equals("11100") && !opcode.equals("11101")){
				alu.setop1(op1);
				alu.setop2(op2);
				alures=alu.eval(opcode);
				System.out.println("alures:"+alures+"\n");
				if(opcode.equals("00110")) containingProcessor.getRegisterFile().setValue(31, op1%op2);
				EX_MA_Latch.setaluRes(alures);
				EX_MA_Latch.setMA_enable(true);

			}
			else{
				
				switch(containingProcessor.getcontrol_unit().getopcode()){
					case "11000":{
						isbranchtaken= 1;
						branchPC = OF_EX_Latch.getbranchtarget();
						break;
					}
					case "11001":{
						
						if(op1 == op2){
							
							isbranchtaken=1;
							branchPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case "11010":{
						if(op1 != op2){
							isbranchtaken= 1;
							branchPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case "11011":{
						if(op1 <op2){
							isbranchtaken= 1;
							branchPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
					case "11100":{
						if(op1>op2){
							isbranchtaken= 1;
							branchPC = OF_EX_Latch.getbranchtarget();
						}
						break;
					}
				}
				//default: Misc.printErrorAndExit("unknown instruction!!");
				
				System.out.println("isbrannchtaken:"+isbranchtaken+"\n");
				System.out.println("branch target:"+branchPC+"\n");

				//IF_OF_Latch.setOF_enable(false);
				
			}
			if(isbranchtaken==1) {
				EX_IF_Latch.setisbranchtaken();
				EX_IF_Latch.setbranchtarget(branchPC);
				EX_IF_Latch.setIF_enable(true);
			}
			else {
				OF_EX_Latch.setEX_enable(false);
				EX_MA_Latch.setrd(OF_EX_Latch.getrd());
			}

		}
		else {
			controlunit.opcode="111111";
			controlunit.rs1="111111";
			controlunit.rs2="111111";
			controlunit.rd="111111";
			controlunit.Imm = "111111111111111111111111111111111111";
		}
		
	}
}