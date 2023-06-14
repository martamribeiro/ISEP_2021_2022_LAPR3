
package lapr.project.genericDataStructures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 *
 * @author DEI-ESINF
 */
public class UtilsTest {
    
    public UtilsTest() {
    }

    /**
     * Test of sortByBST method, of class Utils.
     */
    @Test
    public void testSortByBST() {
        List<String> lStrIni = Arrays.asList("rui","joao","carlos","filipe","berta","ze","cunha");
        List<String> lStrRes = Arrays.asList("berta","carlos","cunha","filipe","joao","rui","ze");

        List<Integer> lIntIni = Arrays.asList(12,4,8,2,4,5,78,1,6);
        List<Integer> lIntRes = Arrays.asList(1,2,4,5,6,8,12,78);
        
        assertEquals(lIntRes, Utils.sortByBST(lIntIni));
        assertEquals(lStrRes, Utils.sortByBST(lStrIni));
    } 
}
