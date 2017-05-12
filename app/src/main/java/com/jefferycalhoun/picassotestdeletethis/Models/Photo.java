package com.jefferycalhoun.picassotestdeletethis.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jeff on 4/22/17.
 */


public class Photo {
  private String id;
  private String owner;
  private String secret;
  private String server;
  private String title;
  private int farm;


  public Photo(JSONObject json){

    try{
      this.id = json.getString("id");
      this.owner = json.getString("owner");
      this.secret = json.getString("secret");
      this.server = json.getString("server");
      this.title = json.getString("title");
      this.farm = json.getInt("farm");
    }
    catch(JSONException e){
      e.printStackTrace();
    }
  }

  public Photo(String id, String owner, String secret, String server, String title, int farm) {
    this.id = id;
    this.owner = owner;
    this.secret = secret;
    this.server = server;
    this.title = title;
    this.farm = farm;
  }

  public Photo() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

  public String getServer() {
    return server;
  }

  public void setServer(String server) {
    this.server = server;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public int getFarm() {
    return farm;
  }

  public void setFarm(int farm) {
    this.farm = farm;
  }


  /**
   * Generates a flickr-valid url from photo data
   * @return
   */
  public String getUrlString(){
    StringBuilder builder = new StringBuilder();
    builder.append("http://farm");
    builder.append(farm);
    builder.append(".staticflickr.com/");
    builder.append(server);
    builder.append("/");
    builder.append(id);
    builder.append("_");
    builder.append(secret);
    builder.append(".jpg");

    return builder.toString();
  }
}
