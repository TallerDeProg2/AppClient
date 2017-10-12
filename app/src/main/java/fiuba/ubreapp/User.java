package fiuba.ubreapp;

/**Datos basicos de un usuario.*/
class User {
    protected String name;
    protected String lastname;
    protected String username;
    protected String country;
    protected String email;
    protected String password;
    protected String birthdate;
    protected String images;
    protected String id;

    User(String username,String name,String lastname,String password){
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = "";
        this.country = "";
        this.images = "";
        this.birthdate = "";
    }

    void setName(String name) { this.name = name; }

    void setLastname(String lastname) { this.lastname = lastname; }

    void setEmail(String email){
        this.email = email;
    }

    void setCountry(String country){
        this.country = country;
    }

    void setImages(String images){
        this.images = images;
    }

    void setBirthdate(String birthdate){
        this.birthdate = birthdate;
    }

    void setId(String id) { this.id = id; }

    String getUsername(){ return this.username;}
    String getName(){ return this.name;}
    String getLastName(){ return this.lastname;}
    String getEmail(){ return this.email;}
    String getPassword(){ return this.password;}
    String getCountry(){ return this.country;}
    String getImages(){ return this.images;}
    String getBirthdate(){ return this.birthdate;}
    String getId(){ return this.id; }

}


