package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.Label;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Print statement (print, println, ...).
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractPrint extends AbstractInst {

    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }
    
    /**
     * Règle (3.21) : inst --> print
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        
        if (this.arguments.size() == 0) {
            throw new ContextualError("Print function requires at least one argument.", getLocation());
        }
        
        for (AbstractExpr argument : arguments.getList()) {
            Type argumentType = argument.verifyExpr(compiler, localEnv, currentClass);
            if (!(argumentType.isString() || argumentType.isInt() || argumentType.isFloat())) {
                throw new ContextualError("Argument for print function must be int, float or String.", getLocation());
            }
        }
        
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
    	  if(this.printHex){
    		  for (AbstractExpr argument : getArguments().getList()) {
                  argument.codeGenPrintx(compiler);
              }
          }else{
        	  for (AbstractExpr argument : getArguments().getList()) {
                  argument.codeGenPrint(compiler);
              }
          }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (this.getPrintHex()) {
            s.print("print"+this.getSuffix()+"x(");
            this.arguments.decompile(s);
            s.print(");");
        } else {
            s.print("print"+this.getSuffix()+"(");
            this.arguments.decompile(s);
            s.print(");");
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
