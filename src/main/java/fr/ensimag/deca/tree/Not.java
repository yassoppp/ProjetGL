package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class Not extends AbstractUnaryExpr {
	
	private static int i=0;
    private Label Fin;
    private Label F;
    
    protected void labelsatate() {
    	i++;
    	this.F=new Label("not"+i);
    	this.Fin=new Label("true"+i);
    }
    
    public Not(AbstractExpr operand) {
        super(operand);
    }
    
    /**
     * Règle (3.37) : expr --> op_un
     * type := type_unary_op(op, type1)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError("Unary operator '!' requires boolean", getLocation());
        }
        this.setType(type);
        return this.getType();
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
    @Override
    public DVal codePreGen(DecacCompiler compiler) {
    	labelsatate();
        DVal value = getOperand().codePreGen(compiler);
        GPRegister reg;
        if(!(value instanceof GPRegister)){
        	reg = compiler.getRegsManager().getReg();
            compiler.addInstruction(new LOAD(value, reg));
        }else{
        	reg = (GPRegister) value;
        }
        compiler.addInstruction(new CMP(1, reg));
        compiler.addInstruction(new BNE(F));
        compiler.addInstruction(new LOAD(0, reg));
        compiler.addInstruction(new BRA(Fin));
        compiler.addLabel(F);
        compiler.addInstruction(new LOAD(1, reg));
        compiler.addLabel(Fin);
        return reg;
        
        
        
        
        
    }
}
