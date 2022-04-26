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


    @Test
    public  void H3() throws IOException, PentagonEncounteredException {
            H3Core h3Core =H3Core.newInstance();
            List<GeoCoord> list =new ArrayList<GeoCoord>() {{
                add(new GeoCoord(40.816700,-73.963400));
                add(new GeoCoord(40.799000,-73.917400));
                add(new GeoCoord(40.710600,-74.039300));
                add(new GeoCoord(40.692400,-73.993600));
            }};
        List<Long> polyfill = h3Core.polyfill(list, null, 10);
        List<List<List<GeoCoord>>> lists = h3Core.h3SetToMultiPolygon(polyfill, true);
        polyfill.forEach(System.out::println);


    }
}
