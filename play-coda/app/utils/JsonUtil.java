package utils;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;

/**
 * Utilities for creating json.
 */
public final class JsonUtil {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    static final String ERROR = "error";
    static final String SUCCESS = "success";
    static final String HTML_BY_SELECTOR = "htmlBySelector";

    private JsonUtil() {
    }

    /**
     * Return base json for successful ajax calls.
     * 
     * @return the ObjectNode containing the basis for successful answers
     */
    public static ObjectNode successMessage() {
        final ObjectNode ret = Json.newObject();
        ret.put(SUCCESS, true);
        return ret;
    }

    public static ObjectNode successMessageWithHtml(Map<String, String> htmlBySelector) {
        final ObjectNode successMessage = successMessage();
        successMessage.put(HTML_BY_SELECTOR, MAPPER.valueToTree(htmlBySelector));
        return successMessage;
    }
}
