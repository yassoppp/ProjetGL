package fr.ensimag.deca;

import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.codegen.RegsManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.Location;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.AbstractLine;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

/**
 * Decac compiler instance.
 *
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 *
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl59
 * @date 01/01/2022
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);
    private RegsManager regsManager;
    private EnvironmentType env_types;
    private EnvironmentExp env_exp;
    private SymbolTable symbolTable;
    public void initialize() {
    	this.regsManager=new RegsManager(this);
    }
    
    
    public RegsManager getRegsManager() {
		return regsManager;
	}


	public void setRegsManager(RegsManager regsManager) {
		this.regsManager = regsManager;
	}


	public EnvironmentType getEnvTypes() {
        return this.env_types;
    }
    
    public EnvironmentExp getEnvExp() {
        return this.env_exp;
    }
    
    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }
    
    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        this.env_types = new EnvironmentType(null);
        this.env_exp =  new EnvironmentExp(null);
        this.symbolTable = new SymbolTable();
        this.initiate_env();
    }


    public void initiate_env() {
        Symbol void_symbol = symbolTable.create("void");
        Symbol boolean_symbol = symbolTable.create("boolean");
        Symbol float_symbol = symbolTable.create("float");
        Symbol int_symbol = symbolTable.create("int");
        Symbol object_symbol = symbolTable.create("Object");
        Symbol equals_symbol = symbolTable.create("equals");
        
        try {
            // Types prédéfinis
            env_types.declare(void_symbol, new TypeDefinition(new VoidType(void_symbol), Location.BUILTIN));
            env_types.declare(boolean_symbol, new TypeDefinition(new BooleanType(boolean_symbol), Location.BUILTIN));
            env_types.declare(float_symbol, new TypeDefinition(new FloatType(float_symbol), Location.BUILTIN));
            env_types.declare(int_symbol, new TypeDefinition(new IntType(int_symbol), Location.BUILTIN));
          
            ClassType object_class = new ClassType(object_symbol, Location.BUILTIN, null);
            env_types.declare(object_symbol, object_class.getDefinition());
            
            // Méthode equals de la classe Object
            Signature equals_signature = new Signature();
            equals_signature.add(object_class.getDefinition().getType());
            MethodDefinition equalsDef = new MethodDefinition(new BooleanType(boolean_symbol), Location.BUILTIN, equals_signature, 0);
            object_class.getDefinition().getMembers().declare(equals_symbol, equalsDef);
            env_exp.declare(equals_symbol, equalsDef);
            
        } catch (EnvironmentType.DoubleDefException | EnvironmentExp.DoubleDefException e) {
            System.err.println("Double definition :\n");
        }
        
    }
    public void addFirst(Instruction i) {
    	this.program.addFirst(i);
    }
    public void setIMA(IMAProgram prog) {
    	this.program=prog;
    }
    public IMAProgram getIMA() {
    	return program;
    }
    
    
    public TypeDefinition getObjectClass() {
        return env_types.get(symbolTable.create("Object"));
    }
    
    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see
     * fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }
    
    /**
     * @see 
     * fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }
    
    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private IMAProgram program = new IMAProgram();
 

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = null;
        int lastIndex = sourceFile.lastIndexOf('.');
        destFile = sourceFile.substring(0, lastIndex) + ".ass";
        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);
        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName name of the destination (assembly) file
     * @param out stream to use for standard output (output of decac -p)
     * @param err stream to use to display compilation errors
     *
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName,
            PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {
        AbstractProgram prog = doLexingAndParsing(sourceName, err);

        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }
        assert(prog.checkAllLocations());


        prog.verifyProgram(this);
        assert(prog.checkAllDecorations());

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);

        FileOutputStream fstream = null;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     * compiler.
     * @throws LocationException When a compilation error (incorrect program)
     * occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }
    
    

}
