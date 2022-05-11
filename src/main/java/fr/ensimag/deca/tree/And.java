package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl59
 * @date 01/01/2022
 */
public class And extends AbstractOpBool {
	private static int i=0;
	private Label labelfalse=new Label("LabelFalseAnd"+i);
	private Label labelend=new Label("LabelFinAnd"+i);
	private void labelstate() {
		i++;
		labelfalse=new Label("LabelFalseAnd"+i);
		labelend=new Label("LabelFinAnd"+i);
	}

    

	public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler,Label label) {
    	DVal reg=codePreGen(compiler);
    	compiler.addInstruction(new CMP(1,(GPRegister)reg));
        compiler.addInstruction(new BNE(label));
    }


	@Override
	protected DVal codePreGen(DecacCompiler compiler) {
		labelstate();
		this.getLeftOperand().codeGenInst(compiler,labelfalse);
		this.getRightOperand().codeGenInst(compiler,labelfalse);
		GPRegister reg=compiler.getRegsManager().getReg();
		compiler.addInstruction(new LOAD(1, reg));
		compiler.addInstruction(new BRA(labelend));
		compiler.addLabel(labelfalse);
		compiler.addInstruction(new LOAD(0, reg));
		compiler.addLabel(labelend);
		return reg;
	}

	public Label getLabelfalse() {
		return labelfalse;
	}

	public void setLabelfalse(Label labelfalse) {
		this.labelfalse = labelfalse;
	}

	public Label getLabelend() {
		return labelend;
	}

	public void setLabelend(Label labelend) {
		this.labelend = labelend;
	}


}
