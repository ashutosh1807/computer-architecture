package processor.pipeline;


public class ALU{
	int operand1;
	int operand2;

	public void setop1(int o1){
		operand1=o1;
	}
	public void setop2(int o2){
		operand2=o2;
	}
	public int eval(String opcode){
		int res=0;
		
		switch(opcode) {
			case "00000":{res=operand1+operand2;break;}
			case "00001":{res=operand1+operand2;break;}
			case "00010":{res=operand1-operand2;break;}
			case "00011":{res=operand1-operand2;break;}
			case "00100":{res=operand1*operand2;break;}
			case "00101":{res=operand1*operand2;break;}
			case "00110":{res=operand1/operand2;break;}
			case "00111":{res=operand1/operand2;break;}
			case "01000":{res=operand1 & operand2;break;}
			case "01001":{res=operand1 & operand2;break;}
			case "01010":{res=operand1 | operand2;break;}
			case "01011":{res=operand1 |operand2;break;}
			case "01100":{res=operand1 ^ operand2;break;}
			case "01101":{res=operand1 ^ operand2;break;}
			case "01110":{
				if(operand1<operand2){ res = 1;}
				else{res = 0;}
				break;
			}
			case "01111":{
				if(operand1<operand2){ res = 1;}
				else{res = 0;}
				break;
			}
			case "10000":{res=operand1 << operand2;break;}
			case "10001":{res=operand1 << operand2;break;}
			case "10010":{res=operand1 >>> operand2;break;}
			case "10011":{res=operand1 >>>operand2;break;}
			case "10100":{res=operand1 >> operand2;break;}
			case "10101":{res=operand1 >> operand2;break;}
			case "10110":{res=operand1 + operand2;break;}
			case "10111":{res=operand1 + operand2;break;}
		
		
		}
		return res;

	}
}
