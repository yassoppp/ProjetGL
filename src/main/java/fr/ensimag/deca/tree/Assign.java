package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl59
 * @date 01/01/2022
 */
public class Assign extends AbstractBinaryExpr {

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * Règle (3.32) : expr --> Assign
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        AbstractExpr newExpr = this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, leftType);
        this.setRightOperand(newExpr);
        this.setType(leftType);
        return leftType;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }
    @Override
    public void codeGenInst(DecacCompiler compiler) {
    	DVal dval= getRightOperand().codePreGen(compiler);
    	GPRegister tmp;
    	if(! (dval instanceof GPRegister)) {
    	tmp = compiler.getRegsManager().getReg();
    	compiler.getRegsManager().pushregister(tmp);
        compiler.addInstruction(new LOAD(dval, tmp));
    	}//tmp=(GPRegister)dval;
    	else {
    	tmp=(GPRegister)dval;
    	}
    	if(this.getLeftOperand() instanceof Identifier) {
    	Identifier id=(Identifier)getLeftOperand();
    	if(id.getDefinition().isField()) {
    		int index= id.getFieldDefinition().getIndex();
    		GPRegister reg=compiler.getRegsManager().getReg();
    		compiler.getRegsManager().pushregister(reg);
    		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), reg));
    		compiler.addInstruction(new STORE((GPRegister)dval, new RegisterOffset(index+1,reg)));
    		compiler.getRegsManager().freeReg(dval);
    		compiler.getRegsManager().freeReg(reg);
    	}
    	else {
        Identifier var = (Identifier) getLeftOperand();
        compiler.addInstruction(new STORE(tmp, var.getVariableDefinition().getOperand()));
        compiler.getRegsManager().freeReg(tmp);
    	}}
    	else if(this.getLeftOperand() instanceof Selection) {
    	  ((Selection)getLeftOperand()).codeGenInst(compiler,tmp);
    	}
    }
    @Override
    public DVal codePreGen(DecacCompiler compiler) {
        return getRightOperand().codePreGen(compiler);
    }

}
