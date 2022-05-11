package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;

import java.io.PrintStream;


public class Cast extends AbstractExpr {
  private Identifier ident;
  private AbstractExpr expr;


    public Cast(Identifier ident,AbstractExpr expr) {
      this.ident = ident;
      this.expr = expr;
    }

    /**
     * Règle (3.39) : expr --> Cast
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type type = this.ident.verifyType(compiler);
        Type type2 = this.expr.verifyExpr(compiler, localEnv, currentClass);
        if (!EnvironmentType.castCompatible(type2, type)) {
            throw new ContextualError("Incompatible cast of type "+type2+" to type "+type, getLocation());
        }
        this.setType(type);
        return type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        this.ident.decompile(s);
        s.print(") (");
        this.expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        ident.prettyPrint(s, prefix, false);
        expr.prettyPrint(s, prefix, true);
    }

	@Override
	protected DVal codePreGen(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		return null;
	}

}
