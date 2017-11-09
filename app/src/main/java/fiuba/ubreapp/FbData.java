package fiuba.ubreapp;

/** Datos de Facebook */

class FbData {
    protected String userID;
    protected String authToken;

    FbData(){
        this.userID = "";
        this.authToken = "";
    }

    void setUserID(String userID){
        this.userID = userID;
    }

    void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    String getUserID(){
        return this.userID;
    }

    String getAuthToken(){
        return this.authToken;
    }

}
