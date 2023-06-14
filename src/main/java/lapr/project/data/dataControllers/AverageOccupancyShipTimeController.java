package lapr.project.data.dataControllers;

import lapr.project.controller.App;
import lapr.project.data.PortStoreDB;
import lapr.project.data.ShipStoreDB;
import lapr.project.domain.model.Company;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

public class AverageOccupancyShipTimeController {

    /**
     * Company instance of the session.
     */
    private Company company;

    /**
     * Constructor for the controller.
     */
    public AverageOccupancyShipTimeController(){
        this(App.getInstance().getCompany());
    }

    /**
     * Constructor receiving the company as an argument.
     *
     * @param companyy instance of company to be used.
     */
    public AverageOccupancyShipTimeController(Company companyy){
        this.company=companyy;
    }

    /**
     * Get ship average occupancy rate by ship id and two dates.
     * @param shipID ship id.
     * @param month1 beginning month.
     * @param year1 beginning year.
     * @param day1 beginning day.
     * @param month2 ending month.
     * @param year2 ending year.
     * @param day2 ending day.
     * @return ship occupancy rate in percentage.
     */
    public double getAverageOccupancyRateByShipIDandDates(int shipID, int month1, int year1, int day1, int month2, int year2, int day2) throws SQLException {
        if (checkIfShipExists(shipID)==0){
            return -1.0;
        }
        if (year1>year2){
            return -1.0;
        } else if (year1==year2 && month1>month2){
            return -1.0;
        } else if (year1==year2 && month1==month2 && day1>day2){
            return -1.0;
        }
        if(month1<1 || month1>12 || month2<1 || month2>12){
            return -1.0;
        }else if (day1<1 || day1>31 || day2<1 || day2>31){
            return -1.0;
        }else if (((month1==4 || month1==6 || month1==9 || month1==11) && day1>30) || ((month2==4 || month2==6 || month2==9 || month2==11) && day2>30)){
            return -1.0;
        } else if ((month1==2 && day1>29) || (month2==2 && day2>29)){
            return -1.0;
        } else if ((!(((year1 % 4 == 0) && (year1 % 100!= 0)) || (year1 % 400 == 0)) && month1==2 && day1>28) || (!(((year2 % 4 == 0) && (year2 % 100!= 0)) || (year2 % 400 == 0)) && month2==2 && day2>28)){
            return -1.0;
        }
        Date current = new Date(Calendar.getInstance().getTime().getTime());
        Date bDate = new Date(year1-1900,month1-1,day1);
        Date eDate = new Date(year2-1900,month2-1,day2);
        if (current.before(eDate)){
            return -1.0;
        }
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        double maxCapacity = (double) shipStoreDB.getShipMaxCapacity(shipID); //get with sql
        double sumData=0.0, numData=0.0;
        int capacityTime;
        Date someDate = bDate;
        Date prevSomeDate;
        do{
            numData++;
            capacityTime = shipStoreDB.getNumContainersShipDay(shipID, someDate);
            sumData=sumData+shipStoreDB.calculateOccupancyRate(maxCapacity,capacityTime);
            someDate=new java.sql.Date(someDate.getTime() + 24*60*60*1000);
            prevSomeDate=new java.sql.Date(someDate.getTime() - 24*60*60*1000);
        } while (!prevSomeDate.toString().equals(eDate.toString()));
        double result = sumData/numData;
        return result;
    }

    /**
     * Check if a ship exists in the data base.
     * @param shipID Ship's id.
     * @return 1 if the ship exists and 0 if it doesn't.
     */
    public int checkIfShipExists(int shipID) throws SQLException {
        ShipStoreDB shipStoreDB = this.company.getShipStoreDB();
        return shipStoreDB.checkIfShipExists(shipID);
    }

}
