import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AirflowApiClient {
    private static final String AIRFLOW_API_URL = "http://localhost:8080/api/v1";
    private static final String AIRFLOW_DAG_RUN_URL = AIRFLOW_API_URL + "/dags/%s/dagRuns"; // Trigger DAG run URL

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
//ObjectMapper：用于解析 API 返回的 JSON 数据。
    public AirflowApiClient() {
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    // 获取所有 DAG
    //getAllDags()：使用 GET 请求从 Airflow API 获取所有 DAG 的信息。该方法将返回 Airflow 中已定义的所有 DAG 的列表。
    public void getAllDags() throws IOException {
        HttpGet request = new HttpGet(AIRFLOW_API_URL + "/dags");
        HttpResponse response = httpClient.execute(request);

        String responseJson = EntityUtils.toString(response.getEntity());
        JsonNode jsonResponse = objectMapper.readTree(responseJson);

        System.out.println("DAGs: " + jsonResponse.toString());
    }

    // 触发某个 DAG 运行
    //triggerDagRun():使用 POST 请求触发特定 DAG 的运行。通过传递 DAG ID，API 将触发该 DAG 并返回 DAG 运行的状态。
    public void triggerDagRun(String dagId) throws IOException {
        String url = String.format(AIRFLOW_DAG_RUN_URL, dagId);
        HttpPost request = new HttpPost(url);

        HttpResponse response = httpClient.execute(request);
        String responseJson = EntityUtils.toString(response.getEntity());
        JsonNode jsonResponse = objectMapper.readTree(responseJson);

        System.out.println("Triggered DAG Run: " + jsonResponse.toString());
    }

    // 关闭 HTTP 客户端
    public void close() throws IOException {
        httpClient.close();
    }

    //运行 AirflowApiClient 的 main() 方法，
    // 这将调用 Airflow API，输出 DAG 列表并触发特定的 DAG 运行。
    public static void main(String[] args) {
        AirflowApiClient client = new AirflowApiClient();

        try {
            // 获取所有 DAGs
            client.getAllDags();

            // 触发指定的 DAG
            client.triggerDagRun("airflow_api_dag");  // 使用实际的 DAG ID

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
