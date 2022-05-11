package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.RTS;

import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class DeclMethod extends AbstractDeclMethod {

    private AbstractIdentifier returnType;
    private AbstractIdentifier methodName;
    private ListDeclParam params;
    private AbstractMethodBody methodBody;

    public DeclMethod(AbstractIdentifier returnType, AbstractIdentifier methodName,
            ListDeclParam params, AbstractMethodBody methodBody) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.params = params;
        this.methodBody = methodBody;
    }
    
    public AbstractIdentifier getReturnType() {
        return this.returnType;
    }
    
    public AbstractIdentifier getMethodName() {
		return methodName;
	}

	public void setMethodName(AbstractIdentifier methodName) {
		this.methodName = methodName;
	}

	public ListDeclParam getParams() {
        return this.params;
    }
    
    public AbstractMethodBody getMethodBody() {
        return this.methodBody;
    }
    
    /**
     * Règle (2.7)
     */
    @Override
    public void verifyDeclMethod(DecacCompiler compiler, AbstractIdentifier superClass, 
                                AbstractIdentifier currentClass)
            throws ContextualError {
        SymbolTable.Symbol symbol = this.methodName.getName();
        Type type = this.returnType.verifyType(compiler);
        this.returnType.setType(type);
        
        MethodDefinition def = (MethodDefinition) superClass.getClassDefinition().getMembers().get(symbol);
        Signature methodSignature = params.verifyListDeclParam(compiler);
        if (def != null && !def.getSignature().equals(methodSignature)) {
            throw new ContextualError("Redefinition of method "+symbol+
                    " should have the same signature as the Superclass method", getLocation());
        }
        if (def != null && !compiler.getEnvTypes().subType(type, def.getType())) {
            throw new ContextualError("Return type "+type+" of the redefined method "+symbol+
                    " must be a subtype of the return type "+def.getType()+" of the Superclass method",
                    getLocation());
        }
        
        ClassDefinition classDef = currentClass.getClassDefinition();
        int index = currentClass.getClassDefinition().getNumberOfMethods();
        if (def == null) index++;
        MethodDefinition methodDefiniton = new MethodDefinition(type, getLocation(), 
                                    methodSignature, index);
        classDef.incNumberOfMethods();
        
        try {
            currentClass.getClassDefinition().getMembers().declare(symbol, methodDefiniton);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Double definition of method "+symbol, getLocation());
        }

        this.methodName.verifyExpr(compiler, classDef.getMembers(), classDef);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        returnType.decompile(s);
        s.print(" ");
        methodName.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(")");
        methodBody.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        returnType.prettyPrint(s, prefix, false);
        methodName.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        methodBody.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        returnType.iter(f);
        methodName.iter(f);
        params.iter(f);
        methodBody.iter(f);
    }

	@Override
	protected void codeGenMethodCode(DecacCompiler compiler) {
		// TODO Auto-generated method stub
		int numparam=0;
		for(AbstractDeclParam parm:this.params.getList()) {
			DAddr v=(DAddr)(new RegisterOffset(-3-numparam, Register.LB));
			parm.getName().getParamDefinition().setOperand(v);
			numparam++;
			compiler.getRegsManager().setNumparam(numparam);
		}
		IMAProgram j=compiler.getIMA();
		IMAProgram i=new IMAProgram();
		compiler.setIMA(i);
		methodBody.codeGenbody(compiler);
		compiler.getRegsManager().pushall();;
		compiler.getRegsManager().popall();
		compiler.addInstruction(new RTS());
		j.append(i);
		compiler.setIMA(j);
	}

}
