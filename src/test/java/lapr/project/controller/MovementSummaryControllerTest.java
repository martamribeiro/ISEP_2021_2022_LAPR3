package lapr.project.controller;

import lapr.project.domain.dataStructures.PositionsBST;
import lapr.project.domain.model.Company;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.dto.MovementsSummaryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class MovementSummaryControllerTest {
    private Company comp;
    private int mmsi1,mmsi2, mmsi3, mmsi4;
    private String vesselName;
    private String imo;
    private String callSign;
    private Ship s1, s2, s3, s4;

    int [] mmsiCodes = {333333333, 111111111, 222222222, 123456789};
    double [] lats = {-30.033056, -42.033006, -55.022056, 23.008721};
    double [] lons = {-51.230000, -47.223056, -46.233056, 24.092123};
    double [] sogs = {25.4, 25.8, 31.7, 10.2};
    double [] cogs = {341.2, 330.3, 328.5, 320.9};
    int [] headings = {300, 302, 315, 300};
    String transcieverClass = "AIS";
    Date[] d1 = {new SimpleDateFormat("dd/MM/yyyy").parse("04/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("07/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("10/01/2021"),
            new SimpleDateFormat("dd/MM/yyyy").parse("13/01/2021")} ;

    MovementSummaryControllerTest() throws ParseException {
    }

    @BeforeEach
    public void setUp(){
        comp = new Company("Shipping company");
        PositionsBST positions = new PositionsBST();
        for(int i=0; i<3;i++){
            positions.insert(new ShipPosition(mmsiCodes[0], d1[i], lats[i], lons[i], sogs[i], cogs[i], headings[i], transcieverClass));
        }
        mmsi1 = 123456789;
        mmsi2 = 123456780;
        mmsi3 = 123456788;
        mmsi4 = 123456790;
        vesselName = "VARAMO";
        imo = "IMO9395044";
        callSign = "C4SQ2";
        s1 = new ShipSortMmsi(positions, mmsi1, vesselName, imo, callSign, 70, 294,32,13.6,"79");
        s2 = new ShipSortMmsi(positions, mmsi2, vesselName, imo, callSign, 70, 294,32,13.6,"79");
        s3 = new ShipSortMmsi(positions, mmsi3, vesselName, imo, callSign, 70, 294,32,13.6,"79");
        s4 = new ShipSortMmsi(positions, mmsi4, vesselName, imo, callSign, 70, 294,32,13.6,"79");

        comp.getShipStore().getShipsBstMmsi().insert(s1);
        comp.getShipStore().getShipsBstMmsi().insert(s2);
        comp.getShipStore().getShipsBstMmsi().insert(s3);
        comp.getShipStore().getShipsBstMmsi().insert(s4);

    }

    @Test
    public void integrationTestWithController(){
        MovementsSummaryDto expect = new MovementsSummaryDto(vesselName, d1[0], d1[2], s1.getPositionsBST().getMaxSog(), s1.getPositionsBST().getMeanSog(),
                s1.getPositionsBST().getMeanCog(), s1.getPositionsBST().getDepartLatitude(), s1.getPositionsBST().getDepartLongitude(),
                s1.getPositionsBST().getArrivalLatitude(), s1.getPositionsBST().getArrivalLongitude(), s1.getPositionsBST().getTotalDistance(), s1.getPositionsBST().getDeltaDistance(), s1.getMmsi());
        MovementSummaryController controller = new MovementSummaryController(comp);
        MovementsSummaryDto mSummary = controller.getShipMovementsSummary("123456789");
        System.out.println(mSummary.toString());
        Assertions.assertEquals(expect.toString(), mSummary.toString());
    }

    @Test
    public void testSummaryDto(){
       MovementSummaryController controller = new MovementSummaryController(comp);
       MovementsSummaryDto mSummary = controller.getShipMovementsSummary("123456789");
       Assertions.assertEquals(vesselName, mSummary.getName());
        Assertions.assertEquals(d1[0], mSummary.getStartDate());
        Assertions.assertEquals(d1[2], mSummary.getEndDate());
        Assertions.assertEquals(s1.getPositionsBST().getMaxSog(), mSummary.getMaxSog());
        Assertions.assertEquals(s1.getPositionsBST().getMeanSog(), mSummary.getMeanSog());
        Assertions.assertEquals(s1.getPositionsBST().getMeanCog(), mSummary.getMeanCog());
        Assertions.assertEquals(s1.getPositionsBST().getDepartLatitude(), mSummary.getDepartLatitude());
        Assertions.assertEquals(s1.getPositionsBST().getDepartLongitude(), mSummary.getDepartLongitude());
        Assertions.assertEquals(s1.getPositionsBST().getArrivalLatitude(), mSummary.getArrivalLatitude());
        Assertions.assertEquals(s1.getPositionsBST().getArrivalLongitude(), mSummary.getArrivalLongitude());
        Assertions.assertEquals(s1.getPositionsBST().getTotalDistance(), mSummary.getTravelledDist());
        Assertions.assertEquals(s1.getPositionsBST().getDeltaDistance(), mSummary.getDeltaDist());


    }

}