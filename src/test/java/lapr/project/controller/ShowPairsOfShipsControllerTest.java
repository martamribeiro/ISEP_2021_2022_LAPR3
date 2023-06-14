package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.dto.ShipsFileDTO;
import lapr.project.utils.ShipsFileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ShowPairsOfShipsControllerTest {

    private Company comp;
    private List<ShipsFileDTO> shipsOfFile;
    private File fileTest;
    private ImportShipsController ctrl;

    @BeforeEach
    public void setUp() throws Exception {
        comp = new Company("Shipping company");
        this.shipsOfFile = Collections.emptyList();
        fileTest = new File("data-ships&ports/bships.csv");
        this.ctrl = new ImportShipsController(comp);
    }


    @Test
    void getPairsOfShips() throws IOException {
        ImportShipsController importCtrl = new ImportShipsController(comp);
        ShipsFileUtils shipsFileUtils = new ShipsFileUtils();
        shipsOfFile = shipsFileUtils.getShipsDataToDto(fileTest.toString());
        List<ShipsFileDTO> addedShips = new ArrayList<>();

        for (int i = 0; i < shipsOfFile.size(); i++) {
            if (ctrl.importShipFromFile(shipsOfFile.get(i)))
                //System.out.println(shipsOfFile.get(i));
                addedShips.add(shipsOfFile.get(i));
        }

        ShowPairsOfShipsController ctrl = new ShowPairsOfShipsController(comp);
        ctrl.getPairsOfShips();
    }




}