package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.RTS;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class DeclField extends AbstractDeclField {
    
    private AbstractIdentifier type;
    private AbstractIdentifier fieldName;
    private AbstractInitialization initialization;
    private Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier fieldName,
            AbstractInitialization initialisation, Visibility visibility) {
        this.type = type;
        this.fieldName = fieldName;
        this.initialization = initialisation;
        this.visibility = visibility;
    }
    
    public Type getType() {
        return this.type.getType();
    }
    
    public void setType(Type t) {
        this.type.setType(t);
    }
    
    /**
     * Règle (2.5)
     */
    @Override
    public void verifyDeclField(DecacCompiler compiler,
            AbstractIdentifier superClass, AbstractIdentifier currentClass) 
            throws ContextualError {
        SymbolTable.Symbol symbol = this.fieldName.getName();
        Type actualType = this.type.verifyType(compiler);
        if (actualType.isVoid()) {
            throw new ContextualError("Type of field " + symbol + " cannot be void", getLocation());
        }
        this.type.setType(actualType);
        
        Definition def = superClass.getClassDefinition().getMembers().get(symbol);
        if (def != null && !def.isField()) {
            throw new ContextualError("Field "+symbol+" is already defined "
                    + "in Superclass "+superClass.getName()+" as a "+def.getNature(), getLocation());
        }
        
        FieldDefinition fieldDef = new FieldDefinition(actualType, getLocation(), 
                                        visibility, currentClass.getClassDefinition(),
                currentClass.getClassDefinition().getNumberOfFields());
        currentClass.getClassDefinition().incNumberOfFields();
        
        try {
            currentClass.getClassDefinition().getMembers().declare(symbol, fieldDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Double definition of field "+symbol, getLocation());
        }
        this.fieldName.verifyExpr(compiler, currentClass.getClassDefinition().getMembers(),
                                    currentClass.getClassDefinition());
    }
    
    /**
     * Règle (3.7)
     */
    @Override
    public void verifyFieldInitialisation(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        this.initialization.verifyInitialization(compiler, t, currentClass.getMembers(),
                                                currentClass);
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        if (visibility == Visibility.PROTECTED) {
            s.print("protected");
            s.print(" ");
        }
        this.type.decompile(s);
        s.print(" ");
        this.fieldName.decompile(s);
        this.initialization.decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        fieldName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        fieldName.iter(f);
        initialization.iter(f);
    }

	@Override
	protected void codeGenField(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		if (this.initialization instanceof NoInitialization) {
            if (type.getType().isInt()) {
                compiler.addInstruction(new LOAD(0, Register.R0));
            } else if (type.getType().isFloat()) {
            	//compiler.addInstruction(new FLOAT(new ImmediateFloat(0), Register.R0));
                compiler.addInstruction(new LOAD((float) 0, Register.R0));
            } else if (type.getType().isBoolean()) {
                compiler.addInstruction(new LOAD(0, Register.R0));
            }else {
            	this.initialization.codeGeninitializationField(compiler);
            }
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
            int index = this.fieldName.getFieldDefinition().getIndex();
            compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(index+1, Register.R1)));
           
        }
	}
    
}
