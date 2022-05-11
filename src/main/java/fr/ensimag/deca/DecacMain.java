package fr.ensimag.deca;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;


import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;



/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl59
 * @date 01/01/2022
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);

    public static class C implements Callable{
    	private File sourceFile;
    	private CompilerOptions options;

    	public C(File sourceFile, CompilerOptions options) {
            this.sourceFile = sourceFile;
            this.options = options;
    	}

	@Override
	public Boolean call() throws IOException, DecacFatalError {
            if (options.getVerify() || options.getParser()) {
                parseVerify(options, sourceFile);
                return false;
            } else {
                return (new DecacCompiler(options, sourceFile)).compile();
            }
	}

    }
    
  
    public static void parseVerify(CompilerOptions options, File sourceFile)
            throws IOException, DecacFatalError{
 	AbstractProgram aprogram = null;
 	DecacCompiler dcompiler = new DecacCompiler(options, sourceFile);
        try {
     	    aprogram = dcompiler.doLexingAndParsing(sourceFile.getPath(), System.err);
        } catch (DecacFatalError erreur) {
            System.exit(1);
        }

        // -v option
 	if (options.getVerify()) {
            try {
                aprogram.verifyProgram(dcompiler);
            } catch (ContextualError erreur) {
                erreur.display(System.err);
                System.exit(1);
            }
        }
        else{
            // -p option
             if (aprogram != null){
                 aprogram.decompile(System.out);
             } else{
                 System.exit(1);
             }
         }
    }
    
    // Main programm
    public static void main(String[] args) throws IOException, DecacInternalError, DecacFatalError {
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
            System.out.println("|Equipe GL59|");
            System.exit(0);
        }

        if (options.getParallel()) {
            LinkedList<Future<Boolean>> future_list = new LinkedList<Future<Boolean>>();
            int available = java.lang.Runtime.getRuntime().availableProcessors();
            ExecutorService exec = Executors.newFixedThreadPool(available);

            for (File source : options.getSourceFiles()) {
        	@SuppressWarnings("unchecked")
		Callable<Boolean> callable = new C(source, options);
		Future<Boolean> f = exec.submit(callable);
                future_list.add(f);
            }

            for (Future<Boolean> f : future_list) {
        	try {
                    if (f.get()) {
                        error = true;
                    }
		} catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    System.out.println("Compilation failed");
		}
            }
        } else {
            if (options.getVerify() || options.getParser()) {
                for (File sourceFile : options.getSourceFiles()){
                    parseVerify(options, sourceFile);
                }
            } else {
                for (File source : options.getSourceFiles()) {
                    DecacCompiler compiler = new DecacCompiler(options, source);
                    if (compiler.compile()) {
                        error = true;
                    }
                }
            }
        }
        
        System.exit(error ? 1 : 0);
    }

}
