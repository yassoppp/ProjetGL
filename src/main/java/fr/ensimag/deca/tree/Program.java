package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.*;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl59
 * @date 01/01/2022
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    public ListDeclClass getClasses() {
        return classes;
    }
    public AbstractMain getMain() {
        return main;
    }
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        //LOG.debug("verify program: start");
        // Pass 1 of [SyntaxeContextuelle]
        this.classes.verifyListClass(compiler);
        // Pass 2 of [SyntaxeContextuelle]
        this.classes.verifyListClassMembers(compiler);
        // Pass 3 of [SyntaxeContextuelle]
        this.classes.verifyListClassBody(compiler);
        this.main.verifyMain(compiler);
        //LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
    	compiler.initialize();
    	if(this.classes.size()==0) {
        compiler.addComment("Main program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
    	}else {
            classes.codeGenListTabledeMethode(compiler);
    		compiler.addComment("Main program");
            main.codeGenMain(compiler);
            compiler.addInstruction(new HALT());
            compiler.addComment("code des methodes");
            classes.codeGenMethode(compiler);
    	}
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
