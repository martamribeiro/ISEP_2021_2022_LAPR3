--PLEASE FIRST RUN THE create_tables.sql AND bootstrap2.sql

------------------------------------------------------------REUSED------------------------------------------------------------
--GET NUM CONTAINERS PER CARGO MANIFEST
create or replace function get_num_containers_per_cargoManifest(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer
is
f_num_containers_per_cargoManifest integer;
begin
select count(*) into f_num_containers_per_cargoManifest
from containerInCargoManifest
where cargoManifest_id = f_cargoManifest_id;
return (f_num_containers_per_cargoManifest);
exception
when no_data_found then
return 0;
end;
/

--US305/US312
--get_route_id
create or replace function get_route_id(f_container_id container.container_id%type, f_registration_code client.registration_code%type) return varchar
is
    container_exists integer;
    not_leased_client integer;
    f_route_id route.route_id%type;

    ex_invalid_container_id exception;
    ex_not_leased_client exception;
begin

    select count(*) into container_exists from container where container_id=f_container_id;

    if container_exists = 0 then
        raise ex_invalid_container_id;
    end if;

    select count(*) into not_leased_client from shipment where container_id=f_container_id AND registration_code=f_registration_code;

    if not_leased_client = 0 then
        raise ex_not_leased_client;
    end if;

    select route_id into f_route_id from shipment where container_id=f_container_id AND registration_code=f_registration_code;

    return f_route_id;

exception
    when ex_invalid_container_id then
        return '10 – invalid container id';
    when ex_not_leased_client then
        return '11 – container is not leased by client';
    when others then
        return 'Invalid data';
end;
/

--get_path_function
CREATE OR REPLACE FUNCTION get_path_function(f_route_id route.route_id%type) return varchar
IS
   c_departure_location integer;
   c_arrival_location integer;
   c_est_departure_date date;
   c_est_arrival_date date;
   c_real_departure_date date;
   c_real_arrival_date date;
   c_transport integer;

   departure_location varchar(50);
   arrival_location varchar(50);
   type_of_transport varchar(20);

   isDeparturePort integer;
   isArrivalPort integer;
   isShip integer;

   route varchar(32700);

   CURSOR c_trips is
      SELECT mmsi, departure_location, arrival_location, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date FROM SHIPTRIP WHERE ROUTE_ID=f_route_id
      union all
      SELECT truck_id, departure_location, arrival_location, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date FROM TRUCKTRIP WHERE ROUTE_ID=f_route_id
      ORDER BY EST_DEPARTURE_DATE;
BEGIN
   OPEN c_trips;
   LOOP
   FETCH c_trips into c_transport, c_departure_location, c_arrival_location, c_est_departure_date, c_est_arrival_date, c_real_departure_date, c_real_arrival_date;
      EXIT WHEN c_trips%notfound;

      SELECT COUNT(*) INTO isShip FROM SHIP WHERE mmsi=c_transport;
      IF isShip = 0 THEN
        type_of_transport := 'Truck';
      ELSE
        type_of_transport := 'Ship';
      END IF;

      IF type_of_transport = 'Ship' THEN --if the type of transport is a ship then the arrival and departure location must be a Port
        SELECT name INTO departure_location FROM PORT where port_id=c_departure_location;
        departure_location := 'Port ' || departure_location;
        SELECT name INTO arrival_location FROM PORT where port_id=c_arrival_location;
        arrival_location := 'Port ' || arrival_location;
      ELSE --if the type of transport is a truck
        --there are 2 options:
        --if the departure location is a Port, then the arrival location must be a Warehouse;
        --if the departure location is a Warehouse, then arrival location can be either a Port OR Warehouse.
        SELECT COUNT(*) INTO isDeparturePort FROM PORT WHERE location_id=c_departure_location;
        IF isDeparturePort = 0 THEN --the departure location is a Warehouse
            SELECT name INTO departure_location FROM WAREHOUSE where location_id=c_departure_location;
            departure_location := 'Warehouse ' || departure_location;
            --check whether arrival location is a Warehouse or Port
            SELECT COUNT(*) INTO isArrivalPort FROM PORT WHERE location_id=c_arrival_location;
            IF isArrivalPort = 0 THEN
                SELECT name INTO arrival_location FROM WAREHOUSE where location_id=c_arrival_location;
                arrival_location := 'Warehouse ' || arrival_location;
            ELSE
                SELECT name INTO arrival_location FROM PORT where location_id=c_arrival_location;
                arrival_location := 'Port ' || arrival_location;
            END IF;

        ELSE --the departure location is a Port, therefore the arrival location must be a Warehouse
            SELECT name INTO departure_location FROM PORT where location_id=c_departure_location;
            departure_location := 'Port ' || departure_location;
            SELECT name INTO arrival_location FROM WAREHOUSE where location_id=c_arrival_location;
            arrival_location := 'Warehouse ' || arrival_location;
        END IF;

      END IF;

      IF c_real_departure_date IS NULL THEN --the container is at the location
        route := route || '  > Current Container Location: ' || departure_location || ' < ';
        return route;
      ELSE --the container is NOT in the location
        route := route || 'Departure Location: ' || departure_location || ', Departure Date: ' || c_real_departure_date;
        IF c_real_arrival_date IS NOT NULL THEN
            route := route || ', Arrival Location: ' || arrival_location || ', Arrival Date: ' || c_real_arrival_date;
        ELSE --the transport hasn't arrived to the location
            route := route || chr(10) || '  > The ' || type_of_transport || ' is between ' || departure_location || ' and ' || arrival_location || ' < ';
            return route;
        END IF;
      END IF;
      route := route || ', Transport: ' || type_of_transport || chr(10);
   END LOOP;
   CLOSE c_trips;
   --the container has arrived its final destination
   route := route || '  > Container has arrived at its final destination at ' || arrival_location || ' < ';
   return route;

   exception
    when no_data_found then
        return ('There is no route id defined for that container and registration code. Please verify if you inserted your data correctly.');
END;
/

--US306

--GET NUM CONTAINERS OUT WAREHOUSE NEXT 30 DAYS
create or replace function get_num_containers_out_warehouse(f_warehouse_id warehouse.warehouse_id%type,f_currentDate truckTrip.est_departure_date%type, f_finalDate truckTrip.est_departure_date%type) return integer
is
f_comp_manifest cargoManifest.cargoManifest_id%type;
f_num_containers_out_warehouse integer:=0;
cursor desiredManifests
is
(select loading_cargo_id from trucktrip where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id)
AND est_departure_date > f_currentDate and est_departure_date <= f_finalDate);
begin
open desiredManifests;
loop
fetch desiredManifests into f_comp_manifest;
exit when desiredManifests%notfound;
f_num_containers_out_warehouse:=f_num_containers_out_warehouse+get_num_containers_per_cargoManifest(f_comp_manifest);
end loop;
return (f_num_containers_out_warehouse);
exception
when no_data_found then
return 0;
end;
/

--CHECK IF WAREHOUSE EXISTS
create or replace function check_if_warehouse_exists(f_warehouse_id warehouse.warehouse_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from warehouse
where warehouse_id = f_warehouse_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET CURRENT CONTAINERS WAREHOUSE
create or replace function get_current_containers_warehouse(f_warehouse_id warehouse.warehouse_id%type, f_current_date shipTrip.est_departure_date%type) return integer
is
f_comp_cargo_id cargoManifest.cargoManifest_id%type;
f_current_containers integer:=0;
cursor neededCargosPut
is
(select unloading_cargo_id
from truckTrip
where arrival_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_arrival_date < f_current_date);
cursor neededCargosTake
is
(select loading_cargo_id
from truckTrip
where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_departure_date < f_current_date);
begin
open neededCargosPut;
loop
fetch neededCargosPut into f_comp_cargo_id;
exit when neededCargosPut%notfound;
f_current_containers:= f_current_containers + get_num_containers_per_cargoManifest(f_comp_cargo_id);
end loop;
open neededCargosTake;
loop
fetch neededCargosTake into f_comp_cargo_id;
exit when neededCargosTake%notfound;
f_current_containers:= f_current_containers - get_num_containers_per_cargoManifest(f_comp_cargo_id);
end loop;
return f_current_containers;
exception
when no_data_found then
return 0;
end;
/

--GET WAREHOUSE MAX CAPACITY
create or replace function get_warehouse_max_capacity(f_warehouse_id warehouse.warehouse_id%type) return integer
is
f_max_capacity integer;
begin
select maxCapacity into f_max_capacity
from warehouse
where warehouse_id = f_warehouse_id;
return (f_max_capacity);
exception
when no_data_found then
return 0;
end;
/


--US307

--TRIGGER FOR NUMBER OF CONTAINERS UNLOADING MANIFEST BECOMES GREATER THAN MAX NUMBER OF CONTAINERS WAREHOUSE
create or replace trigger trgContainersWarehouse
before insert or update on TruckTrip
                            for each row
declare
f_trucktrip_id trucktrip.trucktrip_id%type;
f_cargoManifest_id cargomanifest.cargomanifest_id%type;
f_arrival_location truckTrip.arrival_location%type;
f_warehouse_id warehouse.warehouse_id%type;
f_estArrDate truckTrip.est_arrival_date%type;
f_containers_before integer;
f_containers_max integer;
f_containers_after integer;
begin
f_trucktrip_id:= :new.trucktrip_id;
f_cargoManifest_id:= :new.unloading_cargo_id;
f_arrival_location:= :new.arrival_location;
select warehouse_id into f_warehouse_id from warehouse where location_id=f_arrival_location;
f_estArrDate:= :new.est_arrival_date;
f_containers_before:=get_current_containers_warehouse(f_warehouse_id,f_estArrDate);
select maxCapacity into f_containers_max from Warehouse where warehouse_id=f_warehouse_id;
f_containers_after:=f_containers_before+get_num_containers_per_cargomanifest(f_cargomanifest_id);
if f_containers_after>f_containers_max then
raise_application_error(-20030,'Currently, the destiny warehouse doesnt have enough capacity for the containers in the unloading cargo manifest.');
end if;
end;

/

--CREATE TRUCK TRIP WITH UNLOADING CARGO MANIFEST
create or replace function create_truckTrip_with_unloading
(f_trucktrip_id trucktrip.trucktrip_id%type, f_route_id route.route_id%type, f_truck_id trucktrip.truck_id%type, f_departure_location trucktrip.departure_location%type,
f_arrival_location trucktrip.arrival_location%type, f_loading_cargo_id trucktrip.loading_cargo_id%type, f_unloading_cargo_id trucktrip.unloading_cargo_id%type,
f_est_departure_date trucktrip.est_departure_date%type, f_est_arrival_date trucktrip.est_arrival_date%type) return integer
is
f_check integer;
f_check2 integer;
f_check3 integer;
begin
f_check:=check_if_truck_exists(f_truck_id);
if f_check=0 then
return -1;
end if;
f_check2:=check_if_cargoManifest_exists(f_loading_cargo_id);
if f_check2=0 then
return -1;
end if;
f_check3:=check_if_cargoManifest_exists(f_unloading_cargo_id);
if f_check3=0 then
return -1;
end if;
insert into trucktrip (trucktrip_id, route_id, truck_id, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (f_trucktrip_id, f_route_id, f_truck_id, f_departure_location, f_arrival_location, f_loading_cargo_id, f_unloading_cargo_id, f_est_departure_date, f_est_arrival_date, NULL, NULL);
return 1;
exception
when no_data_found then
return -1;
end;
/

--DELETE TRUCK TRIP - IMPORTADO
create or replace function delete_truckTrip
(f_trucktrip_id trucktrip.trucktrip_id%type) return integer
is
begin
delete
from truckTrip
where
trucktrip_id = f_trucktrip_id;
return 1;
exception
when no_data_found then
return -1;
end;
/

--CHECK IF TRUCK TRIP EXISTS - IMPORTADO
create or replace function check_if_truckTrip_exists(f_truckTrip_id truckTrip.truckTrip_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from truckTrip
where truckTrip_id = f_truckTrip_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--CHECK IF TRUCK EXISTS
create or replace function check_if_truck_exists(f_truck_id truck.truck_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from truck
where truck_id = f_truck_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

---------------------------------------------------------------------------REUSES:---------------------------------------------------------------------------

--CHECK IF CARGO MANIFEST EXISTS
create or replace function check_if_cargoManifest_exists(f_cargoManifest_id cargomanifest.cargomanifest_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from cargomanifest
where cargomanifest_id = f_cargomanifest_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--CHECK IF WAREHOUSE EXISTS
create or replace function check_if_warehouse_exists(f_warehouse_id warehouse.warehouse_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from warehouse
where warehouse_id = f_warehouse_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET CURRENT CONTAINERS WAREHOUSE
create or replace function get_current_containers_warehouse(f_warehouse_id warehouse.warehouse_id%type, f_current_date shipTrip.est_departure_date%type) return integer
is
f_comp_cargo_id cargoManifest.cargoManifest_id%type;
f_current_containers integer:=0;
cursor neededCargosPut
is
(select unloading_cargo_id
from truckTrip
where arrival_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_arrival_date < f_current_date);
cursor neededCargosTake
is
(select loading_cargo_id
from truckTrip
where departure_location=(select location_id from warehouse where warehouse_id=f_warehouse_id) AND est_departure_date < f_current_date);
begin
open neededCargosPut;
loop
fetch neededCargosPut into f_comp_cargo_id;
exit when neededCargosPut%notfound;
f_current_containers:= f_current_containers + get_num_containers_per_cargoManifest(f_comp_cargo_id);
end loop;
open neededCargosTake;
loop
fetch neededCargosTake into f_comp_cargo_id;
exit when neededCargosTake%notfound;
f_current_containers:= f_current_containers - get_num_containers_per_cargoManifest(f_comp_cargo_id);
end loop;
return f_current_containers;
exception
when no_data_found then
return 0;
end;
/

--GET WAREHOUSE MAX CAPACITY
create or replace function get_warehouse_max_capacity(f_warehouse_id warehouse.warehouse_id%type) return integer
is
f_max_capacity integer;
begin
select maxCapacity into f_max_capacity
from warehouse
where warehouse_id = f_warehouse_id;
return (f_max_capacity);
exception
when no_data_found then
return 0;
end;
/

--GET NUM CONTAINERS PER CARGO MANIFEST
create or replace function get_num_containers_per_cargoManifest(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer
is
f_num_containers_per_cargoManifest integer;
begin
select count(*) into f_num_containers_per_cargoManifest
from containerInCargoManifest
where cargoManifest_id = f_cargoManifest_id;
return (f_num_containers_per_cargoManifest);
exception
when no_data_found then
return 0;
end;
/

--US308

--TRIGGER FOR NUMBER OF CONTAINERS GREATER THAN MAX NUMBER OF CONTAINERS
create or replace trigger trgContainers
before insert or update on ShipTrip
                            for each row
declare
f_shiptrip_id shiptrip.shiptrip_id%type;
f_cargoManifest_id cargomanifest.cargomanifest_id%type;
f_mmsi shipTrip.mmsi%type;
f_estDepDate shipTrip.est_departure_date%type;
f_containers_before integer;
f_containers_max integer;
f_containers_after integer;
begin
f_shiptrip_id:= :new.shiptrip_id;
f_cargoManifest_id:= :new.loading_cargo_id;
f_mmsi:= :new.mmsi;
f_estDepDate:= :new.est_departure_date;
select currentCapacity into f_containers_max from Ship where mmsi=f_mmsi;
f_containers_before:=get_initial_num_containers_per_ship_trip(f_cargoManifest_id,f_estDepDate,f_mmsi);
f_containers_after:=f_containers_before+get_num_containers_per_cargomanifest(f_cargomanifest_id);
if f_containers_after>f_containers_max then
raise_application_error(-20001,'Currently, the ship doesnt have enough capacity for the cargo manifest.');
end if;
end;
/

--CHECK IF SHIP TRIP EXISTS
create or replace function check_if_shipTrip_exists(f_shipTrip_id shipTrip.shipTrip_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from shipTrip
where shipTrip_id = f_shipTrip_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--DELETE SHIP TRIP
create or replace function delete_shipTrip
(f_shiptrip_id shiptrip.shiptrip_id%type) return integer
is
begin
delete
from shipTrip
where
        shiptrip_id = f_shiptrip_id;
return 1;
exception
when no_data_found then
return -1;
end;
/

--CREATE SHIP TRIP
create or replace function create_shipTrip
(f_shiptrip_id shiptrip.shiptrip_id%type, f_route_id route.route_id%type, f_mmsi shiptrip.mmsi%type, f_departure_location shiptrip.departure_location%type,
f_arrival_location shiptrip.arrival_location%type, f_loading_cargo_id shiptrip.loading_cargo_id%type,
f_est_departure_date shiptrip.est_departure_date%type, f_est_arrival_date shiptrip.est_arrival_date%type) return integer
is
f_check integer;
f_check2 integer;
begin
f_check:=check_if_ship_exists(f_mmsi);
if f_check=0 then
return -1;
end if;
f_check2:=check_if_cargoManifest_exists(f_loading_cargo_id);
if f_check2=0 then
return -1;
end if;
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date) values (f_shiptrip_id, f_route_id, f_mmsi, f_departure_location, f_arrival_location, f_loading_cargo_id, NULL, f_est_departure_date, f_est_arrival_date, NULL, NULL);
return 1;
exception
when no_data_found then
return -1;
end;
/

--GET REAL DEPARTURE DATE FROM SHIP TRIP
create or replace function get_real_departure_date_from_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return shipTrip.real_departure_date%type
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_real_departure_date shipTrip.real_departure_date%type;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;
select real_departure_date into f_real_departure_date
from shipTrip
where shiptrip_id = f_shiptrip_id;
return f_real_departure_date;
end;
/

--GET INITIAL NUM CONTAINERS PER SHIP TRIP WITH REAL DATES
create or replace function get_initial_num_containers_per_ship_trip_2(f_cargoManifest_id cargoManifest.cargoManifest_id%type,
f_real_departure_date shipTrip.real_departure_date%type, f_mmsi ship.mmsi%type) return integer
is
f_comp_loading_cargo_id cargoManifest.cargoManifest_id%type;
f_comp_unloading_cargo_id cargoManifest.cargoManifest_id%type;
f_initial_num_containers_per_ship_trip integer:=0;
cursor neededShipTrips
is
(select loading_cargo_id, unloading_cargo_id
from shipTrip
where mmsi=f_mmsi AND real_departure_date < f_real_departure_date);
begin
open neededShipTrips;
loop
fetch neededShipTrips into f_comp_loading_cargo_id, f_comp_unloading_cargo_id;
exit when neededShipTrips%notfound;
f_initial_num_containers_per_ship_trip := f_initial_num_containers_per_ship_trip + get_num_containers_per_cargoManifest(f_comp_loading_cargo_id) - get_num_containers_per_cargoManifest(f_comp_unloading_cargo_id);
end loop;
return f_initial_num_containers_per_ship_trip;
exception
when no_data_found then
return 0;
end;
/

---------------------------------------------------------------------------REUSING US209:---------------------------------------------------------------------------

--CHECK IF CARGO MANIFEST EXISTS
create or replace function check_if_cargoManifest_exists(f_cargoManifest_id cargomanifest.cargomanifest_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from cargomanifest
where cargomanifest_id = f_cargomanifest_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET CARGO MANIFEST BY MMSI AND DATE
create or replace function get_cargo_manifest_by_mmsi_and_date(f_mmsi shipTrip.mmsi%type, f_date shipTrip.est_departure_date%type) return cargoManifest.cargoManifest_id%type
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_unloading_cargo_id cargoManifest.cargoManifest_id%type;
f_est_departure_date shipTrip.est_departure_date%type;
f_est_arrival_date shipTrip.est_arrival_date%type;
f_cargoManifest_id cargoManifest.cargoManifest_id%type;
begin
select shiptrip_id, unloading_cargo_id, est_departure_date, est_arrival_date, loading_cargo_id
into f_shiptrip_id, f_unloading_cargo_id, f_est_departure_date, f_est_arrival_date, f_cargoManifest_id
from
    (select * from
        (select * from shipTrip
         where mmsi=f_mmsi AND est_departure_date<=f_date)
     order by est_departure_date desc)
where rownum=1;
if f_est_arrival_date <= f_date then
select unloading_cargo_id into f_cargoManifest_id
from shipTrip
where shiptrip_id=f_shiptrip_id;
end if;
return (f_cargoManifest_id);
exception
when no_data_found then
return -1;
end;
/

---------------------------------------------------------------------------REUSING US208:---------------------------------------------------------------------------

--CHECK IF SHIP EXISTS
create or replace function check_if_ship_exists(f_mmsi ship.mmsi%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from ship
where mmsi = f_mmsi;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--GET ADDED AND REMOVED CONTAINERS IN A SHIP TRIP FOR A MOMENT
create or replace function get_added_removed_containers_ship_trip_moment(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer --est date como parametro
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_loading_cargo_id shipTrip.loading_cargo_id%type;
f_unloading_cargo_id shipTrip.unloading_cargo_id%type;
f_num_added_removed_containers_ship_trip_moment integer:=0;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id=f_cargoManifest_id or unloading_cargo_id=f_cargoManifest_id;
select loading_cargo_id, unloading_cargo_id into f_loading_cargo_id, f_unloading_cargo_id
from shipTrip
where shiptrip_id = f_shiptrip_id;
f_num_added_removed_containers_ship_trip_moment := f_num_added_removed_containers_ship_trip_moment + get_num_containers_per_cargoManifest(f_loading_cargo_id);
if f_unloading_cargo_id=f_cargoManifest_id then
f_num_added_removed_containers_ship_trip_moment := f_num_added_removed_containers_ship_trip_moment + get_num_containers_per_cargoManifest(f_unloading_cargo_id);
end if;
return f_num_added_removed_containers_ship_trip_moment;
exception
when no_data_found then
return 0;
end;
/

--GET EST DEPARTURE DATE FROM SHIP TRIP
create or replace function get_est_departure_date_from_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return shipTrip.est_departure_date%type
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_est_departure_date shipTrip.est_departure_date%type;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;
select est_departure_date into f_est_departure_date
from shipTrip
where shiptrip_id = f_shiptrip_id;
return f_est_departure_date;
end;
/

--GET INITIAL NUM CONTAINERS PER SHIP TRIP
create or replace function get_initial_num_containers_per_ship_trip(f_cargoManifest_id cargoManifest.cargoManifest_id%type,
f_est_departure_date shipTrip.est_departure_date%type, f_mmsi ship.mmsi%type) return integer
is
f_comp_loading_cargo_id cargoManifest.cargoManifest_id%type;
f_comp_unloading_cargo_id cargoManifest.cargoManifest_id%type;
f_initial_num_containers_per_ship_trip integer:=0;
cursor neededShipTrips
is
(select loading_cargo_id, unloading_cargo_id
from shipTrip
where mmsi=f_mmsi AND est_departure_date < f_est_departure_date);
begin
open neededShipTrips;
loop
fetch neededShipTrips into f_comp_loading_cargo_id, f_comp_unloading_cargo_id;
exit when neededShipTrips%notfound;
f_initial_num_containers_per_ship_trip := f_initial_num_containers_per_ship_trip + get_num_containers_per_cargoManifest(f_comp_loading_cargo_id) - get_num_containers_per_cargoManifest(f_comp_unloading_cargo_id);
end loop;
return f_initial_num_containers_per_ship_trip;
exception
when no_data_found then
return 0;
end;
/

--GET SHIP MAX CAPACITY
create or replace function get_max_capacity(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer
is
f_mmsi shipTrip.mmsi%type;
f_max_capacity integer;
begin
select mmsi into f_mmsi
from shipTrip
where loading_cargo_id = f_cargoManifest_id OR unloading_cargo_id = f_cargoManifest_id;
select to_number(currentCapacity) into f_max_capacity
from ship
where mmsi = f_mmsi;
return (f_max_capacity);
exception
when no_data_found then
return 0;
end;
/

--GET MMSI BY CARGO MANIFEST ID
create or replace function get_mmsi_by_cargo_manifest_id(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer --est date como parametro
is
f_shiptrip_id shipTrip.shiptrip_id%type;
f_mmsi ship.mmsi%type;
begin
select shiptrip_id into f_shiptrip_id
from shipTrip
where loading_cargo_id=f_cargoManifest_id or unloading_cargo_id=f_cargoManifest_id;
select mmsi into f_mmsi
from shipTrip
where shiptrip_id = f_shiptrip_id;
return f_mmsi;
exception
when no_data_found then
return -1;
end;
/

--GET NUM CONTAINERS PER CARGO MANIFEST
create or replace function get_num_containers_per_cargoManifest(f_cargoManifest_id cargoManifest.cargoManifest_id%type) return integer
is
f_num_containers_per_cargoManifest integer;
begin
select count(*) into f_num_containers_per_cargoManifest
from containerInCargoManifest
where cargoManifest_id = f_cargoManifest_id;
return (f_num_containers_per_cargoManifest);
exception
when no_data_found then
return 0;
end;
/

--US309
--reused from us 210
create or replace PROCEDURE check_availability_of_ship(daydate in date, ship in Ship.mmsi%type, isValid out boolean)
IS
    estDepartDate date;
    estArrivalDate date;

Cursor tripsOfShip IS
        select est_departure_date, est_arrival_date
        from shiptrip
        where mmsi=ship;

BEGIN
open tripsOfShip;
    LOOP
        fetch tripsOfShip INTO estDepartDate, estArrivalDate;
        Exit When tripsOfShip%notfound;
        --if the arriavl date is before the monday is valid
        if daydate > estArrivalDate then
            isValid := true;
        -- if the date was not before monday then if the depart date is AFTER monday ship is available
        elsif daydate < estDepartDate then
            isValid := true;
        -- else false
        else
            isValid := false;
            exit;
        end if;
    END LOOP;
END;
/
create or replace trigger trgShipAvailability
before insert or update on ShipTrip
                            for each row
declare
f_mmsi shipTrip.mmsi%type;
f_estDepDate shipTrip.est_departure_date%type;
f_isValid boolean;

begin
f_mmsi:= :new.mmsi;
f_estDepDate:= :new.est_departure_date;
f_isValid := false;
check_availability_of_ship(f_estDepDate, f_mmsi, f_isValid);
if not f_isValid then
raise_application_error(-20010,'Currently, the ship is not available in this date.');
end if;
end;

-- test
/
--should run correctly
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date)
values (101001, 1020, 228339600, 13390, 20512, 34491, NULL, to_date('18/02/2021', 'dd/MM/YYYY'), to_date('21/03/2021', 'dd/MM/YYYY'), NULL, NULL);

-- should trigger the warning
insert into shiptrip (shiptrip_id, route_id, mmsi, departure_location, arrival_location, loading_cargo_id, unloading_cargo_id, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date)
values (101011, 1020, 228339600, 13390, 20512, 34491, NULL, to_date('18/02/2021', 'dd/MM/YYYY'), to_date('21/03/2021', 'dd/MM/YYYY'), NULL, NULL);

/


--US310

--CHECK PORT OCCUPANCY FOR A GIVEN DAY

create or replace function get_current_num_containers_port(f_port_id port.port_id%type,
f_date shipTrip.est_departure_date%type) return integer
is
f_current_num integer:=0;
f_comp_cargo_id cargoManifest.cargoManifest_id%type;
cursor cursorShipTripLoading
is
(select loading_cargo_id
from shipTrip
where departure_location=f_port_id AND real_departure_date < f_date);
cursor cursorShipTripUnloading
is
(select unloading_cargo_id
from shipTrip
where arrival_location=f_port_id AND real_arrival_date < f_date);
cursor cursorTruckTripLoading
is
(select loading_cargo_id
from truckTrip
where departure_location=(select location_id from port where port_id=f_port_id) AND real_departure_date < f_date);
cursor cursorTruckTripUnloading
is
(select unloading_cargo_id
from truckTrip
where arrival_location=(select location_id from port where port_id=f_port_id) AND real_arrival_date < f_date);
begin
open cursorShipTripUnloading;
loop
fetch cursorShipTripUnloading into f_comp_cargo_id;
exit when cursorShipTripUnloading%notfound;
f_current_num:=f_current_num+get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
open cursorTruckTripUnloading;
loop
fetch cursorTruckTripUnloading into f_comp_cargo_id;
exit when cursorTruckTripUnloading%notfound;
f_current_num:=f_current_num+get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
open cursorShipTripLoading;
loop
fetch cursorShipTripLoading into f_comp_cargo_id;
exit when cursorShipTripLoading%notfound;
f_current_num:=f_current_num-get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
open cursorTruckTripLoading;
loop
fetch cursorTruckTripLoading into f_comp_cargo_id;
exit when cursorTruckTripLoading%notfound;
f_current_num:=f_current_num-get_num_containers_per_cargomanifest(f_comp_cargo_id);
end loop;
return f_current_num;
exception
when no_data_found then
return 0;
end;
/

--GET PORT MAX CAPACITY

create or replace function get_port_max_capacity(f_port_id port.port_id%type) return integer
is
f_max_capacity integer;
begin
select maxCapacity into f_max_capacity
from port
where port_id = f_port_id;
return (f_max_capacity);
exception
when no_data_found then
return 0;
end;
/


--CHECK IF PORT EXISTS

create or replace function check_if_port_exists(f_port_id port.port_id%type) return integer
is
f_result integer;
begin
select count(*) into f_result
from port
where port_id = f_port_id;
return (f_result);
exception
when no_data_found then
return 0;
end;
/

--US312

CREATE OR REPLACE FUNCTION get_location(f_route_id route.route_id%type) return varchar
IS
   c_departure_location integer;
   c_arrival_location integer;
   c_est_departure_date date;
   c_est_arrival_date date;
   c_real_departure_date date;
   c_real_arrival_date date;
   c_transport integer;

   departure_location varchar(50);
   arrival_location varchar(50);
   type_of_transport varchar(20);
   transport varchar(50);

   isDeparturePort integer;
   isArrivalPort integer;
   isShip integer;

   transport_name varchar(50);
   place varchar(100);

   CURSOR c_trips is
      SELECT mmsi, departure_location, arrival_location, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date FROM SHIPTRIP WHERE ROUTE_ID=f_route_id
      union all
      SELECT truck_id, departure_location, arrival_location, est_departure_date, est_arrival_date, real_departure_date, real_arrival_date FROM TRUCKTRIP WHERE ROUTE_ID=f_route_id
      ORDER BY EST_DEPARTURE_DATE;
BEGIN
OPEN c_trips;
   LOOP
   FETCH c_trips into c_transport, c_departure_location, c_arrival_location, c_est_departure_date, c_est_arrival_date, c_real_departure_date, c_real_arrival_date;
      EXIT WHEN c_trips%notfound;

      SELECT COUNT(*) INTO isShip FROM SHIP WHERE mmsi=c_transport;
      IF isShip = 0 THEN
        select name into transport_name from truck where truck_id=c_transport;
        type_of_transport := 'TRUCK';
        transport := type_of_transport || ', ' || transport_name;
      ELSE
        select shipName into transport_name from ship where mmsi=c_transport;
        type_of_transport := 'SHIP';
        transport := type_of_transport || ', ' || transport_name;
      END IF;

      IF type_of_transport = 'SHIP' THEN --if the type of transport is a ship then the arrival and departure location must be a Port
        SELECT name INTO departure_location FROM PORT where port_id=c_departure_location;
        departure_location := 'PORT, ' || departure_location;
        SELECT name INTO arrival_location FROM PORT where port_id=c_arrival_location;
        arrival_location := 'PORT, ' || arrival_location;
      ELSE --if the type of transport is a truck
        --there are 2 options:
        --if the departure location is a Port, then the arrival location must be a Warehouse;
        --if the departure location is a Warehouse, then arrival location can be either a Port OR Warehouse.
        SELECT COUNT(*) INTO isDeparturePort FROM PORT WHERE location_id=c_departure_location;
        IF isDeparturePort = 0 THEN --the departure location is a Warehouse
            SELECT name INTO departure_location FROM WAREHOUSE where location_id=c_departure_location;
            departure_location := 'WAREHOUSE, ' || departure_location;
            --check whether arrival location is a Warehouse or Port
            SELECT COUNT(*) INTO isArrivalPort FROM PORT WHERE location_id=c_arrival_location;
            IF isArrivalPort = 0 THEN
                SELECT name INTO arrival_location FROM WAREHOUSE where location_id=c_arrival_location;
                arrival_location := 'WAREHOUSE, ' || arrival_location;
            ELSE
                SELECT name INTO arrival_location FROM PORT where location_id=c_arrival_location;
                arrival_location := 'PORT, ' || arrival_location;
            END IF;

        ELSE --the departure location is a Port, therefore the arrival location must be a Warehouse
            SELECT name INTO departure_location FROM PORT where location_id=c_departure_location;
            departure_location := 'PORT, ' || departure_location;
            SELECT name INTO arrival_location FROM WAREHOUSE where location_id=c_arrival_location;
            arrival_location := 'WAREHOUSE, ' || arrival_location;
        END IF;

      END IF;

      IF c_real_departure_date IS NULL THEN --the container is at the location
        place := departure_location;
        return place;
      ELSE --the container is NOT in the location

        IF c_real_arrival_date IS NULL THEN --the transport hasn't arrived to the location
            place := transport;
            return place;
        END IF;
      END IF;

   END LOOP;
   CLOSE c_trips;
   --the container has arrived its final destination
   place := arrival_location;
   return place;

   exception
    when no_data_found then
        return ('There is no route id defined for that container and registration code. Please verify if you inserted your data correctly.');
END;
/


--US304

create or replace trigger TRG_AUDIT_CONTAINER_IN_MANIFEST
after insert or update or delete on CONTAINERINCARGOMANIFEST
for each row
declare
    l_operation varchar(6) :=
        case when updating then 'UPDATE'
            when deleting then 'DELETE'
            else 'INSERT' end;
    begin
        if updating or inserting then
            insert into AuditContainerInManifest
                (aud_who,
                aud_when,
                aud_operation,
                container_id,
                cargoManifest_id,
                temperature_kept
                )
                values(
                    user,
                    sysdate,
                    l_operation,
                    :new.container_id,
                    :new.cargoManifest_id,
                    :new.temperature_kept
                );
            else
                insert into AuditContainerInManifest
                (aud_who,
                aud_when,
                aud_operation,
                container_id,
                cargoManifest_id,
                temperature_kept
                ) values(
                    user,
                    sysdate,
                    l_operation,
                    :old.container_id,
                    :old.cargoManifest_id,
                    :old.temperature_kept
                    );
            end if;
        end;

     -- test select after update
/
    --US304
    update containerincargomanifest set temperature_kept = 2 where container_id=8950208 and CARGOMANIFEST_ID=11031;
    select * from auditContainerInManifest where container_id=8950208 and CARGOMANIFEST_ID=11031;

/

--TESTS -> get_route_id

--the container id is invalid, therefore raises ex_invalid_container_id exception
SET SERVEROUTPUT ON;
begin
    dbms_output.put_line('The route id is: '||get_route_id(12,2));
end;
/
--the container id is valid, but it was not leased by client, therefore raises ex_not_leased_client exception
begin
    dbms_output.put_line('The route id is: '||get_route_id(9803333,2));
end;
/
--the container id is valid AND leased by the client, therefore it doesn't raise exception and returns route id
begin
    dbms_output.put_line('The route id is: '||get_route_id(9803333,6));
end;
/
--TESTS -> get_path_function

--FIRST SITUATION: the container is at the a location
begin
    dbms_output.put_line(get_path_function(7));
end;
/
--SECOND SITUATION: the container is in the middle of the ocean
begin
    dbms_output.put_line(get_path_function(8));
end;
/
--THIRD SITUATION: the container is in a truck
begin
    dbms_output.put_line(get_path_function(9));
end;
/
--FOURTH SITUATION: the container has arrived its destination
begin
    dbms_output.put_line(get_path_function(10));
end;
/
--FIRST SITUATION: the container is at the a location
begin
    dbms_output.put_line(get_location(7));
end;
/
--SECOND SITUATION: the container is in the middle of the ocean (ship: VARAMO)
begin
    dbms_output.put_line(get_location(8));
end;
/
--THIRD SITUATION: the container is in a truck (truck: blaze)
begin
    dbms_output.put_line(get_location(9));
end;
/
--FOURTH SITUATION: the container has arrived its destination
begin
    dbms_output.put_line(get_location(10));
end;
/

--For User Stories 306, 307, 308 and 310, the created functions were called in the Controller Classes. By testing the controllers, the PL SQL functionalities were tested.
