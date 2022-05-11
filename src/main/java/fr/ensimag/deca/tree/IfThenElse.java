package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Full if/else if/else statement.
 *
 * @author gl59
 * @date 01/01/2022
 */
public class IfThenElse extends AbstractInst {
	
	private static int i=0;
    private Label Iftruethen;
    private Label Else;
    private Label Endif;
    
    protected void labelsatate() {
    	i++;
    	this.Else=new Label("else"+i);
    	this.Endif=new Label("endif"+i);
    	this.Iftruethen=new Label("if.true.then"+i);
    }
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;

    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    /**
     * Règle (3.22) : inst --> IfThenElse
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
        this.elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	labelsatate();
    	if (condition instanceof AbstractBinaryExpr) {
            condition.codeGenInst(compiler,Else);
        }else{
            condition.codeGenInst(compiler,Else);
        }
    	compiler.addLabel(this.Iftruethen);
    	thenBranch.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(this.Endif));
        compiler.addLabel(this.Else);
        elseBranch.codeGenListInst(compiler);
        compiler.addLabel(this.Endif);
    	
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("if (");
        this.condition.decompile(s);
        s.print(") {");
        s.indent();
        this.thenBranch.decompile(s);
        s.unindent();
        s.print("} else {");
        s.indent();
        this.elseBranch.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }
    
    
}
