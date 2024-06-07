import org.example.MyHttp;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class MyHttpTest {

    // https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst?serviceKey=wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D&pageNo=1&numOfRows=1000&dataType=json&base_date=20240607&base_time=1200&nx=55&ny=127
    @Test
    public void get_test() throws IOException, InterruptedException {
        String uri = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String serviceKey = "wJmmW29e3AEUjwLioQR22CpmqS645ep4S8TSlqtSbEsxvnkZFoNe7YG1weEWQHYZ229eNLidnI2Yt5EZ3Stv7g%3D%3D";
        String baseDate = "20240607";
        String baseTime = "1200";
        Integer nx = 55;
        Integer ny = 127;


        String responseBody = MyHttp.get(uri, serviceKey, baseDate, baseTime, nx, ny);

    }
}