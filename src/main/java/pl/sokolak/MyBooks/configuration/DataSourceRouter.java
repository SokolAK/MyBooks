package pl.sokolak.MyBooks.configuration;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

// This is a DataSource.
public class DataSourceRouter extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        String keyDS = "main";
        //System.out.println("KeyDS=" + keyDS);

        if (keyDS == null) {
            keyDS = "main";
        }

        return keyDS;
    }

    public void initDataSources(DataSource dataSource1, DataSource dataSource2) {
        Map<Object, Object> dsMap = new HashMap<Object, Object>();
        dsMap.put("main", dataSource1);
        dsMap.put("test", dataSource2);

        this.setTargetDataSources(dsMap);
    }
}
