package processor.pipeline;

import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class InstructionFetch {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	boolean conflict = true;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void performIF()
	{
			if(IF_EnableLatch.isIF_enable())
			{
				System.out.println("IF:"+"\n");
				if(EX_IF_Latch.IF_enable){
					if(EX_IF_Latch.isBranchTaken){
						containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.branchTarget);
					}
					EX_IF_Latch.IF_enable=false;
				}
			
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				System.out.println("Clock:"+Long.toString(Clock.getCurrentTime()));
				int newInstruction = containingProcessor.getMainMemory().getWord(currentPC);
				
				String instructionString = Integer.toBinaryString(newInstruction);
				int n = instructionString.length();
				String todo="" ;
				for (int i=0;i<32-n;i++){
					todo = todo + "0" ;
				}
				instructionString = todo + instructionString;
				if(!(instructionString.substring(0,5).equals("11101")) ){
					if(conflict) {	// checking conflicts
						IF_OF_Latch.setInstruction(newInstruction);
						containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
					}
					//IF_EnableLatch.setIF_enable(false);
					IF_OF_Latch.setOF_enable(true);
				}
				else {
					IF_EnableLatch.setIF_enable(false);
					Simulator.setSimulationComplete(true);
				}
			}
	}

}
