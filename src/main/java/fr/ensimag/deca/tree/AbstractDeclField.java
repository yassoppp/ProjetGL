package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import java.io.PrintStream;

import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractDeclField extends Tree {

    public abstract void verifyDeclField(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass)
            throws ContextualError;
    
    public abstract void verifyFieldInitialisation(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass) 
            throws ContextualError;
    
    public abstract Type getType();

	protected abstract void codeGenField(DecacCompiler compiler);
}
