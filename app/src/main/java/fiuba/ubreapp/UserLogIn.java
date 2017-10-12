package fiuba.ubreapp;

/**Clase que tiene los datos de LogIn para enviar a la REST API.*/
class UserLogIn {

    private String username;
    private String password;
    private String token;
    private Boolean loginbyface;
    private Boolean loginasdriver;

    UserLogIn(String username,String password,String token){
        this.username = username;
        this.password = password;
        this.token = token;
    }

    void SetUsername(String username){
        this.username = username;
    }
    void SetPassword(String password){
        this.password = password;
    }
    void SetToken(String token){
        this.token = token;
    }

    String GetUsername(){
        return this.username;
    }
    String GetPassword(){
        return this.password;
    }
    String GetToken(){
        return this.token;
    }
}
