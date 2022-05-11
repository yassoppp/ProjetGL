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
public class DeclParam extends AbstractDeclParam {
    
    AbstractIdentifier type;
    AbstractIdentifier paramName;

    public DeclParam(AbstractIdentifier type, AbstractIdentifier paramName) {
        this.type = type;
        this.paramName = paramName;
    }
    
    public Type getType() {
        return this.type.getType();
    }
    
    public AbstractIdentifier getName() {
        return this.paramName;
    }
    
    /**
     * Règle (2.9)
     */
    @Override
    public Type verifyDeclParam(DecacCompiler compiler) throws ContextualError {
        Type actualType = type.verifyType(compiler);
        if (actualType.isVoid()) {
            throw new ContextualError("Type of parameter "+ paramName.getName()+
                        " cannot be void", getLocation());
        }
        
        this.type.setType(actualType);
        return this.type.getType();
    }
    
    /**
     * Règle (3.13)
     */
    @Override
    public ParamDefinition verifyParamName(DecacCompiler compiler) {
        Type type = this.getType();
        ParamDefinition paramDef = new ParamDefinition(type, getLocation());
        this.paramName.setDefinition(paramDef);
        return paramDef;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        this.type.decompile(s);
        s.print(" ");
        this.paramName.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        paramName.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        paramName.iter(f);
    }

}
