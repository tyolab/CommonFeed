package au.com.tyo.common.feed;

import java.util.Date;
import java.util.List;

public class Feed {

    protected List list;

    private long lastModifiedDate = new Date().getTime();

    public List getList() {
        return list;
    }

    public long getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setList(List list) {
        this.list = list;
    }
}
