package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.NullType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

public class Null extends AbstractExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
        this.setType(new NullType(compiler.getSymbolTable().create("null")));
        return this.getType(); 
    }

    @Override
    protected DVal codePreGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("null");
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
