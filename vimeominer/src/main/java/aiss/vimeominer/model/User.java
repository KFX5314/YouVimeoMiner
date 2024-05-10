
package aiss.vimeominer.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @NotNull(message = "Uri cannot be null")
    @JsonProperty("uri")
    private String uri;
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("link")
    private String link;
    @JsonProperty("picture_link")
    private String picture_link;

    @JsonProperty("id")
    public String getId() {
        if(uri == null){
            System.out.println("Uri de user null");
        }
        assertNotNull(uri);
        return id = uri.trim().split("/")[2];
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    public String getUser_link() {
        return link;
    }

    public void setUser_link(String user_link) {
        this.link = user_link;
    }

    public String getPicture_link() {
        return picture_link;
    }

    public void setPicture_link(String picture_link) {
        this.picture_link = picture_link;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", user_link='" + link + '\'' +
                ", picture_link='" + picture_link + '\'' +
                '}';
    }

}
