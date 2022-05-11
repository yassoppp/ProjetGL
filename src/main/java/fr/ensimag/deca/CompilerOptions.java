package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl59
 * @date 01/01/2022
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }

    public boolean getParser() {
        return parser;
    }

    public boolean getVerify(){
        return verify;
    }

    public boolean getCheck(){
        return check;
    }

    public boolean getRegistre(){
        return registre;
    }

    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }


    private int debug = 0;
    private boolean parallel = false;
    private boolean printBanner = false;
    private boolean parser = false;
    private boolean verify = false;
    private boolean check = false;
    private boolean registre = false;
    private List<File> sourceFiles = new ArrayList<File>();


    public void banner(String[] args){
        if (args.length == 1){
            this.printBanner = true;
            // System.exit(0);
        } else {
            System.out.println("the banner -b runs with no source files");
            System.exit(1);
        }
    }

    public void eMessage(String[] args){
        if (args.length == 1){
            System.out.println("You need source files, this options cannot run without at least one source file!");
            System.exit(0);
        }
    }

    public static int findFirstSrc(String[] args){
        int a = 0;
        for (int i = 0; i < args.length; i++){
            if (!args[i].startsWith("-")){
                a = i;
                break;
            }
        }
        return a;
    }

    public void pArguments(String[] args){
        if (args[0].equals("-b")){
            banner(args);
        }
        int firstIndex = findFirstSrc(args);
        for (int i = 0; i < firstIndex; i++){
            // -p option
            if (args[i].equals("-p")){
                eMessage(args);
                parser = true;
            }
            if (args[i].equals("-r")){
                //gestion du nombre de registre max
                eMessage(args);
                registre = true;
            }
            if (args[i].equals("-v")){
                eMessage(args);
                this.verify = true;
            }
            if (args[i].equals("-n")){
                eMessage(args);
                check = true;
            }
            if (args[i].equals("-d")){
                eMessage(args);
                debug += 1;
            }
            if (args[i].equals("-P")){
                eMessage(args);
                parallel = true;
            }
        }
        if (parser && verify){
            System.out.println("Incompatible options [-v and -p]");
            System.exit(1);
        }
        addFiles(args, firstIndex);
    }

    public void addFiles(String[] args, int start){
        for (int i = start; i < args.length; i++){
            File f = new File(args[i]);
            this.sourceFiles.add(f);
        }
    }


    public void parseArgs(String[] args) throws CLIException {
        if (args.length == 0){
            displayUsage();

        } else {
            if (!args[0].startsWith("-")){
                addFiles(args,0);
        } else {
            pArguments(args);
        }

        Logger logger = Logger.getRootLogger();
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }

        //throw new UnsupportedOperationException("not yet implemented");
        // File f = new File(args[0]);
        // sourceFiles.add(f);

    }
}
    protected void displayUsage(){
        //throw new UnsupportedOperationException("not yet implemented");
        String usage = "-b (banner) : affiche une bannière indiquant le nom de l'équipe \n"+
                       "-p (parse) : arrête decac après l'étape de construction de l'arbre, et affiche la décompilation de ce dernier\n"+
                       "-v (verification) : arrête decac après l'étape de vérifications (ne produit aucune sortie en l'absence d'erreur)\n"+
                       "-n (no check) : supprime les tests à l'exécution spécifiés dans les points 11.1 et 11.3 de la sémantique de Deca.\n"+
                       "-r X (registers) : limite les registres banalisés disponibles à R0 ... R{X-1}, avec 4 <= X <= 16\n"+
                       "-d (debug) : active les traces de debug. Répéter l'option plusieurs fois pour avoir plus de traces.\n"+
                       "-P (parallel) : s'il y a plusieurs fichiers sources, lance la compilation des fichiers en parallèle (pour accélérer la compilation)";
        System.out.println("Decac without arguments");
        System.out.println("Here are the available options : ");
        System.out.println(usage);
    }
}
