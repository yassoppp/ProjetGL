package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ParamDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class ListDeclParam extends TreeList<AbstractDeclParam> {
    
    /**
     * Règle (2.8)
     */
    public Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature methodSignature = new Signature();
        for (AbstractDeclParam param : this.getList()) {
            Type type = param.verifyDeclParam(compiler);
            methodSignature.add(type);
        }
        return methodSignature;
    } 
    
    /**
     * Règle (3.12)
     */
    public EnvironmentExp verifyListParamsNames(DecacCompiler compiler, EnvironmentExp localEnv) throws ContextualError {
        EnvironmentExp envExpParams = new EnvironmentExp(localEnv);
        for (AbstractDeclParam param : this.getList()) {
            Symbol symbol = param.getName().getName();
            ParamDefinition paramDef = param.verifyParamName(compiler);
            try {
                envExpParams.declare(symbol, paramDef);
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("Name of parameter "+symbol+" is already used in method signature", getLocation());
            }
        }
        return envExpParams;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractDeclParam> iter = this.getList().iterator();
        while (iter.hasNext()) {
            iter.next().decompile(s);
            if (iter.hasNext()) {
                s.print(", ");
            }
        }
    }
    
}
