package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class MethodBody extends AbstractMethodBody {
    private ListDeclVar var;
    private ListInst inst;

    public MethodBody(ListDeclVar var, ListInst inst) {
        super();
	this.var = var;
	this.inst = inst;
    }
    
    /**
     * Règle (3.14)
     */
    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            EnvironmentExp envParams, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        this.var.verifyListDeclVariable(compiler, envParams, currentClass);
        this.inst.verifyListInst(compiler, envParams, currentClass, returnType);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        var.decompile(s);
        inst.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        var.prettyPrint(s, prefix, false);
        inst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        var.iter(f);
        inst.iter(f);
    }

	@Override
	protected void codeGenbody(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		compiler.getRegsManager().freeall();
		var.codeGenListDeclLocale(compiler);
		compiler.getRegsManager().setNumparam(0);
		inst.codeGenListInst(compiler);
	}

}
