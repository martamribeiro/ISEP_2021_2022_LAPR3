package lapr.project.utils;

import lapr.project.dto.PositionDTO;
import lapr.project.dto.ShipsFileDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Marta Ribeiro (1201592)
 */
public class ShipsFileUtils {

    private List<String> dataLabels;

    public ShipsFileUtils (){
        dataLabels = new ArrayList<>();
    }

    public List<ShipsFileDTO> getShipsDataToDto(String filePath){
        File csvFile = new File(filePath);
        List<ShipsFileDTO> processedListData = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            String line = bufferedReader.readLine();
            if (line==null || !line.equals("MMSI,BaseDateTime,LAT,LON,SOG,COG,Heading,VesselName,IMO," +
                    "CallSign,VesselType,Length,Width,Draft,Cargo,TranscieverClass")){
                throw new IllegalArgumentException("Incompatible file format.");
            }
            dataLabels = Arrays.asList(line.split(","));
            line = bufferedReader.readLine();
            while(line != null){
                String [] attributes = line.split(",");
                processedListData.add(attributesToDto(attributes));
                line = bufferedReader.readLine();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return processedListData;
    }

    private  ShipsFileDTO attributesToDto(String[] shipData) throws ParseException {
        return new ShipsFileDTO(positionToDto(shipData),
                Integer.parseInt(shipData[dataLabels.indexOf("MMSI")]),
                shipData[dataLabels.indexOf("VesselName")],
                shipData[dataLabels.indexOf("IMO")],
                shipData[dataLabels.indexOf("CallSign")],
                Integer.parseInt(shipData[dataLabels.indexOf("VesselType")]),
                Integer.parseInt(shipData[dataLabels.indexOf("Length")]),
                Integer.parseInt(shipData[dataLabels.indexOf("Width")]),
                Double.parseDouble(shipData[dataLabels.indexOf("Draft")]),
                shipData[dataLabels.indexOf("Cargo")]);
    }

    private Date getDateOfString(String stringFormatDate){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            return sdf.parse(stringFormatDate);
        } catch(ParseException p){
            return null;
        }
    }

    private PositionDTO positionToDto(String[] data){
        Date baseDateTime = getDateOfString(data[dataLabels.indexOf("BaseDateTime")]);
        double lat = Double.parseDouble(data[dataLabels.indexOf("LAT")]);
        double lon = Double.parseDouble(data[dataLabels.indexOf("LON")]);
        double sog = Double.parseDouble(data[dataLabels.indexOf("SOG")]);
        double cog = Double.parseDouble(data[dataLabels.indexOf("COG")]);
        int heading = Integer.parseInt(data[dataLabels.indexOf("Heading")]);
        String transcieverClass = data[dataLabels.indexOf("TranscieverClass")];
        return new PositionDTO(baseDateTime, lat, lon, sog, cog, heading, transcieverClass);
    }

}
