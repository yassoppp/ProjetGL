package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class ListDeclField extends TreeList<AbstractDeclField> {
    
    /**
     * Règle (2.4)
     */
    public void verifyListDeclField(DecacCompiler compiler, AbstractIdentifier superClass, 
            AbstractIdentifier currentClass) throws ContextualError {
        for (AbstractDeclField field : this.getList()) {
            field.verifyDeclField(compiler, superClass, currentClass);
        }
    } 
    
    /**
     * Règle (3.6)
     */
    public void verifyListFieldInitialisation(DecacCompiler compiler,
                EnvironmentExp localEnv, ClassDefinition currentClass)
                throws ContextualError {
        for (AbstractDeclField field : this.getList()) {
            field.verifyFieldInitialisation(compiler, field.getType(),
                    currentClass.getMembers(), currentClass);
        }
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField field : this.getList()) {
            field.decompile(s);
            s.println();
        }
    }

	public void codeGenListField(DecacCompiler compiler) {
		for (AbstractDeclField field : this.getList()) {
			
            field.codeGenField(compiler);
        }
	}
    
    
}
