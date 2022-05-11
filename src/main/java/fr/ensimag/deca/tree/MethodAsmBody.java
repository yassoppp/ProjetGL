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
public class MethodAsmBody extends AbstractMethodBody {
    StringLiteral code;

    public MethodAsmBody(StringLiteral code) {
	super();
	this.code = code;
    }
    
    /**
     * Règle (3.15)
     */
    @Override
    public void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv,
            EnvironmentExp envParams, ClassDefinition currentClass,
            Type returnType) throws ContextualError {
        this.code.verifyExpr(compiler, localEnv, currentClass);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("asm(");
        code.decompile(s);
        s.println(");");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        code.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        code.iter(f);
    }

    @Override
    protected void codeGenbody(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
