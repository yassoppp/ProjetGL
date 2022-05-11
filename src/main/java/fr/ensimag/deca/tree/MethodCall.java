package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;


public class MethodCall extends AbstractExpr {
    private AbstractExpr expr;
    private AbstractIdentifier ident;
    private ListExpr list_expr;


    public MethodCall(AbstractExpr expr,AbstractIdentifier ident,ListExpr list_expr) {
        this.expr = expr;
        this.ident = ident;
        this.list_expr = list_expr;
    }

    /**
     * Règle (3.41)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeClass = this.expr.verifyExpr(compiler, localEnv, currentClass);
        if (typeClass == null || !typeClass.isClass()) {
            throw new ContextualError("Identifier "+expr.decompile()+
                    " is not an instance of class", getLocation());
        }

        EnvironmentExp env = ((ClassType)typeClass).getDefinition().getMembers();
        this.ident.verifyExpr(compiler, env, currentClass);
        Definition Def = this.ident.getDefinition();
        if (Def == null || !Def.isMethod()) {
            throw new ContextualError(this.ident.getName()+" is not a method",
                    getLocation());
        }

        MethodDefinition methodDef = (MethodDefinition) Def;
        Signature expectedSig = methodDef.getSignature();
        Signature currentSig = new Signature();
        if (list_expr.getList().size() != expectedSig.size()) {
            throw new ContextualError("Incorrect signature of method "+ident.getName(),
                    getLocation());
        } else {
            int i = 0;
            for (AbstractExpr expression : list_expr.getList()) {
                list_expr.set(i, expression.verifyRValue(compiler, localEnv, currentClass,
                        expectedSig.paramNumber(i)));
                Type type = list_expr.getList().get(i).getType();
                i += 1;
                currentSig.add(type);
            }
        }

        this.setType(methodDef.getType());
        return this.getType();
    }


    @Override
    String prettyPrintNode() {
        return "MethodCall";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        ident.decompile(s);
        s.print("(");
        list_expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.expr.prettyPrint(s, prefix, false);
        this.ident.prettyPrint(s, prefix, false);
        this.list_expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected DVal codePreGen(DecacCompiler compiler) {
	// TODO Auto-generated method stub
    	this.codeGenInst(compiler);
    	return Register.R0;
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	// TODO Auto-generated method stub
    	/*IMAProgram x=new IMAProgram();
    	IMAProgram y= compiler.getIMA();
    	compiler.setIMA(x);*/
    	compiler.addInstruction(new ADDSP(list_expr.size()+1));
    	GPRegister reg=compiler.getRegsManager().getReg();
    	compiler.addInstruction(new LOAD(((Identifier)expr).getVariableDefinition().getOperand(), reg));
    	compiler.addInstruction(new STORE(reg, new RegisterOffset(0, Register.SP)));
    	int i=1;
    	for (AbstractExpr expression : list_expr.getList()) {
    		if(expression instanceof Assign) {
    			((Assign)expression).codeGenInst(compiler);
    		}
    		DVal dval=expression.codePreGen(compiler);
    		compiler.addInstruction(new LOAD(dval, reg));
    		compiler.addInstruction(new STORE(reg,new RegisterOffset(-i, Register.SP)));
    		i++;
    	}
    	compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), reg));
    	compiler.addInstruction(new LOAD(new RegisterOffset(0, reg), reg));
    	compiler.addInstruction(new BSR(new RegisterOffset(ident.getMethodDefinition().getIndex()+1, reg)));
    	compiler.addInstruction(new SUBSP(list_expr.size()+1));
    	
    }

}
