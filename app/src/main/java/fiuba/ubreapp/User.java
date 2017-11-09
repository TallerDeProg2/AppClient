package fiuba.ubreapp;

/**Datos basicos de un usuario.*/
class User {
    protected String username;
    protected String password;
    protected FbData fb;
    protected String firstname;
    protected String lastname;
    protected String country;
    protected String email;
    protected String birthdate;
    protected String type;
    protected String id;

    User(String username,String name,String lastname,String password, String type){
        this.username = username;
        this.password = password;
        this.fb = new FbData();
        this.firstname = name;
        this.lastname = lastname;
        this.type = type;
        this.id = "";
        this.email = "";
        this.country = "";
        this.birthdate = "";
    }
    void setUsername(String username) { this.username = username; }

    void setPassword(String password) { this.password = password; }

    void setFirstname(String name) { this.firstname = name; }

    void setLastname(String lastname) { this.lastname = lastname; }

    void setEmail(String email){
        this.email = email;
    }

    void setCountry(String country){
        this.country = country;
    }

    void setfbuserID(String userID){
        this.fb.setUserID(userID);
    }

    void setfbauthToken(String authToken){
        this.fb.setAuthToken(authToken);
    }

    void setBirthdate(String birthdate){
        this.birthdate = birthdate;
    }

    void setId(String id) { this.id = id; }

    void setType(String type){ this.type = type; }

    String getUsername(){ return this.username;}
    String getFirstname(){ return this.firstname;}
    String getLastName(){ return this.lastname;}
    String getEmail(){ return this.email;}
    String getPassword(){ return this.password;}
    String getCountry(){ return this.country;}
    String getBirthdate(){ return this.birthdate;}
    String getId(){ return this.id; }
    String getType(){ return this.type; }
    String getfbuserID(){
        return this.fb.getUserID();
    }
    String getfbauthToken(){
        return this.fb.getAuthToken();
    }

}


