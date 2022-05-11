package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl59
 * @date 01/01/2022
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Signature)) {
            return false;
        }
        Signature o = (Signature) other;
        if (this.size() != o.size()) {
            return false;
        }
        boolean result = true;
        int i = 0;
        for (Type type : args) {
            if (!type.sameType(o.paramNumber(i))) {
                result = false;
                break;
            }
            i++;
        }
            return result;
    }
}
