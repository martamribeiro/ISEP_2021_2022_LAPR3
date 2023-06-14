package lapr.project.utils;

import lapr.project.dto.PortFileDTO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PortsFileUtils {

    private List<String> dataLabels;

    public PortsFileUtils (){
        dataLabels = new ArrayList<>();
    }

    public List<PortFileDTO> getPortsDataToDto(String filePath){
        File csvFile = new File(filePath);
        List<PortFileDTO> processedListData = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            String line = bufferedReader.readLine();
            if (line==null || !line.equals("continent,country,code,port,lat,lon")){
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


    private  PortFileDTO attributesToDto(String[] portData) throws ParseException {
        return new PortFileDTO(Integer.parseInt(portData[dataLabels.indexOf("code")]),
                portData[dataLabels.indexOf("port")],
                portData[dataLabels.indexOf("continent")],
                portData[dataLabels.indexOf("country")],
                Double.parseDouble(portData[dataLabels.indexOf("lat")]),
                Double.parseDouble(portData[dataLabels.indexOf("lon")]));
    }

}
