package com.core.common;

@SuppressWarnings("serial")
public class GenericRuntimeException extends RuntimeException {
    protected String _code = "NO_ERROR_CODE";
    
    public GenericRuntimeException(Throwable throwable) {
        super(throwable);
    }

    public GenericRuntimeException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public GenericRuntimeException(String string) {
        super(string);
    }

    public GenericRuntimeException(String code, String message, Throwable throwable) {        
        super(message, throwable);
        _code = code;
    }
    
    public GenericRuntimeException(String code, String message){
        super(message);
        _code = code;
    }

    
    public GenericRuntimeException() {
        super();
    }
    
    public String getCode(){
        return _code;
    }
}