package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
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
public class DeclVar extends AbstractDeclVar {

    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    /**
     * Règle (3.17) : decl_var --> DeclVar
     */
    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        Symbol symbol = this.varName.getName();
        Type actualType = this.type.verifyType(compiler);
        if (actualType.isVoid()) {
            throw new ContextualError("Type of identifier " + symbol + " cannot be void", getLocation());
        }
        
        this.initialization.verifyInitialization(compiler, actualType, localEnv, currentClass);

        VariableDefinition def = new VariableDefinition(actualType, getLocation());
        
        try {
            localEnv.declare(symbol, def);
        }
        catch (DoubleDefException e) {
            throw new ContextualError("Double definition of identifier " + symbol, getLocation());
        }
        
        this.varName.verifyExpr(compiler, localEnv, currentClass);
    }

   
    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.varName.decompile(s);
        this.initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

	@Override
	protected void codeGenDeclVar(DecacCompiler compiler, RegisterOffset addr) {
		    if (this.initialization instanceof NoInitialization) {
		    this.varName.getVariableDefinition().setOperand(addr);
		    GPRegister reg= compiler.getRegsManager().getReg();
            if (this.type.getName().toString().equals("int")) {
                compiler.addInstruction(new LOAD(0, reg));
                compiler.addInstruction(new STORE(reg, varName.getVariableDefinition().getOperand()));
            } else if (this.type.getName().toString().equals("float")) {
            	//compiler.addInstruction(new FLOAT(new ImmediateFloat(0), Register.R0));
                compiler.addInstruction(new LOAD((float) 0, reg));
                compiler.addInstruction(new STORE(reg, varName.getVariableDefinition().getOperand()));
            } else if (this.type.getName().toString().equals("boolean")) {
                compiler.addInstruction(new LOAD(0, reg));
                compiler.addInstruction(new STORE(reg, varName.getVariableDefinition().getOperand()));
            } compiler.getRegsManager().freeReg(reg);}
            else {
			this.varName.getVariableDefinition().setOperand(addr);
	        initialization.codeGenInitialization(compiler,varName.getVariableDefinition());
            }	
	}

	@Override
	protected void codeGenDeclVarLocale(DecacCompiler compiler, RegisterOffset addr) {
		// TODO Auto-generated method stub
		if (this.initialization instanceof NoInitialization) {
		    this.varName.getVariableDefinition().setOperand(addr);
		    GPRegister reg= compiler.getRegsManager().getReg();
            if (this.type.getName().toString().equals("int")) {
                compiler.addInstruction(new LOAD(0, reg));
                compiler.addInstruction(new STORE(reg, varName.getVariableDefinition().getOperand()));
            } else if (this.type.getName().toString().equals("float")) {
            	//compiler.addInstruction(new FLOAT(new ImmediateFloat(0), Register.R0));
                compiler.addInstruction(new LOAD((float) 0, reg));
                compiler.addInstruction(new STORE(reg, varName.getVariableDefinition().getOperand()));
            } else if (this.type.getName().toString().equals("boolean")) {
                compiler.addInstruction(new LOAD(0, reg));
                compiler.addInstruction(new STORE(reg, varName.getVariableDefinition().getOperand()));
            } compiler.getRegsManager().freeReg(reg);}
			else {
			this.varName.getVariableDefinition().setOperand(addr);
	        initialization.codeGenInitialization(compiler,varName.getVariableDefinition());
            }	
            
	}
}
