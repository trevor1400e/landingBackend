package murraco.dto;

import io.swagger.annotations.ApiModelProperty;

public class ThemeDataDTO {
  
  @ApiModelProperty(position = 0)
  private String uniquename;
  @ApiModelProperty(position = 1)
  private String themename;
  @ApiModelProperty(position = 2)
  private String data;

  public String getUniquename() {
    return uniquename;
  }

  public void setUniquename(String uniquename) {
    this.uniquename = uniquename;
  }

  public String getThemename() {
    return themename;
  }

  public void setThemename(String themename) {
    this.themename = themename;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
