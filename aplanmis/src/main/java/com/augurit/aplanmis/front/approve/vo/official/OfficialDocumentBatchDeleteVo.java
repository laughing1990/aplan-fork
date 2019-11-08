package com.augurit.aplanmis.front.approve.vo.official;

import java.util.ArrayList;
import java.util.List;

public class OfficialDocumentBatchDeleteVo {

    /*待删除批文批复id*/
    private List<String> matinstIds = new ArrayList<>();

    public List<String> getMatinstIds() {
        return matinstIds;
    }

    public void setMatinstIds(List<String> matinstIds) {
        this.matinstIds = matinstIds;
    }
}
