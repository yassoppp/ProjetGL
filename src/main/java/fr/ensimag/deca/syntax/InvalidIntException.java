package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Class to handle the exception of an int greater than 2^32.
 *
 * @author gl59
 * @date 01/01/2022
 */

public class InvalidIntException extends DecaRecognitionException {

    public InvalidIntException(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "Integer's value must be lower than 2^32 to fit in 32 bits.";
    }
}
