package org.kpi.fict.pzks.automate.concreteAuto.arifm;

import java.util.Stack;

import org.kpi.fict.pzks.automate.base.ActionsImplPool;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement;
import org.kpi.fict.pzks.automate.concreteAuto.arifm.dto.ResultElement.ResultElementType;
import org.kpi.fict.pzks.automate.exceptions.ParceException;

public class ArifmActionImpl extends ActionsImplPool {
    private StringBuilder buffer;
    private Stack<Integer> bracketStack = new Stack<Integer>();
    
    public ArifmActionImpl() {
        
        // Read next car (do nothing)
        addAction(0, new ActionImpl() {
            public void doAction(int curChar, int curPosition) { }
        });
        
        // Start variable accumulation
        addAction(1, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                buffer = new StringBuilder();
                buffer.append((char) curChar);
            }
        });

        // Save char (-+)
        addAction(2, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                saveSign(curChar);
            }
        });

        // Start constant accumulation
        addAction(3, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                buffer = new StringBuilder();
                buffer.append((char) curChar);
            }
        });

        // Variable accumulation
        addAction(4, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                buffer.append((char) curChar);
            }
        });

        // Pop bracket stack
        addAction(5, new ActionImpl() {
            public void doAction(int curChar, int curPosition) throws ParceException {
                popBracketStack(curPosition);
            }
        });

        // Push bracket stack
        addAction(6, new ActionImpl() {
            public void doAction(int curChar, int curPosition) throws ParceException {
                if (buffer != null && buffer.length() > 0 && buffer.toString().charAt(0) == '-') {
                    resStruct.addElement(
                            new ResultElement(ResultElement.ResultElementType.INVERTOR,
                                    "-"));
                    buffer = new StringBuilder();
                    //throw new ParceException("sign between operation sign and open braket");
                }
                pushBracketStack(curPosition);
            }
        });

        // Constant accumulation
        addAction(7, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                buffer.append((char) curChar);
            }
        });

        // Save constant
        addAction(8, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                saveConstant();
            }
        });

        // Save variable
        addAction(9, new ActionImpl() {
            public void doAction(int curChar, int curPosition) {
                saveVariable();
            }
        });

        // Save variable and pop bracket stack
        addAction(10, new ActionImpl() {
            public void doAction(int curChar, int curPosition) throws ParceException {
                saveVariable();
                popBracketStack(curPosition);
            }
        });

        // Save constant and pop bracket stack
        addAction(11, new ActionImpl() {
            public void doAction(int curChar, int curPosition) throws ParceException {
                saveConstant();
                popBracketStack(curPosition);
            }
        });

        // Save constant and end
        addAction(12, new ActionImpl() {
            public void doAction(int curChar, int curPosition) throws ParceException {
                saveConstant();
                end();
            }
        });

        // Save variable and end
        addAction(13, new ActionImpl() {
            public void doAction(int curChar, int curPosition) throws ParceException {
                saveVariable();
                end();
            }
        });

        // End
        addAction(14, new ActionImpl()  {
            public void doAction(int curChar, int curPosition) throws ParceException {
                end();
            }
        });       
       
        // Save constant and save sign
        addAction(15, new ActionImpl()  {
            public void doAction(int curChar, int curPosition) throws ParceException {
                saveConstant();
                saveSign(curChar);
            }
        });  
        
        // Save variable and save sign
        addAction(16, new ActionImpl()  {
            public void doAction(int curChar, int curPosition) throws ParceException {
                saveVariable();
                saveSign(curChar);
            }
        });  
    }
    
    private void saveVariable() {
        resStruct.addElement(
                new ResultElement(ResultElement.ResultElementType.VARIABLE, 
                        buffer.toString()));
        buffer = null;
    }
    
    private void saveConstant() {
        resStruct.addElement(
                new ResultElement(ResultElement.ResultElementType.CONSTANT, 
                        buffer.toString()));
        buffer = null;
    }
    
    private void pushBracketStack(int curPosition) {
        bracketStack.push(curPosition);
        resStruct.addElement(
                new ResultElement(ResultElement.ResultElementType.OPEN_BRACKET,
                        "("));
    }
    
    private void popBracketStack(int curPosition) throws ParceException {
        if (bracketStack.isEmpty()) {
            throw new ParceException("Excess bracket at position " + curPosition);
        }
        
        bracketStack.pop();
        resStruct.addElement(
                new ResultElement(ResultElement.ResultElementType.CLOSE_BRACKET,
                        ")"));
    }
    
    private void end() throws ParceException {
        if (!bracketStack.isEmpty()) {
            throw new ParceException("Bracket on position " + bracketStack.peek() + " isn't closed.");
        }
    }
    
    private void saveSign(int curChar) {
        resStruct.addElement(
                new ResultElement(
                        ResultElementType.SIGN, 
                        "" + (char) curChar));
    }
}