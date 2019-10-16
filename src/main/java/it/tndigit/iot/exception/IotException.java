package it.tndigit.iot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IotException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 285218528627583070L;
    protected String errorCode;
    protected String message;
    protected String classe;
    protected String metodo;
    protected String custom;

    public IotException(){

    }
    public IotException(String message) {
        super();
        this.setMessage(message);
        this.custom="";
        this.metodo="";
        this.classe="";
    }

    public IotException(String errorCode, String message) {
        super();
        this.setMessage(message);
        this.setErrorCode(errorCode);
        this.custom="";
        this.metodo="";
        this.classe="";
    }

    public IotException(String message, String classe, String metodo, String custom) {
        super();
        this.classe =classe;
        this.metodo=metodo;
        this.custom=custom;
        this.setMessage(message);
    }
    public IotException(Exception e, String classe, String metodo, String custom) {
        super(e);
        this.classe =classe;
        this.metodo=metodo;
        this.custom=custom;
        this.setMessage(e.getCause()!= null ? e.getCause().toString() : e.toString());
    }

    public IotException(String errorCode, String message, String classe, String metodo, String custom) {
        super();
        this.errorCode =errorCode;
        this.classe =classe;
        this.metodo=metodo;
        this.custom=custom;
        this.setMessage(message);

    }




    public void setScuException(String message,String classe, String metodo,String custom) {

        this.classe =classe;
        this.metodo=metodo;
        this.custom=custom;
        this.setMessage(message);
//		spedisciMailErrore(this);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getCustom() {
        return custom;
    }
    public void setCustom(String custom) {
        this.custom = custom;
    }
    @Override
    public String toString() {

        return "SCU-ERRORE:"+
                "Messaggio:" + message 	+"n" +
                "Classe:" 	+ classe 	+"n" +
                "Metodo:" 	+ metodo 	+"n" +
                "Custom:" 	+ custom;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}