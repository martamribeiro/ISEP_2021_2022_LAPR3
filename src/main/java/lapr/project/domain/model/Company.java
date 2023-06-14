package lapr.project.domain.model;

import auth.AuthFacade;
import lapr.project.data.*;
import lapr.project.domain.dataStructures.FreightNetwork;
import lapr.project.domain.store.ContainerStore;
import lapr.project.domain.store.CountryStore;
import lapr.project.domain.store.PortStore;
import lapr.project.domain.store.ShipStore;
import org.apache.commons.lang3.StringUtils;

/**
 * Class to instantiate a new ShipPosition
 *
 * @author Group 54
 */
public class Company {

    /**
     * The company designation.
     */
    private final String designation;

    /**
     * The company's authfacade.
     */
    private final AuthFacade authFacade;

    /**
     * The Warehouse Store Date Base.
     */
    private final WarehouseStoreDB warehouseStoreDB;

    /**
     * The Ship Store.
     */
    private final ShipStore shipsStore;
    private final ShipStoreDB shipStoreDB;
    /**
     * The Port Store.
     */
    private final PortStore portStore;

    /**
     * The Ship Trip Store Data Base.
     */
    private final ShipTripStoreDB shipTripStoreDB;
    /**
     * The Route Store Data Base.
     */
    private final RouteStoreDB routeStoreDB;

    /**
     * The Truck Trip Store Data Base.
     */
    private final TruckTripStoreDB truckTripStoreDB;

    /**
     * The Port Store Data Base.
     */
    private final PortStoreDB portStoreDB;

    /**
     * The Cargo Manifest Store Data Base.
     */
    private final CargoManifestStoreDB cargoManifestStoreDB;

    private final CountryStore countryStore;

    private final ContainerStore containerStore;

    public final FreightNetwork freightNetwork;
    /**
     * Constructs an instance of Company receiving as a parameter the Company's designation
     * @param designation the Company's designation
     */
    public Company(String designation){
        if (StringUtils.isBlank(designation))
            throw new IllegalArgumentException("Designation cannot be blank.");
        this.authFacade=new AuthFacade();
        this.designation=designation;
        this.shipsStore = new ShipStore();
        this.portStore = new PortStore();
        this.shipTripStoreDB = new ShipTripStoreDB();
        this.truckTripStoreDB = new TruckTripStoreDB();
        this.shipStoreDB = new ShipStoreDB();
        this.cargoManifestStoreDB=new CargoManifestStoreDB();
        this.warehouseStoreDB=new WarehouseStoreDB();
        this.routeStoreDB = new RouteStoreDB();
        this.portStoreDB = new PortStoreDB();
        this.countryStore = new CountryStore();
        this.freightNetwork = new FreightNetwork();
        this.containerStore = new ContainerStore();
    }

    /**
     * Returns the Warehouse Store Data Base.
     *
     * @return Warehouse Store Data Base.
     */
    public WarehouseStoreDB getWarehouseStoreDB(){
        return warehouseStoreDB;
    }

    /**
     * Returns the Ships' binary search tree.
     *
     * @return the Ships' binary search tree.
     */
    public ShipStore getShipStore() {
        return shipsStore;
    }

    /**
     * Returns the Port Store.
     *
     * @return the Port Store.
     */
    public PortStore getPortStore() {
        return portStore;
    }

    /**
     * Returns the Ship Trip Store DataBase.
     *
     * @return the Ship Trip Store DataBase.
     */
    public ShipTripStoreDB getShipTripStoreDB() {
        return shipTripStoreDB;
    }
    /**
     * Returns the Route Store DataBase.
     *
     * @return the Route Store DataBase.
     */
    public RouteStoreDB getRouteStoreDB() {
        return routeStoreDB;
    }

    /**
     * Returns the Truck Trip Store DataBase.
     *
     * @return the Truck Trip Store DataBase.
     */
    public TruckTripStoreDB getTruckTripStoreDB(){
        return truckTripStoreDB;
    }

    /**
     * Returns the Port Store DataBase.
     *
     * @return the Port Store DataBase.
     */
    public PortStoreDB getPortStoreDB(){
        return portStoreDB;
    }

    public ShipStoreDB getShipStoreDB() {
        return shipStoreDB;
    }

    /**
     * Returns the Cargo Manifest Store DataBase.
     *
     * @return the Cargo Manifest Store DataBase.
     */
    public CargoManifestStoreDB getCargoManifestStoreDB(){
        return cargoManifestStoreDB;
    }

    /**
     * Returns the Company's Authfacade.
     * @return the Company's Authfacade.
     */
    public AuthFacade getAuthFacade() {
        return authFacade;
    }

    public ContainerStore getContainerStore() {return containerStore;}

    public CountryStore getCountryStore(){return this.countryStore;}

    public FreightNetwork getFreightNetwork(){return this.freightNetwork;}


}