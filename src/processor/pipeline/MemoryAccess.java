package processor.pipeline;

import processor.Processor;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	co_unit controlunit = new co_unit();
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable()) {
			System.out.println("MA:"+"\n");
			int op2 = EX_MA_Latch.getop2();
			int alures = EX_MA_Latch.getaluRes();
			int ldres=0;
			
			int instruction = EX_MA_Latch.getInstruction();
			controlunit.setInstruction(instruction);
			System.out.print("Instruction code:"+instruction+"\n");
			System.out.print("Opcode:"+controlunit.opcode+"\n");
			System.out.print("rs1:"+Integer.parseInt(controlunit.rs1,2)+"\n");
			System.out.print("rs2:"+Integer.parseInt(controlunit.rs2,2)+"\n");
			System.out.print("rd:"+Integer.parseInt(controlunit.rd,2)+"\n");
			MA_RW_Latch.setInstruction(instruction);
			
			if(containingProcessor.getcontrol_unit().isSt()){
				containingProcessor.getMainMemory().setWord( alures, op2);
			}
			else if (containingProcessor.getcontrol_unit().isLd()){
	
				ldres = containingProcessor.getMainMemory().getWord(alures);
				MA_RW_Latch.setldres(ldres);
			}
			else{
				MA_RW_Latch.setalures(alures);
			}
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setrd(EX_MA_Latch.getrd());
		

		}
		else {
			controlunit.opcode="";
			controlunit.rs1="";
			controlunit.rs2="";
			controlunit.rd="";
			controlunit.Imm = "";
		}
	
	}

}
