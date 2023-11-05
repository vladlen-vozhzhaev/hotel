import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;

public class Main {
    public static void main(String[] args) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -3);
            int month = calendar.get(Calendar.MONTH)+1;
            String monthStr = String.valueOf(month);
            if(month<10) monthStr = "0"+monthStr;
            String yesterday = calendar.get(Calendar.YEAR)+"-"+monthStr+"-"+calendar.get(Calendar.DAY_OF_MONTH);

            double todayRUB = getRub(null);
            double yesterdayRUB  = getRub(yesterday);
            System.out.println("Курс на сегодня: "+todayRUB);
            System.out.println("Курс на (вчера): "+yesterdayRUB);
            if(todayRUB>yesterdayRUB){
                System.out.println(getGifURL("rich"));
            }else{
                System.out.println(getGifURL("broke"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    static double getRub(String date) throws Exception{
        String appKey = "346c770e8eb6432ebc9c635342d5b7e9";
        String endPoint = "latest.json";
        if(date != null){
            endPoint = "historical/"+date+".json";
        }
        String spec = "https://openexchangerates.org/api/"+endPoint+"?app_id="+appKey;
        URL url = new URL(spec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null){
            result.append(line);
        }
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
        String rates = jsonObject.get("rates").toString();
        jsonObject = (JSONObject) jsonParser.parse(rates);
        double rub = Double.parseDouble(jsonObject.get("RUB").toString());
        return rub;
    }

    static String getGifURL(String q){
        Random random = new Random();
        int rand = (random.nextInt(24));
        String imageURL = null;
        try {
            URL url = new URL("https://api.giphy.com/v1/gifs/search?api_key=yogxacUoXdKZX0wkD0depV72uwdbz3gj&q="+q+"&limit=25");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                result.append(line);
            }
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONArray jsonArray = (JSONArray) jsonObject.get("data");
            jsonObject = (JSONObject) jsonParser.parse(jsonArray.get(rand).toString());
            jsonObject = (JSONObject) jsonParser.parse(jsonObject.get("images").toString());
            jsonObject = (JSONObject) jsonParser.parse(jsonObject.get("original").toString());
            imageURL = jsonObject.get("url").toString();
            String imageName = imageURL.split("/")[4];
            imageURL = "https://i.giphy.com/media/"+imageName+"/giphy.gif";
            //https://media3.giphy.com/media/LdOyjZ7io5Msw/giphy.gif?cid=af03a50bym4k541n5vjuys9jt8oana73qd3vlok67876d7bm&ep=v1_gifs_search&rid=giphy.gif&ct=g
            //https://i.giphy.com/media/LdOyjZ7io5Msw/giphy.gif
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return imageURL;
    }
}
