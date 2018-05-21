package murraco.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Theme {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Size(min = 4, max = 255, message = "Minimum username length: 4 characters")
  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String themename;

  @Size(min = 3, max = 255)
  @Column(unique = true, nullable = false)
  private String uniquename;

  @Size(max = 5000)
  @Column
  private String data;

  @Column
  private Integer emailcount;

  @Column
  private Integer pageviews;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getThemename() {
    return themename;
  }

  public void setThemename(String themename) {
    this.themename = themename;
  }

  public String getUniquename() {
    return uniquename;
  }

  public void setUniquename(String uniquename) {
    this.uniquename = uniquename;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public Integer getEmailcount() {
    return emailcount;
  }

  public void setEmailcount(Integer emailcount) {
    this.emailcount = emailcount;
  }

  public void addEmail(Integer email) {
    this.emailcount += email;
  }

  public Integer getPageviews() {
    return pageviews;
  }

  public void setPageviews(Integer pageviews) {
    this.pageviews = pageviews;
  }

  public void addView(Integer view) {
    this.pageviews += view;
  }
}

