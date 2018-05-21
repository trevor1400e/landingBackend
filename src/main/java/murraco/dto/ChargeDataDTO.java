package murraco.dto;

import io.swagger.annotations.ApiModelProperty;

public class ChargeDataDTO {
  
  @ApiModelProperty(position = 0)
  private String chargetoken;
  @ApiModelProperty(position = 1)
  private String email;


  public String getChargetoken() {
    return chargetoken;
  }

  public void setChargetoken(String chargetoken) {
    this.chargetoken = chargetoken;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
