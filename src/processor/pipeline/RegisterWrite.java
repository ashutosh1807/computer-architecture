package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	co_unit controlunit = new co_unit();
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW()
	{
		if(MA_RW_Latch.isRW_enable())
		{
			System.out.println("RW:"+"\n");
			int instruction = MA_RW_Latch.getInstruction();
			controlunit.setInstruction(instruction);
			System.out.print("Instruction code:"+instruction+"\n");
			System.out.print("Opcode:"+controlunit.opcode+"\n");
			System.out.print("rs1:"+Integer.parseInt(controlunit.rs1,2)+"\n");
			System.out.print("rs2:"+Integer.parseInt(controlunit.rs2,2)+"\n");
			System.out.print("rd:"+Integer.parseInt(controlunit.rd,2)+"\n");

			//TODO
			// if instruction being processed is an end instruction, remember to call Simulator.setSimulationComplete(true);
			int result;
			if (containingProcessor.getcontrol_unit().isLd()){
				result = MA_RW_Latch.getldres();
			}
			else{
				result = MA_RW_Latch.getalures();
			}
			int rd = MA_RW_Latch.getrd();
			System.out.println("rd = "+ rd+"\n");
			System.out.println("result = "+ result+"\n");

			if(containingProcessor.getcontrol_unit().isWb()){
				containingProcessor.getRegisterFile().setValue(rd, result);
			}
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
			
			if(containingProcessor.getcontrol_unit().isend()){
				Simulator.setSimulationComplete(true);
				IF_EnableLatch.setIF_enable(false);
			}

		}
		else {
			IF_EnableLatch.setIF_enable(true);
			controlunit.opcode="111111";
			controlunit.rs1="111111";
			controlunit.rs2="111111";
			controlunit.rd="111111";
			controlunit.Imm = "111111111111111111111111";
		}
	}

}
