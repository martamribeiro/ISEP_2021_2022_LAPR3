package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;
import lapr.project.dto.ShipsFileDTO;
import lapr.project.utils.ShipsFileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImportShipsControllerTest {

    private Company comp;
    private List<ShipsFileDTO> shipsOfFile, shipsOfFileExp;
    private File fileTest, expFileTest;
    private ImportShipsController ctrl;

    @BeforeEach
    public void SetUp() {
        comp = new Company("Company");
        this.shipsOfFile = Collections.emptyList();
        this.shipsOfFileExp = Collections.emptyList();
        fileTest = new File("data-ships&ports/testFile.csv");
        expFileTest = new File("data-ships&ports/expImpTestFile.csv");
        this.ctrl = new ImportShipsController(comp);
    }

    /**
     * Test to ensure that the ships are imported correctly.
     */
    @Test
    public void testCorrectlyImportedFromExistingFile() {
        ShipsFileUtils shipsFileUtils = new ShipsFileUtils();
        shipsOfFile = shipsFileUtils.getShipsDataToDto(fileTest.toString());
        List<Ship> addedShips = new ArrayList<>();
        for (int i = 0; i < shipsOfFile.size(); i++) {
            if (ctrl.importShipFromFile(shipsOfFile.get(i)))
                addedShips.add(this.comp.getShipStore().createShip(shipsOfFile.get(i)));
        }
        shipsOfFileExp = shipsFileUtils.getShipsDataToDto(expFileTest.toString());
        List<Ship> expAddedShips = new ArrayList<>();
        for (int i = 0; i < shipsOfFileExp.size(); i++) {
            expAddedShips.add(this.comp.getShipStore().createShip(shipsOfFileExp.get(i)));
        }
        Assert.assertEquals(expAddedShips, addedShips);
    }

    @Test
    public void testImpShip(){
        ShipsFileUtils shipsFileUtils = new ShipsFileUtils();
        int j=0;
        try {
            shipsOfFile = shipsFileUtils.getShipsDataToDto(fileTest.toString());
            for (int i = 0; i < shipsOfFile.size(); i++) {
                if (!ctrl.importShipFromFile(shipsOfFile.get(i))) {
                    System.out.println("DIDN'T IMPORT LINE " + i + "\n");
                } else {
                    j++;
                }
            }
        } catch (IllegalArgumentException e){
            System.out.println("NOT ADDED : " + e);
        }
        System.out.println("TOTAL IMPORTED: " + j + "\n");
    }

}