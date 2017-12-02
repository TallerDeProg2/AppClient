package fiuba.ubreapp;

/**Response with Error Code*/

class ErrorResponse {
    private String message;
    private String status;

    ErrorResponse(){
        message = "";
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getMessage(){
        return this.message;
    }

    public  String getStatus(){
        return this.status;
    }

}
