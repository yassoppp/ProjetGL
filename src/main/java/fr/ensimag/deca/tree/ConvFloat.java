package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl59
 * @date 01/01/2022
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) {
        this.getOperand().setType(new IntType(compiler.getSymbolTable().create("int")));
        this.setType(new FloatType(compiler.getSymbolTable().create("float")));
        return this.getType();
    }
    @Override
    public void decompile(IndentPrintStream s) {
        this.getOperand().decompile(s);
    }
    
    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }
    @Override
    	protected DVal codePreGen(DecacCompiler compiler) {
    		// TODO Auto-generated method stub
    		DVal dval=this.getOperand().codePreGen(compiler);
    		GPRegister tmp =compiler.getRegsManager().getReg();
    		compiler.addInstruction(new FLOAT(dval, tmp));
    		return tmp;
    	}

}
