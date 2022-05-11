package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * @author gl59
 * @date 01/01/2022
 */
public class Initialization extends AbstractInitialization {

    public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }
    
    /**
     * Règle (3.8)
     */
    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Type valueType = this.getExpression().verifyExpr(compiler, localEnv, currentClass);
        if (!EnvironmentType.assignCompatible(t, valueType)) {
            throw new ContextualError("Incompatible value of type " + valueType +
                    " for identifier of type "+t, getLocation());
        }
        if (t.isFloat() && valueType.isInt()) {
            ConvFloat newValue = new ConvFloat(this.getExpression());
            Type newType = newValue.verifyExpr(compiler, localEnv, currentClass);
            this.setExpression(newValue);
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        this.getExpression().decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }
    @Override
    protected void codeGenInitialization(DecacCompiler compiler, VariableDefinition varDefinition) {
    	if(expression instanceof New) {
    		GPRegister reg= ((New)expression).codeGenNew(compiler);
    		compiler.addInstruction(new STORE(reg, varDefinition.getOperand()));
    	}else {
    	DVal value;
    	value =expression.codePreGen(compiler);
    	
        GPRegister reg;
        reg = compiler.getRegsManager().getReg();
        compiler.addInstruction(new LOAD(value,reg));
        compiler.getRegsManager().freeReg(value);
        compiler.addInstruction(new STORE(reg, varDefinition.getOperand()));
        compiler.getRegsManager().freeReg(reg);
    	}
    }

	@Override
	protected void codeGeninitializationField(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		if(this.expression instanceof IntLiteral) {
			IntLiteral exp=(IntLiteral)expression;
			compiler.addInstruction(new LOAD(exp.getValue(), Register.R0));
		}if(this.expression instanceof FloatLiteral) {
			FloatLiteral exp=(FloatLiteral)expression;
			compiler.addInstruction(new LOAD(exp.getValue(), Register.R0));
		}if(this.expression instanceof BooleanLiteral) {
			BooleanLiteral exp=(BooleanLiteral)expression;
			compiler.addInstruction(new LOAD(exp.codePreGen(compiler), Register.R0));
		}else {
			DVal dval=expression.codePreGen(compiler);
			compiler.addInstruction(new LOAD(dval, Register.R0));
			//free all
		}
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), (GPRegister) Register.R1));
        
        //compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index, getR(1))));
	}
}
