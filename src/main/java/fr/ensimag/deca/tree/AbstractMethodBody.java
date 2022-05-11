package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractMethodBody extends Tree {
    
    /**
     * Règle (3.11)
     */
    public abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
                                    EnvironmentExp envParams, ClassDefinition currentClass,
                                    Type returnType) throws ContextualError;

	protected abstract void codeGenbody(DecacCompiler compiler);
}
