package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    } 
    @Override
    protected void codeGenInst(DecacCompiler compiler,Label l) {
    	DVal left=this.getLeftOperand().codePreGen(compiler);
    	DVal right=this.getRightOperand().codePreGen(compiler);
    	GPRegister reg;
    	if(!(left instanceof GPRegister)){
    		reg=compiler.getRegsManager().getReg();
    		compiler.addInstruction(new LOAD(left, reg));
    		compiler.getRegsManager().freeReg(reg);
    	}else {
    		reg=(GPRegister)left;
    	}
        
    	compiler.addInstruction(new CMP((DVal)right,reg));
        compiler.addInstruction(new BNE(l));
         
    }
    @Override
	protected DVal codePreGen(DecacCompiler compiler) {
		DVal left=this.getLeftOperand().codePreGen(compiler);
		DVal right=this.getRightOperand().codePreGen(compiler);
		GPRegister reg;
    	if(!(left instanceof GPRegister)){
    		reg=compiler.getRegsManager().getReg();
    		compiler.addInstruction(new LOAD(left, reg));
    	}else {
    		reg=(GPRegister)left;
    	}
    	compiler.addInstruction(new CMP((DVal)right,reg));
        compiler.addInstruction(new SEQ(reg));
        return reg;
	}
   
    
}
