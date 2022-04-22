import com.alibaba.fastjson.JSON;
import com.asja.finaldesign.common.dto.GeoJson;
import com.asja.finaldesign.service.H3Service;
import com.uber.h3core.H3Core;
import com.uber.h3core.exceptions.PentagonEncounteredException;
import com.uber.h3core.util.GeoCoord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.Decoder;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class JavaGrammerTest{

    H3Service h3Service;

    @Test
    public  void H3() throws IOException, PentagonEncounteredException {
        GeoJson hexagonsGeoJson = h3Service.getHexagonsGeoJson(100, 20, 13, 8);

        System.out.println(
                JSON.toJSON(hexagonsGeoJson)
        );


    }
}
