package bean;
import java.io.File;
import java.io.IOException;
import java.net.URL;

//the old namespace mentioned in the original tutorial is outdated
//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserTest {
public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

 URL jsonUrl = new URL("https://raw.github.com/gist/2481734/bc37d3aa3521cd2645243d68663686e6dcce75bf/user.json");
 String jsonStr = 
   "{\"name\":{\"first\":\"Joe\",\"last\":\"Sixpack\"},\"gender\":\"MALE\",\"verified\":false,\"userImage\":\"Rm9vYmFyIQ==\"}";
 User user = null;

 ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

 // IMPORTANT
 // without this option set adding new fields breaks old code
 mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

 user = mapper.readValue(jsonStr, User.class);
 System.out.println(user.getGender());
}
}

