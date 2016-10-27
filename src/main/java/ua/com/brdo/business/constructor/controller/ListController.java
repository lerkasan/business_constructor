package ua.com.brdo.business.constructor.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.brdo.business.constructor.Ajax;
import ua.com.brdo.business.constructor.model.dto.UserDTO;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ListController {

    private static Map<String, UserDTO> response;
    private static int counter = 3;

    static {
        response = new HashMap<>();
        response.put("user_1", new UserDTO("John", "Doe", "john.doe@mail.com"));
        response.put("user_2", new UserDTO("Rob", "Williams", "rob.williams@mail.com"));
    }


    @ResponseBody
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getList() {
        return ResponseEntity.status(HttpStatus.OK).body(Ajax.successResponse(response));
    }

    @ResponseBody
    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postList(@RequestBody(required = false) String newUserJson) {
        JsonParser parser = new JsonParser();
        JsonObject rootObj = parser.parse(newUserJson).getAsJsonObject();
        response.put("user_"+counter++, new UserDTO(rootObj.get("firstName").getAsString(), rootObj.get("lastName").getAsString(), rootObj.get("email").getAsString()));
        return ResponseEntity.status(HttpStatus.OK).body(Ajax.successResponse(response));
    }
}
