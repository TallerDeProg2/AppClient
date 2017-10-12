package fiuba.ubreapp;


/**Clase utilizada para enviar y obtener datos de las clases que se comunican con la REST API.*/
class Info {
    private String info;
    private int status;

    Info(){
        this.info = "";
        this.status = 0;
    }

    void setInfo(String json){
        this.info = json;
    }

    void setStatus(int status){
        this.status = status;
    }

    String getInfo(){
        return this.info;
    }

    int getStatus(){
        return this.status;
    }
}
