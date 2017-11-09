package fiuba.ubreapp;

/**Clase que tiene los datos de LogIn para enviar a la REST API.*/
class UserLogIn {

    private String username;
    private String password;
    private FbData fb;

    UserLogIn(String username,String password,String fbid,String fbtoken){
        this.username = username;
        this.password = password;
        this.fb = new FbData();
        this.fb.setUserID(fbid);
        this.fb.setAuthToken(fbtoken);
    }

    void SetUsername(String username){
        this.username = username;
    }
    void SetPassword(String password){
        this.password = password;
    }
    void SetFbID(String fbid){
        this.fb.setUserID(fbid);
    }
    void SetFbToken(String token){
        this.fb.setAuthToken(token);
    }

    String GetUsername(){
        return this.username;
    }
    String GetPassword(){
        return this.password;
    }
    String GetFbToken(){
        return this.fb.getAuthToken();
    }
    String GetFbID(){
        return this.fb.getUserID();
    }
}
