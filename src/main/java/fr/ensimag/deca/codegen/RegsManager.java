package fr.ensimag.deca.codegen;

import java.util.ArrayList;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

public class RegsManager {
	DecacCompiler compiler;
	private int nbVG;
	private int pile;
	private int numparam;
	private int nbr_declLOcale;
	public int getNbr_declLOcale() {
		return nbr_declLOcale;
	}
	public void setNbr_declLOcale(int nbr_declLOcale) {
		this.nbr_declLOcale = nbr_declLOcale;
	}
	private ArrayList<GPRegister> freeRegs;
	private ArrayList<GPRegister> pushedregister;
	public int getNbVG() {
		return nbVG;
	}
	public void setNbVG(int nbVG) {
		this.nbVG = nbVG;
	}
	public ArrayList<GPRegister> getFreeRegs() {
		return freeRegs;
	}
	public void setFreeRegs(ArrayList<GPRegister> freeRegs) {
		this.freeRegs = freeRegs;
	}
	public RegsManager(DecacCompiler compiler) {
		nbVG=0;
		pile=1;
		numparam=0;
		nbr_declLOcale=0;
		this.compiler=compiler;
		pushedregister=new ArrayList<GPRegister>();
		freeRegs=new ArrayList<GPRegister>();
		for(int i=2;i<15;i++) {
			freeRegs.add(Register.getR(i));
		}
		
	}
	public int getNumparam() {
		return numparam;
	}
	public void setNumparam(int numparam) {
		this.numparam = numparam;
	}
	public ArrayList<GPRegister> getPushedregister() {
		return pushedregister;
	}
	public void setPushedregister(ArrayList<GPRegister> pushedregister) {
		this.pushedregister = pushedregister;
	}
	public int getPile() {
		return pile;
	}
	public void setPile(int pile) {
		this.pile = pile;
	}
	public void freeReg(DVal reg) {
		if(reg instanceof GPRegister) {
		freeRegs.add(0,(GPRegister)reg);
		}
	}
	public GPRegister getReg() {
		GPRegister result= freeRegs.remove(0);
		return result;
	}
	public void ajoutpile() {
		this.pile++;
	}
	 public void pushall(){
	   for(GPRegister reg: pushedregister){
	      compiler.addFirst(new PUSH(reg));
	   }
	 }
	 public void popall(){
		   for(GPRegister reg: pushedregister){
		      compiler.addInstruction(new POP(reg));
		   }
		   pushedregister=new ArrayList<GPRegister>();
	 }
	 public void pushregister(GPRegister reg) {
		 if(!(pushedregister.contains(reg))) {
		 pushedregister.add(reg);
		 }
	 }
	 public void freeall() {
		 freeRegs=new ArrayList<GPRegister>();
		 for(int i=2;i<15;i++) {
				freeRegs.add(Register.getR(i));
		}
	 }
	 public void newPushstate() {
		pushedregister=new ArrayList<GPRegister>();
	 }
	

}