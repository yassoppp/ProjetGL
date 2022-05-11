package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class Selection extends AbstractLValue{
    private AbstractExpr expr;
    private AbstractIdentifier ident;

    public Selection(AbstractExpr expr, AbstractIdentifier ident) {
	super();
	this.expr = expr;
        this.ident = ident;
    }

    @Override
    protected DVal codePreGen(DecacCompiler compiler) {
	// TODO Auto-generated method stub
    if(expr instanceof This) {
			GPRegister reg=compiler.getRegsManager().getReg();
	        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), reg));
	        compiler.addInstruction(new LOAD(new RegisterOffset(ident.getFieldDefinition().getIndex()+1,reg),reg));
	        return reg;
	}else {
    GPRegister reg=compiler.getRegsManager().getReg();
    compiler.addInstruction(new LOAD(((Identifier)expr).getVariableDefinition().getOperand(), reg));
    compiler.addInstruction(new LOAD(new RegisterOffset(ident.getFieldDefinition().getIndex()+1,reg),reg));
    return reg;
	}
    }

    /**
     * Règle (3.65) et (3.66)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = this.expr.verifyExpr(compiler, localEnv, currentClass);
        if (type == null || !type.isClass()) {
            throw new ContextualError("Identifier "+expr.decompile()+" is not not an instance of class",
                    getLocation());
        }
        
        EnvironmentExp env = ((ClassType)type).getDefinition().getMembers();
        this.ident.verifyExpr(compiler, env, currentClass);
        Definition fieldDef = this.ident.getDefinition();
        if (fieldDef == null || !fieldDef.isField()) {
            throw new ContextualError("Identifier "+ident.getName()+" is not a field",
                    getLocation());
        }
        
        Visibility visibility = this.ident.getFieldDefinition().getVisibility();
        ClassDefinition containingClass = ident.getFieldDefinition().getContainingClass();
        if (visibility == Visibility.PROTECTED) {
            if (currentClass == null) {
                throw new ContextualError("Cannot access protected field "+ident.getName()+
                        " : main program is not a subType of class "+
                        containingClass.getType(), getLocation());
            } else if (!compiler.getEnvTypes().subType(type, currentClass.getType())) {
                throw new ContextualError("Cannot access protected field "+ident.getName()
                        +" : Type "+type+" of expression "+expr.decompile()+
                        " should be a subType of the current class "+ currentClass.getType(),
                        getLocation());
            } else if (!compiler.getEnvTypes().subType(currentClass.getType(),
                    containingClass.getType())) {
                throw new ContextualError("Cannot access protected field "+ident.getName()+
                        " : current class " +currentClass.getType()+
                        " should be a subType of the class " +containingClass.getType()+
                        " where the field is declared", getLocation());
            }
        }
        this.setType(fieldDef.getType());
        return fieldDef.getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        ident.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
	// TODO Auto-generated method stub
    }


	public void codeGenInst(DecacCompiler compiler, GPRegister tmp) {
		// TODO Auto-generated method stub
		if(expr instanceof This) {
			GPRegister reg=compiler.getRegsManager().getReg();
	        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), reg));
	        compiler.addInstruction(new STORE(tmp, new RegisterOffset(ident.getFieldDefinition().getIndex()+1,reg)));
		}else {
		GPRegister reg=compiler.getRegsManager().getReg();
        compiler.addInstruction(new LOAD(((Identifier)expr).getVariableDefinition().getOperand(), reg));
        compiler.addInstruction(new STORE(tmp, new RegisterOffset(ident.getFieldDefinition().getIndex()+1,reg)));
		}
	}

}
