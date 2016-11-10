package bean;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User{
  private String gender;
  private Name name;
  private byte[] userImage;
  private boolean verified;

  public String getGender(){
    return this.gender;
  }
  public void setGender(String gender){
    this.gender = gender;
  }
  public Name getName(){
    return this.name;
  }
  public void setName(Name name){
    this.name = name;
  }
  public byte[] getUserImage(){
    return this.userImage;
  }
  public void setUserImage(byte[] userImage){
    this.userImage = userImage;
  }
  public boolean getVerified(){
    return this.verified;
  }
  public void setVerified(boolean verified){
    this.verified = verified;
  }
}

