package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;


import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class While extends AbstractInst {
    private static int  i=0;
    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }
    private Label Whilebody=new Label("while.body");
    private Label Whilefin=new Label("while.fin");
    private Label Whilestart=new Label("while.start");
    
    protected void labelstate() {
    	i++;
    	Whilebody=new Label("while.body"+i);
        Whilefin=new Label("while.fin"+i);
        Whilestart=new Label("while.start"+i);
    }

    public Label getWhile_body() {
            return Whilebody;
    }

    public Label getWhile_fin() {
        return Whilefin;
    }

    public Label getWhile_start() {
        return Whilestart;
    }

    public ListInst getBody() {
        return body;
    }

    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	labelstate();
    	compiler.addLabel(this.getWhile_start());
    	if (condition instanceof AbstractBinaryExpr) {
    		condition.codeGenInst(compiler,getWhile_fin());
    	}else {
    		condition.codeGenInst(compiler,getWhile_fin());
    	}
        body.codeGenListInst(compiler);
        compiler.addInstruction(new BRA(getWhile_start()));
        compiler.addLabel(getWhile_fin());
    }

    /**
     * Règle (3.25) : inst --> While
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        this.condition.verifyCondition(compiler, localEnv, currentClass);
        this.body.verifyListInst(compiler, localEnv, currentClass, returnType);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }
}
