package Result;
import java.util.ArrayList;
import java.util.List;

public class EncResultStorage {
    private List<EncResult> encResultList;

    public EncResultStorage() {
        this.encResultList = new ArrayList<>();
    }

    public void addEncResult(EncResult encResult) {
        encResultList.add(encResult);
    }
    public List<EncResult> getEncResults() {
        return encResultList;
    }

}
