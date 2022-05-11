package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    /**
     * Règle (3.33) expr --> op_bin
     * type := type_binary_op(op, type1, type2)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        String operator = this.getOperatorName();
        Type returnType = null;
        if (operator.equals("==") || operator.equals("!=") || operator.equals("<") ||
                operator.equals(">") || operator.equals("<=") || operator.equals(">=")) {
            Type leftType = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            Type rightType = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            if (leftType.isFloat() && rightType.isInt()) {
                ConvFloat newValue = new ConvFloat(this.getRightOperand());
                Type newType = newValue.verifyExpr(compiler, localEnv, currentClass);
                this.setRightOperand(newValue);
                returnType =  new BooleanType(compiler.getSymbolTable().create("boolean"));
            } else if (leftType.isInt() && rightType.isFloat()) {
                ConvFloat newValue = new ConvFloat(this.getLeftOperand());
                Type newType = newValue.verifyExpr(compiler, localEnv, currentClass);
                this.setLeftOperand(newValue);
                returnType =  new BooleanType(compiler.getSymbolTable().create("boolean"));
            } else if ((leftType.isBoolean() && rightType.isBoolean())
                    && (operator.equals("==") || operator.equals("!="))) {
                returnType = new BooleanType(compiler.getSymbolTable().create("boolean"));
            } else if (leftType.isInt() && rightType.isInt()) {
                returnType = new BooleanType(compiler.getSymbolTable().create("boolean"));
            } else if (leftType.isClassOrNull() && rightType.isClassOrNull()) {
                returnType = new BooleanType(compiler.getSymbolTable().create("boolean"));
            } else if (leftType.isFloat() && rightType.isFloat()) {
                returnType = new BooleanType(compiler.getSymbolTable().create("boolean"));
            } else {
                throw new ContextualError("Types "+leftType+" and "+rightType+
                        " are incompatible for operator "+operator, getLocation());
            }
        } else {
            throw new ContextualError("Unrecognised operator "+operator, getLocation());
        }
        this.setType(returnType);
        return returnType;
    }

	protected DVal codePreGen(DecacCompiler compiler) {
		return null;
	}



}
