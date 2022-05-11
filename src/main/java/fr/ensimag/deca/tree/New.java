package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;


public class New extends AbstractExpr {
    private AbstractIdentifier type;

    public New(AbstractIdentifier type) {
        this.type = type;
    }
    
    /**
     * Règle (3.42)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type actualType = this.type.verifyType(compiler);
        if (!actualType.isClass()) {
            throw new ContextualError("new can only be applied to a class type", getLocation());
        }
        
        this.setType(actualType);
        return this.getType();
    }


    @Override
    String prettyPrintNode() {
        return "";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        type.decompile(s);
        s.print("()");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.type.prettyPrint(s, prefix, true);
    }

    @Override
    protected DVal codePreGen(DecacCompiler compiler) {
        // TODO Auto-generated method stub
        return null;
    }

	public GPRegister codeGenNew(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		GPRegister reg=compiler.getRegsManager().getReg();
		String s=this.type.getClassDefinition().getSuperClass().getType().getName().toString();
		if(!(s=="Object")) {
			int nbr_super_Field=this.type.getClassDefinition().getSuperClass().getNumberOfFields();
			compiler.addInstruction(new NEW(this.type.getClassDefinition().getNumberOfFields()+1+nbr_super_Field, reg));
		}else {
			compiler.addInstruction(new NEW(this.type.getClassDefinition().getNumberOfFields()+1, reg));
		}
		compiler.addInstruction(new LEA(this.type.getClassDefinition().getOperand(), Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, reg)));
		compiler.addInstruction(new PUSH(reg));
		compiler.addInstruction(new BSR(new LabelOperand(new Label("init."+this.type.getName().toString()))));
		compiler.addInstruction(new POP(reg));
		return reg;
	}

}
