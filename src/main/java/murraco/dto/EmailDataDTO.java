package murraco.dto;

import io.swagger.annotations.ApiModelProperty;
import murraco.model.Role;

import java.util.Arrays;
import java.util.List;

public class EmailDataDTO {
  
  @ApiModelProperty(position = 0)
  private String uniquename;
  @ApiModelProperty(position = 1)
  private String email;

  public String getUniquename() {
    return uniquename;
  }

  public void setUniquename(String uniquename) {
    this.uniquename = uniquename;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
