package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl59
 * @date 01/01/2022
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclVar var : this.getList()) {
            var.decompile(s);
            s.println();
        }
    }

    /**
     * Règle (3.16)
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclVar var : this.getList()) {
            var.verifyDeclVar(compiler, localEnv, currentClass);
        }
    }
    public void codeGenListDecl(DecacCompiler compiler){
    	int i=compiler.getRegsManager().getPile();
        for(AbstractDeclVar var: this.getList()){
            RegisterOffset addr = new RegisterOffset(i, Register.GB);
            var.codeGenDeclVar(compiler, addr);
            i++;
            compiler.getRegsManager().ajoutpile();
        }
        compiler.addFirst(new ADDSP(compiler.getRegsManager().getPile()-1));
    }

	public void codeGenListDeclLocale(DecacCompiler compiler) {
		int i=compiler.getRegsManager().getNumparam();
		for(AbstractDeclVar var:this.getList()) {
			RegisterOffset addr = new RegisterOffset(-i-3, Register.LB);
            var.codeGenDeclVarLocale(compiler, addr);
            i++;
		}
		
	}

}
