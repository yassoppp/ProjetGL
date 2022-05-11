package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractDeclParam extends Tree {
    
    public abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;
    
    public abstract ParamDefinition verifyParamName(DecacCompiler compiler);
    
    public abstract AbstractIdentifier getName();

}
