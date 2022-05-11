package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class ListDeclMethod extends TreeList<AbstractDeclMethod>{

    /**
     * Règle (2.6)
     */
    public void verifyListDeclMethod(DecacCompiler compiler, AbstractIdentifier superClass, 
                        AbstractIdentifier currentClass)
            throws ContextualError {
        for (AbstractDeclMethod method : this.getList()) {
            method.verifyDeclMethod(compiler, superClass, currentClass);
        }
    } 
    
    /**
     * Règle (3.10)
     */
    public void verifyListMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
                        ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod method : this.getList()) {
            EnvironmentExp envExpParams = method.getParams().verifyListParamsNames(compiler, localEnv);
            method.getMethodBody().verifyMethodBody(compiler, currentClass.getMembers(),
                                    envExpParams, currentClass, method.getReturnType().getType());
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod method : this.getList()) {
            method.decompile(s);
            s.println();
        }
    }

}
