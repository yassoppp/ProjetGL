package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegsManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * @author gl59
 * @date 01/01/2022
 */
public class Main extends AbstractMain {
    private static final Logger LOG = Logger.getLogger(Main.class);

    private ListDeclVar declVariables;
    private ListInst insts;
    public Main(ListDeclVar declVariables,
            ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

    /**
     * Règle (3.4) : main --> Main
     */
    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
        //LOG.debug("verify Main: start");
        this.declVariables.verifyListDeclVariable(compiler, compiler.getEnvExp(), null);
        this.insts.verifyListInst(compiler, compiler.getEnvExp(), null, new VoidType(compiler.getSymbolTable().create("void")));
        //LOG.debug("verify Main: end");
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler) {
    	compiler.addComment("Beginning of main declarations:");
        declVariables.codeGenListDecl(compiler);
        compiler.addComment("Beginning of main instructions:");
        insts.codeGenListInst(compiler);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.println("{");
        s.indent();
        declVariables.decompile(s);
        insts.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        declVariables.iter(f);
        insts.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
    }
}
