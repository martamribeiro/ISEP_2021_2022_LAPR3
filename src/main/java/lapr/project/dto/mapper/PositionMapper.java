package lapr.project.dto.mapper;

import lapr.project.domain.model.ShipPosition;
import lapr.project.dto.PositionDTO;

public class PositionMapper {

    public ShipPosition toDomain(PositionDTO positionDTO, int mmsi){
        return new ShipPosition(mmsi, positionDTO.getBaseDateTime(), positionDTO.getLatDto(), positionDTO.getLonDto(), positionDTO.getSogDto(), positionDTO.getCogDto(),
                positionDTO.getHeadingDto(), positionDTO.getTranscieverClassDto());
    }
}
