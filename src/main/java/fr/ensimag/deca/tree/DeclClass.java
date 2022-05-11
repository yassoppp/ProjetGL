package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl59
 * @date 01/01/2022
 */
public class DeclClass extends AbstractDeclClass {

    private static final String ArrayList = null;
	private AbstractIdentifier name;
    private AbstractIdentifier nameSuper;
    private ListDeclField fields;
    private ListDeclMethod methods;
    
    public DeclClass(AbstractIdentifier name, AbstractIdentifier nameSuper,
            ListDeclField fields, ListDeclMethod methods) {
        Validate.notNull(name);
        Validate.notNull(nameSuper);
        Validate.notNull(fields);
        Validate.notNull(methods);
        this.name = name;
        this.nameSuper = nameSuper;
        this.fields = fields;
        this.methods = methods;
    }
    
    public AbstractIdentifier getName() {
        return this.name;
    }
    
    public AbstractIdentifier getNameSuper() {
        return this.nameSuper;
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class ");
        name.decompile(s);
        if (!nameSuper.getName().toString().equals("Object")) {
            s.print(" extends ");
            nameSuper.decompile(s);
        }
        s.println(" {");
        s.indent();
        fields.decompile(s);
        methods.decompile(s);
        s.unindent();
        s.println("}");
    }
    
    /**
     * Règle (1.3)
     */
    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        SymbolTable.Symbol superSymbol = compiler.getSymbolTable().create(this.nameSuper.getName().toString());
        TypeDefinition superDef = compiler.getEnvTypes().get(superSymbol);
        if (superDef == null) {
            throw new ContextualError("Superclass "+superSymbol+" is undefined", getLocation());
        } else if (!superDef.isClass()) {
            throw new ContextualError("the Superclass given "+superSymbol+" is not a class", getLocation());
        }
        
        if (compiler.getSymbolTable().getMap().containsValue(this.name.getName())) {
            throw new ContextualError("Name of class cannot be the predefined type "+name.getName() , getLocation());
        }
        
        this.getNameSuper().setDefinition(superDef);
        this.getNameSuper().setType(superDef.getType());
        SymbolTable.Symbol classSymbol = compiler.getSymbolTable().create((this.name.getName()).toString());
        ClassType classType = new ClassType(classSymbol, Location.BUILTIN, (ClassDefinition) superDef);
        try {
            compiler.getEnvTypes().declare(classSymbol, classType.getDefinition());
        } catch (EnvironmentType.DoubleDefException e) {
            throw new ContextualError("Double definition of class " + classSymbol, getLocation());
        }
        this.getName().setDefinition(classType.getDefinition());
        this.getName().setType(classType);
    }
    
    /**
     * Règle (2.3)
     */
    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        this.fields.verifyListDeclField(compiler, this.nameSuper, this.name);
        this.methods.verifyListDeclMethod(compiler, this.nameSuper, this.name);
    }
    
    /**
     * Règle (3.5)
     */
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        this.fields.verifyListFieldInitialisation(compiler, this.name.getClassDefinition().getMembers(),
                this.name.getClassDefinition());
        this.methods.verifyListMethodBody(compiler, this.name.getClassDefinition().getMembers(), 
                    this.name.getClassDefinition());
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        name.prettyPrint(s, prefix, false);
        nameSuper.prettyPrint(s, prefix, false);
        fields.prettyPrint(s, prefix, false);
        methods.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        name.iter(f);
        nameSuper.iter(f);
        fields.iter(f);
        methods.iter(f);
    }
    @Override
	protected void codeGenTabledeMethode(DecacCompiler compiler) {
		// TODO Auto-generated method
		if(nameSuper.getType().toString().equals("Object")) {
			compiler.addInstruction(new LEA(new RegisterOffset(1, Register.GB),Register.R0 ));
		}else {
		compiler.addInstruction(new LEA(nameSuper.getClassDefinition().getOperand(),Register.R0 ));
		}
		compiler.addInstruction(new STORE(Register.R0, this.name.getClassDefinition().getOperand()));
		compiler.getRegsManager().ajoutpile();
		LabelOperand reg=new LabelOperand(new Label("code.Object.equals"));
		compiler.addInstruction(new LOAD(reg,Register.R0));
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(compiler.getRegsManager().getPile(), Register.GB)));
		compiler.getRegsManager().ajoutpile();
		for(AbstractDeclMethod methode: this.methods.getList()) {
			MethodDefinition v=methode.getMethodName().getMethodDefinition();
			Label l= new Label("code."+this.getName().getName().toString()+"."+methode.getMethodName().getName().toString());
			v.setLabel(l);
			compiler.addInstruction(new LOAD(new LabelOperand(l), Register.R0));
			compiler.addInstruction(new STORE(Register.R0,new RegisterOffset(compiler.getRegsManager().getPile(), Register.GB)));
			compiler.getRegsManager().ajoutpile();
		}
	}
		
	@Override
	protected void codeGenMethode(DecacCompiler compiler) {
		compiler.addLabel(new Label("init."+this.getName().getName().toString()));
		//freealllregs;
		if(this.getNameSuper().getName().toString().equals("Object")) {
			fields.codeGenListField(compiler);
		}else {
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
            compiler.addInstruction(new PUSH(Register.R0));
            compiler.addInstruction(new BSR(new Label("init."+ getNameSuper().getName().toString())));
            compiler.addInstruction(new SUBSP(1));
            fields.codeGenListField(compiler);
		}
		compiler.addInstruction(new RTS());
		for(AbstractDeclMethod methode:this.methods.getList()) {
			compiler.addLabel(new Label("code."+this.getName().getName().toString()+"."+methode.getMethodName().getName().toString()));
			methode.codeGenMethodCode(compiler);
		}
	}

	
}