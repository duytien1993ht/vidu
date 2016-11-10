package controller;

import java.io.IOException;
import java.net.URL;

//the old namespace mentioned in the original tutorial is outdated
//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import bean.User;

public class UserTest {
public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
 URL jsonUrl = new URL("https://raw.githubusercontent.com/coolaj86/json-examples/master/java/jackson/user.json");
 User user = null;

 ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

 // IMPORTANT
 // without this option set adding new fields breaks old code
 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

 user = mapper.readValue(jsonUrl, User.class);
 System.out.println(user.getName().getLast());
 System.out.println(user.getName().getFirst());
 System.out.println(user.getUserImage());
}
}

