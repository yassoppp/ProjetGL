package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * String literal
 *
 * @author gl59
 * @date 01/01/2022
 */
public class StringLiteral extends AbstractStringLiteral {

    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    /**
     * Règle (3.38) : expr --> literal
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(new StringType(compiler.getSymbolTable().create("String")));
        return this.getType();
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        compiler.addInstruction(new WSTR(new ImmediateString(value.substring(1, value.length()-1))));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(this.getValue());
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }
    @Override
	protected DVal codePreGen(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		return null;
	}

}
