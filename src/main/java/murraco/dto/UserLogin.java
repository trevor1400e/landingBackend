package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import murraco.model.Role;

import java.util.Arrays;
import java.util.List;

public class UserLogin {
  
  @ApiModelProperty(position = 0)
  private String username;
  @ApiModelProperty(position = 1)
  private String password;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
