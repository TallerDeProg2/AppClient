package fiuba.ubreapp;

/** Datos de Facebook */

class FbData {
    protected String userId;
    protected String authToken;

    FbData(){
        this.userId = "";
        this.authToken = "";
    }

    void setUserID(String userID){
        this.userId = userID;
    }

    void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    String getUserID(){
        return this.userId;
    }

    String getAuthToken(){
        return this.authToken;
    }

}
