package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Return extends AbstractInst {
    private AbstractExpr expression;

    public Return(AbstractExpr expression) {
        super();
        this.expression = expression;
    }
    
    /**
     * Règle (3.24)
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType) throws ContextualError {
        if (returnType.isVoid()) {
            throw new ContextualError("Return type cannot be void", getLocation());
        }
        AbstractExpr expr = this.expression.verifyRValue(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // TODO Auto-generated method stub
    	GPRegister davl=(GPRegister)(expression.codePreGen(compiler));
    	compiler.addInstruction(new LOAD(davl, Register.R0));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("return ");
        expression.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.expression.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
    }

}
