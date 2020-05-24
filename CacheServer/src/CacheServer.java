import java.util.List;

public class CacheServer {

    public static void main(String[] args) {
        MyDao md = new MyDao();
        List<USModel> data = md.queryAll();
        System.out.println(data.size());
        USModel model1 = new USModel();
        model1.setUrl("www.baidu.com");
        model1.setService_id("dfjldejfdlsjdflajdflasjflajflfjldfj0f");
        md.insert(model1);
        List<USModel> data2 = md.queryAll();
        System.out.println(data2.size());
        System.out.println(data2.toString());
    }
}
