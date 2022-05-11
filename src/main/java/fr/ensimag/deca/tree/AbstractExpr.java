package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Règle (3.28)
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        Type valueType = this.verifyExpr(compiler, localEnv, currentClass);
        if (!EnvironmentType.assignCompatible(expectedType, valueType)) {
            throw new ContextualError("Incompatible value of type " + valueType +
                    " for identifier of type "+ expectedType, getLocation());
        }
        
        if (expectedType.isFloat() && valueType.isInt()) {
            ConvFloat newValue = new ConvFloat(this);
            Type newType = newValue.verifyExpr(compiler, localEnv, currentClass);
            return newValue;
        } else {
            return this;
        }
    }
    
    /**
     * Règle (3.20) : inst --> expr
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.verifyExpr(compiler, localEnv, currentClass);
    }

    /**
     * Règle (3.29) : condition --> expr
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = this.verifyExpr(compiler, localEnv, currentClass);
        if (!type.isBoolean()) {
            throw new ContextualError("Incompatible result type for condition", getLocation());
        }
    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
    	DVal value=this.codePreGen(compiler);
        if(this.getType().isInt()) {
        	//FloatLiteral f=(FloatLiteral)this;
        	compiler.addInstruction(new LOAD(value, Register.R1));
        	compiler.addInstruction(new WINT());
        	return;
        }if(this.getType().isFloat()) {
        	//IntLiteral f=(IntLiteral)this;
        	compiler.addInstruction(new LOAD(value, Register.R1));
        	compiler.addInstruction(new WFLOAT());
        	return;
        }
    }
    public void codeGenPrintx(DecacCompiler compiler){
        DVal value = codePreGen(compiler);
        compiler.addInstruction(new LOAD(value, Register.R1));
        compiler.addInstruction( new WFLOATX());
        compiler.getRegsManager().freeReg(value);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }
    

    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }

	protected abstract DVal codePreGen(DecacCompiler compiler);

	protected void codeGenInst(DecacCompiler compiler, Label else1) {
		
	}


}
