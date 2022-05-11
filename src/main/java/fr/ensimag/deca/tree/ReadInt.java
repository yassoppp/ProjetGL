package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RINT;

import java.io.PrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class ReadInt extends AbstractReadExpr {

    /**
     * Règle (3.35) expr --> ReadInt
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = new IntType(compiler.getSymbolTable().create("int"));
        this.setType(type);
        return this.getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print("readInt()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    @Override
    protected DVal codePreGen(DecacCompiler compiler) {
    	// TODO Auto-generated method stub
    	GPRegister reg= compiler.getRegsManager().getReg();
        compiler.addInstruction(new RINT());
        compiler.addInstruction(new LOAD(Register.R1, reg));
        return reg;
    }

}
