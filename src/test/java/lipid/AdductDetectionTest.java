package lipid;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdductDetectionTest {

    @Before
    public void setup() {
        // !! TODO Empty by now,you can create common objects for all tests.
    }


    @Test
    public void shouldDetectAdductBasedOnMzDifference() {

        Peak mH = new Peak(700.500, 100000.0); // [M+H]+
        Peak mNa = new Peak(722.482, 80000.0);  // [M+Na]+
        Lipid lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", LipidType.PC, 34, 1);

        double annotationMZ = 700.49999d;
        double annotationIntensity = 80000.0;
        double annotationRT = 6.5d;
        Annotation annotation = new Annotation(lipid, annotationMZ, annotationIntensity, annotationRT, IoniationMode.POSITIVE, Set.of(mH, mNa));

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());
        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }


    @Test
    public void shouldDetectLossOfWaterAdduct() {
        Peak mh = new Peak(700.500, 90000.0);
        Peak mhH2O = new Peak(682.4894, 70000.0);

        Lipid lipid = new Lipid(1, "PE 36:2", "C41H78NO8P", LipidType.PE, 36, 2);
        Annotation annotation = new Annotation(lipid, mh.getMz(), mh.getIntensity(), 7.5d, IoniationMode.POSITIVE, Set.of(mh, mhH2O));

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }


    @Test
    public void shouldDetectDoublyChargedAdduct() {
        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.754, 85000.0);   // [M+2H]2+

        Lipid lipid = new Lipid(3, "TG 54:3", "C57H104O6", LipidType.PG, 54, 3);
        Annotation annotation = new Annotation(lipid, singlyCharged.getMz(), singlyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));

        assertNotNull("[M+H]+ should be detected", annotation.getAdduct());

        assertEquals( "Adduct inferred from lowest mz in group","[M+H]+", annotation.getAdduct());
    }

}
