package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Règle (1.2)
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        //LOG.debug("verify listClass: start");
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.verifyClass(compiler);
        }
        // LOG.debug("verify listClass: end");
    }

    /**
     * Règle (2.2)
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.verifyClassMembers(compiler);
        }
    }
    
    /**
     * Règle (3.2)
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass declClass : this.getList()) {
            declClass.verifyClassBody(compiler);
        }
    }

	public void codeGenListTabledeMethode(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		int addsp=2;
		ClassDefinition objet=(ClassDefinition)compiler.getObjectClass();
		for(AbstractDeclClass classe :this.getList()) {
			classe.getName().getClassDefinition().setOperand(new RegisterOffset(addsp+1, Register.GB));
			String n=classe.getNameSuper().getType().toString();
			if(n=="Object") {
				addsp=addsp+2+classe.getName().getClassDefinition().getNumberOfMethods();
			}else {
				addsp=addsp+2+classe.getNameSuper().getClassDefinition().getNumberOfMethods()+classe.getName().getClassDefinition().getNumberOfMethods();
			}
		}
		compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, Register.GB)));
		LabelOperand reg=new LabelOperand(new Label("code.Object.equals"));
		compiler.addInstruction(new LOAD(reg,Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, Register.GB)));
		compiler.getRegsManager().setPile(3);
		for(AbstractDeclClass classe:this.getList()) {
			classe.codeGenTabledeMethode(compiler);
		}

	}


	public void codeGenMethode(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		compiler.addLabel(new Label("code.Object.equals") );
		compiler.addInstruction(new RTS());
		for(AbstractDeclClass classe: this.getList()) {
			classe.codeGenMethode(compiler);
		}
	}
    
}
