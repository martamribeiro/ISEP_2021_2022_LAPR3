package lapr.project.controller;

import lapr.project.domain.model.Company;
import lapr.project.domain.model.Port;
import lapr.project.dto.PortFileDTO;
import lapr.project.utils.PortsFileUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ImportPortsControllerTest {
    private Company comp;
    private List<PortFileDTO> portsOfFile, portsOfFileExp;
    private File fileTest, expFileTest;
    private ImportPortsController ctrl;

    @BeforeEach
    public void SetUp() {
        comp = new Company("Company");
        this.portsOfFile = Collections.emptyList();
        this.portsOfFileExp = Collections.emptyList();
        fileTest = new File("data-ships&ports/bportsTest.csv");
        expFileTest = new File("data-ships&ports/bportsTestExp.csv");
        this.ctrl = new ImportPortsController(comp);
    }

    @Test
    void importPortFromFile() {
        PortsFileUtils portsFileUtils = new PortsFileUtils();
        portsOfFile = portsFileUtils.getPortsDataToDto(fileTest.toString());

        for (int i = 0; i < portsOfFile.size(); i++) {
            ctrl.importPortFromFile(portsOfFile.get(i));
        }

        List<Port> expAddedPorts = new ArrayList<>();

        Port port1 = new Port(10563,"Copenhagen","Europe","Denmark",55.7,12.61666667);
        Port port2 = new Port(10138, "Marsaxlokk", "Europe", "Malta", 35.84194, 14.54306);
        Port port3 = new Port(10860, "Matarani", "America", "Peru", -17.0, -72.1);
        Port port4 = new Port(10358, "Aarhus", "Europe", "Denmark", 56.15, 10.21666667);

        expAddedPorts.add(port1);
        expAddedPorts.add(port2);
        expAddedPorts.add(port3);
        expAddedPorts.add(port4);

        ctrl.balancePorts2DTree();
        List<Port> listOfBalancedPorts = this.comp.getPortStore().getPorts2DTree().getAll();

        Assert.assertEquals(expAddedPorts, listOfBalancedPorts);
    }




    @Test
    void ensureCorrectNodesAreAdded() {
        PortsFileUtils portsFileUtils = new PortsFileUtils();
        portsOfFile = portsFileUtils.getPortsDataToDto(fileTest.toString());
        List<Port> addedPorts = new ArrayList<>();

        for (int i = 0; i < portsOfFile.size(); i++) {
            if (ctrl.importPortFromFile(portsOfFile.get(i))) {
                addedPorts.add(this.comp.getPortStore().createPort(portsOfFile.get(i)));
            }
        }
        assertEquals(addedPorts.size(), this.comp.getPortStore().getPorts2DTree().getListOfPortNodes().size());
    }
}