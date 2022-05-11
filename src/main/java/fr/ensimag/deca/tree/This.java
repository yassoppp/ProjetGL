package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import org.mockito.internal.creation.SuspendMethod;

public class This extends AbstractExpr{

    /**
     * Règle (3.43)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
        if (currentClass == null) {
            throw new ContextualError("this can only be used inside a class", getLocation());
        }
        Type type = currentClass.getType();
        this.setType(type);
        return type;
    }

    @Override
    protected DVal codePreGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("this");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO Auto-generated method stub
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO Auto-generated method stub
    }
    

}
