package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateFloat;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Single precision, floating-point literal
 *
 * @author gl59
 * @date 01/01/2022
 */
public class FloatLiteral extends AbstractExpr {

    public float getValue() {
        return value;
    }

    private float value;

    public FloatLiteral(float value) {
        this.value = value;
    }
    
    /**
     * Règle (3.38) : expr --> literal
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.setType(new FloatType(compiler.getSymbolTable().create("float")));
        return this.getType();        
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toHexString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    public DVal codePreGen(DecacCompiler compiler) {
    	return new ImmediateFloat(value);
    }

}
