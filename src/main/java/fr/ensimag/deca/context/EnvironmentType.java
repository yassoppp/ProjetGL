package fr.ensimag.deca.context;

import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.util.HashMap;
import java.util.Map;

/**
 * Dictionary associating identifier's TypeDefinition to their names.
 * 
 * This is actually a linked list of dictionaries: each EnvironmentType has a
 * pointer to a parentEnvironment, corresponding to superblock (eg superclass).
 * 
 * The dictionary at the head of this list thus corresponds to the "current" 
 * block (eg class).
 * 
 * Searching a definition (through method get) is done in the "current" 
 * dictionary and in the parentEnvironment if it fails. 
 * 
 * Insertion (through method declare) is always done in the "current" dictionary.
 * 
 * @author gl59
 * @date 01/01/2022
 */
public class EnvironmentType {
    EnvironmentType parentEnvironment;
    private Map< Symbol, TypeDefinition> environment = new HashMap< Symbol, TypeDefinition>();
    
    public EnvironmentType(EnvironmentType parentEnvironment) {
        this.parentEnvironment = parentEnvironment;
    }
 
    public static class DoubleDefException extends Exception {
        private static final long serialVersionUID = -2733379901827316441L;
    }
    
    /** 
     * assign_compatible(env, T1, T2) 
     * Return true if the assignment of a value of type 2 to a variable 
     * of type 1 is compatible
     */
    public static boolean assignCompatible(Type type1, Type type2) {
        if (type1.sameType(type2)) {
            return true;
        }
        else if (type1.isFloat() && type2.isInt()) {
            return true;
        }
        else {
            return false;
        }
    }
    
    /** 
     * cast_compatible(env, T1, T2)
     * Return true if the cast of type1 to a value of type2 is compatible
     */
    public static boolean castCompatible(Type type1, Type type2) {
        if (type1.isVoid()) {
            return false;
        } else {
            return assignCompatible(type1, type2) || assignCompatible(type2, type1);
        }
    }
    
    public boolean subType(Type type1, Type type2) {
        boolean result = false;
        if (type1.sameType(type2)) {
            result = true;
        } else if (type1.isClass() && type2.isClass()
                && ((ClassType)type1).isSubClassOf((ClassType)type2)) {
            result = true;
        } else if (type1.isNull() && type2.isClass()) {
            result = true;
        }
        return result;
    }
    
    /**
     * Return the definition of the symbol in the environment, or null if the
     * symbol is undefined.
     */
    public TypeDefinition get(Symbol key) {
        TypeDefinition definition = environment.get(key);
        if (definition != null) return definition;
        if (parentEnvironment == null) {
            return null;
        } else {
            return parentEnvironment.get(key);
        }
    }
    
    /**
     * Add the definition def associated to the symbol name in the environment.
     * 
     * Adding a symbol which is already defined in the environment,
     * - throws DoubleDefException if the symbol is in the "current" dictionary 
     * - or, hides the previous declaration otherwise.
     * 
     * @param name
     *            Name of the symbol to define
     * @param def
     *            Definition of the symbol
     * @throws DoubleDefException
     *             if the symbol is already defined at the "current" dictionary
     *
     */
    public void declare(Symbol name, TypeDefinition def) throws DoubleDefException {
        if (environment.containsKey(name)) {
            throw new DoubleDefException();
        } else {
            environment.put(name, def);
        }
    }
}
