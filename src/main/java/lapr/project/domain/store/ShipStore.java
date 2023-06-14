package lapr.project.domain.store;

import lapr.project.data.ShipStoreDB;
import lapr.project.domain.dataStructures.*;
import lapr.project.domain.model.Ship;
import lapr.project.domain.model.ShipPosition;
import lapr.project.domain.model.ShipSortMmsi;
import lapr.project.dto.ShipsFileDTO;
import lapr.project.dto.mapper.PositionMapper;
import org.apache.commons.lang3.StringUtils;

public class ShipStore {

    private ShipTreeMmsi shipsBstMmsi = new ShipTreeMmsi();
    private ShipTreeImo shipBstImo = new ShipTreeImo();
    private ShipTreeCallsign shipBstCallSign = new ShipTreeCallsign();
    private ShipStoreDB shipStoreDatabase = new ShipStoreDB();

    public Ship createShip(ShipsFileDTO shipsFileDTO){
        PositionsBST positionsBST = new PositionsBST();
        PositionMapper positionMapper = new PositionMapper();
        ShipPosition shipPosition = positionMapper.toDomain(shipsFileDTO.getPositionDTO(), shipsFileDTO.getMmsi());
        positionsBST.insert(shipPosition);

        return new ShipSortMmsi(positionsBST, shipsFileDTO.getMmsi(), shipsFileDTO.getVesselName(), shipsFileDTO.getImo(), shipsFileDTO.getCallSign(),
                shipsFileDTO.getVesselType(), shipsFileDTO.getLength(), shipsFileDTO.getWidth(), shipsFileDTO.getDraft(), shipsFileDTO.getCargo());
    }

    public boolean validateShip(Ship ship){
        if(ship == null){
            return false;
        }
        return true;
    }

    public boolean saveShip(Ship ship){
        if(!validateShip(ship)){
            return false;
        }
        shipsBstMmsi.insert(shipsBstMmsi.createShip(ship.getPositionsBST(), ship.getMmsi(), ship.getVesselName(),
                ship.getImo(), ship.getCallSign(), ship.getVesselTypeID(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo()));
        shipBstImo.insert(shipBstImo.createShip(ship.getPositionsBST(), ship.getMmsi(), ship.getVesselName(),
                ship.getImo(), ship.getCallSign(), ship.getVesselTypeID(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo()));
        shipBstCallSign.insert(shipBstCallSign.createShip(ship.getPositionsBST(), ship.getMmsi(), ship.getVesselName(),
                ship.getImo(), ship.getCallSign(), ship.getVesselTypeID(), ship.getLength(), ship.getWidth(), ship.getDraft(), ship.getCargo()));
       /* System.out.println("here???");
        shipStoreDatabase.save(App.getInstance().getConnection() ,ship);*/
        return true;
    }

    public ShipTreeMmsi getShipsBstMmsi() {
        return shipsBstMmsi;
    }


    public void addPosition(int mmsiCode, ShipPosition position){
        Ship shipMmsi = shipsBstMmsi.getShipByMmsiCode(mmsiCode);
        shipMmsi.addPosition(position);
        Ship shipCallSign = getShipByAnyCode(shipMmsi.getCallSign());
        shipCallSign.addPosition(position);
        Ship shipImo = getShipByAnyCode(shipMmsi.getImo());
        shipImo.addPosition(position);
       /* shipStoreDatabase.save(App.getInstance().getConnection() ,position);*/
    }

    public Ship getShipByAnyCode(String code){
        Ship result;
        if(code.substring(0,3).equalsIgnoreCase("imo")){
            result = shipBstImo.getShip(code);
        }else if(code.length() == 9 && StringUtils.isNumeric(code)){
            result = shipsBstMmsi.getShip(code);
        }else{
            result = shipBstCallSign.getShip(code);
        }
        if(result == null){
            throw new UnsupportedOperationException("Couldn't find a ship with given code");
        }
        return result;
    }


}
