package ru.fizteh.fivt.students.DariaZvereva.TwitterStream;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by Dasha on 18.12.2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class GoogleGeoLocationTest extends TestCase {
    static final double LATITUDEMoscow = 55.755826;
    static final double LONGTITUDEMoscow = 37.6173;
    static final double LATITUDEOrsk = 51.20487;
    static final double LONGTITUDEOrsk = 58.56685;

    @Test
    public void GeoLocationTest() throws Exception {
        GoogleGeoLocation location;

        location = new GoogleGeoLocation("Moscow");
        assert (LATITUDEMoscow ==
                Math.rint(location.getLocation().lat * 1000000.0)
                        / 1000000.0);
        assert (LONGTITUDEMoscow ==
                Math.rint(location.getLocation().lng * 1000000.0)
                        / 1000000.0);
    }
}