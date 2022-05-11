package fr.ensimag.deca.tree;

/**
 * Function that takes a tree as argument.
 * 
 * @see fr.ensimag.deca.tree.Tree#iter(TreeFunction)
 * 
 * @author gl59
 * @date 01/01/2022
 */
public interface TreeFunction {
    void apply(Tree t);
}
