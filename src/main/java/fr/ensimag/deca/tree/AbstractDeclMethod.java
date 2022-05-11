package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import java.io.PrintStream;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractDeclMethod extends Tree {
    
    public abstract void verifyDeclMethod(DecacCompiler compiler, AbstractIdentifier superClass, 
                            AbstractIdentifier currentClass)
            throws ContextualError;
    
    public abstract AbstractIdentifier getReturnType();

    public abstract ListDeclParam getParams();

    public abstract AbstractMethodBody getMethodBody();

	public abstract AbstractIdentifier getMethodName();

	protected abstract void codeGenMethodCode(DecacCompiler compiler);

}
