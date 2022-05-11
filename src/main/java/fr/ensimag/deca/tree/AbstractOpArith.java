package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructions.QUO;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * type_arith_op : Type × Type → Type
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type returnType = null;
        Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (leftType.isFloat() && rightType.isInt()) {
            ConvFloat newValue = new ConvFloat(this.getRightOperand());
            Type newType = newValue.verifyExpr(compiler, localEnv, currentClass);
            this.setRightOperand(newValue);
            returnType =  leftType;
        } else if (leftType.isInt() && rightType.isFloat()) {
            ConvFloat newValue = new ConvFloat(this.getLeftOperand());
            Type newType = newValue.verifyExpr(compiler, localEnv, currentClass);
            this.setLeftOperand(newValue);
            returnType = rightType;
        } else {
            returnType =  leftType;
        }
        this.setType(returnType);
        return returnType;
    }
    @Override
    public DVal codePreGen(DecacCompiler compiler){
        DVal leftVal = getLeftOperand().codePreGen(compiler);
        DVal rightVal = getRightOperand().codePreGen(compiler);
        GPRegister tmp;
        if(!(leftVal instanceof GPRegister )) {
        	tmp = compiler.getRegsManager().getReg();
        	compiler.getRegsManager().pushregister(tmp);
        	compiler.addInstruction(new LOAD(leftVal,tmp));
        }else {
        tmp=(GPRegister)leftVal;
        }
        if(this.getOperatorName().equals("+")) {
        	compiler.addInstruction(new ADD(rightVal,tmp));
        	
        }if(this.getOperatorName().equals("-")) {
        	compiler.addInstruction(new SUB(rightVal,tmp));
        	
        }if(this.getOperatorName().equals("*")) {
        	compiler.addInstruction(new MUL(rightVal,tmp));
        }if(this.getOperatorName().equals("/")) {
        	if (getType().isInt()) {
            compiler.addInstruction(new QUO(rightVal, tmp));
            } else {
            compiler.addInstruction(new DIV(rightVal, tmp));
            }
        }
        compiler.getRegsManager().freeReg(rightVal);
        return(tmp); 
    }
}
