package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Class to handle the exception of an Nan value for float.
 *
 * @author gl59
 * @date 01/01/2022
 */
public class SmallFloatException extends DecaRecognitionException {
    
    public SmallFloatException(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "Literal values cannot be Nan.";
    }
}
